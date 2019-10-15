package ua.nure.romanikvladislav.common.notes.presentation.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ua.nure.romanikvladislav.common.notes.data.model.Note;
import ua.nure.romanikvladislav.common.notes.databinding.ItemNoteBinding;

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
        return new NoteViewHolder(ItemNoteBinding.inflate(inflater));
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
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredNotes.clear();
                filteredNotes.addAll((ArrayList<? extends Note>) filterResults.values);
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
            noteBinding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClickNote(note);
                }
            });
        }
    }

    public interface NoteClickListener {
        void onClickNote(Note note);
    }
}
