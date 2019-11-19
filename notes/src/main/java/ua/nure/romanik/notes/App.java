package ua.nure.romanik.notes;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ua.nure.romanik.notes.data.repository.NotesSharedPrefRepository;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;
import ua.nure.romanik.notes.data.repository.NoteRepository;

public class App extends Application {

    private static final String DEFAULT_SHARED_PREFERENCES = "DEFAULT_SHARED_PREFERENCES";

    private static List<Note> noteList = new ArrayList<>();
    private static final MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
    private static final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();
    private static final INoteRepository repository = new NoteRepository();
    private static NotesSharedPrefRepository notesSharedPrefRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        notesSharedPrefRepository = new NotesSharedPrefRepository(
                getSharedPreferences(DEFAULT_SHARED_PREFERENCES, MODE_PRIVATE)
        );
        noteList = new ArrayList<>(notesSharedPrefRepository.getNotes());
    }

    public static LiveData<Note> getNoteLiveData(int id) {
        for (int i = 0; i < noteList.size(); i++) {
            if (id == noteList.get(i).getId()) {
                noteLiveData.setValue(noteList.get(i));
                return noteLiveData;
            }
        }
        noteLiveData.setValue(Note.emptyNote());
        return noteLiveData;
    }

    public static LiveData<List<Note>> getNotesLiveData() {
        notesLiveData.setValue(noteList);
        return notesLiveData;
    }

    public static void saveNote(Note note) {
        note.setId(noteList.size());
        noteList.add(note);
        notesSharedPrefRepository.saveNotes(noteList);
        notifyNotes();
    }

    public static void editNote(Note note) {
        for (int i = 0; i < noteList.size(); i++) {
            if (note.getId() == noteList.get(i).getId()) {
                noteList.remove(i);
                noteList.add(i, note);
                notifyNotes();
                break;
            }
        }
        notesSharedPrefRepository.saveNotes(noteList);
    }

    public static void deleteNote(Note note) {
        for (int i = 0; i < noteList.size(); i++) {
            if (note.getId() == noteList.get(i).getId()) {
                noteList.remove(i);
                notifyNotes();
                break;
            }
        }
        notesSharedPrefRepository.saveNotes(noteList);
    }

    public static void notifyNotes() {
        notesLiveData.setValue(noteList);
    }

    public static INoteRepository getNoteRepository() {
        return repository;
    }
}
