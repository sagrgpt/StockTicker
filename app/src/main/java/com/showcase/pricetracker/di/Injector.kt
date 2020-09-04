package com.showcase.pricetracker.di

import com.showcase.pricetracker.ui.history.HistoryFragment
import com.showcase.pricetracker.ui.stocks.WatchListFragment

/**
 * Injection framework to inject public dependencies of any class.
 */
class Injector(private val presentationRoot: PresentationRoot) {

    /**
     * @param client
     *  The object which needs dependencies injected into
     */
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