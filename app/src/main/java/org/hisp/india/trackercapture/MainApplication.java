package org.hisp.india.trackercapture;

import android.support.multidex.MultiDexApplication;

import com.logentries.logger.AndroidLogger;

import java.io.IOException;

/**
 * Created by nhancao on 4/4/17.
 */

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            AndroidLogger.createInstance(getApplicationContext(),
                    false, true, false, null, 0, "92565dca-71d0-4a1d-9b53-ec0696eda359", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
