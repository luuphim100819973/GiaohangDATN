package com.example.giaohang.Driver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.giaohang.Objects.DriverObject;
import com.example.giaohang.R;
import com.example.giaohang.Utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hiển thị settings Driver
 */
public class DriverSettingsActivity extends AppCompatActivity {

    private EditText mNameField, mPhoneField, mCarField, mLicense, mService;
    private ImageView mProfileImage;
    private DatabaseReference mDriverDatabase;
    private String userID;
    private Uri resultUri;
    String database_name = "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    DriverObject mDriver = new DriverObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);
        mNameField = findViewById(R.id.name);
        mPhoneField = findViewById(R.id.phone);
        mCarField = findViewById(R.id.car);
        mLicense = findViewById(R.id.license);
        mProfileImage = findViewById(R.id.profileImage);
        mService = findViewById(R.id.service);
        Button mConfirm = findViewById(R.id.confirm);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDriverDatabase = FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(userID);
        getUserInfo();
        mProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
        mService.setOnClickListener(view -> {
            Intent i = new Intent(DriverSettingsActivity.this, DriverChooseTypeActivity.class);
            i.putExtra("service", mDriver.getService());
            startActivityForResult(i, 2);
        });
        mConfirm.setOnClickListener(v -> saveUserInformation());
        setupToolbar();
    }
    /**
     * Set toolbar
     */
    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Thông tin tài xế");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#3F51B5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(v -> finish());
    }
    /**
     * Lấy user info hiện tại và đổ vào
     */
    private void getUserInfo(){
        mDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                mDriver.parseData(dataSnapshot);
                mNameField.setText(mDriver.getName());
                mPhoneField.setText(mDriver.getPhone());
                mCarField.setText(mDriver.getCar());
                mLicense.setText(mDriver.getLicense());
                mService.setText(Utils.getTypeById(DriverSettingsActivity.this, mDriver.getService()).getName());
                if (!mDriver.getProfileImage().equals("default"))
                    Glide.with(getApplication()).load(mDriver.getProfileImage())
                            .apply(RequestOptions.circleCropTransform()).into(mProfileImage);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    /**
     * Lưu user info database.
     * upload image vào storage
     */
    private void saveUserInformation() {
        String name = mNameField.getText().toString();
        String phone = mPhoneField.getText().toString();
        String car = mCarField.getText().toString();
        String license = mLicense.getText().toString();
        String service = mDriver.getService();
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        userInfo.put("car", car);
        userInfo.put("license", license);
        userInfo.put("service", service);
        mDriverDatabase.updateChildren(userInfo);
        if(resultUri != null) {
            final StorageReference filePath = FirebaseStorage
                    .getInstance("gs://delivery-b9821.appspot.com").getReference().child("profile_images").child(userID);
            UploadTask uploadTask = filePath.putFile(resultUri);
            uploadTask.addOnFailureListener(e -> {
                finish();
            });
            uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                Map newImage = new HashMap();
                newImage.put("profileImageUrl", uri.toString());
                mDriverDatabase.updateChildren(newImage);
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
        if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            String result= data.getStringExtra("result");
            mDriver.setService(result);
            mService.setText(Utils.getTypeById(DriverSettingsActivity.this, result).getName());
        }
    }
}
