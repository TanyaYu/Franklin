package com.tanyayuferova.franklin.di

import android.content.Context
import com.tanyayuferova.franklin.domain.main.MainViewModelFactory
import com.tanyayuferova.franklin.data.*
import com.tanyayuferova.franklin.data.database.AppDatabase
import com.tanyayuferova.franklin.data.point.PointsRepository
import com.tanyayuferova.franklin.data.settings.SettingsRepository
import com.tanyayuferova.franklin.data.sharedpreferences.KeyValueStorage
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.data.week.WeeksRepository
import com.tanyayuferova.franklin.domain.settings.SettingsViewModelFactory
import com.tanyayuferova.franklin.domain.statistics.StatisticsViewModelFactory
import java.util.Date

/**
 * Enables injection of data sources.
 */
object Injection {

    fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        return MainViewModelFactory(
            getVirtueRepository(context),
            getPointsRepository(context),
            getWeekRepository(context)
        )
    }

    fun provideSettingsViewModelFactory(context: Context): SettingsViewModelFactory {
        return SettingsViewModelFactory(
            getSettingsRepository(context)
        )
    }

    fun provideStatisticsViewModelFactory(context: Context, date: Date): StatisticsViewModelFactory {
        return StatisticsViewModelFactory(
            getVirtueRepository(context),
            date
        )
    }

    private fun getSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepository.getInstance(
            getKeyValueStorage(context.applicationContext)
        )
    }

    private fun getVirtueRepository(context: Context): VirtueRepository {
        return VirtueRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).virtueDao(),
            getWeekRepository(context.applicationContext)
        )
    }

    private fun getWeekRepository(context: Context): WeeksRepository {
        return WeeksRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).weekDao()
        )
    }

    private fun getPointsRepository(context: Context): PointsRepository {
        return PointsRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).pointDao()
        )
    }

    private fun getKeyValueStorage(context: Context): KeyValueStorage {
        return KeyValueStorage(
            context.getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}
