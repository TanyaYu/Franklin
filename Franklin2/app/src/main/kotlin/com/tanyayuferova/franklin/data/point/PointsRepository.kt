package com.tanyayuferova.franklin.data.point

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
class PointsRepository private constructor(
    private val pointsDao: PointsDao
) {


    suspend fun addPoint(virtueId: Int, date: Date) {
        withContext(Dispatchers.IO) {
            PointEntity(
                id = 0,
                date = date,
                virtueId = virtueId
            ).let(pointsDao::insert)
        }
    }


    suspend fun removePoint(virtueId: Int, date: Date) {
        withContext(Dispatchers.IO) {
            pointsDao.delete(virtueId, date)
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: PointsRepository? = null

        fun getInstance(pointsDao: PointsDao) =
            instance ?: synchronized(this) {
                instance
                    ?: PointsRepository(pointsDao).also { instance = it }
            }
    }
}