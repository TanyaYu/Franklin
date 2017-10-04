package com.example.tanyayuferova.franklin.data;

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
    private static final int DATABASE_VERSION = 1;
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
                        VirtueEntry._ID               + " INTEGER PRIMARY KEY, " +
                        VirtueEntry.COLUMN_NAME       + " varchar(50) NOT NULL, "                 +
                        VirtueEntry.COLUMN_DESCRIPTION + " text NOT NULL,"                  +
                        VirtueEntry.COLUMN_SHORT_NAME   + " varchar(5) NOT NULL); ";


        db.execSQL(SQL_CREATE_VIRTUES_TABLE);
    }

    private void createPointsTable(SQLiteDatabase db) {
        final String SQL_CREATE_POINTS_TABLE =
                "CREATE TABLE " + PointEntry.TABLE_NAME + " (" +
                        PointEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PointEntry.COLUMN_DATE       + " INTEGER NOT NULL, "                 +
                        PointEntry.COLUMN_VIRTUE_ID + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + PointEntry.COLUMN_VIRTUE_ID + ") REFERENCES " +
                            VirtueEntry.TABLE_NAME +" ("+VirtueEntry._ID+")); ";
        db.execSQL(SQL_CREATE_POINTS_TABLE);
    }

    private void insertDefaultVirtuesValues(SQLiteDatabase db) {
        Resources resources = mContext.getResources();
        final String SQL_INSERT_DEFAULT_VALUES =
                        "insert into " + VirtueEntry.TABLE_NAME + " values (1, '" +
                        resources.getString(R.string.tem_name) + "', '" +
                        resources.getString(R.string.tem_desc) + "', '" +
                        resources.getString(R.string.tem_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (2, '" +
                        resources.getString(R.string.sil_name) + "', '" +
                        resources.getString(R.string.sil_desc) + "', '" +
                        resources.getString(R.string.sil_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (3, '" +
                        resources.getString(R.string.o_name) + "', '" +
                        resources.getString(R.string.o_desc) + "', '" +
                        resources.getString(R.string.o_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (4, '" +
                        resources.getString(R.string.r_name) + "', '" +
                        resources.getString(R.string.r_desc) + "', '" +
                        resources.getString(R.string.r_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (5, '" +
                        resources.getString(R.string.f_name) + "', '" +
                        resources.getString(R.string.f_desc) + "', '" +
                        resources.getString(R.string.f_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (6, '" +
                        resources.getString(R.string.i_name) + "', '" +
                        resources.getString(R.string.i_desc) + "', '" +
                        resources.getString(R.string.i_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (7, '" +
                        resources.getString(R.string.sin_name) + "', '" +
                        resources.getString(R.string.sin_desc) + "', '" +
                        resources.getString(R.string.sin_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (8, '" +
                        resources.getString(R.string.j_name) + "', '" +
                        resources.getString(R.string.j_desc) + "', '" +
                        resources.getString(R.string.j_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (9, '" +
                        resources.getString(R.string.m_name) + "', '" +
                        resources.getString(R.string.m_desc) + "', '" +
                        resources.getString(R.string.m_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (10, '" +
                        resources.getString(R.string.cl_name) + "', '" +
                        resources.getString(R.string.cl_desc) + "', '" +
                        resources.getString(R.string.cl_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (11, '" +
                        resources.getString(R.string.tra_name) + "', '" +
                        resources.getString(R.string.tra_desc) + "', '" +
                        resources.getString(R.string.tra_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (12, '" +
                        resources.getString(R.string.ch_name) + "', '" +
                        resources.getString(R.string.ch_desc) + "', '" +
                        resources.getString(R.string.ch_short) +"'); " +

                        "insert into " + VirtueEntry.TABLE_NAME + " values (13, '" +
                        resources.getString(R.string.h_name) + "', '" +
                        resources.getString(R.string.h_desc) + "', '" +
                        resources.getString(R.string.h_short) +"'); ";
        db.execSQL(SQL_INSERT_DEFAULT_VALUES);
    }
}
