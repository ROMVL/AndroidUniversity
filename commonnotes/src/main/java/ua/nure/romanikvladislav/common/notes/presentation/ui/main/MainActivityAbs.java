package ua.nure.romanikvladislav.common.notes.presentation.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.R;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.databinding.ActivityMainBinding;
import ua.nure.romanikvladislav.common.notes.presentation.adapter.NoteAdapter;

public abstract class MainActivityAbs extends AppCompatActivity implements NoteAdapter.NoteClickListener {

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
        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        setSupportActionBar(toolbar);
        //mainViewModelAbs.notifyNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setOnQueryTextListener(searchListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_priority_high) {
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.action_priority_medium) {
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.action_priority_low) {
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.action_priority_all) {
            item.setChecked(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            //item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.action_remove) {
            //item.setChecked(true);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }


    protected void setupRecycler() {
        adapter = new NoteAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.rvNotes.setLayoutManager(manager);
        binding.rvNotes.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvNotes.getContext(),
                manager.getOrientation());
        binding.rvNotes.addItemDecoration(dividerItemDecoration);
    }

    protected void setupLiveDataObservables() {
        mainViewModelAbs.getNotes().observe(this, notes -> {
            adapter.setNotesData(notes, query);
            Log.d("MainActivityAbs", notes.toString());
        });
    }

    @Override
    public void onContextMenuClosed(@NonNull Menu menu) {
        super.onContextMenuClosed(menu);
    }

    public abstract MainViewModelAbs createViewModel();

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public MainViewModelAbs getMainViewModel() {
        return mainViewModelAbs;
    }

}
