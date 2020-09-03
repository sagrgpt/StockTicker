package com.showcase.pricetracker.usecase

import com.nhaarman.mockito_kotlin.any
import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.model.StockEntity
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class StockRecorderTest {

    @Mock
    private lateinit var remote: QuotationRemote

    @Mock
    private lateinit var schedulerProvider: SchedulerProvider

    private lateinit var recorder: StockRecorder

    private val relStockEntity = StockEntity(
        40.95f,
        2087.25f,
        1599131179,
        2138f,
        913.5f,
        9f,
        "RELI",
        124564
    )

    private val infyStockEntity = relStockEntity.copy(
        change = -20f,
        price = 10f,
        sid = "INFY",
    )

    private val hulStockEntity = infyStockEntity.copy(
        sid = "HUL",
        change = 0.2f,
        price = 12f
    )

    private val relStockEntity2 = relStockEntity.copy(
        change = 10f,
        price = 19f,
    )

    private val infyStockEntity2 = infyStockEntity.copy(
        change = 2f,
        price = 32f,
    )

    private val hulStockEntity2 = hulStockEntity.copy(
        change = -3f,
        price = 8.8f
    )

    private val stockList = listOf(
        relStockEntity,
        infyStockEntity,
        hulStockEntity
    )

    private val stockList2 = listOf(
        relStockEntity2,
        infyStockEntity2,
        hulStockEntity2
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        recorder = StockRecorder(remote, schedulerProvider)
    }

    @Test
    fun entityToWatchlistConversionTest() {

        val testScheduler = TestScheduler()

        doReturn(testScheduler)
            .`when`(schedulerProvider)
            .io

        doReturn(Single.just(stockList))
            .`when`(remote)
            .getStockQuotation(any())

        val testObserver = recorder.play()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertNoErrors()
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0) {
            assertThat(it.stockList.size, `is`(3))
            assertThat(it.stockList[0].isDayChangePositive, `is`(true))
            assertThat(it.stockList[1].isDayChangePositive, `is`(false))
            assertThat(it.stockList[0].name, `is`("RELIANCE INDUSTRIES"))
            assertThat(it.stockList[1].name, `is`("INFOSYS"))
            assertThat(it.stockList[2].name, `is`("HUL"))
            true
        }
        testScheduler.advanceTimeBy(5L, TimeUnit.SECONDS)
        testObserver.assertValueCount(2)
        testScheduler.advanceTimeBy(10L, TimeUnit.SECONDS)
        testObserver.assertValueCount(4)

        testObserver.dispose()
    }


    @Test
    fun stockRecordingTest() {

        val testScheduler = TestScheduler()

        doReturn(testScheduler)
            .`when`(schedulerProvider)
            .io

        doReturn(Single.just(stockList))
            .`when`(remote)
            .getStockQuotation(any())

        val testObserver = recorder.play()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertNoErrors()
        testScheduler.advanceTimeBy(20L, TimeUnit.SECONDS)

        assertThat(
            recorder.stockRecord["RELI"]!!.size,
            `is`(5)
        )

        assertThat(
            recorder.stockRecord.size,
            `is`(3)
        )

        testObserver.dispose()
    }

    @Test
    fun mostRecentExpensiveTest() {
        val testScheduler = TestScheduler()

        doReturn(testScheduler)
            .`when`(schedulerProvider)
            .io

        doReturn(Single.just(stockList))
            .`when`(remote)
            .getStockQuotation(any())

        val testObserver = recorder.play()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertNoErrors()
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)
        assertThat(recorder.stockRecord.size, `is`(3))
        assertThat(recorder.mostExpensiveSid, `is`("HUL"))

        doReturn(Single.just(stockList2))
            .`when`(remote)
            .getStockQuotation(any())

        testScheduler.advanceTimeBy(4L, TimeUnit.SECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(2)
        assertThat(recorder.stockRecord.size, `is`(3))
        assertThat(recorder.mostExpensiveSid, `is`("INFY"))

    }
}