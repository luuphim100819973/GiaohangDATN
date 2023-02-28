package com.example.giaohang;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button mStatusBtn;
    private TextInputLayout mStatusInput;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private EditText mTitle,mMessage;
  // private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mTitle=findViewById(R.id.mTitle);
        mMessage=findViewById(R.id.mMessage);
        findViewById(R.id.mButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=mTitle.getText().toString().trim();
                String mesage=mMessage.getText().toString().trim();
                if(!title.equals("")&&!mesage.equals("")){
                    FCMsend.FirebaseMessagingService(StatusActivity.this,
                                  "egLxL23qTvCOHPDYJcEJxd:APA91bETsfGcG3dc0M84NVIc_nLL9KykTKokm6QAO5r0-ecqPmPmEFFaA45oqwOdHjNA8ZoszvNwL1x2AebMZrn65efJdbAZeeRjojDU4MBd_nCc0gUGqD3kltCqQyKdtGfWx3aZPJCc",
                       //     "enDS1PRyTqm_S5te4ZzzTn:APA91bEazSNOWBePnEUxQFNKnV6xS-8NWCMO0W5xqtsXyPPAmEnTveaaCh-yr8X0LY4MgHZqvAACXz-D9S1Aw5bTZrTTZoG65ZXenYaXB4RzHLegT28UjBDuFHBCA9KswLWKPdajtwco",
                       //     "c3ywzpF-RGS1_tKxTmEVFs:APA91bGFre9DiUCSrSeTUOhzRe5t2ppdT1FJsStJdmrkfkPKeMzmWlNJJKJqOtqpTRSttqy7dDsP8I0TMFJa3H1oYeiZzcXSYfEiGfx5__CgqZrVDld26PSlOUliJJtxB43QGlegeCUl",
                            title,
                            mesage);
                }

            }
        });
  //      mDialog = new ProgressDialog(StatusActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(StatusActivity.this);
   //     builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.app_bar_layout_driver);
        AlertDialog dialog = builder.create();
        mToolbar =(Toolbar) findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String get_status_exta= getIntent().getStringExtra("status_link");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mUser.getUid();
        //https://lapitchat-app-da5c4-default-rtdb.asia-southeast1.firebasedatabase.app
       // https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app
        mDatabase = FirebaseDatabase.getInstance("https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Drivers").child(current_uid);
        mStatusBtn =(Button) findViewById(R.id.status_save_btn) ;
        mStatusInput= (TextInputLayout) findViewById(R.id.status_input);
        mStatusInput.getEditText().setText(get_status_exta);
        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mDialog.setTitle("Save Status");
                mDialog.setMessage("Saving Status. Please wait!!! Dont be angry.Fuck!!! ");
                mDialog.show();*/
                dialog.show();
                String status =mStatusInput.getEditText().getText().toString();
                mDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "Some error. Fuck!!", Toast.LENGTH_LONG);
                        }
                    }
                });

            }
        });
    }
}
/*
package com.example.giaohang.Driver;
        import android.Manifest;
        import android.content.ActivityNotFoundException;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.location.Location;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Looper;
        import android.view.Gravity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewTreeObserver;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;

        import com.example.giaohang.Chat.ChatActivity;
        import com.example.giaohang.Customer.CustomerMapActivity;
        import com.example.giaohang.FCMsend;
        import com.example.giaohang.History.HistoryActivity2;
        import com.example.giaohang.ProfileActivity;
        import com.example.giaohang.R;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;

        import com.akexorcist.googledirection.DirectionCallback;
        import com.akexorcist.googledirection.GoogleDirection;
        import com.akexorcist.googledirection.constant.TransportMode;
        import com.akexorcist.googledirection.model.Direction;
        import com.akexorcist.googledirection.model.Route;
        import com.akexorcist.googledirection.util.DirectionConverter;
        import com.example.giaohang.Adapters.CardRequestAdapter;
        import com.example.giaohang.History.HistoryActivity;
        import com.example.giaohang.Login.LauncherActivity;
        import com.example.giaohang.Login.LoginFragment;
        import com.example.giaohang.Objects.DriverObject;
        import com.example.giaohang.Objects.RideObject;
        import com.example.giaohang.StatusActivity;
        import com.example.giaohang.UsersActivity;
        import com.example.giaohang.WatchDriverActivity;
        import com.firebase.geofire.GeoFire;
        import com.firebase.geofire.GeoLocation;
        import com.firebase.geofire.GeoQuery;
        import com.firebase.geofire.GeoQueryEventListener;
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
        import com.google.android.gms.maps.model.MapStyleOptions;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.material.bottomsheet.BottomSheetBehavior;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.navigation.NavigationView;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ServerValue;
        import com.google.firebase.database.ValueEventListener;
        import com.lorentzos.flingswipe.SwipeFlingAdapterView;
        import com.ncorti.slidetoact.SlideToActView;

        import org.jetbrains.annotations.NotNull;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Locale;
        import java.util.Map;
*/
/**
 * Màn hình chính khi driver đăng nhập
 *//*

public class DriverMapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, DirectionCallback {
    int MAX_SEARCH_DISTANCE = 20;
    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SlideToActView mRideStatus;
    private Switch mWorkingSwitch;
    String database_name= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private LinearLayout mCustomerInfo, mBringUpBottomLayout;
    private TextView mCustomerName;
    private TextView mLocationStop;
    private TextView mLocationStart;
    private TextView mMoney;
    private ImageView Image_Show_Driver_Map;
    private FloatingActionButton mMaps_start,mMaps_stop;
    DatabaseReference mUser;
    RideObject mCurrentRide;
    Marker pickupMarker, destinationMarker;
    DriverObject mDriver = new DriverObject();
    TextView mUsername, mLogout,mHistory_navi_fake_btn;
    private ValueEventListener driveHasEndedRefListener;
    private CardRequestAdapter cardRequestAdapter;
    List<RideObject> requestList = new ArrayList<>();
    View mBottomSheet;
    BottomSheetBehavior<View> mBottomSheetBehavior;
    GeoQuery geoQuery;
    boolean started = false;
    boolean zoomUpdated = false;
    private String customerId="";
    private int status =0;
    private FloatingActionButton mCall;
    private FloatingActionButton mMessage;
    private Button mHistoryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);
        Toolbar toolbar = findViewById(R.id.main_page_toolbar_55);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        polylines = new ArrayList<>();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mUser = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(FirebaseAuth.getInstance().getUid());
        mCustomerInfo = findViewById(R.id.customerInfo);
        mHistoryBtn=findViewById(R.id.history_driver_btn);
        mBringUpBottomLayout = findViewById(R.id.bringUpBottomLayout);
        mCustomerName = findViewById(R.id.name);
        mLocationStop = findViewById(R.id.location_stop);
        mLocationStart = findViewById(R.id.location_start);
        mMoney = findViewById(R.id.money);
        mUsername = navigationView.getHeaderView(0).findViewById(R.id.usernameDrawer);
        mMaps_start = findViewById(R.id.openMaps_start);
        mMaps_stop = findViewById(R.id.openMaps_stop);
        mCall = findViewById(R.id.phone_from_cust);
        mMessage = findViewById(R.id.message_to_cus);
        //  FloatingActionButton mCall = findViewById(R.id.phone);
        ImageView mCancel = findViewById(R.id.cancel);
        mRideStatus = findViewById(R.id.rideStatus);
        mLogout = findViewById(R.id.logout);
        mHistory_navi_fake_btn = findViewById(R.id.history_navigation_fake_driver);
        mWorkingSwitch = findViewById(R.id.workingSwitch);
        mLogout.setOnClickListener(v -> logOut());
        mHistory_navi_fake_btn.setOnClickListener(v -> {
            Intent intent= new Intent(com.example.giaohang.Driver.DriverMapActivity.this, HistoryActivity2.class);
            intent.putExtra("customerOrDriver","Drivers");
            startActivity(intent);
        });
        mWorkingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!mDriver.getActive()) {
                Toast.makeText(com.example.giaohang.Driver.DriverMapActivity.this, R.string.not_approved, Toast.LENGTH_LONG).show();
                mWorkingSwitch.setChecked(false);
                return;
            }
            if (isChecked) {
                connectDriver();
            } else {
                disconnectDriver();
            }
        });
       */
/* mRideStatus.setOnSlideCompleteListener(v -> {
            switch (mCurrentRide.getState()) {
                case 1:
                    if (mCurrentRide == null) {
                        return;
                    }
                    mCurrentRide.pickedCustomer();
                    break;
                case 2:
                    if (mCurrentRide != null)
                        mCurrentRide.recordRide();
                    break;
            }
        });*//*

        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(com.example.giaohang.Driver.DriverMapActivity.this, HistoryActivity2.class);
                intent.putExtra("customerOrDriver","Drivers");
                startActivity(intent);
            }
        });
        mRideStatus.setOnSlideCompleteListener(v -> {
            switch (status) {
                case 1:

                    status=2;
                    mRideStatus.setText("Đơn đã hoàn thành");
                    mRideStatus.resetSlider();
                    break;
                //Driver tren duong den Destinaton
                case 2:
                    */
/*if (mCurrentRide != null)
                        mCurrentRide.recordRide();*//*

                    recordRide2();
                    endRide2();
                    break;
            }
        });
        mMaps_start.setOnClickListener(view -> {
            openGoogleDirectionMaps("chợ nấp");
        });
        */
/*mMaps.setOnClickListener(view -> {
            if (mCurrentRide.getState() == 1) {
                openMaps(mCurrentRide.getPickup().getCoordinates().latitude, mCurrentRide.getPickup().getCoordinates().longitude);
            } else {
                openMaps(mCurrentRide.getDestination().getCoordinates().latitude, mCurrentRide.getDestination().getCoordinates().longitude);
            }
        });*//*

        */
/*mCall.setOnClickListener(view -> {
            if (mCurrentRide == null) {
                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mCurrentRide.getCustomer().getPhone()));
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.no_phone_call_permissions), Snackbar.LENGTH_LONG).show();
            }
        });*//*

        */
/*mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{"Gọi người gửi", "Gọi người nhận"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(DriverMapActivity.this);
                builder.setTitle("Chọn");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            if (mCurrentRide == null) {
                                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
                            }
                            if (ContextCompat.checkSelfPermission(DriverMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mCurrentRide.getCustomer().getPhone_start()));
                                startActivity(intent);
                            }
                        }
                        if(i == 1){
                            if (ContextCompat.checkSelfPermission(DriverMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0981596497"));
                                startActivity(intent);}

                        }
                    }
                });
                builder.show();
            }
        });*//*

        mCancel.setOnClickListener(v -> {
            mCurrentRide.cancelRide();
            endRide2();
        });
        ImageView mDrawerButton = findViewById(R.id.drawerButton);
        mDrawerButton.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
        mBringUpBottomLayout = findViewById(R.id.bringUpBottomLayout);
        mBringUpBottomLayout.setOnClickListener(v -> {
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            else
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (mCurrentRide == null) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        getUserData();
        initializeRequestCardSwipe();
        isRequestInProgress();
        ViewTreeObserver vto = mBringUpBottomLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this::initializeBottomLayout);
        getAssignedCustomer2();
    }
    */
/*private void openMaps(double latitude, double longitude) {
        try {
            String url = "https://waze.com/ul?ll=" + latitude + "," + longitude + "&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("  + latitude + "," + longitude));
            startActivity(intent);
        }
    }*//*

    private void openGoogleDirectionMaps(String destination1) {
        try {
            destination1 = destination1.replace(" ","+");
            System.out.println(destination1);
            String url2="https://www.google.com/maps/dir/?api=1&destination="+destination1+"&travelmode=BICYCLING";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            */
/*Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("  + latitude + "," + longitude));
                            startActivity(intent);*//*

        }
    }
    private void recordRide2() {
        String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(driverId).child("history");
        DatabaseReference customerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Customers").child(customerId).child("history");
        DatabaseReference historyRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("history");
        String requestId =historyRef.push().getKey();
        driverRef.child(requestId).setValue(true);
        customerRef.child(requestId).setValue(true);
        HashMap map= new HashMap();
        map.put("driver",driverId);
        map.put("customer",customerId);
        map.put("rating",0);
        map.put("timestamp",getTimestamp());
        historyRef.child(requestId).updateChildren(map);
    }
    private Long getTimestamp() {
        Long timestamp =System.currentTimeMillis()/1000;
        return timestamp;
    }
    private void getAssignedCustomer2() {
        String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        */
/*DatabaseReference assignedCustomerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(driverId);*//*

        DatabaseReference assignedCustomerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(driverId).child("customerRequest").child("customerRideId");
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // tai xe dang tren duong pickup customer
                    status=1;
                    */
/*Map<String,Object> map= (Map<String,Object>)snapshot.getValue();
                    if(map.get("customerRideId")!=null){
                         customerId= map.get("customerRideId").toString();
                         getAssignedCustomerPickupLocation2();
                    }*//*

                    customerId= snapshot.getValue().toString();
                    getAssignedCustomerPickupLocation2();
                    getAssignedCustomerInfo2();

                    mMessage.setOnClickListener(view -> {
                        Intent intent=new Intent(com.example.giaohang.Driver.DriverMapActivity.this, ChatActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id_chat",customerId);
                        extras.putString("customerOrDriver","Drivers");
                        extras.putString("user_name","name_cust");
                        intent.putExtras(extras);
                        startActivity(intent);
                    });
                } else {
                    endRide2();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getAssignedCustomerInfo2() {
        // mCustomerInfo.setVisibility(View.VISIBLE);
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Customers").child(customerId);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() & dataSnapshot.getChildrenCount()>0) {
                    //if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                    Map<String,Object> map =(Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("productRate")!= null){
                        mMoney.setText(map.get("productRate").toString());
                    }
                    if(map.get("locationStart")!= null){
                        mLocationStart.setText(map.get("locationStart").toString());
                    }
                    String mstart= mLocationStart.getText().toString();
                    mMaps_start.setOnClickListener(view -> {
                        openGoogleDirectionMaps(mstart);
                    });
                    if(map.get("locationStop")!= null){
                        mLocationStop.setText(map.get("locationStop").toString());
                    }
                    if((map.get("phone_start")!= null) && (map.get("phone_start")!= null)){
                        mCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String phone_start=map.get("phone_start").toString();
                                String phone_stop=map.get("phone_stop").toString();
                                CharSequence options[] = new CharSequence[]{"Gọi người gửi", "Gọi người nhận"};
                                final AlertDialog.Builder builder = new AlertDialog.Builder(com.example.giaohang.Driver.DriverMapActivity.this);
                                builder.setTitle("Chọn");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i == 0){
                                            if (mCurrentRide == null) {
                                                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
                                            }
                                            if (ContextCompat.checkSelfPermission(com.example.giaohang.Driver.DriverMapActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_start));
                                                startActivity(intent);
                                            }
                                        }
                                        if(i == 1){
                                            if (ContextCompat.checkSelfPermission(com.example.giaohang.Driver.DriverMapActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_stop));
                                                startActivity(intent);}

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                        // mLocationStop.setText(map.get("locationStop").toString());
                    }
                    if(map.get("phone_stop")!= null){
                        // mLocationStop.setText(map.get("locationStop").toString());
                    }
                    String mstop= mLocationStop.getText().toString();
                    mMaps_stop.setOnClickListener(view -> {
                        openGoogleDirectionMaps(mstop);
                    });
                    if(map.get("name")!= null){
                        mCustomerName.setText(map.get("name").toString());
                    }
                    String token_id = dataSnapshot.child("token_id").getValue().toString();
                    */
/*FCMsend.FirebaseMessagingService(DriverMapActivity.this,
                            token_id, "New Order", uId);*//*

                    FCMsend.FirebaseMessagingService(com.example.giaohang.Driver.DriverMapActivity.this,
                            token_id
                            , "New Order", "Có tài xế đã nhận đơn");
                }
                */
/*if (mCurrentRide != null) {
                    mCurrentRide.getCustomer().parseData(dataSnapshot);
                    mCustomerName.setText(mCurrentRide.getCustomer().getName());
                }*//*

                mCustomerInfo.setVisibility(View.VISIBLE);
                mBottomSheetBehavior.setHideable(false);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    private Marker mCustomMaker2;
    DatabaseReference assignedCustomedPickupLocation;
    ValueEventListener assignedCustomedPickupLocationListener;
    private void getAssignedCustomerPickupLocation2() {
        assignedCustomedPickupLocation= FirebaseDatabase.getInstance(database_name).getReference()
                .child("customerRequest").child(customerId).child("l");
        assignedCustomedPickupLocationListener=assignedCustomedPickupLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Object> map=(List<Object>) snapshot.getValue();
                    double locationLat =0;
                    double locationLng =0;
                    if(map.get(0)!=null){
                        locationLat=Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1)!=null){
                        locationLng=Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng= new LatLng(locationLat,locationLng);
                    if(mCustomMaker2 != null){
                        mCustomMaker2.remove();
                    }
                    // mCustomMaker2=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("pick up customer here"));
                    mCustomMaker2 = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin_loca_32))
                            .position(driverLatLng).title("New Request"));
                    // mMap.addMarker(new MarkerOptions().position(driverLatLng).title("pickup locationn"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    */
/**
     * Mở openmap cho driver biết tuyến đường đi vầ đến
     * first open waze,nếu fail mở tiếp openmap
     * biến latitude  - destination's latitude
     * biến longitude - destination's longitude
     *//*

    */
/*private void openMaps(double latitude, double longitude) {
        try {
            String url = "https://waze.com/ul?ll=" + latitude + "," + longitude + "&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("  + latitude + "," + longitude));
            startActivity(intent);
        }
    }*//*

    */
/**
     * Khởi tạo swipe card và control các event qua các listeners
     * Left swipe: từ chối request ride
     * right: chấp nhận request
     *//*

    private void initializeRequestCardSwipe() {
        cardRequestAdapter = new CardRequestAdapter(getApplicationContext(), R.layout.item__card_request, requestList);
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(cardRequestAdapter);
        //Handling swipe of cards
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                requestList.remove(0);
                cardRequestAdapter.notifyDataSetChanged();
            }
            @Override
            public void onLeftCardExit(Object dataObject) {
                RideObject mRide = (RideObject) dataObject;
                requestList.remove(mRide);
                cardRequestAdapter.notifyDataSetChanged();
            }
            @Override
            public void onRightCardExit(Object dataObject) {
                RideObject mRide = (RideObject) dataObject;
                if (mRide.getDriver() == null) {
                    try {
                        mCurrentRide = (RideObject) mRide.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    mCurrentRide.confirmDriver();
                    requestListener();
                }
            }
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }
            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });
    }
    */
/**
     * Listener bottom popup cho nó đc show hay ko khi users kéo hoặc click
     *//*

    private void initializeBottomLayout() {
        mBottomSheet = findViewById(R.id.bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(mBringUpBottomLayout.getHeight());
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (mCurrentRide == null) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
    */
/**
     * lấy user info
     * Check xem user có work hay ko trước khi đóng app
     * Set radio button "working"
     *//*

    private void getUserData() {
        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(driverId);
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mDriver.parseData(dataSnapshot);
                    mUsername.setText(mDriver.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance(database_name).getReference("driversWorking").child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    connectDriver();
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    disconnectDriver();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    */
/**
     * Check request có đang tiến hành hay k. nhìn vào ride_info xem id driver hiện tại có ở trong
     * đó, nếu có ride tiếp tục
     *//*

    private void isRequestInProgress() {
        FirebaseDatabase.getInstance(database_name).getReference().child("ride_info").orderByChild("driverId")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        for(DataSnapshot mData : dataSnapshot.getChildren()){
                            mCurrentRide = new RideObject();
                            mCurrentRide.parseData(mData);
                            if (mCurrentRide.getCancelled() || mCurrentRide.getEnded()) {
                                endRide2();
                                return;
                            }
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            requestListener();
                        }
                    }
                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                    }
                });
    }
    */
/**
     * Check biến mCurrentRide và current state, thức hiệ hành động với từng state
     *//*

    private void checkRequestState() {
        switch (mCurrentRide.getState()) {
            case 1:
                destinationMarker = mMap.addMarker(new MarkerOptions().position(mCurrentRide.getDestination().getCoordinates())
                        .title("Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_radio_filled)));
                pickupMarker = mMap.addMarker(new MarkerOptions().position(mCurrentRide.getPickup().getCoordinates())
                        .title("Pickup").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_radio)));
                mRideStatus.setText(getResources().getString(R.string.picked_customer));
                mRideStatus.resetSlider();
                mCustomerName.setText(mCurrentRide.getDestination().getName());
                getAssignedCustomerInfo();
                requestList.clear();
                cardRequestAdapter.notifyDataSetChanged();
                erasePolylines();
                getRouteToMarker(mCurrentRide.getPickup().getCoordinates());
                break;
            case 2:
                erasePolylines();
                if (mCurrentRide.getDestination().getCoordinates().latitude != 0.0 && mCurrentRide.getDestination().getCoordinates().longitude != 0.0) {
                    getRouteToMarker(mCurrentRide.getDestination().getCoordinates());
                }
                mRideStatus.setText(getResources().getString(R.string.drive_complete));
                mRideStatus.resetSlider();
                break;
            default:
                endRide2();
        }
    }
    */
/**
     * Lấy các rider gần nhất với radius of MAX_SEARCH_DISTANCE và vị trí driver hiện tại.
     * nếu 1 request đc tìm thấy và rider đang ko thực hiện 1 request nào khác thi gọi getRequestInfo(key)
     * key - id of the request.
     *//*

    private void getRequestsAround() {
        if (mLastLocation == null) {
            return;
        }
        DatabaseReference requestLocation = FirebaseDatabase.getInstance(database_name).getReference().child("customer_requests");
        GeoFire geoFire = new GeoFire(requestLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), MAX_SEARCH_DISTANCE);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mWorkingSwitch.isChecked()){
                    return;
                }
                if (mCurrentRide == null) {
                    for (RideObject mRideIt : requestList) {
                        if (mRideIt.getId().equals(key)) {
                            return;
                        }
                    }
                    getRequestInfo(key);
                }else{
                    requestList.clear();
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
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }
    */
/**
     * Nhận info request and nếu nó chưa end or cancell thi thêm vào requestList và
     * đẩy 1 card of request tới màn hình driver.
     * biến key - id của request để fetch info
     *//*

    private void getRequestInfo(String key) {
        FirebaseDatabase.getInstance(database_name).getReference().child("ride_info").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {return;}
                if (mCurrentRide != null) {return;}
                RideObject mRide = new RideObject();
                mRide.parseData(dataSnapshot);
                if(!mRide.getRequestService().equals(mDriver.getService())){return;}
                for (RideObject mRideIt : requestList) {
                    if (mRideIt.getId().equals(mRide.getId())) {
                        if (mRide.getCancelled() || mRide.getEnded() || mRide.getDriver() != null) {
                            requestList.remove(mRideIt);
                            cardRequestAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }
                if (!mRide.getCancelled() && !mRide.getEnded() && mRide.getDriver() == null && mRide.getState() == 0) {
                    requestList.add(mRide);
                    cardRequestAdapter.notifyDataSetChanged();
                    makeSound();
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("timestamp_last_driver_read", ServerValue.TIMESTAMP);
                    FirebaseDatabase.getInstance(database_name).getReference().child("ride_info").child(key).updateChildren(map);
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    */
/**
     * notification sound
     *//*

    private void makeSound() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.driver_notification);
        mp.start();
    }
    */
/**
     * Listener request mà driver đang assigned
     *//*

    private void requestListener() {
        if (mCurrentRide == null) {
            return;
        }
        driveHasEndedRefListener = mCurrentRide.getRideRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                mCurrentRide.parseData(dataSnapshot);
                //if drive has ended or been cancelled then call endRide to retrieve all variables to their default state
                if (mCurrentRide.getCancelled() || mCurrentRide.getEnded()) {
                    endRide2();
                    return;
                }
                checkRequestState();
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    */
/**
     * Nhận Route from điểm đi tới điểm đến, showing the route
     * biến destination - LatLng of the location
     *//*

    private void getRouteToMarker(LatLng destination) {
        String serverKey = getResources().getString(R.string.google_maps_key);
        if (destination != null && mLastLocation != null) {
            GoogleDirection.withServerKey(serverKey)
                    .from(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        }
    }
    */
/**
     * Lấy customer info đã được thiết lập và hiển thị trong Bottom sheet
     *//*

    private void getAssignedCustomerInfo() {
        if (mCurrentRide.getCustomer().getId() == null) {
            return;
        }
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Customers").child(mCurrentRide.getCustomer().getId());
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                if (mCurrentRide != null) {
                    mCurrentRide.getCustomer().parseData(dataSnapshot);
                    mCustomerName.setText(mCurrentRide.getCustomer().getName());
                }
                mCustomerInfo.setVisibility(View.VISIBLE);
                mBottomSheetBehavior.setHideable(false);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    */
/**
     * Loại bỏ các active listeners,
     * return to default state
     * Xóa markers
     *//*

    */
/*private void endRide() {
        if (mCurrentRide == null) {
            return;
        }
        if (driveHasEndedRefListener != null) {
            mCurrentRide.getRideRef().removeEventListener(driveHasEndedRefListener);
        }
        mRideStatus.setText(getString(R.string.picked_customer));
        erasePolylines();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(userId).child("customerRequest");
        driverRef.removeValue();
        //Remove the request from the geofire child so that other drivers don't have to check this request in the future
        DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("customerRequest");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(mCurrentRide.getId(), (key, error) -> {
        });
        mCurrentRide = null;
        if (pickupMarker != null) {
            pickupMarker.remove();
        }
        if (destinationMarker != null) {
            destinationMarker.remove();
        }
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mCustomerName.setText("");
        mMap.clear();
        getRequestsAround();
        //This will allow the map to re-zoom on the current location
        zoomUpdated = false;
    }*//*

    private void  endRide2() {
        mRideStatus.setText("Trạng thái đơn đã nhan");

        */
/*String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(userId).child("customerRequest");
        driverRef.removeValue();*//*

        //Remove the request from the geofire child so that other drivers don't have to check this request in the future
        DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("customerRequest");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(customerId, (key, error) -> {
        });
        customerId="";
        if(mCustomMaker2!=null){
            mCustomMaker2.remove();
        }
        if(assignedCustomedPickupLocationListener!=null){
            assignedCustomedPickupLocation.removeEventListener(assignedCustomedPickupLocationListener);
        }
        mCustomerInfo.setVisibility(View.GONE);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mCustomerName.setText("");
        mLocationStop.setText("");
        mLocationStart.setText("");
        mMoney.setText("");
        */
/*mMap.clear();
        getRequestsAround();*//*

        //This will allow the map to re-zoom on the current location
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);//interval with which the driver location will be updated
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                    .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                checkLocationPermission();
            }
        }
    }
    */
/**
     * Update vị trí driver hiện  tại để customer có thể lấy info drivers xung quanh
     *//*

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference refWorking = FirebaseDatabase.getInstance(database_name).getReference("driversWorking");
                    GeoFire geoFireWorking = new GeoFire(refWorking);
                    if (!mWorkingSwitch.isChecked()) {
                        geoFireWorking.removeLocation(userId, (key, error) -> {
                        });
                        return;
                    }
                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
                    });
                    if (mCurrentRide != null && mLastLocation != null) {
                        mCurrentRide.setRideDistance(mCurrentRide.getRideDistance() + mLastLocation.distanceTo(location) / 1000);
                    }
                    mLastLocation = location;
                    if (!started) {
                        getRequestsAround();
                        started = true;
                    }
                    Map<String, Object> newUserMap = new HashMap<>();
                    newUserMap.put("last_updated", ServerValue.TIMESTAMP);
                    mUser.updateChildren(newUserMap);
                    if (!zoomUpdated) {
                        float zoomLevel = 17.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                        zoomUpdated = true;
                        */
/*mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                        zoomUpdated = true;*//*

                    }
                    */
/*String userId2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference refAvailable2 = FirebaseDatabase.getInstance(database_name).getReference("driversWorking");
                    DatabaseReference refWorking2 = FirebaseDatabase.getInstance(database_name).getReference("driversAvailable");
                    GeoFire geoFireAvailable2 = new GeoFire(refAvailable2);
                    GeoFire geoFireWorking2 = new GeoFire(refWorking2);
                    switch (customerId){
                        case "":
                            geoFireWorking2.removeLocation(userId2);
                            geoFireAvailable2.setLocation(userId2, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;

                        default:
                            geoFireAvailable2.removeLocation(userId2);
                            geoFireWorking2.setLocation(userId2, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;*//*

                }
            }
        }
    };
    */
/**
     * permissions
     *//*

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(com.example.giaohang.Driver.DriverMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1))
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(com.example.giaohang.Driver.DriverMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.CALL_PHONE}, 1);
            }
        }
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
                Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void logOut() {
        disconnectDriver();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(com.example.giaohang.Driver.DriverMapActivity.this, LauncherActivity.class);
        startActivity(intent);
        finish();
    }
    */
/**
     * Connects driver
     *//*

    private void connectDriver() {
        mWorkingSwitch.setChecked(true);
        checkLocationPermission();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }
    */
/**
     Disconnects driver
     *//*

    private void disconnectDriver() {
        mWorkingSwitch.setChecked(false);
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref1 = FirebaseDatabase.getInstance(database_name).getReference("driversAvailable").child(userId);
        DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("driversWorking").child(userId);
        ref.removeValue();
        ref1.removeValue();
    }
    private List<Polyline> polylines;
    */
/**
     Remove route polylines
     *//*

    private void erasePolylines() {
        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();
    }
    */
/**
     * Show map với điểm đi và điểm đến
     * biến route - route giữa điểm đi và đến
     *//*

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
    */
/**
     * Add rout to map
     * biến direction - Direction object
     * biến rawBody   - thông tin the route
     *//*

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            Polyline polyline = mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.BLACK));
            polylines.add(polyline);
            setCameraWithCoordinationBounds(route);
        }
    }
    @Override
    public void onDirectionFailure(Throwable t) {
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId()==R.id.main_new_order){
            Intent order_intent = new Intent(this, LoginFragment.class);
            startActivity(order_intent);
            finish();
        }
        if(item.getItemId()==R.id.ordering){
            */
/*Intent ordering_intent = new Intent(MainActivity__55.this, DetailActivity.class);
            startActivity(ordering_intent);*//*

            finish();
        }
        if(item.getItemId()==R.id.receiving){
            */
/*Intent ordering_intent = new Intent(MainActivity__55.this, ReceivingActivity.class);
            startActivity(ordering_intent);*//*

            finish();
        }
        if(item.getItemId()==R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            //   sendToStart();
        }
        if(item.getItemId()==R.id.main_setting_btn){
            */
/*Intent setting_intent = new Intent(MainActivity__55.this, SettingsActivity.class);
            startActivity(setting_intent);
            finish();*//*

        }
        if(item.getItemId()==R.id.main_all_users_btn){
            */
/*Intent setting_intent = new Intent(MainActivity__55.this, UsersActivity.class);
            startActivity(setting_intent);
            finish();*//*

        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.history) {
            Intent intent= new Intent(com.example.giaohang.Driver.DriverMapActivity.this, HistoryActivity2.class);
            intent.putExtra("customerOrDriver","Customers");
            startActivity(intent);
            */
/*Intent intent = new Intent(DriverMapActivity.this, HistoryActivity.class);
            intent.putExtra("customerOrDriver", "Drivers");
            startActivity(intent);*//*

        } else if (id == R.id.settings) {
            Intent intent = new Intent(com.example.giaohang.Driver.DriverMapActivity.this, DriverSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.payout) {
            */
/*Intent intent = new Intent(DriverMapActivity.this, PayoutActivity.class);
            startActivity(intent);*//*

        } else if (id == R.id.logout_driver) {
            logOut();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}

*/
