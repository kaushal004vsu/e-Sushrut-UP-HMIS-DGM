package com.cdac.uphmis.covid19;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cdac.uphmis.PatientDrawerHomeActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.model.ScreeningDetails;
import com.cdac.uphmis.util.ManagingSharedData;

public class AppointmentSuccefullActivity extends AppCompatActivity {
    private String requestId, crno, appointmentDetails;
    private ScreeningDetails screeningDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_succefull);
        
        requestId = getIntent().getStringExtra("requestId");
        crno = getIntent().getStringExtra("crno");
        appointmentDetails = getIntent().getStringExtra("apptDetails");
        screeningDetails = (ScreeningDetails) getIntent().getSerializableExtra("screeningDetails");

        TextView tvMessage = findViewById(R.id.tv_message);
        TextView tvMessageCrno = findViewById(R.id.tv_message_crno);
        TextView tvCrno = findViewById(R.id.tv_crno);
        TextView tvAppointmentDetails = findViewById(R.id.tv_appointment_details);
        TextView tvFurtherAssistance = findViewById(R.id.tv_further_assistance);

        tvFurtherAssistance.setText(Html.fromHtml(getString(R.string.tv_further_assistance)));
//        Log.i("dddd", "onCreate: "+crno+"\n"+screeningDetails.getCrno());
        tvCrno.setText(getString(R.string.cr_no) + crno);
        tvCrno.setVisibility(View.GONE);
//        if (!screeningDetails.getCrno().equalsIgnoreCase("0")) {
//            tvCrno.setVisibility(View.GONE);
//            tvMessageCrno.setVisibility(View.GONE);
//        }


//        tvMessageCrno.setText(screeningDetails.getPatName() + ", Please note down your Centralized Registration No. (CRNo.).This is one time generated UHID which you will be required to refer in all your future communication/requests with us or hospital.");


        String sourceString = getString(R.string.track_status2)+"("+appointmentDetails+getString(R.string.during_hosp_hour)+")";
        tvMessage.setText(Html.fromHtml(sourceString));


        tvAppointmentDetails.setText(screeningDetails.getPatName() + " (" + screeningDetails.getPatGender() + "/" + screeningDetails.getPatAge() + ") \t, "+getString(R.string.cr_no) +" : "+crno+"\n"+getString(R.string.mobile_no)+" : "+screeningDetails.getPatMobileNo()+getString(R.string.hospital_colon)+screeningDetails.getHospName()+ getString(R.string.department_unit)+screeningDetails.getDeptUnitName()  +getString(R.string.request_date)+ appointmentDetails  + "\n" );

        //        tvMessage.setText("Your Centralized registration number/CR number is " + requestId + ".Please refer this CR No for all your fututre communications.");
    }

    public void btnDone(View view) {
//        documentUploadDialog(requestId);
        ManagingSharedData msd=new ManagingSharedData(AppointmentSuccefullActivity.this);

            Intent intent = new Intent(AppointmentSuccefullActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

    }


}
