package com.showcase.pricetracker.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit interface that exposes the Ticker Tape APIs
 */
interface TickerTapeService {

    /**
     * Get quotation for a multiple stocks.
     * @param ids: The unique SID for stocks separated with comma ","
     * @return A list of stock schema object that is derived from server's json response
     * wrapped inside an api response schema.
     */
    @GET("stocks/quotes")
    fun getBulkQuotes(
        @Query("sids") ids: String
    ): Single<ApiResponse<List<StocksSchema>>>

}