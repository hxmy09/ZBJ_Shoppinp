package com.android.shop.shopapp.model.request

import com.google.gson.annotations.SerializedName

class RegisterRequest {

    //用户名
    @SerializedName("user_name")
    var userName: String? = null  //这个应该作为主键
    //密码
    var password: String? = null
    //电话号码
    var phone: String? = null
    //地址
    var address: String? = null
    //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
    @SerializedName("user_state")
    var userState: Int = 0

}