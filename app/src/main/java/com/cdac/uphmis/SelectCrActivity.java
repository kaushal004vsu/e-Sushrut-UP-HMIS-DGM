package com.cdac.uphmis;


import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.adapter.SelectCrAdapter;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.model.UMIDData;
import com.cdac.uphmis.util.ManagingSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SelectCrActivity extends AppCompatActivity {
    ManagingSharedData msd;
    ListView lv;
    SelectCrAdapter adapter;
    private CardView cardregistration;
    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.select_patient_profile);
        }
        Intent intent=getIntent();
        if (intent!=null) {
            from= intent.getStringExtra("from");
        }

        msd = new ManagingSharedData(this);
        // requestQueue = Volley.newRequestQueue(this);
        lv = findViewById(R.id.lv);
        cardregistration = findViewById(R.id.card_registration);
        cardregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectCrActivity.this, RegistrationActivity.class));
            }
        });


        if (msd.getPatientDetails() == null) {
            cardregistration.setVisibility(View.GONE);
        }
        Gson gson = new Gson();
        String response = msd.getCrList();
        ArrayList<PatientDetails> lstArrayList = gson.fromJson(response,
                new TypeToken<List<PatientDetails>>() {
                }.getType());
        adapter = new SelectCrAdapter(this, lstArrayList);
        lv.setAdapter(adapter);


/**
 * hide registration card if 6 or more patients are registered.
 */
        if (lstArrayList != null && lstArrayList.size() >= 6) {
            if (cardregistration != null) {
                cardregistration.setVisibility(View.GONE);
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(from.equals("Login")){
                    exitByBackKey();
                }else{
                this.finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(from.equals("Login")){
                exitByBackKey();
            }else {
                this.finish();
            }

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {


            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
        }

    }

    public void back(View view) {
        finish();
    }
}
