package com.gotranspo.tramtransit.data.model.directions

import com.gotranspo.tramtransit.data.model.directions.Bounds
import com.gotranspo.tramtransit.data.model.directions.Leg
import com.gotranspo.tramtransit.data.model.directions.OverviewPolyline

data class Route(
    val bounds: Bounds,
    val copyrights: String,
    val legs: List<Leg>,
    val overview_polyline: OverviewPolyline,
    val summary: String,
    val warnings: List<Any>,
    val waypoint_order: List<Any>
)