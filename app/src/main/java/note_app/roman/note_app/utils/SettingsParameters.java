package note_app.roman.note_app.utils;

public class SettingsParameters {

    private String info = "V 0.3";
    private int color = 2;

    static SettingsParameters instance;

    public static SettingsParameters getInstance() {
        if (instance == null) {
            instance = new SettingsParameters();
        }
        return instance;
    }

    public String getInfo() {
        return info;
    }

    public int getColor() {
        return color;
    }

    public void setParam(int color, String info) {
        this.color = color;
        this.info = info;
    }

}
