package com.renansantiago.restaurantmenu.presentation.restaurant

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.renansantiago.restaurantmenu.R
import com.renansantiago.restaurantmenu.databinding.ActivityRestaurantBinding
import com.renansantiago.restaurantmenu.model.MenuItem
import com.renansantiago.restaurantmenu.model.MenuSection
import com.renansantiago.restaurantmenu.presentation.ui.base.BaseActivity
import com.renansantiago.restaurantmenu.presentation.ui.extensions.observeNotNull
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantActivity : BaseActivity<ActivityRestaurantBinding>() {

    val viewModel by viewModel<RestaurantViewModel>()
    private val menuItemRecyclerAdapter by inject<MenuItemRecyclerAdapter>()

    override fun getLayoutRes(): Int = R.layout.activity_restaurant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        setupRecyclerView()
        setupTabMenu()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchRestaurantDetails()
    }

    private fun setupRecyclerView() {
        binding.rvMenuItems.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvMenuItems.adapter = menuItemRecyclerAdapter
    }

    private fun setupTabMenu() {
        binding.tabMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val section = tab?.text.toString()
                val menuItems = viewModel.getTabMenuSelectedItems(section)
                menuItems?.let { setMenuItemsListData(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setMenuName() {
        val menu = viewModel.getMenuName()
        binding.textViewMenu.text = menu
    }

    private fun setTabMenuData(sections: List<MenuSection>) {
        sections.forEach { section ->
            binding.tabMenu.addTab(binding.tabMenu.newTab().setText(section.section_name))
        }
        binding.tabMenu.getTabAt(0)?.select()
    }

    private fun setMenuItemsListData(items: List<MenuItem>) {
        menuItemRecyclerAdapter.notifyChanged(items)
    }

    private fun observeData() {
        viewModel.restaurant.observeNotNull(this) {
            binding.restaurant = it

            viewModel.mapMenuItemsToPutPriceWithCurrency()

            val sectionItems: List<MenuSection> = viewModel.getSectionItems()
            val menuItems: List<MenuItem> = viewModel.getInitialMenuItems()

            setMenuName()
            setTabMenuData(sectionItems)
            setMenuItemsListData(menuItems)
        }

        viewModel.error.observeNotNull(this) {
            binding.textViewError.text = getString(R.string.default_error)
            binding.textViewToolbarTitle.text = "-"
            binding.textViewMenu.text = "-"
        }
    }
}