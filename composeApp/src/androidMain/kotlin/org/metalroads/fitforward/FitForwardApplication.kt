package org.metalroads.fitforward

import android.app.Application
import core.di.initKoin

class FitForwardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}