package com.android.shop.shopapp.model.response

import com.android.shop.shopapp.model.request.RegisterRequest

class UserResponse {
    var code: String? = null
    var data: MutableList<RegisterRequest>? = null
}

//
//class AllUser {
//    var users: List<RegisterRequest>? = null
//}