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

    public LiveData<Boolean> getBackEvent() { return backEvent; }

    public void saveNote(int priority, String name, String description) {
        Note currentNote = note.getValue();
        if (currentNote == null) currentNote = Note.emptyNote();
        currentNote.setPriority(priority);
        currentNote.setTitle(name);
        currentNote.setDescription(description);
        if (currentNote.getId() == -1 && currentNote.getRowId() == 0) {
            noteRepository.saveNote(currentNote);
        } else {
            noteRepository.editNote(currentNote);
        }
        backEvent.setValue(true);
    }

    void setImage(String imageUri) {
        note.getValue().setImagePath(imageUri);
    }
}
