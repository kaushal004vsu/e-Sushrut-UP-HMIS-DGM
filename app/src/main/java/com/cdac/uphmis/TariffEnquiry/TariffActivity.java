package com.cdac.uphmis.TariffEnquiry;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class TariffActivity extends AppCompatActivity {
    ListView tariffListView;
    EditText etSearch;
    private TariffAdapter adapter1;
    GeometricProgressView progressView;
    private ImageView speechSearch;

    private Spinner hospSpinner;

//    private String hospCode = "";
//    String hospName = "";
    LinearLayout tariff_search_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariff);

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
//            hospCode = intent.getStringExtra("hospCode");
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }

        tariffListView = findViewById(R.id.tariff_list);
        etSearch = findViewById(R.id.etSearch);
        progressView = findViewById(R.id.progress_view);
        hospSpinner = findViewById(R.id.hosp_spinner);
        tariff_search_ll = findViewById(R.id.tariff_search_ll);
        getHospitals();
//        getOpdTariffList();



        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HospitalDetails hospitalDetails=(HospitalDetails) hospSpinner.getSelectedItem();
                String hospCode=hospitalDetails.getHospCode();


                getOpdTariffList(hospCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        speechSearch = findViewById(R.id.speech_search);
        speechSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if (TariffActivity.this != null) {
                    adapter1.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                //if (!s.toString().startsWith("Dr. ")) {

                Selection.setSelection(etSearch.getText(), etSearch.getText().length());

                //}


            }
        });


    }

    public void getOpdTariffList(String hospCode) {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.tariffurl + hospCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<TariffDetails> tariffList = new ArrayList<TariffDetails>();
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);
                JSONArray jsonObj;
                try {
                    jsonObj = new JSONArray(response);


                    for (int i = 0; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        if (c.getString("chargeType").contains("OPD") && c.getString("patCategory").equalsIgnoreCase("General")) {
                            String patCategory = c.getString("patCategory");
                            //String chargeType = c.getString("chargeType");
                            String tariffName = c.getString("tariffName");
                            String tariffCharge = c.getString("tariffCharge");
                            String tariffServiceID = c.getString("tariffServiceID");

                            tariffList.add(new TariffDetails(patCategory, tariffName, tariffCharge));
                        }
                        //hospitalList.add(name);
                    }
                    if (TariffActivity.this != null) {
                        if (tariffList.size() > 0) {
                            tariff_search_ll.setVisibility(View.VISIBLE);
                            adapter1 = new TariffAdapter(TariffActivity.this, tariffList);
                            tariffListView.setAdapter(adapter1);
                        } else {
                            tariff_search_ll.setVisibility(View.GONE);
                            Toast.makeText(TariffActivity.this, "" + getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressView.setVisibility(View.GONE);
                //Snackbar.make(this, "Unable to connect server please try again later.", Snackbar.LENGTH_SHORT).show();
                Log.i("error", "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(TariffActivity.this.getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(TariffActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etSearch.setText(result.get(0));

                }

                break;
        }
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

    private void getHospitals() {

        Log.d("department", ServiceUrl.getHospitalUrl);
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
                hospSpinner.setAdapter(new ArrayAdapter(TariffActivity.this, R.layout.for_layout, hospitalDetailsArrayList));


                //  getItemBrand();

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

}

