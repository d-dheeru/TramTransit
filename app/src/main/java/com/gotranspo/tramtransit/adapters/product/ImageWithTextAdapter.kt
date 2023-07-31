import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.databinding.ProductItemImageDescriptionBinding

class ImageWithTextAdapter(private var items: List<ProductItemData>) :
    RecyclerView.Adapter<ImageWithTextAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ProductItemImageDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageList = listOf(
            R.drawable.shop,
            R.drawable.map,
            R.drawable.car,
            // Add more image resource IDs as needed
        )

        fun bind(itemData: ProductItemData) {
            binding.imageView.setImageResource(itemData.imageId)
            binding.itemName.text = itemData.itemName
            binding.itemSize.text = itemData.itemSize
            binding.itemCost.text = "${itemData.currency} ${itemData.itemCost}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageWithTextAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemImageDescriptionBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImageWithTextAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // New method to update the list of items
    fun setItems(newItems: List<ProductItemData>) {
        items = newItems
        notifyDataSetChanged()
    }
}
