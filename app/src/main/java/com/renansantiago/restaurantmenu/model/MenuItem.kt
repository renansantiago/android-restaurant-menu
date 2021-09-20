package com.renansantiago.restaurantmenu.model

data class MenuItem(
    val name: String,
    val description: String?,
    val price: String,
    val pricing: List<Pricing>,
    var priceWithCurrency: String?
) {
    fun hasDescription(): Boolean {
        return !description.isNullOrEmpty()
    }
}