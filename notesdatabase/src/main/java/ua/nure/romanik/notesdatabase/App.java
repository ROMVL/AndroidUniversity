package ua.nure.romanik.notesdatabase;

import android.app.Application;

import androidx.room.Room;

import ua.nure.romanik.notesdatabase.data.database.NoteDatabase;
import ua.nure.romanik.notesdatabase.data.repository.NoteRepository;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public class App extends Application {

    private static NoteDatabase noteDatabase;
    private static INoteRepository noteRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        noteDatabase = Room
                .databaseBuilder(this, NoteDatabase.class, "notedatabase")
                .build();
        noteRepository = new NoteRepository(noteDatabase.noteDao());
    }

    public static NoteDatabase getNoteDatabase() { return noteDatabase; }

    public static INoteRepository getNoteRepository() { return noteRepository; }

}