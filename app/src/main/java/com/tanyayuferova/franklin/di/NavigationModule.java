package com.tanyayuferova.franklin.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by Tanya Yuferova on 5/26/2018.
 */
@Module
public abstract class NavigationModule {

    private static Cicerone cicerone = Cicerone.create();

    @Provides
    @Singleton
    static NavigatorHolder provideNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }

    @Provides
    @Singleton
    static Router provideRouter() {
        return (Router) cicerone.getRouter();
    }
}
