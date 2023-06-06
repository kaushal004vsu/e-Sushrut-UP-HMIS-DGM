package com.cdac.uphmis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.cdac.uphmis.util.AppUtilityFunctions.SHA1;
import static com.cdac.uphmis.util.AppUtilityFunctions.getIndex;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class DoctorLoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    String sha;
    String pass;
    ManagingSharedData msd;
    GeometricProgressView progressView;


    Spinner hospSpinner;
    String hospitalCode;

    LinearLayout llData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        
        llData = findViewById(R.id.ll_data);

//        //NukeSSLCerts.nuke(this);
       // requestQueue = Volley.newRequestQueue(this);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        progressView = findViewById(R.id.progress_view);
        progressView.setType(TYPE.KITE);
        progressView.setNumberOfAngles(10);
        progressView.setColor(Color.parseColor("#FF9F00"));

        msd = new ManagingSharedData(DoctorLoginActivity.this);

        try {
            ManagingSharedData msd = new ManagingSharedData(this);
            String id = msd.getUsername();
            if (id != null) {
                startActivity(new Intent(DoctorLoginActivity.this, DoctorDrawerHomeActivity.class));
                finish();
            }

        } catch (Exception ex) {
            Log.d("getting_session_error", ex.getMessage());
        }


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
                    llData.setVisibility(View.VISIBLE);
                    msd.setHospCode(hospCode);

                } else {
                    llData.setVisibility(View.GONE);
                    Toast.makeText(DoctorLoginActivity.this, "Please select hospital.", Toast.LENGTH_SHORT).show();
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


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
                hospSpinner.setAdapter(new ArrayAdapter(DoctorLoginActivity.this, R.layout.for_layout, hospitalDetailsArrayList));

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
    public void login(final View view) {

        HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();
        if (hospitalDetails.getHospCode()!=null&&hospitalDetails.getHospCode().equalsIgnoreCase("-1"))
        {
            Toast.makeText(this, "Please select hospital.", Toast.LENGTH_SHORT).show();
        return;
        }





        //NukeSSLCerts.nuke(this);
        progressView.setVisibility(View.VISIBLE);
        String strUsername = edtUsername.getText().toString();
        String strPassword = edtPassword.getText().toString();
        try {
            sha = SHA1(strPassword + strUsername);
            Log.i("sha", "onCreate: " + sha);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.loginSalturl, response -> {
            String salt = response;
            Log.i("salt", "onResponse: " + salt);
            try {
                pass = SHA1(sha + salt);
                Log.i("sha22", "onCreate: " + pass);
                String query = null;
                query = URLEncoder.encode(salt, "utf-8");
                loginCheck(pass, query);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.i("error response salt", "onErrorResponse: " + error);
            Toast.makeText(DoctorLoginActivity.this, "please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            progressView.setVisibility(View.GONE);
        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void loginCheck(String pass, String salt) {
        Log.i("loginurl", "loginCheck: " + ServiceUrl.loginCheckurl + "?user=" + edtUsername.getText().toString() + "&pass=" + pass + "&salt=" + salt + "&hcode=" + hospitalCode + "&role=1");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.loginCheckurl + "?user=" + edtUsername.getText().toString() + "&pass=" + pass + "&salt=" + salt + "&hcode=" + hospitalCode + "&role=1", response -> {
            progressView.setVisibility(View.GONE);
            Log.i("logincheck", "onResponse: " + response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);

                if (jsonObject.getString("valid").equalsIgnoreCase("1")) {

                    msd.setUsername(jsonObject.getString("userdisplayname"));
                    msd.writeSeatNo(jsonObject.getString("userseatid"));
                    msd.setUserId(jsonObject.getString("userId"));
                    msd.setWhichModuleToLogin("doctorlogin");

                   // msd.setMobileNo(jsonObject.getString("mobileNo"));
                    msd.setEmloyeeCode(jsonObject.getString("empcode"));
                    msd.setHospCode(jsonObject.getString("hospCode"));


                    Intent intent = new Intent(DoctorLoginActivity.this, DoctorDrawerHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DoctorLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                progressView.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }, error -> {
            AppUtilityFunctions.handleExceptions(error, DoctorLoginActivity.this);
            progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }





    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {


            startActivity(new Intent(DoctorLoginActivity.this, LoginMainScreenActivity.class));
            finish();
        }

    }


}