package com.example.a2thepeak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TTPDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "2ThePeak.db";
    private static final int DATABASE_VERSION = 13;

    private SQLiteDatabase db;

    public TTPDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_PROFILE_TABLE = "CREATE TABLE " +
                ProfileContract.ProfileTable.TABLE_NAME + " ( " +
                ProfileContract.ProfileTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProfileContract.ProfileTable.COLUMN_USERNAME + " TEXT, " +
                ProfileContract.ProfileTable.COLUMN_PASSWORD + " TEXT, " +
                ProfileContract.ProfileTable.COLUMN_PHONE + " TEXT DEFAULT '', " +
                ProfileContract.ProfileTable.COLUMN_EMAIL + " TEXT DEFAULT '', " +
                ProfileContract.ProfileTable.COLUMN_PROFILE_PICTURE + " TEXT DEFAULT 'android.resource://com.example.a2thepeak/drawable/profile')";

        final String SQL_CREATE_HIKE_TABLE = "CREATE TABLE " +
                HikeContract.HikeTable.TABLE_NAME + " ( " +
                HikeContract.HikeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HikeContract.HikeTable.COLUMN_TITLE + " TEXT, " +
                HikeContract.HikeTable.COLUMN_REGION + " TEXT, " +
                HikeContract.HikeTable.COLUMN_DIFFICULTY + " INTEGER, " +
                HikeContract.HikeTable.COLUMN_RATING + " DOUBLE DEFAULT 0, " +
                HikeContract.HikeTable.COLUMN_RATING_COUNT + " INTEGER DEFAULT 0, " +
                HikeContract.HikeTable.COLUMN_LENGTH + " DOUBLE, " +
                HikeContract.HikeTable.COLUMN_TIME + " TEXT, " +
                HikeContract.HikeTable.COLUMN_ELEVATION_GAIN + " INTEGER, " +
                HikeContract.HikeTable.COLUMN_ROUTE_TYPE + " TEXT, " +
                HikeContract.HikeTable.COLUMN_DESCRIPTION + " TEXT)";

        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE " +
                FavouriteContract.FavouriteTable.TABLE_NAME + " ( " +
                FavouriteContract.FavouriteTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouriteContract.FavouriteTable.COLUMN_USERNAME + " TEXT, " +
                FavouriteContract.FavouriteTable.COLUMN_HIKE1 + " INTEGER DEFAULT 0, " +
                FavouriteContract.FavouriteTable.COLUMN_HIKE2 + " INTEGER DEFAULT 0, " +
                FavouriteContract.FavouriteTable.COLUMN_HIKE3 + " INTEGER DEFAULT 0)";

        final String SQL_CREATE_RATING_TABLE = "CREATE TABLE " +
                RatingContract.RatingTable.TABLE_NAME + " ( " +
                RatingContract.RatingTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RatingContract.RatingTable.COLUMN_USERNAME + " TEXT, " +
                RatingContract.RatingTable.COLUMN_RATING + " DOUBLE, " +
                RatingContract.RatingTable.COLUMN_DESCRIPTION + " TEXT, " +
                RatingContract.RatingTable.COLUMN_PAGEID + " INTEGER)";

        final String SQL_CREATE_HIKE_INSERTED_INDICATOR = "CREATE TABLE isHikeInserted (_id INTEGER PRIMARY KEY AUTOINCREMENT, boolean INTEGER DEFAULT 0)";

        db.execSQL(SQL_CREATE_PROFILE_TABLE);
        db.execSQL(SQL_CREATE_HIKE_TABLE);
        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);
        db.execSQL(SQL_CREATE_RATING_TABLE);
        db.execSQL(SQL_CREATE_HIKE_INSERTED_INDICATOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProfileContract.ProfileTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HikeContract.HikeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.FavouriteTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RatingContract.RatingTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS isHikeInserted");
        onCreate(db);
    }

    public boolean createProfile(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ProfileContract.ProfileTable.COLUMN_USERNAME, username);
        cv.put(ProfileContract.ProfileTable.COLUMN_PASSWORD, password);
        long isInserted = db.insert(ProfileContract.ProfileTable.TABLE_NAME, null, cv);
        if(isInserted == -1) return false;
        else return true;
    }

    public void createFavouriteProfile(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FavouriteContract.FavouriteTable.COLUMN_USERNAME, username);
        db.insert(FavouriteContract.FavouriteTable.TABLE_NAME, null, cv);
    }

    public boolean validateProfile(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ProfileContract.ProfileTable.TABLE_NAME +
                " WHERE " + ProfileContract.ProfileTable.COLUMN_USERNAME + " = ?", new String[]{username});
        if(c.getCount() > 0) return true;
        else return false;
    }

    public boolean verifyProfile(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ProfileContract.ProfileTable.TABLE_NAME +
                " WHERE " + ProfileContract.ProfileTable.COLUMN_USERNAME + " = ? AND " +
                ProfileContract.ProfileTable.COLUMN_PASSWORD + " = ?", new String[]{username, password});
        if(c.getCount() > 0) return true;
        else return false;
    }

    public String getProfileUri(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ProfileContract.ProfileTable.TABLE_NAME +
                " WHERE " + ProfileContract.ProfileTable.COLUMN_USERNAME + " = ?", new String[]{username});
        c.moveToFirst();
        return c.getString(c.getColumnIndex(ProfileContract.ProfileTable.COLUMN_PROFILE_PICTURE));
    }

    public void setProfileUri(String username, String uri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ProfileContract.ProfileTable.COLUMN_PROFILE_PICTURE, uri);
        db.update(ProfileContract.ProfileTable.TABLE_NAME, cv, ProfileContract.ProfileTable.COLUMN_USERNAME + " = ?", new String[]{username});
    }

    public String getInfo(String username, int category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ProfileContract.ProfileTable.TABLE_NAME + " WHERE " + ProfileContract.ProfileTable.COLUMN_USERNAME + " = ?", new String[]{username});
        String value = null;
        if(c.getCount() > 0) {
            c.moveToFirst();
            if(category == 1) value = c.getString(c.getColumnIndex(ProfileContract.ProfileTable.COLUMN_PHONE));
            if(category == 2) value = c.getString(c.getColumnIndex(ProfileContract.ProfileTable.COLUMN_EMAIL));
        }
        return value;
    }

    public boolean updatePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ProfileContract.ProfileTable.COLUMN_PASSWORD, password);
        int i = db.update(ProfileContract.ProfileTable.TABLE_NAME, cv, ProfileContract.ProfileTable.COLUMN_USERNAME + " = ?", new String[] {username});
        if(i > 0) return true;
        else return false;
    }

    public boolean updateProfile(String username, String newUsername, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(!newUsername.equals("")) cv.put(ProfileContract.ProfileTable.COLUMN_USERNAME, newUsername);
        if(!phone.equals("")) cv.put(ProfileContract.ProfileTable.COLUMN_PHONE, phone);
        if(!email.equals("")) cv.put(ProfileContract.ProfileTable.COLUMN_EMAIL, email);
        int i = db.update(ProfileContract.ProfileTable.TABLE_NAME, cv, ProfileContract.ProfileTable.COLUMN_USERNAME + " = ?", new String[] {username});
        if(i > 0) return true;
        else return false;
    }

    public void updateFavourite(String username, int hikeID, boolean save) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        int value = save ? 1 : 0;
        if(hikeID == 1) cv.put(FavouriteContract.FavouriteTable.COLUMN_HIKE1, value);
        if(hikeID == 2) cv.put(FavouriteContract.FavouriteTable.COLUMN_HIKE2, value);
        if(hikeID == 3) cv.put(FavouriteContract.FavouriteTable.COLUMN_HIKE3, value);
        db.update(FavouriteContract.FavouriteTable.TABLE_NAME, cv, FavouriteContract.FavouriteTable.COLUMN_USERNAME + " = ?", new String[] {username});
    }

    public boolean getFavourite(String username, int hikeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "";
        if(hikeID == 1) query = "SELECT " + FavouriteContract.FavouriteTable.COLUMN_HIKE1 + " AS hike FROM " + FavouriteContract.FavouriteTable.TABLE_NAME + " WHERE " + FavouriteContract.FavouriteTable.COLUMN_USERNAME + " = ?";
        if(hikeID == 2) query = "SELECT " + FavouriteContract.FavouriteTable.COLUMN_HIKE2 + " AS hike FROM " + FavouriteContract.FavouriteTable.TABLE_NAME + " WHERE " + FavouriteContract.FavouriteTable.COLUMN_USERNAME + " = ?";
        if(hikeID == 3) query = "SELECT " + FavouriteContract.FavouriteTable.COLUMN_HIKE3 + " AS hike FROM " + FavouriteContract.FavouriteTable.TABLE_NAME + " WHERE " + FavouriteContract.FavouriteTable.COLUMN_USERNAME + " = ?";
        Cursor c = db.rawQuery(query, new String[] {username});
        if(c.getCount() > 0) {
            c.moveToFirst();
            int saved = c.getInt(c.getColumnIndex("hike"));
            if(saved == 1) return true;
        }
        return false;
    }

    public void insertHikeTable() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(HikeContract.HikeTable.COLUMN_TITLE, "Bukit Broga Hill");
        cv.put(HikeContract.HikeTable.COLUMN_REGION, "Semenyih, Selangor, Malaysia");
        cv.put(HikeContract.HikeTable.COLUMN_DIFFICULTY, 1);
        cv.put(HikeContract.HikeTable.COLUMN_LENGTH, "3.2");
        cv.put(HikeContract.HikeTable.COLUMN_TIME, "0h 45m");
        cv.put(HikeContract.HikeTable.COLUMN_ELEVATION_GAIN, 247);
        cv.put(HikeContract.HikeTable.COLUMN_ROUTE_TYPE, "Out & Back");
        cv.put(HikeContract.HikeTable.COLUMN_DESCRIPTION, "Bukit Broga is a 3.2 kilometer moderately trafficked out and back trail located near Semenyih, Selangor, Malaysia that features beautiful wild flowers and is rated as moderate. The trail is primarily used for hiking, walking, and nature trips.");
        db.insert(HikeContract.HikeTable.TABLE_NAME, null, cv);
        cv.clear();

        cv.put(HikeContract.HikeTable.COLUMN_TITLE, "Bukit Gasing Loop");
        cv.put(HikeContract.HikeTable.COLUMN_REGION, "Bukit Gasing Forest Park");
        cv.put(HikeContract.HikeTable.COLUMN_DIFFICULTY, 2);
        cv.put(HikeContract.HikeTable.COLUMN_LENGTH, "4.7");
        cv.put(HikeContract.HikeTable.COLUMN_TIME, "2h 30m");
        cv.put(HikeContract.HikeTable.COLUMN_ELEVATION_GAIN, 283);
        cv.put(HikeContract.HikeTable.COLUMN_ROUTE_TYPE, "Loop");
        cv.put(HikeContract.HikeTable.COLUMN_DESCRIPTION, "Bukit Gasing Loop is a 4.7 kilometer heavily trafficked loop trail located near Petaling Jaya, Selangor, Malaysia that features beautiful wild flowers and is rated as moderate. The trail is primarily used for hiking, walking, running, and nature trips.");
        db.insert(HikeContract.HikeTable.TABLE_NAME, null, cv);
        cv.clear();

        cv.put(HikeContract.HikeTable.COLUMN_TITLE, "Bukit Saga Waterfall");
        cv.put(HikeContract.HikeTable.COLUMN_REGION, "Cheras, Kuala Lumpur, Malaysia");
        cv.put(HikeContract.HikeTable.COLUMN_DIFFICULTY, 3);
        cv.put(HikeContract.HikeTable.COLUMN_LENGTH, "3.9");
        cv.put(HikeContract.HikeTable.COLUMN_TIME, "3h 0m");
        cv.put(HikeContract.HikeTable.COLUMN_ELEVATION_GAIN, 402);
        cv.put(HikeContract.HikeTable.COLUMN_ROUTE_TYPE, "Loop");
        cv.put(HikeContract.HikeTable.COLUMN_DESCRIPTION, "Bukit Saga Waterfall is a 3.9 kilometer moderately trafficked loop trail located near Cheras, Kuala Lumpur, Malaysia that features a waterfall and is rated as difficult. The trail is primarily used for hiking and nature trips and is accessible year-round.");
        db.insert(HikeContract.HikeTable.TABLE_NAME, null, cv);
        cv.clear();

        cv.put("boolean", 1);
        db.insert("isHikeInserted", null, cv);
    }

    public Hike getHikeInfo(int id) {
        Hike hike = new Hike();
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        if(id == 1) cursor = db.rawQuery("SELECT * FROM " + HikeContract.HikeTable.TABLE_NAME + " WHERE " + HikeContract.HikeTable._ID + " = 1", null);
        if(id == 2) cursor = db.rawQuery("SELECT * FROM " + HikeContract.HikeTable.TABLE_NAME + " WHERE " + HikeContract.HikeTable._ID + " = 2", null);
        if(id == 3) cursor = db.rawQuery("SELECT * FROM " + HikeContract.HikeTable.TABLE_NAME + " WHERE " + HikeContract.HikeTable._ID + " = 3", null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            hike.set_id(cursor.getInt(cursor.getColumnIndex(HikeContract.HikeTable._ID)));
            hike.setTitle(cursor.getString(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_TITLE)));
            hike.setRegion(cursor.getString(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_REGION)));
            hike.setDifficulty(cursor.getInt(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_DIFFICULTY)));
            hike.setRating(cursor.getDouble(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_RATING)));
            hike.setRatingCount(cursor.getInt(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_RATING_COUNT)));
            hike.setLength(cursor.getString(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_LENGTH)));
            hike.setTime(cursor.getString(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_TIME)));
            hike.setElevationGain(cursor.getInt(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_ELEVATION_GAIN)));
            hike.setRouteType(cursor.getString(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_ROUTE_TYPE)));
            hike.setDescription(cursor.getString(cursor.getColumnIndex(HikeContract.HikeTable.COLUMN_DESCRIPTION)));
        }
        cursor.close();
        return hike;
    }

    public void updateHikeRating(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor c = db.rawQuery("SELECT COUNT(" + RatingContract.RatingTable._ID + ") AS number, AVG(" + RatingContract.RatingTable.COLUMN_RATING + ") as average FROM " + RatingContract.RatingTable.TABLE_NAME
                + " WHERE " + RatingContract.RatingTable.COLUMN_PAGEID + " = " + id, null);
        int ratingCount = 0;
        double rating = 0.0;
        if(c.moveToFirst()) {
            ratingCount = c.getInt(c.getColumnIndex("number"));
            rating = c.getDouble(c.getColumnIndex("average"));
            c.close();
        }
        int precision = 10;
        rating = Math.floor(rating * precision + 0.5)/precision;
        cv.put(HikeContract.HikeTable.COLUMN_RATING_COUNT, ratingCount);
        cv.put(HikeContract.HikeTable.COLUMN_RATING, rating);
        db.update(HikeContract.HikeTable.TABLE_NAME, cv, HikeContract.HikeTable._ID + " = " + id, null);
    }

    public List<Hike> searchHike(String keyword) {
        ArrayList<Hike> searchHikes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + HikeContract.HikeTable.TABLE_NAME + " WHERE " + HikeContract.HikeTable.COLUMN_TITLE + " LIKE ?", new String[]{"%" + keyword + "%"});
        
        if(c.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.set_id(c.getInt(c.getColumnIndex(HikeContract.HikeTable._ID)));
                hike.setTitle(c.getString(c.getColumnIndex(HikeContract.HikeTable.COLUMN_TITLE)));
                hike.setRegion(c.getString(c.getColumnIndex(HikeContract.HikeTable.COLUMN_REGION)));
                hike.setDifficulty(c.getInt(c.getColumnIndex(HikeContract.HikeTable.COLUMN_DIFFICULTY)));
                hike.setRating(c.getDouble(c.getColumnIndex(HikeContract.HikeTable.COLUMN_RATING)));
                hike.setRatingCount(c.getInt(c.getColumnIndex(HikeContract.HikeTable.COLUMN_RATING_COUNT)));
                hike.setLength(c.getString(c.getColumnIndex(HikeContract.HikeTable.COLUMN_LENGTH)));
                hike.setTime(c.getString(c.getColumnIndex(HikeContract.HikeTable.COLUMN_TIME)));
                hike.setElevationGain(c.getInt(c.getColumnIndex(HikeContract.HikeTable.COLUMN_ELEVATION_GAIN)));
                hike.setRouteType(c.getString(c.getColumnIndex(HikeContract.HikeTable.COLUMN_ROUTE_TYPE)));
                hike.setDescription(c.getString(c.getColumnIndex(HikeContract.HikeTable.COLUMN_DESCRIPTION)));

                searchHikes.add(hike);
            } while (c.moveToNext());
        }
        c.close();
        return searchHikes;
    }

    public boolean createReview(String username, String description, double rating, int pageID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RatingContract.RatingTable.COLUMN_USERNAME, username);
        cv.put(RatingContract.RatingTable.COLUMN_RATING, rating);
        cv.put(RatingContract.RatingTable.COLUMN_DESCRIPTION, description);
        cv.put(RatingContract.RatingTable.COLUMN_PAGEID, pageID);

        long isInserted = db.insert(RatingContract.RatingTable.TABLE_NAME, null, cv);
        if(isInserted == -1) return false;
        else return true;
    }

    public boolean deleteReview(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int i = db.delete(RatingContract.RatingTable.TABLE_NAME, RatingContract.RatingTable._ID + " = " + id, null);
        if(i > 0) return true;
        else return false;
    }

    public List<Rating> getAllRating(int pageID) {
        List<Rating> allRating = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + RatingContract.RatingTable.TABLE_NAME + " WHERE " + RatingContract.RatingTable.COLUMN_PAGEID + " = " + pageID, null);
        if(c.moveToFirst()) {
            do {
                Rating rating = new Rating();
                rating.setUsername(c.getString(c.getColumnIndex(RatingContract.RatingTable.COLUMN_USERNAME)));
                rating.setRating(c.getDouble(c.getColumnIndex(RatingContract.RatingTable.COLUMN_RATING)));
                rating.setDescription(c.getString(c.getColumnIndex(RatingContract.RatingTable.COLUMN_DESCRIPTION)));
                rating.setId(c.getInt(c.getColumnIndex(RatingContract.RatingTable._ID)));

                allRating.add(rating);
            }while (c.moveToNext());
        }
        c.close();
        return allRating;
    }

    public boolean getIsHikeInserted() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM isHikeInserted", null);
        if(c.getCount() > 0) return true;
        else return false;
    }
}
