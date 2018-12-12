package note_app.roman.note_app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import io.realm.Realm;
import note_app.roman.note_app.R;
import note_app.roman.note_app.note.Note;
import note_app.roman.note_app.ui.activity.MainActivity;
import note_app.roman.note_app.utils.Constants;
import note_app.roman.note_app.utils.Preference;
import note_app.roman.note_app.utils.RecyclerViewAdapter;

public class FragmentForTabs extends Fragment {

    private String tab_type = "";
    private Realm realm;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_for_notes, container, false);

        realm = Realm.getDefaultInstance();

        recyclerView = view.findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setRecyclerViewAdapter();
/*
        FragmentActivity activity = getActivity();
        if(activity instanceof MainActivity){
            ((MainActivity) activity).ivAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) activity).showDialog(FragmentForTabs.this);
                }
            });
        }
*/
        return view;
    }

    public void setFragmentType(String str) {
        this.tab_type = str;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setRecyclerViewAdapter() {
        ArrayList<Note> myNoteArray = new ArrayList<Note>();

        List<Note> myAllNoteArray = realm.where(Note.class).findAll();
        for (int i = 0; i < myAllNoteArray.size(); i++) {
            if (myAllNoteArray.get(i).getLogin().equals(Preference.getUser(Objects.requireNonNull(getContext())))) {
                myNoteArray.add(myAllNoteArray.get(i));
            }
        }


        switch (tab_type) {
            case Constants.TASKS:
                List<Note> tasksArray = new ArrayList<>();
                for (int i = 0; i < myNoteArray.size(); i++) {
                    if (Constants.CURRENT.equals(Objects.requireNonNull(myNoteArray.get(i)).getStatus())) {
                        tasksArray.add(myNoteArray.get(i));
                    }
                }
                adapter = new RecyclerViewAdapter(tasksArray, ((MainActivity) getActivity()));
                break;

            case Constants.NOTES:
                List<Note> notesArray = new ArrayList<>();
                for (int j = 0; j < myNoteArray.size(); j++) {
                    if (Constants.COMPLETED.equals(Objects.requireNonNull(myNoteArray.get(j)).getStatus())) {
                        notesArray.add(myNoteArray.get(j));
                    }
                }
                adapter = new RecyclerViewAdapter(notesArray, ((MainActivity) getActivity()));
                break;

            case Constants.ALL:
                adapter = new RecyclerViewAdapter(myNoteArray, ((MainActivity) getActivity()));
                break;
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }
}
