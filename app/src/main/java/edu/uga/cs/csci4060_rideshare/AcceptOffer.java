package edu.uga.cs.csci4060_rideshare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptOffer extends AppCompatActivity {

    public static final String DEBUG_TAG = "AcceptOffer";
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private EditText destLoc,newDepart,newDest,newTime;
    private Button update;
    private List<Ride> rideList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_ride);
        update = findViewById(R.id.update);
        newDepart = findViewById(R.id.newDepartureAddress);
        newDest = findViewById(R.id.newDestinationAddress);
        newTime = findViewById(R.id.newDepartureTime);
        destLoc = findViewById(R.id.destLoc);
        auth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );
        rideList = new ArrayList<Ride>();
        // get a Firebase DB instance reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride-offers");

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String destToDelete = destLoc.getText().toString();
                System.out.println("DEST SELECTED::::" + destToDelete);

                Query delQuery = myRef.orderByChild("destinationLocation").equalTo(destToDelete);

                String pickup, dropoff, start;

                pickup = newDepart.getText().toString();
                dropoff = newDest.getText().toString();
                start = newTime.getText().toString();
                Ride newRide = new Ride(Boolean.FALSE, null, dropoff, pickup, start);


                delQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot rideSnapshot: dataSnapshot.getChildren()) {
                            rideSnapshot.getRef().setValue(newRide).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Show a quick confirmation
                                    Toast.makeText(getApplicationContext(), "Ride updated: " + newRide.getDestinationLocation(),
                                            Toast.LENGTH_SHORT).show();

                                    // Clear the EditTexts for next use.
                                    newDepart.setText("");
                                    newDest.setText("");
                                    newTime.setText("");
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(DEBUG_TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

    }
}
