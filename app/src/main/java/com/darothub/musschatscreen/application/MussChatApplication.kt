package com.darothub.musschatscreen.application

import android.app.Application
import com.darothub.musschatscreen.application.di.ServiceLocator

class MussChatApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initialize(applicationContext)
    }
}