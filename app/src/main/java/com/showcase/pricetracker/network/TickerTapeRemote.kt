package com.showcase.pricetracker.network

import com.showcase.pricetracker.usecase.QuotationRemote
import com.showcase.pricetracker.usecase.model.NetworkException
import com.showcase.pricetracker.usecase.model.StockEntity
import io.reactivex.rxjava3.core.Single
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TickerTapeRemote(
    private val service: TickerTapeService
) : QuotationRemote {

    override fun getStockQuotation(
        sidList: List<String>
    ): Single<List<StockEntity>> {

        return service.getBulkQuotes(sidList.toParamString())
            .map {
                it?.let { getSuccessResponse(it) }
            }
    }

    private fun getSuccessResponse(
        response: ApiResponse<List<StocksSchema>>
    ): List<StockEntity> {

        return when (response.success) {
            true -> response.data
                ?.toStockList()
                ?: throw NetworkException(200, "Stock list is null")
            false -> throw NetworkException(400, "Message: ${response.error} Error Type: ${response.errorType}")
        }

    }

    private fun List<StocksSchema>.toStockList(): List<StockEntity> {
        val stockList = mutableListOf<StockEntity>()
        forEach {
            stockList.add(it.toStockEntity())
        }
        return stockList
    }

    private fun List<String>.toParamString(): String {
        var queryParam = ""
        forEach { queryParam += "$it," }
        return queryParam.removeSuffix(",")
    }

    private fun StocksSchema.toStockEntity(): StockEntity {
        return StockEntity(
            sid = sid,
            volume = volume,
            change = change,
            close = close,
            high = high,
            low = low,
            price = price,
            epoch = getTimeInMillis(date)
        )
    }

    private fun getTimeInMillis(date: String): Long {
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSz",
            Locale.ROOT
        ).run { parse(date.replace("Z", "GMT")) }
            ?: throw ParseException("Unable to parse $date", 0)
        return cal.timeInMillis
    }

}
