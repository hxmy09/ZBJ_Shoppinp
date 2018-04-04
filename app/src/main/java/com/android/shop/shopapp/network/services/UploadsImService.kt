package com.android.shop.shopapp.network.services

import com.android.shop.shopapp.network.JsonAndXmlConverters
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * @author a488606
 * @since 3/29/18
 */

interface UploadsImService {
    @Multipart
    @POST("/api")
    @JsonAndXmlConverters.Json
    // @Headers(*arrayOf("Content-Type: application/json", "session_id:2d9e7004e3a320755d1d554e234573b4"))
    // fun postImage(@Part desc: MultipartBody.Part, @Part price: MultipartBody.Part, @Part group: MultipartBody.Part, @Part image: MultipartBody.Part): Single<ResponseBody>
    fun postImage(@Header("Content-Type") contentType: String, @Header("desc") desc: String, @Header("price") price: String, @Header("group") group: String, @Header("userName") userName: String, @Header("productId") productId: String, @Part image: MultipartBody.Part): Single<ResponseBody>
}