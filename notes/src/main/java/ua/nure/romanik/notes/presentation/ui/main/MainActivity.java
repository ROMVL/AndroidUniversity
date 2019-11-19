package ua.nure.romanik.notes.presentation.ui.main;


import android.util.Log;

import ua.nure.romanik.notes.App;
import ua.nure.romanik.notes.presentation.ui.note.NoteActivity;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainActivityAbs;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainViewModelAbs;

public class MainActivity extends MainActivityAbs {

    @Override
    public void onClickEditNote(Note note) {
        Log.d("MainActivity", note.toString());
        startActivity(NoteActivity.newIntent(this, note.getId()));
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
