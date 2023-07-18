package com.example.devtransportationapp.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.devtransportationapp.R
import com.example.devtransportationapp.databinding.ActivityHomeScreenBinding
import com.example.devtransportationapp.databinding.ActivitySearchScreenBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class SearchScreen : AppCompatActivity() {
    private var binding : ActivitySearchScreenBinding? = null
    private val TAG = "SearchPLaces"
    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {

                    val place = Autocomplete.getPlaceFromIntent(intent)
                    val intent = Intent(this,SourceLocationScreen::class.java)
                    intent.putExtra("Latitude",place.latLng.latitude.toString())
                     intent.putExtra("Longitude",place.latLng.longitude.toString())
                    startActivity(intent)

                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "User canceled autocomplete")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        Places.initialize(applicationContext, "AIzaSyDgoscMvIQhSCWPnoREpISJv-witpxH2N8")


        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,Place.Field.ADDRESS_COMPONENTS)
        binding!!.tvSearchPlaces.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startAutocomplete.launch(intent)
        }
    }


}