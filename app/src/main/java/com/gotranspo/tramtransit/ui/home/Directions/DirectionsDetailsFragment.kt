package com.gotranspo.tramtransit.ui.home.Directions


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.directions.route.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.gotranspo.tramtransit.databinding.FragmentDirectionsDetailsBinding
import com.gotranspo.tramtransit.ui.home.directionbymodes.DirectionViewModel
import com.gotranspo.tramtransit.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DirectionsDetailsFragment : Fragment() , OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener,
    RoutingListener {
    private var _binding  : FragmentDirectionsDetailsBinding? = null
    private val binding
    get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var originLat : Double = 0.0
    private var originLong : Double = 0.0
    private var destLat : Double = 0.0
    private var destLong : Double  = 0.0
    private lateinit var latLngList : ArrayList<LatLng>
    private var polylineOption : PolylineOptions? = null
    private lateinit var markerOptionList : ArrayList<MarkerOptions>
    private val directionViewmodel: DirectionViewModel by viewModels()
    private val TAG = "DirectionDetails"
    private lateinit var startLatLng : LatLng
    private lateinit var endLatLng : LatLng

    //polyline object
    private var polylines: ArrayList<Polyline>? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDirectionsDetailsBinding.inflate(layoutInflater)
        val mapFragment = childFragmentManager.findFragmentById(com.gotranspo.tramtransit.R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
        latLngList = ArrayList()
        markerOptionList = ArrayList()
        originLat = arguments?.getString("originLatitude")!!.toDouble()
        originLong =  arguments?.getString("originLongitude")!!.toDouble()
        destLat =  arguments?.getString("destLatitude")!!.toDouble()
        destLong =  arguments?.getString("destLongitude")!!.toDouble()
//        latLngList.add(LatLng(originLat,originLong))
//        latLngList.add(LatLng(destLat,destLong))
        val origin = originLat.toString() + "," + originLong.toString()
        val destination = destLat.toString() + "," + destLong.toString()
        startLatLng = LatLng(originLat,originLong)
        endLatLng = LatLng(destLat,destLong)

        val routing = Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(startLatLng, endLatLng)
            .key(Constants.API_KEY) //also define your api key here.
            .build()
        routing.execute()

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), com.gotranspo.tramtransit.R.raw.mapstyle_night));

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onRoutingFailure(p0: RouteException?) {

    }

    override fun onRoutingStart() {
        Toast.makeText(requireActivity(),"Finding Route...",Toast.LENGTH_LONG).show();
    }

    override fun onRoutingSuccess(route: java.util.ArrayList<Route>?, shortestRouteIndex: Int) {
        val center = CameraUpdateFactory.newLatLng(startLatLng)
        val zoom = CameraUpdateFactory.zoomTo(16f)
        val polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null
        polylines = ArrayList()

        for (i in 0 until route!!.size) {
            if (i == shortestRouteIndex) {
                polyOptions.width(7f)
                polyOptions.addAll(route!![shortestRouteIndex].points)
                val polyline = mMap.addPolyline(polyOptions)
                polylineStartLatLng = polyline.points[0]
                val k: Int = polyline.points.size
                polylineEndLatLng = polyline.points[k - 1]
                polylines!!.add(polyline)

            } else {
            }

        }

        val startMarker = MarkerOptions()
        startMarker.position(polylineStartLatLng!!)
        startMarker.title("My Location")
        mMap.addMarker(startMarker)
        val endMarker = MarkerOptions()
        endMarker.position(polylineEndLatLng!!)
        endMarker.title("Destination")
        mMap.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {

    }


}