package com.example.eticaret.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val sepetId: Int,
    //val urun_id: Int,
    val ad: String,
    val fiyat: Int,
    val resim: String,
    val marka: String,
    val kategori: String,
    val siparisAdeti: Int,
    val kullaniciAdi: String
) : Parcelable 