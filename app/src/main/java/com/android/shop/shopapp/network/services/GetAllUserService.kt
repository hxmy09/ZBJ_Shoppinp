package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.response.UserResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * @author a488606
 * @since 3/14/18
 */

interface GetAllUserService {
    @POST("mall/api/users/get/uncheck")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:bcd2dbef917e85c08c45639785f88371"))
    @JsonAndXmlConverters.Json
    fun getAllUser(): Observable<UserResponse>

}
