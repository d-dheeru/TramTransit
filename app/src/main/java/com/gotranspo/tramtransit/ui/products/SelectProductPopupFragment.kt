package com.gotranspo.tramtransit.ui.products

import SelectProductPopupViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.adapters.product.SelectProductPopupPagerAdapter
import com.gotranspo.tramtransit.adapters.product.TAB_POSITION_TAB1
import com.gotranspo.tramtransit.adapters.product.TAB_POSITION_TAB2
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.databinding.FragmentSelectProductPopupBinding
import com.gotranspo.tramtransit.utils.NumberUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectProductPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentSelectProductPopupBinding
    private val viewModel: SelectProductPopupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectProductPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setUpTabLayoutMediator()
    }

    private fun setupViewPager() {
        val finalTotalListener = FinalTotalListener()
        val adapter =
            SelectProductPopupPagerAdapter(childFragmentManager, lifecycle, finalTotalListener)
        binding.productsViewPager.adapter = adapter
    }

    private fun setUpTabLayoutMediator() {
        TabLayoutMediator(binding.tabLayout, binding.productsViewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    // TODO: Use this when proper icons are available
    private fun getTabIcon(position: Int): Int {
        return when (position) {
            TAB_POSITION_TAB1 -> R.drawable.coffee_tab_selector
            TAB_POSITION_TAB2 -> R.drawable.meals_tab_selector
            else -> R.drawable.coffee_tab_selector
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            TAB_POSITION_TAB1 -> "COFFEE"
            TAB_POSITION_TAB2 -> "MEAL"
            else -> "Drink"
        }
    }


    init {
//        ProductFirstFragment()
    }

    private inner class FinalTotalListener : ProductsFragment.FinalTotalListener {
        var coffeeItemTotalCost = mutableMapOf<String, Double>()
        var mealItemTotalCost = mutableMapOf<String, Double>()
        var coolDrinkItemTotalCost = mutableMapOf<String, Double>()

        override fun finalTotal(
            item: ProductItemData,
            sum: Double
        ) {

            when (item.itemType.lowercase()) {
                "coffee" -> {
                    coffeeItemTotalCost[item.productGuid] = sum
                }

                "meal" -> {
                    mealItemTotalCost[item.productGuid] = sum
                }

                else -> {
                    coolDrinkItemTotalCost[item.productGuid] = sum
                }

            }

            val finalTotal =
                coffeeItemTotalCost.values.sum() + mealItemTotalCost.values.sum() + coolDrinkItemTotalCost.values.sum()

            "${item.currency} ${NumberUtils.formatNumberWithLocale(finalTotal)}".also {
                binding.finalAmountValue.text = it
            }
        }

    }

//    override fun finalTotal(value: Double) {
//        binding.finalAmountText.text = value.toString()
//    }
    //    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SelectProductPopupViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}