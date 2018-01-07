package com.tanyayuferova.franklin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.data.VirtuesContract;
import com.tanyayuferova.franklin.entity.Virtue;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tanya Yuferova on 11/3/2017.
 */

public class VirtueOfWeekUtils {

    /* Milliseconds in one week */
    private static final long MILLIS_IN_WEEK = TimeUnit.DAYS.toMillis(7);

    /**
     * Finds virtue for specific date
     * @param context
     * @param date
     * @return
     */
    public static Virtue getVirtueOfWeek(Context context, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        // This is necessary for the last week of the year, which is the first week of the next year also
        if(week==1 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            year++;

        return Virtue.newVirtueById(context, getVirtueIdOfWeek(context, week, year));
    }

    /**
     * Finds virtue id for specific week number and year
     * @param context
     * @param week
     * @param year
     * @return
     */
    public static int getVirtueIdOfWeek(Context context, int week, int year) {
        /* Try to find virtue for current week and year */
        Uri uri = VirtuesContract.buildVirtueUriWithWeek(week, year);
        Cursor cursor = context.getContentResolver().query(uri, new String[] {VirtuesContract.WeekEntry.COLUMN_VIRTUE_ID},
                null, null, null);
        if(cursor.moveToFirst()){
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        /* If enable to find, try to find next id */
        int next = getNextVirtueId(context, week, year);
        /* If enable to find next, try to find previous */
        if(next == 0)
            next = getPreviousId(context, week, year);
        /* If enable to find previous, try to find first */
        if(next == 0)
            // Take first id
            next = context.getResources().getIntArray(R.array.virtues_ids)[0];
        if(next == 0)
            throw new UnsupportedOperationException("Unable to find any virtue");
        return next;
    }

    /**
     * Finds next virtue id for specific week and year using previous history of weeks
     * @param context
     * @param week
     * @param year
     * @return virtue id or 0 if enable to find next id
     */
    private static int getNextVirtueId(Context context, int week, int year) {
        /* Try to find previous history of weeks */
        Cursor cursor = context.getContentResolver().query(VirtuesContract.CONTENT_WEEKS_URI,
                null,
                VirtuesContract.WeekEntry.COLUMN_YEAR + " = ? and "
                        + VirtuesContract.WeekEntry.COLUMN_WEEK + " < ? or " +
                        VirtuesContract.WeekEntry.COLUMN_YEAR + " < ?",
                new String[] {String.valueOf(year), String.valueOf(week), String.valueOf(year)},
                VirtuesContract.WeekEntry.COLUMN_YEAR + " DESC, " + VirtuesContract.WeekEntry.COLUMN_WEEK + " DESC");
        if(cursor.moveToFirst()) {
            /* The last id in history and its week number and year */
            int lastId = cursor.getInt(cursor.getColumnIndex(VirtuesContract.WeekEntry.COLUMN_VIRTUE_ID));
            int lastWeek = cursor.getInt(cursor.getColumnIndex(VirtuesContract.WeekEntry.COLUMN_WEEK));
            int lastYear = cursor.getInt(cursor.getColumnIndex(VirtuesContract.WeekEntry.COLUMN_YEAR));
            cursor.close();

            Calendar last = Calendar.getInstance();
            last.set(Calendar.YEAR, lastYear);
            last.set(Calendar.WEEK_OF_YEAR, lastWeek);

            Calendar current = Calendar.getInstance();
            current.set(Calendar.YEAR, year);
            current.set(Calendar.WEEK_OF_YEAR, week);

            /* How many weeks passed after last entry */
            int weeks = (int) ((current.getTime().getTime() - last.getTime().getTime()) / MILLIS_IN_WEEK);

            int virtuesAmount = context.getResources().getIntArray(R.array.virtues_ids).length;
            int newId = (lastId + weeks) % virtuesAmount;
            return newId == 0 ? virtuesAmount : newId;
        }
        return 0;
    }

    /**
     * Finds previous virtue id for specific week and year using following history of weeks
     * @param context
     * @param week
     * @param year
     * @return virtue id or 0 if enable to find
     */
    private static int getPreviousId(Context context, int week, int year) {
        /* Try to find following history of weeks */
        Cursor cursor = context.getContentResolver().query(VirtuesContract.CONTENT_WEEKS_URI,
                null,
                VirtuesContract.WeekEntry.COLUMN_YEAR+" = ? and "
                        + VirtuesContract.WeekEntry.COLUMN_WEEK + " > ? or " +
                        VirtuesContract.WeekEntry.COLUMN_YEAR + " > ?",
                new String[] {String.valueOf(year), String.valueOf(week), String.valueOf(year)},
                VirtuesContract.WeekEntry.COLUMN_YEAR + " ASC, " + VirtuesContract.WeekEntry.COLUMN_WEEK + " ASC");
        if(cursor.moveToFirst()) {
            /* The first id in history and its week number and year */
            int lastId = cursor.getInt(cursor.getColumnIndex(VirtuesContract.WeekEntry.COLUMN_VIRTUE_ID));
            int lastWeek = cursor.getInt(cursor.getColumnIndex(VirtuesContract.WeekEntry.COLUMN_WEEK));
            int lastYear = cursor.getInt(cursor.getColumnIndex(VirtuesContract.WeekEntry.COLUMN_YEAR));
            cursor.close();

            Calendar last = Calendar.getInstance();
            last.set(Calendar.YEAR, lastYear);
            last.set(Calendar.WEEK_OF_YEAR, lastWeek);

            Calendar current = Calendar.getInstance();
            current.set(Calendar.YEAR, year);
            current.set(Calendar.WEEK_OF_YEAR, week);

            /* How many weeks passed before first entry */
            int weeks = (int) ((last.getTime().getTime() - current.getTime().getTime()) / MILLIS_IN_WEEK);

            int virtuesAmount = context.getResources().getIntArray(R.array.virtues_ids).length;
            int newId = (lastId - weeks) % virtuesAmount;
            return newId == 0 ? virtuesAmount : newId;
        }
        return 0;
    }

    /**
     * Saves virtue id for specific date
     * @param context
     * @param virtueId
     * @param date
     */
    public static void setVirtueOfWeek(Context context, int virtueId, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        // This is necessary for the last week of the year, which is the first week of the next year also
        if(week==1 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            year++;

        setVirtueOfWeek(context, virtueId, week, year);
    }

    /**
     * Saves virtue id for specific week and year
     * @param context
     * @param virtueId
     * @param week
     * @param year
     */
    public static void setVirtueOfWeek(Context context, int virtueId, int week, int year) {
        Uri uri = VirtuesContract.buildVirtueUriWithWeek(week, year);
        ContentValues values = new ContentValues();
        values.put(VirtuesContract.WeekEntry.COLUMN_VIRTUE_ID, virtueId);
        context.getContentResolver().delete(uri, null, null);
        context.getContentResolver().insert(uri, values);
    }
}
