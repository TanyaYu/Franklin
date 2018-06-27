package com.tanyayuferova.franklin

import android.support.v7.app.AppCompatDelegate

import com.tanyayuferova.franklin.di.DaggerApplicationComponent

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

class FranklinApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .builder()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        //todo is necessary?
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
