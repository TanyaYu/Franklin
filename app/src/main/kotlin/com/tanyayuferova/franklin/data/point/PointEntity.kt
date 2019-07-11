package com.tanyayuferova.franklin.data.point

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import com.tanyayuferova.franklin.data.virtue.VirtueEntity
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
@Entity(
    tableName = "point",
    foreignKeys = [
        ForeignKey(
            entity = VirtueEntity::class,
            parentColumns = ["id"],
            childColumns = ["virtue_id"],
            onDelete = NO_ACTION
        )
    ]
)
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Date,
    @ColumnInfo(name = "virtue_id")
    val virtueId: Int
)