package com.sample.ecommerce.presentation.orderdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.ecommerce.databinding.ItemOrderDetailBinding
import com.sample.ecommerce.domain.model.OrderItem

class OrderDetailItemsAdapter(private val items: List<OrderItem>) : RecyclerView.Adapter<OrderDetailItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(private val binding: ItemOrderDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.itemProductName.text = item.productName
            binding.itemQuantityPrice.text = "${item.quantity} × $${String.format("%.2f", item.unitPrice)}"
            binding.itemSubtotal.text = "$${String.format("%.2f", item.unitPrice * item.quantity)}"
        }
    }
}
