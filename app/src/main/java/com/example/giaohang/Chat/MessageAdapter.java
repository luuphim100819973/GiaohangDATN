package com.example.giaohang.Chat;



import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.giaohang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    String database= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);
        return new MessageViewHolder(v);
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        Messages c = mMessageList.get(i);
        String from_user = c.getFrom();
        String message_type = c.getType();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String current_user_id = "temp";
        if (user != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
        }
      //  Messages c = mMessageList.get(i);
       // String from_user = c.getFrom();
        if (from_user.equals(current_user_id)) {
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);
            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background2);
        } else {
            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }
        viewHolder.messageText.setText(c.getMessage());
        mUserDatabase = FirebaseDatabase.getInstance(database).getReference().child("Users").child("Drivers").child(from_user);
        /*mUserDatabase = FirebaseDatabase.getInstance(database).getReference().child("Users")
                .child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());*/
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                   // String image = dataSnapshot.child("profileImageUrl").getValue().toString();
                    viewHolder.displayName.setText(name);
                    if(dataSnapshot.child("profileImageUrl").exists()){
                        /*Glide.with(viewHolder.profileImage.getContext())
                                .load(dataSnapshot.child("profileImageUrl").getValue().toString())
                                .apply(RequestOptions.circleCropTransform())
                                .into(viewHolder.profileImage);*/
                    }
                }
                else {
                    mUserDatabase = FirebaseDatabase.getInstance(database).getReference().child("Users").child("Customers").child(from_user);
                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String name = dataSnapshot.child("name").getValue().toString();
                                String image = dataSnapshot.child("profileImageUrl").getValue().toString();

                                viewHolder.displayName.setText(name);
                                /*Glide.with(viewHolder.profileImage.getContext())
                                        .load(dataSnapshot.child("profileImageUrl").getValue().toString())
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(viewHolder.profileImage);*/
                            }


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                //Picasso.with(viewHolder.profileImage.getContext()).load(image)
                 //       .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(message_type.equals("text")) {
            /*viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);*/
        } else {
            /*viewHolder.messageText.setVisibility(View.INVISIBLE);*/
            /*Glide.with(viewHolder.profileImage.getContext())
                    .load(c.getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);*/
            /*Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);*/

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}
/*
package com.example.giaohang.Chat;

        import android.graphics.Color;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.example.giaohang.R;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

        import java.util.List;

        import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout,parent,false);
        return new MessageViewHolder(v);
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public CircleImageView profileImage;
        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView)view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView)view.findViewById(R.id.message_profile_layout);
        }
    }
    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder,int i) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String current_user_id = "temp";
        if (user != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
        }
        Messages c = mMessageList.get(i);
        String from_user = c.getFrom();
        if (from_user.equals(current_user_id)) {
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);
        } else {
            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }
        viewHolder.messageText.setText(c.getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}*/
