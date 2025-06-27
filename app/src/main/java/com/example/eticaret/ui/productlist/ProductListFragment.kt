package com.example.eticaret.ui.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eticaret.R
import com.example.eticaret.data.model.Product
import com.example.eticaret.util.Resource
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class ProductListFragment : Fragment() {
    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var chipGroupCategories: ChipGroup
    private lateinit var buttonSort: ImageButton
    private var loadingView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        editTextSearch = view.findViewById(R.id.editTextSearch)
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories)
        buttonSort = view.findViewById(R.id.buttonSort)
        
        // Setup adapter
        adapter = ProductListAdapter { product ->
            val productJson = Gson().toJson(product)
            val bundle = Bundle().apply {
                putString("product", productJson)
            }
            findNavController().navigate(R.id.action_productList_to_productDetail, bundle)
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        // Setup search functionality
        editTextSearch.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text?.toString() ?: "")
        }

        // Setup category filter
        chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            val category = when (selectedChip?.id) {
                R.id.chipAll -> "All"
                R.id.chipTeknoloji -> "Teknoloji"
                R.id.chipAksesuar -> "Aksesuar"
                R.id.chipKozmetik -> "Kozmetik"
                else -> "All"
            }
            viewModel.setCategory(category)
        }

        // Setup sorting functionality
        buttonSort.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), buttonSort)
            popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                val sorting = when (item.itemId) {
                    R.id.action_sort_default -> "default"
                    R.id.action_sort_price_asc -> "price_asc"
                    R.id.action_sort_price_desc -> "price_desc"
                    R.id.action_sort_name_asc -> "name_asc"
                    R.id.action_sort_name_desc -> "name_desc"
                    else -> "default"
                }
                viewModel.setSortType(sorting)
                // Sıralama yapıldıktan sonra en üste kaydır
                recyclerView.post {
                    recyclerView.smoothScrollToPosition(0)
                }
                true
            }
            popupMenu.show()
        }

        // Observe original products for loading states
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