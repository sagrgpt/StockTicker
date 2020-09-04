package com.showcase.pricetracker.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.StockAnalyser
import com.showcase.pricetracker.usecase.StockHistory
import com.showcase.pricetracker.usecase.StockRecorder
import com.showcase.pricetracker.usecase.Watchlist
import io.reactivex.rxjava3.disposables.Disposable

/**
 * A shared view-model between
 * Watchlist and history screens.
 *
 * @param  recorder: A stock recorder use case to record stocks data
 * @param stockAnalyser: A analyser use case to get stock history
 * @param scheduler: Scheduler for threading requirements.
 */
class SharedViewModel(
    private val recorder: StockRecorder,
    private val stockAnalyser: StockAnalyser,
    private val scheduler: SchedulerProvider
) : ViewModel() {

    private var disposable: Disposable? = null
    private val watchListLiveData = MutableLiveData<Watchlist>()
    private val stockHistoryLiveData = MutableLiveData<StockHistory>()
    private var state = RecordingState.PAUSE

    /**
     * Use this to play/pause stocks recording.
     */
    fun toggleRecording() {
        if (state.isRecording())
            pauseRecorder()
        else
            playRecorder()
    }

    /**
     * Trigger analysis of history of the
     * most expensive stock known.
     */
    fun analyseStockHistory() {
        analyseStockHistory(recorder.mostExpensiveSid)
    }

    /**
     * Trigger analysis of history of a stock
     * @param sid: Sid of the stock
     */
    fun analyseStockHistory(sid: String) {
        pauseRecorder()
        getStockHistory(sid)
    }

    fun watchList(): LiveData<Watchlist> = watchListLiveData

    fun history(): LiveData<StockHistory> = stockHistoryLiveData

    fun isRecording() = state.isRecording()

    override fun onCleared() {
        super.onCleared()
        pauseRecorder()
    }

    private fun pauseRecorder() {
        disposable?.dispose()
        state = RecordingState.PAUSE
    }

    private fun playRecorder() {
        disposable = recorder.play()
            .subscribeOn(scheduler.io)
            .observeOn(scheduler.ui)
            .doOnSubscribe { state = RecordingState.PLAY }
            .subscribe(
                ::read
            ) { Log.e("WatchListVM", "Error while fetching watch list. ${it.message}") }
    }

    private fun getStockHistory(sid: String) {
        (recorder.stockRecord[sid] ?: emptyList())
            .let { stockAnalyser.getStockHistory(it) }
            .also { stockHistoryLiveData.value = it }
    }

    private fun read(watchlist: Watchlist) {
        watchListLiveData.value = watchlist
    }

    enum class RecordingState {
        PLAY, PAUSE;

        fun isRecording(): Boolean {
            return this == PLAY
        }
    }

    class SharedVmFactory(
        private val recorder: StockRecorder,
        private val stockAnalyser: StockAnalyser,
        private val scheduler: SchedulerProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SharedViewModel(recorder, stockAnalyser, scheduler) as T
        }
    }
}
