package com.tanyayuferova.franklin.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.data.VirtuesContract.*;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "virtues.db";
    private static final int DATABASE_VERSION = 7;
    private final Context context;

    public VirtuesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createVirtuesTable(db);
        createPointsTable(db);
        createWeekTable(db);
        insertDefaultVirtuesValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // After adding translations for 10 languages, we need to update virtues name in DB.
        updateDefaultVirtuesValues(db);
    }

    private void createVirtuesTable(SQLiteDatabase db) {
        final String SQL_CREATE_VIRTUES_TABLE =
                "CREATE TABLE " + VirtueEntry.TABLE_NAME + " (" +
                        VirtueEntry._ID + " INTEGER PRIMARY KEY, " +
                        VirtueEntry.COLUMN_NAME + " varchar(50) NOT NULL, " +
                        VirtueEntry.COLUMN_DESCRIPTION + " text NOT NULL," +
                        VirtueEntry.COLUMN_SHORT_NAME + " varchar(5) NOT NULL); ";


        db.execSQL(SQL_CREATE_VIRTUES_TABLE);
    }

    private void createPointsTable(SQLiteDatabase db) {
        final String SQL_CREATE_POINTS_TABLE =
                "CREATE TABLE " + PointEntry.TABLE_NAME + " (" +
                        PointEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PointEntry.COLUMN_DATE + " DATE NOT NULL, " +
                        PointEntry.COLUMN_VIRTUE_ID + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + PointEntry.COLUMN_VIRTUE_ID + ") REFERENCES " +
                        VirtueEntry.TABLE_NAME + " (" + VirtueEntry._ID + ")); ";
        db.execSQL(SQL_CREATE_POINTS_TABLE);
    }

    private void createWeekTable(SQLiteDatabase db) {
        final String SQL_CREATE_WEEK_TABLE =
                "CREATE TABLE " + WeekEntry.TABLE_NAME + " (" +
                        WeekEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeekEntry.COLUMN_WEEK + " INTEGER NOT NULL, " +
                        WeekEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                        WeekEntry.COLUMN_VIRTUE_ID + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + WeekEntry.COLUMN_VIRTUE_ID + ") REFERENCES " +
                        VirtueEntry.TABLE_NAME + " (" + VirtueEntry._ID + ")); ";
        db.execSQL(SQL_CREATE_WEEK_TABLE);
    }

    private void insertDefaultVirtuesValues(SQLiteDatabase db) {
        Resources resources = context.getResources();
        int[] ids = resources.getIntArray(R.array.virtues_ids);
        String[] names = resources.getStringArray(R.array.virtues_names);
        String[] shortNames = resources.getStringArray(R.array.virtues_short_names);
        String[] descriptions = resources.getStringArray(R.array.virtues_descriptions);

        for(int id = 0; id < ids.length; id++) {
            insertDefaultVirtueValue(db, ids[id], names[id], shortNames[id], descriptions[id]);
        }
    }

    public void updateDefaultVirtuesValues(SQLiteDatabase db) {
        Resources resources = context.getResources();
        int[] ids = resources.getIntArray(R.array.virtues_ids);
        String[] names = resources.getStringArray(R.array.virtues_names);
        String[] shortNames = resources.getStringArray(R.array.virtues_short_names);
        String[] descriptions = resources.getStringArray(R.array.virtues_descriptions);

        for(int id = 0; id < ids.length; id++) {
            updateDefaultVirtueValue(db, ids[id], names[id], shortNames[id], descriptions[id]);
        }
    }

    private long insertDefaultVirtueValue(SQLiteDatabase db, long id, String name, String shortName, String description) {
        ContentValues values = new ContentValues();
        values.put(VirtueEntry._ID, id);
        values.put(VirtueEntry.COLUMN_NAME, name);
        values.put(VirtueEntry.COLUMN_SHORT_NAME, shortName);
        values.put(VirtueEntry.COLUMN_DESCRIPTION, description);
        return db.insert(VirtueEntry.TABLE_NAME, null, values);
    }

    private long updateDefaultVirtueValue(SQLiteDatabase db, long id, String name, String shortName, String description) {
        ContentValues values = new ContentValues();
        values.put(VirtueEntry.COLUMN_NAME, name);
        values.put(VirtueEntry.COLUMN_SHORT_NAME, shortName);
        values.put(VirtueEntry.COLUMN_DESCRIPTION, description);
        return db.update(VirtueEntry.TABLE_NAME, values, VirtueEntry._ID + " = ? ", new String[] {String.valueOf(id)});
    }
}
