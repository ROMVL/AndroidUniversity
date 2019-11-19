package ua.nure.romanik.notesdatabase.presentation.ui.main;

import ua.nure.romanik.notesdatabase.App;
import ua.nure.romanik.notesdatabase.presentation.ui.note.NoteActivity;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainActivityAbs;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainViewModelAbs;

public class MainActivity extends MainActivityAbs {

    @Override
    public void onClickEditNote(Note note) {
        startActivity(NoteActivity.newIntent(this, note.getRowId()));
    }

    @Override
    public void onClickAddNote() {
        startActivity(NoteActivity.newIntent(this, -1));
    }

    @Override
    public MainViewModelAbs createViewModel() {
        return new MainViewModelFactory(App.getNoteRepository()).create(MainViewModel.class);
    }

}
