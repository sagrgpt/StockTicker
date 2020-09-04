package com.showcase.pricetracker.usecase

import com.showcase.pricetracker.usecase.model.StockEntity
import io.reactivex.rxjava3.core.Single

/**
 * Use this class to get access to the remote that provides quotation for stocks
 * This layer helps decouple the communication logic with repository logic
 */
interface QuotationRemote {

    /**
     * Get a list of stock quotation for each stock in the list provided.
     * @param sidList: List of sids of stocks for which quotation is needed
     * @return A list of quotation
     */
    fun getStockQuotation(
        sidList: List<String>
    ): Single<List<StockEntity>>

}