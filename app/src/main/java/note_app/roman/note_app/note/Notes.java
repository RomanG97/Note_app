package note_app.roman.note_app.note;

import java.util.ArrayList;

import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.IdGenerator;

public class Notes {

    public ArrayList<Note> notes;
    static Notes instance;

    public static Notes getInstance() {
        if (instance == null) {
            instance = new Notes();
        }
        return instance;
    }

    private Notes() {
        notes = new ArrayList<>();
        notes.add(new Note(IdGenerator.Generate("1"), "1", "1", "1", Constants.CURRENT,
                System.currentTimeMillis(), System.currentTimeMillis(), "user"));
        notes.add(new Note(IdGenerator.Generate("2"), "2", "2", "2", Constants.CURRENT,
                System.currentTimeMillis(), System.currentTimeMillis(), "user"));
        notes.add(new Note(IdGenerator.Generate("3"), "3", "3", "3", Constants.COMPLETED,
                System.currentTimeMillis(), System.currentTimeMillis(), "user"));
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }
}
