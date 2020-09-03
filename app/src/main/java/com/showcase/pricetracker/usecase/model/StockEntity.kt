package com.showcase.pricetracker.usecase.model

data class StockEntity(
    val change: Float,
    val close: Float,
    val epoch: Long,
    val high: Float,
    val low: Float,
    val price: Float,
    val sid: String,
    val volume: Int
)