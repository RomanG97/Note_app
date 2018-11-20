package note_app.roman.note_app.utils;

public class IdGenerator {

    public static String Generate(String titleOfNote) {
        return System.currentTimeMillis() + titleOfNote;
    }
}
