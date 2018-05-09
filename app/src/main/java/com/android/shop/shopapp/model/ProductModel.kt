package com.android.shop.shopapp.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName

class ProductModel() : Parcelable {

    @SerializedName("product_id")
    var productId: String? = null
    @SerializedName("group_name")
    var groupName: String? = null
    @SerializedName("img_urls")
    var imageUrls: List<String>? = null
//        get() {
//            if (field != null && field!!.isNotEmpty()) {
//                return field
//            } else {
//                var list = arrayListOf<String>()
//                list.add(imageUrl!!)
//                return list
//            }
//        }
    @SerializedName("img_url")
    var imageUrl: String? = null
    var desc: String? = null
    var price: Double? = null
    var orderAmount: Int? = null //购买数量

    var isSelected: Boolean = false

    @SerializedName("user_name")
    var userName: String? = null


    @SerializedName("userName")
    var userName_2: String? = null

    @SerializedName("key_words")
    var keyWords: String? = null

    var details: List<Detail>? = null
    //这个字段主要是为了购买商品加入购物车之后的流程中使用，识别唯一商品
    var shoppingId: String? = null

    constructor(parcel: Parcel) : this() {
        productId = parcel.readString()
        groupName = parcel.readString()
        imageUrls = parcel.createStringArrayList()
        imageUrl = parcel.readString()
        desc = parcel.readString()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        orderAmount = parcel.readValue(Int::class.java.classLoader) as? Int
        isSelected = parcel.readByte() != 0.toByte()
        userName = parcel.readString()
        keyWords = parcel.readString()
        details = parcel.createTypedArrayList(Detail)
        shoppingId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(groupName)
        parcel.writeStringList(imageUrls)
        parcel.writeString(imageUrl)
        parcel.writeString(desc)
        parcel.writeValue(price)
        parcel.writeValue(orderAmount)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(userName)
        parcel.writeString(keyWords)
        parcel.writeTypedList(details)
        parcel.writeString(shoppingId)
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

class Detail() : Parcelable {
    var img: String? = null
    var color: String? = null
    var store: List<Size>? = null

    constructor(parcel: Parcel) : this() {
        img = parcel.readString()
        color = parcel.readString()
        store = parcel.createTypedArrayList(Size)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(color)
        parcel.writeTypedList(store)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Detail> {
        override fun createFromParcel(parcel: Parcel): Detail {
            return Detail(parcel)
        }

        override fun newArray(size: Int): Array<Detail?> {
            return arrayOfNulls(size)
        }
    }

}

class Size() : Parcelable {
    var size: String? = null
    var amount: Int? = null

    constructor(parcel: Parcel) : this() {
        size = parcel.readString()
        amount = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(size)
        parcel.writeValue(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Size> {
        override fun createFromParcel(parcel: Parcel): Size {
            return Size(parcel)
        }

        override fun newArray(size: Int): Array<Size?> {
            return arrayOfNulls(size)
        }
    }

}