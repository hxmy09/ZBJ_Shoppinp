package com.android.shop.shopapp.model.response

import com.google.gson.annotations.SerializedName

class LoginResponse {

    var code: String? = null
    var msg: String? = null  // userState = 0 -> "对不起，你的账号还没有通过审核" ； 其他状态。返回- > 登陆成功。登录失败
    var data: Data? = null
}

class Data {
    @SerializedName("user_name")
    var userName: String? = null
    //用户状态 0 - 未审核，1 - 超级管理员 2-普通管理员 3- 普通会员
    @SerializedName("user_state")
    var userState: Int = 0
    //电话号码
    var phone: String? = null
    //地址
    var address: String? = null
    //推荐码
    var superior:String? = null
    @SerializedName("id")
    var userId:String? = null
}