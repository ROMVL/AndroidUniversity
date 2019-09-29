package un.nure.romanik.notes;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;
import un.nure.romanik.notes.data.repository.NoteRepository;

public class App extends Application {

    private static final List<Note> noteList = new ArrayList<>();
    private static final MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
    private static final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();
    private static final INoteRepository repository = new NoteRepository();

    @Override
    public void onCreate() {
        super.onCreate();
        noteList.add(new Note("Title", "description", 1, ""));
    }

    public static LiveData<Note> getNoteLiveData(int id) {
        for (int i = 0; i < noteList.size(); i++) {
            if (id == noteList.get(i).getId()) {
                noteLiveData.setValue(noteList.get(i));
                return noteLiveData;
            }
        }
        noteLiveData.setValue(null);
        return noteLiveData;
    }

    public static LiveData<List<Note>> getNotesLiveData() {
        notesLiveData.setValue(noteList);
        return notesLiveData;
    }

    public static void saveNote(Note note) {
        note.setId(noteList.size());
        noteList.add(note);
    }

    public static INoteRepository getNoteRepository() {
        return repository;
    }
}
