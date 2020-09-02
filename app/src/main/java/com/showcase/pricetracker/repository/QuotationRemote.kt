package com.showcase.pricetracker.repository

import com.showcase.pricetracker.repository.model.StockEntity
import io.reactivex.rxjava3.core.Single

interface QuotationRemote {

    fun getStockQuotation(
        sidList: List<String>
    ): Single<List<StockEntity>>

}