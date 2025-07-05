package com.example.eticaret.ui.cart

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eticaret.R
import com.example.eticaret.data.model.CartItem
import com.example.eticaret.util.loadUrl

class CartAdapter(
    private val onDeleteClick: (CartItem) -> Unit,
    private val onQuantityUpdate: (CartItem, Int) -> Unit
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
        private val buttonMinus: ImageButton = itemView.findViewById(R.id.buttonMinus)
        private val buttonPlus: ImageButton = itemView.findViewById(R.id.buttonPlus)

        fun bind(cartItem: CartItem) {
            textName.text = cartItem.ad
            textBrand.text = cartItem.marka
            textPrice.text = "${cartItem.fiyat} ₺"
            textQuantity.text = cartItem.siparisAdeti.toString()
            imageView.loadUrl("http://kasimadalan.pe.hu/urunler/resimler/${cartItem.resim}")
            
            // Setup delete button
            buttonDelete.setOnClickListener { onDeleteClick(cartItem) }
            
            // Setup quantity controls
            buttonMinus.setOnClickListener {
                val currentQuantity = cartItem.siparisAdeti
                if (currentQuantity > 1) {
                    // Decrease quantity
                    onQuantityUpdate(cartItem, currentQuantity - 1)
                } else {
                    // Show confirmation dialog for removal
                    showRemoveConfirmationDialog(cartItem)
                }
            }
            
            buttonPlus.setOnClickListener {
                val currentQuantity = cartItem.siparisAdeti
                onQuantityUpdate(cartItem, currentQuantity + 1)
            }
        }
        
        private fun showRemoveConfirmationDialog(cartItem: CartItem) {
            val context = itemView.context
            AlertDialog.Builder(context)
                .setTitle("Ürünü Kaldır")
                .setMessage("Bu ürünü sepetten kaldırmak istediğinizden emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    onDeleteClick(cartItem)
                }
                .setNegativeButton("Hayır", null)
                .show()
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean = oldItem.sepetId == newItem.sepetId
    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean = oldItem == newItem
} 