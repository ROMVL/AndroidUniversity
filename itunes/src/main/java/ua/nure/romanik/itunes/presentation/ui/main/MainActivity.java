package ua.nure.romanik.itunes.presentation.ui.main;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import ua.nure.romanik.itunes.R;
import ua.nure.romanik.itunes.data.model.Song;
import ua.nure.romanik.itunes.databinding.ActivityMainBinding;
import ua.nure.romanik.itunes.presentation.adapter.SongAdapter;

import static ua.nure.romanik.itunes.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnClickSongListener {

    private static final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    private static final int permissionRequestCode = 607590;
    private static final String MUSIC_PLAY_OR_PAUSE = "music_action_play_or_pause";
    private static final String MUSIC_NEXT = "music_action_next";
    private static final String MUSIC_PREV = "music_action_prev";

    private ActivityMainBinding activityMainBinding;
    private SongAdapter songAdapter;
    private MainViewModel mainViewModel;
    private NotificationManagerCompat notificationManagerCompat;

    private BroadcastReceiver musicBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        notificationManagerCompat = NotificationManagerCompat.from(this);
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
                activityMainBinding.playButton.setImageResource(R.drawable.ic_play_arrow);
                showNotification(mainViewModel.getCurrentSong().getValue(), R.drawable.ic_play_arrow_black_24dp);
            } else if (userEvent.equals(UserEvent.SHOW_NOTIFICATION)) {
                showNotification(mainViewModel.getCurrentSong().getValue(), R.drawable.ic_pause_black_24dp);
            } else {
                activityMainBinding.playButton.setImageResource(R.drawable.ic_pause);
                showNotification(mainViewModel.getCurrentSong().getValue(), R.drawable.ic_pause_black_24dp);
            }
        });
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

    private void showNotification(@Nullable Song song, @DrawableRes int resourcePlayOrPauseButton) {
        if (song == null) return;
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_music_collapsed);

        collapsedView.setImageViewResource(R.id.ivIconApp, R.drawable.ic_launcher_foreground);
        collapsedView.setTextViewText(R.id.tvAppName, getString(R.string.app_name));
        collapsedView.setTextViewText(R.id.tvTitle, song.getTitle());
        collapsedView.setImageViewResource(R.id.ivPlay, resourcePlayOrPauseButton);
        collapsedView.setImageViewResource(R.id.ivNext, R.drawable.ic_skip_next_black_24dp);
        collapsedView.setImageViewResource(R.id.ivPrev, R.drawable.ic_skip_previous_black_24dp);
        collapsedView.setOnClickPendingIntent(R.id.ivPlay, createActionPendingIntent(MUSIC_PLAY_OR_PAUSE, 1));
        collapsedView.setOnClickPendingIntent(R.id.ivNext, createActionPendingIntent(MUSIC_NEXT, 2));
        collapsedView.setOnClickPendingIntent(R.id.ivPrev, createActionPendingIntent(MUSIC_PREV, 3));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(true)
                .setCustomContentView(collapsedView)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    private PendingIntent createActionPendingIntent(String action, int requestCode) {
        Intent intent = new Intent(action);
        return PendingIntent.getBroadcast(this,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
