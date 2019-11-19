package ua.nure.romanik.notesdatabase.presentation.ui.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ua.nure.romanik.notesdatabase.App;
import ua.nure.romanikvladislav.common.notes.presentation.ui.note.NoteActivityAbs;
import ua.nure.romanikvladislav.common.notes.presentation.ui.note.NoteViewModelAbs;

public class NoteActivity extends NoteActivityAbs {

    private static final String NOTE_ID_ARG = "note_id_arg";

    public static Intent newIntent(Context context, int noteId) {
        Intent noteIntent = new Intent(context, NoteActivity.class);
        noteIntent.putExtra(NOTE_ID_ARG, noteId);
        return noteIntent;
    }

    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        noteId = getIntent().getIntExtra(NOTE_ID_ARG, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public NoteViewModelAbs createViewModel() {
        return new NoteViewModelFactory(App.getNoteRepository(), noteId).create(NoteViewModel.class);
    }

}
