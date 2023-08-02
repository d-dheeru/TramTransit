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
import com.gotranspo.tramtransit.adapters.product.TAB_POSITION_TAB3
import com.gotranspo.tramtransit.databinding.FragmentSelectProductPopupBinding
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
        val adapter = SelectProductPopupPagerAdapter(childFragmentManager, lifecycle)
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
            TAB_POSITION_TAB3 -> R.drawable.coffee_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            TAB_POSITION_TAB1 -> "COFFEE"
            TAB_POSITION_TAB2 -> "MEAL"
            TAB_POSITION_TAB3 -> "OTHER"
            else -> null
        }
    }


    init {
//        ProductFirstFragment()
    }
    //    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SelectProductPopupViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}