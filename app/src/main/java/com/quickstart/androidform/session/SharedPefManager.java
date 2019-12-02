package com.quickstart.androidform.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPefManager {

    private static final String PREFERENCES_NAME = "shared_preferences";

    private static Context mContext;
    private static SharedPefManager mManager;

    public static synchronized SharedPefManager getInstance(Context context) {
        if (mManager == null) {
            mContext = context;
            mManager = new SharedPefManager();
        }
        return mManager;
    }

    public void saveToken(String token) {
        SharedPreferences pre = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("token", token);
        editor.apply();
        editor.commit();
    }

    public String getToken() {
        SharedPreferences pre = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pre.getString("token", null);
    }
}
