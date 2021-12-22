package com.example.a2thepeak;

import android.provider.BaseColumns;

public class FavouriteContract {

    private FavouriteContract() {}

    public static class FavouriteTable implements BaseColumns {
        public static final String TABLE_NAME = "favouriteTable";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_HIKE1 = "hike1";
        public static final String COLUMN_HIKE2 = "hike2";
        public static final String COLUMN_HIKE3 = "hike3";
    }
}
