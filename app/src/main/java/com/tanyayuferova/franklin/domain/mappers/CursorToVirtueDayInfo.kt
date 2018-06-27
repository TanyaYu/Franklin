package com.tanyayuferova.franklin.domain.mappers

import android.database.Cursor
import com.tanyayuferova.franklin.data.VirtuesContract.VirtueEntry
import com.tanyayuferova.franklin.domain.virtues.VirtuesInteractor
import com.tanyayuferova.franklin.data.virtue.VirtueDayInfo

/**
 * Author: Tanya Yuferova
 * Date: 6/27/2018
 */
class CursorToVirtueDayInfoList(
    private val virtuesInteractor: VirtuesInteractor
): Mapper<Cursor, List<VirtueDayInfo>> {
    override fun map(from: Cursor) = with(from) {
        val resultList = arrayListOf<VirtueDayInfo>()
        for (i in 0 until count) {
            moveToPosition(i)
            val id = getInt(getColumnIndex(VirtueEntry._ID))
            val virtue = virtuesInteractor.getVirtueById(id)
            val marksAmount = getInt(getColumnIndex("day"))
            resultList.add(VirtueDayInfo(virtue, marksAmount))
        }
        return@with resultList
    }
}