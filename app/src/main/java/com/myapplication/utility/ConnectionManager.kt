package com.myapplication.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(@ApplicationContext private val context: Context) :
    DefaultLifecycleObserver {

    private val connectivityManager get() = context.getSystemService<ConnectivityManager>()

    private val mutableConnectionStatus = MutableStateFlow(getInitialConnectionStatus())

    private val connectionStatus: StateFlow<Boolean> = mutableConnectionStatus

    val isConnected get() = connectionStatus.value

    private val networkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            mutableConnectionStatus.value = true
        }

        override fun onLost(network: Network) {
            mutableConnectionStatus.value = false
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    private fun getInitialConnectionStatus(): Boolean {
        connectivityManager?.let {
            val networkCapabilities = it.getNetworkCapabilities(it.activeNetwork)
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }

        return false
    }
}