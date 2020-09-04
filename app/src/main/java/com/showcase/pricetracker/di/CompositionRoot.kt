package com.showcase.pricetracker.di

import com.showcase.pricetracker.network.NetworkFactory
import com.showcase.pricetracker.schedulers.DefaultScheduler
import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.NetworkGateway
import com.showcase.pricetracker.usecase.QuotationRemote

/**
 * Top level component in the dependency graph.
 * This component is attached to the top most android framework component in use.
 * Currently, this root is attached to Activity
 * @see [PresentationRoot]
 */
class CompositionRoot {

    private var networkGateway: NetworkGateway? = null
    private var scheduler: SchedulerProvider? = null

    fun getSchedulerProvider(): SchedulerProvider {
        return scheduler
            ?: DefaultScheduler()
    }

    fun getQuotationRemote(): QuotationRemote {
        return getNetworkGateway().getQuotationRemote()
    }

    private fun getNetworkGateway(): NetworkGateway {
        return networkGateway
            ?: NetworkFactory.createGateway()
    }
}
