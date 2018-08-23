package com.android.shop.shopapp.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName

class ShoppingModel() : Parcelable {

    @SerializedName("product_id")
    var productId: String? = null
    @SerializedName("img_url")
    var imageUrl: String? = null
    var desc: String? = null
    var price: Double? = null
    @SerializedName("order_amount")
    var orderAmount: Int? = null //购买数量
    var isSelected: Boolean = false
    @SerializedName("seller")
    var seller: String? = null
    @SerializedName("buyer")
    var buyer: String? = null
    @SerializedName("order_time")
    var orderTime: String? = null
    @SerializedName("key_words")
    var keyWords: String? = null
    @SerializedName("size")
    var size: String? = null
    @SerializedName("color")
    var color: String? = null
    @SerializedName("amount")
    var amount: String? = null
    @SerializedName("product_state")
    var productState: Int? = null
    @SerializedName("order_state")
    var orderState: Int? = null
    //这个字段主要是为了购买商品加入购物车之后的流程中使用，识别唯一商品
    @SerializedName("shopping_id")
    var shoppingId: String? = null
    @SerializedName("order_number")
    var orderNumber: String? = null

    var start: Int? = null
    var end: Int? = null
    @SerializedName("user_state")
    var userState: Int? = null
    @SerializedName("is_search_seller")
    var isSearchSellerOrders = true
    //订单总额
    var total: Double? = null
    @SerializedName("buyer_address")
    var buyerAddress: String? = null
    @SerializedName("buyer_phone")
    var buyerPhone: String? = null
    @SerializedName("seller_address")
    var sellerAddress: String? = null
    @SerializedName("seller_phone")
    var sellerPhone: String? = null

    //起订量
    @SerializedName("beginOrderAmount")
    var beginOrderAmount: Int = 1

    constructor(parcel: Parcel) : this() {
        productId = parcel.readString()
        imageUrl = parcel.readString()
        desc = parcel.readString()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        orderAmount = parcel.readValue(Int::class.java.classLoader) as? Int
        isSelected = parcel.readByte() != 0.toByte()
        seller = parcel.readString()
        buyer = parcel.readString()
        orderTime = parcel.readString()
        keyWords = parcel.readString()
        size = parcel.readString()
        color = parcel.readString()
        amount = parcel.readString()
        productState = parcel.readValue(Int::class.java.classLoader) as? Int
        orderState = parcel.readValue(Int::class.java.classLoader) as? Int
        shoppingId = parcel.readString()
        orderNumber = parcel.readString()
        start = parcel.readValue(Int::class.java.classLoader) as? Int
        end = parcel.readValue(Int::class.java.classLoader) as? Int
        userState = parcel.readValue(Int::class.java.classLoader) as? Int
        isSearchSellerOrders = parcel.readByte() != 0.toByte()
        total = parcel.readValue(Double::class.java.classLoader) as? Double
        buyerAddress = parcel.readString()
        buyerPhone = parcel.readString()
        sellerAddress = parcel.readString()
        sellerPhone = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(imageUrl)
        parcel.writeString(desc)
        parcel.writeValue(price)
        parcel.writeValue(orderAmount)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(seller)
        parcel.writeString(buyer)
        parcel.writeString(orderTime)
        parcel.writeString(keyWords)
        parcel.writeString(size)
        parcel.writeString(color)
        parcel.writeString(amount)
        parcel.writeValue(productState)
        parcel.writeValue(orderState)
        parcel.writeString(shoppingId)
        parcel.writeString(orderNumber)
        parcel.writeValue(start)
        parcel.writeValue(end)
        parcel.writeValue(userState)
        parcel.writeByte(if (isSearchSellerOrders) 1 else 0)
        parcel.writeValue(total)
        parcel.writeString(buyerAddress)
        parcel.writeString(buyerPhone)
        parcel.writeString(sellerAddress)
        parcel.writeString(sellerPhone)
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