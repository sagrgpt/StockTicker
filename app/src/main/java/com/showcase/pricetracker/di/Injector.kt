package com.showcase.pricetracker.di

import com.showcase.pricetracker.ui.history.HistoryFragment
import com.showcase.pricetracker.ui.stocks.WatchListFragment

class Injector(private val presentationRoot: PresentationRoot) {

    fun inject(client: Any) {
        when (client) {
            is WatchListFragment -> injectDependencies(client)
            is HistoryFragment -> injectDependencies(client)
            else -> throw RuntimeException("Invalid view injection")
        }

    }

    private fun injectDependencies(client: WatchListFragment) {
        client.viewModelFactory = presentationRoot.getSharedVMFactory()
        client.adapter = presentationRoot.getWatchListAdapter(client.itemClickListener)
    }

    private fun injectDependencies(client: HistoryFragment) {
        client.chartAdapter = presentationRoot.getChartAdapter(client.requireContext())
    }
}