package com.mostafa.fci.flowerserverapp.Services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mostafa.fci.flowerserverapp.Activities.OrdersActivity;
import com.mostafa.fci.flowerserverapp.R;

import java.util.Map;

public class FBMessagingServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0 ){
            Map<String,String> payload = remoteMessage.getData();
            showNotification(payload);
        }else
            showNotification(remoteMessage);
    }

    private void showNotification(Map<String, String> payload) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, OrdersActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.flowerlog3)
                .setTicker(payload.get("title")).setWhen(0)
                .setContentTitle(payload.get("title"))
                .setContentText(payload.get("message"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(payload.get("message")))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.flowerlog3));

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }
    private void showNotification(RemoteMessage message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Notification From Server")
                .setContentText(message.getNotification().getBody());
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


}
