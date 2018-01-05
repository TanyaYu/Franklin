package com.tanyayuferova.franklin;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanyayuferova.franklin.data.VirtuesContract;
import com.tanyayuferova.franklin.databinding.FragmentWeekTableBinding;
import com.tanyayuferova.franklin.entity.Virtue;
import com.tanyayuferova.franklin.utils.DateUtils;
import com.tanyayuferova.franklin.utils.VirtueOfWeekUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI;

/**
 * Created by Tanya Yuferova on 12/26/2017.
 */

public class WeekTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        VirtuesAdapter.VirtuesAdapterOnClickHandler {

    protected FragmentWeekTableBinding binding;
    private VirtuesAdapter virtuesAdapter;

    private static AsyncTask<Void, Void, Virtue> findVirtueTask;
    /**
     * How many days to display
     */
    public static final int DAYS_COUNT = 7;
    /**
     * The first date in the column
     */
    private Date startDate;

    protected static int countLoaders = 0;

    public static String[] MAIN_PROJECTION = new String[DAYS_COUNT + 4];
    public static final int MAIN_PROJECTION_ID_INDEX = DAYS_COUNT;
    public static final int MAIN_PROJECTION_SHORT_NAME_INDEX = DAYS_COUNT + 1;
    public static final int MAIN_PROJECTION_NAME_INDEX = DAYS_COUNT + 2;
    public static final int MAIN_PROJECTION_DESCRIPTION_INDEX = DAYS_COUNT + 3;

    public static String DAY_CODE = "day";

    public static final String ARGUMENT_LOADER_ID = "arg.loader_id";
    public static final String ARGUMENT_START_DATE = "arg.start_date";
    public static final String ARGUMENT_VIRTUE_OF_WEEK = "arg.virtue_of_week";

    public WeekTableFragment() {
    }

    public static WeekTableFragment newInstance(Date startDate, Virtue virtue) {
        WeekTableFragment fragment = new WeekTableFragment();
        fragment.setArguments(new Bundle());
        fragment.getArguments().putInt(ARGUMENT_LOADER_ID, ++countLoaders);
        fragment.getArguments().putLong(ARGUMENT_START_DATE, startDate.getTime());
        fragment.getArguments().putParcelable(ARGUMENT_VIRTUE_OF_WEEK, virtue);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeekTableBinding.inflate(inflater, container, false);

        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSettingsActivity = new Intent(getContext(), SettingsActivity.class);
                startActivity(startSettingsActivity);
            }
        });

        startDate = new Date(getArguments().getLong(ARGUMENT_START_DATE));
        binding.setVirtue((Virtue) getArguments().getParcelable(ARGUMENT_VIRTUE_OF_WEEK));

        initMainProjection(startDate);

        initRecyclerView();
        initDaysOfWeekLayout();

        getActivity().getSupportLoaderManager().initLoader(getArguments().getInt(ARGUMENT_LOADER_ID), null, this);
        return binding.getRoot();
    }

    private void initMainProjection(Date date) {
        MAIN_PROJECTION = new String[MAIN_PROJECTION.length];
        for (int i = 0; i < DAYS_COUNT; i++) {
            MAIN_PROJECTION[i] = createSelectString(DAY_CODE + i, date, i);
        }
        MAIN_PROJECTION[MAIN_PROJECTION_ID_INDEX] = VirtuesContract.VirtueEntry._ID;
        MAIN_PROJECTION[MAIN_PROJECTION_SHORT_NAME_INDEX] = VirtuesContract.VirtueEntry.COLUMN_SHORT_NAME;
        MAIN_PROJECTION[MAIN_PROJECTION_NAME_INDEX] = VirtuesContract.VirtueEntry.COLUMN_NAME;
        MAIN_PROJECTION[MAIN_PROJECTION_DESCRIPTION_INDEX] = VirtuesContract.VirtueEntry.COLUMN_DESCRIPTION;
    }

    protected void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setHasFixedSize(true);
        virtuesAdapter = new VirtuesAdapter(getContext(), this);
        binding.recyclerview.setAdapter(virtuesAdapter);
    }

    protected void initDaysOfWeekLayout() {
        for(int i = 0; i<DAYS_COUNT; i++) {
            /* We need to create TextView for every day or update text and color of old TextView */
            TextView tv = (TextView) binding.daysOfWeekLayout.getChildAt(i+1);
            if(tv == null) {
                tv = new TextView(new ContextThemeWrapper(getContext(), R.style.DayOfWeekTextView));
                binding.daysOfWeekLayout.addView(tv, i + 1, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.addDaysToDate(startDate, i));
            /* Set color */
            tv.setTextColor(getResources().getColor(DateUtils.isToday(calendar.getTime()) ?
                    R.color.colorAccent : R.color.colorPrimaryText));
            /* Set text */
            tv.setText(getDayTextViewCaption(calendar));
        }
    }

    protected String getDayTextViewCaption (Calendar calendar) {
        String caption = new SimpleDateFormat("dd.MM").format(calendar.getTime()) + "\n";
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY : caption += getResources().getString(R.string.mon);
                break;
            case Calendar.TUESDAY : caption += getResources().getString(R.string.tue);
                break;
            case Calendar.WEDNESDAY : caption += getResources().getString(R.string.wed);
                break;
            case Calendar.THURSDAY : caption += getResources().getString(R.string.thu);
                break;
            case Calendar.FRIDAY : caption += getResources().getString(R.string.fri);
                break;
            case Calendar.SATURDAY : caption += getResources().getString(R.string.sat);
                break;
            case Calendar.SUNDAY : caption += getResources().getString(R.string.sun);
                break;
        }
        return caption;
    }

    /**
     * Creates subquery for main points selection. This is query only for one column. It will contain
     * the amounts of points at start date + daysShift for all virtues
     *
     * @param label     alias for column
     * @param daysShift days shift for start date
     * @return subquery string
     */
    protected String createSelectString(String label, Date startDate, int daysShift) {
        String formattedDateString = new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDaysToDate(startDate, daysShift));
        return " (select count(*) from " + VirtuesContract.PointEntry.TABLE_NAME +
                " where " + VirtuesContract.VirtueEntry.TABLE_NAME + "." + VirtuesContract.VirtueEntry._ID +
                " = " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_VIRTUE_ID +
                " and " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_DATE +
                "='" + formattedDateString + "') as '" + label + "' ";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = VirtuesContract.VirtueEntry._ID + " ASC";
        return new CursorLoader(getContext(),
                CONTENT_VIRTUES_URI,
                MAIN_PROJECTION,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        virtuesAdapter.swapCursor(data);
        if(binding.getVirtue() != null)
            selectVirtueInTable(binding.getVirtue());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        virtuesAdapter.swapCursor(null);
        virtuesAdapter.setSelectedPosition(-1);
    }

    @Override
    public void onDayClick(long virtueId, int daysShift) {
        getContext().getContentResolver().insert(VirtuesContract.buildPointsUriWithDate(virtueId, DateUtils.addDaysToDate(startDate, daysShift)), null);
    }

    @Override
    public void onDayLongClick(long virtueId, int daysShift) {
        getContext().getContentResolver().delete(VirtuesContract.buildPointsUriWithDate(virtueId, DateUtils.addDaysToDate(startDate, daysShift)), null, null);
    }

    /**
     * Highlight virtue in table
     * @param virtue
     */
    protected void selectVirtueInTable(Virtue virtue) {
        virtuesAdapter.setSelectedId(virtue.getId());
        virtuesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVirtueNameClick(Virtue virtue) {
        binding.setVirtue(virtue);
        selectVirtueInTable(virtue);
        VirtueOfWeekUtils.setVirtueOfWeek(getContext(), virtue.getId(), startDate);
    }
}
