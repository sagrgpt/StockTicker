package com.showcase.pricetracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.showcase.pricetracker.R
import com.showcase.pricetracker.usecase.StockOverview

class WatchListAdapter(
    var dataSet: List<StockOverview>,
    private val listener: (StockOverview) -> Unit
) : RecyclerView.Adapter<WatchListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListViewHolder {
        return WatchListViewHolder(parent.inflate(R.layout.watch_list_item))
    }

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        holder.bindData(dataSet[position], listener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun ViewGroup.inflate(
        @LayoutRes layoutRes: Int,
        attachToRoot: Boolean = false
    ): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

}