package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.request.ProductIdsReqeust
import com.android.shop.shopapp.model.response.CommonResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import com.google.gson.annotations.SerializedName
import com.hxmy.sm.network.services.payDetail
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
    fun getApppayInfo(@JsonAndXmlConverters.Json @Body param: PayParameter): Observable<AppPayResponse>


    @POST("mall/order/query")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:11d4cd4e9711d481abfd10c6a99c4a9c"))
    @JsonAndXmlConverters.Json
    fun queryPayResult(@JsonAndXmlConverters.Json @Body param: PayParameter): Observable<AppPayResponse>
}

class AppPayResponse(
        var code: String,
        var msg: String,
        var data: PayRequest
)

class PayRequest(val appid: String,
                 @SerializedName("mch_id")
                 val partnerid: String,
                 val prepayid: String,
                 val noncestr: String,
                 val timestamp: String,
                 @SerializedName("package")
                 val pack: String,
                 val sign: String)

class PayParameter(
        var device_info: String,
        var body: String = "冀汇聚-支付",
        var detail: String,
        var order_number: String
)