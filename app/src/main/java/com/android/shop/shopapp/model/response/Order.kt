package com.android.shop.shopapp.model.response

import android.os.Parcel
import android.os.Parcelable

/**
 * @author a488606
 * @since 4/4/18
 */
data class Order(var userName: String, var orderNum: String, var orderAmount: String, var orderTime: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(orderNum)
        parcel.writeString(orderAmount)
        parcel.writeString(orderTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}