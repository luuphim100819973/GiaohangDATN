package com.example.giaohang;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.giaohang.Driver.DriverMapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.RemoteMessage;
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String database = "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private Uri alarmSound;
    private final long[] pattern = {100, 300, 300, 300};
    private FirebaseUser mAuth;
    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";
    private DatabaseReference mDatabaseReference;
    NotificationManager notificationManager;
    public boolean shouldSound;
    public boolean shouldVibrate;

    @TargetApi(Build.VERSION_CODES.O)
    public void registerNormalNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel_all = new NotificationChannel("CHANNEL_ID_ALL", "CHANNEL_NAME_ALL", NotificationManager.IMPORTANCE_HIGH);
        channel_all.enableVibration(true);
        notificationManager.createNotificationChannel(channel_all);
    }/*
    if (isOreoOrAbove()) {
        setupNotificationChannels();
    }*/

    private void setupNotificationChannels() {
        registerNormalNotificationChannel(notificationManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_message = remoteMessage.getNotification().getBody();
        CharSequence name;
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String current_id = notification_message;
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/raw/sound_noti");
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        mDatabaseReference = FirebaseDatabase.getInstance(database).getReference().child("Drivers").child(current_id);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "message notification",
                NotificationManager.IMPORTANCE_HIGH);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setSound(sound, attributes);
        Intent resultIntent = new Intent(this, DriverMapActivity.class);
        resultIntent.putExtra("from_user_id", current_id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        // getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder mBuilder =
                new Notification.Builder(this, CHANNEL_ID)
                        //  .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                        .setSmallIcon(R.mipmap.ic_giao_hang)
                        .setContentTitle(notification_title)
                        //  .setSound(alarmSound)
                        //    .setVibrate(pattern)
                        .setContentText(notification_message)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, mBuilder.build());
        super.onMessageReceived(remoteMessage);
    }
}
