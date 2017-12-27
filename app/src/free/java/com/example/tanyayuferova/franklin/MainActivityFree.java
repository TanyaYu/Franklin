package com.example.tanyayuferova.franklin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

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
            return WeekTableFragmentFree.newInstance(getStartDateForPage(position));
        }
    }
}