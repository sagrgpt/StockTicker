package com.showcase.pricetracker.usecase

import java.util.*

data class Stock(
    val change: Float,
    val close: Float,
    val date: Date,
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