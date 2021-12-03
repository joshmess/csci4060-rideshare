package edu.uga.cs.csci4060_rideshare;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all rides.
 */
public class RideRecyclerAdapter extends RecyclerView.Adapter<RideRecyclerAdapter.RideHolder> {

    public static final String DEBUG_TAG = "RideRecyclerAdapter";

    private List<Ride> rideList;

    public RideRecyclerAdapter(List<Ride> rideList ) {
        this.rideList = rideList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class RideHolder extends RecyclerView.ViewHolder {

        TextView destination, departure, time;

        public RideHolder(View itemView ) {
            super(itemView);

            destination = itemView.findViewById( R.id.destinationAddress );
            departure = itemView.findViewById( R.id.departureAddress );
            time = itemView.findViewById( R.id.departureTime );
        }
    }

    @Override
    public RideHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.ride, parent, false );
        return new RideHolder( view );
    }

    // This method fills in the values of the Views to show a Ride
    @Override
    public void onBindViewHolder(RideHolder holder, int position ) {
        Ride ride = rideList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + ride );

        holder.destination.setText( ride.getDestinationLocation());
        holder.departure.setText( ride.getPickupLocation() );
        holder.time.setText( ride.getDepartureTime() );
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }
}
