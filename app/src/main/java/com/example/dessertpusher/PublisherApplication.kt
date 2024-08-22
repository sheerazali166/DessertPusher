package com.example.dessertpusher

import android.app.Application
import timber.log.Timber

class PublisherApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}