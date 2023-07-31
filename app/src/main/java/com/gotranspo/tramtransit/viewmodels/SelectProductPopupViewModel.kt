import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class SelectProductPopupViewModel : ViewModel() {

    // MutableLiveData to hold the product items
    private val _productItemsLiveData = MutableLiveData<List<ProductItemData>>()
    val productItemsLiveData: LiveData<List<ProductItemData>> get() = _productItemsLiveData

    // Example method to fetch product items (replace this with your actual logic)
    fun fetchProductItems() {
        // Replace this with your actual data source or API call
        val productItems = listOf(
            ProductItemData(R.drawable.coffee_1, "Ice coffee", "small", 2.59, "$", true, 9),
            ProductItemData(R.drawable.coffee_2, "Ice coffee", "medium", 3.59, "$", true, 6),
            ProductItemData(R.drawable.coffee_3, "Ice coffee", "large", 4.59, "$", false, 3),
            ProductItemData(R.drawable.coffee_4, "Cappuccino", "small", 5.59, "$", true, 9),
            ProductItemData(R.drawable.coffee_5, "Hot Chocolate coffee", "small", 6.09, "$", true, 9)
        )

        // Update the LiveData with the new product items
        _productItemsLiveData.postValue(productItems)
    }
}
