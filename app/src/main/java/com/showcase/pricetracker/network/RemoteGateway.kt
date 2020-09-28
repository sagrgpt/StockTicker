package com.showcase.pricetracker.network

import com.showcase.pricetracker.usecase.NetworkGateway
import com.showcase.pricetracker.usecase.QuotationRemote

class RemoteGateway(
    private val stockQuoteService: StockQuoteService
) : NetworkGateway {

    override fun getQuotationRemote(): QuotationRemote {
        return StockQuoteRemote(stockQuoteService)
    }

}