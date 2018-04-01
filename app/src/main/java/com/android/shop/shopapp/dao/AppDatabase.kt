package com.android.shop.shopapp.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration




/**
 * @author a488606
 * @since 3/21/18
 */
@Database(version = 3, entities = [ShoppingModel::class, ProductModel::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingDao(): ShoppingDao
    abstract fun productDao(): ProductDao
}

class DBUtil(context: Context) {
    val mAppDatabase: AppDatabase by lazy {
        var mAppDatabase = Room.databaseBuilder(context,
                AppDatabase::class.java, "database-shopping").addMigrations(MIGRATION_2_3).allowMainThreadQueries().build()

        mAppDatabase
    }

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    }

    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE users "
//                    + " ADD COLUMN createDate INTEGER");
        }
    }
}

