package com.showcase.pricetracker.di

import com.showcase.pricetracker.network.NetworkFactory
import com.showcase.pricetracker.schedulers.DefaultScheduler
import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.NetworkGateway
import com.showcase.pricetracker.usecase.QuotationRemote

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
