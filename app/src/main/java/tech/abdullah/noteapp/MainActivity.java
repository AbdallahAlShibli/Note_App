package tech.abdullah.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private EditText noteTitle;
    private EditText noteTextBody;
    private Button save;
    private Button add;
    private LinearLayout noteBodyLayout;
    private FloatingActionButton signOut;

    private ListView noteList;

    private FirebaseFirestore FF;
    private FirebaseAuth FA;

    private List<NoteModel> noteListData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteTitle = findViewById(R.id.note_title);
        noteTextBody = findViewById(R.id.note_body_text);
        add = findViewById(R.id.add_note);
        save = findViewById(R.id.save_note);
        noteBodyLayout = findViewById(R.id.body_box);
        signOut = findViewById(R.id.signOut);

        noteList = findViewById(R.id.noteList);

        noteList.setVisibility(View.GONE);

        noteBodyLayout.setVisibility(View.GONE);

        FF = FirebaseFirestore.getInstance();
        FA = FirebaseAuth.getInstance();


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!noteTitle.getText().toString().isEmpty()) {

                    noteBodyLayout.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(MainActivity.this, "Enter your note Title!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                noteTitle.getText().clear();


                final String NoteTitle = noteTitle.getText().toString();
                final String NoteBody = noteTextBody.getText().toString();

                if (!TextUtils.isEmpty(NoteTitle) && !TextUtils.isEmpty(NoteBody)) {
                    setNoteData();
                    noteList.setVisibility(View.VISIBLE);
                    noteBodyLayout.setVisibility(View.GONE);

                    getNoteData();

                }


            }
        });


        getNoteData();


    }

    private void getNoteData() {

        noteList.setVisibility(View.VISIBLE);


        DocumentReference documentReference0 = FF.collection("user").document(FA.getUid());
        documentReference0.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        Object object = document.getData();

                        int counter = 0;

                        if (object instanceof Map) {

                            Map map = (Map) object;

                            while (map.toString().contains("note" + counter)) {

                                Map<String, String> n = new HashMap<String , String>((Map<? extends String, ? extends String>) map.get("note"+counter));

                                noteListData.add(new NoteModel(n.get("noteTitle"), n.get("noteBody")));

                                counter++;

                            }


                            NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), R.layout.note_details, noteListData);

                            noteList.setAdapter(noteAdapter);

                        }


                    } else {

                        Log.d(TAG, "No such document!");

                    }


                } else {

                    Log.d(TAG, "Get failed with", task.getException());

                }

            }
        });



    }

    private void setNoteData() {

        final String NoteTitle = noteTitle.getText().toString();
        final String NoteBody = noteTextBody.getText().toString();

        DocumentReference documentReference0 = FF.collection("user").document(FA.getUid());
        documentReference0.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        Object object = document.getData();

                        int counter = 0;

                        if (object instanceof Map) {

                            Map map = (Map) object;

                            while (map.toString().contains("note" + counter)) {

                                counter++;


                            }

                        }

                        CollectionReference noteData = FF.collection("user");

                        Map<String, Object> note = new HashMap<>();

                        Map<String, Object> noteDetailsMap = new HashMap<>();
                        noteDetailsMap.put("noteTitle", NoteTitle);
                        noteDetailsMap.put("noteBody", NoteBody);



                        if (counter > 0) {

                            note.put("note" + counter, noteDetailsMap);

                            noteData.document(FA.getUid()).set(note, SetOptions.merge());


                        } else {

                            note.put("note0", noteDetailsMap);

                            noteData.document(FA.getUid()).set(note, SetOptions.merge());

                        }


                    } else {

                        Log.d(TAG, "No such document!");

                    }


                } else {

                    Log.d(TAG, "Get failed with", task.getException());

                }

            }
        });


    }
}