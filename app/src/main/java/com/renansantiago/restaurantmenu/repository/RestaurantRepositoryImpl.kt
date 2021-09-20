package com.renansantiago.restaurantmenu.repository

import com.renansantiago.restaurantmenu.remote.RestaurantService
import com.renansantiago.restaurantmenu.model.Restaurant
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RestaurantRepositoryImpl(
    private val service: RestaurantService
) : RestaurantRepository {

    /**
     * Hardcoded parameters to fetch one restaurant,
     * avoid using the service with directly id because it doesn't return items description
     */

    companion object {
        const val RESTAURANT_PHONE = "7183768800"
        const val EXACT = true
        const val FULL_MENU = true
    }

    override fun getRestaurantDetails(): Single<Restaurant> {
        return service.getRestaurantDetails(RESTAURANT_PHONE, EXACT, FULL_MENU)
            .subscribeOn(Schedulers.io())
            .map { it.data.first() }
    }

}