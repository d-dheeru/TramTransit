package com.gotranspo.tramtransit.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.gotranspo.tramtransit.db.entities.DestinationEnitity
import retrofit2.http.Query

@Dao
interface AppDao {

    @androidx.room.Query("SELECT * FROM Destinations")
    fun getDestinations() : List<DestinationEnitity>

    @Insert
    fun insertRecord(destinations : DestinationEnitity)
}