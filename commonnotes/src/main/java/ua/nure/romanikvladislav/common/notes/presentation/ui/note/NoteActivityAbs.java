package ua.nure.romanikvladislav.common.notes.presentation.ui.note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ua.nure.romanikvladislav.common.notes.R;
import ua.nure.romanikvladislav.common.notes.databinding.ActivityNoteBinding;

public abstract class NoteActivityAbs extends AppCompatActivity {

    private NoteViewModelAbs noteViewModelAbs;
    private ActivityNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
    }

    public abstract NoteViewModelAbs createViewModel();

    public ActivityNoteBinding getBinding() {
        return binding;
    }

    public NoteViewModelAbs getNoteViewModel() {
        return noteViewModelAbs;
    }
}
