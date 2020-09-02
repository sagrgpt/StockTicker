package com.showcase.pricetracker.repository.network

import com.showcase.pricetracker.repository.NetworkGateway
import com.showcase.pricetracker.repository.QuotationRemote

class RemoteGateway(
    private val tickerTapeService: TickerTapeService
) : NetworkGateway {

    override fun getQuotationRemote(): QuotationRemote {
        return TickerTapeRemote(tickerTapeService)
    }

}