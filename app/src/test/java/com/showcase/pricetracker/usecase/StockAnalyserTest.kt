package com.showcase.pricetracker.usecase

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class StockAnalyserTest {

    //region Stock quotes test data
    private val quote1 = Quote(
        5f,
        2087.25f,
        1599131179,
        2138f,
        913.5f,
        10f,
        "RELI",
        124564
    )

    private val quote2 = quote1.copy(
        price = 5f,
        epoch = quote1.epoch + 5000,
        change = 0f
    )

    private val quote3 = quote2.copy(
        price = 20f,
        epoch = quote2.epoch + 5000,
        change = 15f
    )

    val quote4 = quote3.copy(
        price = 155f,
        change = 150f,
        epoch = quote3.epoch + 7000
    )

    val quote5 = quote3.copy(
        price = 1f,
        change = -4f,
        epoch = quote3.epoch + 25000
    )

    val quote6 = quote5.copy(
        price = 55f,
        change = 50f,
        epoch = quote5.epoch + 13000
    )

    val quote7 = quote6.copy(
        price = 30f,
        change = 25f,
        epoch = quote6.epoch + 5000
    )
    //endregion

    private val usecase = StockAnalyser()

    @Test
    fun singleStockInRecordTest() {
        val quoteList = listOf(quote1)

        val actualValue = usecase.getStockHistory(quoteList)

        assertThat(actualValue.change, `is`(quote1.change))
        assertThat(actualValue.price, `is`(quote1.price))
        assertThat(actualValue.epoch, `is`(quote1.epoch))
        assertThat(actualValue.quotes, `is`(emptyList()))
    }

    @Test
    fun twoQuotesInRecordTest() {
        val quoteList = listOf(quote1, quote2)

        val actualValue = usecase.getStockHistory(quoteList)

        assertThat(actualValue.change, `is`(quote2.change))
        assertThat(actualValue.price, `is`(quote2.price))
        assertThat(actualValue.epoch, `is`(quote2.epoch))
        assertThat(actualValue.quotes, `is`(quoteList))
    }

    @Test
    fun emptyQuotesListTest() {
        val actualValue = usecase.getStockHistory(emptyList())
        assertThat(actualValue, `is`(StockHistory()))
    }

    @Test
    fun multipleRecordingTest() {
        val quote8 = quote3.copy(
            price = 25f,
            change = 20f,
            epoch = quote3.epoch + 5000 + 5000
        )

        val actualValue = usecase.getStockHistory(listOf(quote1, quote2, quote3, quote8))

        val insertedQuote = actualValue.quotes[3]
        assertThat(actualValue.quotes.size, `is`(5))

        assertThat(insertedQuote.price, `is`(quote3.price))
        assertThat(insertedQuote.epoch, `is`(quote3.epoch + 5000))
    }

    @Test
    fun lowNetworkRecordingTest() {
        val stockList = listOf(
            quote1,
            quote2,
            quote3,
            quote4,
            quote5,
            quote6,
            quote7
        )

        val actualValue = usecase.getStockHistory(stockList)
        val quotationList = actualValue.quotes


        assertThat(quotationList[4].price, `is`(quote4.price))
        assertThat(quotationList[5].price, `is`(quote4.price))
        assertThat(quotationList[7].price, `is`(quote5.price))

    }

}