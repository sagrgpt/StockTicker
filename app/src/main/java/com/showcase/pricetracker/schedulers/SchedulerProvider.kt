package com.showcase.pricetracker.schedulers

import io.reactivex.rxjava3.core.Scheduler


interface SchedulerProvider {
    val io: Scheduler
    val ui: Scheduler
    val computation: Scheduler
    val newThread: Scheduler
}