package com.gotranspo.tramtransit.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Destinations")
class DestinationEnitity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id : Int = 0
    @ColumnInfo(name = "latitude")
    var latitude : Double = 0.0
    @ColumnInfo(name = "longitude")
    var longitude : Double = 0.0
    @ColumnInfo(name = "Address")
    var address : String? = null

}