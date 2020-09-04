package com.showcase.pricetracker.usecase

const val INTERVAL = 5000L

/**
 * A data model that describes the quotation for a given stock SID
 */
data class Quote(
    val change: Float,
    val close: Float,
    val epoch: Long,
    val high: Float,
    val low: Float,
    val price: Float,
    val sid: String,
    val volume: Int
)

/**
 * A data model that describes a simplified
 * version of quotation for a given stock

 * -> This is the lite version of [Quote] <-
 */
data class StockOverview(
    val name: String,
    val sid: String,
    val price: Float,
    val isDayChangePositive: Boolean
)

data class Watchlist(
    val stockList: List<StockOverview> = emptyList()
)

/**
 * A data model that describes an overview
 * and all known quotation history for a stock.
 */
data class StockHistory(
    val sid: String = "",
    val price: Float = 0f,
    val change: Float = 0f,
    val percentChange: Float = 0f,
    val epoch: Long = 0,
    val quotes: List<Quote> = emptyList()
)
