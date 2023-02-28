package com.example.giaohang.Customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.example.giaohang.*;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.giaohang.Adapters.TypeAdapter;
import com.example.giaohang.Chat.ChatActivity;
//import com.example.giaohang.History.HistoryActivity;
import com.example.giaohang.History.HistoryActivity2;
import com.example.giaohang.Login.LauncherActivity;
import com.example.giaohang.Login.LoginFragment;
import com.example.giaohang.Objects.CustomerObject;
import com.example.giaohang.Objects.LocationObject;
import com.example.giaohang.Objects.RideObject;
import com.example.giaohang.Objects.TypeObject;
import com.example.giaohang.SettingsActivity;
import com.example.giaohang.UsersActivity;
import com.example.giaohang.Utils.Utils;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.logicbeanzs.uberpolylineanimation.MapAnimator;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * màn hình chính cho customer
 */
public class CustomerMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback
        {
    int TIMEOUT_MILLISECONDS = 20000,
            CANCEL_OPTION_MILLISECONDS = 10000;
    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
 //   private SlideToActView mRequest;
    private LocationObject pickupLocation, currentLocation, destinationLocation ;
    private Boolean requestBol = false;
    int bottomSheetStatus = 1;
    private Marker destinationMarker, pickupMarker;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private LinearLayout mDriverInfo;
    private ImageView mDriverProfileImage;
    private Button mRequestBtn;
    private Button mHistoryBtn;
    private LatLng pickupLocation_cust,destination_cust;
    private TextView mDriverName;
    private TextView mDriverCar;
    private TextView mDriverLicense;
    private TextView mRatingText;
    //thu hoi request
    private Boolean requestBool=false;
    FloatingActionButton mCallDriver, mMessage;
    FloatingActionButton mCancel;

    double lat1;
    double long1;
    DrawerLayout drawer;
    RideObject mCurrentRide;
    private TypeAdapter mAdapter;
    ArrayList<TypeObject> typeArrayList = new ArrayList<>();
    private Boolean driverFound = false;
    private ValueEventListener driveHasEndedRefListener;

    private Marker maker_pick_me_here_cust_map;
    public String database= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FloatingActionButton Image_Show_Driver_Map;
    private  TextView mLogout,mHistory_navigation_fake_cust;
    private String location_stop_request;

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_customer);

        Toolbar toolbar = findViewById(R.id.main_page_toolbar_customer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mCurrentRide = new RideObject(CustomerMapActivity.this, null);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserData();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        destination_cust= new LatLng(0.0,0.0);
        Image_Show_Driver_Map=findViewById(R.id.show_drivers_loc);
        mDriverInfo = findViewById(R.id.driverInfo);
      //  mRadioLayout = findViewById(R.id.radioLayout);
        mDriverProfileImage = findViewById(R.id.driverProfileImage);
        mDriverName = findViewById(R.id.driverName);
        mDriverCar = findViewById(R.id.driverCar);
        mDriverLicense = findViewById(R.id.driverPlate);
        mRequestBtn=findViewById(R.id.id_request_btn);
        mHistoryBtn=findViewById(R.id.history_btn);
        mCallDriver = findViewById(R.id.phone_to_driver_from_cus);
        mMessage = findViewById(R.id.message_from_cus);
        mRatingText = findViewById(R.id.ratingText);
        mLogout = findViewById(R.id.logout_cust);
        mHistory_navigation_fake_cust = findViewById(R.id.history_navigation_fake_cust);
        mCancel = findViewById(R.id.cancel);
        mLogout.setOnClickListener(v -> logOut());
        mHistory_navigation_fake_cust.setOnClickListener(view -> {
            Intent intent= new Intent(CustomerMapActivity.this, HistoryActivity2.class);
            intent.putExtra("customerOrDriver","Customers");
            startActivity(intent);

        });
        mRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestBool){
                    endRide2();
                }else{
                    requestBool=true;
                    String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref= FirebaseDatabase.getInstance(database).getReference("customerRequest");
                    GeoFire geoFire=new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                    pickupLocation_cust =new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    if(maker_pick_me_here_cust_map != null){
                        maker_pick_me_here_cust_map.remove();
                    }
                    maker_pick_me_here_cust_map=mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.man)).position(pickupLocation_cust).title("Pick me here"));

                    mRequestBtn.setText("Đang tìm tài xế....");
                    getCloserDriver();
                }
            }
        });
        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CustomerMapActivity.this, HistoryActivity2.class);
                intent.putExtra("customerOrDriver","Customers");
                startActivity(intent);
            }
        });
        mCallDriver.setOnClickListener(view -> {
            if (mCurrentRide == null) {
                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mCurrentRide.getDriver().getPhone()));
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.no_phone_call_permissions), Snackbar.LENGTH_LONG).show();
            }
        });
        ImageView mDrawerButton = findViewById(R.id.drawerButton);
        mDrawerButton.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

        getHasRideEnded2();
        getHasRideEnded4();
    }
    private int radius=1;
    private Boolean driverFound_cust=false;
    private String driverFoundId_cust;
    DatabaseReference driverLocation;
    GeoQuery geoQuery;
    private void getCloserDriver() {
        driverLocation=FirebaseDatabase.getInstance(database).getReference()
                .child("driversWorking");
        GeoFire geoFire=new GeoFire(driverLocation);
        geoQuery=geoFire.queryAtLocation(new GeoLocation(pickupLocation_cust.latitude,pickupLocation_cust.longitude),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!driverFound_cust && requestBool){
                    driverFound_cust=true;
                    driverFoundId_cust=key;
                    DatabaseReference diverRef= FirebaseDatabase.getInstance(database).getReference()
                            .child("Users").child("Drivers").child(driverFoundId_cust).child("customerRequest");
                    String customerId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map =new HashMap();
                    map.put("customerRideId",customerId);
                    String current_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference location_ref= FirebaseDatabase.getInstance(database).getReference()
                            .child("Users").child("Customers").child(current_id);
                    location_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                location_stop_request = dataSnapshot.child("locationStop").getValue().toString();
                                //geoLocate_stop(location_stop[0]);
                                try {
                                    Geocoder geocoder = new Geocoder(CustomerMapActivity.this, Locale.getDefault());
                                    Address address ;
                                    List<Address> addressList = geocoder.getFromLocationName(location_stop_request, 1);
                                    while (addressList.size()==0) {
                                        addressList = geocoder.getFromLocationName(location_stop_request, 1);
                                    }
                                   if (addressList != null && addressList.size() > 0) {
                                        address = addressList.get(0);
                                         lat1 = address.getLatitude();
                                         long1 = address.getLongitude();
                                       map.put("destination",location_stop_request);
                                       map.put("destinationLat",lat1);
                                       map.put("destinationLng",long1);
                                       diverRef.updateChildren(map);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    map.put("destination",location_stop_request);
                    map.put("destinationLat",lat1);
                    map.put("destinationLng",long1);
                    diverRef.updateChildren(map);

                    getDriverLocation2();
                    getDriverInfo();
                    getHasRideEnded();
                 //   getHasRideEnded2();

                    mRequestBtn.setText("Đang tìm vị trí tài xế");
                }
            }
            @Override
            public void onKeyExited(String key) {
            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }
            @Override
            public void onGeoQueryReady() {
                if(!driverFound_cust){
                    radius++;
                    getCloserDriver();
                }
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }



    private Address geoLocate_stop(String userInput) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList = geocoder.getFromLocationName(userInput, 3);
        Address address= null ;
        if (addressList.size() > 0) {
            address = addressList.get(0);
            double lat1 = address.getLatitude();
            double long1 = address.getLongitude();
            List<Address> addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1);
            String address3 = addresses.get(0).getAddressLine(0);
            //   mLocationStop.setText(address3);
        }
        return address;
    }
    private void getDriverInfo2() {
    }
    private Marker mDriverMarker2;
   // DatabaseReference driverLocationRef;
 //   ValueEventListener driverLocationRefListener2;
    private void getDriverLocation2() {
        driverLocationRef= FirebaseDatabase.getInstance(database).getReference()
                .child("driversWorking").child(driverFoundId_cust).child("l");
        driverLocationRefListener=driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && requestBool){
                    List<Object> map=(List<Object>) snapshot.getValue();
                    double locationLat =0;
                    double locationLng =0;
                    mRequestBtn.setText("Driver Found");
                    if(map.get(0)!=null){
                        locationLat=Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1)!=null){
                        locationLng=Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng= new LatLng(locationLat,locationLng);

                    if(mDriverMarker2 != null){
                        mDriverMarker2.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation_cust.latitude);
                    loc1.setLongitude(pickupLocation_cust.longitude);
                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);
                    float distance = loc1.distanceTo(loc2);
                    distance=distance;
                    mRequestBtn.setText("Tài xế cách:"+String.valueOf(df.format(distance))+"mét");
                    Image_Show_Driver_Map.setOnClickListener(view -> {
                        Intent intent = new Intent(CustomerMapActivity.this, WatchDriverActivity.class);
                        intent.putExtra("id_driver_working",driverFoundId_cust);
                        CustomerMapActivity.this.startActivity(intent);
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     // Khởi tạo recycleview hiển thị cho customer loại car,bike
   /* private void initRecyclerView() {
        typeArrayList = Utils.getTypeList(CustomerMapActivity.this);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CustomerMapActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TypeAdapter(typeArrayList, CustomerMapActivity.this, routeData);
        mRecyclerView.setAdapter(mAdapter);
    }*/
    private DatabaseReference driveHasEndedRef;
    private DatabaseReference check1Ref;
    ValueEventListener check1RefListerner;
    ValueEventListener check2RefListerner;
    private DatabaseReference check2Ref;
     // Kết thúc ride

    private void getHasRideEnded4() {
        String this_customer_uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        driveHasEndedRef= FirebaseDatabase.getInstance(database).getReference()
                .child("customerRequestAgain").child(this_customer_uid).child("state");
        driveHasEndedRefListener=driveHasEndedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getValue().equals("1"))
                    mDriverInfo.setVisibility(View.VISIBLE);
                }
                //else mDriverInfo.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getHasRideEnded2() {
        String this_customer_uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        /*driveHasEndedRef= FirebaseDatabase.getInstance(database).getReference()
                .child("customerRequestAgain").child(this_customer_uid).child("state");*/
        driveHasEndedRef= FirebaseDatabase.getInstance(database).getReference()
                .child("customerRequestAgain").child(this_customer_uid);
        driveHasEndedRefListener=driveHasEndedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("state").exists()&&snapshot.child("state").getValue().toString().equals("0")){
                    mDriverInfo.setVisibility(View.GONE);
                }
                if(snapshot.child("state").exists()&&snapshot.child("state").getValue().toString().equals("2")){

                    DatabaseReference driverRef= FirebaseDatabase.getInstance(database).getReference()
                            .child("customerRequestAgain").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    driverRef.removeValue();
                    mDriverInfo.setVisibility(View.GONE);
                    //endRide2();
                    if(driveHasEndedRef!=null){
                        driveHasEndedRef.removeEventListener(driveHasEndedRefListener);
                    }


                }
                if(snapshot.child("state").exists()&&snapshot.child("state").getValue().toString().equals("1")){
                    if(snapshot.child("from").exists()){
                        //Snackbar.make(findViewById(R.id.drawer_layout), "thu nghiem", Snackbar.LENGTH_LONG).show();
                        String uid_driver_request_again=snapshot.child("from").getValue().toString();
                        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database).getReference()
                                .child("Users").child("Drivers").child(uid_driver_request_again);
                        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String name_chat=dataSnapshot.child("name").getValue().toString();
                                    mMessage.setOnClickListener(view -> {
                                        Intent intent=new Intent(CustomerMapActivity.this, ChatActivity.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("customerOrDriver","Customers");
                                        extras.putString("user_id_chat",uid_driver_request_again);
                                        extras.putString("user_name",name_chat);
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                    });
                                    String token_id = dataSnapshot.child("token_id").getValue().toString();
                                /*FCMsend.FirebaseMessagingService(DriverMapActivity.this,
                                token_id, "New Order", uId);*/
                                    FCMsend.FirebaseMessagingService(CustomerMapActivity.this,
                                            token_id
                                            , "New Order", "Bạn có đơn mới");
                                    //if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                                    Map<String,Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                    if(map.get("name")!= null){
                                        mDriverName.setText(map.get("name").toString());
                                    }
                                    if(map.get("car")!= null){
                                        mDriverCar.setText(map.get("car").toString());
                                    }
                                    if(map.get("phone")!= null){
                                        //mDriverCar.setText(map.get("car").toString());
                                        String phone_driver= map.get("phone").toString();
                                        mCallDriver.setOnClickListener(view -> {
                                            if (mCurrentRide == null) {
                                                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
                                            }
                                            if (ContextCompat.checkSelfPermission(CustomerMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_driver));
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if(map.get("lincense")!= null){
                                        mDriverLicense.setText(map.get("license").toString());
                                    }
                                    if(dataSnapshot.child("profileImageUrl")!=null){
                                        Glide.with(getApplication())
                                                .load(dataSnapshot.child("profileImageUrl").getValue().toString())
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(mDriverProfileImage);

                                    }
                                    int ratingSum = 0;
                                    float ratingsTotal = 0;
                                    float ratingsAvg =0;
                                    for (DataSnapshot child : dataSnapshot.child("rating").getChildren()) {
                                        ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
                                        ratingsTotal++;
                                    }
                                    if(ratingsTotal!=0){
                                        ratingsAvg=ratingSum/ratingsTotal;
                                    }
                                    DecimalFormat df = new DecimalFormat("#.#");
                                    df.format(ratingsAvg);
                                    mRatingText.setText(String.valueOf(ratingsAvg));
                                }
                                mDriverInfo.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });

                        //endRide2();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getHasRideEnded() {
        //check jet thuc
        check1Ref= FirebaseDatabase.getInstance(database ).getReference()
                .child("Users").child("Drivers").child(driverFoundId_cust).child("customerRequest").child("customerRideId");
        check1RefListerner=check1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                check2Ref= FirebaseDatabase.getInstance(database ).getReference()
                        .child("Users").child("Customers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state_request");
                check2RefListerner=check2Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot3) {
                        if(snapshot3.exists()){
                            String state=snapshot3.getValue().toString();
                            if(state.equals("1")&&(!snapshot2.exists())){
                                endRide2();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    /**
     * Lấy user info và đổ vào navigation
     */
    private void getUserData() {
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance(database).getReference()
                .child("Users").child("Customers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    CustomerObject mCustomer = new CustomerObject();
                    mCustomer.parseData(dataSnapshot);
                    TextView mUsername = header.findViewById(R.id.usernameDrawer);
                    ImageView mProfileImage = header.findViewById(R.id.imageViewDrawer);
                    mUsername.setText(mCustomer.getName());
                    if (!mCustomer.getProfileImage().equals("default"))
                        Glide.with(getApplication()).load(mCustomer.getProfileImage()).apply(RequestOptions.circleCropTransform()).into(mProfileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Marker mDriverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;
    private void getDriverLocation() {
        if (mCurrentRide.getDriver().getId() == null) {
            return;
        }
        driverLocationRef = FirebaseDatabase.getInstance(database).getReference()
                .child("driversWorking").child(mCurrentRide.getDriver().getId()).child("l");
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    if(map == null){
                        return;
                    }
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LocationObject mDriverLocation = new LocationObject(new LatLng(locationLat, locationLng), "");
                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.getCoordinates().latitude);
                    loc1.setLongitude(pickupLocation.getCoordinates().longitude);
                    Location loc2 = new Location("");
                    loc2.setLatitude(mDriverLocation.getCoordinates().latitude);
                    loc2.setLongitude(mDriverLocation.getCoordinates().longitude);
                    float distance = loc1.distanceTo(loc2);
                    if (distance < 100) {
                      //  mRequest.setText(getResources().getString(R.string.driver_here));
                    } else {
                      //  mRequest.setText(getResources().getString(R.string.driver_found));
                    }
                    mCurrentRide.getDriver().setLocation(mDriverLocation);
                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(mCurrentRide.getDriver().getLocation()
                            .getCoordinates()).title("your driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });

    }
    private void getDriverInfo() {
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database).getReference()
                .child("Users").child("Drivers").child(driverFoundId_cust);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name_chat=dataSnapshot.child("name").getValue().toString();
                    mMessage.setOnClickListener(view -> {

                        Intent intent=new Intent(CustomerMapActivity.this, ChatActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("customerOrDriver","Customers");
                        extras.putString("user_id_chat",driverFoundId_cust);
                        extras.putString("user_name",name_chat);

                        intent.putExtras(extras);
                        startActivity(intent);
                    });
                    String token_id = dataSnapshot.child("token_id").getValue().toString();
                     /*FCMsend.FirebaseMessagingService(DriverMapActivity.this,
                            token_id, "New Order", uId);*/
                    FCMsend.FirebaseMessagingService(CustomerMapActivity.this,
                            token_id
                            , "New Order", "Bạn có đơn mới");
                    //if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                    Map<String,Object> map =(Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!= null){
                        mDriverName.setText(map.get("name").toString());
                    }
                    if(map.get("car")!= null){
                        mDriverCar.setText(map.get("car").toString());
                    }
                    if(map.get("phone")!= null){
                        //mDriverCar.setText(map.get("car").toString());
                        String phone_driver= map.get("phone").toString();
                        mCallDriver.setOnClickListener(view -> {
                            if (mCurrentRide == null) {
                                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
                            }
                            if (ContextCompat.checkSelfPermission(CustomerMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_driver));
                                startActivity(intent);
                            }
                        });

                    }
                    if(map.get("lincense")!= null){
                        mDriverLicense.setText(map.get("license").toString());
                    }
                    if(dataSnapshot.child("profileImageUrl")!=null){
                        Glide.with(getApplication())
                                .load(dataSnapshot.child("profileImageUrl").getValue().toString())
                                .apply(RequestOptions.circleCropTransform())
                                .into(mDriverProfileImage);

                    }
                    int ratingSum = 0;
                    float ratingsTotal = 0;
                    float ratingsAvg =0;
                    for (DataSnapshot child : dataSnapshot.child("rating").getChildren()) {
                        ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
                        ratingsTotal++;
                    }
                    if(ratingsTotal!=0){
                        ratingsAvg=ratingSum/ratingsTotal;
                    }
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.format(ratingsAvg);
                    mRatingText.setText(String.valueOf(ratingsAvg));
                }
                mDriverInfo.setVisibility(View.VISIBLE);
            }


            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    private void endRide2() {
        requestBool=false;
        //geoQuery.removeAllListeners();
        if(check1RefListerner!=null){
            check1Ref.removeEventListener(check1RefListerner);
        }
        if(check2RefListerner!=null){
            check2Ref.removeEventListener(check2RefListerner);
        }

        if(driveHasEndedRefListener!=null){
            driveHasEndedRef.removeEventListener(driveHasEndedRefListener);}

        if(driverLocationRefListener!=null){
            driverLocationRef.removeEventListener(driverLocationRefListener);
        }
        if(driverFoundId_cust!= null){

            DatabaseReference driverRef= FirebaseDatabase.getInstance(database).getReference()
                    .child("Users").child("Drivers").child(driverFoundId_cust).child("customerRequest");
            driverRef.removeValue();
            driverFoundId_cust=null;
        }
        driverFound_cust=false;
        radius=1;
        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref =FirebaseDatabase.getInstance(database).getReference("customerRequest");
        GeoFire geoFire= new GeoFire(ref);
        geoFire.removeLocation(userId);
        if(maker_pick_me_here_cust_map!= null){
            maker_pick_me_here_cust_map.remove();
        }
        mRequestBtn.setText("Tìm Tài xế ");
       // bringBottomSheetDown();
        getDriversAround2();
        mDriverInfo.setVisibility(View.GONE);
        mDriverInfo.clearAnimation();
        mDriverName.setText("");
        mDriverCar.setText("");
        mDriverLicense.setText("");
        zoomUpdated = false;
        //    mMoney.setText("");
    }
    /**
     * Tìm và update vị trí user
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance(database).getReference()
                        .child("Users").child("Customers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                assignedCustomerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name1= snapshot.child("name").getValue().toString();
                        String location_start1 = snapshot.child("profileImageUrl").getValue().toString();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });
                return false;
            }
        });
    }
    boolean zoomUpdated = false;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplication() != null) {
                    mLastLocation =location;
                    currentLocation = new LocationObject(new LatLng(location.getLatitude(), location.getLongitude()), "");
                    mCurrentRide.setCurrent(currentLocation);
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (!zoomUpdated) {
                        float zoomLevel = 17.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                        zoomUpdated = true;
                    }
                    //getDriverInfoAgainHandPick();
                    //gọi function 1 lần
                    if (!getDriversAroundStarted){
                        getDriversAround2();
                    }
                }
            }
        }
    };
    /**
     * requestCode -> the number assigned to the request.
     * Mỗi request có 1 request code riêng
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat
                                .requestPermissions(CustomerMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.CALL_PHONE}, 1))
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(CustomerMapActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }
    public static Bitmap generateBitmap(Context context, String location, String duration) {
        Bitmap bitmap = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = new RelativeLayout(context);
        try {
            mInflater.inflate(R.layout.item_marker, view, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView locationTextView = (TextView) view.findViewById(R.id.location);
        TextView durationTextView = (TextView) view.findViewById(R.id.duration);
        locationTextView.setText(location);
        if(duration != null){
            durationTextView.setText(duration);
        }else{
            durationTextView.setVisibility(View.GONE);
            durationTextView.clearAnimation();
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }
    public ArrayList<Double> parseJson(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        long totalDistance = 0;
        int totalSeconds = 0;
        try {
            jRoutes = jObject.getJSONArray("routes");
            /* Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                /* Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    totalDistance = totalDistance + Long.parseLong(jDistance.getString("value"));
                    /** Getting duration from the json data */
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    totalSeconds = totalSeconds + Integer.parseInt(jDuration.getString("value"));
                }
            }
            double dist = totalDistance / 1000.0;
            Log.d("distance", "Calculated distance:" + dist);
            int days = totalSeconds / 86400;
            int hours = (totalSeconds - days * 86400) / 3600;
            int minutes = (totalSeconds - days * 86400 - hours * 3600) / 60;
            int seconds = totalSeconds - days * 86400 - hours * 3600 - minutes * 60;
            Log.d("duration", days + " days " + hours + " hours " + minutes + " mins" + seconds + " seconds");
            ArrayList<Double> list = new ArrayList<Double>();
            list.add(dist);
            list.add((double) totalSeconds);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getApplication(), "Please provide the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    //gọi funciton 1 lần
    boolean getDriversAroundStarted = false;
    List<Marker> markerList = new ArrayList<Marker>();
    /**
     * Hiển thị drivers xung quanh
     * cập nhật vị trí real time.
     */
    //List<Marker> markerList2 = new ArrayList<Marker>();
    private void getDriversAround2(){
        getDriversAroundStarted = true;
        DatabaseReference driversLocation = FirebaseDatabase.getInstance(database).getReference().child("driversWorking");
        GeoFire geoFire = new GeoFire(driversLocation);
        //Lấy định vị của driver làm gốc, tìm xung quanh bán kính của driver 10000km
        GeoQuery geoQuery = geoFire
                .queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 10000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            //lay tat ca data khi lần đầu tiên ta gọi listener
            //gọi khi data trong firebase thay đổi màu xanh
            public void onKeyEntered(String key, GeoLocation location) {
                //vòng for kiêm tra chắc chắn ta ko add các marker trùng nhau
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key))
                        return;
                }
              //  checkDriverLastUpdated(key);
                //cho all key của các driver vào markerlist
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
                Marker mDriverMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                        .position(driverLocation).title(key));
                mDriverMarker.setTag(key);
                markerList.add(mDriverMarker);

            }
            //khi driver stop woking
            @Override
            public void onKeyExited(String key) {
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    //xóa marker khỏi markerlist khi drivers ngoài bán kính quét
                    if (markerIt.getTag().equals(key)) {
                        markerIt.remove();
                        markerList.remove(markerIt);
                        return;
                    }
                }
            }
            //neu tài xe đang di chuyển, lấy key id của driver và thay đổi vị trí các marker liên tục
            //trên bản đồ
            //gọi khi data trong firebase thay đổi màu vàng
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key)) {
                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                        return;
                    }
                }
               // checkDriverLastUpdated(key);
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
                Marker mDriverMarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(driverLocation).title(key));
                mDriverMarker.setTag(key);
                markerList.add(mDriverMarker);
            }
            @Override
            public void onGeoQueryReady() {
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });

    }
    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(CustomerMapActivity.this, LauncherActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Show map với marker đi và đến,
     * biến route - route giữa đi và đến
     */
    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (bottomSheetStatus == 2) {
                bottomSheetStatus = 0;
            } else {
                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_right_driver, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()== android.R.id.home) {
            Intent intent = new Intent(this, LoginFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        if(item.getItemId()==R.id.main_new_order){
            Intent order_intent = new Intent(this, LoginFragment.class);
            startActivity(order_intent);
            finish();
        }
        if(item.getItemId()==R.id.main_setting_btn){
            Intent setting_intent = new Intent(this, SettingsActivity.class);
            startActivity(setting_intent);
            finish();
        }
        if(item.getItemId()==R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
        }
        if(item.getItemId()==R.id.main_all_users_btn){
            Intent setting_intent = new Intent(this, UsersActivity.class);
            startActivity(setting_intent);
            finish();
        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.history) {
        /*//    Intent intent = new Intent(CustomerMapActivity.this, HistoryActivity.class);
            intent.putExtra("customerOrDriver", "Customers");
            startActivity(intent);*/
        } else if (id == R.id.request) {
            Intent intent = new Intent(CustomerMapActivity.this, CustomerSettingsRequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.payment) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
