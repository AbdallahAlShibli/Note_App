package tech.abdullah.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<NoteModel> {

    private List<NoteModel> noteInfo;
    private Context context;
    private int resource;


    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<NoteModel> noteInfo) {
        super(context, resource, noteInfo);

        this.noteInfo = noteInfo;
        this.context = context;
        this.resource = resource;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        TextView title = view.findViewById(R.id.noteTitle_re);
        TextView body = view.findViewById(R.id.noteBody_re);

        NoteModel noteModel = noteInfo.get(position);

        title.setText(noteModel.getNoteTitle());
        body.setText(noteModel.getNoteBody());

        return view;
    }
}
