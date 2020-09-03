package com.showcase.pricetracker.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.showcase.pricetracker.R
import com.showcase.pricetracker.usecase.StockOverview

class WatchListViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val stockLabel = itemView.findViewById<TextView>(R.id.stockLabel)
    private val price = itemView.findViewById<TextView>(R.id.price)
    private val dayChangeImage = itemView.findViewById<ImageView>(R.id.dayChangeImage)

    fun bindData(
        item: StockOverview,
        listener: (StockOverview) -> Unit
    ) {

        setStockName(item)
        setStockPrice(item)
        setChangeIndicator(item)
        itemView.setOnClickListener { listener(item) }

    }

    private fun setStockName(item: StockOverview) {
        stockLabel?.text = item.name
    }

    private fun setStockPrice(item: StockOverview) {
        price?.apply {
            context.getString(R.string.rupees_symbol)
                .let { "$it ${item.price}" }
                .also { text = it }
        }
    }

    private fun setChangeIndicator(item: StockOverview) {
        if (item.isDayChangePositive)
            dayChangeImage?.setDrawable(R.drawable.ic_up_arrow)
        else
            dayChangeImage?.setDrawable(R.drawable.ic_down_arrow)
    }

    private fun ImageView.setDrawable(id: Int) {
        setImageDrawable(ContextCompat.getDrawable(context, id))
    }
}
