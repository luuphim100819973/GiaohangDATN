package com.example.giaohang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.giaohang.Chat.ChatActivity;
import com.example.giaohang.Customer.CustomerMapActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class IncomeActivity extends AppCompatActivity {
    private TextView totalIncomeADay,totalNumberOder;
    private DatabaseReference IncomeDb;
    String database= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private DatabaseReference incomeDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_driver);
        String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        incomeDbRef = FirebaseDatabase.getInstance(database).getReference().child("Users").child("Drivers")
                .child(currentUid);
        totalIncomeADay=findViewById(R.id.income_today);
        totalNumberOder=findViewById(R.id.total_order_today);
        Toolbar toolbar = findViewById(R.id.income_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thu nhập");
        calculateIncome();

    }
    private String getDateToday() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String datetime =formatter.format(date);
        return datetime;
        /*nt ratingSum = 0;
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
        mRatingText.setText(String.valueOf(ratingsAvg));*/
    }

    private void calculateIncome() {
        incomeDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                   // String name_chat=dataSnapshot.child("name").getValue().toString();
                    int Sum = 0;
                    int Total = 0;
                    for (DataSnapshot child : dataSnapshot.child("income").child(getDateToday()).getChildren()) {
                        Sum = Sum + Integer.valueOf(child.getValue().toString());
                        Total++;
                    }
                    totalNumberOder.setText(String.valueOf(Total));
                    totalIncomeADay.setText(String.valueOf(Sum) +" đ");
                    }

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });






    }


}
