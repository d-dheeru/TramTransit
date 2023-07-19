package com.gotranspo.tramtransit.remote.api

import com.example.devtransportationapp.model.directions.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") key: String,
    ) : DirectionsResponse
}