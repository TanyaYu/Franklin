package com.tanyayuferova.franklin.domain.mappers

/**
 * Author: Tanya Yuferova
 * Date: 6/27/2018
 */
interface Mapper<T, R> {
    fun map(from: T): R
}