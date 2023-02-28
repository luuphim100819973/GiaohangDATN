package com.example.giaohang.Objects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.text.format.DateFormat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.example.giaohang.R;

import com.example.giaohang.Utils.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Lưu info of the ride-
 * Post the ride request, records it,...
 */
public class RideObject  implements Cloneable{
    private String id;
    private float rideDistance = 0;
    private LocationObject pickup, current,destination;
    private String  requestService = "type_1", car = "--", cancelledReason;
    DatabaseReference rideRef,rideRef2;
    private DriverObject mDriver;
    private CustomerObject mCustomer;
    Activity activity;
    Boolean ended = false, customerPaid = false, driverPaid = false, cancelled = false;
    private double duration, distance, estimatedPrice;
    private Double ridePrice = 0.0;
    private float calculatedRideDistance, timePassed = 0;
    private Long timestamp;
    private int rating = 0, cancelledType = 0, state;
    String database_name= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    /**
     * id of the ride
     */
    public RideObject(Activity activity, String id){
        this.id = id;
        this.activity = activity;
    }
    public RideObject(){}

    public void parseData(DataSnapshot dataSnapshot){
        id = dataSnapshot.getKey();
        pickup = new LocationObject();
        destination = new LocationObject();
        if(dataSnapshot.child("pickup").child("name").getValue()!=null){
            pickup.setName(dataSnapshot.child("pickup").child("name").getValue().toString());
        }
        if(dataSnapshot.child("destination").child("name").getValue()!=null){
            destination.setName(dataSnapshot.child("destination").child("name").getValue().toString());
        }
        if(dataSnapshot.child("pickup").child("lat").getValue()!=null && dataSnapshot.child("pickup").child("lng").getValue()!=null){
            pickup.setCoordinates(
                    new LatLng(Double.parseDouble(dataSnapshot.child("pickup").child("lat").getValue().toString()),
                                Double.parseDouble(dataSnapshot.child("pickup").child("lng").getValue().toString())));
        }
        if(dataSnapshot.child("destination").child("lat").getValue()!=null && dataSnapshot.child("destination").child("lng").getValue()!=null){
            destination.setCoordinates(
                    new LatLng(Double.parseDouble(dataSnapshot.child("destination").child("lat").getValue().toString()),
                                Double.parseDouble(dataSnapshot.child("destination").child("lng").getValue().toString())));
        }
        if(dataSnapshot.child("service").getValue() != null){
            requestService = dataSnapshot.child("service").getValue().toString();
        }
        if(dataSnapshot.child("customerId").getValue() != null){
            mCustomer = new CustomerObject(dataSnapshot.child("customerId").getValue().toString());
        }
        if(dataSnapshot.child("driverId").getValue() != null){
            mDriver = new DriverObject(dataSnapshot.child("driverId").getValue().toString());
        }
        if(dataSnapshot.child("ended").getValue() != null){
            ended = Boolean.parseBoolean(dataSnapshot.child("ended").getValue().toString());
        }
        if(dataSnapshot.child("cancelled").getValue() != null){
            cancelled = Boolean.valueOf(dataSnapshot.child("cancelled").getValue().toString());
        }
        if(dataSnapshot.child("cancelled_info").child("type").getValue() != null){
            cancelled = true;
            cancelledType = Integer.parseInt(dataSnapshot.child("cancelled_info").child("type").getValue().toString());
        }
        if(dataSnapshot.child("cancelled_info").child("reason").getValue() != null){
            cancelledReason = dataSnapshot.child("cancelled_info").child("reason").getValue().toString();
        }
        if(dataSnapshot.child("state").getValue() != null){
            state = Integer.parseInt(dataSnapshot.child("state").getValue().toString());
        }
        if(dataSnapshot.child("timestamp").getValue() != null){
            timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());
        }
        if(dataSnapshot.child("price").getValue() != null){
            ridePrice = Double.valueOf(dataSnapshot.child("price").getValue().toString());
        }
        if(dataSnapshot.child("car").getValue() != null){
            car = dataSnapshot.child("car").getValue().toString();
        }
        if(dataSnapshot.child("customerPaid").getValue() != null){
            customerPaid = true;
        }
        if(dataSnapshot.child("driverPaidOut").getValue() != null){
            driverPaid = true;
        }
        if(dataSnapshot.child("rating").getValue() != null){
            rating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
        }
        if(dataSnapshot.child("calculated_duration").getValue() != null){
            duration = Double.parseDouble(dataSnapshot.child("calculated_duration").getValue().toString());
        }
        if(dataSnapshot.child("calculated_distance").getValue() != null){
            distance = Double.parseDouble(dataSnapshot.child("calculated_distance").getValue().toString());
        }
        if(dataSnapshot.child("calculated_Price").getValue() != null){
            estimatedPrice = Double.parseDouble(dataSnapshot.child("calculated_Price").getValue().toString());
        }
        rideRef = FirebaseDatabase.getInstance(database_name).getReference().child("ride_info").child(this.id);
        rideRef2 = FirebaseDatabase.getInstance(database_name).getReference().child("history").child(this.id);

        //Calculate the ride distance, by calculating the direct distance between pickup and destination
        if(this.pickup.getCoordinates() != null && this.destination.getCoordinates() != null){
            Location loc1 = new Location("");
            loc1.setLatitude(this.pickup.getCoordinates().latitude);
            loc1.setLongitude(this.pickup.getCoordinates().longitude);
            Location loc2 = new Location("");
            loc2.setLatitude(this.destination.getCoordinates().latitude);
            loc2.setLongitude(this.destination.getCoordinates().longitude);
            calculatedRideDistance = loc1.distanceTo(loc2) / 1000;
        }
    }
    /**
     * timestamp
     */
    public String getDate() {
        if(timestamp == null){return "--";}
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("MM-dd-yyyy, hh:mm", cal).toString();
        return date;
    }

    public DriverObject getDriver() {
        return mDriver;
    }
    public void setDriver(DriverObject mDriver) {
        this.mDriver = mDriver;
    }
    public CustomerObject getCustomer() {
        return mCustomer;
    }
    public void setCustomer(CustomerObject mCustomer) {
        this.mCustomer = mCustomer;
    }
    public LocationObject getPickup() {
        return pickup;
    }
    public void setPickup(LocationObject pickup) {
        this.pickup = pickup;
    }
    public LocationObject getCurrent() {
        return current;
    }
    public void setCurrent(LocationObject current) {
        this.current = current;
    }
    public LocationObject getDestination() {
        return destination;
    }
    public void setDestination(LocationObject destination) {
        this.destination = destination;
    }
    public String getRequestService() {
        return requestService;
    }
    public void setRequestService(String requestService) {
        this.requestService = requestService;
    }
    public float getRideDistance() {
        return rideDistance;
    }
    public void setRideDistance(float rideDistance) {
        this.rideDistance = rideDistance;
    }
    public void setTimePassed(float timePassed) {
        this.timePassed = timePassed;
    }
    public String getId() {
        return id;
    }
    public Boolean getEnded() {
        return ended;
    }
    public int getCancelledType() {
        return cancelledType;
    }
    public String getCancelledReason() {
        return cancelledReason;
    }
    public Double getRidePrice() {
        return ridePrice;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public Boolean getCustomerPaid() {
        return customerPaid;
    }
    public String getCar() {
        return car;
    }
    public int getRating() {
        return rating;
    }
    @SuppressLint("DefaultLocale")
    public String getPriceString(){
        return String.format("%.2f", ridePrice);
    }
    public Boolean getCancelled() {
        return cancelled;
    }
    public DatabaseReference getRideRef() {
        return rideRef;
    }
    public String getCalculatedRideDistance() {
        String s = String.valueOf(calculatedRideDistance);
        String s1 = s.substring(s.indexOf(".")+2).trim();
        return s.replace(s1, "") + " km";
    }
    public float getTimePassed() {
        return timePassed;
    }
    public int getCalculatedTime(){
        return (int) calculatedRideDistance * 100 / 60;
    }

    public int getState() {
        return state;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public double getDistance() {
        return distance;
    }
    public double getDuration() {
        return duration;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }
}
