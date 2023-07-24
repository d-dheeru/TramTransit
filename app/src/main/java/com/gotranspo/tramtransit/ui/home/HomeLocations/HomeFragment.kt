package com.gotranspo.tramtransit.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.databinding.FragmentHomeBinding
import com.gotranspo.tramtransit.db.Room.RoomViewModel
import com.gotranspo.tramtransit.ui.home.HomeLocations.RecentAdapter
import com.gotranspo.tramtransit.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() , OnMapReadyCallback {
    private var  _binding : FragmentHomeBinding? = null
    private val binding
    get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val TAG = "HomeFragment"
    private val REQUEST_CODE  = 101
    private val roomViewModel : RoomViewModel by viewModels()
    private lateinit var sharedPreferencesHome : SharedPreferences
    private lateinit var sharedPreferencesWork : SharedPreferences
    private var originLat : Double = 0.0
    private var  originLong  : Double = 0.0
    private var destLat : Double = 0.0
    private var destLong : Double = 0.0
    private val homeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {

                    val place = Autocomplete.getPlaceFromIntent(intent)
                    val homeEditor : SharedPreferences.Editor  = sharedPreferencesHome.edit()
                    homeEditor.putString(Constants.HOME_LAT, place.latLng.latitude.toString())
                    homeEditor.putString(Constants.HOME_LONG,place.latLng.longitude.toString())
                    homeEditor.putString(Constants.HOME_ADDRESS, place.address)
                    homeEditor.apply()
                    binding.homeadress.text = "Home"

                    binding.homeadress.setCompoundDrawablesWithIntrinsicBounds(com.gotranspo.tramtransit.R.drawable.home_24, 0, 0, 0);


                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {

                Log.i(TAG, "User canceled autocomplete")
            }
        }

    private val officeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {

                    val place = Autocomplete.getPlaceFromIntent(intent)
                    val workEditor : SharedPreferences.Editor  = sharedPreferencesWork.edit()
                    workEditor.putString(Constants.WORK_LAT, place.latLng.latitude.toString())
                    workEditor.putString(Constants.WORK_LONG,place.latLng.longitude.toString())
                    workEditor.putString(Constants.WORK_ADDRESS, place.address)
                    workEditor.apply()
                    binding.workaddress.text = "Work"
                    binding.workaddress.setCompoundDrawablesWithIntrinsicBounds(com.gotranspo.tramtransit.R.drawable.baseline_work, 0, 0, 0);




                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {

                Log.i(TAG, "User canceled autocomplete")
            }
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        val mapFragment = childFragmentManager.findFragmentById(com.gotranspo.tramtransit.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLayoutHeight()
        Places.initialize(requireContext(), Constants.API_KEY)
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS
        )
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        sharedPreferencesHome = requireContext().getSharedPreferences(Constants.SHAREDPREFERENCES_HOME,Context.MODE_PRIVATE)
        sharedPreferencesWork = requireContext().getSharedPreferences(Constants.SHAREDPREFERENCES_WORK,Context.MODE_PRIVATE)
        if (sharedPreferencesHome.contains(Constants.HOME_ADDRESS) && sharedPreferencesWork.contains(Constants.WORK_ADDRESS))
        {
            binding.homeadress.text = "Home"
            binding.workaddress.text = "Work"
            binding.homeadress.setCompoundDrawablesWithIntrinsicBounds(com.gotranspo.tramtransit.R.drawable.home_24, 0, 0, 0);
            binding.workaddress.setCompoundDrawablesWithIntrinsicBounds(com.gotranspo.tramtransit.R.drawable.baseline_work, 0, 0, 0);



        }
        if (sharedPreferencesHome.contains(Constants.HOME_ADDRESS))
        {
            binding.homeadress.text = "Home"
            binding.homeadress.setCompoundDrawablesWithIntrinsicBounds(com.gotranspo.tramtransit.R.drawable.home_24, 0, 0, 0);


        }
        if (sharedPreferencesWork.contains(Constants.WORK_ADDRESS))
        {
            binding.workaddress.text = "Work"
            binding.workaddress.setCompoundDrawablesWithIntrinsicBounds(com.gotranspo.tramtransit.R.drawable.baseline_work, 0, 0, 0);


        }




        binding.searchplaceseditttext.setOnClickListener {
            findNavController().navigate(com.gotranspo.tramtransit.R.id.directionFragment)

        }

        binding.homeadress.setOnClickListener {
            if (binding.homeadress.text.equals("Add Home"))
            {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(requireContext())
                homeLauncher.launch(intent)
            }
            else
            {
                destLat = sharedPreferencesHome.getString(Constants.HOME_LAT,null)!!.toDouble()
                destLong = sharedPreferencesHome.getString(Constants.HOME_LONG, null)!!.toDouble()
                val bundle = Bundle()
                bundle.putString("originLatitude",originLat.toString())
                bundle.putString("originLongitude",originLong.toString())
                bundle.putString("destLatitude",destLat.toString())
                bundle.putString("destLongitude",destLong.toString())
                findNavController().navigate(R.id.directionsDetailsFragment,bundle)

            }
        }
        binding.workaddress.setOnClickListener {
            if (binding.workaddress.text.equals("Add Work"))
            {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(requireContext())
                officeLauncher.launch(intent)
            }
            else
            {
                destLat = sharedPreferencesWork.getString(Constants.WORK_LAT,null)!!.toDouble()
                destLong = sharedPreferencesWork.getString(Constants.WORK_LONG, null)!!.toDouble()
                val bundle = Bundle()
                bundle.putString("originLatitude",originLat.toString())
                bundle.putString("originLongitude",originLong.toString())
                bundle.putString("destLatitude",destLat.toString())
                bundle.putString("destLongitude",destLong.toString())
                findNavController().navigate(R.id.directionsDetailsFragment,bundle)

            }
        }


        roomViewModel.fetchDestinations()
        roomViewModel.allDestinations.observe(viewLifecycleOwner){
            if (it.size  >= 1)
            {
                val adapter = RecentAdapter(it , object  : RecentAdapter.OnClickListener{
                    override fun onClick(position: Int) {
                        destLat = it[position].latitude
                       destLong =  it[position].longitude
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
                binding.norecents.visibility = View.GONE
            }
        }


        return binding.root
    }
    private fun getLayoutHeight() {
        val vto: ViewTreeObserver = binding.layoutTop.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    binding.layoutTop.viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    binding.layoutTop.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                val width: Int = binding.layoutTop.measuredWidth
                val height = (binding.layoutTop.measuredHeight + 30)
                BottomSheetBehavior.from(binding.sheet).apply {
                    this.setPeekHeight(resources.displayMetrics.heightPixels - (height + 10), true)
//                    this.setPeekHeight(600, true)

                }
            }
        })

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), com.gotranspo.tramtransit.R.raw.mapstyle_night));
        checkPermision()
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
        mMap.isMyLocationEnabled = true

    }

    private fun checkPermision() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermision()
        }
        else
        {
            getCurrentLocation()
        }

    }

    private fun requestPermision() {
        ActivityCompat.requestPermissions(requireActivity() , arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_CODE)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE)
        {
            getCurrentLocation()
        }
    }

    fun getCurrentLocation() {
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
                val currentLocation = LatLng(location.latitude, location.longitude)
                originLat = location.latitude
                originLong = location.longitude

                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 15.0f
                    )
                )
                mMap.addMarker(
                    MarkerOptions().position(currentLocation).title("Your Current Location")
                )

            }

        }
    }

}