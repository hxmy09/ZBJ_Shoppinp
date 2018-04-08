package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.request.LoginRequest
import com.android.shop.shopapp.model.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.android.shop.shopapp.network.JsonAndXmlConverters


/**
 * @author a488606
 * @since 3/14/18
 */

interface LoginService {
    @POST("mall/api/user/login")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:a945f9123981e29ccbebc98eed680786"))
    @JsonAndXmlConverters.Json
    fun login(@JsonAndXmlConverters.Json @Body login: LoginRequest): Observable<LoginResponse>
}