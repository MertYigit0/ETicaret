package com.example.eticaret.ui.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eticaret.R
import com.example.eticaret.data.model.Product
import com.example.eticaret.util.Resource
import com.example.eticaret.util.loadUrl
import com.google.android.material.snackbar.Snackbar

class ProductDetailFragment : Fragment() {
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()
    private lateinit var imageView: ImageView
    private lateinit var textName: TextView
    private lateinit var textBrand: TextView
    private lateinit var textCategory: TextView
    private lateinit var textPrice: TextView
    private lateinit var editQuantity: EditText
    private lateinit var buttonAddToCart: Button
    private var quantity: Int = 1
    private var loadingView: View? = null
    private lateinit var container: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageViewProductDetail)
        textName = view.findViewById(R.id.textProductNameDetail)
        textBrand = view.findViewById(R.id.textProductBrandDetail)
        textCategory = view.findViewById(R.id.textProductCategoryDetail)
        textPrice = view.findViewById(R.id.textProductPriceDetail)
        editQuantity = view.findViewById(R.id.editTextQuantity)
        buttonAddToCart = view.findViewById(R.id.buttonAddToCart)
        container = view.findViewById(R.id.productDetailContainer)

        val product = args.product
        textName.text = product.ad
        textBrand.text = product.marka
        textCategory.text = product.kategori
        textPrice.text = "${product.fiyat} ₺"
        imageView.loadUrl("http://kasimadalan.pe.hu/urunler/resimler/${product.resim}")
        editQuantity.setText(quantity.toString())

        editQuantity.doAfterTextChanged {
            val value = it.toString().toIntOrNull() ?: 1
            quantity = if (value < 1) 1 else value
            if (value < 1) editQuantity.setText("1")
        }

        buttonAddToCart.setOnClickListener {
            if (product.ad.isNotBlank() && product.resim.isNotBlank() && product.kategori.isNotBlank() &&
                product.fiyat != null && product.marka.isNotBlank() && quantity > 0) {
                viewModel.addToCart(
                    ad = product.ad,
                    resim = product.resim,
                    kategori = product.kategori,
                    fiyat = product.fiyat,
                    marka = product.marka,
                    siparisAdeti = quantity,
                    kullaniciAdi = "mertdev"
                )
            } else {
                Snackbar.make(view, "Tüm alanlar dolu ve geçerli olmalı!", Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.addToCartResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.loading_view, container, false)
                        container.addView(loadingView)
                    }
                }
                is Resource.Success -> {
                    Snackbar.make(view, "Added to cart!", Snackbar.LENGTH_SHORT).show()
                    loadingView?.let { container.removeView(it) }
                    loadingView = null
                    findNavController().popBackStack()
                }
                is Resource.Error -> {
                    Snackbar.make(view, resource.message ?: "Error", Snackbar.LENGTH_LONG).show()
                    loadingView?.let { container.removeView(it) }
                    loadingView = null
                }
            }
        }
    }
} 