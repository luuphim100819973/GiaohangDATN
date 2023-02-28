package com.example.giaohang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaohang.Customer.CustomerMapActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUserDatabase;
    private personAdapter adapter;
    String database ="https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mToolbar = (Toolbar) findViewById(R.id.user_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //gs://delivery-b9821.appspot.com/
        //https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app/
        mUserDatabase = FirebaseDatabase.getInstance(database).getReference().child("Users").child("Drivers");
        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Users> options
                = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUserDatabase, Users.class)
                .build();
        adapter = new personAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        mUsersList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            Intent intent = new Intent(this, CustomerMapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public class personAdapter extends FirebaseRecyclerAdapter<Users,personAdapter.personsViewHolder> {
        public personAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
            super(options);
        }
        @Override
        protected void onBindViewHolder(@NonNull personsViewHolder holder, int position, @NonNull Users model) {
            holder.name.setText(model.getName());
          //  holder.status.setText(model.getStatus());
            //     holder.getUserImage();
            ///.,   Picasso.with().load().placeholder(R.drawable.avatar_10).into(holder.circleImageView);
            String user_uid = getRef(position).getKey();
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                    profileIntent.putExtra("user_id", user_uid);
               //   String m=mUserDatabase.child(user_uid).child("token_id").get
                   /* FCMsend.FirebaseMessagingService(UsersActivity.this,
                            "egLxL23qTvCOHPDYJcEJxd:APA91bETsfGcG3dc0M84NVIc_nLL9KykTKokm6QAO5r0-ecqPmPmEFFaA45oqwOdHjNA8ZoszvNwL1x2AebMZrn65efJdbAZeeRjojDU4MBd_nCc0gUGqD3kltCqQyKdtGfWx3aZPJCc",
                            //     "enDS1PRyTqm_S5te4ZzzTn:APA91bEazSNOWBePnEUxQFNKnV6xS-8NWCMO0W5xqtsXyPPAmEnTveaaCh-yr8X0LY4MgHZqvAACXz-D9S1Aw5bTZrTTZoG65ZXenYaXB4RzHLegT28UjBDuFHBCA9KswLWKPdajtwco",
                            //     "c3ywzpF-RGS1_tKxTmEVFs:APA91bGFre9DiUCSrSeTUOhzRe5t2ppdT1FJsStJdmrkfkPKeMzmWlNJJKJqOtqpTRSttqy7dDsP8I0TMFJa3H1oYeiZzcXSYfEiGfx5__CgqZrVDld26PSlOUliJJtxB43QGlegeCUl",
                            "new Order",
                            "New Order");*/
                    startActivity(profileIntent);
                }
            });
        }
        @NonNull
        @Override
        public personsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view
                    = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_single_layout, parent, false);
            return new personsViewHolder(view);
        }
        public class personsViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView name;
           // TextView status;
            CircleImageView circleImageView;

            public personsViewHolder(@NonNull View itemView) {
                super(itemView);
                mView=itemView;
                name = (TextView) mView.findViewById(R.id.use_single_name);
            //    status = (TextView) mView.findViewById(R.id.user_single_data);
            }
            public void getUserImage(String thumb) {
                circleImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
                //  Picasso.with(txt).load(thumb).placeholder(R.drawable.avatar_10).into(circleImageView);
            }
        }
    }

}






