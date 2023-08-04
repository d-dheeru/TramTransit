package com.gotranspo.tramtransit.remote

import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.remote.api.ApiService
import com.gotranspo.tramtransit.utils.CommonUtils
import javax.inject.Inject

class ProductsRepo @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    suspend fun getProducts(position: Int) =
        safeapicall {
            when (position) {
                0 -> {
                    listOf(
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.coffee_1,
                            "Coffee",
                            "Ice coffee",
                            "small",
                            2.59,
                            "€",
                            true,
                            1
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.coffee_2,
                            "Coffee",
                            "Ice coffee",
                            "medium",
                            3.59,
                            "€",
                            true,
                            6
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.coffee_3,
                            "Coffee",
                            "Ice coffee",
                            "large",
                            4.59,
                            "€",
                            false,
                            3
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.coffee_4,
                            "Coffee",
                            "Cappuccino",
                            "small",
                            5.59,
                            "€",
                            true,
                            9
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.coffee_5,
                            "Coffee",
                            "Cappuccino",
                            "medium",
                            6.79,
                            "€",
                            true,
                            4
                        ),
                    )
                }

                1 -> {
                    listOf(
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.burger_1,
                            "Meal",
                            "Chicken Burger",
                            "small",
                            2.59,
                            "€",
                            true,
                            1
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.burger_2,
                            "Meal",
                            "Chicken Burger",
                            "medium",
                            3.59,
                            "€",
                            true,
                            6
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.burger_3,
                            "Meal",
                            "Chicken Burger",
                            "large",
                            4.59,
                            "€",
                            false,
                            3
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.burger_4,
                            "Meal",
                            "Veg Burger",
                            "small",
                            5.59,
                            "€",
                            true,
                            9
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.burger_5,
                            "Meal",
                            "Veg Burger",
                            "medium",
                            6.79,
                            "€",
                            true,
                            4
                        ),
                    )
                }

                else -> {
                    listOf(
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.cooldrink_5,
                            "Drink",
                            "Coke No-Sugar",
                            "Small",
                            2.59,
                            "€",
                            true,
                            1
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.cooldrink_4,
                            "Drink",
                            "Coke Normal",
                            "Small",
                            3.59,
                            "€",
                            true,
                            6
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.cooldrink_3,
                            "Drink",
                            "Pepsi large",
                            "large",
                            4.59,
                            "€",
                            false,
                            3
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.cooldrink_2,
                            "Drink",
                            "Pepsi small",
                            "small",
                            5.59,
                            "€",
                            true,
                            9
                        ),
                        ProductItemData(
                            CommonUtils.createNewGuid(),
                            R.drawable.cooldrink_1,
                            "Drink",
                            "Jaffa medium",
                            "medium",
                            6.79,
                            "€",
                            true,
                            4
                        ),
                    )
                }
            }
        }
}