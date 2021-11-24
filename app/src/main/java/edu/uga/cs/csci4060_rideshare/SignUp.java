package edu.uga.cs.csci4060_rideshare;
/**
 * CSCI 4060 RideShare
 * This is the activity for creating a new user with an email and password.
 *
 * @author Josh Messitte
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "RideShare";
    private EditText emailEt, pwdEt;
    private Button signUpBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        auth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.email);
        pwdEt = findViewById(R.id.pwd);
        signUpBtn = findViewById(R.id.signup);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        String email, password;
        email = emailEt.getText().toString();
        password = pwdEt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please fill all fields...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please fill all fields...", Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                }
                else {

                    Toast.makeText(getApplicationContext(), "Registration failed." + " Maybe you already have an account", Toast.LENGTH_LONG).show();


                }
            }
        });
    }
}
