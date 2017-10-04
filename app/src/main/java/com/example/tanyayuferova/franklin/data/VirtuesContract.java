package com.example.tanyayuferova.franklin.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesContract {

    public static final String CONTENT_AUTHORITY = "com.example.tanyayuferova.franklin";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_POINTS = "points";
    public static final String PATH_VIRTUES = "virtues";
    public static final Uri CONTENT_VIRTUES_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_VIRTUES)
            .build();
    public static Uri buildPointsUriWithDate(long virtueId, long date) {
        return CONTENT_VIRTUES_URI.buildUpon()
                .appendPath(Long.toString(virtueId))
                .appendPath(PATH_POINTS)
                .appendPath(Long.toString(date))
                .build();
    }

    public static final class VirtueEntry implements BaseColumns {
        public static final String TABLE_NAME = "virtue";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SHORT_NAME = "short_name";
    }

    public static final class PointEntry implements BaseColumns {
        public static final String TABLE_NAME = "point";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_VIRTUE_ID = "virtue_id";
    }
}
