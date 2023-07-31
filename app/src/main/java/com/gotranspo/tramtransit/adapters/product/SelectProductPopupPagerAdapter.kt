package com.gotranspo.tramtransit.adapters.product

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gotranspo.tramtransit.ui.products.ProductFirstFragment

const val TAB_POSITION_TAB1 = 0
const val TAB_POSITION_TAB2 = 1
const val TAB_POSITION_TAB3 = 2

class SelectProductPopupPagerAdapter(
    fm: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fm, lifeCycle) {

    private val tabFragmentCreator: Map<Int, () -> Fragment> = mapOf(
        TAB_POSITION_TAB1 to { ProductFirstFragment() },
        TAB_POSITION_TAB2 to { Fragment() },
        TAB_POSITION_TAB3 to { Fragment() },
    )

    override fun getItemCount(): Int {
        return tabFragmentCreator.size
    }

    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            TAB_POSITION_TAB1 -> Fragment()
//            TAB_POSITION_TAB2 -> Fragment()
//            else -> throw IllegalArgumentException("Invalid tab position")
//        }

        return tabFragmentCreator[position]?.invoke()
            ?: throw IllegalArgumentException("Invalid tab position")
    }
}