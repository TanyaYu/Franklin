package com.tanyayuferova.franklin.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.tanyayuferova.franklin.data.VirtuesContract.*;

import static com.tanyayuferova.franklin.data.VirtuesContract.BASE_CONTENT_URI;
import static com.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI;
import static com.tanyayuferova.franklin.data.VirtuesContract.PATH_POINTS;
import static com.tanyayuferova.franklin.data.VirtuesContract.PATH_VIRTUES;
import static com.tanyayuferova.franklin.data.VirtuesContract.PATH_WEEKS;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesProvider extends ContentProvider {

    public static final int CODE_VIRTUES = 100;
    public static final int CODE_POINTS = 200;
    public static final int CODE_WEEKS = 300;
    public static final int CODE_POINTS_WITH_DATE = 201;
    public static final int CODE_VIRTUES_WITH_WEEK = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private VirtuesDbHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = VirtuesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PATH_VIRTUES, CODE_VIRTUES);
        matcher.addURI(authority, PATH_POINTS, CODE_POINTS);
        matcher.addURI(authority, PATH_WEEKS, CODE_WEEKS);
        matcher.addURI(authority, PATH_VIRTUES + "/#/" + PATH_POINTS + "/#",
                CODE_POINTS_WITH_DATE);
        matcher.addURI(authority, PATH_VIRTUES + "/#/#", CODE_VIRTUES_WITH_WEEK);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new VirtuesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            // Select points for specific virtue at specific date
            case CODE_POINTS_WITH_DATE: {
                String virtueId = uri.getPathSegments().get(1);
                String dateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{virtueId, dateString};
                cursor = dbHelper.getReadableDatabase().query(
                        PointEntry.TABLE_NAME,
                        projection,
                        PointEntry.COLUMN_VIRTUE_ID + " = ? and " +
                                PointEntry.COLUMN_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // Selects all virtues
            case CODE_VIRTUES: {
                cursor = dbHelper.getReadableDatabase().query(
                        VirtueEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // Selects all virtues of the weeks
            case CODE_WEEKS: {
                cursor = dbHelper.getReadableDatabase().query(
                        WeekEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // Selects virtue of specific week and year
            case CODE_VIRTUES_WITH_WEEK: {
                String week = uri.getPathSegments().get(1);
                String year = uri.getLastPathSegment();
                cursor = dbHelper.getReadableDatabase().query(
                        WeekEntry.TABLE_NAME,
                        projection,
                        WeekEntry.COLUMN_WEEK + " = ? and " + WeekEntry.COLUMN_YEAR + " = ? ",
                        new String[]{week, year},
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result;
        switch (uriMatcher.match(uri)) {
            // Creates point for specific virtue at specific date
            case CODE_POINTS_WITH_DATE: {
                String virtueId = uri.getPathSegments().get(1);
                String dateString = uri.getLastPathSegment();
                values = new ContentValues();
                values.put(PointEntry.COLUMN_DATE, dateString);
                values.put(PointEntry.COLUMN_VIRTUE_ID, virtueId);

                long newId = dbHelper.getWritableDatabase().insert(PointEntry.TABLE_NAME,
                        null, values);
                if (newId > 0) {
                    result = BASE_CONTENT_URI.buildUpon()
                            .appendPath(PATH_POINTS)
                            .appendPath(String.valueOf(newId))
                            .build();
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            // Creates virtue
            case CODE_VIRTUES: {
                long newId = dbHelper.getWritableDatabase().insert(VirtueEntry.TABLE_NAME,
                        null, values);
                if (newId > 0) {
                    result = ContentUris.withAppendedId(CONTENT_VIRTUES_URI, newId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            // Creates virtue of the specific week and year
            case CODE_VIRTUES_WITH_WEEK: {
                String week = uri.getPathSegments().get(1);
                String year = uri.getLastPathSegment();
                values.put(WeekEntry.COLUMN_WEEK, week);
                values.put(WeekEntry.COLUMN_YEAR, year);

                long newId = dbHelper.getWritableDatabase().insert(WeekEntry.TABLE_NAME,
                        null, values);
                if (newId > 0) {
                    result = uri.buildUpon()
                            .appendPath(String.valueOf(newId))
                            .build();
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result;
        switch (uriMatcher.match(uri)) {
            //Deletes all from Points table
            case CODE_POINTS: {
                result = dbHelper.getWritableDatabase().delete(
                        PointEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }

            //Deletes all from Week table
            case CODE_WEEKS: {
                result = dbHelper.getWritableDatabase().delete(
                        WeekEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }

            //Deletes first point with current date and virtue id
            case CODE_POINTS_WITH_DATE: {
                String virtueId = uri.getPathSegments().get(1);
                String dateString = uri.getLastPathSegment();
                result = dbHelper.getWritableDatabase().delete(
                        PointEntry.TABLE_NAME,
                        PointEntry._ID + " in (select " + PointEntry._ID + " from " +
                                PointEntry.TABLE_NAME + " where " +
                                PointEntry.COLUMN_VIRTUE_ID + " = ? and " +
                                PointEntry.COLUMN_DATE + " = ? limit 1)",
                        new String[]{virtueId, dateString});
                break;
            }

            //Deletes all from Week table with given week number and year
            case CODE_VIRTUES_WITH_WEEK: {
                String week = uri.getPathSegments().get(1);
                String year = uri.getLastPathSegment();
                result = dbHelper.getWritableDatabase().delete(
                        WeekEntry.TABLE_NAME,
                        WeekEntry.COLUMN_WEEK + " = ? and " +
                                WeekEntry.COLUMN_YEAR + " = ? " ,
                        new String[]{week, year});
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(result > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result;
        switch (uriMatcher.match(uri)) {
            //Updates virtue
            case CODE_VIRTUES: {
                result = dbHelper.getWritableDatabase().update(
                        VirtueEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(result > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("GetType function is not implemented");
    }
}
