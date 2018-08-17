package com.android.shop.shopapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.arch.persistence.room.Room


/**
 * Created by myron on 3/25/18.
 */
class ShopApplication : Application() {
    var sharedPreferences: SharedPreferences? = null
    var db: AppDatabase? = null
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
    }

    fun getSharePReference(): SharedPreferences? {
        return sharedPreferences;
    }

    val isLoggedIn: Boolean
        get() = sharedPreferences?.getBoolean("loggedin", false) ?: false
    val address: String
        get() = sharedPreferences?.getString("address", "") ?: ""
    val phone: String
        get() = sharedPreferences?.getString("phone", "") ?: ""
    val userName: String
        get() = sharedPreferences?.getString("userName", "") ?: ""
    val userState: Int
        get() = sharedPreferences?.getInt("userState", 0) ?: 0


     db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "database-name").build()

}


enum class UserType(type: Int) {
    UN_KNOWN(0),//没有审核的用户
    SUPER_ADMIN(1),//总部人员，最好权限
    ADMIN(2),//厂家（卖家，上货的）
    NORMAL(3),//零售商（普通会员买货的）
    AGENT(4) //代理商

}