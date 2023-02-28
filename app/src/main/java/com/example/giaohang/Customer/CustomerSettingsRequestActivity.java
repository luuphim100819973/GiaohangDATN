package com.example.giaohang.Customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.giaohang.Objects.CustomerObject;
import com.example.giaohang.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Hiển thị các setting của customer
 */
public class CustomerSettingsRequestActivity extends AppCompatActivity {

    private EditText mNameField, mPhone_start_cus,mPhone_stop_cus;
    private ImageView mProfileImage;
    private DatabaseReference mCustomerDatabase;
    private String userID;
    private Uri resultUri;
    String database= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    CustomerObject mCustomer;
    private EditText mLocationStart;
    private EditText mLocationStop;
    private EditText mNamePersonDestination,mProductName,mProductRate,mProductQuantity;
    private EditText mNote;
    private TextView mResultCalculateMoney;
    private TextView mResultProductName,mResultCalculateFeeShip,mResultCalculateDistance;
    private FloatingActionButton btn_start_auto,btn_stop_auto;
   // private String address3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_settings);
        mNameField = findViewById(R.id.name);
        mNamePersonDestination=findViewById(R.id.name_person_destination);
        mProductName=findViewById(R.id.product_name);
        mProductRate=findViewById(R.id.product_money_rate);
        mProductQuantity=findViewById(R.id.product_number);

        mPhone_start_cus = findViewById(R.id.phone_cus_start);
        mPhone_stop_cus = findViewById(R.id.phone_cus_stop);
        mProfileImage = findViewById(R.id.profileImage);
        mLocationStart = findViewById(R.id.location_start_cust);
        mLocationStop=findViewById(R.id.location_stop_cust);
        btn_stop_auto=findViewById(R.id.btn_auto_stop);
        btn_start_auto=findViewById(R.id.btn_auto_start);
    //    mMoney = findViewById(R.id.money_cust);
        mNote = findViewById(R.id.note_cust);
        mResultProductName=findViewById(R.id.result_product_name);
        mResultCalculateMoney=findViewById(R.id.result_caculate_money);
        mResultCalculateFeeShip =findViewById(R.id.result_calculate_fee);
        mResultCalculateDistance=findViewById(R.id.result_calculate_distance);
        Button mConfirm = findViewById(R.id.confirm);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance(database).getReference().child("Users")
                .child("Customers").child(userID);
        mCustomer = new CustomerObject(userID);
        getUserInfo();
        mProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
        mConfirm.setOnClickListener(v -> saveUserInformation());
        setupToolbar();
        btn_start_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationStart =mLocationStart.getText().toString();
                try {
                    geoLocate_start(locationStart);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        btn_stop_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationStop =mLocationStop.getText().toString();
                try {
                    geoLocate_stop(locationStop);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            boolean _ignore = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!mProductQuantity.getText().toString().equals("")  && !mProductRate.getText().toString().equals("")
                 && !mProductName.getText().toString().equals("")){
                    int temp1=Integer.parseInt(mProductQuantity.getText().toString());
                    int temp2=Integer.parseInt(mProductRate.getText().toString());
                    mResultCalculateMoney.setText(String.valueOf(temp1*temp2));
                    mResultProductName.setText(mProductName.getText().toString()+" x "+mProductQuantity.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                /*if (_ignore)
                    return;
                _ignore = true;
                try {
                    calculateDistance(mLocationStart.getText().toString(),mLocationStop.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                _ignore = false;*/
            }
        };
        mProductQuantity.addTextChangedListener(textWatcher);
        mProductRate.addTextChangedListener(textWatcher);
        mProductName.addTextChangedListener(textWatcher);
        //mLocationStart.addTextChangedListener(textWatcher);
        //mLocationStop.addTextChangedListener(textWatcher);
       // mResultProductNameQuantity.addTextChangedListener(textWatcher);
        mResultCalculateDistance.setText("=>Click để tính");
        mResultCalculateDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    calculateDistance(mLocationStart.getText().toString(),mLocationStop.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#673AB7"));
        // Set BackgroundDrawable
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        myToolbar.setTitle("Tạo đơn hàng");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Lấy info user và đổ vào
     */
    private void getUserInfo(){
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){return;}
                mCustomer.parseData(dataSnapshot);
                mNameField.setText(mCustomer.getName());
                mPhone_start_cus.setText(mCustomer.getPhone_start());
                mNamePersonDestination.setText(mCustomer.getName_person_destination());
                mPhone_stop_cus.setText(mCustomer.getPhone_stop());
                mLocationStart.setText(mCustomer.getmLocationStart());
                mLocationStop.setText(mCustomer.getmLocationStop());
                mProductName.setText(mCustomer.getProduct_name());
                mResultCalculateDistance.setText(mCustomer.getmCalculateDistance());
                mResultCalculateFeeShip.setText(mCustomer.getmCalculateFee());
                mProductQuantity.setText(mCustomer.getProduct_quantity());
                mProductRate.setText(mCustomer.getProduct_rate());
             //   mMoney.setText(mCustomer.getmMoney());
                mNote.setText(mCustomer.getmNote());
                if(!mCustomer.getProfileImage().equals("default"))
                    Glide.with(getApplication()).load(mCustomer.getProfileImage())
                            .apply(RequestOptions.circleCropTransform()).into(mProfileImage);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Lưu user info hiện tại
     * upload vào storage và update link storage vào database Users-> Customers
     */
    private void saveUserInformation() {
        String mName = mNameField.getText().toString();
        String mPhone_start =mPhone_start_cus.getText().toString();
        String mName_person_destination = mNamePersonDestination.getText().toString();
        String mPhone_stop = mPhone_stop_cus.getText().toString();
        String LocationStart =mLocationStart.getText().toString();
        String LocationStop = mLocationStop.getText().toString();
        String mProduct_Name = mProductName.getText().toString();
        String mProduct_Quantity = mProductQuantity.getText().toString();
        String mProduct_Rate = mProductRate.getText().toString();
        String mCalculate_distance=mResultCalculateDistance.getText().toString();
        String mCalculate_feeShip=mResultCalculateFeeShip.getText().toString();



     //   String money = mMoney.getText().toString();
        String mnote= mNote.getText().toString();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", mName);
        userInfo.put("phone_start", mPhone_start);
        userInfo.put("name_person_destination", mName_person_destination);
        userInfo.put("phone_stop", mPhone_stop);
        userInfo.put("locationStart", LocationStart);
        userInfo.put("locationStop", LocationStop);
        userInfo.put("productName", mProduct_Name);
        userInfo.put("productQuantity", mProduct_Quantity);
        userInfo.put("productRate", mProduct_Rate);
        userInfo.put("calculateDistance", mCalculate_distance);
        userInfo.put("shipFee", mCalculate_feeShip);

     //   userInfo.put("money", money);
        userInfo.put("note", mnote);
        mCustomerDatabase.updateChildren(userInfo);
        Toast.makeText(this, "Tạo đơn hàng thành công, cùng đi tìm tài xế nào", Toast.LENGTH_LONG).show();
        if(resultUri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance("gs://delivery-b9821.appspot.com").getReference().child("profile_images").child(userID);
            UploadTask uploadTask = filePath.putFile(resultUri);
            uploadTask.addOnFailureListener(e -> {
                finish();
            });
            uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                Map newImage = new HashMap();
                newImage.put("profileImageUrl", uri.toString());
                mCustomerDatabase.updateChildren(newImage);
                finish();
            }).addOnFailureListener(exception -> {
                finish();
            }));
        }else{
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            resultUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                Glide.with(getApplication())
                        .load(bitmap) // Uri of the picture
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void geoLocate_start(String userInput) throws IOException {
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addressList=geocoder.getFromLocationName(userInput,3);
        if(addressList.size()>0){
            Address address=addressList.get(0);
            List<Address> addresses=geocoder.getFromLocation(address.getLatitude(),address.getLongitude(),1);
            String address3 =addresses.get(0).getAddressLine(0);
            Toast.makeText(this,"Vị trí 1 : "+ address3, Toast.LENGTH_SHORT).show();
            mLocationStart.setText(address3);
        }

    }
    private void calculateDistance(String place1, String place2) throws IOException {
        //float distance= 0.0F;
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addressList=geocoder.getFromLocationName(place1,3);
        List<Address> addressList2=geocoder.getFromLocationName(place2,3);
        //if(addressList.isEmpty() && addressList2.isEmpty())
        if(addressList.size()>0 &&addressList2.size()>0){
            if(addressList==null){return ;}
            if(addressList2==null){return;}

            Address address=addressList.get(0);
            Address address2=addressList2.get(0);
            Location loc1 = new Location("");
            loc1.setLatitude(address.getLatitude());
            loc1.setLongitude(address.getLongitude());
            Location loc2 = new Location("");
            loc2.setLatitude(address2.getLatitude());
            loc2.setLongitude(address2.getLongitude());
             float distance = loc1.distanceTo(loc2);

            distance=distance/1000;
            int precision = 10; //keep 4 digits
            distance= (float) (Math.floor(distance * precision +.5)/precision);
            mResultCalculateDistance.setText(String.valueOf(distance));
            if(distance<3.0){
                mResultCalculateFeeShip.setText("13500");
            }else {
                int precision2=10;
                float shipfee= (float) (5000*distance*1.2);
                shipfee= (float) (Math.floor(shipfee * precision2 +.5)/precision2);
                int shipfee2=(int)shipfee;
                mResultCalculateFeeShip.setText(String.valueOf(shipfee2));
            }

        }
       // return distance;

    }
    private void geoLocate_stop(String userInput) throws IOException {
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addressList=geocoder.getFromLocationName(userInput,3);
        if(addressList.size()>0){
            Address address=addressList.get(0);
            List<Address> addresses=geocoder.getFromLocation(address.getLatitude(),address.getLongitude(),1);
            String address3 =addresses.get(0).getAddressLine(0);
            Toast.makeText(this,"Vị trí 1 : "+ address3, Toast.LENGTH_SHORT).show();
            mLocationStop.setText(address3);
        }

    }

}
