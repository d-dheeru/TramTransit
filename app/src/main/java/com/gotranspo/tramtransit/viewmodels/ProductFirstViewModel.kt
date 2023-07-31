package com.gotranspo.tramtransit.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.remote.NetworkResult
import com.gotranspo.tramtransit.remote.ProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductFirstViewModel @Inject constructor(
    private val productsRepo: ProductsRepo
): ViewModel() {

    // LiveData to hold the product items data
    private val _productItemsLiveData = MutableLiveData<List<ProductItemData>>()
    val productItemsLiveData: LiveData<List<ProductItemData>> get() = _productItemsLiveData

    // Function to set the product items data
    fun setProductItemsData(productItems: List<ProductItemData>) {
        _productItemsLiveData.value = productItems
    }

    fun getProducts(){
        viewModelScope.launch {

            productsRepo.getProducts().collect{
                when(it){
                    is NetworkResult.Loading -> {
                        Log.d("ProductFirstVM", "IS LOADING")
                    }

                    is NetworkResult.Success -> {
                        _productItemsLiveData.postValue(it.data!!)
                    }

                    is NetworkResult.Failure -> {
                        Log.d("ProductFirstVM", "FAILED")
                    }
                }
            }
        }
    }
}
