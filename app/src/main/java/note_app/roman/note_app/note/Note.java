package note_app.roman.note_app.note;

import io.realm.RealmObject;

public class Note extends RealmObject {

    private String id;
    private String title;
    private String description;
    private String type;
    private String status;
    private long date;
    private long curDate;
    private String login;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public long getDate() {
        return date;
    }

    public long getCurDate() {
        return curDate;
    }

    public String getLogin() {
        return login;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setCurDate(long curDate) {
        this.curDate = curDate;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Note() {
    }

    public Note(String id, String title, String description, String type, String status, long date, long curDate, String login) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.date = date;
        this.curDate = curDate;
        this.login = login;
    }

}
