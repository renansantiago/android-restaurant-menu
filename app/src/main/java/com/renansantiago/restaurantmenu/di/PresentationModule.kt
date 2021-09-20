package com.renansantiago.restaurantmenu.di

import com.renansantiago.restaurantmenu.presentation.restaurant.RestaurantViewModel
import com.renansantiago.restaurantmenu.presentation.restaurant.MenuItemRecyclerAdapter
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val presentationModule = module {

    viewModel { RestaurantViewModel(get()) }

    factory { MenuItemRecyclerAdapter() }

}