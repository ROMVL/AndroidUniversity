package ua.nure.romanik.itunes.presentation.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ua.nure.romanik.itunes.data.model.Song;

public class SongDiffUtilsCallback extends DiffUtil.Callback {

    private List<Song> oldList;
    private List<Song> newList;

    public SongDiffUtilsCallback(List<Song> oldList, List<Song> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Song oldItem = oldList.get(oldItemPosition);
        Song newItem = newList.get(newItemPosition);
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Song oldItem = oldList.get(oldItemPosition);
        Song newItem = newList.get(newItemPosition);
        return oldItem.getArtist().equals(newItem.getArtist()) && oldItem.getTitle().equals(newItem.getArtist());
    }
}
