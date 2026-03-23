package com.sample.ecommerce.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.ecommerce.domain.model.Category
import com.sample.ecommerce.domain.model.Product
import com.sample.ecommerce.databinding.ItemCategoryBinding
import com.sample.ecommerce.databinding.ItemProductBinding

class CategoryAdapter(
    private val productsByCategory: Map<Long, List<Product>>,
    private val quantities: Map<Long, Int>,
    private val onQuantityPlus: (Long) -> Unit,
    private val onQuantityMinus: (Long) -> Unit,
    private val onAddToCart: (Long) -> Unit,
    private val onProductClick: (Long) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.categoryName.text = category.name
            val products = productsByCategory[category.id] ?: emptyList()
            if (binding.productsRecyclerView.layoutManager == null) {
                binding.productsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            }
            binding.productsRecyclerView.adapter = ProductAdapter(
                products = products,
                quantities = quantities,
                onQuantityPlus = onQuantityPlus,
                onQuantityMinus = onQuantityMinus,
                onAddToCart = onAddToCart,
                onProductClick = onProductClick
            )
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(old: Category, new: Category) = old.id == new.id
        override fun areContentsTheSame(old: Category, new: Category) = old == new
    }
}

class ProductAdapter(
    private val products: List<Product>,
    private val quantities: Map<Long, Int>,
    private val onQuantityPlus: (Long) -> Unit,
    private val onQuantityMinus: (Long) -> Unit,
    private val onAddToCart: (Long) -> Unit,
    private val onProductClick: (Long) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.root.setOnClickListener { onProductClick(product.id) }
            binding.productName.text = product.name
            binding.productDescription.text = product.description
            binding.productPrice.text = "$${String.format("%.2f", product.price)}"
            val qty = quantities[product.id] ?: 0
            binding.quantityText.text = qty.toString()
            binding.quantityPlus.setOnClickListener { onQuantityPlus(product.id) }
            binding.quantityMinus.setOnClickListener { onQuantityMinus(product.id) }
            binding.addToCartButton.setOnClickListener { onAddToCart(product.id) }
        }
    }
}
