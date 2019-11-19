package ua.nure.romanik.notesdatabase.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ua.nure.romanik.notesdatabase.data.database.dao.NoteDao;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public class NoteRepository implements INoteRepository {

    private NoteDao noteDao;

    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public void saveNote(Note note) {
        new Thread(() -> noteDao.saveNote(note)).start();
    }

    @Override
    public LiveData<List<Note>> fetchAllNotes() {
        return noteDao.getAllNotes();
    }

    @Override
    public void deleteNote(Note note) {
        new Thread(() -> noteDao.deleteNote(note));
    }

    @Override
    public void editNote(Note note) {
        new Thread(() -> noteDao.updateNote(note));
    }

    @Override
    public LiveData<Note> fetchNoteById(int id) {
        return noteDao.getNoteById(id);
    }

    @Override
    public void notifyNotes() {

    }
}
