package com.stetel.preferencesdemo;

import android.app.Application;
import android.content.Context;

import com.stetel.preferences.Preferences;

/**
 * Created by lorenzo on 14/02/18.
 */

public class MyApplication extends Application {
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        this.preferences = new Preferences(getSharedPreferences(BuildConfig.APPLICATION_ID, 0));
    }

    public static Preferences getPreferences(Context context) {
        return ((MyApplication)context.getApplicationContext()).preferences;
    }
}
