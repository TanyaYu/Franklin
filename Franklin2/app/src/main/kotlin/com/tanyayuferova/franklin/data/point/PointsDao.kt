package com.tanyayuferova.franklin.data.point

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
@Dao
interface PointsDao {

    @Query("SELECT * FROM point")
    fun getAll(): LiveData<List<PointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(point: List<PointEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: PointEntity)

    @Query("""
        delete from point
        where id in (
            select id from point
            where virtue_id = :virtueId and date = :date
            limit 1 
        )""")
    fun delete(virtueId: Int, date: Date)
}