package com.renansantiago.restaurantmenu.di

import com.renansantiago.restaurantmenu.remote.RestaurantService
import com.renansantiago.restaurantmenu.remote.interceptor.RemoteRequestInterceptor
import com.renansantiago.restaurantmenu.remote.interceptor.RxRemoteErrorInterceptor
import org.koin.dsl.module.module

val dataModule = module {

    factory { RxRemoteErrorInterceptor() }

    factory { RemoteRequestInterceptor(getProperty("api_key")) }

    single {
        RestaurantService.createService(
            getProperty("base_url"),
            get(),
            get()
        )
    }
}