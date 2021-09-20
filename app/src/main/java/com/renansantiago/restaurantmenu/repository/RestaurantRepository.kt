package com.renansantiago.restaurantmenu.repository

import com.renansantiago.restaurantmenu.model.Restaurant
import io.reactivex.Single

interface RestaurantRepository {

    fun getRestaurantDetails(): Single<Restaurant>
}