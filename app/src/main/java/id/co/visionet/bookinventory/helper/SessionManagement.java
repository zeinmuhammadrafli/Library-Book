package id.co.visionet.bookinventory.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManagement {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REMEMBER_ME = "rememberMe";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("bookscatalog", PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setActiveInformation(String email, String password, boolean rememberMe) {
        // Storing name in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        // commit changes
        editor.commit();
    }

    /* Get stored session data */
    public HashMap<String, String> getActiveInformation() {
        HashMap<String, String> activeUser = new HashMap<String, String>();
        // user id
        activeUser.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        activeUser.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, ""));
        return activeUser;
    }

    public boolean getRememberMe() {
        return pref.getBoolean(KEY_REMEMBER_ME, false);
    }

    public boolean getKeyIsLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setKeyIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }
}
