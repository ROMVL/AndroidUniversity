package ua.nure.romanik.itunes.data.model;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Environment;

public interface Constants {

    int CODE_READ_EXTERNAL_STORAGE = 0;
    int NOTIFY_ID = 1;

    String PLAY_MODE = "play_mode";
    String DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3";
    Uri DATA_URI = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 13359);

}
