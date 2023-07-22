package com.gotranspo.tramtransit.data.model.directions

import com.gotranspo.tramtransit.data.model.directions.Distance
import com.gotranspo.tramtransit.data.model.directions.Polyline

data class Step(
    val distance: Distance,
//    val duration: DurationX,
//    val end_location: EndLocationX,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
//    val start_location: StartLocationX,
    val travel_mode: String
)