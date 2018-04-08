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
    @POST("mall/api/products/delete")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:11d4cd4e9711d481abfd10c6a99c4a9c"))
    @JsonAndXmlConverters.Json
    fun deleteProducts(@JsonAndXmlConverters.Json @Body request: ProductIdsReqeust): Observable<CommonResponse>
}