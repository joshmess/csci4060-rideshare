package edu.uga.cs.csci4060_rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class RiderScreen extends AppCompatActivity {

    private Button driverViewBtn, makeRequestBtn, logOutBtn;
    private EditText departureLocation, arrivalLocation, departureTime;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider);

        logOutBtn = findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();

        makeRequestBtn = findViewById(R.id.submitRequest);

        makeRequestBtn.setOnClickListener(new View.OnClickListener() {
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
                Ride newRide = new Ride(Boolean.FALSE, null, dropoff, pickup, start);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ride-requests");

                myRef.push().setValue( newRide )
                        .addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Show a quick confirmation
                                Toast.makeText(getApplicationContext(), "Ride created for " + newRide.getDestinationLocation(),
                                        Toast.LENGTH_SHORT).show();

                                // Clear the EditTexts for next use.
                                departureLocation.setText("");
                                arrivalLocation.setText("");
                                departureTime.setText("");
                            }
                        })
                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText( getApplicationContext(), "Failed to create a ride for " + newRide.getDestinationLocation(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

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