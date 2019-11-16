package ua.nure.romanik.notes.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;
import ua.nure.romanik.notes.App;

public class NoteRepository implements INoteRepository {
    @Override
    public void saveNote(Note note) {

    }

    @Override
    public LiveData<List<Note>> fetchAllNotes() {
        return App.getNotesLiveData();
    }

    @Override
    public void deleteNote(Note note) {

    }

    @Override
    public void editNote(Note note) {

    }

    @Override
    public LiveData<Note> fetchNoteById(int id) {
        return App.getNoteLiveData(id);
    }

    @Override
    public void notifyNotes() {
        App.notifyNotes();
    }


}
