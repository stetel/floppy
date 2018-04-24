package com.stetel.floppy;

/**
 * Callback useful to perform addition operations, based on the version.<br/>
 * Used with Floppy.driveUpgrade()
 */
public interface Loader {
    /**
     * Called if and only if the previous version is different to the current one.<br/>
     * It allows to adjust the variables structure based on the version information.
     *
     * @param floppy Instance of floppy
     * @param previousVersion previous version of Floppy
     * @param currentVersion Current version of Floppy passed via Floppy.driveUpgrade()
     */
    void onUpgrade(Floppy floppy, int previousVersion, int currentVersion);
}
