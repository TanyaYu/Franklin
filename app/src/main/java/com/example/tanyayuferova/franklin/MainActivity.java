package com.example.tanyayuferova.franklin;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanyayuferova.franklin.data.VirtuesContract;
import com.example.tanyayuferova.franklin.data.VirtuesContract.*;
import com.example.tanyayuferova.franklin.entity.Virtue;
import com.example.tanyayuferova.franklin.utils.DateUtils;
import com.example.tanyayuferova.franklin.utils.PreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        VirtuesAdapter.VirtuesAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private VirtuesAdapter virtuesAdapter;
    private LinearLayout daysOfWeekLayout;
    private Spinner virtuesSpinner;
    private TextView virtueDescription;
    private ArrayAdapter<Virtue> virtueSpinnerAdapter;
    private static final int ID_VIRTUES_LOADER = 1;
    private final String TAG = MainActivity.class.getSimpleName();
    /**
     * How many days to display
     */
    public static int DAYS_COUNT = 7;
    /**
     * The first date in the column
     */
    private Date startDate;
    public static String[] MAIN_PROJECTION = new String[DAYS_COUNT + 3];
    public static int MAIN_PROJECTION_ID_INDEX = DAYS_COUNT;
    public static int MAIN_PROJECTION_SHORT_NAME_INDEX = DAYS_COUNT + 1;
    public static int MAIN_PROJECTION_NAME_INDEX = DAYS_COUNT + 2;

    public static String DAY_CODE = "day";
    public static String START_DATE = "startDate";
    private List<Virtue> spinnerData;
    private SharedPreferences sharedPreferences;
    private Toast nameHintToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            startDate = DateUtils.getFirstDayOfWeek();
        } else {
            startDate = new Date(savedInstanceState.getLong(START_DATE));
        }

        initMainProjection(startDate);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        daysOfWeekLayout = (LinearLayout) findViewById(R.id.daysOfWeekLayout);
        virtuesSpinner = (Spinner) findViewById(R.id.sp_virtues);
        virtueDescription = (TextView) findViewById(R.id.tv_virtue_description);

        initRecyclerView();
        initDaysOfWeekLayout();
        initSpinnerData();
        initSpinner();
        setupSharedPreferences();
    }

    private void initMainProjection(Date date) {
        MAIN_PROJECTION = new String[MAIN_PROJECTION.length];
        for (int i = 0; i < DAYS_COUNT; i++) {
            MAIN_PROJECTION[i] = createSelectString(DAY_CODE + i, date, i);
        }
        MAIN_PROJECTION[MAIN_PROJECTION_ID_INDEX] = VirtueEntry._ID;
        MAIN_PROJECTION[MAIN_PROJECTION_SHORT_NAME_INDEX] = VirtueEntry.COLUMN_SHORT_NAME;
        MAIN_PROJECTION[MAIN_PROJECTION_NAME_INDEX] = VirtueEntry.COLUMN_NAME;
    }

    protected void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        virtuesAdapter = new VirtuesAdapter(this, this);
        recyclerView.setAdapter(virtuesAdapter);
        getSupportLoaderManager().initLoader(ID_VIRTUES_LOADER, null, this);
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                /* Go ahead */
                setNewStartDateAndRefreshData(DateUtils.addDaysToDate(startDate, 7));
            }

            @Override
            public void onSwipeRight() {
                /* Go back */
                setNewStartDateAndRefreshData(DateUtils.addDaysToDate(startDate, -7));
            }
        });
    }

    /**
     * Refresh table with new data where newStartDate is new startDate
     * @param newStartDate
     */
    protected void setNewStartDateAndRefreshData(Date newStartDate) {
        this.startDate = newStartDate;
        initMainProjection(startDate);
        getSupportLoaderManager().restartLoader(ID_VIRTUES_LOADER, null, this);
        initDaysOfWeekLayout();
    }

    protected void initSpinner() {
        virtueSpinnerAdapter = new ArrayAdapter<Virtue>(this, R.layout.virtue_spinner_item, spinnerData);
        virtueSpinnerAdapter.setDropDownViewResource(R.layout.virtue_spinner_dropdown_item);
        virtuesSpinner.setAdapter(virtueSpinnerAdapter);
        virtuesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Virtue selected = virtueSpinnerAdapter.getItem(position);
                virtueDescription.setText(selected.getDescription());
                //Save current period virtue id
                PreferencesUtils.setSelectedVirtueId(selected.getId(), sharedPreferences, MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                virtueDescription.setText("");
            }
        });
    }

    protected void initSpinnerData() {
        spinnerData = new ArrayList<>();
        Cursor cursor = getContentResolver().query(CONTENT_VIRTUES_URI, null, null, null, null);
        if(cursor != null){
            int idInd = cursor.getColumnIndex(VirtueEntry._ID);
            int nameInd = cursor.getColumnIndex(VirtueEntry.COLUMN_NAME);
            int shortInd = cursor.getColumnIndex(VirtueEntry.COLUMN_SHORT_NAME);
            int descInd = cursor.getColumnIndex(VirtueEntry.COLUMN_DESCRIPTION);
            while (cursor.moveToNext()) {
                spinnerData.add(new Virtue(cursor.getInt(idInd), cursor.getString(nameInd),
                        cursor.getString(shortInd), cursor.getString(descInd)));
            }
            cursor.close();
        }
    }

    private void setupSharedPreferences() {
        if(sharedPreferences.contains(getString(R.string.pref_virtue_id_key))) {
            int id = sharedPreferences.getInt(getString(R.string.pref_virtue_id_key),
                    getResources().getInteger(R.integer.pref_virtue_id_default));
            virtuesSpinner.setSelection(virtueSpinnerAdapter.getPosition(new Virtue(id)));
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    protected void initDaysOfWeekLayout() {
        for(int i = 0; i<DAYS_COUNT; i++) {
            /* We need to create TextView for every day or update text and color of old TextView */
            //TODO ERROR ContextThemeWrapper
            TextView tv = (TextView) daysOfWeekLayout.getChildAt(i+1);
            if(tv == null) {
                tv = new TextView(new ContextThemeWrapper(this, R.style.DayOfWeekTextView));
                daysOfWeekLayout.addView(tv, i + 1, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));
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
        return " (select count(*) from " + PointEntry.TABLE_NAME +
                " where " + VirtueEntry.TABLE_NAME + "." + VirtueEntry._ID +
                " = " + PointEntry.TABLE_NAME + "." + PointEntry.COLUMN_VIRTUE_ID +
                " and " + PointEntry.TABLE_NAME + "." + PointEntry.COLUMN_DATE +
                "='" + formattedDateString + "') as '" + label + "' ";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_VIRTUES_LOADER:
                Uri uri = CONTENT_VIRTUES_URI;
                String sortOrder = VirtueEntry._ID + " ASC";

                return new CursorLoader(this,
                        uri,
                        MAIN_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        virtuesAdapter.swapCursor(data);
        virtuesAdapter.setSelectedId(PreferencesUtils.getSelectedVirtueId(sharedPreferences, this));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        virtuesAdapter.swapCursor(null);
        virtuesAdapter.setSelectedPosition(-1);
    }

    @Override
    public void onDayClick(long virtueId, int daysShift) {
        if(DateUtils.isToday(DateUtils.addDaysToDate(startDate, daysShift)))
            getContentResolver().insert(VirtuesContract.buildPointsUriWithDate(virtueId, DateUtils.addDaysToDate(startDate, daysShift)), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.virtues_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_data_action:
                getContentResolver().delete(VirtuesContract.CONTENT_POINTS_URI, null, null);
                Toast.makeText(this, "All points are deleted.", Toast.LENGTH_LONG).show();
                //FIXME KOSTYL
                getSupportLoaderManager().restartLoader(ID_VIRTUES_LOADER, null, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.pref_virtue_id_key))) {
            virtuesAdapter.setSelectedId(PreferencesUtils.getSelectedVirtueId(sharedPreferences, this));
            virtuesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onVirtueNameClick(String virtueName) {
        /* Show virtue name hint when click on it*/
        if(nameHintToast!= null){
            nameHintToast.cancel();
        }
        nameHintToast = Toast.makeText(this, virtueName, Toast.LENGTH_LONG);
        nameHintToast.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(START_DATE, startDate.getTime());
        super.onSaveInstanceState(outState);
    }
}
