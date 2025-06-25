package com.example.eticaret.ui.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eticaret.R
import com.example.eticaret.data.model.Product
import com.example.eticaret.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ProductListFragment : Fragment() {
    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private var loadingView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        adapter = ProductListAdapter { product ->
            val productJson = Gson().toJson(product)
            val bundle = Bundle().apply {
                putString("product", productJson)
            }
            findNavController().navigate(R.id.action_productList_to_productDetail, bundle)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.loading_view, view as ViewGroup, false)
                        (view as ViewGroup).addView(loadingView)
                    }
                }
                is Resource.Success -> {
                    adapter.submitList(resource.data ?: emptyList())
                    loadingView?.let { (view as ViewGroup).removeView(it) }
                    loadingView = null
                }
                is Resource.Error -> {
                    Snackbar.make(view, resource.message ?: "Error", Snackbar.LENGTH_LONG).show()
                    loadingView?.let { (view as ViewGroup).removeView(it) }
                    loadingView = null
                }
            }
        })
        viewModel.fetchProducts()
    }
} 