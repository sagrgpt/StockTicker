package com.showcase.pricetracker.ui

import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.StockRecorder
import org.mockito.Mock

class WatchListViewModelTest {

    @Mock
    private lateinit var recorder: StockRecorder

    @Mock
    private lateinit var scheduler: SchedulerProvider

    private lateinit var viewmodel: WatchListViewModel

}