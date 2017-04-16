package org.hisp.india.trackercapture;

import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.splunk.mint.Mint;

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

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(this)
            .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
            .setStorage(HawkBuilder.newSharedPrefStorage(this)).build();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                              .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
                                              .setFontAttrId(R.attr.fontPath)
                                              .build()
                                     );
        Mint.initAndStartSession(this, "d80c7dbf");

        setApplicationComponent(DaggerApplicationComponent
                                        .builder()
                                        .applicationModule(new ApplicationModule(this))
                                        .build());

        //The Realm file will be located in Context.getFilesDir() with name "default.realm"
        initRealmConfig();

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
            success = true;
        }
        return success;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

}
