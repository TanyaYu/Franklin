package com.example.tanyayuferova.franklin;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tanyayuferova.franklin.data.VirtuesContract.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private VirtuesAdapter virtuesAdapter;

    private static final int ID_VIRTUES_LOADER = 1;
    private final String TAG = MainActivity.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;

    private Date startDate;

    public static String[] MAIN_PROJECTION;
    public static final String DAY_1 = "day1";
    public static final String DAY_2 = "day2";
    public static final String DAY_3 = "day3";
    public static final String DAY_4 = "day4";
    public static final String DAY_5 = "day5";
    public static final String DAY_6 = "day6";
    public static final String DAY_7 = "day7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDate = Calendar.getInstance().getTime();

        MAIN_PROJECTION = new String[] {
                VirtueEntry.COLUMN_SHORT_NAME,
                createSelectString(DAY_1, 0),
                createSelectString(DAY_2, 1),
                createSelectString(DAY_3, 2),
                createSelectString(DAY_4, 3),
                createSelectString(DAY_5, 4),
                createSelectString(DAY_6, 5),
                createSelectString(DAY_7, 6),
        };

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        virtuesAdapter = new VirtuesAdapter(this);
        recyclerView.setAdapter(virtuesAdapter);
        getSupportLoaderManager().initLoader(ID_VIRTUES_LOADER, null, this);
    }

    String createSelectString(String label, int daysOff) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, daysOff);
        String formattedDateString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
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
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        virtuesAdapter.swapCursor(null);
    }
}
