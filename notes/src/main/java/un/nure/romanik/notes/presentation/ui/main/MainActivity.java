package un.nure.romanik.notes.presentation.ui.main;


import android.os.Bundle;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainActivityAbs;
import un.nure.romanik.notes.App;

public class MainActivity extends MainActivityAbs {

    @Override
    public void onClickNote(Note note) {

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public void createViewModel() {
        setMainViewModel(
                new ModelFactory(App.getNoteRepository()).create(MainViewModel.class)
        );
    }
}
