package com.example.a2thepeak;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;
    
    private static final String IS_LOGIN = "isLoggedIn";
    
    public static final String KEY_USERNAME = "username";
    public static final String KEY_HIKE1 = "hike1";
    public static final String KEY_HIKE2 = "hike2";
    public static final String KEY_HIKE3 = "hike3";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String username, boolean hike1, boolean hike2, boolean hike3, String phone, String email) {
        int iHike1 = hike1 ? 1 : 0;
        int iHike2 = hike2 ? 1 : 0;
        int iHike3 = hike3 ? 1 : 0;

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_HIKE1, String.valueOf(iHike1));
        editor.putString(KEY_HIKE2, String.valueOf(iHike2));
        editor.putString(KEY_HIKE3, String.valueOf(iHike3));
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    public HashMap<String, String> getUserDataFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_HIKE1, usersSession.getString(KEY_HIKE1, null));
        userData.put(KEY_HIKE2, usersSession.getString(KEY_HIKE2, null));
        userData.put(KEY_HIKE3, usersSession.getString(KEY_HIKE3, null));
        userData.put(KEY_PHONE, usersSession.getString(KEY_PHONE, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));

        return userData;
    }

    public void setKeyUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void setKeyHike1(int saved) {
        editor.putString(KEY_HIKE1, String.valueOf(saved));
        editor.commit();
    }

    public void setKeyHike2(int saved) {
        editor.putString(KEY_HIKE2, String.valueOf(saved));
        editor.commit();
    }

    public void setKeyHike3(int saved) {
        editor.putString(KEY_HIKE3, String.valueOf(saved));
        editor.commit();
    }

    public void setKeyPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }

    public void setKeyEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public boolean checkLogin() {
        if(usersSession.getBoolean(IS_LOGIN, true)) return true;
        else return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
