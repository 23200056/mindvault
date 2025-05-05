package com.example.mindvault.ui.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Note;
import com.example.mindvault.data.NoteDao;

public class NotesFragment extends Fragment {

    private NoteDao noteDao;
    private NoteAdapter adapter;
    private RecyclerView recycler;
    private View emptyState;
    private View searchBtn;
    private EditText editBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "mindvault-db")
                .allowMainThreadQueries()
                .build();

        noteDao = db.noteDao();
        recycler = root.findViewById(R.id.recyclerNotes);
        emptyState = root.findViewById(R.id.emptyState);
        searchBtn = root.findViewById(R.id.buttonSearch);
        editBox = root.findViewById(R.id.editSearch);

        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        adapter = new NoteAdapter(
                noteDao.getAllNotes(),
                note -> openFragment(CreateNoteFragment.newInstance(note.id)),
                note -> confirmDelete(note)
        );
        recycler.setAdapter(adapter);

        root.findViewById(R.id.buttonCreateNotesPage)
                .setOnClickListener(v -> openFragment(CreateNoteFragment.newInstance(null)));

        updateVisibility();

        searchBtn.setOnClickListener(v -> toggleSearch());

        editBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                String q = s.toString().trim();
                if (q.isEmpty()) {
                    adapter.setNotes(noteDao.getAllNotes());
                } else {
                    adapter.setNotes(noteDao.searchNotes(q));
                }
                updateVisibility();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        });

        editBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_SEARCH) {
                collapseSearch();
                return true;
            }
            return false;
        });

        editBox.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && editBox.getVisibility() == View.VISIBLE) {
                collapseSearch();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setNotes(noteDao.getAllNotes());
        updateVisibility();
    }

    private void updateVisibility() {
        String query = editBox.getText().toString().trim();
        boolean searching = !query.isEmpty();
        int count = adapter.getItemCount();

        if (searching) {
            emptyState.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        } else {
            boolean hasNotes = count > 0;
            emptyState.setVisibility(hasNotes ? View.GONE : View.VISIBLE);
            recycler.setVisibility(hasNotes ? View.VISIBLE : View.GONE);
        }
    }

    private void openFragment(Fragment f) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, f)
                .addToBackStack(null)
                .commit();
    }

    private void confirmDelete(Note note) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete this note?")
                .setMessage("This action can’t be undone.")
                .setPositiveButton("Delete", (d, w) -> {
                    noteDao.delete(note);
                    adapter.setNotes(noteDao.getAllNotes());
                    updateVisibility();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void toggleSearch() {
        if (editBox.getVisibility() == View.GONE) {
            searchBtn.setVisibility(View.GONE);
            editBox.setVisibility(View.VISIBLE);
            editBox.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager) requireContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editBox, InputMethodManager.SHOW_IMPLICIT);
        } else {
            collapseSearch();
        }
    }


    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void collapseSearch() {
        editBox.setVisibility(View.GONE);
        searchBtn.setVisibility(View.VISIBLE);
        hideKeyboard(editBox);
        updateVisibility();
    }
}
