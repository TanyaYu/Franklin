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
import android.support.v4.view.ViewPager;
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
import java.util.concurrent.TimeUnit;

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
    private int LOADER_ID = 11;

    public static final int PAGES_COUNT = 100;
    // Start page in the middle
    public static final int START_PAGE_INDEX = PAGES_COUNT / 2;
    public static String DAY_CODE = "day";

    private Date firstDate = DateUtils.getFirstDayOfWeek();

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

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

        initRecyclerView();
        initDaysOfWeekPager();
        updateTitle(DateUtils.getFirstDayOfWeek(FranklinApplication.INSTANCE.getSelectedDate()));

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
        if (item.getItemId() == R.id.action_results) {
            router.navigateTo(ResultsFragment.SCREEN_KEY, firstDate);
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
        binding.daysOfWeekPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Date newStartDate = getStartDateForPage(position);
                updateTitle(newStartDate);
                setResultsActionVisible(Calendar.getInstance().getTime().getTime() >= newStartDate.getTime());
            }
        });
    }

    private void updateTitle(Date startDate) {
        firstDate = startDate;
        Date endDate = DateUtils.addDaysToDate(startDate, 7);
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        SimpleDateFormat withYear = new SimpleDateFormat("LLLL, yyyy");
        SimpleDateFormat onlyMonth = new SimpleDateFormat("LLLL");

        String title = start.get(Calendar.YEAR) != end.get(Calendar.YEAR) ?
                withYear.format(startDate) + " - " + withYear.format(endDate) :
                start.get(Calendar.MONTH) != end.get(Calendar.MONTH) ?
                        onlyMonth.format(startDate) + " - " + withYear.format(endDate) :
                        withYear.format(startDate);
        binding.toolbar.setTitle(title);

    }

    //fixme
    private String[] getMainProjection() {
        String formattedDateString = new SimpleDateFormat("yyyyMMdd").format(FranklinApplication.INSTANCE.getSelectedDate());

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
        int selectedVirtueId = VirtueOfWeekUtils.getVirtueIdOfWeek(getContext(), FranklinApplication.INSTANCE.getSelectedDate());
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
        getContext().getContentResolver().insert(VirtuesContract.buildPointsUriWithDate(virtueId, FranklinApplication.INSTANCE.getSelectedDate()), null);
    }

    @Override
    public boolean onDayLongClick(int virtueId) {
        //Remove mark
        //fixme can cause CursorIndexOutOfBoundsException
        getContext().getContentResolver().delete(VirtuesContract.buildPointsUriWithDate(virtueId, FranklinApplication.INSTANCE.getSelectedDate()), null, null);
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
        VirtueOfWeekUtils.setVirtueOfWeek(getContext(), virtueId, FranklinApplication.INSTANCE.getSelectedDate());
    }

    @Override
    public void onDateClicked(Date date) {
        FranklinApplication.INSTANCE.setSelectedDate(date);
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        removeSelections();
// fixme depends on selected week
//     binding.toolbar.getMenu().findItem(R.id.action_results).setVisible(Calendar.getInstance().getTime().getTime() - date.getTime() > 0);
    }

    private void setResultsActionVisible(boolean visible) {
//todo fix
        if (binding.toolbar.getMenu().findItem(R.id.action_results) != null)
            binding.toolbar.getMenu().findItem(R.id.action_results).setVisible(visible);
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }

    //fixme
    private void removeSelections() {
        Date currentDate = FranklinApplication.INSTANCE.getSelectedDate();
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
