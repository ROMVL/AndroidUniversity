package ua.nure.romanik.itunes.presentation.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import ua.nure.romanik.itunes.R;
import ua.nure.romanik.itunes.data.model.Song;
import ua.nure.romanik.itunes.databinding.ActivityMainBinding;
import ua.nure.romanik.itunes.presentation.adapter.SongAdapter;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnClickSongListener {

    private ActivityMainBinding activityMainBinding;
    private SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initSongsList();
    }

    private void initSongsList() {
        songAdapter = new SongAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        activityMainBinding.rvSongs.setAdapter(songAdapter);
        activityMainBinding.rvSongs.setLayoutManager(layoutManager);
        activityMainBinding.rvSongs.setHasFixedSize(true);
        activityMainBinding.rvSongs.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onClick(Song song) {

    }
}
