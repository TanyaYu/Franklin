package com.tanyayuferova.franklin.domain.mappers

import android.database.Cursor
import com.tanyayuferova.franklin.data.VirtuesContract.VirtueEntry
import com.tanyayuferova.franklin.domain.virtues.VirtuesInteractor
import com.tanyayuferova.franklin.data.Result.*
import com.tanyayuferova.franklin.data.virtue.VirtueResult

/**
 * Author: Tanya Yuferova
 * Date: 6/27/2018
 */
class CursorToVirtueResultList(
    private val virtuesInteractor: VirtuesInteractor,
    private val firstVirtueId: Int
) : Mapper<Cursor, List<VirtueResult>> {
    override fun map(from: Cursor) = with(from) {
        val resultList = arrayListOf<VirtueResult>()
        var moveToFirstIndex = 0

        for (i in 0 until count) {
            moveToPosition(i)
            val id = getInt(getColumnIndex(VirtueEntry._ID))
            val current = getInt(getColumnIndex("current_week"))
            val previous = getInt(getColumnIndex("prev_week"))
            val result = current - previous
            resultList.add(VirtueResult(
                virtuesInteractor.getVirtueById(id),
                current,
                when {
                    result > 0 -> NEGATIVE
                    result < 0 -> POSITIVE
                    else -> NEUTRAL
                },
                when {
                    result > 0 -> NEGATIVE
                    result < 0 -> POSITIVE
                    else -> NEUTRAL
                }
            ))
            if (id == firstVirtueId) {
                moveToFirstIndex = i
            }
        }

        resultList.add(0, resultList.get(moveToFirstIndex))
        resultList.removeAt(moveToFirstIndex + 1)
        return@with resultList
    }
}