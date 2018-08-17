package com.android.shop.shopapp

import android.arch.persistence.room.*
import android.database.Cursor


@Dao
interface DbProductDao {
    @Query("SELECT * FROM DbProductModel")
    fun getDealsCursor(dealText: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: DbProductModel)
}

@Entity(tableName = "DbProductModel")
class DbProductModel(
        @ColumnInfo(name = "imgUrl")
        private val imgUrl: String? = null,

        @ColumnInfo(name = "desc")
        private val desc: String? = null,

        @ColumnInfo(name = "price")
        private val price: String? = null
)

@Database(entities = [(DbProductModel::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbProductDao(): DbProductDao
}