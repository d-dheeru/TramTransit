package com.gotranspo.tramtransit.data.model.directions

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)