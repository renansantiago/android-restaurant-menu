package com.renansantiago.restaurantmenu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.renansantiago.restaurantmenu.exception.DefaultException
import com.renansantiago.restaurantmenu.model.*
import com.renansantiago.restaurantmenu.presentation.restaurant.RestaurantViewModel
import com.renansantiago.restaurantmenu.repository.RestaurantRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

class RestaurantViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    companion object {
        private const val MOCK_DEFAULT_VALUE = "-"
        private const val MOCK_SECTION_NAME = "Smoothies"
        private val MOCK_MENU_ITEM = MenuItem(
            "Tropical Storm Smoothie",
            "Mango, coconut milk and banana.",
            "6.95",
            listOf(
                Pricing(
                    "$6.95"
                )
            ),
            "$6.95"
        )
        private val MOCK_MENU_ITEM_PRICE_EMPTY = MenuItem(
            "Tropical Storm Smoothie",
            "Mango, coconut milk and banana.",
            "6.95",
            listOf(),
            "$6.95"
        )
        private val MOCK_RESTAURANT = Restaurant(
            "Dolce Vita",
            "(718) 376-8800",
            "https://restaurant.com",
            "00:00",
            listOf(
                Menu(
                    "Menu 1",
                    listOf(
                        MenuSection(
                            "Smoothies",
                            "Smoothies",
                            listOf(
                                MenuItem(
                                    "Tropical Storm Smoothie",
                                    "Mango, coconut milk and banana.",
                                    "6.95",
                                    listOf(
                                        Pricing(
                                            "$6.95"
                                        )
                                    ),
                                    null
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private val restaurantRepositoryMock = mock<RestaurantRepository>()
    private val observerRestaurantMock: Observer<Restaurant> = mock()
    private val observerErrorMock: Observer<DefaultException> = mock()

    lateinit var viewModel: RestaurantViewModel

    @Before
    fun `Setup test`() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        viewModel = RestaurantViewModel(restaurantRepositoryMock)
    }

    @Test
    fun `Test fetchRestaurantDetails() when success`() {
        //Prepare
        val subjectDelay = PublishSubject.create<Restaurant>()
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT).delaySubscription(subjectDelay))

        viewModel.restaurant.observeForever(observerRestaurantMock)

        //Action
        viewModel.fetchRestaurantDetails()

        //Test
        `Test showLoading`(subjectDelay)
        verify(observerRestaurantMock).onChanged(MOCK_RESTAURANT)
    }

    @Test
    fun `Test fetchRestaurantDetails() when error`() {
        //Prepare
        val mockException = DefaultException("Mock error message")
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.error(mockException))

        viewModel.error.observeForever(observerErrorMock)

        //Action
        viewModel.fetchRestaurantDetails()

        //Test
        verify(observerErrorMock).onChanged(mockException)
    }

    @Test
    fun `Test getMenuName() should get first menu name of array menus when has value and was found`() {
        //Prepare
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT))

        //Action
        viewModel.fetchRestaurantDetails()
        val menu = viewModel.getMenuName()

        //Test
        assertEquals(MOCK_RESTAURANT.menus.first().menu_name, menu)
    }

    @Test
    fun `Test getMenuName() should return a default value when fetch has a problem`() {
        //Prepare
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT))

        //Action
        //fetchRestaurantDetails not called to force the error
        val menu = viewModel.getMenuName()

        //Test
        assertEquals(MOCK_DEFAULT_VALUE, menu)
    }

    @Test
    fun `Test getPriceWithCurrency() should get priceString inside the first item of pricing array`() {
        //Action
        val pricing = viewModel.getPriceWithCurrency(MOCK_MENU_ITEM)

        //Test
        assertEquals(MOCK_MENU_ITEM.pricing.first().priceString, pricing)
    }

    @Test
    fun `Test getPriceWithCurrency() should return a default value when menuItem is empty`() {
        //Action
        val price = viewModel.getPriceWithCurrency(MOCK_MENU_ITEM_PRICE_EMPTY)

        //Test
        assertEquals(MOCK_DEFAULT_VALUE, price)
    }

    @Test
    fun `Test mapMenuItemsToPutPriceWithCurrency() should put priceWithCurrency prop inside menu_items from pricing array`() {
        //Prepare
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT))

        //Action
        viewModel.fetchRestaurantDetails()
        viewModel.mapMenuItemsToPutPriceWithCurrency()

        //Test
        assertNotNull(MOCK_RESTAURANT.menus.first().menu_sections.first().menu_items.first().priceWithCurrency)
    }

    @Test
    fun `Test getSectionItems() should return menu sections from first item of menu array`() {
        //Prepare
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT))

        //Action
        viewModel.fetchRestaurantDetails()
        val sections = viewModel.getSectionItems()

        //Test
        assertEquals(MOCK_RESTAURANT.menus.first().menu_sections, sections)
    }

    @Test
    fun `Test getInitialMenuItems() should return first menu items at startup`() {
        //Prepare
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT))

        //Action
        viewModel.fetchRestaurantDetails()
        val items = viewModel.getInitialMenuItems()

        //Test
        assertEquals(MOCK_RESTAURANT.menus.first().menu_sections.first().menu_items, items)
    }

    @Test
    fun `Test getTabMenuSelectedItems() should find a tab through a section`() {
        //Prepare
        `when`(restaurantRepositoryMock.getRestaurantDetails())
            .thenReturn(Single.just(MOCK_RESTAURANT))

        //Action
        viewModel.fetchRestaurantDetails()
        val sections = viewModel.getSectionItems()
        val items = viewModel.getTabMenuSelectedItems(MOCK_SECTION_NAME)

        //Test
        assertEquals(sections.find { MOCK_SECTION_NAME == it.section_name }?.menu_items, items)
    }

    private fun `Test showLoading`(subjectDelay: PublishSubject<Restaurant>) {
        assertThat(viewModel.showLoading.get(), `is`(true))

        subjectDelay.onComplete()

        assertThat(viewModel.showLoading.get(), `is`(false))
    }

}