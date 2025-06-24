package com.example.eticaret.ui.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eticaret.R
import com.example.eticaret.data.model.Product
import com.example.eticaret.util.loadUrl

class ProductListAdapter(
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val textName: TextView = itemView.findViewById(R.id.textProductName)
        private val textBrand: TextView = itemView.findViewById(R.id.textProductBrand)
        private val textPrice: TextView = itemView.findViewById(R.id.textProductPrice)

        fun bind(product: Product) {
            textName.text = product.ad
            textBrand.text = product.marka
            textPrice.text = "${product.fiyat} ₺"
            imageView.loadUrl("http://kasimadalan.pe.hu/urunler/resimler/${product.resim}")
            itemView.setOnClickListener { onItemClick(product) }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
} 