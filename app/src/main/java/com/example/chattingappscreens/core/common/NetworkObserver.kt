package com.example.chattingappscreens.core.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkObserver (private val context : Context){

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected : Flow<Boolean> = callbackFlow {
        val callback = object  : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                trySend(true)
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                trySend(false)
                super.onLost(network)
            }
        }

        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest,callback)

        trySend(checkInitialConnection())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    fun  checkInitialConnection(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return  caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


}