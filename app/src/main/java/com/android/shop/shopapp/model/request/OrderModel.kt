package com.android.shop.shopapp.model.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by myron on 4/12/18.
 */

data class OrderModel(
        @SerializedName("seller") var seller: String?, //卖家
        @SerializedName("buyer") var buyer: String?,//买家
        @SerializedName("product_id") var product_id: String?,//商品id
        @SerializedName("product_state") var product_state: Int?, //商品状态 0 - 购物车
        @SerializedName("color") var color: String?, //商品颜色
        @SerializedName("size") var size: String?, //商品尺寸
        @SerializedName("order_amount") var orderAmount: Int?,//购买数量
        @SerializedName("order_time") var orderTime: String?,//时间
        @SerializedName("order_number") var orderNumber: String?,//订单号
        @SerializedName("img_url") var imgUrl: String?,
        @SerializedName("desc") var desc: String?,
        @SerializedName("price") var price: Double?

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(seller)
        parcel.writeString(buyer)
        parcel.writeString(product_id)
        parcel.writeValue(product_state)
        parcel.writeString(color)
        parcel.writeString(size)
        parcel.writeValue(orderAmount)
        parcel.writeString(orderTime)
        parcel.writeString(orderNumber)
        parcel.writeString(imgUrl)
        parcel.writeString(desc)
        parcel.writeValue(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }

}