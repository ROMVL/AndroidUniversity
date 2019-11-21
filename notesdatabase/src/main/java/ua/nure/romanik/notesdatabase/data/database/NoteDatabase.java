package ua.nure.romanik.notesdatabase.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ua.nure.romanik.notesdatabase.data.database.dao.NoteDao;
import ua.nure.romanikvladislav.common.notes.data.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notedatabase";

    public abstract NoteDao noteDao();

}
