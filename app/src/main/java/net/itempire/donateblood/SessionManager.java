package net.itempire.donateblood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by MIRSAAB on 11/3/2016.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;


    // Editor for Shared preferences
    SharedPreferences.Editor editor;


    // Context
    Context _context;

    // Temp Context
    Context _tempContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "BloodDonation";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // TEMP login status
    private static final String IS_TEMP_LOGIN = "IsLoggedIn";


    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_NAME = "full_name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_AGE = "age";
    public static final String KEY_BLOOD_GROUP = "blood_group";
    public static final String KEY_CONTACT_NUMBER = "contact_no";
    public static final String KEY_CITY = "city";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_THANKS = "thanks";
    public static final String KEY_LAST_DONATION = "last_donation_data";
    public static final String KEY_REG_DATE = "reg_date";
    private String username;


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


    }

//    public void setUserName(String user){
//        editor.putString("USER",user);
//        editor.commit();
//    }
//    public String getUserName(){
//        String user=pref.getString("USER",null);
//        return user;
//    }

    /**
     * Create Temp login to view application
     */
    public void createTempLoginSession(String status){
        // Storing login value as TRUE
        editor.putBoolean(IS_TEMP_LOGIN, true);
        editor.putBoolean(IS_LOGIN, false);

        // commit changes
        editor.commit();
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String user_id, String name, String gender, String age,
                                   String bloodGroup, String phoneNumber, String city, String email,
                                   String password, String thanks, String last_donation_data, String reg_date){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_TEMP_LOGIN, true);

        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_BLOOD_GROUP, bloodGroup);
        editor.putString(KEY_CONTACT_NUMBER, phoneNumber);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_THANKS, thanks);
        editor.putString(KEY_LAST_DONATION, last_donation_data);
        editor.putString(KEY_REG_DATE, reg_date);


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Log.v("URL", "User Is not logged in");
            Toast.makeText(_context, "Please login First!", Toast.LENGTH_SHORT).show();
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(this._context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            this._context.startActivity(i);
            //return false;
        }else {
            Log.v("URL", "User Is logged in");
            //return true;
        }

    }

    public boolean checkLoginreturn(){
        // Check login status
        if(!this.isLoggedIn()){
            Log.v("URL", "User Is not logged in");
            return false;
        }else {
            Log.v("URL", "User Is logged in");
            return true;
        }

    }

    /**
     * Check TEMP login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkTempLogin(){
        // Check login status

        if(!this.isTempLoggedIn() && !this.isLoggedIn()){
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
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_AGE, pref.getString(KEY_AGE, null));
        user.put(KEY_BLOOD_GROUP, pref.getString(KEY_BLOOD_GROUP, null));
        user.put(KEY_CONTACT_NUMBER, pref.getString(KEY_CONTACT_NUMBER, null));
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_THANKS, pref.getString(KEY_THANKS, null));
        user.put(KEY_LAST_DONATION, pref.getString(KEY_LAST_DONATION, null));
        user.put(KEY_REG_DATE, pref.getString(KEY_REG_DATE, null));

        // return user
        return user;
    }

//    public void disconnectFromFacebook() {
//
//        if (AccessToken.getCurrentAccessToken() == null) {
//            return; // already logged out
//        }
//
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                .Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//
//                LoginManager.getInstance().logOut();
//
//            }
//        }).executeAsync();
//    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
//        FacebookSdk.sdkInitialize(_context);
//        LoginManager.getInstance().logOut();
//        disconnectFromFacebook();

        android.webkit.CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                // a callback which is executed when the cookies have been removed
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("REMOVED COOKIE:", "Cookie removed: " + aBoolean);
                }
            });
        }
        else cookieManager.removeAllCookie();

        Log.e("@@@@@@@@@@@", "Log out......");
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        // Staring Login Activity
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
        //return true;
    }


    public boolean isTempLoggedIn(){

        return pref.getBoolean(IS_TEMP_LOGIN, false);
    }
}