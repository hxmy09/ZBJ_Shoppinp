package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.request.OrderModel
import com.android.shop.shopapp.model.response.CommonResponse
import com.android.shop.shopapp.model.response.ProductDetailResponse
import com.android.shop.shopapp.model.response.ProductsResponse
import com.android.shop.shopapp.model.response.ShoppingResponse
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface GetProductsDetailsService {

    //传入productId , 获取产品详细
    @POST("mall/api/product/detail/get")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:8b0a32cbfd4322a71b8fa0e7c7628833"))
    @JsonAndXmlConverters.Json
    fun getProductDetail(@JsonAndXmlConverters.Json @Body request: ProductParameterRequest): Observable<ProductDetailResponse>


    //加入到购物车
    @POST("mall/purchase/add")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
    @JsonAndXmlConverters.Json
    fun addShoppingTrolley(@JsonAndXmlConverters.Json @Body request: OrderModel): Observable<CommonResponse>


    //根据用户名和购物车状态0  获取购物车商品
    //这个服务器端。需要先根据买家用户名，在购物车表里查询所有的商品，得到商品id, 在根据商品id 得到所有商品信息
    @POST("mall/purchase/get")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
    @JsonAndXmlConverters.Json
    fun queryShoppingTrolley(@JsonAndXmlConverters.Json @Body request: ProductParameterRequest): Observable<ShoppingResponse>


    //根据用户名和购物车状态0  获取购物车商品
    //这个服务器端。需要先根据买家用户名，在购物车表里查询所有的商品，得到商品id, 在根据商品id 得到所有商品信息
    @POST("mall/purchase/delete")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
    @JsonAndXmlConverters.Json
    fun deleteShoppingTrolley(@JsonAndXmlConverters.Json @Body request: ProductParameterRequest): Observable<CommonResponse>
}
