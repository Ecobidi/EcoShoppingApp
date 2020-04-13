package com.example.obidi.eco_shopping_mall.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserSession {
    private static final String COOKIE_KEY = "cookie";
    private static final String IS_LOGGED_IN = "loginStatus";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String USER_ID = "_id";
    public static final String CART_QUANTITY = "cart quantity";

    private static UserSession sUserSession;

    private SharedPreferences mSharedPreferences;

    // Singleton implementation
    public static UserSession getInstance(Context context) {
        if (sUserSession == null) {
            sUserSession = new UserSession(context);
        }
        return sUserSession;
    }

    public UserSession(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setCookie(String value) {
        mSharedPreferences.edit()
                .putString(COOKIE_KEY, value)
                .apply();
    }

    public String getCookie() {
        return mSharedPreferences
                .getString(COOKIE_KEY, null);
    }

    public boolean getLoginStatus() {
        return mSharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoginStatus(boolean loginStatus) {
        mSharedPreferences.edit()
                .putBoolean(IS_LOGGED_IN, loginStatus)
                .apply();
    }

    public String getUsername() {
        return mSharedPreferences.getString(USERNAME_KEY, null);
    }

    public void setUsername(String username) {
        mSharedPreferences.edit()
                .putString(USERNAME_KEY, username)
                .apply();
    }

    public String getPassword() {
        return mSharedPreferences.getString(PASSWORD_KEY, null);
    }

    public void setPassword(String password) {
        mSharedPreferences.edit()
                .putString(PASSWORD_KEY, password)
                .apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(USER_ID, null);
    }

    public void setUserId(String id) {
        mSharedPreferences.edit()
                .putString(USER_ID, id)
                .apply();
    }

    public int getCartQuantity() { return mSharedPreferences.getInt(CART_QUANTITY, 0); }

    public void setCartQuantity(int cartQuantity) {
        mSharedPreferences.edit()
                .putInt(CART_QUANTITY, cartQuantity)
                .apply();
    }

    public void clear() {
        mSharedPreferences.edit()
                .remove(COOKIE_KEY)
                .remove(IS_LOGGED_IN)
                .remove(USERNAME_KEY)
                .remove(PASSWORD_KEY)
                .apply();
    }
}
