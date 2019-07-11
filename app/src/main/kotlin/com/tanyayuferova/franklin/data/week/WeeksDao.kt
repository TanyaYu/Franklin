package com.tanyayuferova.franklin.data.week

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
@Dao
interface WeeksDao {

    @Query("SELECT * FROM week")
    fun getAll(): LiveData<List<WeekEntity>>

    @Query("SELECT virtue_id FROM week where week = :week and year = :year")
    fun getVirtueByWeek(week: Int, year: Int): LiveData<List<Int>>

    @Query(""" 
        SELECT * FROM week 
        where 
            year = :year and week > :week 
            or year > :year
        order by year asc, week asc
        limit 1 """)
    fun getNextExisting(week: Int, year: Int): LiveData<List<WeekEntity>>

    @Query("""
        SELECT * FROM week 
        where 
            year = :year and week < :week 
            or year < :year
        order by year desc, week desc
        limit 1 """)
    fun getPreviousExisting(week: Int, year: Int): LiveData<List<WeekEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: WeekEntity)
}