package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.request.ProductIdsReqeust
import com.android.shop.shopapp.model.response.CommonResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * @author a488606
 * @since 3/14/18
 */

interface DeleteProductService {
    @POST("api/deleteProduct")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun deleteProducts(@JsonAndXmlConverters.Json @Body request: ProductIdsReqeust): Observable<CommonResponse>
}