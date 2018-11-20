package note_app.roman.note_app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import note_app.roman.note_app.utils.realm.CustomRealmConfiguration;

public class NoteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = CustomRealmConfiguration.getRealmConfiguration();
        Realm.setDefaultConfiguration(realmConfig);
    }
}