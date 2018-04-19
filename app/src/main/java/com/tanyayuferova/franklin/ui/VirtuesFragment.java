package com.tanyayuferova.franklin.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.FranklinApplication;
import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.database.VirtuesContract;
import com.tanyayuferova.franklin.databinding.FragmentVirtuesBinding;
import com.tanyayuferova.franklin.listeners.BackButtonListener;
import com.tanyayuferova.franklin.utils.DateUtils;
import com.tanyayuferova.franklin.utils.VirtueOfWeekUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.terrakok.cicerone.Router;

import static com.tanyayuferova.franklin.database.VirtuesContract.CONTENT_VIRTUES_URI;

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

public class VirtuesFragment extends Fragment implements
        BackButtonListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        VirtuesAdapter.VirtuesAdapterOnClickHandler,
        DaySelectorWidget.OnDayClickedListener {

    final public static String SCREEN_KEY = "VIRTUES_FRAGMENT_SCREEN_KEY";
    private Router router = FranklinApplication.INSTANCE.getRouter();
    private FragmentVirtuesBinding binding;
    private VirtuesAdapter virtuesAdapter;
    private WeekTablePagerAdapter pagerAdapter;
    private Date currentDate;
    private int LOADER_ID = 1;

    public static final int PAGES_COUNT = 100;
    // Start page in the middle
    public static final int START_PAGE_INDEX = PAGES_COUNT / 2;
    public static String DAY_CODE = "day";
    public static final String STATE_CURRENT_DATE = "state.current_date";

    public static VirtuesFragment newInstance(Object data) {
        VirtuesFragment fragment = new VirtuesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = FragmentVirtuesBinding.inflate(inflater, container, false);

        if (savedInstanceState == null) {
            currentDate = Calendar.getInstance().getTime();
        } else {
            currentDate = new Date(savedInstanceState.getLong(STATE_CURRENT_DATE));
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

        initRecyclerView();
        initDaysOfWeekPager();

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.virtues_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            router.navigateTo(SettingsFragment.SCREEN_KEY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setHasFixedSize(true);
        virtuesAdapter = new VirtuesAdapter(getContext(), this);
        virtuesAdapter.setHasStableIds(true);
        binding.recyclerview.setAdapter(virtuesAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
    }

    protected void initDaysOfWeekPager() {
        pagerAdapter = new WeekTablePagerAdapter(getChildFragmentManager());
        binding.daysOfWeekPager.setAdapter(pagerAdapter);
        binding.daysOfWeekPager.setCurrentItem(START_PAGE_INDEX);
    }

    //fixme
    private String[] getMainProjection() {
        String formattedDateString = new SimpleDateFormat("yyyyMMdd").format(currentDate);

        String[] result = new String[2];
        result[0] = VirtuesContract.VirtueEntry._ID;
        result[1] = " (select count(*) from " + VirtuesContract.PointEntry.TABLE_NAME +
                " where " + VirtuesContract.VirtueEntry.TABLE_NAME + "." + VirtuesContract.VirtueEntry._ID +
                " = " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_VIRTUE_ID +
                " and " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_DATE +
                "='" + formattedDateString + "') as '" + DAY_CODE + "' ";
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putLong(STATE_CURRENT_DATE, currentDate.getTime());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = VirtuesContract.VirtueEntry._ID + " ASC";
        return new CursorLoader(getContext(),
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
        int selectedVirtueId = VirtueOfWeekUtils.getVirtueIdOfWeek(getContext(), currentDate);
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
        //fixme can cause CursorIndexOutOfBoundsException
        getContext().getContentResolver().insert(VirtuesContract.buildPointsUriWithDate(virtueId, currentDate), null);
    }

    @Override
    public boolean onDayLongClick(int virtueId) {
        //Remove mark
        //fixme can cause CursorIndexOutOfBoundsException
        getContext().getContentResolver().delete(VirtuesContract.buildPointsUriWithDate(virtueId, currentDate), null, null);
        return true;
    }

    protected void selectVirtueInTable(int virtueId) {
        virtuesAdapter.selectVirtue(virtueId);
    }

    @Override
    public void onVirtueNameClick(int virtueId) {
        //Set new virtue of the week
        //fixme can cause CursorIndexOutOfBoundsException
        selectVirtueInTable(virtueId);
        VirtueOfWeekUtils.setVirtueOfWeek(getContext(), virtueId, currentDate);
    }

    @Override
    public void onDateClicked(Date date) {
        currentDate = date;
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        removeSelections();
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    //fixme
    private void removeSelections() {
        for (int i = 0; i < binding.daysOfWeekPager.getChildCount(); i++) {
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
