package com.cdac.uphmis.labEnquiry;

import static com.cdac.uphmis.util.AppUtilityFunctions.getIndex;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.cdac.uphmis.OPDEnquiry.Enquiry;
import com.cdac.uphmis.R;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class LabEnquiryActivity extends AppCompatActivity {
    ListView labsListview;
    GeometricProgressView progressView;
    private EditText etSearch;
    private LabEnquiryAdapter labEnquiryAdapter;
    private Spinner hospSpinner;
    private LinearLayout llNoResultFound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_enquiry);
        llNoResultFound = findViewById(R.id.ll_no_record_found);
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        /*Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
           TextView tvSelectedHospital=findViewById(R.id.tv_selected_hospital);
            hospCode = intent.getStringExtra("hospCode");
            String hospName = intent.getStringExtra("hospName");
            tvSelectedHospital.setText("Selected Hospital: "+hospName);

        }*/
        progressView = findViewById(R.id.progress_view);
        hospSpinner = findViewById(R.id.hosp_spinner);

        // requestQueue = Volley.newRequestQueue(this);
        labsListview = findViewById(R.id.labs_listview);


        getHospitals();
//            getAllLAbs("0");

        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // getHospitals();
                HospitalDetails hospitalDetails=(HospitalDetails) hospSpinner.getSelectedItem();

                Log.d("hosppppital",""+hospitalDetails.getHospCode());

                String hospCode=hospitalDetails.getHospCode();
                String hospName=hospitalDetails.getHospName();
                getAllLAbs(hospCode,"0");
                // getDepartments(hospitalDetailsArrayList.get(i).getHospCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etSearch = findViewById(R.id.etSearch);

        //      etSearch.setText("Dr");
        Selection.setSelection(etSearch.getText(), etSearch.getText().length());
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if (labEnquiryAdapter!=null)
                    labEnquiryAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                //if (!s.toString().contains("Dr")) {
                //  etSearch.setText("Dr");
                Selection.setSelection(etSearch.getText(), etSearch.getText().length());

//                }


            }
        });



    }
    private void getHospitals() {
        Log.d("department",ServiceUrl.getHospitalUrl);
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<>();
//        hospitalDetailsArrayList.add(new HospitalDetails("-1", "Select Hospital"));
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
                hospSpinner.setAdapter(new ArrayAdapter(LabEnquiryActivity.this, R.layout.for_layout, hospitalDetailsArrayList));
              // hospSpinner.setSelection(getIndex(hospSpinner, AppConstants.BOKARO_GENERAL_HOSPITAL));




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
    private void getAllLAbs(String hospCode,String labCode) {
        progressView.setVisibility(View.VISIBLE);

        Log.d("hosp_lab",ServiceUrl.getLabsurl+ hospCode + "&labCode=" + labCode);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getLabsurl+ hospCode + "&labCode=" + labCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<LabEnquiryDetails> labDetailsArrayList = new ArrayList<>();
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);
                JSONArray jsonObj;
                try {
                    jsonObj = new JSONArray(response);

                    for (int i = 1; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        String testId = c.getString("TEST_ID");
                        String testName = c.getString("TEST_NAME");
                        String labCode = c.getString("LAB_CODE");
                        String labName = c.getString("LAB_NAME");
                        String isApptBased = c.getString("IS_APPT_BASED");
                        String testCharge = c.getString("TEST_CHARGE");
                        String isApptBooking =c.getString("IS_APPT_BOOKING");

                        String strLabName = "Lab Name : " + labName;

                        if (c.getString("IS_APPT_BASED").equalsIgnoreCase("1")) {
                            isApptBased = "Appointment Mandatory*";
                        } else {
                            isApptBased = "";
                        }

                        labDetailsArrayList.add(new LabEnquiryDetails(testId, testName, labCode, strLabName, isApptBased,isApptBooking,testCharge));

                    }
                    if (LabEnquiryActivity.this != null) {
                        if (labDetailsArrayList.isEmpty()){
                            llNoResultFound.setVisibility(View.VISIBLE);
                            labsListview.setVisibility(View.GONE);
                        }else {
                            llNoResultFound.setVisibility(View.GONE);
                            labsListview.setVisibility(View.VISIBLE);
                            labEnquiryAdapter = new LabEnquiryAdapter(LabEnquiryActivity.this, labDetailsArrayList);
                            labsListview.setAdapter(labEnquiryAdapter);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error,LabEnquiryActivity.this);
                Log.i("error", "onErrorResponse: " + error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
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

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
