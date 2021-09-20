package com.renansantiago.restaurantmenu.presentation.restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.renansantiago.restaurantmenu.R
import com.renansantiago.restaurantmenu.databinding.ItemMenuBinding
import com.renansantiago.restaurantmenu.model.MenuItem

class MenuItemRecyclerAdapter : RecyclerView.Adapter<MenuItemRecyclerAdapter.ViewHolder>() {

    private val menuItems = mutableListOf<MenuItem>()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_menu,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = menuItems.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    fun notifyChanged(menuItems: List<MenuItem>) {
        this.menuItems.clear()
        this.menuItems.addAll(menuItems)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: MenuItem) {
            binding.menuItem = menuItem

            binding.executePendingBindings()
        }
    }
}