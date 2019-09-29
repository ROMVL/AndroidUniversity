package ua.nure.romanikvladislav.common.notes.presentation.ui.note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ua.nure.romanikvladislav.common.notes.R;

public abstract class NoteActivityAbs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
    }
}
