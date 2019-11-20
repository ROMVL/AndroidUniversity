package ua.nure.romanikvladislav.common.notes.presentation.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ua.nure.romanikvladislav.common.notes.R;
import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.databinding.ItemNoteBinding;
import ua.nure.romanikvladislav.common.notes.presentation.ui.main.MainActivityAbs;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {

    private List<Note> notesData;
    private List<Note> filteredNotes;
    private NoteClickListener listener;

    public NoteAdapter(NoteClickListener listener) {
        this.notesData = new ArrayList<>();
        this.listener = listener;
        this.filteredNotes = new ArrayList<>(notesData);
    }

    public void setNotesData(List<Note> notes, String filteredQuery) {
        notesData.clear();
        notesData.addAll(notes);
        if (!TextUtils.isEmpty(filteredQuery)) {
            getFilter().filter(filteredQuery);
        } else {
            filteredNotes.clear();
            filteredNotes.addAll(notes);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new NoteViewHolder(ItemNoteBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.onBind(filteredNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredNotes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                FilterResults filterResults = new FilterResults();
                filteredNotes.clear();
                if (query.isEmpty()) {
                    filteredNotes.addAll(notesData);
                    filterResults.values = filteredNotes;
                    return filterResults;
                }
                switch (query) {
                    case MainActivityAbs.ACTION_PRIORITY_ALL:
                        filteredNotes.addAll(notesData);
                        filterResults.values = filteredNotes;
                        break;
                    case MainActivityAbs.ACTION_PRIORITY_HIGH:
                        for (Note note : notesData) {
                            if (note.getPriority() == 2) {
                                filteredNotes.add(note);
                            }
                        }
                        break;
                    case MainActivityAbs.ACTION_PRIORITY_MEDIUM:
                        for (Note note : notesData) {
                            if (note.getPriority() == 1) {
                                filteredNotes.add(note);
                            }
                        }
                        break;
                    case MainActivityAbs.ACTION_PRIORITY_LOW:
                        for (Note note : notesData) {
                            if (note.getPriority() == 0) {
                                filteredNotes.add(note);
                            }
                        }
                        break;
                    default:
                        for (Note note : notesData) {
                            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                                filteredNotes.add(note);
                            }
                        }
                        break;
                }
                filterResults.values = filteredNotes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private ItemNoteBinding noteBinding;

        NoteViewHolder(@NonNull ItemNoteBinding itemNoteBinding) {
            super(itemNoteBinding.getRoot());
            noteBinding = itemNoteBinding;
        }

        void onBind(Note note) {
            noteBinding.setNote(note);
            noteBinding.getRoot().setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                MenuItem edit = menu.add(0, R.id.action_edit, 1, R.string.edit);
                MenuItem remove = menu.add(0, R.id.action_remove, 2, R.string.remove);

                edit.setOnMenuItemClickListener(item -> {
                    listener.onClickEditNote(note);
                    return true;
                });

                remove.setOnMenuItemClickListener(item -> {
                    listener.onClickRemoveNote(note);
                    return true;
                });
            });
        }
    }

    public interface NoteClickListener {
        void onClickEditNote(Note note);
        void onClickRemoveNote(Note note);
    }
}
