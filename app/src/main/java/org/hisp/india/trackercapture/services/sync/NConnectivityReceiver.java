package org.hisp.india.trackercapture.services.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by nhancao on 7/13/17.
 */

public class NConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                if (isConnected(context)) {
                    //@nhancv TODO: 8/20/17 do something when network connected



                }
                break;
        }
    }

    private boolean isConnected(Context appContext) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
               connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
