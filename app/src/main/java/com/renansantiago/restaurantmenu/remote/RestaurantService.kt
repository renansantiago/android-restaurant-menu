package com.renansantiago.restaurantmenu.remote

import com.renansantiago.restaurantmenu.model.Restaurant
import com.renansantiago.restaurantmenu.remote.interceptor.RemoteRequestInterceptor
import com.renansantiago.restaurantmenu.remote.interceptor.RxRemoteErrorInterceptor
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RestaurantService {

    @GET("restaurants/search/fields")
    fun getRestaurantDetails(
        @Query("restaurant_phone") restaurant_phone: String,
        @Query("exact") exact: Boolean,
        @Query("fullmenu") fullmenu: Boolean
    ): Single<ResponseWrap<Restaurant>>

    companion object {
        fun createService(
            baseUrl: String,
            requestInterceptor: RemoteRequestInterceptor,
            rxRemoteErrorInterceptor: RxRemoteErrorInterceptor
        ): RestaurantService {
            val client = OkHttpClient().newBuilder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(rxRemoteErrorInterceptor)
                .addInterceptor(getHttpLoggingInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(baseUrl)
                .build()
                .create(RestaurantService::class.java)
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return loggingInterceptor
        }
    }
}