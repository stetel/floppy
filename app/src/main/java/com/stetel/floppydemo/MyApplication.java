package com.stetel.floppydemo;

import android.app.Application;

import com.stetel.floppy.Floppy;
import com.stetel.floppy.Loader;

public class MyApplication extends Application {
    private static final int FLOPPY_DRIVE_VERSION = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        Floppy.driveUpgrade(this, FLOPPY_DRIVE_VERSION, new Loader() {
            @Override
            public void onUpgrade(Floppy floppy, int previousVersion, int currentVersion) {
                if (previousVersion < 1) {
                    floppy.format();
                }
                if (previousVersion < 2) {
                    boolean setupBool = floppy.readBoolean("setup");
                    floppy.write("setup", setupBool ? "account" : "none");
                }
            }
        });
    }
}
