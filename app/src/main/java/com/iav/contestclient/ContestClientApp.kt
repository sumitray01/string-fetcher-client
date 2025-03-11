package com.iav.contestclient

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ContestClientApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
