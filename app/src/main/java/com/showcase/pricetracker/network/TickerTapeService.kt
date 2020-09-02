package com.showcase.pricetracker.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TickerTapeService {

    @GET("stocks/quotes")
    fun getBulkQuotes(
        @Query("sids") ids: String
    ): Single<ApiResponse<List<StocksSchema>>>

}