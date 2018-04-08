package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.request.RegisterRequest
import com.android.shop.shopapp.model.response.RegisterResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.android.shop.shopapp.network.JsonAndXmlConverters


/**
 * @author a488606
 * @since 3/14/18
 */

interface RegisterService {
    @POST("mall/api/user/reg")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:78c6d5bc30cc3c664c6c53dbcdff792b"))
    @JsonAndXmlConverters.Json
    fun register(@JsonAndXmlConverters.Json @Body login: RegisterRequest): Observable<RegisterResponse>

    @POST("mall/api/user/validate")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5ce8d31cbf2863733eecee81cb66acf3"))
    @JsonAndXmlConverters.Json
    fun validate(@JsonAndXmlConverters.Json @Body login: RegisterRequest): Observable<RegisterResponse>
}