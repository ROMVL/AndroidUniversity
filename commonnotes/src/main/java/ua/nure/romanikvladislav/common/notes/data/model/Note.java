package ua.nure.romanikvladislav.common.notes.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "note")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;
    private long dateCreated;
    private String imagePath;

    public Note(String title, String description, int priority, String imagePath) {
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

    public Date getDateCreated() {
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
}
