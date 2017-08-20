package org.hisp.india.trackercapture;

import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nhancv.realmbowser.NRealmDiscovery;
import com.nhancv.realmbowser.NRealmServer;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.splunk.mint.Mint;

import net.danlew.android.joda.JodaTimeAndroid;

import org.androidannotations.annotations.EApplication;
import org.hisp.india.trackercapture.di.ApplicationComponent;
import org.hisp.india.trackercapture.di.ApplicationModule;
import org.hisp.india.trackercapture.di.DaggerApplicationComponent;
import org.hisp.india.trackercapture.models.RMigration;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by nhancao on 4/4/17.
 */

@EApplication
public class MainApplication extends MultiDexApplication {
    private static final String TAG = MainApplication.class.getSimpleName();

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Hawk.init(this)
            .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
            .setStorage(HawkBuilder.newSharedPrefStorage(this)).build();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                              .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
                                              .setFontAttrId(R.attr.fontPath)
                                              .build()
                                     );
        Mint.disableNetworkMonitoring();
        Mint.initAndStartSession(this, "d80c7dbf");

        resetApplicationComponent();

        //The Realm file will be located in Context.getFilesDir() with name "default.realm"
        initRealmConfig();
    }

    @Override
    public void onTerminate() {
        if (NRealmServer.isStart()) {
            NRealmServer.stop();
        }
        super.onTerminate();
    }

    public boolean initRealmConfig() {
        String dbDir = Environment.getExternalStorageDirectory() + "/" + getPackageName();
        File folder = new File(dbDir);
        if (!folder.exists()) {
            folder.mkdir();
        }
        boolean success = false;
        if (folder.canWrite()) {
            Realm.init(this);
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .directory(folder)
                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(RMigration.DB_VERSION)
                    .migration(new RMigration())
                    .build();
            Realm.setDefaultConfiguration(config);

            //Start realm browser
            //startRealmBrowserServer(config);

            success = true;
        }
        return success;
    }

    private void startRealmBrowserServer(RealmConfiguration config) {
        NRealmServer.init(new NRealmDiscovery(this, config));
        NRealmServer.start();
        String address = NRealmServer.getServerAddress(this);
        Log.e(TAG, "Server address: " + address);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public void resetApplicationComponent() {
        setApplicationComponent(getApplicationModuleInstance());
    }

    public ApplicationComponent getApplicationModuleInstance() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
