package com.gotranspo.tramtransit.remote

import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.remote.api.ApiService
import javax.inject.Inject

class ProductsRepo @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    suspend fun getProducts() =
        safeapicall {
            listOf(
                ProductItemData(R.drawable.coffee_1, "Ice coffee", "small", 2.59, "$", true, 9),
                ProductItemData(R.drawable.coffee_2, "Ice coffee", "medium", 3.59, "$", true, 6),
                ProductItemData(R.drawable.coffee_3, "Ice coffee", "large", 4.59, "$", false, 3),
                ProductItemData(R.drawable.coffee_4, "Cappuccino", "small", 5.59, "$", true, 9),
                ProductItemData(
                    R.drawable.coffee_5,
                    "Hot Chocolate coffee",
                    "small",
                    6.09,
                    "$",
                    true,
                    9
                )
            )
        }
}