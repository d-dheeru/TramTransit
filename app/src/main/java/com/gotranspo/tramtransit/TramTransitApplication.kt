package com.gotranspo.tramtransit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TramTransitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}