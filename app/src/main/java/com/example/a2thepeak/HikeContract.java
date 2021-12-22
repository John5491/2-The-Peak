package com.example.a2thepeak;

import android.provider.BaseColumns;

public class HikeContract {

    private HikeContract() {}

    public static class HikeTable implements BaseColumns {
        public static final String TABLE_NAME = "hikeInfo";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RATING_COUNT = "ratingCount";
        public static final String COLUMN_LENGTH = "length";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ELEVATION_GAIN = "elevationGain";
        public static final String COLUMN_ROUTE_TYPE = "routeType";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
