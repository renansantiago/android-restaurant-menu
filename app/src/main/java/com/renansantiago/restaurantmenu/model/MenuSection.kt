package com.renansantiago.restaurantmenu.model

data class MenuSection(
    val section_name: String,
    val description: String,
    val menu_items: List<MenuItem>
)