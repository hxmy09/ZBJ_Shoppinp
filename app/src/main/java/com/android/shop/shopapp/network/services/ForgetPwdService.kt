package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.request.ForgetPwdRequest
import com.android.shop.shopapp.model.response.ForgetPwdResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.android.shop.shopapp.network.JsonAndXmlConverters


/**
 * @author a488606
 * @since 3/14/18
 */

interface ForgetPwdService {
    @POST("api/authentication")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun forgetPwd(@JsonAndXmlConverters.Json @Body login: ForgetPwdRequest): Observable<ForgetPwdResponse>
}