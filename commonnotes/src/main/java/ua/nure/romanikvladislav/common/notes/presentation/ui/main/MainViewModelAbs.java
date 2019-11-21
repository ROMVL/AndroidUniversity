package ua.nure.romanikvladislav.common.notes.presentation.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public abstract class MainViewModelAbs extends ViewModel {

    protected static final String TAG = MainViewModelAbs.class.getSimpleName();

    private INoteRepository noteRepository;

    public MainViewModelAbs(INoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<List<Note>> getNotes() {
        return noteRepository.fetchAllNotes();
    }

    void removeNote(Note note) {
        noteRepository.deleteNote(note);
    }
}
