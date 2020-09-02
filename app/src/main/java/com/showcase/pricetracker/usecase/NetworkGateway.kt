package com.showcase.pricetracker.usecase

interface NetworkGateway {

    fun getQuotationRemote(): QuotationRemote

}