package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.request.ProductIdsReqeust
import com.android.shop.shopapp.model.response.CommonResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by myron on 5/8/18.
 */
interface AppPayService {
    @POST("mall/app_pay")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:11d4cd4e9711d481abfd10c6a99c4a9c"))
    @JsonAndXmlConverters.Json
    fun getApppayInfo(): Observable<AppPayResponse>
}

class AppPayResponse {
    val appid: String? = null
    val partnerid: String? = null
    val prepayid: String? = null
    val noncestr: String? = null
    val timestamp: String? = null
    @SerializedName("package")
    val pack: String? = null
    val sign: String? = null

}