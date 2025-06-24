package com.example.eticaret.data.repository

import com.example.eticaret.data.model.Product
import com.example.eticaret.data.model.CartItem
import com.example.eticaret.data.remote.ApiClient
import com.example.eticaret.data.remote.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    private val api = ApiClient.apiService

    suspend fun getAllProducts() = withContext(Dispatchers.IO) {
        api.getAllProducts()
    }

    suspend fun getCartProducts(kullaniciAdi: String) = withContext(Dispatchers.IO) {
        api.getCartProducts(kullaniciAdi)
    }

    suspend fun addProductToCart(
        ad: String,
        resim: String,
        kategori: String,
        fiyat: Int,
        marka: String,
        siparisAdeti: Int,
        kullaniciAdi: String
    ) = withContext(Dispatchers.IO) {
        api.addProductToCart(
            ad, resim, kategori, fiyat, marka, siparisAdeti, kullaniciAdi
        )
    }

    suspend fun removeProductFromCart(sepetId: Int, kullaniciAdi: String) = withContext(Dispatchers.IO) {
        api.removeProductFromCart(sepetId, kullaniciAdi)
    }
} 