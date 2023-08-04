package com.gotranspo.tramtransit.ui.products

import CenterZoomLayoutManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gotranspo.tramtransit.adapters.product.ImageWithTextAdapter
import com.gotranspo.tramtransit.adapters.product.TAB_POSITION_TAB1
import com.gotranspo.tramtransit.adapters.product.TAB_POSITION_TAB2
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.databinding.FragmentProductsBinding
import com.gotranspo.tramtransit.utils.NumberUtils
import com.gotranspo.tramtransit.viewmodels.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment(
    private val position: Int,
    private val finalTotalListener: FinalTotalListener
) : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(
            inflater,
            container,
            false
        )

        when (position) {
            TAB_POSITION_TAB1 -> {
                binding.whiteTextView.text = "Coffee total:"
            }

            TAB_POSITION_TAB2 -> {
                binding.whiteTextView.text = "Meal total:"
            }

            else -> {
                binding.whiteTextView.text = "Drinks total:"
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getProducts(position)
        observeProductItems()
    }

    private fun setupRecyclerView() {
        // Click listener for the left and right images
        val itemClickListener = ImageItemClickListener()

        val adapter = ImageWithTextAdapter(emptyList(), itemClickListener)

        binding.productHolderRecyclerView.apply {
            this.adapter = adapter
            layoutManager =
                CenterZoomLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL)
        }
    }

    private fun observeProductItems() {
        viewModel.productItemsLiveData.observe(viewLifecycleOwner, Observer { productItems ->
            // Update the adapter with the new list of product items
            (binding.productHolderRecyclerView.adapter as? ImageWithTextAdapter)?.apply {
                setItems(productItems)
            }
        })
    }


    private inner class ImageItemClickListener : ImageWithTextAdapter.OnItemClickListener {
        // TODO: Make it scalable
        var coffeeItemTotalCost = mutableMapOf<String, Double>()
        var mealItemTotalCost = mutableMapOf<String, Double>()
        var coolDrinkItemTotalCost = mutableMapOf<String, Double>()
        override fun onLeftImageClick(item: ProductItemData, itemAddedCount: Int) {
            setTotalCostText(item, itemAddedCount)
        }

        override fun onRightImageClick(item: ProductItemData, itemAddedCount: Int) {
            setTotalCostText(item, itemAddedCount)
        }

        private fun setTotalCostText(item: ProductItemData, itemAddedCount: Int) {
            var sum = 0.0
            when (item.itemType.lowercase()) {
                "coffee" -> {
                    coffeeItemTotalCost[item.productGuid.toString()] =
                        (itemAddedCount * item.itemCost)
                    sum = coffeeItemTotalCost.values.sum()
                }

                "meal" -> {
                    mealItemTotalCost[item.productGuid.toString()] =
                        (itemAddedCount * item.itemCost)
                    sum = mealItemTotalCost.values.sum()
                }

                else -> {
                    coolDrinkItemTotalCost[item.productGuid.toString()] =
                        (itemAddedCount * item.itemCost)
                    sum = coolDrinkItemTotalCost.values.sum()
                }

            }

            "${item.currency} ${NumberUtils.formatNumberWithLocale(sum)}".also {
                binding.whiteTextView2.text = it
            }

            finalTotalListener.finalTotal(
                item,
                sum
            )
        }

    }

    interface FinalTotalListener {
        fun finalTotal(
            item: ProductItemData,
            sum: Double
        )
    }

}
