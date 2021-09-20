package com.renansantiago.restaurantmenu.di

import com.renansantiago.restaurantmenu.repository.RestaurantRepositoryImpl
import com.renansantiago.restaurantmenu.repository.RestaurantRepository
import org.koin.dsl.module.module

val domainModule = module {

    single<RestaurantRepository> { RestaurantRepositoryImpl(get()) }
}