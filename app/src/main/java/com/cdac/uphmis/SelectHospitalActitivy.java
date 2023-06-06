package com.cdac.uphmis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.OPDEnquiry.Enquiry;
import com.cdac.uphmis.TariffEnquiry.TariffActivity;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.covid19.model.DivisionDetails;
import com.cdac.uphmis.covid19.model.ZoneDetails;
import com.cdac.uphmis.drugavailability.DrugAvailabilityActivity;
import com.cdac.uphmis.labEnquiry.LabEnquiryActivity;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.opdLite.DrugsActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cdac.uphmis.util.AppConstants.DRUG_AVAILABILITY;
import static com.cdac.uphmis.util.AppConstants.LAB_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.OPD_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.TARIFF_ENQUIRY;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class SelectHospitalActitivy extends AppCompatActivity {

    private Spinner zoneSpinner, divisionSpinner, hospSpinner;
    private GeometricProgressView progressView;
    private String zoneId = "0";
    private String divisionId = "0";
    private View zoneSpinnerView, divisionSpinnerView, hospSpinnerView;

    private Button btnSubmit;
    String hospCode = "-1";
    String hospName = "";
    String module = "";
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hospital_actitivy);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = findViewById(R.id.tv_title);
        Intent i = getIntent();
        if (getIntent().getExtras() != null) {
            module = i.getStringExtra("module");

        }

        switch (module) {
            case OPD_ENQUIRY:
                tvTitle.setText(R.string.OE_title);
                break;
            case LAB_ENQUIRY:
                tvTitle.setText(R.string.LE_title);
                break;
            case TARIFF_ENQUIRY:
                tvTitle.setText(R.string.TR_title);
                break;
            case DRUG_AVAILABILITY:
                tvTitle.setText(R.string.DA_title);
                break;

            default:
                finish();
        }
        initializeSpinners();

        btnSubmit.setOnClickListener(v -> {
            if (!hospCode.equalsIgnoreCase("-1")) {
                switch (module) {
                    case OPD_ENQUIRY:
                        gotoActivity(Enquiry.class);
                        break;
                    case LAB_ENQUIRY:
                        gotoActivity(LabEnquiryActivity.class);
                        break;
                    case TARIFF_ENQUIRY:
                        gotoActivity(TariffActivity.class);
                        break;
                    case DRUG_AVAILABILITY:
                        gotoActivity(DrugAvailabilityActivity.class);
                        break;
                    default:
                        finish();
                }

            } else {
                Toast.makeText(this, "Please Select Hospital", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void gotoActivity(Class toClass) {

        Intent intent = new Intent(this, toClass);
        intent.putExtra("hospCode", hospCode);
        intent.putExtra("hospName", hospName);
        startActivity(intent);
    }


    private void initializeSpinners() {
        progressView = findViewById(R.id.progress_view);
        zoneSpinner = findViewById(R.id.zone_spinner);
        zoneSpinnerView = findViewById(R.id.zone_spinner_view);

        divisionSpinner = findViewById(R.id.division_spinner);
        divisionSpinnerView = findViewById(R.id.division_spinner_view);

        hospSpinner = findViewById(R.id.hosp_spinner);
        hospSpinnerView = findViewById(R.id.hosp_spinner_view);

        btnSubmit = findViewById(R.id.btn_proceed);

        getZoneList();


        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                ZoneDetails zoneDetails = (ZoneDetails) zoneSpinner.getSelectedItem();
                zoneId = zoneDetails.getZoneId();
                hospCode = "-1";
                hospName = "";
                if (zoneId.equalsIgnoreCase("-1")) {
                    divisionSpinner.setVisibility(View.GONE);
                    divisionSpinnerView.setVisibility(View.GONE);
                    hospSpinner.setVisibility(View.GONE);
                    hospSpinnerView.setVisibility(View.GONE);

                    btnSubmit.setVisibility(View.GONE);
                } else {
                    divisionSpinner.setVisibility(View.VISIBLE);
                    divisionSpinnerView.setVisibility(View.VISIBLE);
                    getDivisionList(zoneDetails.getZoneId());
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                DivisionDetails divisionDetails = (DivisionDetails) divisionSpinner.getSelectedItem();
                hospCode = "-1";
                hospName = "";
                divisionId = divisionDetails.getDivisionId();
                if (divisionId.equalsIgnoreCase("-1")) {
                    hospSpinner.setVisibility(View.GONE);
                    hospSpinnerView.setVisibility(View.GONE);

                    btnSubmit.setVisibility(View.GONE);
                } else {
                    hospSpinner.setVisibility(View.VISIBLE);
                    hospSpinnerView.setVisibility(View.VISIBLE);
                    getHospitals(zoneId, divisionId);
                }
            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();

                String hosCode = hospitalDetails.getHospCode();

                if (!hosCode.equalsIgnoreCase("-1")) {
                    hospCode = hosCode;
                    hospName = hospitalDetails.getHospName();
                    btnSubmit.setVisibility(View.VISIBLE);
                } else {
                    hospName = "";
                    btnSubmit.setVisibility(View.GONE);
                    Toast.makeText(SelectHospitalActitivy.this, "Please select hospital.", Toast.LENGTH_SHORT).show();
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });
    }


    /**
     * get zones
     */
    private void getZoneList() {

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

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1") && jsonArray.length() > 0) {
                        zoneArrayList.add(new ZoneDetails("-1", "Select Zone"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String zoneId = c.getString("ZONE_ID");
                            String zoneName = c.getString("ZONE_NAME");

                            zoneArrayList.add(new ZoneDetails(zoneId, zoneName));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(SelectHospitalActitivy.this, R.layout.new_patient_custom_layout, zoneArrayList);
                        zoneSpinner.setAdapter(adapter);

//    zoneSpinner.setSelection(setDefaultZone());


                        progressView.setVisibility(View.GONE);
                    } else {

                        getHospitals("0", "0");
                    }

                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                    getHospitals("0", "0");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getHospitals("0", "0");
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, SelectHospitalActitivy.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showZoneAndDivisionSpinners() {
        zoneSpinner.setVisibility(View.VISIBLE);
        divisionSpinner.setVisibility(View.VISIBLE);
        zoneSpinnerView.setVisibility(View.VISIBLE);
        divisionSpinnerView.setVisibility(View.VISIBLE);
    }


    private void hideZoneAndDivisionSpinners() {
        zoneSpinner.setVisibility(View.GONE);
        divisionSpinner.setVisibility(View.GONE);
        zoneSpinnerView.setVisibility(View.GONE);
        divisionSpinnerView.setVisibility(View.GONE);
        hospSpinner.setVisibility(View.VISIBLE);
        hospSpinnerView.setVisibility(View.VISIBLE);
    }


    /**
     * get divisions
     */
    private void getDivisionList(String zoneId) {

        final ArrayList<DivisionDetails> divisionArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionListUrl + "?zoneId=" + zoneId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("division_details");

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1") && jsonArray.length() > 0) {
                        divisionArrayList.add(new DivisionDetails("-1", "Select Division"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String divisionId = c.getString("DIVISION_ID");
                            String divisionName = c.getString("DIVISION_NAME");

                            divisionArrayList.add(new DivisionDetails(divisionId, divisionName));
                        }

                    } else {
                        divisionArrayList.add(new DivisionDetails("-1", "No Division Available"));
//                        getHospitals("0","0");
                    }
                    ArrayAdapter adapter = new ArrayAdapter(SelectHospitalActitivy.this, R.layout.new_patient_custom_layout, divisionArrayList);
                    divisionSpinner.setAdapter(adapter);

//                    divisionSpinner.setSelection(setDefaultDivision());
                } catch (final Exception e) {
                    getHospitals("0", "0");
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getHospitals("0", "0");
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, SelectHospitalActitivy.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void getHospitals(String zoneId, String divisionId) {
        if (zoneId.equalsIgnoreCase("0") || divisionId.equalsIgnoreCase("0")) {
            hideZoneAndDivisionSpinners();
        } else {
            showZoneAndDivisionSpinners();
        }
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<HospitalDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionAndZoneWiseHospitalList + "?zoneId=" + zoneId + "&divisionId=" + divisionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray hospitalList = jsonObject.getJSONArray("hospital_details");
                    if (hospitalList.length() == 0) {
                        hospitalDetailsArrayList.add(new HospitalDetails("-1", "No Hospital Available"));
                    } else {
                        for (int i = 0; i < hospitalList.length(); i++) {
                            JSONObject c = hospitalList.getJSONObject(i);

                            String hospCode = c.getString("CODE");
                            String hospName = c.getString("NAME");
                            String opdtimings = c.optString("OPDTIMINGS");

//if (hospitalCode.equalsIgnoreCase(hospCode)) {
                            hospitalDetailsArrayList.add(new HospitalDetails(hospCode, hospName, opdtimings));
//}

                        }
                    }
                    hospSpinner.setAdapter(new ArrayAdapter(SelectHospitalActitivy.this, R.layout.new_patient_custom_layout, hospitalDetailsArrayList));


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
                AppUtilityFunctions.handleExceptions(error, SelectHospitalActitivy.this);
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