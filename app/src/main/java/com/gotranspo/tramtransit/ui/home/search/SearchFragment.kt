package com.gotranspo.tramtransit.ui.home.search


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.databinding.FragmentSearchBinding
import com.gotranspo.tramtransit.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val TAG = "SearchFragment"
    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {


                    val place = Autocomplete.getPlaceFromIntent(intent)
                    val bundle = Bundle()
                    bundle.putString("Latitude", place.latLng.latitude.toString())
                    bundle.putString("Longitude", place.latLng.longitude.toString())
                    findNavController().navigate(R.id.directionFragment, bundle)


                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "User canceled autocomplete")
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        Places.initialize(requireContext(), Constants.API_KEY)
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS
        )

        binding.tvSearchPlaces.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startAutocomplete.launch(intent)
        }

        return binding.root
    }

}