package com.android.shop.shopapp.model.network

import com.android.shop.shopapp.network.JsonAndXmlConverters
import com.android.shop.shopapp.network.services.AppPayService
import com.android.shop.shopapp.network.services.GetProductsDetailsService
import com.android.shop.shopapp.network.services.GetProductsService
import com.android.shop.shopapp.network.services.UploadsImService
import com.hxmy.sm.network.services.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitHelper() {

    fun getLoginService(): LoginService {
        val retrofit = createRetrofit()
        return retrofit.create(LoginService::class.java);
    }

    fun getRegisterService(): RegisterService {
        val retrofit = createRetrofit()
        return retrofit.create(RegisterService::class.java);
    }

    fun getForgetPwdService(): ForgetPwdService {
        val retrofit = createRetrofit()
        return retrofit.create(ForgetPwdService::class.java);
    }

    fun getUploadsImService(): UploadsImService {
        val retrofit = createRetrofit()
        return retrofit.create(UploadsImService::class.java);
    }

    fun getAllUserService(): GetAllUserService {
        val retrofit = createRetrofit()
        return retrofit.create(GetAllUserService::class.java);
    }

    fun getProductsService(): GetProductsService {
        val retrofit = createRetrofit()
        return retrofit.create(GetProductsService::class.java);
    }

    fun getProductDetailService(): GetProductsDetailsService {
        val retrofit = createRetrofit()
        return retrofit.create(GetProductsDetailsService::class.java);
    }

    fun getOrdersService(): OrderListService {
        val retrofit = createRetrofit()
        return retrofit.create(OrderListService::class.java);
    }

    fun getUsersService(): UsersServices {
        val retrofit = createRetrofit()
        return retrofit.create(UsersServices::class.java);
    }

    fun getAppPay(): AppPayService {
        val retrofit = createRetrofit()
        return retrofit.create(AppPayService::class.java);
    }

    fun getDeleteProductService(): DeleteProductService {
        val retrofit = createRetrofit()
        return retrofit.create(DeleteProductService::class.java);
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    //.addQueryParameter("username", "demo")
                    .build()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .url(url)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build();
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://118.24.76.50:8080/")//http://118.24.76.50:8080///47.104.169.240 - http://8f3kqq.natappfree.cc/
                .addConverterFactory(JsonAndXmlConverters.QualifiedTypeConverterFactory(
                        GsonConverterFactory.create(),
                        SimpleXmlConverterFactory.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // <- add this
                .client(createOkHttpClient())
                .build()
    }


}