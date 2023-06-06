package com.cdac.uphmis;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.adapter.PatientProfileAdapter;
import com.cdac.uphmis.model.PatientProfileDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PatientProfileActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private ManagingSharedData msd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);


        profileImageView = findViewById(R.id.expandedImage);

        msd = new ManagingSharedData(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout coll_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        coll_toolbar.setTitle(msd.getPatientDetails().getFirstname());
        coll_toolbar.setExpandedTitleColor(Color.parseColor("#ffffff"));
        coll_toolbar.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        setProfileData();
        if (msd.getPatientDetails().getCrno() != null){
            getPatientProfileImage();
        }


     /*   try {
            File f = new File(getFilesDir().toString(), "/"+msd.getPatientDetails().getCrno()+".jpg");
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            if (bmp != null) {
                profileImageView.setImageBitmap(bmp);
            }else {
                getPatientProfileImage();
            }
        } catch (Exception ex) {
        }*/
    }


    private void getPatientProfileImage() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/getImageByCrNoAndUmid?crNo=" + msd.getPatientDetails().getCrno() + "&episodeCode=&hospCode=&seatid=&umid=", response -> {
            Log.i("resopnse", "onResponse: ");
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                JSONArray jsonArray = jsonObject.getJSONArray("profilePicBase64");
                if (status.equalsIgnoreCase("1")) {
                    if (jsonArray.length() > 0) {
                        String profileBase64 = jsonArray.getJSONObject(0).optString("IMAGEDATA");
                        byte[] decodedString = Base64.decode(profileBase64, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        //navImage.setImageBitmap(decodedByte);
                        profileImageView.setImageBitmap(decodedByte);

                        saveFile(decodedString, msd.getPatientDetails().getCrno() + ".jpg");
                    } else {
                        setDefaultAvatar();
                    }


                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, error -> {
            Log.i("error", "onResponse: ");
            AppUtilityFunctions.handleExceptions(error, PatientProfileActivity.this);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void saveFile(byte[] data, String fileName) throws RuntimeException {
        if (!getFilesDir().exists() && !getFilesDir().mkdirs()) {
            return;
        }
        File mainPicture = new File(getFilesDir().toString(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(mainPicture);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaultAvatar() {
        if (msd.getPatientDetails().getGender().equalsIgnoreCase("F")) {
            profileImageView.setImageDrawable(getDrawable(R.drawable.woman));
        } else {
            profileImageView.setImageDrawable(getDrawable(R.drawable.man));
        }
    }

    private void setProfileData() {
        List<PatientProfileDetails> patientProfileDetailsList = new ArrayList<>();
        if (!(msd.getPatientDetails().getCrno() == null || msd.getPatientDetails().getCrno().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.cr_no), msd.getPatientDetails().getCrno()));
        }
        if (!(msd.getPatientDetails().getUmidData() == null || msd.getPatientDetails().getIsSailEmployee().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.emp_id), msd.getPatientDetails().getUmidData().getUmidNo()));
        }
        if (!(msd.getPatientDetails().getFirstname() == null || msd.getPatientDetails().getFirstname().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.name) + "", msd.getPatientDetails().getFirstname()));
        }
        if (!(msd.getPatientDetails().getAge() == null || msd.getPatientDetails().getAge().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.age), msd.getPatientDetails().getAge()));
        }
        if (!(msd.getPatientDetails().getGender() == null || msd.getPatientDetails().getGender().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.gender) + "", msd.getPatientDetails().getGender()));
        }
       /* if (!(msd.getPatientDetails().getFathername() == null || msd.getPatientDetails().getFathername().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.father_name), msd.getPatientDetails().getFathername()));
        }*/
        if (!(msd.getPatientDetails().getSPOUSE_NAME() == null || msd.getPatientDetails().getSPOUSE_NAME().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.spouse_name), msd.getPatientDetails().getSPOUSE_NAME()));
        }
        /*if (!(msd.getPatientDetails().getSublocality() == null || msd.getPatientDetails().getSublocality().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.address), msd.getPatientDetails().getSublocality() + ", " + msd.getPatientDetails().getCity() + ", " + msd.getPatientDetails().getDistrict_name() + ", " + msd.getPatientDetails().getState_name() + " - " + msd.getPatientDetails().getPincode()));
        }*/
        if (!(msd.getPatientDetails().getMobileNo() == null || msd.getPatientDetails().getMobileNo().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.mobile_no), msd.getPatientDetails().getMobileNo()));
        }
        if (!(msd.getPatientDetails().getEmailId() == null || msd.getPatientDetails().getEmailId().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.email_id), msd.getPatientDetails().getEmailId()));
        }

        if (!(msd.getPatientDetails().getCatName() == null || msd.getPatientDetails().getCatName().isEmpty())) {
            patientProfileDetailsList.add(new PatientProfileDetails(getString(R.string.category), msd.getPatientDetails().getCatName()));
        }
        setUpRecyclerView(patientProfileDetailsList);
    }

    private void setUpRecyclerView(List<PatientProfileDetails> PatientProfileDetailsList) {
        if (PatientProfileDetailsList != null && PatientProfileDetailsList.size() > 0) {
            RecyclerView recyclerView = findViewById(R.id.rv_profile);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            PatientProfileAdapter adapter = new PatientProfileAdapter(this, PatientProfileDetailsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}