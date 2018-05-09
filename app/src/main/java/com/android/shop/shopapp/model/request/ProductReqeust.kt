package com.android.shop.shopapp.model.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by myron on 3/25/18.
 */
//class ProductReqeust() : Parcelable {
//    @SerializedName("group_name")
//    var groupName: String? = null
//    var price: Double? = null
//    var desc: String? = null
//    //    @SerializedName("img_url")
//    var img: String? = null
//    //var groupId: String? = null //商品唯一性
//    @SerializedName("user_name")
//    var userName: String? = null
//    @SerializedName("product_id")
//    var productId: String? = null
//    var isSelected: Boolean = false
//
//    var imgs: List<String>? = arrayListOf()
//
//    constructor(parcel: Parcel) : this() {
//        groupName = parcel.readString()
//        price = parcel.readValue(Double::class.java.classLoader) as? Double
//        desc = parcel.readString()
//        img = parcel.readString()
//        userName = parcel.readString()
//        productId = parcel.readString()
//        isSelected = parcel.readByte() != 0.toByte()
//        imgs = parcel.createStringArrayList()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(groupName)
//        parcel.writeValue(price)
//        parcel.writeString(desc)
//        parcel.writeString(img)
//        parcel.writeString(userName)
//        parcel.writeString(productId)
//        parcel.writeByte(if (isSelected) 1 else 0)
//        parcel.writeStringList(imgs)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<ProductReqeust> {
//        override fun createFromParcel(parcel: Parcel): ProductReqeust {
//            return ProductReqeust(parcel)
//        }
//
//        override fun newArray(size: Int): Array<ProductReqeust?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//
//}