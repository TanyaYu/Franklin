package com.example.tanyayuferova.franklin.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tanyayuferova.franklin.R;
import com.example.tanyayuferova.franklin.data.VirtuesContract.*;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "virtues.db";
    private static final int DATABASE_VERSION = 3;
    private final Context mContext;

    public VirtuesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createVirtuesTable(db);
        createPointsTable(db);
        insertDefaultVirtuesValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PointEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VirtueEntry.TABLE_NAME);
        onCreate(db);
    }

    private void createVirtuesTable(SQLiteDatabase db) {
        final String SQL_CREATE_VIRTUES_TABLE =
                "CREATE TABLE " + VirtueEntry.TABLE_NAME + " (" +
                        VirtueEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        VirtueEntry.COLUMN_NAME + " varchar(50) NOT NULL, " +
                        VirtueEntry.COLUMN_DESCRIPTION + " text NOT NULL," +
                        VirtueEntry.COLUMN_SHORT_NAME + " varchar(5) NOT NULL); ";


        db.execSQL(SQL_CREATE_VIRTUES_TABLE);
    }

    private void createPointsTable(SQLiteDatabase db) {
        final String SQL_CREATE_POINTS_TABLE =
                "CREATE TABLE " + PointEntry.TABLE_NAME + " (" +
                        PointEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PointEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        PointEntry.COLUMN_VIRTUE_ID + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + PointEntry.COLUMN_VIRTUE_ID + ") REFERENCES " +
                        VirtueEntry.TABLE_NAME + " (" + VirtueEntry._ID + ")); ";
        db.execSQL(SQL_CREATE_POINTS_TABLE);
    }

    private void insertDefaultVirtuesValues(SQLiteDatabase db) {
        Resources resources = mContext.getResources();
        insertDefaultVirtueValue(db, resources.getString(R.string.tem_name), resources.getString(R.string.tem_short), resources.getString(R.string.tem_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.sil_name), resources.getString(R.string.sil_short), resources.getString(R.string.sil_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.o_name), resources.getString(R.string.o_short), resources.getString(R.string.o_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.r_name), resources.getString(R.string.r_short), resources.getString(R.string.r_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.f_name), resources.getString(R.string.f_short), resources.getString(R.string.f_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.i_name), resources.getString(R.string.i_short), resources.getString(R.string.i_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.sin_name), resources.getString(R.string.sin_short), resources.getString(R.string.sin_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.j_name), resources.getString(R.string.j_short), resources.getString(R.string.j_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.m_name), resources.getString(R.string.m_short), resources.getString(R.string.m_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.cl_name), resources.getString(R.string.cl_short), resources.getString(R.string.cl_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.tra_name), resources.getString(R.string.tra_short), resources.getString(R.string.tra_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.ch_name), resources.getString(R.string.ch_short), resources.getString(R.string.ch_desc));
        insertDefaultVirtueValue(db, resources.getString(R.string.h_name), resources.getString(R.string.h_short), resources.getString(R.string.h_desc));
    }

    private long insertDefaultVirtueValue(SQLiteDatabase db, String name, String shortName, String description) {
        ContentValues values = new ContentValues();
        values.put(VirtueEntry.COLUMN_NAME, name);
        values.put(VirtueEntry.COLUMN_SHORT_NAME, shortName);
        values.put(VirtueEntry.COLUMN_DESCRIPTION, description);
        return db.insert(VirtueEntry.TABLE_NAME, null, values);
    }
}
