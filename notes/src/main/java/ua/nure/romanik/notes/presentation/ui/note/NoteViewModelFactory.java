package ua.nure.romanik.notes.presentation.ui.note;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public class NoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final INoteRepository repository;
    private final int noteId;

    public NoteViewModelFactory(INoteRepository repository, int noteId) {
        super();
        this.repository = repository;
        this.noteId = noteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NoteViewModel.class) {
            return (T) new NoteViewModel(repository, noteId);
        }
        return null;
    }

}
