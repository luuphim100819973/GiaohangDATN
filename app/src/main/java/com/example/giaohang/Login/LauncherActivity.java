package com.example.giaohang.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.example.giaohang.Customer.CustomerMapActivity;
import com.example.giaohang.Driver.DriverMapActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Kiểm tra user đăng nhập hay chưa, từ đó gọi AuthenticationActivity or MainActivity
 */
public class LauncherActivity extends AppCompatActivity {
    int triedTypes = 0;
    String database_name = "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            checkUserAccType();
        } else {
            Intent intent = new Intent(LauncherActivity.this, AuthenticationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    /**
     * Check user là "Drivers" hay "Customer", nếu chưa mở DetailsActivity để chọn loại user
     */
    private void checkUserAccType() {
        String userID;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Customers").child(userID);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            String token = task.getResult();
                            if(task.isSuccessful()){
                                mCustomerDatabase.child("token_id").setValue(token).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(getApplication(), CustomerMapActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                                //  Intent mainIntent = new Intent(LoginActivity.this, MainActivity__55.class);
                                                //  mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                //  startActivity(mainIntent);
                                            }
                                        });
                            }else{
                            }
                        }
                    });
                  // startApis("Customers");
                    /*Intent intent = new Intent(getApplication(), CustomerMapActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();*/
                } else {
                    checkNoType();
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
        DatabaseReference mDriverDatabase = FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(userID);
        mDriverDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            String token = task.getResult();
                            if(task.isSuccessful()){

                                mDriverDatabase.child("token_id").setValue(token).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(getApplication(), DriverMapActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }else{
                            }
                        }
                    });
               //     startApis("Drivers");
                    Intent intent = new Intent(getApplication(), DriverMapActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    checkNoType();
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    /**
     * Check nếu chưa chọn loại nào thì sẽ mở DetailsActivity
     */
    void checkNoType() {
        triedTypes++;
        if (triedTypes == 2) {
            Intent intent = new Intent(getApplication(), DetailsChooseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    /**
     * Mở onesignal and stripe apis
     * biến type - loại user (customer, driver)
     */
    /*void startApis(String type) {
        OneSignal.startInit(this).init();
        OneSignal.sendTag("User_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        OneSignal.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        OneSignal.idsAvailable((userId, registrationId) -> FirebaseDatabase.getInstance(database_name).getReference().child("Users").child(type).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notificationKey").setValue(userId));
        *//*PaymentConfiguration.init(
                getApplicationContext(),
                getResources().getString(R.string.publishablekey)
        );*//*
    }*/
}
