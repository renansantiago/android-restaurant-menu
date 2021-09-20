package com.renansantiago.restaurantmenu.exception

class DefaultException(
    override val message: String = "Unexpected Error"
) : Exception()