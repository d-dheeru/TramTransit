package com.gotranspo.tramtransit.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.gotranspo.tramtransit.adapters.product.SelectProductPopupPagerAdapter
import com.gotranspo.tramtransit.databinding.FragmentSelectProductPopupBinding
import com.gotranspo.tramtransit.viewmodels.SelectProductPopupViewModel
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
        TabLayoutMediator(binding.tabLayout, binding.productsViewPager){ tab, position ->
            tab.text = when(position) {
                0 -> "Coffee"
                1 -> "Meal"
                else -> "Other"
            }
        }.attach()
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SelectProductPopupViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}