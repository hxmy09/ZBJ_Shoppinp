package com.android.shop.shopapp.model.request

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by myron on 3/25/18.
 */
class ProductReqeust() : Parcelable {
    var groupName: String? = null
    var price: Double? = null
    var desc: String? = null
    var img: String? = null
    var groupId: String? = null //商品唯一性

    constructor(parcel: Parcel) : this() {
        groupName = parcel.readString()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        desc = parcel.readString()
        img = parcel.readString()
        groupId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(groupName)
        parcel.writeValue(price)
        parcel.writeString(desc)
        parcel.writeString(img)
        parcel.writeString(groupId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductReqeust> {
        override fun createFromParcel(parcel: Parcel): ProductReqeust {
            return ProductReqeust(parcel)
        }

        override fun newArray(size: Int): Array<ProductReqeust?> {
            return arrayOfNulls(size)
        }
    }


}