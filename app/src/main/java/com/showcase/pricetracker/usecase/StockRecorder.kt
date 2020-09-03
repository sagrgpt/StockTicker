package com.showcase.pricetracker.usecase

import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.model.StockEntity
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class StockRecorder(
    private val remote: QuotationRemote,
    private val schedulerProvider: SchedulerProvider
) {

    private val sids = listOf(
        "TCS",
        "RELI",
        "HDBK",
        "INFY",
        "HLL",
        "ITC",
    )
    private var highestPrice = Float.NEGATIVE_INFINITY
    val stockRecord = mutableMapOf<String, List<Quote>>()
    var mostExpensiveSid: String = ""

    fun play(): Observable<Watchlist> {
        return Observable.interval(
            0,
            INTERVAL,
            TimeUnit.MILLISECONDS,
            schedulerProvider.io
        )
            .flatMapSingle { remote.getStockQuotation(sids) }
            .filter {
                it.isNotEmpty()
            }
            .distinctUntilChanged()
            .doOnNext { it?.let { record(it) } }
            .map { it.toWatchList() }
    }

    private fun List<StockEntity>.toWatchList(): Watchlist {
        val stockList = mutableListOf<StockOverview>()
        forEach { stockList.add(it.toStockOverview()) }
        return Watchlist(stockList)
    }

    private fun record(stockEntityList: List<StockEntity>) {
        highestPrice = Float.NEGATIVE_INFINITY
        stockEntityList.forEach { item ->
            recordExpensiveStock(item)
            storeStockHistory(item)
        }
    }

    private fun storeStockHistory(item: StockEntity) {
        val stock = item.toStock()
        val currentList = stockRecord[item.sid]
            ?: emptyList()
        currentList.toMutableList()
            .apply { add(stock) }
            .also { stockRecord[item.sid] = it }
    }

    private fun recordExpensiveStock(stock: StockEntity) {
        if (stock.price > highestPrice) {
            mostExpensiveSid = stock.sid
            highestPrice = stock.price
        }
    }

    private fun StockEntity.toStockOverview(): StockOverview {
        val name = when (sid) {
            "RELI" -> "RELIANCE INDUSTRIES"
            "TCS" -> "TATA CONSULTANCY SERVICES"
            "ITC" -> "ITC Ltd"
            "HDBK" -> "HDFC BANK"
            "INFY" -> "INFOSYS"
            else -> sid
        }
        return StockOverview(name, sid, price, change > 0)
    }

    private fun StockEntity.toStock(): Quote {
        return Quote(
            sid = sid,
            price = price,
            epoch = epoch,
            low = low,
            high = high,
            close = close,
            change = change,
            volume = volume,
        )
    }
}
