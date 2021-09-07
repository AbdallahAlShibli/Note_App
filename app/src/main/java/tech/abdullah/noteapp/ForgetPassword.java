package tech.abdullah.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText email;
    private Button send;

    private FirebaseAuth FA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        email = findViewById(R.id.forget_email);
        send = findViewById(R.id.forget_send);


        FA = FirebaseAuth.getInstance();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email = email.getText().toString();

                if (!TextUtils.isEmpty(Email)){

                    FA.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(ForgetPassword.this, "Email rest password have been sent to you'r email.", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();

                            }else {

                                Toast.makeText(ForgetPassword.this, "Error! please check you'r email and try again!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }


            }
        });
    }
}