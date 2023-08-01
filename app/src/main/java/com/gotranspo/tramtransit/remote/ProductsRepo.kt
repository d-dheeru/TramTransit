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
                ProductItemData("Prod_000001", R.drawable.coffee_1, "Ice coffee", "small", 2.59, "€", true, 1),
                ProductItemData("Prod_000002", R.drawable.coffee_2, "Ice coffee", "medium", 3.59, "€", true, 6),
                ProductItemData("Prod_000003", R.drawable.coffee_3, "Ice coffee", "large", 4.59, "€", false, 3),
                ProductItemData("Prod_000004", R.drawable.coffee_4, "Cappuccino", "small", 5.59, "€", true, 9),
                ProductItemData("Prod_000005", R.drawable.coffee_5, "Cappuccino", "medium", 6.79, "€", true, 4),
            )
        }
}