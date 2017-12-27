package com.example.tanyayuferova.franklin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tanyayuferova.franklin.entity.Virtue;
import com.example.tanyayuferova.franklin.jobs.EveryDayReminderUtils;
import com.example.tanyayuferova.franklin.utils.DateUtils;
import com.example.tanyayuferova.franklin.utils.PreferencesUtils;
import com.example.tanyayuferova.franklin.utils.VirtueOfWeekUtils;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    protected ViewPager viewPager;

    public static int PAGES_COUNT = 100;
    // Start page in the middle
    public static int START_PAGE_INDEX = PAGES_COUNT /2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeekTablePagerAdapter(getSupportFragmentManager()));
        viewPager.setPageTransformer(false, new ParallaxPageTransformer());
        viewPager.setCurrentItem(START_PAGE_INDEX);

         if(PreferencesUtils.getNotificationEnabled(this))
            EveryDayReminderUtils.scheduleStartReminderJob(this);

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
            Date startDate = getStartDateForPage(position);
            Virtue virtueOfWeek = VirtueOfWeekUtils.getVirtueOfWeek(MainActivity. this, startDate);
            VirtueOfWeekUtils.setVirtueOfWeek(MainActivity. this, virtueOfWeek.getId(), startDate);
            return WeekTableFragment.newInstance(startDate, virtueOfWeek);
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }
    }

    public class ParallaxPageTransformer implements ViewPager.PageTransformer {

        public void transformPage(View view, float position) {
            float alpha = 1.0F - Math.abs(position);

            view.findViewById(R.id.tv_virtue_description).setAlpha(alpha);
            view.findViewById(R.id.tv_virtue_title).setAlpha(alpha);
        }
    }
}
