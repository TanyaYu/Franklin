package com.example.tanyayuferova.franklin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.tanyayuferova.franklin.data.VirtuesContract;
import com.example.tanyayuferova.franklin.entity.Virtue;

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
     * Finds virtue for provided date
     * @param context
     * @param date
     * @return
     */
    public static Virtue getVirtueOfWeek(Context context, Date date) {
        int id = getVirtueIdOfWeek(context, date);
        Cursor c = context.getContentResolver().query(VirtuesContract.CONTENT_VIRTUES_URI,
                new String[]{VirtuesContract.VirtueEntry._ID,
                        VirtuesContract.VirtueEntry.COLUMN_NAME,
                        VirtuesContract.VirtueEntry.COLUMN_SHORT_NAME,
                        VirtuesContract.VirtueEntry.COLUMN_DESCRIPTION},
                VirtuesContract.VirtueEntry._ID + " = ? ",
                new String[]{ String.valueOf(id)},
                null);
        if(c!= null && c.moveToFirst()){
            Virtue result = new Virtue(c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3));
            c.close();
            return result;
        } else {
            throw new UnsupportedOperationException("Cannot find given virtue id");
        }
    }

    /**
     * Finds virtue id for provided date
     * @param context
     * @param date
     * @return
     */
    public static int getVirtueIdOfWeek(Context context, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getVirtueIdOfWeek(context, calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
    }

    /**
     * Finds virtue id for provided week number and year
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
        if(cursor != null && cursor.moveToFirst()){
            int result = cursor.getInt(0);
            cursor.close();;
            return result;
        }
        /* If enable to find, try to find next id */
        int next = getNextVirtueId(context, week, year);
        /* If enable to find next, try to find previous */
        if(next == 0)
            next = getPreviousId(context, week, year);
        /* If enable to find previous, try to find first */
        if(next == 0)
            next = getFirstVirtueId(context);
        if(next == 0)
            throw new UnsupportedOperationException("Unable to find any virtue");
        return next;
    }

    /**
     * Finds next virtue id for provided week and year using previous history of weeks
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
        if(cursor!= null && cursor.moveToFirst()) {
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

            int virtuesAmount = countVirtues(context);
            if (virtuesAmount == 0)
                return 0;

            int newId = (lastId + weeks) % virtuesAmount;
            return newId == 0 ? virtuesAmount : newId;
        }
        return 0;
    }

    /**
     * Finds previous virtue id for provided week and year using following history of weeks
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
        if(cursor!= null && cursor.moveToFirst()) {
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

            int virtuesAmount = countVirtues(context);
            if (virtuesAmount == 0)
                return 0;

            int newId = (lastId - weeks) % virtuesAmount;
            return newId == 0 ? virtuesAmount : newId;
        }
        return 0;
    }

    /**
     * Finds the first virtue in table
     * @param context
     * @return vurtue id or 0 if enable to find
     */
    public static int getFirstVirtueId(Context context) {
        Cursor cursor = context.getContentResolver().query(
                VirtuesContract.CONTENT_VIRTUES_URI, new String[]{VirtuesContract.VirtueEntry._ID},
                null, null, VirtuesContract.VirtueEntry._ID + " ASC");
        if(cursor != null && cursor.moveToFirst()){
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        return 0;
    }

    /**
     * Saves virtue id for provided date
     * @param context
     * @param virtueId
     * @param date
     */
    public static void setVirtueOfWeek(Context context, int virtueId, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setVirtueOfWeek(context, virtueId, calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
    }

    /**
     * Saves virtue id for provided week and year
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

    /**
     * Returns amount of virtues in virtues table
     * @param context
     * @return
     */
    private static int countVirtues(Context context) {
        Cursor cursor = context.getContentResolver().query(VirtuesContract.CONTENT_VIRTUES_URI,
                new String[] {"count (*)"}, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }

        return 0;
    }
}
