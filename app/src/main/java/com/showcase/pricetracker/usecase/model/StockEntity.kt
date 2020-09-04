package com.showcase.pricetracker.usecase.model

/**
 * A data model that should be returned from the network remotes.
 */
data class StockEntity(
    val change: Float,
    val close: Float,
    var epoch: Long,
    val high: Float,
    val low: Float,
    val price: Float,
    val sid: String,
    val volume: Int
)