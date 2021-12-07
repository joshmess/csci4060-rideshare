package edu.uga.cs.csci4060_rideshare;
/**
 * CSCI 4060 RideShare
 * This is the splash screen for RideShare.
 *
 * @author Josh Messitte
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private Button signup;
    private Button signin;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        signup = findViewById( R.id.signup );
        signin = findViewById( R.id.signin );
        logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), SignUp.class );
                v.getContext().startActivity( intent );

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), SignIn.class );
                v.getContext().startActivity( intent );

            }
        });

    }
}