package com.showcase.pricetracker.repository

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.showcase.pricetracker.repository.model.NetworkException
import com.showcase.pricetracker.repository.network.ApiResponse
import com.showcase.pricetracker.repository.network.StocksSchema
import com.showcase.pricetracker.repository.network.TickerTapeRemote
import com.showcase.pricetracker.repository.network.TickerTapeService
import io.reactivex.rxjava3.core.Single
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TickerTapeRemoteTest {

    @Mock
    private lateinit var service: TickerTapeService

    private lateinit var remote: TickerTapeRemote

    //region Stock json strings
    private val reilStock = "{\n" +
        "            \"sid\": \"RELI\",\n" +
        "            \"price\": 2087.25,\n" +
        "            \"close\": 2080.7,\n" +
        "            \"change\": 6.55,\n" +
        "            \"high\": 2122.4,\n" +
        "            \"low\": 2062.4,\n" +
        "            \"volume\": 17821488,\n" +
        "            \"date\": \"2020-09-01T10:29:59.812Z\"\n" +
        "        }"

    private val hdbkStock = "{\n" +
        "            \"sid\": \"HDBK\",\n" +
        "            \"price\": 1127.3,\n" +
        "            \"close\": 1115.85,\n" +
        "            \"change\": 11.45,\n" +
        "            \"high\": 1143.6,\n" +
        "            \"low\": 1108,\n" +
        "            \"volume\": 14438949,\n" +
        "            \"date\": \"2020-09-01T10:29:59.812Z\"\n" +
        "        }"
    //endregion

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remote = TickerTapeRemote(service)
    }

    @Test
    fun givenListOfSids_sendSingleStringToApiRequest() {

        val sidsList = listOf(
            "TCS",
            "RELI",
            "HDBK",
            "INFY",
            "HLL",
            "ITC"
        )

        Mockito.`when`(service.getBulkQuotes(any()))
            .thenReturn(Single.just(getSuccessResponse()))

        remote.getStockQuotation(sidsList)

        verify(service).getBulkQuotes("TCS%2CRELI%2CHDBK%2CINFY%2CHLL%2CITC")
    }

    @Test
    fun onSuccessResponse_convertToEntities() {

        val sidsList = listOf(
            "RELI",
            "HDBK"
        )

        Mockito.`when`(service.getBulkQuotes(any()))
            .thenReturn(Single.just(getSuccessResponse()))


        val testObserver = remote.getStockQuotation(sidsList).test()

        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0) {
            assertThat(it[0].price, `is`(2087.25F))
            true
        }
    }

    @Test
    fun onFailureResponse_throwNetworkException() {
        val sidsList = listOf(
            "RELI",
            "HDBK"
        )

        Mockito.`when`(service.getBulkQuotes(any()))
            .thenReturn(Single.just(getFailureResponse()))


        val testObserver = remote.getStockQuotation(sidsList).test()

        testObserver.assertValueCount(0)
        testObserver.assertError(
            NetworkException(
                400,
                "Message: Missing sid in request Error Type: INVALID_INPUT"
            )
        )
    }


    private fun getSuccessResponse(): ApiResponse<List<StocksSchema>> {
        val relianceStock = Gson().fromJson(reilStock, StocksSchema::class.java)
        val hdbStock = Gson().fromJson(hdbkStock, StocksSchema::class.java)

        return ApiResponse(
            data = listOf(relianceStock, hdbStock),
            success = true
        )
    }

    private fun getFailureResponse(): ApiResponse<List<StocksSchema>> {

        return ApiResponse(
            data = null,
            success = false,
            error = "Missing sid in request",
            errorType = "INVALID_INPUT"
        )
    }

}