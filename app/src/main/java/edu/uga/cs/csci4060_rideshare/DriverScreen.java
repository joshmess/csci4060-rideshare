package edu.uga.cs.csci4060_rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;


import java.sql.Driver;

public class DriverScreen extends AppCompatActivity {

    private Button makeOfferBtn, logOutBtn;
    private EditText departureLocation, arrivalLocation, departureTime;
    private FirebaseAuth auth;

    private URL FIREBASE;

    {
        try {
            FIREBASE = new URL("https://csci4060-rideshare-default-rtdb.firebaseio.com/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    public DriverScreen(DatabaseReference database) {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }  // https://github.com/firebase/snippets-android/blob/8184cba2c40842a180f91dcfb4a216e721cc6ae6/database/app/src/main/java/com/google/firebase/referencecode/database/ReadAndWriteSnippets.java

    public DriverScreen(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver);

        logOutBtn = findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();

        logOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( v.getContext(), SignIn.class );
                v.getContext().startActivity( intent );

            }
        });

        makeOfferBtn = findViewById(R.id.submitOffer);

        makeOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                departureLocation = findViewById(R.id.departureAddress);
                arrivalLocation = findViewById(R.id.destinationAddress);
                departureTime = findViewById(R.id.departureTime);

                String pickup, dropoff, start;
                Boolean isOutOfTown;                                  // TODO: set based on destination
                pickup = departureLocation.getText().toString();
                dropoff = arrivalLocation.getText().toString();
                start = departureTime.getText().toString();
                Ride ride = new Ride(Boolean.TRUE, null, dropoff, pickup, start);


                // TODO: pain - invalid path error
                mDatabase = FirebaseDatabase.getInstance().getReference("csci4060-rideshare-default");
                mDatabase.child("rides").child(String.valueOf(departureLocation)).setValue("ride");
            }
        });



    }
}