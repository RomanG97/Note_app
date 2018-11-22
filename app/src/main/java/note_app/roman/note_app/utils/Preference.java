package note_app.roman.note_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static final String PREFS_NAME = "preference";

    private static final String TOOLBAR_COLOR = "color";
    private static final String INFO = "info";
    private static final String USER = "user";
    private static final String SENSOR = "sensor";

    public static int getColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(TOOLBAR_COLOR, Constants.BLUE);
    }

    public static void setColor(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(TOOLBAR_COLOR, color).apply();
    }

    public static String getInfo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(INFO, "V 0.0");
    }

    public static void setInfo(Context context, String info) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(INFO, info).apply();
    }

    public static String getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USER, "Empty User");
    }

    public static void setUser(Context context, String user) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(USER, user).apply();
    }

    public static boolean getSensor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(SENSOR, false);
    }

    public static void setSensor(Context context, boolean sensor) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(SENSOR, sensor).apply();
    }


}
