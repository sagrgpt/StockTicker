package com.showcase.pricetracker.usecase.model

import java.util.*

data class StockEntity(
    val change: Float,
    val close: Float,
    val date: Date,
    val high: Float,
    val low: Float,
    val price: Float,
    val sid: String,
    val volume: Int
)