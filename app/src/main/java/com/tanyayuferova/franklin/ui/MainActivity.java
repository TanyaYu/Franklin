package com.tanyayuferova.franklin.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tanyayuferova.franklin.utils.FragmentFactory;
import com.tanyayuferova.franklin.FranklinApplication;
import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.listeners.BackButtonListener;
import com.tanyayuferova.franklin.utils.EveryDayReminderUtils;
import com.tanyayuferova.franklin.utils.PreferencesUtils;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Command;

public class MainActivity extends AppCompatActivity {

    private Router router = FranklinApplication.INSTANCE.getRouter();
    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.main_container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return FragmentFactory.createFragment(screenKey, data);
        }

        @Override
        protected void showSystemMessage(String message) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        protected void exit() {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        FranklinApplication.INSTANCE.getNavigatorHolder().setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        FranklinApplication.INSTANCE.getNavigatorHolder().removeNavigator();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNotifications();
        router.navigateTo(VirtuesFragment.SCREEN_KEY);
        //todo test me
        if (!PreferencesUtils.getHasInfoBeenShown(this)) {
            router.navigateTo(InfoFragment.SCREEN_KEY);
        }
    }

    private void initNotifications() {
        if (PreferencesUtils.getNotificationEnabled(this))
            EveryDayReminderUtils.scheduleStartReminderJob(this);
        else {
            EveryDayReminderUtils.cancelEveryDayReminder(this);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
