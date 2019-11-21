package ua.nure.romanik.itunes.presentation.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import ua.nure.romanik.itunes.R;
import ua.nure.romanik.itunes.data.model.Song;
import ua.nure.romanik.itunes.databinding.ActivityMainBinding;
import ua.nure.romanik.itunes.presentation.adapter.SongAdapter;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnClickSongListener {

    private static final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    private static final int permissionRequestCode = 607590;

    private ActivityMainBinding activityMainBinding;
    private SongAdapter songAdapter;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new MainViewModelFactory(getApplication()).create(MainViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setViewModel(mainViewModel);
        activityMainBinding.setLifecycleOwner(this);
        initSongsList();
        initObservers();
        if (!isPermissionGranted(permissions[0])) requestPermissions(permissions, permissionRequestCode);
        else mainViewModel.fetchSongsFromLocalStorage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mainViewModel.fetchSongsFromLocalStorage();
        } else {
            requestPermissions(permissions, permissionRequestCode);
        }
    }

    private void initObservers() {
        mainViewModel.getSongs().observe(this, songs -> songAdapter.setSongs(songs));
        mainViewModel.getErrorLiveData().observe(this, this::showErrorMessage);
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
}
