package com.showcase.pricetracker.repository

import com.showcase.pricetracker.repository.schema.ApiResponse
import com.showcase.pricetracker.repository.schema.StocksSchema
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TickerTapeService {

    @GET("stocks/quotes")
    fun getBulkQuotes(
        @Query("sids") ids: String
    ): Single<ApiResponse<List<StocksSchema>>>

}