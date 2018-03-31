package com.android.shop.shopapp.dao

import android.arch.persistence.room.*

/**
 * @author a488606
 * @since 3/21/18
 */
@Dao
interface ProductDao {
    @Delete
    fun delete(vararg model: ProductModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ProductModel)

    @Query("SELECT * FROM ProductModel")
    fun findAll(): List<ProductModel>


    @Query("SELECT * FROM ProductModel where groupName = :group")
    fun findByGroup(group: String): List<ProductModel>

    @Query("SELECT * FROM ProductModel where ProductModel.groupId=:groupId")
    fun findByGroupId(groupId: String): ProductModel

    @Query("DELETE FROM ProductModel where ProductModel.groupId=:groupId")
    fun deleteByGroupId(groupId: String)
}