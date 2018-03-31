package com.android.shop.shopapp.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


/**
 * @author a488606
 * @since 3/21/18
 */
@Database(version = 1, entities = [ShoppingModel::class, ProductModel::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingDao(): ShoppingDao
    abstract fun productDao(): ProductDao
}

class DBUtil(context: Context) {
    val mAppDatabase: AppDatabase by lazy {
        var mAppDatabase = Room.databaseBuilder(context,
                AppDatabase::class.java, "database-shopping").allowMainThreadQueries().build()

        mAppDatabase
    }
}

