package com.tanyayuferova.franklin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tanyayuferova.franklin.entity.Virtue;
import com.tanyayuferova.franklin.utils.VirtueOfWeekUtils;

import java.util.Date;

/**
 * Created by Tanya Yuferova on 12/26/2017.
 */

public class MainActivityFree extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager.setAdapter(new WeekTablePagerAdapterFree(getSupportFragmentManager()));
        viewPager.setCurrentItem(START_PAGE_INDEX);
    }

    public class WeekTablePagerAdapterFree extends WeekTablePagerAdapter {

        public WeekTablePagerAdapterFree(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Date startDate = getStartDateForPage(position);
            Virtue virtueOfWeek = VirtueOfWeekUtils.getVirtueOfWeek(MainActivityFree.this, startDate);
            VirtueOfWeekUtils.setVirtueOfWeek(MainActivityFree.this, virtueOfWeek.getId(), startDate);
            return WeekTableFragmentFree.newInstance(startDate, virtueOfWeek);
        }
    }
}