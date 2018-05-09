package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.response.ProductsResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST


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
    //    @SerializedName("group_name")
    var groupName: String? = null
    //    @SerializedName("user_name")
    var userName: String? = null
    //    @SerializedName("suer_state")
    var userState: Int? = null
    //    @SerializedName("product_id")
    var productId: String? = null
    //    @SerializedName("product_state")
    var productState: Int? = null//0购物车1未付款2代发货3已发货4售后
    var start: Int? = null
    var end: Int? = null
    //根据ID删除  根据产品productId 加上选择的颜色名称 ， 再加上size 名称加上用户名组成的唯一主键。 进行删除，添加。
    //例如： username + "_" + productId + "_"+color+"_"+size
    @SerializedName("shopping_ids")
    var shoppingIds: List<String>? = null
    @SerializedName("key_word")
    var keyWord:String? = null
}


class OrderParameterRequest {
    @SerializedName("group_name")
    var groupName: String? = null
    @SerializedName("user_name")
    var userName: String? = null
    @SerializedName("user_state")
    var userState: Int? = null
    @SerializedName("product_id")
    var productId: String? = null
    @SerializedName("product_state")
    var productState: Int? = null//0购物车1未付款2代发货3已发货4售后
    @SerializedName("orderNumber")
    var orderNumber: String? = null//0购物车1未付款2代发货3已发货4售后
    var start: Int? = null
    var end: Int? = null
    //根据ID删除  根据产品productId 加上选择的颜色名称 ， 再加上size 名称加上用户名组成的唯一主键。 进行删除，添加。
    //例如： username + "_" + productId + "_"+color+"_"+size
    @SerializedName("shopping_ids")
    var shoppingIds: List<String>? = null
}
