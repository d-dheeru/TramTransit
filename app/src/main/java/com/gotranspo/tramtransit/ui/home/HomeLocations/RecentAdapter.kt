package com.gotranspo.tramtransit.ui.home.HomeLocations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.gotranspo.tramtransit.R
import com.gotranspo.tramtransit.db.entities.DestinationEnitity

class RecentAdapter(private val destinations: List<DestinationEnitity>) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val recentTextView: MaterialTextView = itemView.findViewById(R.id.recentsearches)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_recent_searches, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return destinations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recentTextView.text = destinations[position].address

    }
}