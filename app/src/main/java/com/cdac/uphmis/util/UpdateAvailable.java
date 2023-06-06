package com.cdac.uphmis.util;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.LoginMainScreenActivity;
import com.cdac.uphmis.R;


public class UpdateAvailable extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView btnExit, btnUpdate;
    public TextView tvUpdateMessage;
    String message;
    int isForceUpdate;

    public UpdateAvailable(Activity a, String message, int isForceUpdate) {
        super(a);
        this.c = a;
        this.message=message;
        this.isForceUpdate=isForceUpdate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_available);

        btnExit =  findViewById(R.id.btn_exit);
        btnUpdate =  findViewById(R.id.btn_update);
        tvUpdateMessage = (TextView) findViewById(R.id.tv_update_message);
        tvUpdateMessage.setText(message);
        btnExit.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        Log.i("isForceUpdate", "onCreate: "+isForceUpdate);

        if (isForceUpdate!=1)
        {

            btnExit.setText("Continue");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                if (isForceUpdate==1) {
                    c.finish();

                }
                else
                {
                    Intent i = new Intent(c, LoginMainScreenActivity.class);
                    c.startActivity(i);
                }
//                c.finish();
                break;
            case R.id.btn_update:

                 play();

                break;
            default:

                break;
        }
        c.finish();
    }

    public void play() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
        c.startActivity(intent);
    }
}