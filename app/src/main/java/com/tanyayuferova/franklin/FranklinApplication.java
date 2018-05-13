package com.tanyayuferova.franklin;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import java.util.Calendar;
import java.util.Date;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

public class FranklinApplication extends Application {
    public static FranklinApplication INSTANCE;
    private Cicerone<Router> cicerone;
    private Date selectedDate = Calendar.getInstance().getTime();

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initCicerone();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private void initCicerone() {
        cicerone = Cicerone.create();
    }

    public NavigatorHolder getNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }

    public Router getRouter() {
        return cicerone.getRouter();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}
