package ua.nure.romanik.notes.data.repository;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;

public class NotesSharedPrefRepository {

    private static final String NOTES_DATA_PREF = "notes_data_pref";

    private SharedPreferences preferences;

    public NotesSharedPrefRepository(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public List<Note> getNotes() {
        String notesString = preferences.getString(NOTES_DATA_PREF, "");
        if (notesString.isEmpty()) return new ArrayList<>();
        List<Note> notes = Arrays.asList(new Gson().fromJson(notesString, Note[].class));
        return notes;
    }

    public void saveNotes(List<Note> notes) {
        String notesJson = new Gson().toJson(notes);
        preferences.edit().putString(NOTES_DATA_PREF, notesJson).apply();
    }

}
