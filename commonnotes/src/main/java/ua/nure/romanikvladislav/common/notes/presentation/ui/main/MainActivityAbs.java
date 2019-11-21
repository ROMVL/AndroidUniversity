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

    public static final String ACTION_PRIORITY_HIGH = "action_priority_high";
    public static final String ACTION_PRIORITY_MEDIUM = "action_priority_medium";
    public static final String ACTION_PRIORITY_LOW = "action_priority_low";
    public static final String ACTION_PRIORITY_ALL = "action_priority_all";

    private MainViewModelAbs mainViewModelAbs;
    private ActivityMainBinding binding;
    private NoteAdapter adapter;
    private String query = null;

    public abstract void onClickAddNote();

    @Override
    public void onClickRemoveNote(Note note) {
        mainViewModelAbs.removeNote(note);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModelAbs = createViewModel();
        binding.setViewModel(mainViewModelAbs);
        binding.setLifecycleOwner(this);
        binding.floatingActionButton.setOnClickListener(listener -> onClickAddNote());
        setupRecycler();
        setupLiveDataObservables();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String querySubmitted) {
                query = querySubmitted;
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                adapter.getFilter().filter(query);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_priority_high) {
            item.setChecked(true);
            query = ACTION_PRIORITY_HIGH;
            adapter.getFilter().filter(query);
            return true;
        } else if (item.getItemId() == R.id.action_priority_medium) {
            item.setChecked(true);
            query = ACTION_PRIORITY_MEDIUM;
            adapter.getFilter().filter(query);
            return true;
        } else if (item.getItemId() == R.id.action_priority_low) {
            item.setChecked(true);
            query = ACTION_PRIORITY_LOW;
            adapter.getFilter().filter(query);
            return true;
        } else if (item.getItemId() == R.id.action_priority_all) {
            item.setChecked(true);
            query = ACTION_PRIORITY_ALL;
            adapter.getFilter().filter(query);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
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
        });
    }

    public abstract MainViewModelAbs createViewModel();

}
