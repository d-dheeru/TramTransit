package com.gotranspo.tramtransit.ui.home.directionbymodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devtransportationapp.model.directions.DirectionsResponse

import com.gotranspo.tramtransit.remote.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DirectionViewModel @Inject constructor(private val directionRepo: DirectionRepo) : ViewModel() {
    private val _directionsData = MutableLiveData<DirectionsResponse>()
    val directionsData: LiveData<DirectionsResponse> = _directionsData

     fun getdirectionLists(origin : String,
                           destination : String,
                           mode : String,
                           key : String ){
         viewModelScope.launch {
             directionRepo.getDirections(origin,destination,mode,key).collect{

                 when(it){
                    is  NetworkResult.Loading -> {

                     }
                     is  NetworkResult.Success -> {
                         _directionsData.postValue(it.data!!)

                     }
                     is  NetworkResult.Failure -> {

                     }
                     else -> {}
                 }
             }
         }

     }


}