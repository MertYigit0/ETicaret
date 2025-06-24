package com.example.eticaret.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eticaret.R
import com.example.eticaret.data.model.CartItem
import com.example.eticaret.util.loadUrl

class CartAdapter(
    private val onDeleteClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewCartProduct)
        private val textName: TextView = itemView.findViewById(R.id.textCartProductName)
        private val textBrand: TextView = itemView.findViewById(R.id.textCartProductBrand)
        private val textPrice: TextView = itemView.findViewById(R.id.textCartProductPrice)
        private val textQuantity: TextView = itemView.findViewById(R.id.textCartProductQuantity)
        private val buttonDelete: ImageView = itemView.findViewById(R.id.buttonDeleteCartProduct)

        fun bind(cartItem: CartItem) {
            textName.text = cartItem.ad
            textBrand.text = cartItem.marka
            textPrice.text = "${cartItem.fiyat} ₺"
            textQuantity.text = "Adet: ${cartItem.siparisAdeti}"
            imageView.loadUrl("http://kasimadalan.pe.hu/urunler/resimler/${cartItem.resim}")
            buttonDelete.setOnClickListener { onDeleteClick(cartItem) }
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean = oldItem.sepetId == newItem.sepetId
    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean = oldItem == newItem
} 