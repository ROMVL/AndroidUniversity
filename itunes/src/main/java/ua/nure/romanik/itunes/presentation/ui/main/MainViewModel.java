package ua.nure.romanik.itunes.presentation.ui.main;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ua.nure.romanik.itunes.R;
import ua.nure.romanik.itunes.data.model.Song;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static ua.nure.romanik.itunes.App.CHANNEL_ID;
import static ua.nure.romanik.itunes.presentation.ui.main.MainActivity.MUSIC_NEXT;
import static ua.nure.romanik.itunes.presentation.ui.main.MainActivity.MUSIC_PLAY_OR_PAUSE;
import static ua.nure.romanik.itunes.presentation.ui.main.MainActivity.MUSIC_PREV;

public class MainViewModel extends AndroidViewModel {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MutableLiveData<List<Song>> songsLiveData = new MutableLiveData<>();
    private MutableLiveData<Song> currentSongLiveData = new MutableLiveData<>();
    private MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<UserEvent> userEventLiveData = new MutableLiveData<>();
    private NotificationManagerCompat notificationManagerCompat;
    private SensorManager sensorManager;
    private SensorEventListener sensorEvent;
    private Sensor lightSensor;
    private AudioManager audioManager;

    private int currentIndexSong;
    private boolean isRepeatMode = false;

    MainViewModel(@NonNull Application application) {
        super(application);
        notificationManagerCompat = NotificationManagerCompat.from(application);
        audioManager = (AudioManager) getApplication().getSystemService(AUDIO_SERVICE);
        sensorManager = (SensorManager) getApplication().getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            sensorEvent = new SensorEventListener() {
                float prevValue = 0.0F;
                @Override
                public void onSensorChanged(SensorEvent event) {
                    Log.d(MainViewModel.class.getName(), String.valueOf(event.values[0]));
                    float lightValue = event.values[0] / 50;
                    if (lightValue > prevValue) {
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                    } else {
                        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                    }
                    prevValue = lightValue;
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    Log.d(MainViewModel.class.getName(), "accuracy changed!");
                }
            };
            sensorManager.registerListener(sensorEvent, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEvent, lightSensor);
        }
    }

    LiveData<List<Song>> getSongs() {
        return songsLiveData;
    }

    LiveData<Throwable> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Song> getCurrentSong() {
        return currentSongLiveData;
    }

    LiveData<UserEvent> getUserEvent() {
        return userEventLiveData;
    }

    void fetchSongsFromLocalStorage() {
        List<Song> songs = new ArrayList<>();

        ContentResolver musicResolver = getApplication().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                songs.add(new Song(id, title, artist));
            }
            while (musicCursor.moveToNext());
        }

        if (musicCursor != null) {
            musicCursor.close();
        }

        songsLiveData.setValue(songs);
    }

    void startSong(Song song) {
        currentIndexSong = Objects.requireNonNull(songsLiveData.getValue()).indexOf(song);
        Uri trackUri = ContentUris
                .withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getId());

        mediaPlayer.stop();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(getApplication(), trackUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mPlayer -> {
                if (isRepeatMode) {
                    startSong(song);
                } else {
                    playNext();
                }
            });
            userEventLiveData.setValue(UserEvent.PAUSE);
            currentSongLiveData.setValue(song);
            showNotification(song, R.drawable.ic_pause_black_24dp);
            userEventLiveData.setValue(UserEvent.SHOW_NOTIFICATION);
        } catch (IOException e) {
            errorLiveData.setValue(e);
        }
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            userEventLiveData.setValue(UserEvent.PLAY);
            showNotification(currentSongLiveData.getValue(), R.drawable.ic_play_arrow_black_24dp);
        } else {
            mediaPlayer.start();
            userEventLiveData.setValue(UserEvent.PAUSE);
            showNotification(currentSongLiveData.getValue(), R.drawable.ic_pause_black_24dp);
        }
    }

    public void playNext() {
        if (songsLiveData.getValue() != null) {
            if (currentIndexSong < songsLiveData.getValue().size() - 1) {
                startSong(songsLiveData.getValue().get(currentIndexSong + 1));
                if (currentIndexSong < songsLiveData.getValue().size() - 1) currentIndexSong++;
            }
        }
    }

    public void playPrev() {
        if (songsLiveData.getValue() != null) {
            if (currentIndexSong != 0) {
                startSong(songsLiveData.getValue().get(currentIndexSong - 1));
                if (currentIndexSong > 0) currentIndexSong--;
            }
        }
    }

    public void shufflePlayList() {
        if (songsLiveData.getValue() != null) {
            List<Song> songsShuffled = songsLiveData.getValue();
            if (!songsShuffled.isEmpty()) {
                Collections.shuffle(songsShuffled);
                currentIndexSong = 0;
                songsLiveData.setValue(songsShuffled);
                startSong(songsShuffled.get(currentIndexSong));
            }
        }
    }

    public void repeatMelody() {
        if (isRepeatMode) {
            userEventLiveData.postValue(UserEvent.REPEAT_OFF);
        } else {
            userEventLiveData.postValue(UserEvent.REPEAT_ON);
        }
        isRepeatMode = !isRepeatMode;
    }

    private void showNotification(@Nullable Song song, @DrawableRes int resourcePlayOrPauseButton) {
        if (song == null) return;
        RemoteViews collapsedView = new RemoteViews(getApplication().getPackageName(),
                R.layout.notification_music_collapsed);

        collapsedView.setImageViewResource(R.id.ivIconApp, R.drawable.ic_launcher_foreground);
        collapsedView.setTextViewText(R.id.tvAppName, getApplication().getString(R.string.app_name));
        collapsedView.setTextViewText(R.id.tvTitle, String.format("%s - %s", song.getTitle(), song.getArtist()));
        collapsedView.setImageViewResource(R.id.ivPlay, resourcePlayOrPauseButton);
        collapsedView.setImageViewResource(R.id.ivNext, R.drawable.ic_skip_next_black_24dp);
        collapsedView.setImageViewResource(R.id.ivPrev, R.drawable.ic_skip_previous_black_24dp);
        collapsedView.setOnClickPendingIntent(R.id.ivPlay, createActionPendingIntent(MUSIC_PLAY_OR_PAUSE, 1));
        collapsedView.setOnClickPendingIntent(R.id.ivNext, createActionPendingIntent(MUSIC_NEXT, 2));
        collapsedView.setOnClickPendingIntent(R.id.ivPrev, createActionPendingIntent(MUSIC_PREV, 3));

        Notification notification = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
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
        return PendingIntent.getBroadcast(getApplication(),
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
