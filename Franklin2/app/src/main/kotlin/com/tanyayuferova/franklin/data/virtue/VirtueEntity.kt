package com.tanyayuferova.franklin.data.virtue

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
@Entity(tableName = "virtue")
data class VirtueEntity(
    @PrimaryKey
    val id: Int
)