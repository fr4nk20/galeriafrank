package cr.ac.unadeca.galeriafrank.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import cr.ac.unadeca.galeriafrank.activities.LoginActivity;
import cr.ac.unadeca.galeriafrank.activities.MainActivity;

/**
 * Created by pc on 26/3/2018.
 */

public class Session {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "GaleriaPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USERINFO = "user_info";

    // User name (make variable public to access from outside)
    public static final String KEY_USER_ID = "id";

    // Email address (make variable public to access from outside)
    public static final String KEY_USER_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_USER_ROLE = "role";

    // Constructor
    public Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(long id, String name, String role){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing id
        editor.putLong(KEY_USER_ID, id);

        // Storing name in pref
        editor.putString(KEY_USER_NAME, name);
        //storing role in pref
        editor.putString(KEY_USER_ROLE, role);

        // commit changes
        editor.commit();
    }

    /**
     * Check not login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checknotLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }



    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to Main page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }



    }



    /**
     * Get stored session data
     * */
    public HashMap<String, cr.ac.unadeca.galeriafrank.subclases.UserInfo> getUserDetails(){
        HashMap<String, cr.ac.unadeca.galeriafrank.subclases.UserInfo> user = new HashMap<String, cr.ac.unadeca.galeriafrank.subclases.UserInfo>();
        // user name
        cr.ac.unadeca.galeriafrank.subclases.UserInfo useri = new cr.ac.unadeca.galeriafrank.subclases.UserInfo();

        useri.userId = pref.getLong(KEY_USER_ID, Long.valueOf("0"));
        useri.userName= pref.getString(KEY_USER_NAME,null);
        useri.userRole= pref.getString(KEY_USER_ROLE,null);

        user.put(KEY_USERINFO, useri);

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}