package com.example.eticaret.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.eticaret.data.repository.ProductRepository
import com.example.eticaret.util.Resource
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val repository: ProductRepository = ProductRepository()): ViewModel() {
    private val _addToCartResult = MutableLiveData<Resource<Unit>>()
    val addToCartResult: LiveData<Resource<Unit>> = _addToCartResult

    fun addToCart(
        ad: String,
        resim: String,
        kategori: String,
        fiyat: Int,
        marka: String,
        siparisAdeti: Int,
        kullaniciAdi: String
    ) {
        _addToCartResult.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = repository.addProductToCart(
                    ad, resim, kategori, fiyat, marka, siparisAdeti, kullaniciAdi
                )
                if (response.isSuccessful && response.body()?.success == 1) {
                    _addToCartResult.value = Resource.Success(Unit)
                } else {
                    _addToCartResult.value = Resource.Error(response.body()?.message ?: "Failed to add to cart")
                }
            } catch (e: Exception) {
                _addToCartResult.value = Resource.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }
} 