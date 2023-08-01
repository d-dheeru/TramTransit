package com.gotranspo.tramtransit.adapters.product

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gotranspo.tramtransit.data.model.directions.product.ProductItemData
import com.gotranspo.tramtransit.databinding.ProductItemImageDescriptionBinding

class ImageWithTextAdapter(
    private var items: List<ProductItemData>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ImageWithTextAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ProductItemImageDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.leftImage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < items.size) {
                    val item = items[position]
                    var itemAddedCount = binding.itemCount.text.toString().toIntOrNull() ?: 0
                    if (itemAddedCount > 0) {
                        binding.itemCount.text = "${--itemAddedCount}"
                    }
                    itemClickListener.onLeftImageClick(item, itemAddedCount)
                }
            }

            binding.rightImage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < items.size) {
                    val item = items[position]
                    var itemAddedCount = binding.itemCount.text.toString().toIntOrNull() ?: 0
                    if (itemAddedCount < item.itemAvailableCount) {
                        binding.itemCount.text = "${++itemAddedCount}"
                        itemClickListener.onRightImageClick(item, itemAddedCount)
                    } else {
                        // You can show a toast or handle the scenario when the count reaches the itemSize limit
                        // For example:
                        Toast.makeText(
                            itemView.context,
                            "Cannot add more items. Reached the limit.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        fun bind(itemData: ProductItemData) {
            binding.imageView.setImageResource(itemData.imageId)
            binding.itemName.text = itemData.itemName
            binding.itemSize.text = itemData.itemSize
            "${itemData.currency} ${itemData.itemCost}".also { binding.itemCost.text = it }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemImageDescriptionBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
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

    // Define an interface for click listeners
    interface OnItemClickListener {
        fun onLeftImageClick(item: ProductItemData, itemAddedCount: Int)
        fun onRightImageClick(item: ProductItemData, itemAddedCount: Int)
    }
}
