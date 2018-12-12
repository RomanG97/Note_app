package note_app.roman.note_app.interfaces;

import java.io.File;

public interface DialogStateListener {
    void setState(boolean isOpened);

    void setPhotoFile(File photoFile);
}
