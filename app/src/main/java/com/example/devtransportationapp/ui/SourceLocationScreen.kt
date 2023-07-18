package com.example.devtransportationapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.devtransportationapp.databinding.ActivityFromLocationScreenBinding
import com.example.devtransportationapp.viewmodels.DirectionViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.IOException
import java.util.*

class SourceLocationScreen : AppCompatActivity() {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var destLat: String? = null
    private var destLong: String? = null
    private val TAG = "SourceLocationActivity"
    private var binding: ActivityFromLocationScreenBinding? = null
    private var originLat: Double? = 0.0
    private var originLong: Double? = 0.0
    private lateinit var directionViewmodel: DirectionViewModel
    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {

                    val place = Autocomplete.getPlaceFromIntent(intent)
                    updateLocation(place.latLng.latitude, place.latLng.longitude)


                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "User canceled autocomplete")
            }
        }

    private fun updateLocation(latitude: Double, longitude: Double) {




    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromLocationScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        destLat = intent.getStringExtra("Latitude")
        destLong = intent.getStringExtra("Longitude")
        getDestinationAddress()
        directionViewmodel = ViewModelProvider(this).get(DirectionViewModel::class.java)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        } else {
            getCurrentLocations()
        }
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS)
        binding!!.startedlocation.setOnClickListener {
            binding!!.startedlocation.setText("")
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startAutocomplete.launch(intent)

        }



    }

    private fun getCurrentLocations() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener(this) { location ->
                if (location != null) {

                    val origin = location.latitude.toString() + "," + location.longitude.toString()
                    val destination = destLat.toString() + "," + destLong.toString()
                    // For driving
                    directionViewmodel.getdirectionLists(
                        origin, destination,
                        "DRIVING",
                        "AIzaSyDgoscMvIQhSCWPnoREpISJv-witpxH2N8"

                    )
                    directionViewmodel.directionsData.observe(this, androidx.lifecycle.Observer {
                        binding!!.drivingdistance.text = it.routes[0].legs[0].distance.text
                        binding!!.drivingtiming.text = it.routes[0].legs[0].duration.text
                    })
                    // for walk
                    directionViewmodel.getdirectionLists(
                        origin, destination,
                        "WALKING",
                        "AIzaSyDgoscMvIQhSCWPnoREpISJv-witpxH2N8"

                    )
                    directionViewmodel.directionsData.observe(this, androidx.lifecycle.Observer {
                        binding!!.walkingdistance.text = it.routes[0].legs[0].distance.text
                        binding!!.walkingtiming.text = it.routes[0].legs[0].duration.text
                    })

                    // for transit
                    directionViewmodel.getdirectionLists(
                        origin, destination,
                        "TRANSIT",
                        "AIzaSyDgoscMvIQhSCWPnoREpISJv-witpxH2N8"

                    )
                    directionViewmodel.directionsData.observe(this, androidx.lifecycle.Observer {
                        binding!!.transitdistance.text = it.routes[0].legs[0].distance.text
                        binding!!.transittiming.text = it.routes[0].legs[0].duration.text
                    })
                }
            }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getDestinationAddress() {

        val mGeocoder = Geocoder(applicationContext, Locale.getDefault())
        var addressString = ""
        val destinationLat: Double = destLat!!.toDouble()
        val destinationLong: Double = destLong!!.toDouble()

        try {
            val addressList: List<Address> =
                mGeocoder.getFromLocation(destinationLat, destinationLong, 1) as List<Address>
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append("\n")
                }

                if (address.premises != null)
                    sb.append(address.subAdminArea).append("\n")
                sb.append(address.locality).append(", ")
                sb.append(address.adminArea).append(", ")
                sb.append(address.countryName).append(", ")
                addressString = sb.toString()
                binding!!.editText2.text = addressString

            }
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "Unable connect to Geocoder", Toast.LENGTH_LONG)
                .show()
        }
    }
}