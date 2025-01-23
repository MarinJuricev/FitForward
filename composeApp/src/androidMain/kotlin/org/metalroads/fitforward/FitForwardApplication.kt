package org.metalroads.fitforward

import android.app.Application
import core.di.initKoin
import org.koin.android.ext.koin.androidContext

class FitForwardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FitForwardApplication)
        }
    }
}