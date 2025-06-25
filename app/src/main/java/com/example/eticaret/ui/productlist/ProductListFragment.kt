package com.example.eticaret.ui.productlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eticaret.R
import com.example.eticaret.data.model.Product
import com.example.eticaret.util.Resource
import com.google.android.material.chip.ChipGroup
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
        val editTextSearch = view.findViewById<EditText>(R.id.editTextSearch)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupCategories)
        adapter = ProductListAdapter { product ->
            val productJson = Gson().toJson(product)
            val bundle = Bundle().apply {
                putString("product", productJson)
            }
            findNavController().navigate(R.id.action_productList_to_productDetail, bundle)
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        // Search logic
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Category filter logic
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val category = when (checkedId) {
                R.id.chipAll -> "All"
                R.id.chipAksesuar -> "Aksesuar"
                R.id.chipTeknoloji -> "Teknoloji"
                R.id.chipKozmetik -> "Kozmetik"
                else -> "All"
            }
            viewModel.setCategory(category)
        }

        viewModel.filteredProducts.observe(viewLifecycleOwner) { filteredList ->
            adapter.submitList(filteredList)
        }

        viewModel.products.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(requireContext()).inflate(R.layout.loading_view, view as ViewGroup, false)
                        (view as ViewGroup).addView(loadingView)
                    }
                }
                is Resource.Success -> {
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