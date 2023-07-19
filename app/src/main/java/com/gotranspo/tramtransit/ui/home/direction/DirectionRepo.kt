package com.gotranspo.tramtransit.ui.home.direction


import com.gotranspo.tramtransit.remote.BaseRepository
import com.gotranspo.tramtransit.remote.api.ApiService
import javax.inject.Inject

class DirectionRepo @Inject constructor(private val apiService: ApiService): BaseRepository() {
    suspend fun getDirections(origin : String, destination : String,mode : String, key : String) = safeapicall {
        apiService.getDirections(origin,destination,mode,key)
    }
}