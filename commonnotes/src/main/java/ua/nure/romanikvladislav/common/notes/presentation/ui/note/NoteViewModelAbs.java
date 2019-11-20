package ua.nure.romanikvladislav.common.notes.presentation.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;

public abstract class NoteViewModelAbs extends ViewModel {

    private INoteRepository noteRepository;
    private LiveData<Note> note;
    private MutableLiveData<Boolean> backEvent = new MutableLiveData<>();

    public NoteViewModelAbs(INoteRepository noteRepository, int noteId) {
        this.noteRepository = noteRepository;
        initData(noteId);
    }

    private void initData(int noteId) {
        if (noteId == -1) {
            MutableLiveData<Note> liveDataNote = new MutableLiveData<>();
            liveDataNote.setValue(Note.emptyNote());
            note = liveDataNote;
        } else {
            note = noteRepository.fetchNoteById(noteId);
        }
    }

    public LiveData<Note> getNote() {
        return note;
    }

    LiveData<Boolean> getBackEvent() { return backEvent; }

    public void saveNote(Note currentNote) {
        if (currentNote == null) currentNote = Note.emptyNote();
        if (currentNote.getId() == -1 && currentNote.getRowId() == 0) {
            noteRepository.saveNote(currentNote);
        } else {
            noteRepository.editNote(currentNote);
        }
        backEvent.setValue(true);
    }

    void setImage(String imageUri) {
        Note currentNote = note.getValue();
        if (currentNote != null) {
            currentNote.setImagePath(imageUri);
        }
    }
}
