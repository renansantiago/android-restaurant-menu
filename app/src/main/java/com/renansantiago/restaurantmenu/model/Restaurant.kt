package com.renansantiago.restaurantmenu.model

data class Restaurant(
    val restaurant_name: String,
    val restaurant_phone: String,
    val restaurant_website: String,
    val hours: String,
    val menus: List<Menu>
)