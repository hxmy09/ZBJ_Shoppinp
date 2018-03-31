package com.android.shop.shopapp.dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * @author a488606
 * @since 3/21/18
 */
@Entity(tableName = "ProductModel")
class ProductModel() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    @ColumnInfo
    var groupId: String? = null
    @ColumnInfo
    var groupName: String? = null
    @ColumnInfo
    var imageUrl: String? = null
    @ColumnInfo
    var desc: String? = null
    @ColumnInfo
    var price: Double? = null

    @Ignore
    var isSelected: Boolean = true

    constructor(parcel: Parcel) : this() {
        uid = parcel.readInt()
        groupId = parcel.readString()
        groupName = parcel.readString()
        imageUrl = parcel.readString()
        desc = parcel.readString()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(groupId)
        parcel.writeString(groupName)
        parcel.writeString(imageUrl)
        parcel.writeString(desc)
        parcel.writeValue(price)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }


}