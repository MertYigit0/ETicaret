package com.example.eticaret.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.eticaret.R
import com.example.eticaret.util.Resource
import com.google.android.material.snackbar.Snackbar

class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var textTotal: TextView
    private lateinit var textEmptyCart: TextView
    private lateinit var textEmptyCartTitle: TextView
    private lateinit var textEmptyCartSubtitle: TextView
    private lateinit var buttonStartShopping: Button
    private lateinit var lottieEmptyCart: LottieAnimationView
    private var loadingView: View? = null
    private lateinit var emptyCartContainer: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        textTotal = view.findViewById(R.id.textTotalPrice)
        textEmptyCart = view.findViewById(R.id.textEmptyCart)
        textEmptyCartTitle = view.findViewById(R.id.textEmptyCartTitle)
        textEmptyCartSubtitle = view.findViewById(R.id.textEmptyCartSubtitle)
        buttonStartShopping = view.findViewById(R.id.buttonStartShopping)
        lottieEmptyCart = view.findViewById(R.id.lottieEmptyCart)
        emptyCartContainer = view.findViewById(R.id.emptyCartContainer)
        
        // Load Lottie animation programmatically with error handling
        try {
            lottieEmptyCart.setAnimation(R.raw.empty_cart)
        } catch (e: Exception) {
            // If animation fails to load, hide the Lottie view
            lottieEmptyCart.visibility = View.GONE
        }
        
        // Setup shopping button click handler
        buttonStartShopping.setOnClickListener {
            findNavController().navigate(R.id.productListFragment)
        }
        
        adapter = CartAdapter(
            onDeleteClick = { cartItem ->
                viewModel.removeFromCart(cartItem.sepetId, "mertdev")
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.cartItems.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.loading_view, view as ViewGroup, false)
                        (view as ViewGroup).addView(loadingView)
                    }
                }
                is Resource.Success -> {
                    val items = resource.data ?: emptyList()
                    adapter.submitList(items)
                    
                    if (items.isEmpty()) {
                        // Boş sepet durumu
                        recyclerView.visibility = View.GONE
                        textTotal.visibility = View.GONE
                        textEmptyCart.visibility = View.GONE
                        emptyCartContainer.visibility = View.VISIBLE
                    } else {
                        // Sepette ürün var
                        recyclerView.visibility = View.VISIBLE
                        textTotal.visibility = View.VISIBLE
                        textEmptyCart.visibility = View.GONE
                        emptyCartContainer.visibility = View.GONE
                        adapter.submitList(items)
                        val total = items.sumOf { it.fiyat * it.siparisAdeti }
                        textTotal.text = "Toplam: $total ₺"
                    }
                    
                    loadingView?.let { (view as ViewGroup).removeView(it) }
                    loadingView = null
                }
                is Resource.Error -> {
                    Snackbar.make(view, resource.message ?: "Error", Snackbar.LENGTH_LONG).show()
                    loadingView?.let { (view as ViewGroup).removeView(it) }
                    loadingView = null
                }
            }
        }
        viewModel.removeResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.loading_view, view as ViewGroup, false)
                        (view as ViewGroup).addView(loadingView)
                    }
                }
                is Resource.Success -> {
                    Snackbar.make(view, "Ürün sepetten silindi", Snackbar.LENGTH_SHORT).show()
                    loadingView?.let { (view as ViewGroup).removeView(it) }
                    loadingView = null
                }
                is Resource.Error -> {
                    Snackbar.make(view, resource.message ?: "Error", Snackbar.LENGTH_LONG).show()
                    loadingView?.let { (view as ViewGroup).removeView(it) }
                    loadingView = null
                }
            }
        }
        viewModel.fetchCart("mertdev")
    }
} 