package com.cdac.uphmis.covid19;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.cdac.uphmis.R;

public class ChatBotActivity extends AppCompatActivity {
    BottomSheetDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        
        getWindow().setGravity(Gravity.BOTTOM);
        dialog = new BottomSheetDialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        dialog.setContentView(R.layout.chatbot_dialog);
        dialog.setTitle("Custom Alert Dialog");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout(width,500);



        Button btnCancel        = (Button) dialog.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


    }
}