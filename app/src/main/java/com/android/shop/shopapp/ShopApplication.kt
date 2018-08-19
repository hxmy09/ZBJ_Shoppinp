package com.android.shop.shopapp

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.NonNull


/**
 * Created by myron on 3/25/18.
 */
class ShopApplication : Application() {
    var sharedPreferences: SharedPreferences? = null
    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "shopping")
//                .addCallback(object : RoomDatabase.Callback() {
//                    //第一次创建数据库时调用，但是在创建所有表之后调用的
//                    override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                    }
//
//                    //当数据库被打开时调用
//                    override fun onOpen(@NonNull db: SupportSQLiteDatabase) {
//                        super.onOpen(db)
//                    }
//                })
                .allowMainThreadQueries()//允许在主线程查询数据
//                .addMigrations()//迁移数据库使用，下面会单独拿出来讲
//                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .build()
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
    val userId: Int
        get() = sharedPreferences?.getInt("userId", 0) ?: 0
    val superior: String //代理商推荐码
        get() = sharedPreferences?.getString("superior", "") ?: ""

}


enum class UserType(type: Int) {
    UN_KNOWN(0),//没有审核的用户
    SUPER_ADMIN(1),//总部人员，最好权限
    ADMIN(2),//厂家（卖家，上货的）
    NORMAL(3),//零售商（普通会员买货的）
    AGENT(4) //代理商

}