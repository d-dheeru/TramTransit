package com.gotranspo.tramtransit.ui.home.Directions


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DirectionsDetailsFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    private var _binding: FragmentDirectionsDetailsBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var originLat: Double = 0.0
    private var originLong: Double = 0.0
    private var destLat: Double = 0.0
    private var destLong: Double = 0.0
    private lateinit var latLngList: ArrayList<LatLng>
    private var polylineOption: PolylineOptions? = null
    private lateinit var markerOptionList: ArrayList<MarkerOptions>
    private val directionViewmodel: DirectionViewModel by viewModels()
    private val TAG = "DirectionDetails"
    private lateinit var startLatLng: LatLng
    private lateinit var endLatLng: LatLng
    private lateinit var currentTime: Calendar
    private var hours: Int = 0
    private var minuts: Int = 0

    //polyline object
    private var polylines: ArrayList<Polyline>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDirectionsDetailsBinding.inflate(layoutInflater)
        val mapFragment =
            childFragmentManager.findFragmentById(com.gotranspo.tramtransit.R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
        latLngList = ArrayList()
        markerOptionList = ArrayList()
        originLat = arguments?.getString("originLatitude")!!.toDouble()
        originLong = arguments?.getString("originLongitude")!!.toDouble()
        destLat = arguments?.getString("destLatitude")!!.toDouble()
        destLong = arguments?.getString("destLongitude")!!.toDouble()

        startLatLng = LatLng(originLat, originLong)
        endLatLng = LatLng(destLat, destLong)
        currentTime = Calendar.getInstance()
         hours = currentTime.get(Calendar.HOUR_OF_DAY)
        minuts = currentTime.get(Calendar.MINUTE)
        val origin = originLat.toString() + "," + originLong.toString()
        val destination = destLat.toString() + "," + destLong.toString()

        val routing =
            Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING).withListener(this)
                .alternativeRoutes(true).waypoints(startLatLng, endLatLng)
                .key(Constants.API_KEY) //also define your api key here.
                .build()
        routing.execute()
        binding.MaterialCardViewDirection.setOnClickListener {
            binding.linearLayoutCompat3.visibility = View.VISIBLE
            binding.constraintlayoutcoffeecard.visibility = View.GONE
        }
        binding.materialCardViewCoffee.setOnClickListener {
            binding.linearLayoutCompat3.visibility = View.VISIBLE
            binding.constraintlayoutcoffeecard.visibility = View.GONE
        }

        binding.currentimeTextView.text = "$hours : $minuts"
        directionViewmodel.getdirectionLists(
            origin, destination, Constants.DRIVING, Constants.API_KEY
        )
        Observer()
        binding.caricon.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.DRIVING, Constants.API_KEY
            )
            binding.caricon.borderColor = resources.getColor(com.gotranspo.tramtransit.R.color.blue)
            binding.walkingicon.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            Observer()

        }
        binding.walkingicon.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.WALKING, Constants.API_KEY
            )
            binding.caricon.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            binding.walkingicon.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.blue)
            Observer()
        }

        binding.busicon1.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.DRIVING, Constants.API_KEY
            )

            binding.busicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow)
            binding.cycleicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.green)
            binding.cardicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.blue)
            binding.walkicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            Observer()
        }
        binding.cycleicon1.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.WALKING, Constants.API_KEY
            )

            binding.busicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.green)
            binding.cycleicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow)
            binding.cardicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.blue)
            binding.walkicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            Observer()
        }

        binding.cardicon1.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.DRIVING, Constants.API_KEY
            )

            binding.busicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.blue)
            binding.cycleicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow)
            binding.cardicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.green)
            binding.walkicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            Observer()
        }

        binding.walkicon1.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.WALKING, Constants.API_KEY
            )

            binding.busicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            binding.cycleicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow)
            binding.cardicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.blue)
            binding.walkicon1.borderColor =
                resources.getColor(com.gotranspo.tramtransit.R.color.green)
            Observer()
        }
        binding.cycleicon2.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.WALKING, Constants.API_KEY
            )
            setColors(
                resources.getColor(com.gotranspo.tramtransit.R.color.green),
                resources.getColor(com.gotranspo.tramtransit.R.color.blue),
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow),
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            )
            Observer()

        }
        binding.caricon2.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.DRIVING, Constants.API_KEY
            )
            setColors(
                resources.getColor(com.gotranspo.tramtransit.R.color.blue),
                resources.getColor(com.gotranspo.tramtransit.R.color.green),
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow),
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            )
            Observer()

        }
        binding.busicon2.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.DRIVING, Constants.API_KEY
            )
            setColors(
                resources.getColor(com.gotranspo.tramtransit.R.color.blue),
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow),
                resources.getColor(com.gotranspo.tramtransit.R.color.green),
                resources.getColor(com.gotranspo.tramtransit.R.color.white)
            )
            Observer()

        }
        binding.walkicon2.setOnClickListener {
            directionViewmodel.getdirectionLists(
                origin, destination, Constants.WALKING, Constants.API_KEY
            )
            setColors(
                resources.getColor(com.gotranspo.tramtransit.R.color.blue),
                resources.getColor(com.gotranspo.tramtransit.R.color.yellow),
                resources.getColor(com.gotranspo.tramtransit.R.color.white),
                resources.getColor(com.gotranspo.tramtransit.R.color.green)
            )
            Observer()

        }

        binding.startnavigation.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$destination&mode=l"))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }

        return binding.root
    }

    private fun setColors(
        cycle: Int,
        car: Int,
        bus: Int,
        walk: Int
    ) {
        binding.cycleicon2.borderColor = cycle
        binding.caricon2.borderColor = car
        binding.busicon2.borderColor = bus
        binding.walkicon2.borderColor = walk
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), com.gotranspo.tramtransit.R.raw.mapstyle_night
            )
        );
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    originLat, originLong
                ), 15.0f
            )
        )

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onRoutingFailure(p0: RouteException?) {

    }

    override fun onRoutingStart() {
        Toast.makeText(requireActivity(), "Finding Route...", Toast.LENGTH_LONG).show();
    }

    override fun onRoutingSuccess(route: java.util.ArrayList<Route>?, shortestRouteIndex: Int) {
        val center = CameraUpdateFactory.newLatLng(startLatLng)
        val zoom = CameraUpdateFactory.zoomTo(20f)
        val polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null
        polylines = ArrayList()

        for (i in 0 until route!!.size) {
            if (i == shortestRouteIndex) {
                polyOptions.width(20f)
                polyOptions.color(resources.getColor(com.gotranspo.tramtransit.R.color.red))
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

    @SuppressLint("SetTextI18n")
    private fun Observer() {

        directionViewmodel.directionsData.observe(viewLifecycleOwner) {
            hours = currentTime.get(Calendar.HOUR_OF_DAY)
            minuts = currentTime.get(Calendar.MINUTE)
            binding.materialTextView2.text = it.routes[0].legs[0].end_address
            binding.circlImageViewstartingaddress.text = it.routes[0].legs[0].start_address
            binding.circlImageViewendindaddress.text = it.routes[0].legs[0].steps[0].html_instructions

            val secondMint = it.routes[0].legs[0].steps[0].duration.text[0].toInt()
            var totalSecondMinuts = minuts + secondMint
            if (totalSecondMinuts > 60)
            {
                hours += 1
                totalSecondMinuts = 60 - totalSecondMinuts
                binding.SecondTimeTextView.text = "$hours : 06"
            }
            else
            {
                binding.SecondTimeTextView.text = "$hours : ${totalSecondMinuts.toString()}"
            }

            if (it.routes[0].legs[0].steps.size > 1) {
                val thirdMint = it.routes[0].legs[0].steps[1].duration.text[0].toInt()
                var thirdTotalMinuts = totalSecondMinuts + thirdMint
                if (thirdTotalMinuts > 60)
                {
                    hours += 1
                    thirdTotalMinuts = 60 - thirdTotalMinuts
                    binding.ThirdTimeTextView.text = "$hours : ${thirdTotalMinuts.toString()}"
                }
                else
                {
                    binding.ThirdTimeTextView.text = "$hours : ${thirdTotalMinuts.toString()}"
                }

                val LastMint =
                    it.routes[0].legs[0].steps[it.routes[0].legs[0].steps.size - 1].duration.text[0].toInt()
                var lastTotalMinuts = thirdTotalMinuts + LastMint
                if (lastTotalMinuts > 60 )
                {
                    hours += 1
                    lastTotalMinuts = 60 - lastTotalMinuts
                    binding.ForthTimeTextView.text = "$hours : ${lastTotalMinuts.toString()}"
                }
                else
                {
                    binding.ForthTimeTextView.text = "$hours : ${lastTotalMinuts.toString()}"
                }

            } else {
                binding.ThirdTimeTextView.visibility = View.GONE
                binding.ForthTimeTextView.visibility = View.GONE
            }

        }

    }


}