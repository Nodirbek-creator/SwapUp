package com.example.swapup.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver(
    private val context: Context
):ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val currentStatus = getCurrentStatus()
        trySend(currentStatus)
        val callback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                trySend(ConnectivityObserver.Status.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                trySend(ConnectivityObserver.Status.Losing)
            }

            override fun onUnavailable() {
                trySend(ConnectivityObserver.Status.Unavailable)
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityObserver.Status.Lost)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }



    }.distinctUntilChanged()

    fun getCurrentStatus(): ConnectivityObserver.Status {
        val network = connectivityManager.activeNetwork ?: return ConnectivityObserver.Status.Unavailable
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return ConnectivityObserver.Status.Unavailable

        return if(
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ){
            ConnectivityObserver.Status.Available
        } else{
            ConnectivityObserver.Status.Unavailable
        }
    }

}