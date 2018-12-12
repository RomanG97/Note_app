package note_app.roman.note_app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import note_app.roman.note_app.utils.realm.CustomRealmConfiguration;

public class NoteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = CustomRealmConfiguration.getRealmConfiguration();
        Realm.setDefaultConfiguration(realmConfig);
    }
}