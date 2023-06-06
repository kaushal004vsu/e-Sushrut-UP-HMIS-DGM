package com.cdac.uphmis;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.appointment.util.RegistrationValidation;
import com.cdac.uphmis.covid19.model.DistrictDetails;
import com.cdac.uphmis.covid19.model.StateDetails;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.model.PatientDetails;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import timber.log.Timber;

public class RegistrationActivity extends AppCompatActivity {
    private ManagingSharedData msd;
    private EditText edtPatName, eage, edtGuardianName, emobile, eemail, edtAddress;

    private String genderId = "";
    int byDefaultState = 0;

    String stateId = "-1", districtId = "-1";


    GeometricProgressView progressView;



    private Spinner hospSpinner;
    private View hospSpinnerView;

    private String hospCode="-1";

    private Spinner sspinner, districtSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // requestQueue = Volley.newRequestQueue(this);
        msd = new ManagingSharedData(this);

        progressView = findViewById(R.id.progress_view);
        getHospitals("0","0");


        initializeSpinners();
    }


    private void initializeSpinners()
    {
        edtPatName = findViewById(R.id.efname);

        eage = findViewById(R.id.eage);
        edtGuardianName = findViewById(R.id.ername);
        emobile = findViewById(R.id.emobile);
        eemail = findViewById(R.id.eemail);
        edtAddress = findViewById(R.id.edt_address);

        // Spinner sgender = findViewById(R.id.sgender);
        //btnRegister = findViewById(R.id.btn_register);
        emobile.setEnabled(false);
        emobile.setText(msd.getMobileNo());


        hospSpinner = findViewById(R.id.hosp_spinner);
        hospSpinnerView = findViewById(R.id.hosp_spinner_view);

        Spinner sgender = findViewById(R.id.sgender);
        final String[] arGender = {"Male", "Female", "Transgender"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this,
                android.R.layout.simple_spinner_item, arGender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sgender.setAdapter(adapter);
        genderId = "M";
        sgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (arGender[position].equalsIgnoreCase("Male")) {
                    genderId = "M";
                } else if (arGender[position].equalsIgnoreCase("Female")) {
                    genderId = "F";
                } else if (arGender[position].equalsIgnoreCase("Transgender")) {
                    genderId = "T";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
//            getZoneList();

//        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(
//                    AdapterView<?> adapterView, View view,
//                    int i, long l) {
//                ZoneDetails zoneDetails = (ZoneDetails) zoneSpinner.getSelectedItem();
//                zoneId=zoneDetails.getZoneId();
//                hospCode="-1";
//                if (zoneId.equalsIgnoreCase("-1")) {
//                    divisionSpinner.setVisibility(View.GONE);
//                    divisionSpinnerView.setVisibility(View.GONE);
//                    hospSpinner.setVisibility(View.GONE);
//                    hospSpinnerView.setVisibility(View.GONE);
//
//                    //btnRegister.setVisibility(View.GONE);
//                } else {
//                    divisionSpinner.setVisibility(View.VISIBLE);
//                    divisionSpinnerView.setVisibility(View.VISIBLE);
//                    getDivisionList(zoneDetails.getZoneId());
//                }
//
//            }
//
//
//            public void onNothingSelected(
//                    AdapterView<?> adapterView) {
//
//            }
//        });



//        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(
//                    AdapterView<?> adapterView, View view,
//                    int i, long l) {
//                DivisionDetails divisionDetails = (DivisionDetails) divisionSpinner.getSelectedItem();
//
//                divisionId=divisionDetails.getDivisionId();
//                hospCode="-1";
//                if (divisionId.equalsIgnoreCase("-1")) {
//                    hospSpinner.setVisibility(View.GONE);
//                    hospSpinnerView.setVisibility(View.GONE);
//
//                   //btnRegister.setVisibility(View.GONE);
//                }else {
//                    hospSpinner.setVisibility(View.VISIBLE);
//                    hospSpinnerView.setVisibility(View.VISIBLE);
//                    getHospitals(zoneId, divisionId);
//                }
//            }
//
//
//            public void onNothingSelected(
//                    AdapterView<?> adapterView) {
//
//            }
//        });









        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();

                hospCode = hospitalDetails.getHospCode();

                if (!hospCode.equalsIgnoreCase("-1")) {
                    msd.setHospCode(hospCode);
                    //btnRegister.setVisibility(View.VISIBLE);
                } else {

                    //btnRegister.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, "Please select hospital.", Toast.LENGTH_SHORT).show();
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });




        sspinner = findViewById(R.id.sstate);
        districtSpinner = findViewById(R.id.sdistrict);
        getStates();

        sspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                if (byDefaultState == 0) {
//                    TODO statecode
                    sspinner.setSelection(35);

                    byDefaultState++;

                }
                StateDetails stateDetails = (StateDetails) sspinner.getSelectedItem();
                getDistricts(stateDetails.getId());
                stateId = stateDetails.getId();
            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                DistrictDetails districtDetails = (DistrictDetails) districtSpinner.getSelectedItem();
                districtId = districtDetails.getId();
            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


    }
  /*  private void getZoneList() {

        final ArrayList<ZoneDetails> zoneArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.zoneListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("zoneresponse is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("zone_details");

                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")&&jsonArray.length()>0) {
                        zoneArrayList.add(new ZoneDetails("-1","Select Zone"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String zoneId = c.getString("ZONE_ID");
                            String zoneName = c.getString("ZONE_NAME");

                            zoneArrayList.add(new ZoneDetails(zoneId, zoneName));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(RegistrationActivity.this, R.layout.new_patient_custom_layout, zoneArrayList);
                        zoneSpinner.setAdapter(adapter);

                      //  zoneSpinner.setSelection(setDefaultZone());
                        progressView.setVisibility(View.GONE);
                    }else {

                        getHospitals("0","0");
                    }

                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                    getHospitals("0","0");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getHospitals("0","0");
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, RegistrationActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showZoneAndDivisionSpinners()
    {
        zoneSpinner.setVisibility(View.VISIBLE);
        divisionSpinner.setVisibility(View.VISIBLE);
        zoneSpinnerView.setVisibility(View.VISIBLE);
        divisionSpinnerView.setVisibility(View.VISIBLE);
    }



    private void hideZoneAndDivisionSpinners()
    {
        zoneSpinner.setVisibility(View.GONE);
        divisionSpinner.setVisibility(View.GONE);
        zoneSpinnerView.setVisibility(View.GONE);
        divisionSpinnerView.setVisibility(View.GONE);
        hospSpinner.setVisibility(View.VISIBLE);
        hospSpinnerView.setVisibility(View.VISIBLE);
    }


*/


    /**
     * get divisions
     */
  /*  private void getDivisionList(String zoneId) {

        final ArrayList<DivisionDetails> divisionArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionListUrl+"?zoneId="+zoneId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("division_details");

                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")&&jsonArray.length()>0) {
                        divisionArrayList.add(new DivisionDetails("-1", "Select Division"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String divisionId = c.getString("DIVISION_ID");
                            String divisionName = c.getString("DIVISION_NAME");

                            divisionArrayList.add(new DivisionDetails(divisionId, divisionName));
                        }

                    }else {
                        divisionArrayList.add(new DivisionDetails("-1", "No Division Available"));
//                        getHospitals("0","0");
                    }
                    ArrayAdapter adapter = new ArrayAdapter(RegistrationActivity.this, R.layout.new_patient_custom_layout, divisionArrayList);
                 //   divisionSpinner.setAdapter(adapter);

                  //  divisionSpinner.setSelection(setDefaultDivision());
                } catch (final Exception e) {

                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getHospitals("0","0");
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, RegistrationActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }*/




    private void getHospitals(String zoneId,String divisionId) {
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<HospitalDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionAndZoneWiseHospitalList+"?zoneId="+zoneId+"&divisionId="+divisionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray hospitalList =jsonObject.getJSONArray("hospital_details");
                    if (hospitalList.length()==0)
                    {
                        hospitalDetailsArrayList.add(new HospitalDetails("-1", "No Hospital Available"));
                    }else {
                        for (int i = 0; i < hospitalList.length(); i++) {
                            JSONObject c = hospitalList.getJSONObject(i);

                            String hospCode = c.getString("CODE");
                            String hospName = c.getString("NAME");
                            String opdtimings = c.optString("OPDTIMINGS");

//if (hospitalCode.equalsIgnoreCase(hospCode)) {
                            hospitalDetailsArrayList.add(new HospitalDetails(hospCode, hospName,opdtimings));
//}

                        }
                    }
                    hospSpinner.setAdapter(new ArrayAdapter(RegistrationActivity.this, R.layout.new_patient_custom_layout, hospitalDetailsArrayList));


                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, RegistrationActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }




    private void getStates() {
        final ArrayList<StateDetails> statesArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.urlstates, response -> {
//            Log.i("response is ", "onResponse: " + response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    String id = c.getString("ID");
                    String name = c.getString("NAME");

                    statesArrayList.add(new StateDetails(id, name));
                }

                sspinner.setAdapter(new ArrayAdapter(RegistrationActivity.this, R.layout.new_patient_custom_layout, statesArrayList));


            } catch (final JSONException e) {
                Timber.i("onResponse: %s", e.getMessage());
            }


        }, error -> Timber.i("onErrorResponse: " + error));

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void getDistricts(String stateId) {
        final ArrayList<DistrictDetails> districtDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.urldistricts + stateId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String id = c.getString("ID");
                        String name = c.getString("NAME");

                        districtDetailsArrayList.add(new DistrictDetails(id, name));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(RegistrationActivity.this, R.layout.new_patient_custom_layout, districtDetailsArrayList);
                    districtSpinner.setAdapter(adapter);

//TODO district name
                    //districtSpinner.setSelection(setDefaultDistrict(districtSpinner, "Bathinda"));
                } catch (final Exception e) {
                    Timber.i("onResponse: " + e);
                }


            }
        }, error -> Timber.i("onErrorResponse: " + error));

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


  /*  private void save(String patGender, String patName, String patCrNo, String email, String patMobileNo, String address, String patAge, String country_code, String district_code, String state_code, String pat_guardian, String isPaymentDone, String isGatewayAvailable) {
        //btnRegister.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.registerurl, response -> {
            Log.i("response", "save: "+response);
            //btnRegister.setEnabled(true);
            Timber.i("onResponse: %s", response);
            progressView.setVisibility(View.GONE);
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getString("status").equals("1")) {
                        String patName1 = jsonObj.getString("patName");
                        String crno = jsonObj.getString("CrNo");
                        String gender = jsonObj.getString("patGender");
                        String age = jsonObj.getString("patAge");
                        String hospCode = jsonObj.getString("hospitalCode");
                        getCrList(msd.getPatientDetails().getMobileNo(), patName1, crno, gender, age,msd.getPatientDetails().getUmidData().getUmidNo(),hospCode);


                    } else {
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.unable_to_register_patient), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    progressView.setVisibility(View.GONE);
                    e.printStackTrace();
                    //btnRegister.setEnabled(true);
                }
            }
        }, error -> {
            //btnRegister.setEnabled(true);
            Timber.i("onErrorResponse: %s", error.getMessage());
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, RegistrationActivity.this);

        }) {
            @Override
            protected Map<String, String> getParams() {
                //Map<String, String>
                HashMap<String, String> data = new HashMap<>();

                data.put("patGender", patGender);
                data.put("patName", patName);
                data.put("patCrNo", patCrNo);
                data.put("email", email);
                data.put("patMobileNo", patMobileNo);
                data.put("address", address);
                data.put("hospCode", hospCode);
                data.put("patAge", msd.getPatientDetails().getUmidData().getAge());
                data.put("country_code", country_code);
                data.put("district_code", district_code);
                data.put("state_code", state_code);
                data.put("pat_guardian", pat_guardian);
                data.put("isPaymentDone", isPaymentDone);
                data.put("isGatewayAvailable", isGatewayAvailable);

                data.put("patPinCode",msd.getPatientDetails().getUmidData().getPincode());
                data.put("patUMID",msd.getPatientDetails().getUmidData().getUmidNo());
                data.put("pat_dob",msd.getPatientDetails().getUmidData().getDob());

                data.put("patResidentialAddress",msd.getPatientDetails().getUmidData().getResidentialAddress());
                data.put("patCategoryCode",String.valueOf(msd.getPatientDetails().getUmidData().getPatientCategory()));
                data.put("patBeneficiaryType",msd.getPatientDetails().getUmidData().getBeneficiaryType());
                data.put("patAadhaarNo",msd.getPatientDetails().getUmidData().getAadhaarNo());
                data.put("patBloodGroup",msd.getPatientDetails().getUmidData().getBloodGroup());
                data.put("patCity",msd.getPatientDetails().getUmidData().getCity());

                Gson gson=new Gson();

                data.put("umidData",  gson.toJson(msd.getPatientDetails().getUmidData()));

                Log.i("data", "getParams: "+data);
                return data;

            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0
                ,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
*/




    private void save(String patGender, String patName, String patCrNo, String email, String patMobileNo, String address, String patAge, String country_code, String district_code, String state_code, String pat_guardian, String isPaymentDone, String isGatewayAvailable) {
        //  btnRegister.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.registerurl, response -> {
            //btnRegister.setEnabled(true);
            Log.i("TAG", "save: "+response);
            progressView.setVisibility(View.GONE);
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getString("status").equals("1")) {
                        String patName1 =jsonObj.getString("patName");
                        String crno=jsonObj.getString("CrNo");
                        String gender=jsonObj.getString("patGender");
                        String age=jsonObj.getString("patAge");
//                        getCrList(msd.getMobileNo(), patName1,crno,gender,age);
                        getCrList(msd.getMobileNo(), patName1, crno, gender, age,hospCode);


                    } else {
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.unable_to_register_patient), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    progressView.setVisibility(View.GONE);
                    e.printStackTrace();
                    // btnRegister.setEnabled(true);
                }
            }
        }, error -> {
            // btnRegister.setEnabled(true);
            Log.i("TAG", "save: "+error);
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, RegistrationActivity.this);

        }) {
            @Override
            protected Map<String, String> getParams()  {
                //Map<String, String>
                HashMap<String,String> data = new HashMap<>();

                data.put("patGender", patGender);
                data.put("patName", patName);
                data.put("patCrNo", patCrNo);
                data.put("email", email);
                data.put("patMobileNo", patMobileNo);
                data.put("address", address);
                data.put("hospCode", hospCode);
                data.put("patAge", patAge + " Yr");
                data.put("country_code", country_code);
                data.put("district_code", district_code);
                data.put("state_code", state_code);
                data.put("pat_guardian", pat_guardian);
                data.put("isPaymentDone", isPaymentDone);
                data.put("isGatewayAvailable", isGatewayAvailable);





                return data;

            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0
                ,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private int setDefaultDistrict(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    public void registerUser(View view) {
        if (hospCode.equalsIgnoreCase("-1"))
        {
            Toast.makeText(this, "Please select hospital", Toast.LENGTH_SHORT).show();
            return;
        }

        String patname = edtPatName.getText().toString();

        String age = eage.getText().toString();
        //String finalAge=age.trim().substring(0,2);

        final String guardianName = edtGuardianName.getText().toString();
        final String stateid = stateId;
        final String districtid = districtId;



        final String mobile = emobile.getText().toString();
        final String email = eemail.getText().toString();
        String address = edtAddress.getText().toString();

        RegistrationValidation validation = new RegistrationValidation(RegistrationActivity.this, genderId, patname, "0", email, mobile, address, age, "IND", districtid, stateid, guardianName, "0", "0");
        int validationcheck = validation.checkValidation();

        if (validationcheck == 1) {

            CheckBox checkBox = new CheckBox(this);
            String consent = "I give consent to register and share data with " + getResources().getString(R.string.hosp_name) + ".";
            checkBox.setText(consent);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(checkBox);


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(linearLayout);
            alertDialogBuilder.setTitle("Consent");
            alertDialogBuilder.setMessage("Do you want to proceed?");
            String finalPatname = AppUtilityFunctions.capitalizeString(patname);
            alertDialogBuilder.setPositiveButton(getString(R.string.proceed), (dialog, which) -> save(genderId, finalPatname, "0", email, mobile, address, age, "IND", districtid, stateid, guardianName, "0", "0"));

            alertDialogBuilder.setNegativeButton(getString(R.string.cancel), null);

            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (checkBox.isChecked()) {
                    // Initially disable the button
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                } else {
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            });



        }
    }


    /* private void getCrList(String mobile, String patName, String patCrno, String patGender, String patAge,String umid,String hospCode) {

         ArrayList<PatientDetails> PatientDetailsArrayList = new ArrayList<>();
         StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getRegisteredPatientPreviousRegistrations + mobile + "&smsFlag=0&umid="+umid, response -> {
             Timber.i("onResponse: %s", response);
             try {
                 ManagingSharedData msd = new ManagingSharedData(RegistrationActivity.this);
                 response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                 JSONObject jsonObject = new JSONObject(response);
                // msd.setMobileNo(mobile);
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 if (jsonArray.length() == 0) {
                     Toast.makeText(RegistrationActivity.this, "No other record found", Toast.LENGTH_SHORT).show();
 //                    Intent intent = new Intent(RegistrationActivity.this, PatientDrawerHomeActivity.class);
 //                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
 //                    startActivity(intent);
                     finish();
                 } else {
                     Gson gson = new Gson();
                     for (int i = 0; i < jsonArray.length(); i++) {

                         *//*JSONObject c = jsonArray.getJSONObject(i);
                        if (c.has("crno")) {
                            crno = c.getString("crno");
                        }
                        if (c.has("mobileNo")) {
                            mobileNo = c.getString("mobileNo");
                        }
                        if (c.has("firstName")) {
                            firstName = c.getString("firstName");
                        }
                        if (c.has("lastName")) {
                            lastName = c.getString("lastName");
                        }
                        if (c.has("age")) {
                            age = c.getString("age");
                        }
                        if (c.has("gender")) {
                            gender = c.getString("gender");
                        }
                        if (c.has("fatherName")) {
                            fatherName = c.getString("fatherName");
                        }

                        if (c.has("motherName")) {
                            motherName = c.getString("motherName");

                        }
                        if (c.has("spouseName")) {
                            spouseName = c.getString("spouseName");

                        }
                        if (c.has("stateId")) {
                            stateId = c.getString("stateId");
                        }
                        if (c.has("districtId")) {
                            districtId = c.getString("districtId");
                        }
                        if (c.has("email")) {
                            email = c.getString("email");
                        }*//*
                       // String hospCode=c.optString("hospCode");
                      //  String ndhmId=c.optString("ndhmHealthId");
                       // String ndhmPatHealthId=c.optString("ndhmPatHealthId");
                       // String name = AppUtilityFunctions.capitalizeString(firstName + " " + lastName);
                     //   PatientDetailsArrayList.add(new PatientDetails(crno, mobileNo, name, age, gender, fatherName, motherName, spouseName, stateId, districtId, email,hospCode,ndhmId,ndhmPatHealthId));
                        PatientDetails patientDetails=gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), PatientDetails.class);
                    }


                    msd.setPatientDetails(PatientDetailsArrayList.get(0));
                    //msd.setCrNo(PatientDetailsArrayList.get(0).getCrno());
                    msd.setWhichModuleToLogin("patientlogin");


                    Intent intent = new Intent(RegistrationActivity.this, RegistrationSummaryActivity.class);
                    intent.putExtra("name", patName);
                    intent.putExtra("crno", patCrno);
                    intent.putExtra("gender", patGender);
                    intent.putExtra("age", patAge);
                    intent.putExtra("hospCode", hospCode);
                    intent.putExtra("ndhmId", PatientDetailsArrayList.get(0).getHealthId());

                    startActivity(intent);
                    finish();
                }
            } catch (Exception ex) {
                Timber.i("onResponse: %s", ex);
            }
        }, error -> {
            Timber.i("onResponse: %s", error);
            AppUtilityFunctions.handleExceptions(error, RegistrationActivity.this);
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }
*/
    private void getCrList(String mobile, String patName, String patCrno, String patGender, String patAge,String hospCode) {
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<PatientDetails> patientDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + mobile, response -> {
            Log.i("response", "onResponse: " + response);
            progressView.setVisibility(View.GONE);
            try {
                response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Gson gson = new Gson();

                if (jsonArray.length()==0)
                {
                    Toast.makeText(this, "No more patients found.", Toast.LENGTH_SHORT).show();
                }else if (jsonArray.length() >= 1) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String umidData = jsonArray.getJSONObject(i).optString("UMID_DATA");
                        if (umidData.isEmpty()) {
                            jsonArray.getJSONObject(i).remove("UMID_DATA");
                        } else {
                            JSONObject jsonObject1 = new JSONObject(umidData);
                            jsonArray.getJSONObject(i).put("UMID_DATA", jsonObject1);
                        }
                        PatientDetails patientDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), PatientDetails.class);
                        patientDetailsList.add(patientDetails);
                    }
                    List<PatientDetails> arrayList = patientDetailsList.stream().filter(e->e.getCrno().equalsIgnoreCase(patCrno)).collect(Collectors.toList());
                    msd.setPatientDetails(arrayList.get(0));
                    msd.setWhichModuleToLogin("patientlogin");


                    Intent intent = new Intent(RegistrationActivity.this, RegistrationSummaryActivity.class);
                    intent.putExtra("name", patName);
                    intent.putExtra("crno", patCrno);
                    intent.putExtra("gender", patGender);
                    intent.putExtra("age", patAge);
                    intent.putExtra("hospCode", hospCode);
                    intent.putExtra("ndhmId", arrayList.get(0).getHealthId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "No more patients found.", Toast.LENGTH_SHORT).show();
                }

            } catch (final Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                progressView.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
                , error -> {
            Log.i("error", "onErrorResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, this);
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
        MenuItem item=menu.findItem(R.id.action_network_stats);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(RegistrationActivity.this);
                return true;
            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }




}