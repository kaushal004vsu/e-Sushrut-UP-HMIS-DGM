package com.cdac.uphmis.appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.adapter.MyAppointmentsAdapter;
import com.cdac.uphmis.appointment.model.MyAppointmentDetails;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class AppointmentStatusActivity extends AppCompatActivity {

    private Spinner forSpinner;
    private RequestQueue requestQueue;

    private ManagingSharedData msd;

    private ListView lvAppointments;
    private String crno="";
    private TextView tvNoRecordsFound;

    GeometricProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_status);
        
        requestQueue= Volley.newRequestQueue(this);
        msd = new ManagingSharedData(this);
        initializeViews();
        getCrList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressView = (GeometricProgressView) findViewById(R.id.progress_view);

    }


    private void initializeViews()
    {
        forSpinner = findViewById(R.id.for_spinner);
        lvAppointments=findViewById(R.id.lv_appointments);
        tvNoRecordsFound=findViewById(R.id.no_record_found);




        forSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                PatientDetails PatientDetails = (PatientDetails) forSpinner.getSelectedItem();
                crno=PatientDetails.getCrno();
                getAppointmentsFromCr(crno);
            }
            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });
    }

    private void getCrList() {

        final ArrayList<PatientDetails> patientDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + msd.getPatientDetails().getMobileNo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);

//                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    PatientDetails setDefaultPatient = null;

                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String umidData = jsonArray.getJSONObject(i).optString("UMID_DATA");
                            if (umidData.isEmpty())
                            {
                                jsonArray.getJSONObject(i).remove("UMID_DATA");
                            }
                            else
                            {
                                JSONObject jsonObject1=new JSONObject(umidData);
                                jsonArray.getJSONObject(i).put("UMID_DATA",jsonObject1);
                            }
                            PatientDetails patientDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), PatientDetails.class);
                            patientDetailsList.add(patientDetails);

                        if (msd.getPatientDetails() != null) {
                            if (msd.getPatientDetails().toString().equals(patientDetailsList.get(i).toString())) {
                                setDefaultPatient = patientDetailsList.get(i);
                            }
                        }
                    }


                    ArrayAdapter adapter = new ArrayAdapter(AppointmentStatusActivity.this, R.layout.for_layout, patientDetailsList);
                    forSpinner.setAdapter(adapter);


                    if (msd.getPatientDetails() != null) {

                        Log.i(TAG, "onResponse: " + msd.getPatientDetails());
                        if (setDefaultPatient != null) {
                            int spinnerPosition = adapter.getPosition(setDefaultPatient);

                            forSpinner.setSelection(spinnerPosition);

                        }

                    }

                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }
        , error -> {
            Log.i("error", "onErrorResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, AppointmentStatusActivity.this);
//                progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void getAppointmentsFromCr(String crno) {
        progressView.setVisibility(View.VISIBLE);


        final ArrayList<MyAppointmentDetails> myAppointmentDetailsArrayList = new ArrayList();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPreviousAppointmentsByCRNoUrl + crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//        String response = "{\"status\":\"1\",\"appointments_list\":[{\"APPOINTMENTNO\":\"961011902000094\",\"PATCRNO\":\"\",\"EPISODECODE\":\"0\",\"PATFIRSTNAME\":\"gg\",\"PATMIDDLENAME\":\" \",\"PATLASTNAME\":\" \",\"PATGUARDIANNAME\":\"testfather\",\"PATGENDERCODE\":\"F\",\"EMAILID\":\"\",\"MOBILENO\":\"9599566882\",\"APPOINTMENTQUEUENO\":\"1\",\"APPOINTMENTTIME\":\"07:30\",\"APPOINTMENTSTATUS\":\"1\",\"STATUSREMARKS\":\"\",\"SLOTTYPE\":\"3\",\"REMARKS\":\"\",\"APPOINTMENTTYPEID\":\"1\",\"MODULESPECIFICCODE\":\"13\",\"APPOINTMENTMODE\":\"\",\"MODULESPECIFICKEYNAME\":\"\",\"PATAGE\":\"55 Yr\",\"PATSPOUSENAME\":\"\",\"APPOINTMENTDATE\":\"21-Feb-2019\",\"APPOINTMENTFORID\":\"1\",\"APPOINTMENTFORNAME\":\"Special Clinic\",\"ACTULAPARAID1\":\"10311\",\"ACTULAPARAID2\":\"0\",\"ACTULAPARAID3\":\"0\",\"ACTULAPARAID4\":\"0\",\"ACTULAPARAID5\":\"0\",\"ACTULAPARAID6\":\"0\",\"ACTULAPARAID7\":\"0\",\"ACTULAPARANAME1\":\"Cardiology(Cardio Spl)\",\"ACTULAPARANAME2\":\" \",\"ACTULAPARANAME3\":\" \",\"ACTULAPARANAME4\":\" \",\"ACTULAPARANAME5\":\" \",\"ACTULAPARANAME6\":\" \",\"ACTULAPARANAME7\":\" \"}]}";

                Log.i("response is ", "onResponse: " + response);

                try {
                    progressView.setVisibility(View.GONE);


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appointmentList = jsonObject.getJSONArray("appointments_list");

                    if (appointmentList.length() == 0) {
                        Toast.makeText(AppointmentStatusActivity.this, "No appointments found.", Toast.LENGTH_SHORT).show();
                        tvNoRecordsFound.setVisibility(View.VISIBLE);
                        lvAppointments.setVisibility(View.GONE);

                    } else {
                        tvNoRecordsFound.setVisibility(View.GONE);
                        lvAppointments.setVisibility(View.VISIBLE);
                        for (int i = 0; i < appointmentList.length(); i++) {
                            JSONObject c = appointmentList.getJSONObject(i);
                            String appointmentno = c.getString("APPOINTMENTNO");
                            String patcrno = c.getString("PATCRNO");
                            String episodecode = c.getString("EPISODECODE");
                            String patfirstname = c.getString("PATFIRSTNAME");
                            String patmiddlename = c.getString("PATMIDDLENAME");
                            String patlastname = c.getString("PATLASTNAME");
                            String patguardianname = c.getString("PATGUARDIANNAME");
                            String patgendercode = c.getString("PATGENDERCODE");
                            String emailid = c.getString("EMAILID");
                            String mobileno = c.getString("MOBILENO");
                            String appointmentqueueno = c.getString("APPOINTMENTQUEUENO");
                            String appointmenttime = c.getString("APPOINTMENTTIME");
                            String appointmentstatus = c.getString("APPOINTMENTSTATUS");
                            String statusremarks = c.getString("STATUSREMARKS");
                            String slottype = c.getString("SLOTTYPE");
                            String remarks = c.getString("REMARKS");
                            String appointmenttypeid = c.getString("APPOINTMENTTYPEID");
                            String modulespecificcode = c.getString("MODULESPECIFICCODE");
                            String appointmentmode = c.getString("APPOINTMENTMODE");
                            String modulespecifickeyname = c.getString("MODULESPECIFICKEYNAME");
                            String patage = c.getString("PATAGE");
                            String patspousename = c.getString("PATSPOUSENAME");
                            String appointmentdate = c.getString("APPOINTMENTDATE");
                            String appointmentforid = c.getString("APPOINTMENTFORID");
                            String appointmentforname = c.getString("APPOINTMENTFORNAME");
                            String actulaparaid1 = c.getString("ACTUALPARAID1");
                            String actulaparaid2 = c.getString("ACTUALPARAID2");
                            String actulaparaid3 = c.getString("ACTUALPARAID3");
                            String actulaparaid4 = c.getString("ACTUALPARAID4");
                            String actulaparaid5 = c.getString("ACTUALPARAID5");
                            String actulaparaid6 = c.getString("ACTUALPARAID6");
                            String actulaparaid7 = c.getString("ACTUALPARAID7");
                            String actulaparaname1 = c.getString("ACTUALPARANAME1");
                            String actulaparaname2 = c.getString("ACTUALPARANAME2");
                            String actulaparaname3 = c.getString("ACTUALPARANAME3");
                            String actulaparaname4 = c.getString("ACTUALPARANAME4");
                            String actulaparaname5 = c.getString("ACTUALPARANAME5");
                            String actulaparaname6 = c.getString("ACTUALPARANAME6");
                            String actulaparaname7 = c.getString("ACTUALPARANAME7");


//                            myAppointmentDetailsArrayList.add(new MyAppointmentDetails(appointmentno, patcrno, episodecode, patfirstname, patmiddlename, patlastname, patguardianname, patgendercode, emailid, mobileno, appointmentqueueno, appointmenttime, appointmentstatus, statusremarks, slottype, remarks, appointmenttypeid, modulespecificcode, appointmentmode, modulespecifickeyname, patage, patspousename, appointmentdate, appointmentforid, appointmentforname, actulaparaid1, actulaparaid2, actulaparaid3, actulaparaid4, actulaparaid5, actulaparaid6, actulaparaid7, actulaparaname1, actulaparaname2, actulaparaname3, actulaparaname4, actulaparaname5, actulaparaname6, actulaparaname7));




                        }

                       MyAppointmentsAdapter adapter = new MyAppointmentsAdapter(AppointmentStatusActivity.this, myAppointmentDetailsArrayList);
                        lvAppointments.setAdapter(adapter);

                    }
                } catch (final JSONException e) {
                    progressView.setVisibility(View.GONE);
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                Log.i("error", "onErrorResponse: " + error);
               AppUtilityFunctions.handleExceptions(error,AppointmentStatusActivity.this);
                //                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(AppointmentStatusActivity.this);
                return true;
            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;
           /* case R.id.action_help:
                startActivity(new Intent(SlotSelectionActivity.this, HelpActivity.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}