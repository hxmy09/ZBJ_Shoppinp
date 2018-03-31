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
    @POST("api/authentication")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun register(@JsonAndXmlConverters.Json @Body login: RegisterRequest): Observable<RegisterResponse>
}