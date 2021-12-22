package com.example.a2thepeak;

import android.provider.BaseColumns;

public class RatingContract {

    private RatingContract() {}

    public static class RatingTable implements BaseColumns {
        public static final String TABLE_NAME = "ratingTable";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DESCRIPTION = "review";
        public static final String COLUMN_PAGEID = "pageID";
    }
}
