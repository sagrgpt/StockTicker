package com.showcase.pricetracker.repository

interface NetworkGateway {

    fun getQuotationRemote(): QuotationRemote

}