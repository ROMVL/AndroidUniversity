package ua.nure.romanikvladislav.common.notes.presentation.binding;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ua.nure.romanikvladislav.common.notes.R;

public class BindingAdapter {

    @androidx.databinding.BindingAdapter("app:setImageByPath")
    public static void setImageByPath(ImageView imageView, String path) {
        if (path != null) {
            Uri imageUri = Uri.parse(path);
            if (imageUri.equals(Uri.EMPTY)) {
                imageView.setImageResource(R.drawable.ic_camera);
                return;
            }
            Glide.with(imageView.getContext())
                    .load(imageUri)
                    .error(R.drawable.ic_camera)
                    .into(imageView);
        }
    }

}
