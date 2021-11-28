package edu.uga.cs.csci4060_rideshare;


/**
 * This class represents a single ride, including its status as an offer or request, accepted or
 * unaccepted, confirmed by all parties or not confirmed, accepting driver's id / email,
 * point value, and destination, pickup location, and departure time.
 */
public class Ride {
    private Boolean isOffer;                // else isRequest
    private Boolean isAccepted;             // else isUnaccepted
    private Boolean isConfirmed;            // else isUnconfirmed
    private String driverID;
    private Double pointValue;              // 50 in-town, 100 out-of-town
    private String destinationLocation;
    private String pickupLocation;
    private String departureTime;

    public Ride()
    {
        this.isOffer = null;
        this.isAccepted = false;
        this.isConfirmed = false;
        this.driverID = null;
        this.pointValue = null;
        this.destinationLocation = null;
        this.pickupLocation = null;
        this.departureTime = null;
    }

    public Ride( Boolean offer, Double points, String destination, String pickup, String time ) {
        this.isOffer = offer;
        this.pointValue = points;
        this.destinationLocation = destination;
        this.pickupLocation = pickup;
        this.departureTime = time;
    }

    public Boolean getIsOffer() {
        return isOffer;
    }

    public void setIsOffer(Boolean offer) {
        this.isOffer = offer;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String id) {
        driverID = id;
    }

    public Double getPointValue() {
        return pointValue;
    }

    public void setPointValue(Double pointValue) {
        this.pointValue = pointValue;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destination) {
        this.destinationLocation = destination;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    //public String toString() {return companyName + " " + phone + " " + url + " " + comments;}
}
