package com.myapplication.ui.base

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlin.collections.set

abstract class BaseViewModel : ViewModel() {

    protected fun <T> Flow<T>.asLiveDataInViewModelScope() = asLiveData(viewModelScope.coroutineContext)

    private val liveDataObserverMap = mutableMapOf<LiveData<out Any>, Observer<out Any>>()

    @MainThread
    protected fun <T : Any> observe(liveData: LiveData<T>, onChanged: (T) -> Unit) {
        if (liveDataObserverMap.contains(liveData)) {
            throw IllegalStateException("LiveData is already associated with an Observer")
        }

        val observer = Observer(onChanged)
        liveData.observeForever(observer)
        liveDataObserverMap[liveData] = observer
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()

        @Suppress("UNCHECKED_CAST")
        liveDataObserverMap.forEach { it.key.removeObserver(it.value as Observer<Any>) }
    }
}