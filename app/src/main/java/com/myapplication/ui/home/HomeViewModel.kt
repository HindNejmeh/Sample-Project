package com.myapplication.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.myapplication.data.remote.Repository
import com.myapplication.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repository: Repository) : BaseViewModel() {

    private val homePagingSource = repository.getPagedHome()
    val images = homePagingSource.pagingDataflow.cachedIn(viewModelScope)
    val initialLoadState = homePagingSource.initialLoadState.asLiveDataInViewModelScope()

}