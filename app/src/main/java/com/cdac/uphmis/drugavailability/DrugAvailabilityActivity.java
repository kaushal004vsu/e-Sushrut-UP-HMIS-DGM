package com.cdac.uphmis.drugavailability;

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
import android.widget.AutoCompleteTextView;
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
import com.cdac.uphmis.DoctorDesk.model.DiagnosisDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.drugavailability.adapter.DrugAvailabilityAdapter;
import com.cdac.uphmis.drugavailability.model.DrugAvailabilityDetails;
import com.cdac.uphmis.drugavailability.model.ItemBrandDetails;
import com.cdac.uphmis.labEnquiry.LabEnquiryActivity;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.opdLite.DrugsActivity;
import com.cdac.uphmis.opdLite.model.DrugsDetails;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class DrugAvailabilityActivity extends AppCompatActivity {
    private ListView listView;
    private DrugAvailabilityAdapter adapter;

    private GeometricProgressView progressView;
    private Spinner hospSpinner;

    private String hospCode = "";
    String hospName = "";

    private AutoCompleteTextView autoCompleteTextView;

    private ArrayAdapter<ItemBrandDetails> itemBrandAdapter;

    private LinearLayout llData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_availability);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        toolbar.setTitle("Drug Availability");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        hospSpinner = findViewById(R.id.hosp_spinner);

       /* Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            TextView tvSelectedHospital = findViewById(R.id.tv_selected_hospital);
            hospCode = intent.getStringExtra("hospCode");
            String hospName = intent.getStringExtra("hospName");
            tvSelectedHospital.setText("Selected Hospital: " + hospName);

        }*/

        getHospitals();


        progressView = findViewById(R.id.progress_view);
        llData = findViewById(R.id.ll_data);

        listView = findViewById(R.id.lv);


        autoCompleteTextView = findViewById(R.id.auto_complete_drugs);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            ItemBrandDetails selectedItem = (ItemBrandDetails) parent.getItemAtPosition(position);

            autoCompleteTextView.dismissDropDown();
            Log.d("student", selectedItem.getITEM_BRAND_ID());
            getDrugAvailibility(hospCode, selectedItem.getITEM_BRAND_ID());

        });


    }
    private void getHospitals() {

        Log.d("department",ServiceUrl.getHospitalUrl);
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<>();
//        hospitalDetailsArrayList.add(new HospitalDetails("-1", "Select Hospital"));
        Log.d("server_url",""+ServiceUrl.ip);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getHospitalUrl, response -> {
            Log.i("response is ", "onResponse: " + response);
            progressView.setVisibility(View.GONE);
            try {
                JSONArray hospitalList = new JSONArray(response);
                for (int i = 0; i < hospitalList.length(); i++) {
                    JSONObject c = hospitalList.getJSONObject(i);

                    hospCode = c.getString("hospCode");
                    hospName = c.getString("hospName");


                    hospitalDetailsArrayList.add(new HospitalDetails(hospCode, hospName));

                }
                hospSpinner.setAdapter(new ArrayAdapter(DrugAvailabilityActivity.this, R.layout.for_layout, hospitalDetailsArrayList));
                hospSpinner.setSelection(getIndex(hospSpinner, AppConstants.BOKARO_GENERAL_HOSPITAL));
                hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       hospCode=hospitalDetailsArrayList.get(i).getHospCode();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                getItemBrand();

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


    private void getItemBrand() {
        ArrayList<ItemBrandDetails> itemBrandDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "railtelService/getItemBrand", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressView.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String itemBrandId = c.getString("ITEM_BRAND_ID");
                            String brandName = c.getString("BRAND_NAME");

                            itemBrandDetailsArrayList.add(new ItemBrandDetails(itemBrandId, brandName));
                        }

                        itemBrandAdapter = new ArrayAdapter<ItemBrandDetails>(DrugAvailabilityActivity.this, R.layout.autocomplete_dropdown, R.id.tv_suggestion, itemBrandDetailsArrayList);
                        autoCompleteTextView.setAdapter(itemBrandAdapter);
                        // adapter.notifyDataSetChanged();
                    } else {
                        finish();
                    }
                } catch (Exception ex) {
                    progressView.setVisibility(View.GONE);
                    ex.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    void getDrugAvailibility(String hospCode, String itemBrandId) {
        Log.d("student2", ServiceUrl.getDrugAvailabilityUrl + "?hospCode=" + hospCode + "&itemBrandId=" + itemBrandId);
        final ArrayList<DrugAvailabilityDetails> drugsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getDrugAvailabilityUrl + "?hospCode=" + hospCode + "&itemBrandId=" + itemBrandId, (Response.Listener<String>) response -> {
            progressView.setVisibility(View.GONE);

            try {
                JSONObject jsonObj = new JSONObject(response);
                showData();
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    String storeName = c.getString("STORE_NAME");
                    String item = c.getString("ITEM");
                    String quantity = c.getString("QTY");


                    if (!quantity.equalsIgnoreCase("0"))
                        drugsList.add(new DrugAvailabilityDetails(storeName, item, quantity));
                }
                if (jsonArray.length() == 0) {
                    Toast.makeText(this, "No Records found", Toast.LENGTH_SHORT).show();
                }

                adapter = new DrugAvailabilityAdapter(DrugAvailabilityActivity.this, drugsList);
                listView.setAdapter(adapter);
            } catch (final Exception e) {
                progressView.setVisibility(View.GONE);
                Log.i("error", "onResponse: " + e);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, DrugAvailabilityActivity.this);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void showData() {
        progressView.setVisibility(View.GONE);

        llData.setVisibility(View.VISIBLE);
    }

    private void hideData() {
        progressView.setVisibility(View.VISIBLE);
        llData.setVisibility(View.GONE);
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

    public void btnClear(View view) {
        autoCompleteTextView.getText().clear();
        llData.setVisibility(View.GONE);
    }
}
