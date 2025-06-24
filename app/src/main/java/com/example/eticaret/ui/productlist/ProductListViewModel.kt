package com.example.eticaret.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.eticaret.data.model.Product
import com.example.eticaret.data.repository.ProductRepository
import com.example.eticaret.util.Resource
import kotlinx.coroutines.launch

class ProductListViewModel(private val repository: ProductRepository = ProductRepository()): ViewModel() {
    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> = _products

    fun fetchProducts() {
        _products.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful) {
                    _products.value = Resource.Success(response.body()?.urunler ?: emptyList())
                } else {
                    _products.value = Resource.Error("Failed to load products")
                }
            } catch (e: Exception) {
                _products.value = Resource.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }
} 