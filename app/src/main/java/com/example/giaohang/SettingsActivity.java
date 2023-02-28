package com.example.giaohang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.giaohang.Customer.CustomerMapActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {
    private static final int MAX_LENGTH = 50;
    private Button mChangImage;
    private Button mChangStatus;
    private TextView mDisplayName;
    private TextView mStatus;
    private CircleImageView mCircleImageView;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabaseReference;
    private StorageReference storageRef;
    private static final int GALLERY_Pick=1;
    private Toolbar mToolbar;
    String database="https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDisplayName = (TextView) findViewById(R.id.setting_name);
        mStatus =(TextView) findViewById(R.id.setting_status);
        mChangStatus = (Button) findViewById(R.id.setting_status_btn);
        mChangImage = (Button)  findViewById(R.id.setting_image_btn);
        mCircleImageView = (CircleImageView) findViewById(R.id.settings_image);
        mToolbar= findViewById(R.id.tootbar_settings_user);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String current_id= mAuth.getUid();
        storageRef = FirebaseStorage.getInstance("gs://delivery-b9821.appspot.com").getReference();
        mDatabaseReference= FirebaseDatabase.getInstance(database).getReference().child("Users").child("Drivers").child(current_id);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name= snapshot.child("name").getValue().toString();
           //     String status = snapshot.child("status").getValue().toString();
            //    String image =snapshot.child("image").getValue().toString();
             //   String thumb= snapshot.child("thumb").getValue().toString();
                mDisplayName.setText(name);
           //     mStatus.setText(status);
             //   if(!image.equals("defaults")){

              //      Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avatar_10).into(mCircleImageView);
             //   }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mChangStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status_display = mStatus.getText().toString();
                Intent newIntent = new Intent(SettingsActivity.this, StatusActivity.class);
                newIntent.putExtra("status_link",status_display);
                startActivity(newIntent);

            }
        });
        mChangImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"select image"),GALLERY_Pick);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_Pick && resultCode == RESULT_OK){
            String current_id = mAuth.getUid();

            String imageUrl =data.getDataString();
           // String randomname = mAuth.getUid();

            final StorageReference profileImageRef = FirebaseStorage
                    .getInstance("gs://delivery-b9821.appspot.com").getReference()
                    .child("profile_images").child(current_id+".png");
            /*final StorageReference thumbImageRef = FirebaseStorage.getInstance("gs://delivery-b9821.appspot.com")
                    .getReference().child("profile_images/thumb_images").child(current_id+".png");*/
            if (imageUrl != null) {
                profileImageRef.putFile(Uri.parse(imageUrl))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // profileImageUrl taskSnapshot.getDownloadUrl().toString(); //this is depreciated
                                //this is the new way to do it
                                profileImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String profileImageUrl=task.getResult().toString();
                                        mDatabaseReference.child("profileImageUrl").setValue(profileImageUrl);
                                        Log.i("URL",profileImageUrl);
                                    }
                                });
                                /*thumbImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String profileImageUrl=task.getResult().toString();
                                        mDatabaseReference.child("thumb").setValue(profileImageUrl);
                                        Log.i("URL",profileImageUrl);
                                    }
                                });*/
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                              //  progressBar.setVisibility(View.GONE);
                                Toast.makeText(SettingsActivity.this, "aaa "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }
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

}



/*


public class SettingsActivity extends AppCompatActivity {
    private static final int MAX_LENGTH = 50;
    private Button mChangImage;
    private Button mChangStatus;
    private TextView mDisplayName;
    private TextView mStatus;
    private CircleImageView mCircleImageView;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabaseReference;

    private StorageReference storageRef;
    private static final int GALLERY_Pick=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDisplayName = (TextView) findViewById(R.id.setting_name);
        mStatus =(TextView) findViewById(R.id.setting_status);
        mChangStatus = (Button) findViewById(R.id.setting_status_btn);
        mChangImage = (Button)  findViewById(R.id.setting_image_btn);
        mCircleImageView = (CircleImageView) findViewById(R.id.settings_image);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String current_id= mAuth.getUid();
        storageRef = FirebaseStorage.getInstance("gs://delivery-b9821.appspot.com").getReference();
        mDatabaseReference= FirebaseDatabase.getInstance("https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Users").child(current_id);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name= snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String image =snapshot.child("image").getValue().toString();
                //   String thumb= snapshot.child("thumb").getValue().toString();

                mDisplayName.setText(name);
                mStatus.setText(status);
                if(!image.equals("defaults")){
                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avatar_10).into(mCircleImageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mChangStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status_display = mStatus.getText().toString();
                Intent newIntent = new Intent(SettingsActivity.this, StatusActivity.class);
                newIntent.putExtra("status_link",status_display);
                startActivity(newIntent);

            }
        });
        mChangImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"select image"),GALLERY_Pick);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_Pick && resultCode == RESULT_OK){
            String current_id = mAuth.getUid();

            String imageUrl =data.getDataString();
            // String randomname = mAuth.getUid();


            final StorageReference profileImageRef = FirebaseStorage.getInstance("gs://delivery-b9821.appspot.com").getReference().child("storage_images").child(current_id+".png");
            final StorageReference thumbImageRef = FirebaseStorage.getInstance("gs://delivery-b9821.appspot.com").getReference().child("storage_images/thumb_images").child(current_id+".png");
            if (imageUrl != null) {
                profileImageRef.putFile(Uri.parse(imageUrl))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // profileImageUrl taskSnapshot.getDownloadUrl().toString(); //this is depreciated

                                //this is the new way to do it
                                profileImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String profileImageUrl=task.getResult().toString();
                                        mDatabaseReference.child("image").setValue(profileImageUrl);
                                        Log.i("URL",profileImageUrl);
                                    }
                                });
                                */
/*thumbImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String profileImageUrl=task.getResult().toString();
                                        mDatabaseReference.child("thumb").setValue(profileImageUrl);
                                        Log.i("URL",profileImageUrl);
                                    }
                                });*//*

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //  progressBar.setVisibility(View.GONE);
                                Toast.makeText(SettingsActivity.this, "aaa "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }
    }

}
*/
