package com.android.shop.shopapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by myron on 3/25/18.
 */
class ShopApplication : Application() {
    var sharedPreferences: SharedPreferences? = null;
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
    }

    fun getSharePReference(): SharedPreferences? {
        return sharedPreferences;
    }
}