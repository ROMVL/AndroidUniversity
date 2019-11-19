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

public abstract class NoteActivityAbs extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 10487;

    private NoteViewModelAbs noteViewModelAbs;
    private ActivityNoteBinding binding;
    private int priorityNote = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        noteViewModelAbs = createViewModel();
        binding.setLifecycleOwner(this);
        binding.imageView.setOnClickListener(listener -> setupImageOnClick());
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.rbLow.getId()) {
                priorityNote = 0;
            } else if (checkedId == binding.rbMedium.getId()) {
                priorityNote = 1;
            } else {
                priorityNote = 2;
            }
        });
        noteViewModelAbs.getNote().observe(this, note -> {
            Log.d("NoteActivityAbs", "observe " + note.toString());
            if (!note.getImagePath().isEmpty()) {
                setupImage(Uri.parse(note.getImagePath()));
            } else {
                binding.imageView.setImageResource(R.drawable.ic_camera);
            }
            initNoteView(note);
        });
        noteViewModelAbs.getBackEvent().observe(this, backEvent -> {
            if (backEvent) this.onBackPressed();
        });
        binding.saveButton.setOnClickListener(listener ->
                noteViewModelAbs.saveNote(
                        priorityNote,
                        binding.name.getText().toString(),
                        binding.note.getText().toString()
                )
        );
    }

    public abstract NoteViewModelAbs createViewModel();

    protected void setupImageOnClick() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    protected void setupImage(Uri imageUri) {
        if (imageUri.equals(Uri.EMPTY)) {
            binding.imageView.setImageResource(R.drawable.ic_camera);
            return;
        }
        Glide.with(this)
                .load(imageUri)
                .error(R.drawable.ic_camera)
                .into(binding.imageView);
    }

    protected void initNoteView(Note note) {
        binding.name.setText(note.getTitle());
        binding.note.setText(note.getDescription());
        if (note.getPriority() == 0) {
            binding.rbLow.setChecked(true);
        } else if (note.getPriority() == 1) {
            binding.rbMedium.setChecked(true);
        } else if (note.getPriority() == 2) {
            binding.rbHigh.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG) {
            Uri imagePath = data.getData();
            Log.d("NoteActivityAbs", imagePath.toString());
            noteViewModelAbs.setImage(imagePath.toString());
            setupImage(imagePath);
        }
    }

    protected ActivityNoteBinding getBinding() { return binding; }
}
