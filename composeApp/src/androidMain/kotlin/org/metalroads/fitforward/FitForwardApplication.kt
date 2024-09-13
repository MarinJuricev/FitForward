package org.metalroads.fitforward

import android.app.Application
import core.di.AndroidApplicationComponent
import core.di.create

class FitForwardApplication : Application() {

    val component: AndroidApplicationComponent by lazy(LazyThreadSafetyMode.NONE)  {
        AndroidApplicationComponent.create(this)
    }
    override fun onCreate() {
        super.onCreate()
    }
}