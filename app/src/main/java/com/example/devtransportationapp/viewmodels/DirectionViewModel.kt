package com.example.devtransportationapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devtransportationapp.model.directions.DirectionsResponse
import com.example.devtransportationapp.remote.NetworkResult
import com.example.devtransportationapp.repository.DirectionRespository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DirectionViewModel : ViewModel() {
    private val directionRepo : DirectionRespository
    get() = DirectionRespository()

    private val _directionsData = MutableLiveData<DirectionsResponse>()
    val directionsData: LiveData<DirectionsResponse> =  _directionsData

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
                        _directionsData.postValue(it.data)

                    }
                    is  NetworkResult.Failure -> {

                    }
                    else -> {}
                }
            }
        }

    }








}