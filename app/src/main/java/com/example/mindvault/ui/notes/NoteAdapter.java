package com.example.mindvault.ui.notes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindvault.R;
import com.example.mindvault.data.Note;

import java.util.Collections;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes   = Collections.emptyList();
    private final OnNoteClick listener;
    private final OnNoteDelete deleteNote;

    public NoteAdapter(List<Note> notes, OnNoteClick listener, OnNoteDelete deleteNote) {
        this.notes = notes;
        this.listener = listener;
        this.deleteNote = deleteNote;
    }

    public interface OnNoteClick {
        void onClick(Note note);
    }

    public interface OnNoteDelete {
        void deleteNote(Note note);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotes(List<Note> newList) {
        notes = newList;
        notifyDataSetChanged();
    }

    @NonNull
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View vItem = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_note, p, false);
        return new NoteViewHolder(vItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder h, int pos) {
        Note n = notes.get(pos);
        h.title.setText(n.title);
        h.content.setText(n.content);
        h.itemView.setOnClickListener(v -> listener.onClick(n));
        h.buttonDelete.setOnClickListener(v -> deleteNote.deleteNote(n));
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView buttonDelete;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textNoteTitle);
            content = itemView.findViewById(R.id.textNoteContent);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
