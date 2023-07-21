package com.gotranspo.tramtransit.db.Room

import com.gotranspo.tramtransit.db.dao.AppDao
import com.gotranspo.tramtransit.db.entities.DestinationEnitity
import javax.inject.Inject

class RoomRepository @Inject constructor(private val appDao : AppDao) {
    fun insertRecord(destinationEnitity: DestinationEnitity)
    {
        appDao.insertRecord(destinationEnitity)
    }
    fun fetchDestinations() : List<DestinationEnitity>
    {
        return  appDao.getDestinations()
    }
}