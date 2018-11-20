package note_app.roman.note_app.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import note_app.roman.note_app.R;
import note_app.roman.note_app.note.Note;
import note_app.roman.note_app.utils.dialog.DialogAddNote;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Note> mData;
    private Realm realm = Realm.getDefaultInstance();
    private Activity activity = null;

    public RecyclerViewAdapter(List<Note> data, Activity activity) {
        this.mData = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item,
                viewGroup, false);
        return new RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Note note = mData.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
        holder.tvType.setText(note.getType());
        holder.tvStatus.setText(note.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Note selectedNote = realm.where(Note.class).equalTo("title", (String) holder.tvTitle.getText()).findFirst();
            DialogAddNote customDialog = new DialogAddNote((AppCompatActivity) activity, selectedNote);
            customDialog.showDialog();
        });

    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvType;
        private TextView tvStatus;
        private LinearLayout llRecycleViewItem;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvType = itemView.findViewById(R.id.tvType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
