package com.renansantiago.restaurantmenu.remote

data class ResponseWrap<T>(val data: List<T> = arrayListOf())