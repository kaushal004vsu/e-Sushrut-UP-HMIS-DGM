package com.cdac.uphmis.Notofication;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

import com.cdac.uphmis.R;
import com.cdac.uphmis.SplashScreen;


public class RingActivity extends AppCompatActivity {
    Ringtone r;
    String passedArg, doctorsName;

    boolean isAcceptedOrRejected = false;
    Vibrator v;
    private TextView tvName;


    private static int times = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        
        Log.i("aaaa", "onCreate: ");

        tvName = findViewById(R.id.tv_name);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            passedArg = b.getString("requestId");
            doctorsName = b.getString("doctorsName");
            Log.i("passedArg", "onCreate: " + passedArg);
            tvName.setText(doctorsName);
        }


        Handler handler = new Handler();
        Runnable x = new Runnable() {
            @Override
            public void run() {
                if (isAcceptedOrRejected == false) {
                    r.stop();
                    finishAndRemoveTask();
                }
            }
        };
        handler.postDelayed(x, 20000);

        // Get instance of Vibrator from current Context
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0, 1000, 0, 1000, 0, 1000, 0, 20000, 100};

        v.vibrate(pattern, -1);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        r.play();
    }
//    }

    public void btnReject(View view) {
        r.stop();
        v.cancel();
        isAcceptedOrRejected = true;
        finishAndRemoveTask();

    }

    public void btnAccept(View view) {
        r.stop();
        v.cancel();

        times = times + 1;

        jitsiVideoCall(passedArg);
        isAcceptedOrRejected = true;
//        this.finishAndRemoveTask();

    }

    private void jitsiVideoCall(String requestId) {


        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
//            serverURL = new URL("https://meet.jit.si");
            serverURL = new URL("https://mconsultancy.uat.dcservices.in");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
//        ManagingSharedData msd = new ManagingSharedData(this);
     /*   JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setSubject(doctorsName)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);*/


        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
//                .setRoom("https://jitsi.cdac.in/" + patientDetails.getRequestID())
                .setSubject(doctorsName)
                .setRoom("https://mconsultancy.uat.dcservices.in/" + requestId)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.

        //     Intent intent = new Intent(RingActivity.this, JitsiActivity.class);
//        intent.setAction("org.jitsi.meet.CONFERENCE");
//        intent.putExtra("JitsiMeetConferenceOptions", options);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
//finish();
        JitsiMeetActivity.launch(RingActivity.this, options);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("aaaa", "onResume: ");

        if (times >= 1) {
            Intent intent = new Intent(this, SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            times=0;
            finish();


        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("aaaa", "onPause: ");
    }

    @Override
    public void onBackPressed() {

    }
}
