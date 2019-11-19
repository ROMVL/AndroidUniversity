package ua.nure.romanik.notesdatabase.presentation.ui.note;

import ua.nure.romanikvladislav.common.notes.domain.repository.INoteRepository;
import ua.nure.romanikvladislav.common.notes.presentation.ui.note.NoteViewModelAbs;

public class NoteViewModel extends NoteViewModelAbs {
    public NoteViewModel(INoteRepository noteRepository, int noteId) {
        super(noteRepository, noteId);
    }
}
