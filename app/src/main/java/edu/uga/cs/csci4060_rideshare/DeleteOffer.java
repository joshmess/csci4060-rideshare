package edu.uga.cs.csci4060_rideshare;

import android.content.Intent;
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

public class DeleteOffer extends AppCompatActivity {

    public static final String DEBUG_TAG = "DeleteOffer";
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private EditText destLoc;
    private Button delete;
    private List<Ride> rideList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_ride);
        delete = findViewById(R.id.delete);
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String destToDelete = destLoc.getText().toString();
                System.out.println("DEST SELECTED::::" + destToDelete);

                Query delQuery = myRef.orderByChild("destinationLocation").equalTo(destToDelete);
                delQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot rideSnapshot: dataSnapshot.getChildren()) {
                            rideSnapshot.getRef().removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Show a quick confirmation
                                    Toast.makeText(getApplicationContext(), "Ride deleted: " + destToDelete,
                                            Toast.LENGTH_SHORT).show();

                                    // Clear the EditTexts for next use.
                                    destLoc.setText("");
                                }
                            })
                                    .addOnFailureListener( new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText( getApplicationContext(), "Failed to delete a ride for " + destToDelete,
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
