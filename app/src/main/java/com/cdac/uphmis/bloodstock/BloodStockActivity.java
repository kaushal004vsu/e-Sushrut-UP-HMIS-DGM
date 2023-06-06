package com.cdac.uphmis.bloodstock;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.DoctorDeskActivity;
import com.cdac.uphmis.DoctorDrawerHomeActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.bloodstock.adapter.BloodStockAdapter;
import com.cdac.uphmis.bloodstock.model.BloodStockDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.snackbar.Snackbar;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BloodStockActivity extends AppCompatActivity {
    TextView tvHospitalName, tvAddress, tvPhoneNumber, tvEmail, tvLastUpdated;
    ListView lv;
    String email = "", phone = "";
    GeometricProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_stock);
        

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        initializeVies();
        progressView = findViewById(R.id.progress_view);
        getBloodStockAvailability();

    }

    private void initializeVies() {
        tvHospitalName = findViewById(R.id.tv_hospital_name);
        tvAddress = findViewById(R.id.tv_address);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvEmail = findViewById(R.id.tv_email);
        tvLastUpdated = findViewById(R.id.tv_last_updated);
        lv = findViewById(R.id.lv);
    }

    private void getBloodStockAvailability() {


        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.bloodStockurl, (Response.Listener<String>) response -> {
            progressView.setVisibility(View.GONE);
            Log.i("response is ", "onResponse: " + response);
            ArrayList<BloodStockDetails> bloodStockDetailsArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                String hospitalDetails = jsonObject.getString("hospitalDetails");
                Log.i("hospital details", "onCreate: " + hospitalDetails);
                // tvHospitalName.setText(hospitalDetails);


                String temp = hospitalDetails.replaceAll("\\###", ",");
                final String val[] = temp.split(",");
                for (int i = 0; i < val.length; i++) {
                    Log.i("values", "onCreate: " + val[i]);
                    if (val[i].contains("Phone:")) {
                        phone = val[i].replace("Phone:", "");
                        tvPhoneNumber.setText(phone);
                    }
                    if (val[i].contains("Email:")) {
                        email = val[i].replace("Email:", "");
                        tvEmail.setText(email);
                    }

                }
                tvHospitalName.setText(val[0]);
                tvAddress.setText("Phulwarisharif \nNear Bank of India,AIIMS,Patna,Bihar");


                tvLastUpdated.setText("Last Updated : " + val[val.length - 1]);
                tvEmail.setOnClickListener((View.OnClickListener) view -> {
                    String mailTo = email.trim();
                    Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mailTo, null));
                    startActivity(Intent.createChooser(email_intent, "Send email.."));
                });


                tvPhoneNumber.setOnClickListener((View.OnClickListener) view -> {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone.trim(), null));
                    startActivity(intent);
                });

                if (jsonObject.has("Whole Blood")) {
                    String bloodComponent = jsonObject.getString("Whole Blood");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject1 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject1.getString("Oneg"));
                    String abneg="";
                    if (jsonObject1.has("ABneg"))
                    {
                        abneg= jsonObject1.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Whole Blood", jsonObject1.getString("Oneg"), jsonObject1.getString("Aneg"), jsonObject1.getString("Opos"), jsonObject1.getString("Apos"), jsonObject1.getString("ABpos"), jsonObject1.getString("Bpos"), jsonObject1.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Bombay Blood Group")) {
                    String bloodComponent = jsonObject.getString("Bombay Blood Group");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject2 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject2.getString("Oneg"));
                    String abneg="";
                    if (jsonObject2.has("ABneg"))
                    {
                        abneg= jsonObject2.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Bombay Blood Group", jsonObject2.getString("Oneg"), jsonObject2.getString("Aneg"), jsonObject2.getString("Opos"), jsonObject2.getString("Apos"), jsonObject2.getString("ABpos"), jsonObject2.getString("Bpos"), jsonObject2.getString("Bneg"),abneg));
                }


                if (jsonObject.has("Cryo Poor Plasma")) {
                    String bloodComponent = jsonObject.getString("Cryo Poor Plasma");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject3 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject3.getString("Oneg"));
                    String abneg="";
                    if (jsonObject3.has("ABneg"))
                    {
                        abneg= jsonObject3.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Cryo Poor Plasma", jsonObject3.getString("Oneg"), jsonObject3.getString("Aneg"), jsonObject3.getString("Opos"), jsonObject3.getString("Apos"), jsonObject3.getString("ABpos"), jsonObject3.getString("Bpos"), jsonObject3.getString("Bneg"),abneg));
                }


                if (jsonObject.has("Cryoprecipitate")) {

                    String bloodComponent = jsonObject.getString("Cryoprecipitate");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject4 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject4.getString("Oneg"));
                    String abneg="";
                    if (jsonObject4.has("ABneg"))
                    {
                        abneg= jsonObject4.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Cryoprecipitate", jsonObject4.getString("Oneg"), jsonObject4.getString("Aneg"), jsonObject4.getString("Opos"), jsonObject4.getString("Apos"), jsonObject4.getString("ABpos"), jsonObject4.getString("Bpos"), jsonObject4.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Fresh Frozen Plasma")) {
                    String bloodComponent = jsonObject.getString("Fresh Frozen Plasma");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject5 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject5.getString("Oneg"));
                    String abneg="";
                    if (jsonObject5.has("ABneg"))
                    {
                        abneg= jsonObject5.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Fresh Frozen Plasma", jsonObject5.getString("Oneg"), jsonObject5.getString("Aneg"), jsonObject5.getString("Opos"), jsonObject5.getString("Apos"), jsonObject5.getString("ABpos"), jsonObject5.getString("Bpos"), jsonObject5.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Packed Red Blood Cells")) {
                    String bloodComponent = jsonObject.getString("Packed Red Blood Cells");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject6 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject6.getString("Oneg"));
                    String abneg="";
                    if (jsonObject6.has("ABneg"))
                    {
                        abneg= jsonObject6.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Packed Red Blood Cells", jsonObject6.getString("Oneg"), jsonObject6.getString("Aneg"), jsonObject6.getString("Opos"), jsonObject6.getString("Apos"), jsonObject6.getString("ABpos"), jsonObject6.getString("Bpos"), jsonObject6.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Plasma")) {
                    String bloodComponent = jsonObject.getString("Plasma");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject7 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject7.getString("Oneg"));
                    String abneg="";
                    if (jsonObject7.has("ABneg"))
                    {
                        abneg= jsonObject7.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Plasma", jsonObject7.getString("Oneg"), jsonObject7.getString("Aneg"), jsonObject7.getString("Opos"), jsonObject7.getString("Apos"), jsonObject7.getString("ABpos"), jsonObject7.getString("Bpos"), jsonObject7.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Platelet Concentrate")) {
                    String bloodComponent = jsonObject.getString("Platelet Concentrate");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject8 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject8.getString("Oneg"));
                    String abneg="";
                    if (jsonObject8.has("ABneg"))
                    {
                        abneg= jsonObject8.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Platelet Concentrate", jsonObject8.getString("Oneg"), jsonObject8.getString("Aneg"), jsonObject8.getString("Opos"), jsonObject8.getString("Apos"), jsonObject8.getString("ABpos"), jsonObject8.getString("Bpos"), jsonObject8.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Platelet Poor Plasma")) {
                    String bloodComponent = jsonObject.getString("Platelet Poor Plasma");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject9 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject9.getString("Oneg"));
                    String abneg="";
                    if (jsonObject9.has("ABneg"))
                    {
                        abneg= jsonObject9.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Platelet Poor Plasma", jsonObject9.getString("Oneg"), jsonObject9.getString("Aneg"), jsonObject9.getString("Opos"), jsonObject9.getString("Apos"), jsonObject9.getString("ABpos"), jsonObject9.getString("Bpos"), jsonObject9.getString("Bneg"),abneg));
                }


                if (jsonObject.has("Platelet Rich Plasma")) {
                    String bloodComponent = jsonObject.getString("Platelet Rich Plasma");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject10 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject10.getString("Oneg"));
                    String abneg="";
                    if (jsonObject10.has("ABneg"))
                    {
                        abneg= jsonObject10.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Platelet Rich Plasma", jsonObject10.getString("Oneg"), jsonObject10.getString("Aneg"), jsonObject10.getString("Opos"), jsonObject10.getString("Apos"), jsonObject10.getString("ABpos"), jsonObject10.getString("Bpos"), jsonObject10.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Single Donor Plasma")) {
                    String bloodComponent = jsonObject.getString("Single Donor Plasma");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject11 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject11.getString("Oneg"));
                    String abneg="";
                    if (jsonObject11.has("ABneg"))
                    {
                        abneg= jsonObject11.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Single Donor Plasma", jsonObject11.getString("Oneg"), jsonObject11.getString("Aneg"), jsonObject11.getString("Opos"), jsonObject11.getString("Apos"), jsonObject11.getString("ABpos"), jsonObject11.getString("Bpos"), jsonObject11.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Single Donor Platelet")) {
                    String bloodComponent = jsonObject.getString("Single Donor Platelet");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject12 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject12.getString("Oneg"));
                    String abneg="";
                    if (jsonObject12.has("ABneg"))
                    {
                        abneg= jsonObject12.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Single Donor Platelet", jsonObject12.getString("Oneg"), jsonObject12.getString("Aneg"), jsonObject12.getString("Opos"), jsonObject12.getString("Apos"), jsonObject12.getString("ABpos"), jsonObject12.getString("Bpos"), jsonObject12.getString("Bneg"),abneg));
                }

                if (jsonObject.has("Washed Blood Cell")) {
                    String bloodComponent = jsonObject.getString("Washed Blood Cell");
                    Log.i("name", "onCreate: " + bloodComponent);
                    JSONObject jsonObject13 = new JSONObject(bloodComponent);
                    Log.i("onegative", "onCreate:" + jsonObject13.getString("Oneg"));
                    String abneg="";
                    if (jsonObject13.has("ABneg"))
                    {
                        abneg= jsonObject13.getString("ABneg");

                    }
                    bloodStockDetailsArrayList.add(new BloodStockDetails("Washed Blood Cell", jsonObject13.getString("Oneg"), jsonObject13.getString("Aneg"), jsonObject13.getString("Opos"), jsonObject13.getString("Apos"), jsonObject13.getString("ABpos"), jsonObject13.getString("Bpos"), jsonObject13.getString("Bneg"),abneg));
                }

                BloodStockAdapter bloodStockAdapter = new BloodStockAdapter(BloodStockActivity.this, bloodStockDetailsArrayList);
                Log.i("arraylist", "onCreate: " + bloodStockDetailsArrayList);

                lv.setAdapter(bloodStockAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error,BloodStockActivity.this);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
