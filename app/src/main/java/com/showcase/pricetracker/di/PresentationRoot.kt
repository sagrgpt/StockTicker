package com.showcase.pricetracker.di

import android.content.Context
import com.showcase.pricetracker.ui.SharedViewModel
import com.showcase.pricetracker.ui.history.ChartAdapter
import com.showcase.pricetracker.ui.stocks.WatchListAdapter
import com.showcase.pricetracker.usecase.StockAnalyser
import com.showcase.pricetracker.usecase.StockOverview
import com.showcase.pricetracker.usecase.StockRecorder

/**
 * Second level component in the dependency graph.
 * This root is synonymous to presentation layer.
 * Currently, this is attached to every fragment
 * @see [CompositionRoot]
 */
class PresentationRoot(
    private val compositionRoot: CompositionRoot,
) {

    fun getStockRecorderUseCase(): StockRecorder {
        return StockRecorder(
            compositionRoot.getQuotationRemote(),
            compositionRoot.getSchedulerProvider()
        )
    }

    fun getStockAnalyserUseCase(): StockAnalyser {
        return StockAnalyser()
    }

    fun getSharedVMFactory(): SharedViewModel.SharedVmFactory {
        return SharedViewModel.SharedVmFactory(
            getStockRecorderUseCase(),
            getStockAnalyserUseCase(),
            compositionRoot.getSchedulerProvider()
        )
    }

    fun getWatchListAdapter(listener: (StockOverview) -> Unit): WatchListAdapter {
        return WatchListAdapter(emptyList(), listener)
    }

    fun getChartAdapter(fragmentContext: Context): ChartAdapter {
        return ChartAdapter(fragmentContext)
    }

}