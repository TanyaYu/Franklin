package com.example.tanyayuferova.franklin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tanyayuferova.franklin.data.VirtuesContract;
import com.example.tanyayuferova.franklin.utils.DateUtils;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private static int PAGES_COUNT = 100;
    // Start page in the middle
    private static int START_PAGE_INDEX = PAGES_COUNT /2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeekTablePagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(START_PAGE_INDEX);

       // TODO 2 tasks for schedule and sending notification
        // if(PreferencesUtils.getNotificationEnabled(this))
         //   EveryDayReminderUtils.scheduleEveryDayReminder(this, false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.virtues_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.reset_data_action:
                getContentResolver().delete(VirtuesContract.CONTENT_POINTS_URI, null, null);
                getContentResolver().delete(VirtuesContract.CONTENT_WEEKS_URI, null, null);
                Toast.makeText(this, "All data is deleted.", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Date getStartDateForPage(int pageIndex) {
        Date date = DateUtils.getFirstDayOfWeek();
        return DateUtils.addDaysToDate(date, (pageIndex - START_PAGE_INDEX) * 7);
    }

    public class WeekTablePagerAdapter extends FragmentPagerAdapter {

        public WeekTablePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeekTableFragment.newInstance(getStartDateForPage(position));
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }
    }
}
