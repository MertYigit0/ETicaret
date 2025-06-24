package com.example.eticaret.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.eticaret.data.model.CartItem
import com.example.eticaret.data.repository.ProductRepository
import com.example.eticaret.util.Resource
import kotlinx.coroutines.launch

class CartViewModel(private val repository: ProductRepository = ProductRepository()): ViewModel() {
    private val _cartItems = MutableLiveData<Resource<List<CartItem>>>()
    val cartItems: LiveData<Resource<List<CartItem>>> = _cartItems
    private val _removeResult = MutableLiveData<Resource<Unit>>()
    val removeResult: LiveData<Resource<Unit>> = _removeResult

    fun fetchCart(kullaniciAdi: String) {
        if (kullaniciAdi.isBlank()) {
            _cartItems.value = Resource.Error("Kullanıcı adı boş olamaz!")
            return
        }
        _cartItems.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getCartProducts(kullaniciAdi)
                if (response.isSuccessful) {
                    val rawItems = response.body()?.urunler_sepeti ?: emptyList()
                    val mergedItems = mergeDuplicateItems(rawItems)
                    _cartItems.value = Resource.Success(mergedItems)
                } else {
                    _cartItems.value = Resource.Error("Failed to load cart")
                }
            } catch (e: Exception) {
                _cartItems.value = Resource.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    private fun mergeDuplicateItems(items: List<CartItem>): List<CartItem> {
        val groupedItems = items.groupBy { "${it.ad}_${it.marka}_${it.fiyat}" }
        return groupedItems.map { (_, groupItems) ->
            if (groupItems.size == 1) {
                groupItems.first()
            } else {
                // Merge duplicates by combining quantities and using the first item's data
                val firstItem = groupItems.first()
                val totalQuantity = groupItems.sumOf { it.siparisAdeti }
                firstItem.copy(siparisAdeti = totalQuantity)
            }
        }
    }

    fun removeFromCart(sepetId: Int, kullaniciAdi: String) {
        _removeResult.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = repository.removeProductFromCart(sepetId, kullaniciAdi)
                if (response.isSuccessful && response.body()?.success == 1) {
                    _removeResult.value = Resource.Success(Unit)
                    fetchCart(kullaniciAdi)
                } else {
                    _removeResult.value = Resource.Error(response.body()?.message ?: "Failed to remove item")
                }
            } catch (e: Exception) {
                _removeResult.value = Resource.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }
} 