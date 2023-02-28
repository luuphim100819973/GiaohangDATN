package com.example.giaohang.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.giaohang.Adapters.HistoryAdapter2;
import com.example.giaohang.Objects.HistoryObject;
import com.example.giaohang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HistoryActivity2 extends AppCompatActivity {
    String database="https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app/";
    String customerOrDriver,userId;
    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);
        mHistoryRecyclerView = findViewById(R.id.historyRecyclerView2);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);
        mHistoryLayoutManager=new LinearLayoutManager(HistoryActivity2.this);
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter=new HistoryAdapter2(getDataSetHistory2(),HistoryActivity2.this);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        customerOrDriver=getIntent().getExtras().getString("customerOrDriver");
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistoryId();

    }

    private void getUserHistoryId() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance(database).getReference().child("Users")
                .child(customerOrDriver).child(userId).child("history");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot history: snapshot.getChildren()){
                        FetchRideInformation(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void FetchRideInformation(String rideKey) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance(database).getReference().child("history")
                .child(rideKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String rideId=snapshot.getKey();
                    Long timestamp=0L;
                    for(DataSnapshot child: snapshot.getChildren()){
                        if(child.getKey().equals("timestamp")){
                            timestamp=Long.valueOf(child.getValue().toString());
                        }
                    }
                    HistoryObject historyObject= new HistoryObject(rideId,getDate(timestamp));
                    resultHistory.add(historyObject);

                    mHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getDate(long timestamp) {
        // Create a DateFormatter object for displaying date in specified
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp*1000);
        return formatter.format(calendar.getTime());
    }

    private ArrayList resultHistory= new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory2() {
        return resultHistory;
    }


}