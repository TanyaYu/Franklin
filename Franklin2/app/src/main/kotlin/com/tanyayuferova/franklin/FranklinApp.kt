package com.tanyayuferova.franklin

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
class FranklinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}