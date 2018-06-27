package com.tanyayuferova.franklin.di;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tanyayuferova.franklin.FranklinApplication;
import com.tanyayuferova.franklin.data.service.KeyValueStorage;
import com.tanyayuferova.franklin.ui.common.MainActivity;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Tanya Yuferova on 5/26/2018.
 */
@Module
public abstract class ApplicationModule {

    @Binds
    abstract Context provideContext(FranklinApplication application);

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    @ActivityScope
    abstract MainActivity bindMainActivity();

    @Provides
    @Singleton
    static FirebaseAnalytics provideFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }

    @Provides
    @Singleton
    static KeyValueStorage provideKeyValueStorage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new KeyValueStorage(preferences);
    }

    @Provides
    @Singleton
    static FirebaseJobDispatcher provideFirebaseJobDispatcher(Context context) {
        GooglePlayDriver driver = new GooglePlayDriver(context);
        return new FirebaseJobDispatcher(driver);
    }

    @Provides
    @Singleton
    static NotificationManager provideSystemNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    static ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    static Resources providesResources(Context context) {
        return context.getResources();
    }

    //fixme
    @Provides
    @CurrentDate
    static Date provideCurrentDate() {
        return Calendar.getInstance().getTime();
    }
}
