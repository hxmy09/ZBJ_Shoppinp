package com.android.shop.shopapp.network.services

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import com.android.shop.shopapp.network.JsonAndXmlConverters

/**
 * @author a488606
 * @since 3/29/18
 */

interface UploadsImService {
    @Multipart
    @POST("/api")
    @JsonAndXmlConverters.Json
    fun postImage(@Part desc: MultipartBody.Part, @Part price: MultipartBody.Part, @Part group: MultipartBody.Part, @Part image: MultipartBody.Part): Single<ResponseBody>

    fun postImage1(@PartMap map:ArrayList<MultipartBody.Part>): Single<ResponseBody>
}