package com.android.shop.shopapp

import android.arch.persistence.room.*
import android.database.Cursor


@Dao
interface DbProductDao {
    @Query("SELECT * FROM DbProductModel")
    fun getAll(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(vararg model: DbProductModel)

    @Query("DELETE FROM DbProductModel")
    fun deleteAll()
}

@Entity(tableName = "DbProductModel")
class DbProductModel(

        @ColumnInfo
        @PrimaryKey(autoGenerate = true)
        var _id: Int? = null,
        @ColumnInfo(name = "imgUrl")
        var imgUrl: String? = null,

        @ColumnInfo(name = "desc")
        var desc: String? = null,

        @ColumnInfo(name = "price")
        var price: String? = null,
        @ColumnInfo(name = "productId")
        var productId: String
)

@Database(entities = [(DbProductModel::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbProductDao(): DbProductDao
}