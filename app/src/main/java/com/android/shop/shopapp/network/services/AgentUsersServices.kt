package com.hxmy.sm.network.services

import com.android.shop.shopapp.model.UserModel
import com.android.shop.shopapp.model.request.LoginRequest
import com.android.shop.shopapp.model.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.android.shop.shopapp.network.JsonAndXmlConverters
import com.google.gson.annotations.SerializedName


interface AgentUsersServices {
    @POST("mall/api/users/subs")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:4e11f6a621c7fb99e2162e8a6a01038a"))
    @JsonAndXmlConverters.Json
    fun getAllUsers(@JsonAndXmlConverters.Json @Body request: AgentUserReqeust): Observable<UserResponse>


    @POST("mall/api/users/delete")
    @Headers(*arrayOf("Content-Type: application/json", "session_id:f5fb2a301f49fb9110fd6c034ac3f1f8"))
    @JsonAndXmlConverters.Json
    fun deleteUsers(@JsonAndXmlConverters.Json @Body request: UserReqeust): Observable<UserResponse>
}

data class AgentUserReqeust(
        @SerializedName("superior")
        var superior: String? = null
)