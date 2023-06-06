package com.cdac.uphmis.appointment;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cdac.uphmis.PatientDrawerHomeActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.util.ManagingSharedData;

public class AppointmentSummaryActivity extends AppCompatActivity {
    OPDAppointmentDetails screeningDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_summary);
        
        screeningDetails = (OPDAppointmentDetails) getIntent().getSerializableExtra("appointmentDetails");
        String apptno = getIntent().getStringExtra("apptno");

        TextView tvMessage = findViewById(R.id.tv_message);

        TextView tvAppointmentDetails = findViewById(R.id.tv_appointment_details);
        TextView tvFurtherAssistance = findViewById(R.id.tv_further_assistance);

        tvFurtherAssistance.setText(Html.fromHtml(getString(R.string.tv_further_assistance)));

//        if (!screeningDetails.getPatCrNo().equalsIgnoreCase("0")) {
//            tvCrno.setVisibility(View.GONE);
//            tvMessageCrno.setVisibility(View.GONE);
//        }
//        tvMessageCrno.setText(screeningDetails.getPatFirstName()+" "+screeningDetails.getPatLastName() + ", Please note down your Centralized Registration No. (CRNo.).This is one time generated UHID which you will be required to refer in all your future communication/requests with us or hospital.");

        String appointmentDetails = getString(R.string.appli_no_txt) + ":" + apptno + getString(R.string.br) + screeningDetails.getPatFirstName() + " " + screeningDetails.getPatLastName() + " ( " + getString(R.string.cr_no) + screeningDetails.getPatCrNo() + getString(R.string.your_appointment_booked) + screeningDetails.getDeptUnitName() + getString(R.string.dated) + screeningDetails.getAppointmentDate() + getString(R.string.reach_hosp_txt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAppointmentDetails.setText(Html.fromHtml(appointmentDetails, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvAppointmentDetails.setText(Html.fromHtml(appointmentDetails));
        }
//        tvAppointmentDetails.setText(screeningDetails.getPatFirstName()+" "+screeningDetails.getPatLastName() + " (" + screeningDetails.getPatGenderCode() + "/" + screeningDetails.getPatAge() + " Yr) \n"  + screeningDetails.getDeptUnitName()+"\n"+screeningDetails.getAppointmentDate()+" ("+screeningDetails.getSlotST()+" - "+screeningDetails.getSlotET()+")\n\n Your appointment number is "+apptno );
//        tvAppointmentDetails.setText(appointmentDetails);

        String sourceString = getString(R.string.track_stats);
        tvMessage.setText(Html.fromHtml(sourceString));

    }

    public void btnDone(View view) {
        ManagingSharedData msd = new ManagingSharedData(AppointmentSummaryActivity.this);
        if (msd.getWhichModuleToLogin().equalsIgnoreCase("healthworkerlogin")) {
            Intent intent = new Intent(AppointmentSummaryActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(AppointmentSummaryActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}