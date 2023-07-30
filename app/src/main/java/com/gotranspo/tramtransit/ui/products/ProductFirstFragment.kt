package com.gotranspo.tramtransit.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.adapters.product.ImageWithTextAdapter
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.databinding.FragmentProductHolderBinding

class ProductFirstFragment : Fragment() {

    private lateinit var binding: FragmentProductHolderBinding
    private val itemDataList: List<ProductItemData> = listOf(
        ProductItemData(
            R.drawable.shop,
            "Ice coffee",
            "small",
            2.59,
            "$",
            true,
            9
        ),
        ProductItemData(
            R.drawable.map,
            "Ice coffee",
            "medium",
            3.59,
            "$",
            true,
            6
        ),
        ProductItemData(
            R.drawable.blank_avatar,
            "Ice coffee",
            "large",
            4.59,
            "$",
            false,
            3
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductHolderBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = ImageWithTextAdapter(itemDataList)
        binding.productHolderRecyclerView.adapter = adapter

        val customLayoutManager = CenterZoomLayoutManager(requireContext())
        binding.productHolderRecyclerView.layoutManager = customLayoutManager
    }
}