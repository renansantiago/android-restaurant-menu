package com.renansantiago.restaurantmenu.presentation.restaurant

import androidx.lifecycle.MutableLiveData
import com.renansantiago.restaurantmenu.exception.DefaultException
import com.renansantiago.restaurantmenu.model.MenuItem
import com.renansantiago.restaurantmenu.model.MenuSection
import com.renansantiago.restaurantmenu.model.Restaurant
import com.renansantiago.restaurantmenu.repository.RestaurantRepository
import com.renansantiago.restaurantmenu.presentation.ui.base.BaseViewModel
import com.renansantiago.restaurantmenu.presentation.utils.SingleLiveData

class RestaurantViewModel(
    private val restaurantRepository: RestaurantRepository
) : BaseViewModel() {

    val restaurant = MutableLiveData<Restaurant>()
    val error = SingleLiveData<DefaultException>()

    fun fetchRestaurantDetails() {
        subscribeSingle(
            observable = restaurantRepository.getRestaurantDetails()
                .doOnSubscribe { showLoading.set(true) }
                .doFinally { showLoading.set(false) },
            success = {
                restaurant.postValue(it)
            },
            error = { error.postValue(it) }
        )
    }

    fun getMenuName(): String {
        var menu = "-"

        restaurant.value?.let {
            if (it.menus.isNotEmpty()) {
                menu = it.menus.first().menu_name
            }
        }

        return menu
    }

    fun getPriceWithCurrency(menuItem: MenuItem): String {
        var price = "-"

        if (menuItem.pricing.isNotEmpty()) {
            price = menuItem.pricing.first().priceString
        }

        return price
    }

    fun mapMenuItemsToPutPriceWithCurrency() {
        restaurant.value?.let {
            it.menus.flatMap { sections -> sections.menu_sections }.flatMap { items -> items.menu_items }
                .map { item -> item.priceWithCurrency = getPriceWithCurrency(item) }
        }
    }

    fun getSectionItems(): List<MenuSection> {
        var sections: List<MenuSection> = arrayListOf()
        restaurant.value?.let {
            if (it.menus.isNotEmpty() && it.menus.first().menu_sections.isNotEmpty())
                sections = it.menus.first().menu_sections
        }

        return sections
    }

    fun getInitialMenuItems(): List<MenuItem> {
        var items: List<MenuItem> = arrayListOf()
        val sections = getSectionItems()

        if (sections.isNotEmpty() && sections.first().menu_items.isNotEmpty())
            items = sections.first().menu_items

        return items
    }

    fun getTabMenuSelectedItems(section: String?): List<MenuItem>? {
        restaurant.value?.let {
            return getSectionItems().find { section == it.section_name }?.menu_items
        } ?: run {
            return arrayListOf()
        }
    }

}