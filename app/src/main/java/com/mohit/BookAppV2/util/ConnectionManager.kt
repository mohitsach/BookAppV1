package com.mohit.BookAppV2.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkconnectivity(context:Context):Boolean{

        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetwork:NetworkInfo?=connectivityManager.activeNetworkInfo
        if(activenetwork?.isConnected!=null){
            return activenetwork.isConnected
        }else{
            return false
        }
    }
}