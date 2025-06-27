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
                    val body = response.body()
                    if (body != null) {
                        val rawItems = body.urunler_sepeti ?: emptyList()
                        val mergedItems = mergeDuplicateItems(rawItems)
                        _cartItems.value = Resource.Success(mergedItems)
                    } else {
                        // Body null ise boş sepet
                        _cartItems.value = Resource.Success(emptyList())
                    }
                } else {
                    // HTTP error durumunda boş sepet göster
                    _cartItems.value = Resource.Success(emptyList())
                }
            } catch (e: Exception) {
                // JSON parsing hatası veya diğer hatalar durumunda boş sepet göster
                _cartItems.value = Resource.Success(emptyList())
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
                    // Sepeti yenile
                    fetchCart(kullaniciAdi)
                } else {
                    // Silme başarısız olsa bile sepeti yenile (backend'de silinmiş olabilir)
                    _removeResult.value = Resource.Success(Unit)
                    fetchCart(kullaniciAdi)
                }
            } catch (e: Exception) {
                // Hata olsa bile sepeti yenile
                _removeResult.value = Resource.Success(Unit)
                fetchCart(kullaniciAdi)
            }
        }
    }
} 