package com.showcase.pricetracker.ui.history

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.showcase.pricetracker.R
import com.showcase.pricetracker.usecase.Quote
import com.showcase.pricetracker.usecase.StockHistory
import java.lang.ref.WeakReference
import java.util.*

class ChartAdapter(
    context: Context
) {

    private val weakReference = WeakReference(context)
    private var chart: LineChart? = null

    fun setUpChartFor(
        lineChart: LineChart,
        stockHistory: StockHistory
    ) {
        chart = lineChart
        if (stockHistory.quotes.isNotEmpty())
            displayLineChart(stockHistory)
    }

    private fun displayLineChart(stockHistory: StockHistory) {
        if (stockHistory.change > 0) {
            setUpLineChart(stockHistory.quotes, true)
        } else {
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
            "%02d:%02d:%02d".format(
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                calendar[Calendar.SECOND]
            )
                .also { labelList.add(it) }
            entries.add(Entry(i.toFloat(), quotes[i].price))
        }

        chart?.xAxis?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labelList[value.toInt()]
            }
        }

        return LineDataSet(entries, "DataSet1")
    }

    private fun setChartProp() {
        chart?.apply {
            description?.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            xAxis?.apply {
                granularity = 1f
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
            weakReference.get()
                ?.let {
                    fillDrawable = ContextCompat.getDrawable(it, R.drawable.gradient_positive)
                    color = ContextCompat.getColor(it, R.color.positiveChange)
                }
        } else {
            weakReference.get()
                ?.let {
                    fillDrawable = ContextCompat.getDrawable(it, R.drawable.gradient_negative)
                    color = ContextCompat.getColor(it, R.color.negativeChange)
                }
        }
    }


}