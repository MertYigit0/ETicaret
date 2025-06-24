package com.example.eticaret.data.remote

import com.example.eticaret.data.model.Product
import com.example.eticaret.data.model.CartItem
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("tumUrunleriGetir.php")
    suspend fun getAllProducts(): Response<ProductListResponse>

    @FormUrlEncoded
    @POST("sepettekiUrunleriGetir.php")
    suspend fun getCartProducts(
        @Field("kullaniciAdi") kullaniciAdi: String
    ): Response<CartListResponse>

    @FormUrlEncoded
    @POST("sepeteUrunEkle.php")
    suspend fun addProductToCart(
        @Field("ad") ad: String,
        @Field("resim") resim: String,
        @Field("kategori") kategori: String,
        @Field("fiyat") fiyat: Int,
        @Field("marka") marka: String,
        @Field("siparisAdeti") siparisAdeti: Int,
        @Field("kullaniciAdi") kullaniciAdi: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("sepettenUrunSil.php")
    suspend fun removeProductFromCart(
        @Field("sepetId") sepetId: Int,
        @Field("kullaniciAdi") kullaniciAdi: String
    ): Response<ApiResponse>
}

// Response wrappers

data class ProductListResponse(
    val urunler: List<Product>?
)

data class CartListResponse(
    val urunler_sepeti: List<CartItem>?,
    val success: Int?
)

data class ApiResponse(
    val success: Int,
    val message: String
) 