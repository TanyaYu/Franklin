package com.tanyayuferova.franklin.di

import com.tanyayuferova.franklin.FranklinApplication

import javax.inject.Singleton

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by Tanya Yuferova on 5/26/2018.
 */
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    NavigationModule::class
])
@Singleton
interface ApplicationComponent : AndroidInjector<FranklinApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<FranklinApplication>()
}