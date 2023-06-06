package com.cdac.uphmis.Notofication;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import com.cdac.uphmis.R;
import com.cdac.uphmis.SplashScreen;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("content");
        Config.navigateTo = data.get("navigateTo");
        Log.i("ssss", "ssss: " + remoteMessage.getData());

        if (!data.get("navigateTo").equalsIgnoreCase("")) {

            String s=Config.content;
//            String s=remoteMessage.getData();
            Log.i("ssss", "ssss: " + s);
            String doctorsName=   s.substring(0 , s.indexOf("is") - 1);
//            String doctorsName=s.substring(s.indexOf(", ") , s.indexOf("is") - 1);

            Intent intent = new Intent(this, RingActivity.class);
            intent.putExtra("requestId", data.get("navigateTo"));
            intent.putExtra("doctorsName", doctorsName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
        sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
        if(isInBackground) {
            Log.i(TAG,"Your application is in background state");
        }else{
            Log.i(TAG, "sendNotification: foreground state");
            intent = new Intent();
        }


        Log.i("title", "getTitle: " + Config.title);
        Log.i("content", "getContent: " + Config.content);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)

                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Config.content))
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH);


        notificationManager.notify(1, notificationBuilder.build());


    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }





}