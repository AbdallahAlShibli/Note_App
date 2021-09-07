package tech.abdullah.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private EditText userName;
    private EditText age;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView login;

    private FirebaseAuth FA;
    private FirebaseFirestore FF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userName = findViewById(R.id.user_name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        register = findViewById(R.id.button_register);
        login = findViewById(R.id.register_login);

        FA = FirebaseAuth.getInstance();
        FF = FirebaseFirestore.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String UserName = userName.getText().toString();
                final int Age = Integer.parseInt(age.getText().toString());
                final String Email = email.getText().toString();
                final String Password = password.getText().toString();

                if (checkEmail(Email)) {

                    FA.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(Registration.this, "Register is successfully", Toast.LENGTH_SHORT).show();

                                CollectionReference collectionReference = FF.collection("user");

                                Map<String, Object> userData = new HashMap<>();

                                userData.put("userName", UserName);
                                userData.put("age", Age);
                                userData.put("email", Email);


                                try {
                                    Thread.sleep(3000);

                                    collectionReference.document(FA.getUid()).set(userData);

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                    });

                }else {

                    Toast.makeText(Registration.this, "Error! check you'r email!", Toast.LENGTH_SHORT).show();

                }


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });

    }

    private boolean checkEmail(String email) {

        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}