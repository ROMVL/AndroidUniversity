package ua.nure.romanikvladislav.common.notes.presentation.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public abstract class MainViewModelAbs extends ViewModel {

    protected static final String TAG = MainViewModelAbs.class.getSimpleName();

    private INoteRepository noteRepository;
    private LiveData<List<Note>> notes;
    private MediatorLiveData<List<Note>> filteredNotes;

    public MainViewModelAbs(INoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        initNotes();
    }

    public void initNotes() {
        notes = noteRepository.fetchAllNotes();
        Log.d(TAG, notes.getValue().toString());
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public MediatorLiveData<List<Note>> getFilteredNotes() {
        return filteredNotes;
    }
}
