package com.gotranspo.tramtransit.ui.home.directionbymodes

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.databinding.FragmentDirectionBinding
import com.gotranspo.tramtransit.db.Room.RoomViewModel
import com.gotranspo.tramtransit.db.entities.DestinationEnitity
import com.gotranspo.tramtransit.ui.home.HomeLocations.RecentAdapter
import com.gotranspo.tramtransit.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DirectionsFragment : Fragment() {
    private var _binding: FragmentDirectionBinding? = null
    private val binding
        get() = _binding!!
    private val TAG = "DirectionsFragment"
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var destLat: Double? = 0.0
    private var destLong: Double? = 0.0

    private var originLat: Double = 0.0
    private var originLong: Double = 0.0
    private val roomViewModel : RoomViewModel by viewModels()

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {

                    val place = Autocomplete.getPlaceFromIntent(intent)
                    binding.editText2.text = place.address.toString()
                    destLat = place.latLng.latitude
                    destLong = place.latLng.longitude
                    val destinationEnitity = DestinationEnitity()
                    destinationEnitity.apply {
                        address = place.address
                        latitude = place.latLng.latitude
                        longitude = place.latLng.longitude
                    }
                    roomViewModel.insertRecord(destinationEnitity)
                    Log.d(TAG, ":  $originLong , $originLat , $destLat ,  $destLong" )
                    val bundle = Bundle()
                    bundle.putString("originLatitude",originLat.toString())
                    bundle.putString("originLongitude",originLong.toString())
                    bundle.putString("destLatitude",destLat.toString())
                    bundle.putString("destLongitude",destLong.toString())
                    findNavController().navigate(R.id.directionsDetailsFragment,bundle)


//                    updateLocation(place.latLng.latitude, place.latLng.longitude)


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
        _binding = FragmentDirectionBinding.inflate(layoutInflater)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLayoutHeight()


        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        roomViewModel.fetchDestinations()
        roomViewModel.allDestinations.observe(viewLifecycleOwner){
            if (it.size  >= 1)
            {
                val adapter = RecentAdapter(it , object  : RecentAdapter.OnClickListener{
                    override fun onClick(position: Int) {
                        destLat = it[position].latitude
                        destLong = it[position].longitude
                        val bundle = Bundle()
                        bundle.putString("originLatitude",originLat.toString())
                        bundle.putString("originLongitude",originLong.toString())
                        bundle.putString("destLatitude",destLat.toString())
                        bundle.putString("destLongitude",destLong.toString())
                        findNavController().navigate(R.id.directionsDetailsFragment,bundle)
                    }

                })
                binding.recyclerview.adapter = adapter
                binding.recyclerview.visibility = View.VISIBLE
                binding.recentsearches.visibility = View.GONE
            }
        }


//       destLat =  arguments?.getString("Latitude")
//        destLong = arguments?.getString("Longitude")
//        getDestinationAddress()
        checkPermission()

        binding.editText2.setOnClickListener {
            launchIntentForSearch()
        }

//        binding.autopaymentbtn.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("originLatitude", originLat.toString())
//            bundle.putString("originLongitude",originLong.toString())
//            bundle.putString("destLatitude", destLat)
//            bundle.putString("destLongitude",destLong)
//            findNavController().navigate(R.id.directionsDetailsFragment,bundle)
//
//        }


        return binding.root
    }

    private fun launchIntentForSearch() {

        Places.initialize(requireContext(), Constants.API_KEY)
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireContext())

        startAutocomplete.launch(intent)
    }

    private fun updateLocation(latitude: Double, longitude: Double) {}

    //    private fun getDestinationAddress() {
//
//        val mGeocoder = Geocoder(requireContext(), Locale.getDefault())
//        var addressString = ""
//        val destinationLat: Double = destLat!!.toDouble()
//        val destinationLong: Double = destLong!!.toDouble()
//
//        try {
//            val addressList: List<Address> =
//                mGeocoder.getFromLocation(destinationLat, destinationLong, 1) as List<Address>
//            if (addressList != null && addressList.isNotEmpty()) {
//                val address = addressList[0]
//                val sb = StringBuilder()
//                for (i in 0 until address.maxAddressLineIndex) {
//                    sb.append(address.getAddressLine(i)).append("\n")
//                }
//
//                if (address.premises != null)
//                    sb.append(address.subAdminArea).append("\n")
//                sb.append(address.locality).append(", ")
//                sb.append(address.adminArea).append(", ")
//                sb.append(address.countryName).append(", ")
//                addressString = sb.toString()
//                binding!!.editText2.text = addressString
//
//            }
//        } catch (e: IOException) {
//            Toast.makeText(requireContext(), "Unable connect to Geocoder", Toast.LENGTH_LONG)
//                .show()
//        }
//    }
    fun checkPermission() {
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

                originLat = location.latitude
                originLong = location.longitude

//                val origin = location.latitude.toString() + "," + location.longitude.toString()
//                val destination = destLat.toString() + "," + destLong.toString()
//                // For driving
//                directionViewmodel.getdirectionLists(
//                    origin, destination,
//                    Constants.DRIVING,
//                    Constants.API_KEY
//
//                )
//
//                directionViewmodel.directionsData.observe(viewLifecycleOwner) {
//
//
//                        binding!!.drivingdistance.text = it.routes[0].legs[0].distance.text
//                        binding!!.drivingtiming.text = it.routes[0].legs[0].duration.text
//
//
//                }
//                // for walk
//                directionViewmodel.getdirectionLists(
//                    origin, destination,
//                    Constants.WALKING,
//                    Constants.API_KEY
//
//                )
//                directionViewmodel.directionsData.observe(viewLifecycleOwner) {
//                    binding!!.walkingdistance.text = it.routes[0].legs[0].distance.text
//                    binding!!.walkingtiming.text = it.routes[0].legs[0].duration.text
//                }

                // for transit
//                directionViewmodel.getdirectionLists(
//                    origin, destination,
//                    Constants.TRANSIT,
//                    Constants.API_KEY

//                )
//                directionViewmodel.directionsData.observe(viewLifecycleOwner) {
//                    binding!!.transitdistance.text = it.routes[0].legs[0].distance.text
//                    binding!!.transittiming.text = it.routes[0].legs[0].duration.text
//                }
            }


        }
    }

    private fun getLayoutHeight() {
        val vto: ViewTreeObserver = binding.layoutTop1.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    binding.layoutTop1.viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    binding.layoutTop1.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val width: Int = binding.layoutTop1.measuredWidth
                val height = (binding.layoutTop1.measuredHeight + 30)
                BottomSheetBehavior.from(binding.sheet).apply {
                    this.setPeekHeight(resources.displayMetrics.heightPixels - (height + 10), true)
//                    this.setPeekHeight(600, true)

                }
            }
        })

    }


}