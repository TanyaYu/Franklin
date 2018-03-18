package com.tanyayuferova.franklin.database;

import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tanya Yuferova on 10/4/2017.
 */

public class VirtuesContract {

    public static final String CONTENT_AUTHORITY = "com.tanyayuferova.franklin";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_POINTS = "points";
    public static final String PATH_VIRTUES = "virtues";
    public static final String PATH_WEEKS = "weeks";

    public static final Uri CONTENT_VIRTUES_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_VIRTUES)
            .build();

    public static final Uri CONTENT_WEEKS_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_WEEKS)
            .build();

    /**
     * Builds Uri to select points for specific virtue at specific date
     * @param virtueId
     * @param date
     * @return
     */
    public static Uri buildPointsUriWithDate(long virtueId, Date date) {
        String formattedDateString = new SimpleDateFormat("yyyyMMdd").format(date);
        return CONTENT_VIRTUES_URI.buildUpon()
                .appendPath(Long.toString(virtueId))
                .appendPath(PATH_POINTS)
                .appendPath(formattedDateString)
                .build();
    }

    /**
     * Builds Uri to select virtue of the week
     * @param week
     * @param year
     * @return
     */
    public static Uri buildVirtueUriWithWeek(int week, int year) {
        return CONTENT_VIRTUES_URI.buildUpon()
                .appendPath(String.valueOf(week))
                .appendPath(String.valueOf(year))
                .build();
    }

    /**
     * Represents just Virtue with id. Name and descriptions store in resources
     */
    public static final class VirtueEntry implements BaseColumns {
        public static final String TABLE_NAME = "virtue";
    }

    /**
     * Represents a point made at specific date for specific virtue
     */
    public static final class PointEntry implements BaseColumns {
        public static final String TABLE_NAME = "point";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_VIRTUE_ID = "virtue_id";
    }

    /**
     * Represents virtue of the week - specific virtue at specific week
     */
    public static final class WeekEntry implements BaseColumns {
        public static final String TABLE_NAME = "week";

        public static final String COLUMN_WEEK = "week";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_VIRTUE_ID = "virtue_id";
    }
}
