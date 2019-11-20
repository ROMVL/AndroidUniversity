package ua.nure.romanikvladislav.common.notes.presentation.ui.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ua.nure.romanikvladislav.common.notes.R;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.databinding.ActivityNoteBinding;
import ua.nure.romanikvladislav.common.notes.presentation.binding.BindingAdapter;

public abstract class NoteActivityAbs extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 10487;

    private NoteViewModelAbs noteViewModelAbs;
    private ActivityNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        noteViewModelAbs = createViewModel();
        binding.setLifecycleOwner(this);
        binding.setViewModel(noteViewModelAbs);
        binding.imageView.setOnClickListener(listener -> setupImageOnClick());
        noteViewModelAbs.getBackEvent().observe(this, backEvent -> {
            if (backEvent) this.onBackPressed();
        });
    }

    public abstract NoteViewModelAbs createViewModel();

    protected void setupImageOnClick() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG) {
            if (data != null) {
                Uri imagePath = data.getData();
                if (imagePath != null) {
                    noteViewModelAbs.setImage(imagePath.toString());
                    BindingAdapter.setImageByPath(binding.imageView, imagePath.toString());
                }
            }
        }
    }

}
