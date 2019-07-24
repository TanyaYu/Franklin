package com.tanyayuferova.franklin.data.virtue

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
@Dao
interface VirtueDao {

    @Query("SELECT * FROM virtue")
    fun getAll(): LiveData<List<VirtueEntity>>

    @Query("""
        SELECT 
            virtue.id, 
            (
                select count(*) 
                from point 
                where 
                    point.virtue_id = virtue.id 
                    and date >= :start and date < :end
            ) as points_count
        FROM virtue
        order by virtue.id asc  """)
    fun getWithPointsForPeriod(start: Date, end: Date): LiveData<List<VirtueWithPointsEntity>>

    @Query("""
        SELECT 
            virtue.id, 
            (
                select count(*) 
                from point 
                where point.virtue_id = virtue.id and date = :date
            ) as points_count
        FROM virtue
        order by virtue.id asc """)
    fun getWithPointsForDate(date: Date): LiveData<List<VirtueWithPointsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(virtues: List<VirtueEntity>)
}