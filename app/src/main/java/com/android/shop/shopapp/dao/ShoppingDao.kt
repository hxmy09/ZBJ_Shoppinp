package com.android.shop.shopapp.dao

import android.arch.persistence.room.*

/**
 * @author a488606
 * @since 3/21/18
 */
@Dao
interface ShoppingDao {
    @Delete
    fun delete(vararg model: ShoppingModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ShoppingModel)

    @Query("SELECT * FROM ShoppingModel")
    fun findAll(): List<ShoppingModel>

    @Query("SELECT * FROM ShoppingModel where ShoppingModel.groupId=:groupId")
    fun findByGroupId(groupId: String): ShoppingModel

    @Query("DELETE FROM ShoppingModel where ShoppingModel.groupId=:groupId")
    fun deleteByGroupId(groupId: String)
}