package edu.uga.cs.csci4060_rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RiderScreen extends AppCompatActivity {

    private Button driverViewBtn, makeRequestBtn, logOutBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider);

        logOutBtn = findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();

        makeRequestBtn = findViewById(R.id.submitRequest);


        driverViewBtn = findViewById(R.id.viewAsDriver);
        driverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DriverScreen.class);
                v.getContext().startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( v.getContext(), SignIn.class );
                v.getContext().startActivity( intent );

            }
        });

    }
}