package com.tanyayuferova.franklin.utils

import androidx.lifecycle.MediatorLiveData

/**
 * Author: Tanya Yuferova
 * Date: 7/9/19
 */
class ActiveLiveDataMediator<T>(
    private val onActive: () -> Unit = {}
) : MediatorLiveData<T>() {

    override fun onActive() {
        super.onActive()
        onActive.invoke()
    }
}

class InactiveLiveDataMediator<T>(
    private val onInactive: () -> Unit = {}
) : MediatorLiveData<T>() {

    override fun onInactive() {
        super.onInactive()
        onInactive.invoke()
    }
}
