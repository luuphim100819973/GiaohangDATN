package com.example.giaohang.Login;

import android.content.Intent;
import android.os.Bundle;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.google.android.gms.tasks.OnCompleteListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.example.giaohang.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


/**
 * Đăng kí tài khoản mới
 */
public class DetailsChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mName;
    private SegmentedButtonGroup mRadioGroup;
    String database_name = "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initializeObjects();
    }
    /**
     * Đăng kí tài khoản mới, check thêm các thông tin khác
     */
    private void register() {
        if (mName.getText().length() == 0) {
            mName.setError("Không đc để trống.");
            return;
        }
        final String name = mName.getText().toString();
        final String accountType;
        int selectId = mRadioGroup.getPosition();
        if (selectId == 1) {
            accountType = "Drivers";
        } else {
            accountType = "Customers";
        }
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("name", name);
        newUserMap.put("profileImageUrl", "default");
        if (accountType.equals("Drivers")) {
            newUserMap.put("service", "type_1");
            newUserMap.put("activated", true);
        }
        /**
         * Đăng kí tài khoản mới, thêm token_id cho service notification
         */
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        String token = task.getResult();
                        FirebaseDatabase.getInstance(database_name).getReference().child("Users")
                                .child(accountType).child(user_id).
                                child("token_id").setValue(token);
                    }
                });
        FirebaseDatabase.getInstance(database_name).getReference().child("Users").child(accountType).child(user_id)
                .updateChildren(newUserMap).addOnCompleteListener((OnCompleteListener<Void>) task -> {
            Intent intent = new Intent(DetailsChooseActivity.this, LauncherActivity.class);
            startActivity(intent);
            finish();
        });
    }
    /**
     * Khởi tạo các design Elements
     */
    private void initializeObjects() {
        mName = findViewById(R.id.name);
        Button mRegister = findViewById(R.id.register);
        mRadioGroup = findViewById(R.id.radioRealButtonGroup);
        mRadioGroup.setPosition(0, false);
        mRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            register();
        }
    }
}