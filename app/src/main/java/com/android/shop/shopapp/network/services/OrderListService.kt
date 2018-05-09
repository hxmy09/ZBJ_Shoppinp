package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.ShoppingModel
import com.android.shop.shopapp.model.response.CommonResponse
import com.android.shop.shopapp.model.response.OrderResponse
import com.android.shop.shopapp.model.response.ProductOrder
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface OrderListService {
    //根据用户名和购物车状态0  获取购物车商品
    //这个服务器端。需要先根据买家用户名，在购物车表里查询所有的商品，得到商品id, 在根据商品id 得到所有商品信息
    @POST("mall/order/get")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
    @JsonAndXmlConverters.Json
    fun getAllOrders(@JsonAndXmlConverters.Json @Body request: ShoppingModel): Observable<OrderResponse>

//    //根据用户名和购物车状态0  获取购物车商品
//    //这个服务器端。需要先根据买家用户名，在购物车表里查询所有的商品，得到商品id, 在根据商品id 得到所有商品信息
//    @POST("mall/purchase/orderDetail")
//    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
//    @JsonAndXmlConverters.Json
//    fun getOrderDetail(@JsonAndXmlConverters.Json @Body request: OrderParameterRequest): Observable<ShoppingResponse>


    //购买商品触发订单添加，将购买的商品添加到商品订单数据库中
    @POST("mall/order/add")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:5b4465220bf25dbf556ab46d875c98bc"))
    @JsonAndXmlConverters.Json
    fun buyProducts(@JsonAndXmlConverters.Json @Body request: ProductOrder): Observable<CommonResponse>


    //更新order 流程//0购物车100未付款200代发货300已发货400售后
    @POST("mall/order/modify")
    @Headers(*arrayOf("Content-Type: application/json"))
    @JsonAndXmlConverters.Json
    fun updateOrderStatus(@JsonAndXmlConverters.Json @Body request: ShoppingModel): Observable<CommonResponse>
}