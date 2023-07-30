package com.gotranspo.tramtransit.data.model.directions.product

data class ProductItemData(
    val imageId: Int,
    val itemName: String,
    val itemSize: String,
    val itemCost: Double,
    val currency: String,
    val itemAvailability: Boolean,
    val itemAvailableCount: Int
)
