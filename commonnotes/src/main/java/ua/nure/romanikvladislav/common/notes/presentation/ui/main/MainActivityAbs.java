package ua.nure.romanikvladislav.common.notes.presentation.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.R;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.databinding.ActivityMainBinding;
import ua.nure.romanikvladislav.common.notes.presentation.adapter.NoteAdapter;

public abstract class MainActivityAbs extends AppCompatActivity {

    private MainViewModelAbs mainViewModelAbs;
    private ActivityMainBinding binding;
    private NoteAdapter adapter;
    private String query = null;

    public abstract void onClickNote(Note note);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModelAbs = createViewModel();
        binding.setViewModel(mainViewModelAbs);
        setupRecycler();
        setupLiveDataObservables();
    }

    protected void setupLiveDataObservables() {
        mainViewModelAbs.getNotes().observe(this, notes -> {
            adapter.setNotesData(notes, query);
        });
    }

    protected void setupRecycler() {
        adapter = new NoteAdapter(this::onClickNote);
        binding.rvNotes.setAdapter(adapter);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));
    }

    public abstract MainViewModelAbs createViewModel();

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public MainViewModelAbs getMainViewModel() {
        return mainViewModelAbs;
    }
}
