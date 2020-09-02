package com.showcase.pricetracker.usecase.model

data class NetworkException(
    val code: Int,
    override val message: String
) : Exception(message)