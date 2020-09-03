package com.showcase.pricetracker.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.StockRecorder
import com.showcase.pricetracker.usecase.Watchlist
import io.reactivex.rxjava3.disposables.Disposable

class WatchListViewModel(
    private val recorder: StockRecorder,
    private val scheduler: SchedulerProvider
) : ViewModel() {

    private var disposable: Disposable? = null
    private val watchLiveData = MutableLiveData<Watchlist>()
    private var state = RecordingState.PAUSE

    fun toggleRecording() {
        if (state.isRecording())
            pauseRecorder()
        else
            playRecorder()
    }

    fun viewState(): LiveData<Watchlist> {
        return watchLiveData
    }

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

    private fun read(watchlist: Watchlist) {
        watchLiveData.value = watchlist
    }

    enum class RecordingState {
        PLAY, PAUSE;

        fun isRecording(): Boolean {
            return this == PLAY
        }
    }

    class WatchListVmFactory(
        private val recorder: StockRecorder,
        private val scheduler: SchedulerProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WatchListViewModel(recorder, scheduler) as T
        }
    }
}
