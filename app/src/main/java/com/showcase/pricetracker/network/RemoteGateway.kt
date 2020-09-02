package com.showcase.pricetracker.network

import com.showcase.pricetracker.usecase.NetworkGateway
import com.showcase.pricetracker.usecase.QuotationRemote

class RemoteGateway(
    private val tickerTapeService: TickerTapeService
) : NetworkGateway {

    override fun getQuotationRemote(): QuotationRemote {
        return TickerTapeRemote(tickerTapeService)
    }

}