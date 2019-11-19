package ua.nure.romanikvladislav.common.notes.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "note")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;
    private long dateCreated;
    private String imagePath;

    public Note() { }

    @Ignore
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateCreated = new Date().getTime();
        //TODO insert image path
        this.imagePath = "";
    }

    @Ignore
    public Note(String title, String description, int priority, @NonNull String imagePath) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateCreated = new Date().getTime();
        this.imagePath = imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateCreated() { return dateCreated; }

    public Date getDateCreatedF() {
        return new Date(dateCreated);
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated.getTime();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFormattedDate() {
        String pattern = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public String getFormattedPriority() {
        return "Priority: " + priority;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", dateCreated=" + dateCreated +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    public static Note emptyNote() {
        Note emptyNote = new Note("", "", 0, "");
        emptyNote.setId(-1);
        return emptyNote;
    }
}
