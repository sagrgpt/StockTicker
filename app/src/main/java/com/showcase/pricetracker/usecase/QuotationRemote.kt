package com.showcase.pricetracker.usecase

import com.showcase.pricetracker.usecase.model.StockEntity
import io.reactivex.rxjava3.core.Single

interface QuotationRemote {

    fun getStockQuotation(
        sidList: List<String>
    ): Single<List<StockEntity>>

}