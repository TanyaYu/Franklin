package com.tanyayuferova.franklin.domain.main

import androidx.annotation.DrawableRes

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
data class Virtue(
    val id: Int,
    @DrawableRes
    val iconRes: Int,
    val name: String,
    val description: String,
    val points: Int,
    val isSelected: Boolean
)