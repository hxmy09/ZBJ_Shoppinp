package com.android.shop.shopapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by myron on 4/16/18.
 */
data class UserModel(

        var id: String? = null,
        @SerializedName("user_name")
        var userName: String? = null,
        //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
        @SerializedName("user_state")
        var userState: Int = 0,
        //电话号码
        var phone: String? = null,
        //地址
        var address: String? = null,

        var isSelected: Boolean? = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userName)
        parcel.writeInt(userState)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeValue(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}
