package com.renansantiago.restaurantmenu.di

import com.google.gson.Gson
import org.koin.dsl.module.module

val appModule = module {

    single { Gson() }

}