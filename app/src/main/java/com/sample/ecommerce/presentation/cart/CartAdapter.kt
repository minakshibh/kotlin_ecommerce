package com.sample.ecommerce.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.ecommerce.databinding.ItemCartBinding
import com.sample.ecommerce.domain.model.CartItem

class CartAdapter(
    private val items: List<CartItem>,
    private val onQuantityPlus: (CartItem) -> Unit,
    private val onQuantityMinus: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onQuantityPlus, onQuantityMinus)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        private val binding: ItemCartBinding,
        private val onQuantityPlus: (CartItem) -> Unit,
        private val onQuantityMinus: (CartItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.itemName.text = item.productName
            binding.itemPrice.text = "$${String.format("%.2f", item.productPrice)} each"
            binding.quantityText.text = item.quantity.toString()
            binding.itemSubtotal.text = "$${String.format("%.2f", item.productPrice * item.quantity)}"
            binding.quantityPlus.setOnClickListener { onQuantityPlus(item) }
            binding.quantityMinus.setOnClickListener { onQuantityMinus(item) }
        }
    }
}
