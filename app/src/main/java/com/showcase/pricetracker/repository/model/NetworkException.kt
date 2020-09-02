package com.showcase.pricetracker.repository.model

data class NetworkException(
    val code: Int,
    override val message: String
) : Exception(message)