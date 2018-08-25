package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.model.Detail
import com.android.shop.shopapp.model.ProductModel
import com.android.shop.shopapp.model.request.RegisterRequest
import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author a488606
 * @since 3/29/18
 */

interface UploadsImService {
    //    @Multipart
//    @POST("mall/api/product/add")
//    fun postImage(@Header("session_id") session_id: String = "24b75be656d4c9a34f6e16eb427daec4", @Header("desc") desc: String, @Header("price") price: String, @Header("groupName") group: String, @Header("userName") userName: String, @Part img: MultipartBody.Part): Single<ResponseBody>
    @Multipart
    @POST("mall/api/product/add")
    @Headers(*arrayOf( "session_id:24b75be656d4c9a34f6e16eb427daec4"))
    @JsonAndXmlConverters.Json
    fun postImage(@Part("details") details: RequestBody, @Part img: Array<MultipartBody.Part>, @Part("desc") desc: RequestBody, @Part("price") price: RequestBody, @Part("group_name") groupName: RequestBody, @Part("user_name") userName: RequestBody, @Part("key_words") keyWord: RequestBody, @Part("min_order") minOrder: RequestBody): Single<ResponseBody>


//    @Header("session_id") session_id: String = "24b75be656d4c9a34f6e16eb427daec4", @Header("desc") desc: String, @Header("price") price: String, @Header("groupName") group: String, @Header("userName") userName: String,
}

