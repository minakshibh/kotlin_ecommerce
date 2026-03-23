package com.sample.ecommerce.presentation.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.ecommerce.databinding.ItemOrderBinding
import com.sample.ecommerce.domain.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryAdapter(
    private val orders: List<Order>,
    private val onOrderClick: (Long) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size

    inner class ViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.root.setOnClickListener { onOrderClick(order.id) }
            binding.orderId.text = "Order #${order.id}"
            binding.orderDate.text = dateFormat.format(Date(order.createdAt))
            val itemCount = order.items.sumOf { it.quantity }
            binding.orderSummary.text = "${order.items.size} product(s), $itemCount item(s)"
            binding.orderTotal.text = "Total: $${String.format("%.2f", order.totalAmount)}"
        }
    }
}
