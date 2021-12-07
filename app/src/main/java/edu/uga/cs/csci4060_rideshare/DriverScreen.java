package edu.uga.cs.csci4060_rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;
import java.util.List;

public class DriverScreen extends AppCompatActivity {

    public static final String DEBUG_TAG = "DriverScreen";

    private Button makeOfferBtn, riderViewBtn, logOutBtn,deleteBtn;
    private TextView departureLocation, arrivalLocation, departureTime;
    private FirebaseAuth auth;
    private Button account;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private List<Ride> rideList;


    public DriverScreen(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver);

        auth = FirebaseAuth.getInstance();
        account = findViewById(R.id.account);
        deleteBtn = findViewById(R.id.delete);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent( v.getContext(), AccountDetails.class );
                v.getContext().startActivity( intent );
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent( v.getContext(), DeleteOffer.class );
                v.getContext().startActivity( intent );
            }
        });


        // Display ride requests for driver to accept
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );
        rideList = new ArrayList<Ride>();

        // get a Firebase DB instance reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride-requests");

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Ride ride = postSnapshot.getValue(Ride.class);
                    rideList.add(ride);
                    Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onCreate(): added: " + ride );
                }
                Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onCreate(): setting recyclerAdapter" );

                // Now, create a JobLeadRecyclerAdapter to populate a RecyclerView to display the job leads.
                recyclerAdapter = new RideRecyclerAdapter( rideList );
                recyclerView.setAdapter( recyclerAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );

        // Submit TextEdit info to Firebase as a ride
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
                Ride newRide = new Ride(Boolean.TRUE, null, dropoff, pickup, start);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ride-offers");

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

        // Swap to rider view
        riderViewBtn = findViewById(R.id.viewAsRider);
        riderViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RiderScreen.class);
                v.getContext().startActivity(intent);
            }
        });

        // Logout
        logOutBtn = findViewById(R.id.logout);
        logOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( v.getContext(), SignIn.class );
                v.getContext().startActivity( intent );

            }
        });

    }

    // this is our own callback for a DialogFragment which adds a new ride
    public void onFinishNewJobLeadDialog(Ride ride) {
        // add the new ride
        // Add a new element (Ride) to the list of rides in Firebase.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride-requests");

        // First, a call to push() appends a new node to the existing list (one is created
        // if this is done for the first time).  Then, we set the value in the newly created
        // list node to store the new job lead.
        // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
        // the previous apps to maintain rides.
        myRef.push().setValue( ride )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Update the recycler view to include the new job lead
                        rideList.add( ride );
                        recyclerAdapter.notifyItemInserted(rideList.size() - 1);

                        Log.d( DEBUG_TAG, "Ride saved: " + ride );
                        // Show a quick confirmation
                        Toast.makeText(getApplicationContext(), "Ride created for " + ride.getDestinationLocation(),
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText( getApplicationContext(), "Failed to create a ride for " + ride.getDestinationLocation(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}