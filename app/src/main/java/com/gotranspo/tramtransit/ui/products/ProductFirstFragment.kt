package com.gotranspo.tramtransit.ui.products

import CenterZoomLayoutManager
import ImageWithTextAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gotranspo.tramtransit.databinding.FragmentProductFirstBinding
import com.gotranspo.tramtransit.viewmodels.ProductFirstViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFirstFragment : Fragment() {

    private lateinit var binding: FragmentProductFirstBinding
    private val viewModel: ProductFirstViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductFirstBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModelRes = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeProductItems()
    }

    private fun setupRecyclerView() {
        val adapter = ImageWithTextAdapter(emptyList()) // Initialize adapter with an empty list
        binding.productHolderRecyclerView.adapter = adapter

        val orientation = LinearLayoutManager.HORIZONTAL // Set the orientation to horizontal
        val customLayoutManager = CenterZoomLayoutManager(requireContext(), orientation)
        binding.productHolderRecyclerView.layoutManager = customLayoutManager
    }

    private fun observeProductItems() {
        viewModel.getProducts()
        viewModel.productItemsLiveData.observe(viewLifecycleOwner, Observer { productItems ->
            // Update the adapter with the new list of product items
            (binding.productHolderRecyclerView.adapter as? ImageWithTextAdapter)?.apply {
                setItems(productItems)
            }
        })
    }
}
