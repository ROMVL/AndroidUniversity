package ua.nure.romanik.itunes.presentation.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import ua.nure.romanik.itunes.R;
import ua.nure.romanik.itunes.data.model.Song;
import ua.nure.romanik.itunes.databinding.ActivityMainBinding;
import ua.nure.romanik.itunes.presentation.adapter.SongAdapter;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnClickSongListener {

    static final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    static final int permissionRequestCode = 607590;
    static final String MUSIC_PLAY_OR_PAUSE = "music_action_play_or_pause";
    static final String MUSIC_NEXT = "music_action_next";
    static final String MUSIC_PREV = "music_action_prev";

    private ActivityMainBinding activityMainBinding;
    private SongAdapter songAdapter;
    private MainViewModel mainViewModel;

    private BroadcastReceiver musicBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbarMainActivity.toolbar);

        mainViewModel = new ViewModelProvider(
                this.getViewModelStore(),
                new MainViewModelFactory(getApplication())
        ).get(MainViewModel.class);
        activityMainBinding.setViewModel(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);

        initSongsList();
        initObservers();

        if (!isPermissionGranted(permissions[0])) requestPermissions(permissions, permissionRequestCode);
        else mainViewModel.fetchSongsFromLocalStorage();
        musicBroadCastReceiver = new MusicNotificationReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MUSIC_PLAY_OR_PAUSE);
        intentFilter.addAction(MUSIC_NEXT);
        intentFilter.addAction(MUSIC_PREV);
        registerReceiver(musicBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicBroadCastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mainViewModel.fetchSongsFromLocalStorage();
        } else {
            requestPermissions(permissions, permissionRequestCode);
        }
    }

    private void initObservers() {
        mainViewModel.getSongs().observe(this, songs -> songAdapter.setSongs(songs));
        mainViewModel.getErrorLiveData().observe(this, this::showErrorMessage);
        mainViewModel.getUserEvent().observe(this, userEvent -> {
            if (userEvent.equals(UserEvent.PLAY)) {
                activityMainBinding.mediaPlayerControl.ivPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            } else if (userEvent.equals(UserEvent.REPEAT_OFF)) {
                activityMainBinding.mediaPlayerControl.ivRepeat.setImageResource(R.drawable.ic_repeat_off);
            } else if (userEvent.equals(UserEvent.REPEAT_ON)) {
                activityMainBinding.mediaPlayerControl.ivRepeat.setImageResource(R.drawable.ic_repeat_on);
            } else {
                activityMainBinding.mediaPlayerControl.ivPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
            }
        });
    }

    private void initSongsList() {
        songAdapter = new SongAdapter(this);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            activityMainBinding.rvSongs.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            activityMainBinding.rvSongs.setLayoutManager(layoutManager);
        }
        activityMainBinding.rvSongs.setAdapter(songAdapter);
        activityMainBinding.rvSongs.setHasFixedSize(true);
    }

    @Override
    public void onClick(Song song) {
        mainViewModel.startSong(song);
    }

    private void showErrorMessage(Throwable throwable) {
        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat
                .checkSelfPermission(
                        MainActivity.this,
                        permission
                ) == PackageManager.PERMISSION_GRANTED;
    }

    public class MusicNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(MUSIC_PLAY_OR_PAUSE)) {
                    mainViewModel.playOrPause();
                } else if (intent.getAction().equals(MUSIC_NEXT)) {
                    mainViewModel.playNext();
                } else if (intent.getAction().equals(MUSIC_PREV)) {
                    mainViewModel.playPrev();
                }
            }
        }
    }

}
