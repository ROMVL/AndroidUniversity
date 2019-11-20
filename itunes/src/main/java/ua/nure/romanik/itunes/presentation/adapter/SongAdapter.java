package ua.nure.romanik.itunes.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.nure.romanik.itunes.data.model.Song;
import ua.nure.romanik.itunes.databinding.ItemSongBinding;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs;
    private OnClickSongListener listener;

    public SongAdapter(OnClickSongListener listener) {
        this.songs = new ArrayList<>();
        this.listener = listener;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(songs.get(position));
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        private ItemSongBinding itemView;

        SongViewHolder(@NonNull ItemSongBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(final Song song) {
            itemView.setSong(song);
            itemView.getRoot().setOnClickListener(view -> listener.onClick(song));
        }
    }

    public interface OnClickSongListener {
        void onClick(Song song);
    }
}
