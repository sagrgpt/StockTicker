package com.showcase.pricetracker.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.showcase.pricetracker.R
import com.showcase.pricetracker.ui.SharedViewModel
import com.showcase.pricetracker.ui.base.BaseFragment
import com.showcase.pricetracker.usecase.StockHistory
import kotlinx.android.synthetic.main.history_fragment.*

class HistoryFragment : BaseFragment() {

    private lateinit var viewModel: SharedViewModel

    lateinit var chartAdapter: ChartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getInjector().inject(this)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.history().observe(
            viewLifecycleOwner,
            { it?.let { observe(it) } }
        )
    }

    private fun observe(stockHistory: StockHistory) {
        setPrice(stockHistory.price)
        setAbsoluteChange(stockHistory.change)
        setPercentChange(stockHistory.percentChange)
        chartAdapter.setUpChartFor(chart, stockHistory)
        if (stockHistory.change > 0)
            setScreenAppearancePositive()
        else
            setScreenAppearanceNegative()
    }

    private fun setScreenAppearancePositive() {
        dayChangeImage?.setDrawable(R.drawable.ic_up_arrow)
        absoluteChange?.setColor(R.color.positiveChange)
    }

    private fun setScreenAppearanceNegative() {
        dayChangeImage?.setDrawable(R.drawable.ic_down_arrow)
        absoluteChange?.setColor(R.color.negativeChange)

    }

    private fun setPrice(newPrice: Float) {
        price?.text = newPrice.toString()
    }

    private fun setAbsoluteChange(newAbsChange: Float) {
        absoluteChange?.text = newAbsChange.toString()
    }

    private fun setPercentChange(newPercentChange: Float) {
        val change = "(%.2f)".format(newPercentChange)
        percentChange?.text = change
    }

    private fun ImageView.setDrawable(id: Int) {
        setImageDrawable(ContextCompat.getDrawable(context, id))
    }

    private fun TextView.setColor(id: Int) {
        setTextColor(ContextCompat.getColor(context, id))
    }

}