package com.example.giaohang.Objects;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;

import java.text.DecimalFormat;
/**
 * info of customer user request
 */
public class CustomerObject {
    private String id = "";
    private String name = "";
    private String name_person_destination = "";
    private String phone_start = "";
    private String phone_stop = "";
    private String profileImage = "default";
    private String notificationKey = "";
    private String mLocationStart = "";
    private String mLocationStop = "";
    private String product_name = "";
    private String product_quantity = "";
    private String product_rate = "";
    private String mCalculateDistance="";
    private String mCalculateFee="";

    private String mMoney = "";
    private String mNote = "";

    private float ratingsAvg = 0;
    public CustomerObject(String id) {
        this.id = id;
    }

    public CustomerObject() {
    }
    /**
     biến dataSnapshot - lấy customer info từ the database
     */
    public void parseData(DataSnapshot dataSnapshot) {
        id = dataSnapshot.getKey();
        if (dataSnapshot.child("name").getValue() != null) {
            name = dataSnapshot.child("name").getValue().toString();
        }
        if (dataSnapshot.child("name_person_destination").getValue() != null) {
            name_person_destination = dataSnapshot.child("name_person_destination").getValue().toString();
        }
        if (dataSnapshot.child("phone_start").getValue() != null) {
            phone_start = dataSnapshot.child("phone_start").getValue().toString();
        }
        if (dataSnapshot.child("phone_stop").getValue() != null) {
            phone_stop = dataSnapshot.child("phone_stop").getValue().toString();
        }

        if (dataSnapshot.child("profileImageUrl").getValue() != null) {
            profileImage = dataSnapshot.child("profileImageUrl").getValue().toString();
        }
        if (dataSnapshot.child("notificationKey").getValue() != null) {
            notificationKey = dataSnapshot.child("notificationKey").getValue().toString();}

        if (dataSnapshot.child("locationStart").getValue() != null) {
                mLocationStart = dataSnapshot.child("locationStart").getValue().toString();
            }

        if (dataSnapshot.child("locationStop").getValue() != null) {
                mLocationStop = dataSnapshot.child("locationStop").getValue().toString();
            }
        if (dataSnapshot.child("productName").getValue() != null) {
                product_name = dataSnapshot.child("productName").getValue().toString();
            }
        if (dataSnapshot.child("productRate").getValue() != null) {
                product_rate = dataSnapshot.child("productRate").getValue().toString();
            }
        if (dataSnapshot.child("productQuantity").getValue() != null) {
                product_quantity = dataSnapshot.child("productQuantity").getValue().toString();
            }

        if (dataSnapshot.child("note").getValue() != null) {
                mNote = dataSnapshot.child("note").getValue().toString();
            }
        if (dataSnapshot.child("calculateDistance").getValue() != null) {
            mCalculateDistance = dataSnapshot.child("calculateDistance").getValue().toString();
        }
        if (dataSnapshot.child("shipFee").getValue() != null) {
            mCalculateFee = dataSnapshot.child("shipFee").getValue().toString();
        }
        int ratingSum = 0;
        float ratingsTotal = 0;
        for (DataSnapshot child : dataSnapshot.child("rating").getChildren()) {
                ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
                ratingsTotal++;
        }
        if (ratingsTotal != 0) {
                ratingsAvg = ratingSum / ratingsTotal;
        }
    }
    public String getRatingString () {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(ratingsAvg);
    }
    public String getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public void setName (String name){
        this.name = name;
    }


    public String getProfileImage () {
        return profileImage;
    }
    public void setProfileImage (String profileImage){
        this.profileImage = profileImage;
    }
    public String getNotificationKey () {
        return notificationKey;
    }
    public String getPhone_start() {
        return phone_start;
    }

    public void setPhone_start(String phone_start) {
        this.phone_start = phone_start;
    }

    public String getPhone_stop() {
        return phone_stop;
    }

    public void setPhone_stop(String phone_stop) {
        this.phone_stop = phone_stop;
    }
    public String getmLocationStart () {
        return mLocationStart;
    }
    public String getmLocationStop () {
        return mLocationStop;
    }
    public String getmMoney () {
        return mMoney;
    }
    public String getmNote () {
        return mNote;
    }
    public void setmLocationStart (String mLocationStart){
        this.mLocationStart = mLocationStart;
    }
    public void setmLocationStop (String mLocationStop){
        this.mLocationStop = mLocationStop;
    }
    public void setmMoney (String mMoney){
        this.mMoney = mMoney;
    }
    public void setmNote (String mNote){
        this.mNote = mNote;
    }

    public String getName_person_destination() {
        return name_person_destination;
    }

    public void setName_person_destination(String name_person_destination) {
        this.name_person_destination = name_person_destination;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_rate() {
        return product_rate;
    }

    public void setProduct_rate(String product_rate) {
        this.product_rate = product_rate;
    }

    public String getmCalculateDistance() {
        return mCalculateDistance;
    }

    public void setmCalculateDistance(String mCalculateDistance) {
        this.mCalculateDistance = mCalculateDistance;
    }

    public String getmCalculateFee() {
        return mCalculateFee;
    }

    public void setmCalculateFee(String mCalculateFee) {
        this.mCalculateFee = mCalculateFee;
    }
}

