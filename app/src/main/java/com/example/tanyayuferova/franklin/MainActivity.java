package com.example.tanyayuferova.franklin;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanyayuferova.franklin.data.VirtuesContract;
import com.example.tanyayuferova.franklin.data.VirtuesContract.*;
import com.example.tanyayuferova.franklin.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        VirtuesAdapter.VirtuesAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private VirtuesAdapter virtuesAdapter;
    private LinearLayout daysOfWeekLayout;

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
    public static String[] MAIN_PROJECTION = new String[DAYS_COUNT + 2];;
    public static String DAY_CODE = "day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDate = DateUtils.getFirstDayOfWeek();

        for (int i = 0; i < DAYS_COUNT; i++) {
            MAIN_PROJECTION[i] = createSelectString(DAY_CODE + i, i);
        }
        MAIN_PROJECTION[DAYS_COUNT] = VirtueEntry._ID;
        MAIN_PROJECTION[DAYS_COUNT + 1] = VirtueEntry.COLUMN_SHORT_NAME;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        daysOfWeekLayout = (LinearLayout) findViewById(R.id.daysOfWeekLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        virtuesAdapter = new VirtuesAdapter(this, this);
        recyclerView.setAdapter(virtuesAdapter);
        getSupportLoaderManager().initLoader(ID_VIRTUES_LOADER, null, this);

        initDaysOfWeekLayout();
    }

    protected void initDaysOfWeekLayout() {
        for(int i = 0; i<DAYS_COUNT; i++) {
            TextView tv = new TextView(new ContextThemeWrapper(this, R.style.DayOfWeekTextView));
            daysOfWeekLayout.addView(tv, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.addDaysToDate(startDate, i));
            if(DateUtils.isToday(calendar.getTime())){
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            String caption = null;
            switch (calendar.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY : caption = getResources().getString(R.string.mon);
                    break;
                case Calendar.TUESDAY : caption = getResources().getString(R.string.tue);
                    break;
                case Calendar.WEDNESDAY : caption = getResources().getString(R.string.wed);
                    break;
                case Calendar.THURSDAY : caption = getResources().getString(R.string.thu);
                    break;
                case Calendar.FRIDAY : caption = getResources().getString(R.string.fri);
                    break;
                case Calendar.SATURDAY : caption = getResources().getString(R.string.sat);
                    break;
                case Calendar.SUNDAY : caption = getResources().getString(R.string.sun);
                    break;
            }
            tv.setText(caption);
        }

    }

    /**
     * Creates subquery for main points selection. This is query only for one column. It will contain
     * the amounts of points at start date + daysShift for all virtues
     *
     * @param label     alias for column
     * @param daysShift days shift for start date
     * @return subquery string
     */
    protected String createSelectString(String label, int daysShift) {
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
        ;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        virtuesAdapter.swapCursor(null);
    }

    @Override
    public void onClick(long virtueId, int daysShift) {
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
}
