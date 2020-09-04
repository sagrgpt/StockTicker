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

class SharedViewModel(
    private val recorder: StockRecorder,
    private val stockAnalyser: StockAnalyser,
    private val scheduler: SchedulerProvider
) : ViewModel() {

    private var disposable: Disposable? = null
    private val watchListLiveData = MutableLiveData<Watchlist>()
    private val stockHistoryLiveData = MutableLiveData<StockHistory>()
    private var state = RecordingState.PAUSE
    var sidInFocus = ""

    fun toggleRecording() {
        if (state.isRecording())
            pauseRecorder()
        else
            playRecorder()
    }

    fun analyseStockHistory() {
        analyseStockHistory(recorder.mostExpensiveSid)
    }

    fun analyseStockHistory(sid: String) {
        sidInFocus = sid
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
