package ua.nure.romanik.notes.presentation.ui.main;


import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainActivityAbs;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainViewModelAbs;
import ua.nure.romanik.notes.App;

public class MainActivity extends MainActivityAbs {

    @Override
    public void onClickNote(Note note) {
        int noteId = note.getId();
    }

    @Override
    public MainViewModelAbs createViewModel() {
        return new ModelFactory(App.getNoteRepository()).create(MainViewModel.class);
    }
}
