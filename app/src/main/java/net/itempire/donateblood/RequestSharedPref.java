package net.itempire.donateblood;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIRSAAB on 11/29/2016.
 * This class will be used to store "Blood Donation Requests" into SharedPreferences.
 */

public class RequestSharedPref {
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
    private static final String PREF_NAME = "BloodDonationRequests";

    public static final String IS_NULL = "is_null";
    public static final String KEY_REQUEST_ID = "request_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_BLOOD_GROUP = "blood_group";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_DATE_TIME = "date_time";
    public static final String KEY_CITY = "city";

    // Constructor
    public RequestSharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


    }

    /**
     * Create login session
     * */
    public void storeRequest(String request_id, String user_id, String blood_group, String status, String detail,
                                   String date_time, String city){
        editor.putBoolean(IS_NULL, false);
        editor.putString(KEY_REQUEST_ID, request_id);
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_BLOOD_GROUP, blood_group);
        editor.putString(KEY_STATUS, status);
        editor.putString(KEY_DETAIL, detail);
        editor.putString(KEY_DATE_TIME, date_time);
        editor.putString(KEY_CITY, city);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getRequests(){
        HashMap<String, String> request = new HashMap<String, String>();
        // user name
        request.put(KEY_REQUEST_ID, pref.getString(KEY_REQUEST_ID, null));
        request.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        request.put(KEY_BLOOD_GROUP, pref.getString(KEY_BLOOD_GROUP, null));
        request.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        request.put(KEY_DETAIL, pref.getString(KEY_DETAIL, null));
        request.put(KEY_DATE_TIME, pref.getString(KEY_DATE_TIME, null));
        request.put(KEY_CITY, pref.getString(KEY_CITY, null));

        // return user
        return request;
    }

    public boolean isNull(){
        return pref.getBoolean(IS_NULL,true);
    }

    public Map<String, ?> getAll(){
        return pref.getAll();
    }
}
