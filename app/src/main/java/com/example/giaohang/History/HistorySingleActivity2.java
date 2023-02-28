package com.example.giaohang.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaohang.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class HistorySingleActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    String database= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private String rideId,customerId,currentUserId,driverId,userDriverorCustomer;
    private TextView mPickup;
    private TextView mDestination;
    private TextView mPrice;
    private TextView mCar;
    private TextView mDate;
    private TextView userName;
    private TextView userPhone;
    private ImageView userImage;
    private RatingBar mRatingBar;
    private DatabaseReference historyRideInfoDb;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rideId = getIntent().getExtras().getString("rideId2");
      //  rideId = getIntent().getStringExtra("rideId2");
        setContentView(R.layout.activity_history_single2);
        historyRideInfoDb = FirebaseDatabase.getInstance(database).getReference().child("history").child(rideId);
    //    mDestination = findViewById(R.id.destination_location);
    //    mPickup = findViewById(R.id.pickup_location);
     //   mCar = findViewById(R.id.car);
        mDate = findViewById(R.id.time_his);
      //  mPrice = findViewById(R.id.price);
        userName = findViewById(R.id.name_his);
        userPhone = findViewById(R.id.phone_his);
     //   TextView userMail = findViewById(R.id.email);
        userImage = findViewById(R.id.userImage_his);
        mRatingBar = findViewById(R.id.ratingBar2);
        currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        getRideInformation();
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        }
    }
    private void getRideInformation() {
        historyRideInfoDb.addListenerForSingleValueEvent(new ValueEventListener() {
          //  @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child:dataSnapshot.getChildren()){
                        if(child.getKey().equals("customer")){
                            customerId=child.getValue().toString();
                            if(!customerId.equals(currentUserId)){
                                userDriverorCustomer="Drivers";
                                getUserInformation("Customers",customerId);
                            }
                        }
                        if(child.getKey().equals("rating")){
                            mRatingBar.setRating(Integer.valueOf(child.getValue().toString()));
                        }
                        if(child.getKey().equals("driver")){
                            driverId=child.getValue().toString();
                            if(!driverId.equals(currentUserId)){
                                userDriverorCustomer="Customers";
                                getUserInformation("Drivers",driverId);
                                displayCustomerRelatiedObject();
                            }
                        }
                        if (child.getKey().equals("timestamp")) {
                            mDate.setText(getDate(Long.valueOf(child.getValue().toString())));
                        }
                    }
                    return;
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    private void displayCustomerRelatiedObject() {
        mRatingBar.setVisibility(View.VISIBLE);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                historyRideInfoDb.child("rating").setValue(rating);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance(database).getReference()
                        .child("Users").child("Drivers").child(driverId).child("rating");
                databaseReference.child(rideId).setValue(rating);

            }
        });
    }
    private void getUserInformation(String otherUserDriversOrCustomer, String otherUserId) {
        DatabaseReference mOtherUserDB=FirebaseDatabase.getInstance(database).getReference().child("Users")
                .child(otherUserDriversOrCustomer).child(otherUserId);
        mOtherUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map= (Map<String, Object>) snapshot.getValue();
                    if(map.get("name")!=null){
                        userName.setText(map.get("name").toString());
                    }
                    if(map.get("phone")!=null){
                        userPhone.setText(map.get("phone").toString());
                    }
                    if(map.get("profileImageUrl")!=null){
                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(userImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private String getDate(long timestamp) {
        //Long timestamp =System.currentTimeMillis()/1000;
        // Create a DateFormatter object for displaying date in specified
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp*1000);
        return formatter.format(calendar.getTime());
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
       // googleMap.setMapStyle(new MapStyleOptions(getResources()
         //       .getString(R.string.style_json)));
    }
}