package com.android.shop.shopapp.dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * @author a488606
 * @since 3/21/18
 */
@Entity(tableName = "ShoppingModel")
class ShoppingModel() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    @ColumnInfo
    var productId: String? = null//商品id
    @ColumnInfo
    var groupName: String? = null //商品组
    @ColumnInfo
    var imageUrl: String? = null //商品图片
    @ColumnInfo
    var desc: String? = null //商品描述
    @ColumnInfo
    var price: Double? = null//购买价格
    @ColumnInfo
    var amount: Int? = null //购买数量

    @ColumnInfo
    var isSelected: Boolean = false //是否选中

    constructor(parcel: Parcel) : this() {
        uid = parcel.readInt()
        productId = parcel.readString()
        groupName = parcel.readString()
        imageUrl = parcel.readString()
        desc = parcel.readString()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        amount = parcel.readValue(Int::class.java.classLoader) as? Int
        isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(productId)
        parcel.writeString(groupName)
        parcel.writeString(imageUrl)
        parcel.writeString(desc)
        parcel.writeValue(price)
        parcel.writeValue(amount)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShoppingModel> {
        override fun createFromParcel(parcel: Parcel): ShoppingModel {
            return ShoppingModel(parcel)
        }

        override fun newArray(size: Int): Array<ShoppingModel?> {
            return arrayOfNulls(size)
        }
    }


}