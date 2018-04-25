package com.tanyayuferova.franklin;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

public class FranklinApplication extends Application {
    public static FranklinApplication INSTANCE;
    private Cicerone<Router> cicerone;

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
}
