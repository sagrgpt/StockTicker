package com.showcase.pricetracker.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.showcase.pricetracker.R
import com.showcase.pricetracker.ui.SharedViewModel
import com.showcase.pricetracker.usecase.Quote
import com.showcase.pricetracker.usecase.StockHistory
import kotlinx.android.synthetic.main.history_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.history().observe(
            viewLifecycleOwner,
            { it?.let { observe(it) } }
        )
        Log.i("History", "${viewModel.sidInFocus} is in focus.")
    }

    private fun observe(stockHistory: StockHistory) {
        setPrice(stockHistory.price)
        setAbsoluteChange(stockHistory.change)
        setPercentChange(stockHistory.percentChange)
        if (stockHistory.change > 0) {
            setScreenAppearancePositive()
            setUpLineChart(stockHistory.quotes, true)
        } else {
            setScreenAppearanceNegative()
            setUpLineChart(stockHistory.quotes, false)
        }

    }

    private fun setUpLineChart(quotes: List<Quote>, isPositive: Boolean) {
        setUpChartData(quotes, isPositive)
        setChartProp()
    }

    private fun setUpChartData(quotes: List<Quote>, isPositive: Boolean) {
        val dataSet = getChartDataSet(quotes)
            .apply { setProps(isPositive) }
        chart?.data = LineData(dataSet)
            .apply { setValueTextSize(1f) }
    }

    private fun getChartDataSet(quotes: List<Quote>): LineDataSet {
        val calendar = Calendar.getInstance()
        val entries = mutableListOf<Entry>()
        val labelList = mutableListOf<String>()
        for (i in quotes.indices) {
            calendar.timeInMillis = quotes[i].epoch
            calendar[Calendar.SECOND]
            labelList.add(SimpleDateFormat("mm:ss", Locale.ROOT)
                .format(calendar.time))
            entries.add(Entry(i.toFloat(), quotes[i].price))
        }

        chart?.xAxis?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labelList[value.toInt()]
            }
        }

        return LineDataSet(entries, "DataSet1")
    }

    private fun LineDataSet.setProps(positive: Boolean) {
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.1f
        setDrawCircles(false)
        setDrawFilled(true)
        setDrawValues(false)
        setFill(positive)
    }

    private fun LineDataSet.setFill(positive: Boolean) {
        chart?.also {
            fillFormatter = IFillFormatter { _, _ ->
                it.axisLeft.axisMinimum
            }
        }
        if (positive) {
            context?.let {
                fillDrawable = ContextCompat.getDrawable(it, R.drawable.gradient_positive)
                color = ContextCompat.getColor(it, R.color.positiveChange)
            }
        } else {
            context?.let {
                fillDrawable = ContextCompat.getDrawable(it, R.drawable.gradient_negative)
                color = ContextCompat.getColor(it, R.color.negativeChange)
            }
        }
    }

    private fun setChartProp() {
        chart?.apply {
            description?.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            xAxis?.apply {
                isEnabled = true
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
            }
            axisLeft?.apply {
                isEnabled = true
                setDrawGridLines(false)
            }
            axisRight?.isEnabled = false
            legend?.isEnabled = false
            animateX(500, Easing.EaseInExpo)
        }

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
        val change = "(${newPercentChange})"
        percentChange?.text = change
    }

    private fun ImageView.setDrawable(id: Int) {
        setImageDrawable(ContextCompat.getDrawable(context, id))
    }

    private fun TextView.setColor(id: Int) {
        setTextColor(ContextCompat.getColor(context, id))
    }

}