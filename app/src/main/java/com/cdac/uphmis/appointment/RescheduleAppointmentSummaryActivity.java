package com.cdac.uphmis.appointment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.cdac.uphmis.PatientDrawerHomeActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.model.MyAppointmentDetails;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.util.ManagingSharedData;

public class RescheduleAppointmentSummaryActivity extends AppCompatActivity {
    OPDAppointmentDetails appointmentDetails;
    MyAppointmentDetails patientDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_appointment_summary);
        
        appointmentDetails = (OPDAppointmentDetails) getIntent().getSerializableExtra("appointmentDetails");
        patientDetails = (MyAppointmentDetails) getIntent().getSerializableExtra("myAppointmentDetails");
        String apptno=getIntent().getStringExtra("apptno");
        TextView tvMessage = findViewById(R.id.tv_message);
        TextView tvAppointmentDetails = findViewById(R.id.tv_appointment_details);
        TextView tvFurtherAssistance = findViewById(R.id.tv_further_assistance);
        tvFurtherAssistance.setText(Html.fromHtml(getString(R.string.tv_further_assistance)));
        String appointmentDetailsMessge= "( Your Appointment No. is "+apptno+" )<br><br>"+patientDetails.getPatfirstname()+" "+patientDetails.getPatlastname() + " ( CRNO. " + patientDetails.getPatcrno()+" ),<br><br>Your Appointment is Booked for <b>\""+patientDetails.getActulaparaname1()+"\" </b>dated <b>\""+appointmentDetails.getAppointmentDate()+"\"</b> . Please reach the hospital registration counter to complete the hospital formalities.";

//        tvAppointmentDetails.setText(patientDetails.getPatfirstname()+" "+ patientDetails.getPatlastname() + " (" + patientDetails.getPatgendercode() + "/" + patientDetails.getPatage() + ") \n"  + patientDetails.getActulaparaname1()+"\n"+ appointmentDetails.getAppointmentDate()+" ("+appointmentDetails.getSlotST()+" - "+appointmentDetails.getSlotET()+")\n\n Your appointment number is "+apptno );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAppointmentDetails.setText(Html.fromHtml(appointmentDetailsMessge, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvAppointmentDetails.setText(Html.fromHtml(appointmentDetailsMessge));
        }
        String sourceString = "<br><br>You can track status of your request on appointment status through menu provided at home page. ";
        tvMessage.setText(Html.fromHtml(sourceString));
    }

    public void btnDone(View view) {
        ManagingSharedData msd = new ManagingSharedData(RescheduleAppointmentSummaryActivity.this);
        if (msd.getWhichModuleToLogin().equalsIgnoreCase("healthworkerlogin")) {
            Intent intent = new Intent(RescheduleAppointmentSummaryActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(RescheduleAppointmentSummaryActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}