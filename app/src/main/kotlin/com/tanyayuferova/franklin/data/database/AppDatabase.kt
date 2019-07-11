package com.tanyayuferova.franklin.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tanyayuferova.franklin.data.DATABASE_NAME
import com.tanyayuferova.franklin.data.point.PointEntity
import com.tanyayuferova.franklin.data.virtue.VirtueEntity
import com.tanyayuferova.franklin.data.week.WeekEntity
import com.tanyayuferova.franklin.data.point.PointsDao
import com.tanyayuferova.franklin.data.virtue.VirtueDao
import com.tanyayuferova.franklin.data.virtue.VirtueRepository.Companion.FIRST_VIRTUE_ID
import com.tanyayuferova.franklin.data.virtue.VirtueRepository.Companion.virtueInitialValues
import com.tanyayuferova.franklin.data.week.WeeksDao
import com.tanyayuferova.franklin.utils.today
import com.tanyayuferova.franklin.utils.week
import com.tanyayuferova.franklin.utils.yearOfWeek
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
@Database(
    entities = [
        VirtueEntity::class,
        PointEntity::class,
        WeekEntity::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun virtueDao(): VirtueDao
    abstract fun pointDao(): PointsDao
    abstract fun weekDao(): WeeksDao

    private fun populateWithInitialData() {
        // ioThread { todo courutines https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
        GlobalScope.launch {
            virtueDao().insertAll(virtueInitialValues)
            weekDao().insert(
                WeekEntity(
                    week = today.week,
                    year = today.yearOfWeek,
                    virtueId = FIRST_VIRTUE_ID
                )
            )
        }
    }

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            getInstance(context).populateWithInitialData()
                        }
                    })
                    .build()
            }
            return instance!!
        }
    }
}