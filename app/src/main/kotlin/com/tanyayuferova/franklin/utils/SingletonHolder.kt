package com.tanyayuferova.franklin.utils

/**
 * Author: Tanya Yuferova
 * Date: 7/9/19
 */
open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {

    @Volatile
    private var instance: T? = null
    private var creator: ((A) -> T)? = creator

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }
        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}
