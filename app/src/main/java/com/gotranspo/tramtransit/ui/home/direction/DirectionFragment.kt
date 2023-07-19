package com.gotranspo.tramtransit.ui.home.direction

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.widget.Autocomplete
import com.gotranspo.tramtransit.databinding.FragmentDirectionBinding
import com.gotranspo.tramtransit.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class DirectionFragment : Fragment() {
    private var _binding : FragmentDirectionBinding? = null
    private val binding
    get() = _binding!!
    private val TAG = "DirectionsFragment"
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var destLat: String? = null
    private var destLong: String? = null
    private val directionViewmodel: DirectionViewModel by viewModels()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentDirectionBinding.inflate(layoutInflater)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

       destLat =  arguments?.getString("Latitude")
        destLong = arguments?.getString("Longitude")

//        val bundle = this.arguments
//        if (bundle != null) {
//            Log.d(TAG, "onCreateView: " + destLat)
//            destLat = bundle.getString("Latitude", "")
//            destLong = bundle.getString("Longitude", "")
//        }


//        destLat = intent.getStringExtra("Latitude")
//        destLong = intent.getStringExtra("Longitude")
        getDestinationAddress()
        checkPermission()

        return binding.root
    }

    private fun updateLocation(latitude: Double, longitude: Double) {}
    private fun getDestinationAddress() {

        val mGeocoder = Geocoder(requireContext(), Locale.getDefault())
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
            Toast.makeText(requireContext(), "Unable connect to Geocoder", Toast.LENGTH_LONG)
                .show()
        }
    }
    fun checkPermission()
    {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermmisions()

        } else {
            getCurrentLocations()
        }
    }

    private fun requestPermmisions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            101
        )

    }

    private fun getCurrentLocations() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {

                val origin = location.latitude.toString() + "," + location.longitude.toString()
                val destination = destLat.toString() + "," + destLong.toString()
                // For driving
                directionViewmodel.getdirectionLists(
                    origin, destination,
                    Constants.DRIVING,
                    Constants.API_KEY

                )

                directionViewmodel.directionsData.observe(viewLifecycleOwner) {


                        binding!!.drivingdistance.text = it.routes[0].legs[0].distance.text
                        binding!!.drivingtiming.text = it.routes[0].legs[0].duration.text


                }
                // for walk
                directionViewmodel.getdirectionLists(
                    origin, destination,
                    Constants.WALKING,
                    Constants.API_KEY

                )
                directionViewmodel.directionsData.observe(viewLifecycleOwner) {
                    binding!!.walkingdistance.text = it.routes[0].legs[0].distance.text
                    binding!!.walkingtiming.text = it.routes[0].legs[0].duration.text
                }

                // for transit
                directionViewmodel.getdirectionLists(
                    origin, destination,
                    Constants.TRANSIT,
                    Constants.API_KEY

                )
                directionViewmodel.directionsData.observe(viewLifecycleOwner) {
                    binding!!.transitdistance.text = it.routes[0].legs[0].distance.text
                    binding!!.transittiming.text = it.routes[0].legs[0].duration.text
                }
            }


        }
    }


}