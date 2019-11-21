package ua.nure.romanik.notesdatabase;

import android.app.Application;

import androidx.room.Room;

import ua.nure.romanik.notesdatabase.data.database.NoteDatabase;
import ua.nure.romanik.notesdatabase.data.repository.NoteRepository;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public class App extends Application {

    private static INoteRepository noteRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        NoteDatabase noteDatabase = Room
                .databaseBuilder(this, NoteDatabase.class, NoteDatabase.DATABASE_NAME)
                .build();
        noteRepository = new NoteRepository(noteDatabase.noteDao());
    }

    public static INoteRepository getNoteRepository() { return noteRepository; }

}
