package ua.nure.romanikvladislav.common.notes.domain.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;

public interface INoteRepository {

    void saveNote(Note note);

    LiveData<List<Note>> fetchAllNotes();

    void deleteNote(Note note);

    void editNote(Note note);
}
