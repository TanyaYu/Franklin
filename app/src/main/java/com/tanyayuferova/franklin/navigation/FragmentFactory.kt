package com.tanyayuferova.franklin.navigation

import android.support.v4.app.Fragment

import com.tanyayuferova.franklin.ui.onboarding.OnBoardingFragment
import com.tanyayuferova.franklin.ui.results.ResultsFragment
import com.tanyayuferova.franklin.ui.settings.SettingsFragment
import com.tanyayuferova.franklin.ui.virtues.VirtuesFragment

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

class FragmentFactory {

    fun createFragment(screenKey: String, data: Any?): Fragment = when (screenKey) {
        VirtuesFragment.SCREEN_KEY -> VirtuesFragment.newInstance(data)
        SettingsFragment.SCREEN_KEY -> SettingsFragment.newInstance(data)
        OnBoardingFragment.SCREEN_KEY -> OnBoardingFragment.newInstance(data)
        ResultsFragment.SCREEN_KEY ->  ResultsFragment.newInstance(data)
        else -> throw RuntimeException("Unknown screen key")
    }
}