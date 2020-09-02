package com.showcase.pricetracker.repository.schema

import com.google.gson.annotations.SerializedName


data class ApiResponse<T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("error")
    val error: String = "",
    @SerializedName("errorType")
    val errorType: String = ""
)

data class StocksSchema(
    @SerializedName("change")
    val change: Float = 0f,
    @SerializedName("close")
    val close: Float = 0f,
    @SerializedName("date")
    val date: String = "",
    @SerializedName("high")
    val high: Float = 0f,
    @SerializedName("low")
    val low: Float = 0f,
    @SerializedName("price")
    val price: Float = 0f,
    @SerializedName("sid")
    val sid: String = "",
    @SerializedName("volume")
    val volume: Int = 0
)