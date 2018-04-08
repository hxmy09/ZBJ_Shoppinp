package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.response.ProductsResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author a488606
 * @since 3/29/18
 */

interface GetProductsService {
//    @POST("mall/api/products/get/by/users")
//    @Headers(*arrayOf("Content-Type: application/json", "session_id:ec3af30ef54528b513b99888c6e8737a"))
//    @JsonAndXmlConverters.Json
//    fun getAllProduct(@JsonAndXmlConverters.Json @Body request: ProductParameterRequest): Observable<ProductsResponse>

    @POST("mall/api/products/get/by/users")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:ec3af30ef54528b513b99888c6e8737a"))
    @JsonAndXmlConverters.Json
    fun getAllProductByUser(@JsonAndXmlConverters.Json @Body request: ProductParameterRequest): Observable<ProductsResponse>

    @POST("mall/api/products/get")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
    @JsonAndXmlConverters.Json
    fun getAllProductGroup(@JsonAndXmlConverters.Json @Body request: ProductParameterRequest): Observable<ProductsResponse>

    @POST("mall/api/products/get/by/users")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:ec3af30ef54528b513b99888c6e8737a"))
    @JsonAndXmlConverters.Json
    fun search(@Field("query_text") queryText: String?): Observable<ProductsResponse>
}

class ProductParameterRequest {
    var groupName: String? = null
    var userName: String? = null
    var userState: Int? = null
}