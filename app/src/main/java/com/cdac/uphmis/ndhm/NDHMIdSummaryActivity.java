package com.cdac.uphmis.ndhm;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ManagingSharedData;

public class NDHMIdSummaryActivity extends AppCompatActivity {
private TextView tvName,tvGender,tvMobileNo,tvDob,tvHealthIdNo,tvHealthId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_d_h_m_id_summary);
        
        ManagingSharedData msd=new ManagingSharedData(this);
        initializeViews();

        Intent intent=getIntent();
        if (intent.getExtras()!=null)
        {
            String healthId=intent.getStringExtra("healthId");
            String healthIdNo=intent.getStringExtra("healthIdNo");


            tvHealthId.setText(healthId);
            tvHealthIdNo.setText(healthIdNo);
            tvName.setText(msd.getPatientDetails().getFirstname());
            tvGender.setText(msd.getPatientDetails().getGender());
            tvMobileNo.setText(msd.getPatientDetails().getMobileNo());
            tvDob.setText(msd.getPatientDetails().getUmidData().getDob());
        }
    }

    private void initializeViews() {
        tvName=findViewById(R.id.tv_name);
        tvMobileNo=findViewById(R.id.tv_mobile_no);
        tvGender=findViewById(R.id.tv_gender);
        tvDob=findViewById(R.id.tv_dob);
        tvHealthIdNo=findViewById(R.id.tv_health_id_number);
        tvHealthId=findViewById(R.id.tv_health_id);
    }

    public void btnHome(View view) {
        finish();
    }
}