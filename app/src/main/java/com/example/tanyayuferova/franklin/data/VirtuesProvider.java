package com.example.tanyayuferova.franklin.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.example.tanyayuferova.franklin.data.VirtuesContract.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.tanyayuferova.franklin.data.VirtuesContract.BASE_CONTENT_URI;
import static com.example.tanyayuferova.franklin.data.VirtuesContract.CONTENT_VIRTUES_URI;
import static com.example.tanyayuferova.franklin.data.VirtuesContract.PATH_POINTS;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesProvider extends ContentProvider {

    public static final int CODE_VIRTUES = 100;
    public static final int CODE_POINTS = 200;
    public static final int CODE_POINTS_WITH_DATE = 201;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private VirtuesDbHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = VirtuesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, VirtuesContract.PATH_VIRTUES, CODE_VIRTUES);
        matcher.addURI(authority, VirtuesContract.PATH_POINTS, CODE_POINTS);
        matcher.addURI(authority, VirtuesContract.PATH_VIRTUES + "/#/" + VirtuesContract.PATH_POINTS + "/#",
                CODE_POINTS_WITH_DATE);
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
            case CODE_POINTS_WITH_DATE: {
                String virtueId = uri.getPathSegments().get(1);
                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{virtueId, normalizedUtcDateString};
                cursor = dbHelper.getReadableDatabase().query(
                        VirtuesContract.PointEntry.TABLE_NAME,
                        projection,
                        VirtuesContract.PointEntry.COLUMN_VIRTUE_ID + " = ? and " +
                                VirtuesContract.PointEntry.COLUMN_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_VIRTUES: {
                cursor = dbHelper.getReadableDatabase().query(
                        VirtuesContract.VirtueEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
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
            case CODE_POINTS_WITH_DATE: {
                String virtueId = uri.getPathSegments().get(1);
                String dateString = uri.getLastPathSegment();
                values = new ContentValues();
                values.put(VirtuesContract.PointEntry.COLUMN_DATE, dateString);
                values.put(VirtuesContract.PointEntry.COLUMN_VIRTUE_ID, virtueId);

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
            case CODE_POINTS: {
                //Delete all from Points table
                result = dbHelper.getWritableDatabase().delete(
                        VirtuesContract.PointEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }

            case CODE_POINTS_WITH_DATE: {
                //Delete first point with current date and virtue id
                String virtueId = uri.getPathSegments().get(1);
                String dateString = uri.getLastPathSegment();
                result = dbHelper.getWritableDatabase().delete(
                        VirtuesContract.PointEntry.TABLE_NAME,
                        PointEntry._ID + " in (select " + PointEntry._ID + " from " +
                                PointEntry.TABLE_NAME + " where " +
                                VirtuesContract.PointEntry.COLUMN_VIRTUE_ID + " = ? and " +
                                VirtuesContract.PointEntry.COLUMN_DATE + " = ? limit 1)",
                        new String[]{virtueId, dateString});
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
        throw new RuntimeException("Update function is not implemented");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("GetType function is not implemented");
    }
}
