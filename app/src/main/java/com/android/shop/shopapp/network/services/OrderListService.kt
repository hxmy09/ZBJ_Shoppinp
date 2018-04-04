package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.response.OrderListResponse
import com.android.shop.shopapp.model.response.ProductsResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * @author a488606
 * @since 3/14/18
 */

interface OrderListService {
    @FormUrlEncoded
    @POST("api/orderList")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun getAllOrders(@Field("userName") userName: String): Observable<OrderListResponse>

    @FormUrlEncoded
    @POST("api/orderDetail")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun getOrderDetail(@Field("orderNum") orderNum: String): Observable<ProductsResponse>
}