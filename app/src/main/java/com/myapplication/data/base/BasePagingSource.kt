package com.myapplication.data.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myapplication.domain.base.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

const val DEFAULT_PAGE_SIZE = 10

private const val INITIAL_PAGE_NUMBER = 1

private typealias PageRequest<E> = suspend (pageNumber: Int) -> State<E>

class BasePagingSource<T : Any, E>(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val pageRequest: PageRequest<E>,
    private val responsePageGetter: (E) -> List<T>?,
) : PagingSource<Int, T>() {

    val pagingDataflow = Pager(PagingConfig(pageSize, enablePlaceholders = false)) {
        BasePagingSource(pageSize, pageRequest, responsePageGetter).also {
            it.mutableInitialLoadState = mutableInitialLoadState
            it.mutablePages = mutablePages
        }
    }.flow

    private var mutableInitialLoadState = MutableStateFlow<State<Unit>>(State.initial())

    val initialLoadState: StateFlow<State<Unit>> get() = mutableInitialLoadState

    private var mutablePages = MutableStateFlow<Map<Int, List<T>>>(emptyMap())

    val pages: StateFlow<Map<Int, List<T>>> get() = mutablePages

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
        val initialLoad = pageNumber == INITIAL_PAGE_NUMBER
        if (initialLoad) {
            mutableInitialLoadState.value = State.loading()
        }

        val state = pageRequest(pageNumber)
        if (state.isFailure) {
            if (initialLoad) {
                mutableInitialLoadState.value =
                    if (initialLoadState.value is State.Loading) {
                        if (pages.value[pageNumber].isNullOrEmpty()) State.success() else State.Success.Data(
                            Unit
                        )
                    } else {
                        State.failure()
                    }
            }

            return LoadResult.Error(IOException())
        }

        val page: List<T>
        val nextPageNumber: Int?
        if (state is State.Success.Data) {
            page = responsePageGetter(state.data) ?: emptyList()
            nextPageNumber = if (page.size == pageSize) pageNumber + 1 else null
        } else {
            page = emptyList()
            nextPageNumber = null
        }

        if (initialLoad) {
            mutableInitialLoadState.value =
                if (page.isEmpty()) State.success() else State.Success.Data(Unit)
        }

        mutablePages.update { it + (pageNumber to page) }

        val previousPageNumber = if (pageNumber > INITIAL_PAGE_NUMBER) pageNumber - 1 else null

        return LoadResult.Page(page, previousPageNumber, nextPageNumber)
    }

    override fun getRefreshKey(state: PagingState<Int, T>) = state.anchorPosition?.let {
        val closestPageToAnchorPosition = state.closestPageToPosition(it)

        closestPageToAnchorPosition?.prevKey?.plus(1)
            ?: closestPageToAnchorPosition?.nextKey?.minus(1)
    }
}