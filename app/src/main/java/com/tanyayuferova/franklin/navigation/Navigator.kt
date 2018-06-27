package com.tanyayuferova.franklin.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.tanyayuferova.franklin.R
import ru.terrakok.cicerone.android.SupportFragmentNavigator

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class Navigator(
    private val activity: FragmentActivity
): SupportFragmentNavigator(
    activity.supportFragmentManager,
    R.id.container
) {
    private val fragmentFactory = FragmentFactory()

    override fun createFragment(screenKey: String, data: Any?): Fragment {
        return fragmentFactory.createFragment(screenKey, data)
    }

    override fun exit() {
        activity.finish()
    }

    override fun showSystemMessage(message: String) {
        throw Exception("Not implemented")
    }
}