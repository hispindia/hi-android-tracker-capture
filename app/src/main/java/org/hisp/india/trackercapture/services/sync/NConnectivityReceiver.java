package org.hisp.india.trackercapture.services.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nhancv.ntask.NTaskManager;

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
                    NTaskManager.restart();
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
