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
    private static final int DATABASE_VERSION = 8;
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
        //We need to drop redundant columns in Virtue table
        //SQLite does not have drop column syntax, that's why we have to create a copy of Virtue table
        //without redundant columns and drop old table

        db.execSQL("alter table " + VirtueEntry.TABLE_NAME + " rename to " + VirtueEntry.TABLE_NAME + "_old;");
        createVirtuesTable(db);
        db.execSQL("insert into " + VirtueEntry.TABLE_NAME + " (" + VirtueEntry._ID + ") select ("
                + VirtueEntry._ID + ") from " + VirtueEntry.TABLE_NAME + "_old;");
        db.execSQL("drop table " + VirtueEntry.TABLE_NAME + "_old;");
    }

    private void createVirtuesTable(SQLiteDatabase db) {
        final String SQL_CREATE_VIRTUES_TABLE =
                "CREATE TABLE " + VirtueEntry.TABLE_NAME + " (" +
                        VirtueEntry._ID + " INTEGER PRIMARY KEY); ";


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

        for (int id = 0; id < ids.length; id++) {
            insertDefaultVirtueValue(db, ids[id]);
        }
    }

    private long insertDefaultVirtueValue(SQLiteDatabase db, long id) {
        ContentValues values = new ContentValues();
        values.put(VirtueEntry._ID, id);
        return db.insert(VirtueEntry.TABLE_NAME, null, values);
    }
}
