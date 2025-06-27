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

    private var allProducts: List<Product> = emptyList()
    private var searchQuery: String = ""
    private var selectedCategory: String = "All"

    fun fetchProducts() {
        _products.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful) {
                    allProducts = response.body()?.urunler ?: emptyList()
                    filterProducts()
                } else {
                    _products.value = Resource.Error("Failed to load products")
                }
            } catch (e: Exception) {
                _products.value = Resource.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery = query
        filterProducts()
    }

    fun setCategory(category: String) {
        selectedCategory = category
        filterProducts()
    }

    private fun filterProducts() {
        var filtered = allProducts
        if (selectedCategory != "All") {
            filtered = filtered.filter { it.kategori.equals(selectedCategory, ignoreCase = true) }
        }
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.ad.contains(searchQuery, ignoreCase = true) ||
                it.marka.contains(searchQuery, ignoreCase = true)
            }
        }
        _products.value = Resource.Success(filtered)
    }
} 