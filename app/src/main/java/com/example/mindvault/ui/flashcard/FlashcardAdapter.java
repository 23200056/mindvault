package com.example.mindvault.ui.flashcard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindvault.R;
import com.example.mindvault.data.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.ViewHolder> {
    private List<Flashcard> flashcards = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText, descriptionText,statustext;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextView);
            descriptionText = itemView.findViewById(R.id.descriptionTextView);
            statustext = itemView.findViewById(R.id.stateTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flashcard current = flashcards.get(position);
        holder.titleText.setText(current.getTitle());
        holder.descriptionText.setText(current.getDescription());
        holder.statustext.setText(current.getState());

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, FlashcardDetailActivity.class);
            intent.putParcelableArrayListExtra("flashcards", new ArrayList<>(flashcards));
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
    public Flashcard getFlashcardAt(int position) {
        return flashcards.get(position);
    }

}
