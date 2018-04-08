package com.android.shop.shopapp.model.request

import com.google.gson.annotations.SerializedName

class LoginRequest {

    @SerializedName("user_name")
    var userName: String? = null
    var password: String? = null
}