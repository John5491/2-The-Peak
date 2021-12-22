package com.example.a2thepeak;

import android.provider.BaseColumns;

public class ProfileContract {

    private ProfileContract() {}

    public static class ProfileTable implements BaseColumns {
        public static final String TABLE_NAME = "userProfile";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PROFILE_PICTURE = "profilePicture";
    }
}
