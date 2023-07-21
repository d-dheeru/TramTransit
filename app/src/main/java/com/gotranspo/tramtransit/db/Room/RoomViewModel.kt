package com.gotranspo.tramtransit.db.Room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotranspo.tramtransit.db.entities.DestinationEnitity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(private val roomRepository: RoomRepository) : ViewModel() {
    private val _allDestinations = MutableLiveData<List<DestinationEnitity>>()
    val allDestinations: LiveData<List<DestinationEnitity>> = _allDestinations
    fun insertRecord(destinationEnitity: DestinationEnitity)
    {
        viewModelScope.launch {
            roomRepository.insertRecord(destinationEnitity)
        }

    }
    fun fetchDestinations()
    {
        viewModelScope.launch {
          _allDestinations.postValue(roomRepository.fetchDestinations())
        }
    }


}