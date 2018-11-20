package note_app.roman.note_app.utils.realm;

import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import note_app.roman.note_app.note.Note;
import note_app.roman.note_app.user.User;

import static note_app.roman.note_app.utils.realm.MigrationManager.Constants.NOTE;
import static note_app.roman.note_app.utils.realm.MigrationManager.Constants.USER;

public class MigrationManager implements RealmMigration {

    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        RealmObjectSchema note = schema.get(NOTE);
        RealmObjectSchema user = schema.get(USER);
        switch ((int) oldVersion) {
            case CustomRealmConfiguration.VERSION_1:
                note = upgradeMessageInfoNote(schema, note);
                user = upgradeMessageInfoUser(schema, user);
        }

    }

    private RealmObjectSchema upgradeMessageInfoNote(RealmSchema schema, RealmObjectSchema note) {
        if (note == null) {
            note = schema.create(NOTE)
                    .addField("id", Long.class)
                    .addField("title", String.class)
                    .addField("description", String.class)
                    .addField("type", String.class)
                    .addField("status", String.class)
                    .addField("date", Long.class)
                    .addField("curDate", Long.class);
        } else {
            if (!note.hasField("id")) {
                note = note.addField("id", Long.class);
            }
            if (!note.hasField("title")) {
                note = note.addField("title", String.class);
            }
            if (!note.hasField("description")) {
                note = note.addField("description", String.class);
            }
            if (!note.hasField("type")) {
                note = note.addField("type", String.class);
            }
            if (!note.hasField("status")) {
                note = note.addField("status", String.class);
            }
            if (!note.hasField("date")) {
                note = note.addField("date", Long.class);
            }
            if (!note.hasField("curDate")) {
                note = note.addField("curDate", Long.class);
            }
            if (!note.hasField("user")) {
                note = note.addField("user", String.class);
            }

        }
        return note;
    }

    private RealmObjectSchema upgradeMessageInfoUser(RealmSchema schema, RealmObjectSchema user) {
        if (user == null) {
            user = schema.create(USER)
                    .addField("login", String.class)
                    .addField("password", String.class);
        } else {
            if (!user.hasField("login")) {
                user = user.addField("login", String.class);
            }
            if (!user.hasField("password")) {
                user = user.addField("password", String.class);
            }
        }
        return user;
    }

    static class Constants {
        static final String NOTE = Note.class.getSimpleName();
        static final String USER = User.class.getSimpleName();
    }
}
