package com.showcase.pricetracker.usecase

const val INTERVAL = 5000L

data class Stock(
    val change: Float,
    val close: Float,
    val epoch: Long,
    val high: Float,
    val low: Float,
    val price: Float,
    val sid: String,
    val volume: Int
)

data class StockOverview(
    val name: String,
    val sid: String,
    val price: Float,
    val isDayChangePositive: Boolean
)

data class Watchlist(
    val stockList: List<StockOverview> = emptyList()
)

data class QuoteHistory(
    val price: Float = 0f,
    val change: Float = 0f,
    val percentChange: Float = 0f,
    val epoch: Long = 0,
    val quotes: List<Stock> = emptyList()
)
