package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.response.ProductsResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @author a488606
 * @since 3/29/18
 */

interface GetProductsService {
    @POST("api/allProduct")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun getAllProduct(): Observable<ProductsResponse>

    @FormUrlEncoded
    @POST("api/getProdctsByUserNameAndUserState")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun getAllProductByUser(@Field("userName") userName: String?, @Field("userState") userState: Int?): Observable<ProductsResponse>

    @FormUrlEncoded
    @POST("api/getProdctsByGroup")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun getAllProductGroup(@Field("group") group: String?): Observable<ProductsResponse>

    @FormUrlEncoded
    @POST("api/search")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    @JsonAndXmlConverters.Json
    fun search(@Field("queryText") queryText: String?): Observable<ProductsResponse>
}