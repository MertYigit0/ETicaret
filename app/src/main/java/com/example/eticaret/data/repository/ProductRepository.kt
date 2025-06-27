package com.example.eticaret.data.repository

import com.example.eticaret.data.model.Product
import com.example.eticaret.data.model.CartItem
import com.example.eticaret.data.remote.ApiClient
import com.example.eticaret.data.remote.ApiResponse
import com.example.eticaret.data.remote.CartListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    private val api = ApiClient.apiService

    suspend fun getAllProducts() = withContext(Dispatchers.IO) {
        api.getAllProducts()
    }

    suspend fun getCartProducts(kullaniciAdi: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getCartProducts(kullaniciAdi)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    // Başarılı response, cart items'ları döndür
                    response
                } else {
                    // Body null ise boş sepet olarak kabul et
                    val emptyResponse = retrofit2.Response.success(
                        CartListResponse(urunler_sepeti = emptyList(), success = 1)
                    )
                    emptyResponse
                }
            } else {
                // HTTP error durumunda boş sepet döndür
                val emptyResponse = retrofit2.Response.success(
                    CartListResponse(urunler_sepeti = emptyList(), success = 0)
                )
                emptyResponse
            }
        } catch (e: Exception) {
            // JSON parsing hatası veya network hatası durumunda boş sepet döndür
            val emptyResponse = retrofit2.Response.success(
                CartListResponse(urunler_sepeti = emptyList(), success = 0)
            )
            emptyResponse
        }
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
        try {
            // Check if product already exists in cart
            val cartResponse = api.getCartProducts(kullaniciAdi)
            if (cartResponse.isSuccessful) {
                val cartItems = cartResponse.body()?.urunler_sepeti ?: emptyList()
                val existingItem = cartItems.find { 
                    it.ad == ad && it.marka == marka && it.fiyat == fiyat 
                }
                
                if (existingItem != null) {
                    // Product exists, update quantity by deleting and re-adding
                    val deleteResponse = api.removeProductFromCart(existingItem.sepetId, kullaniciAdi)
                    if (deleteResponse.isSuccessful) {
                        val newQuantity = existingItem.siparisAdeti + siparisAdeti
                        api.addProductToCart(ad, resim, kategori, fiyat, marka, newQuantity, kullaniciAdi)
                    } else {
                        // If delete fails, just add normally
                        api.addProductToCart(ad, resim, kategori, fiyat, marka, siparisAdeti, kullaniciAdi)
                    }
                } else {
                    // Product doesn't exist, add new item
                    api.addProductToCart(ad, resim, kategori, fiyat, marka, siparisAdeti, kullaniciAdi)
                }
            } else {
                // If cart fetch fails, add normally
                api.addProductToCart(ad, resim, kategori, fiyat, marka, siparisAdeti, kullaniciAdi)
            }
        } catch (e: Exception) {
            // If any error occurs, fallback to normal add
            api.addProductToCart(ad, resim, kategori, fiyat, marka, siparisAdeti, kullaniciAdi)
        }
    }

    suspend fun removeProductFromCart(sepetId: Int, kullaniciAdi: String) = withContext(Dispatchers.IO) {
        api.removeProductFromCart(sepetId, kullaniciAdi)
    }
} 