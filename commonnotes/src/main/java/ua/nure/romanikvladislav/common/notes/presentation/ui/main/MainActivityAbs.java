package ua.nure.romanikvladislav.common.notes.presentation.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.os.Bundle;

import java.util.List;

import ua.nure.romanikvladislav.common.notes.R;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.databinding.ActivityMainBinding;

public abstract class MainActivityAbs extends AppCompatActivity {

    private MainViewModelAbs mainViewModelAbs;
    private ActivityMainBinding binding;

    public abstract void onClickNote(Note note);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        createViewModel();
        binding.setViewModel(mainViewModelAbs);
        mainViewModelAbs.getNotes().observe(this, notes -> {

        });
    }

    public abstract void createViewModel();

    public void setMainViewModel(MainViewModelAbs mainViewModel) {
        mainViewModelAbs = mainViewModel;
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public MainViewModelAbs getMainViewModel() {
        return mainViewModelAbs;
    }
}
