package com.tanyayuferova.franklin.utils

import androidx.annotation.MainThread
import androidx.lifecycle.*

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
fun <X, Y> LiveData<X>.map(transform: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, transform)
}

fun <X, Y> LiveData<List<X>>.mapList(transform: (X) -> Y): LiveData<List<Y>> {
    return map { it.map(transform) }
}

fun <X, Y> LiveData<X>.switchMap(transform: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, transform)
}

fun <X> LiveData<X>.distinctUntilChanged(): LiveData<X> {
    return Transformations.distinctUntilChanged(this)
}

fun <X> X.toLiveData(): LiveData<X> {
    return MutableLiveData<X>().apply { value = this@toLiveData }
}

fun <X> X.toMutableLiveData(): MutableLiveData<X> {
    return MutableLiveData<X>().apply { value = this@toMutableLiveData }
}

val <X> LiveData<X>.requireValue: X get() {
    return value ?: throw Exception("You request LiveData value too early.")
}

fun <X> LiveData<X>.onActive(onActive: () -> Unit): LiveData<X> {
    return ActiveLiveDataMediator<X>(onActive).apply {
        addSource(this@onActive, this::setValue)
    }
}

fun <X> LiveData<X>.onInactive(onInactive: () -> Unit): LiveData<X> {
    return InactiveLiveDataMediator<X>(onInactive).apply {
        addSource(this@onInactive, this::setValue)
    }
}

//fixme copied from androidx.lifecycle.observe.LiveData.kt
@MainThread
inline fun <T> LiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Observer<T> {
    val wrappedObserver = Observer<T> { t -> onChanged.invoke(t) }
    observe(owner, wrappedObserver)
    return wrappedObserver
}
