package com.cdac.uphmis.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.adapter.AppointmentDepartmentAdapter;
import com.cdac.uphmis.appointment.model.DepartmentDetails;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.android.volley.VolleyLog.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.getIndex;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class OPDAppointmentActivity extends AppCompatActivity {
//    private Spinner forSpinner;
//    View forSpinnerView;
    ManagingSharedData msd;
    RadioGroup radioGroup;
    GeometricProgressView progressView;
    private ListView lvDepartments;
    private TextView tvNoRecordsFound;
    private Spinner hospSpinner;
    private String hospitalCode;
    private LinearLayout llData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opdappointment);
        
        progressView =  findViewById(R.id.progress_view);
        tvNoRecordsFound = findViewById(R.id.tv_no_department_found);

        msd = new ManagingSharedData(this);
        Toolbar toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        if (intent!=null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
        }
        radioGroup = findViewById(R.id.radio_group_department_type);
        llData = findViewById(R.id.ll_data);
        initializeViews();

        //getCrList();
        hospSpinner = findViewById(R.id.hosp_spinner);
        getHospitals();

        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                progressView.setVisibility(View.GONE);
                HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();

                String hospCode = hospitalDetails.getHospCode();
                hospitalCode = hospCode;
                if (!hospCode.equalsIgnoreCase("-1")) {
                    tvNoRecordsFound.setVisibility(View.GONE);
                    llData.setVisibility(View.VISIBLE);
                    radioGroup.check(R.id.radio_general);
                    getDepartments("1");

                    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        switch (checkedId) {
                            case R.id.radio_general:
                                getDepartments("1");
                                break;
                            case R.id.radio_special:
                                getDepartments("2");
                                break;

                            case R.id.radio_all:
                                getAllDepartments();
                                break;
                        }
                    });


                } else {
                    tvNoRecordsFound.setText("Please Select Hospital");
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
                    llData.setVisibility(View.GONE);
                    Toast.makeText(OPDAppointmentActivity.this, "Please select hospital.", Toast.LENGTH_SHORT).show();
                }

            }
            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });
    }

    private void initializeViews() {
//        forSpinner = findViewById(R.id.for_spinner);
//        forSpinnerView = findViewById(R.id.for_spinner_view);

        lvDepartments = findViewById(R.id.department_list);
       /* forSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                PatientDetails PatientDetails = (PatientDetails) forSpinner.getSelectedItem();
                //getDepartments("1");
                radioGroup.clearCheck();
                radioGroup.check(R.id.radio_general);

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });*/

    }


 /*   private void getCrList() {

        final ArrayList<PatientDetails> patientDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + msd.getPatientDetails().getMobileNo(), response -> {
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


                ArrayAdapter adapter = new ArrayAdapter(OPDAppointmentActivity.this, R.layout.for_layout, patientDetailsList);
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
                , error -> {
            Log.i("error", "onErrorResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, OPDAppointmentActivity.this);
//                progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }*/


    public void getDepartments(String departmentType) {
        progressView.setVisibility(View.VISIBLE);
        PatientDetails PatientDetails = (PatientDetails)msd.getPatientDetails();

        Log.d("cr_noo",PatientDetails.getCrno());
        if (PatientDetails.getCrno()==null) {
            //getCrList();
            return;
        }
        final ArrayList<DepartmentDetails> departmentArrayList = new ArrayList();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.appointmentDepartments+"hospCode=" + hospitalCode+"&patRevisitFlag=0&patCrNo="+PatientDetails.getCrno(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray deptList = jsonObject.getJSONArray("dept_list");

                    if (deptList.length() == 0) {
                        Toast.makeText(OPDAppointmentActivity.this, "No departments found.", Toast.LENGTH_SHORT).show();
                        tvNoRecordsFound.setText("No Department Found");
                        lvDepartments.setVisibility(View.GONE);
                        tvNoRecordsFound.setVisibility(View.VISIBLE);
                    } else {
                        for (int i = 0; i < deptList.length(); i++) {
                            JSONObject c = deptList.getJSONObject(i);
                            String unitTypeCode = c.getString("UNIT_TYPE_CODE");
                            if (unitTypeCode.equalsIgnoreCase(departmentType)) {

                                String unitcode = c.getString("UNITCODE");
                                String loCode = c.getString("LOCCODE");
                                String loName = c.getString("LOCNAME");
                                String deptname = c.getString("DEPTNAME");
                                String workingDays = c.optString("WORKINGDAYS");
                                String newPatPortalLimit = c.optString("NEWPATPORTALLIMIT");
                                String oldpatportallimit = c.optString("OLDPATPORTALLIMIT");
                                String loweragelimit = c.optString("LOWERAGELIMIT");
                                String isrefer = c.optString("ISREFER");
                                String actualparameterreferenceid = c.getString("ACTUALPARAMETERREFERENCEID");
                                String isTeleconsUnit = c.optString("IS_TELECONS_UNIT");
                                String unitType = c.getString("UNIT_TYPE");

                                String tariffId = c.getString("TARIFF_ID");
                                String charge = c.getString("CHARGE");
                                if (charge.isEmpty())
                                    charge="0.00";
                                String inchargeName=c.optString("UNIT_INCHARGE");
                                String maxagelimit = "125";

                                    maxagelimit = c.optString("MAXAGELIMIT");

                                String boundGenderCode = c.optString("BOUNDGENDERCODE");

                                departmentArrayList.add(new DepartmentDetails(unitcode, loCode, loName, deptname, workingDays, newPatPortalLimit, oldpatportallimit, loweragelimit, maxagelimit, isrefer, actualparameterreferenceid, boundGenderCode, isTeleconsUnit, unitType, unitTypeCode, tariffId, charge,inchargeName));
                                Collections.sort(departmentArrayList, new Comparator<DepartmentDetails>() {
                                    public int compare(DepartmentDetails v1, DepartmentDetails v2) {
                                        return v1.getDeptname().compareTo(v2.getDeptname());
                                    }
                                });
                            }
                        }


                        PatientDetails PatientDetails = (PatientDetails) msd.getPatientDetails();
                        Log.i(TAG, "onResponse: "+PatientDetails.toString());

                        AppointmentDepartmentAdapter appointmentDepartmentAdapter = new AppointmentDepartmentAdapter(OPDAppointmentActivity.this, departmentArrayList, PatientDetails,hospitalCode);
                        lvDepartments.setAdapter(appointmentDepartmentAdapter);
//                        ArrayAdapter spinneradapter = new ArrayAdapter(OPDAppointmentActivity.this, R.layout.new_patient_custom_layout, departmentArrayList);
//                        spinnerDepartmentUnit.setAdapter(spinneradapter);

                        if (departmentArrayList.size()==0)
                        {
                            lvDepartments.setVisibility(View.GONE);
                            tvNoRecordsFound.setVisibility(View.VISIBLE);
                            tvNoRecordsFound.setText("No Department Found");
                        }else
                        {
                            lvDepartments.setVisibility(View.VISIBLE);
                            tvNoRecordsFound.setVisibility(View.GONE);
                        }

                    }
                } catch (final JSONException e) {
                    progressView.setVisibility(View.GONE);
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                if (OPDAppointmentActivity.this != null) {
                    progressView.setVisibility(View.GONE);
                    AppUtilityFunctions.handleExceptions(error, OPDAppointmentActivity.this);
                }
                //                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void getAllDepartments() {
        progressView.setVisibility(View.VISIBLE);
        PatientDetails PatientDetails = (PatientDetails) msd.getPatientDetails();
        if (PatientDetails.getCrno()==null)
        {
           // getCrList();
            return;
        }
        final ArrayList<DepartmentDetails> departmentArrayList = new ArrayList();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.appointmentDepartments+"hospCode=" + hospitalCode+"&patRevisitFlag=0&patCrNo="+PatientDetails.getCrno(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);

                Log.i("response is ", "onResponse: " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray deptList = jsonObject.getJSONArray("dept_list");

                    if (deptList.length() == 0) {
                        Toast.makeText(OPDAppointmentActivity.this, "No departments found.", Toast.LENGTH_SHORT).show();
                        lvDepartments.setVisibility(View.GONE);
                        tvNoRecordsFound.setVisibility(View.VISIBLE);
                    } else {
                        for (int i = 0; i < deptList.length(); i++) {
                            JSONObject c = deptList.getJSONObject(i);
                            String unitTypeCode = c.getString("UNIT_TYPE_CODE");
                            String unitcode = c.getString("UNITCODE");
                            String loCode = c.getString("LOCCODE");
                            String loName = c.getString("LOCNAME");
                            String deptname = c.getString("DEPTNAME");
                            String workingDays = c.optString("WORKINGDAYS");
                            String newPatPortalLimit = c.optString("NEWPATPORTALLIMIT");
                            String oldpatportallimit = c.optString("OLDPATPORTALLIMIT");
                            String loweragelimit = c.optString("LOWERAGELIMIT");
                            String isrefer = c.optString("ISREFER");
                            String actualparameterreferenceid = c.getString("ACTUALPARAMETERREFERENCEID");
                            String isTeleconsUnit = c.optString("IS_TELECONS_UNIT");
                            String unitType = c.getString("UNIT_TYPE");

                            String tariffId = c.getString("TARIFF_ID");
                            String charge = c.getString("CHARGE");
                            if (charge.isEmpty())
                                charge="0.00";
                            String inchargeName=c.optString("UNIT_INCHARGE");
                            String maxagelimit = "125";

                            maxagelimit = c.optString("MAXAGELIMIT");

                            String boundGenderCode = c.optString("BOUNDGENDERCODE");
                            departmentArrayList.add(new DepartmentDetails(unitcode, loCode, loName, deptname, workingDays, newPatPortalLimit, oldpatportallimit, loweragelimit, maxagelimit, isrefer, actualparameterreferenceid, boundGenderCode, isTeleconsUnit, unitType, unitTypeCode, tariffId, charge,inchargeName));
                            Collections.sort(departmentArrayList, new Comparator<DepartmentDetails>() {
                                public int compare(DepartmentDetails v1, DepartmentDetails v2) {
                                    return v1.getDeptname().compareTo(v2.getDeptname());
                                }
                            });

                        }


                        PatientDetails PatientDetails = (PatientDetails) msd.getPatientDetails();
                        AppointmentDepartmentAdapter appointmentDepartmentAdapter = new AppointmentDepartmentAdapter(OPDAppointmentActivity.this, departmentArrayList, PatientDetails,hospitalCode);
                        lvDepartments.setAdapter(appointmentDepartmentAdapter);
                        if (departmentArrayList.size()==0)
                        {
                            lvDepartments.setVisibility(View.GONE);
                            tvNoRecordsFound.setVisibility(View.VISIBLE);
                        }else
                        {
                            lvDepartments.setVisibility(View.VISIBLE);
                            tvNoRecordsFound.setVisibility(View.GONE);
                        }

                    }
                } catch (final JSONException e) {
                    progressView.setVisibility(View.GONE);
                    Log.i("jsonexception", "onResponse: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                if (OPDAppointmentActivity.this != null) {
                    progressView.setVisibility(View.GONE);
                    AppUtilityFunctions.handleExceptions(error, OPDAppointmentActivity.this);
                }
                //                progressView.setVisibility(View.GONE);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void getHospitals() {
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<>();
        hospitalDetailsArrayList.add(new HospitalDetails("-1", "Select Hospital"));
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getHospitalUrl, response -> {
            Log.i("response is ", "onResponse: " + response);
            progressView.setVisibility(View.GONE);
            try {
                JSONArray hospitalList = new JSONArray(response);
                for (int i = 0; i < hospitalList.length(); i++) {
                    JSONObject c = hospitalList.getJSONObject(i);

                    String hospCode = c.getString("hospCode");
                    String hospName = c.getString("hospName");

                    hospitalDetailsArrayList.add(new HospitalDetails(hospCode, hospName));

                }
                hospSpinner.setAdapter(new ArrayAdapter(OPDAppointmentActivity.this, R.layout.for_layout, hospitalDetailsArrayList));
                hospSpinner.setSelection(getIndex(hospSpinner, AppConstants.BOKARO_GENERAL_HOSPITAL));

            } catch (final JSONException e) {
                Log.i("jsonexception", "onResponse: " + e);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            progressView.setVisibility(View.GONE);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(OPDAppointmentActivity.this);
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
