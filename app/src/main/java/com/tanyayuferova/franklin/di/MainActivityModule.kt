package com.tanyayuferova.franklin.di

import com.tanyayuferova.franklin.ui.onboarding.OnBoardingFragment
import com.tanyayuferova.franklin.ui.results.ResultsFragment
import com.tanyayuferova.franklin.ui.settings.SettingsFragment
import com.tanyayuferova.franklin.ui.virtues.VirtuesFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Author: Tanya Iuferova
 * Date: 6/26/2018
 */
@Module
@ActivityScope
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun bindVirtuesFragment(): VirtuesFragment

    @ContributesAndroidInjector
    abstract fun bindSettingFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun bindResultsFragment(): ResultsFragment

    @ContributesAndroidInjector
    abstract fun bindOnBoardingFragment(): OnBoardingFragment

    /*
    @Provides
    static MainPresenter provideMainPresenter(MainView mainView, ApiService apiService) {
        return new MainPresenterImpl(mainView, apiService);
    }

    @Binds
    abstract MainView provideMainView(MainActivity mainActivity);
     */
}
