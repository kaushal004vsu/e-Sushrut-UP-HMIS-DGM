package com.cdac.uphmis.Notofication;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    private static final String TAG="MyFirebaseInstanceServi";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken =           FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(s);
    }
    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }
}