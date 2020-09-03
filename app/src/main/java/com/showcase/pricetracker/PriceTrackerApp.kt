package com.showcase.pricetracker

import android.app.Application
import com.showcase.pricetracker.schedulers.SchedulerProvider
import com.showcase.pricetracker.usecase.NetworkGateway

class PriceTrackerApp : Application() {

    lateinit var networkGateway: NetworkGateway
    lateinit var schedulerProvider: SchedulerProvider

}