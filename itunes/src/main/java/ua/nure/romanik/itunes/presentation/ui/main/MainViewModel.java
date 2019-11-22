package ua.nure.romanik.itunes.presentation.ui.main;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ua.nure.romanik.itunes.data.model.Song;

public class MainViewModel extends AndroidViewModel {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MutableLiveData<List<Song>> songsLiveData = new MutableLiveData<>();
    private MutableLiveData<Song> currentSongLiveData = new MutableLiveData<>();
    private MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<UserEvent> userEventLiveData = new MutableLiveData<>();

    private int currentIndexSong;

    MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
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

    LiveData<UserEvent> getUserEvent() { return userEventLiveData; }

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
            mediaPlayer.setOnCompletionListener(mPlayer -> playNext());
            userEventLiveData.setValue(UserEvent.PAUSE);
            currentSongLiveData.setValue(song);
            userEventLiveData.setValue(UserEvent.SHOW_NOTIFICATION);//showNotification2(song);
        } catch (IOException e) {
            errorLiveData.setValue(e);
        }
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            userEventLiveData.setValue(UserEvent.PLAY);
        } else {
            mediaPlayer.start();
            userEventLiveData.setValue(UserEvent.PAUSE);
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

}
