package ua.nure.romanikvladislav.common.notes.presentation.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public abstract class NoteViewModel extends ViewModel {

    private INoteRepository noteRepository;
    private LiveData<Note> noteLiveData;

    public NoteViewModel(INoteRepository noteRepository, int noteId) {
        this.noteRepository = noteRepository;
        initData(noteId);
    }

    private void initData(int noteId) {
        noteLiveData = noteRepository.fetchNoteById(noteId);
    }

    public LiveData<Note> getNoteLiveData() {
        return noteLiveData;
    }

    public void saveNote(Note note) {
        noteRepository.saveNote(note);
    }

}
