package com.gotranspo.tramtransit.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng

class Custom(private val context : Context ) {

    fun getCurrentLocation(fusedLocationProviderClient : FusedLocationProviderClient) : LatLng
    {
        var latLng=LatLng(0.0,0.0)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it?.let { location->
            latLng = LatLng(location.latitude,location.longitude)
            }

        }
        return latLng

        }



}