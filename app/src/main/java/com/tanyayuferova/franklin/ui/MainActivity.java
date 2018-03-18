package com.tanyayuferova.franklin.ui;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.database.VirtuesContract;
import com.tanyayuferova.franklin.databinding.ActivityMainBinding;
import com.tanyayuferova.franklin.utils.DateUtils;
import com.tanyayuferova.franklin.utils.EveryDayReminderUtils;
import com.tanyayuferova.franklin.utils.PreferencesUtils;
import com.tanyayuferova.franklin.utils.VirtueOfWeekUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.tanyayuferova.franklin.database.VirtuesContract.*;
import static com.tanyayuferova.franklin.database.VirtuesContract.CONTENT_VIRTUES_URI;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        VirtuesAdapter.VirtuesAdapterOnClickHandler,
        DaySelectorWidget.OnDayClickedListener {

    private ActivityMainBinding binding;
    private VirtuesAdapter virtuesAdapter;
    private WeekTablePagerAdapter pagerAdapter;
    private Date currentDate;
    private int LOADER_ID = 1;

    public static final int PAGES_COUNT = 100;
    // Start page in the middle
    public static final int START_PAGE_INDEX = PAGES_COUNT / 2;
    public static String DAY_CODE = "day";
    public static final String STATE_CURRENT_DATE = "state.current_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState == null) {
            currentDate = Calendar.getInstance().getTime();
        } else {
            currentDate = new Date(savedInstanceState.getLong(STATE_CURRENT_DATE));
        }
        setSupportActionBar(binding.toolbar);

        initRecyclerView();
        initDaysOfWeekPager();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        initNotifications();
        startInfoActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.virtues_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setHasFixedSize(true);
        virtuesAdapter = new VirtuesAdapter(this, this);
        virtuesAdapter.setHasStableIds(true);
        binding.recyclerview.setAdapter(virtuesAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
    }

    protected void initDaysOfWeekPager() {
        pagerAdapter = new WeekTablePagerAdapter(getSupportFragmentManager());
        binding.daysOfWeekPager.setAdapter(pagerAdapter);
        binding.daysOfWeekPager.setCurrentItem(START_PAGE_INDEX);
    }

    private void initNotifications() {
        if (PreferencesUtils.getNotificationEnabled(this))
            EveryDayReminderUtils.scheduleStartReminderJob(this);
        else {
            EveryDayReminderUtils.cancelEveryDayReminder(this);
        }
    }

    private void startInfoActivity() {
        if (!PreferencesUtils.getHasInfoBeenShown(this)) {
            startActivity(new Intent(this, InfoActivity.class));
        }
    }

    //fixme
    private String[] getMainProjection() {
        String formattedDateString = new SimpleDateFormat("yyyyMMdd").format(currentDate);

        String[] result = new String[2];
        result[0] = VirtueEntry._ID;
        result[1] = " (select count(*) from " + PointEntry.TABLE_NAME +
                " where " + VirtueEntry.TABLE_NAME + "." + VirtueEntry._ID +
                " = " + PointEntry.TABLE_NAME + "." + PointEntry.COLUMN_VIRTUE_ID +
                " and " + PointEntry.TABLE_NAME + "." + PointEntry.COLUMN_DATE +
                "='" + formattedDateString + "') as '" + DAY_CODE + "' ";
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putLong(STATE_CURRENT_DATE, currentDate.getTime());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = VirtuesContract.VirtueEntry._ID + " ASC";
        return new CursorLoader(this,
                CONTENT_VIRTUES_URI,
                getMainProjection(),
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        virtuesAdapter.swapCursor(data);

        //fixme async task
        int selectedVirtueId = VirtueOfWeekUtils.getVirtueIdOfWeek(MainActivity.this, currentDate);
        //todo insert new value
        selectVirtueInTable(selectedVirtueId);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        virtuesAdapter.swapCursor(null);
        virtuesAdapter.setSelectedPosition(-1);
        //fixme when close cursor?
    }

    @Override
    public void onDayClick(View view, int virtueId) {
        //Add mark
        getContentResolver().insert(VirtuesContract.buildPointsUriWithDate(virtueId, currentDate), null);
    }

    @Override
    public boolean onDayLongClick(int virtueId) {
        //Remove mark
        getContentResolver().delete(VirtuesContract.buildPointsUriWithDate(virtueId, currentDate), null, null);
        return true;
    }

    protected void selectVirtueInTable(int virtueId) {
        virtuesAdapter.selectVirtue(virtueId);
    }

    @Override
    public void onVirtueNameClick(int virtueId) {
        //Set new virtue of the week
        selectVirtueInTable(virtueId);
        VirtueOfWeekUtils.setVirtueOfWeek(this, virtueId, currentDate);
    }

    @Override
    public void onDateClicked(Date date) {
        currentDate = date;
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        removeSelections();
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    //fixme
    private void removeSelections() {
        for(int i =0; i<binding.daysOfWeekPager.getChildCount(); i++){
            DaySelectorWidget f = (DaySelectorWidget) binding.daysOfWeekPager.getChildAt(i);
            f.selectDate(currentDate);
        }
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
            return DaysOfWeekFragment.newInstance(startDate);
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }
    }
}
