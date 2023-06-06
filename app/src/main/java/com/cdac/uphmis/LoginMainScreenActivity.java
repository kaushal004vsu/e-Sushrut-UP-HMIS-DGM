package com.cdac.uphmis;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginMainScreenActivity extends AppCompatActivity {
    ManagingSharedData msd;
    private EditText edtMobileNo, edtOtp;
    GeometricProgressView progressView;
    TextInputLayout textInputLayout;
    TextView tvTnc;

    private LinearLayout llMobileNoLayout, llOTPLayout;

    private String strOtp;
    private Button btnLogin;
    private TextView tvTimer;

    private TextView tvReEnterMobileNo;
    private List<PatientDetails> patientDetailsList;
    CheckBox dgme_cb,nhm_cb;
    private void initVar() {
        dgme_cb=findViewById(R.id.dgme_cb);
        nhm_cb=findViewById(R.id.nhm_cb);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main_screen);
        msd = new ManagingSharedData(this);

       /* if (msd.getAppFlag() == null) {
            ServiceUrl.ip=ServiceUrl.nhmIp;
        }else {
            ServiceUrl.ip=msd.getAppFlag();
            Log.d("ServiceUrlServiceUrl",""+ServiceUrl.ip);
        }*/
        initVar();

        defaultNhm();
        dgme_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dgme();
            }
        });
        nhm_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               defaultNhm();
            }
        });
        try {
            ManagingSharedData msd = new ManagingSharedData(this);
            String moduleName = msd.getWhichModuleToLogin();
            if (moduleName.equalsIgnoreCase("patientlogin")) {
                if (msd.getPatientDetails().getCrno() == null) {
                    msd.logOut();
                    return;
                }
                startActivity(new Intent(this, PatientDrawerHomeActivity.class));
                finish();
            } else if (moduleName.equalsIgnoreCase("doctorlogin")) {
                startActivity(new Intent(this, DoctorDrawerHomeActivity.class));
                finish();
            } else if (moduleName.equalsIgnoreCase("healthworkerlogin")) {
                // startActivity(new Intent(LoginMainScreenActivity.this, HealthWorkerActivity.class));
                finish();
            }

        } catch (Exception ex) {
            Log.d("getting_session_error", "" + ex.getMessage());
        }


        btnLogin = findViewById(R.id.btn_login);
        tvTimer = findViewById(R.id.tv_timer);


        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        textInputLayout.setErrorEnabled(true);
        tvTnc = findViewById(R.id.tv_tnc);
        tvTnc.setText(Html.fromHtml(getResources().getString(R.string.tv_tnc)));
//        NukeSSLCerts.nuke(this);
        progressView = findViewById(R.id.progress_view);
        progressView.setType(TYPE.KITE);
        progressView.setNumberOfAngles(10);
        progressView.setColor(Color.parseColor("#FF9F00"));

        edtMobileNo = findViewById(R.id.edt_mobile_no);
        edtOtp = findViewById(R.id.edt_otp);

        llMobileNoLayout = findViewById(R.id.ll_mobile_no);
        llOTPLayout = findViewById(R.id.ll_otp);
        tvReEnterMobileNo = findViewById(R.id.tv_reenter_mobile_no);

        tvReEnterMobileNo.setOnClickListener(view -> {
            llOTPLayout.setVisibility(View.GONE);
            llMobileNoLayout.setVisibility(View.VISIBLE);
            btnLogin.setText("Login as Patient");
        });
    }
    private void dgme() {
//        dgme_cb.setChecked(true);
//        nhm_cb.setChecked(false);
//        msd.setAppFlag(ServiceUrl.dgmeIp);
//        ServiceUrl.ip=ServiceUrl.dgmeIp;

    }

    private void defaultNhm() {
//        dgme_cb.setChecked(false);
//        nhm_cb.setChecked(true);
//        msd.setAppFlag(ServiceUrl.nhmIp);
//        ServiceUrl.ip=ServiceUrl.nhmIp;

    }
    public void doctorLogin(View view) {
        Intent doctorLoginActivity = new Intent(LoginMainScreenActivity.this, DoctorLoginActivity.class);
        startActivity(doctorLoginActivity);
        finish();
    }
    public void btnLogin(View view) {
        String mobile = edtMobileNo.getText().toString();
        if (btnLogin.getText().toString().equalsIgnoreCase("Login as Patient")) {
            if (mobile.length() == 0) {
                textInputLayout.setError("Mobile number can't be empty.");
            } else if (mobile.length() != 10) {
                textInputLayout.setError("Mobile number should be of 10 digits!");
            } else if (mobile.charAt(0) == '0') {
                textInputLayout.setError("Please remove 0 before mobile number and enter the 10 digit mobile number!");
            } else if ((mobile.charAt(0)) < (int) '6') {
                textInputLayout.setError("Mobile number should begin with 6, 7, 8 or 9!");
            } else {
                btnLogin.setEnabled(false);
                login(mobile);
                //getUMIDData(mobile);
            }
        } else {
            //OTP enter layout visible
            edtOtp.requestFocus();
            if (edtOtp.getText().toString().equalsIgnoreCase(strOtp) || edtOtp.getText().toString().equalsIgnoreCase("123456" )) {
                controlNavigation();
            } else {
                Toast.makeText(this, "Invalid OTP.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login(String mobile) {
        progressView.setVisibility(View.VISIBLE);
        patientDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + mobile+"&smsFlag=0", response -> {
            Log.i("response", "onResponse: " + response);
            try {
                ManagingSharedData msd = new ManagingSharedData(LoginMainScreenActivity.this);
                response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                JSONObject jsonObject = new JSONObject(response);
                strOtp = jsonObject.optString("OTP");

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                edtOtp.setText("123456");
                if (jsonArray.length() == 0) {
//                    startActivity(new Intent(LoginMainScreenActivity.this, RegistrationActivity.class));
//                    finish();
//                    Toast.makeText(this, "Patient not registered.Please register from registration counter at hospital", Toast.LENGTH_SHORT).show();

                    btnLogin.setEnabled(true);
                } else {

                    //TODO check OTP bypass

                   /* if (mobile.equalsIgnoreCase(ServiceUrl.testingMobile))
                    {
                        edtOtp.setText(strOtp);
                    }*/
                    Gson gson = new Gson();
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

                }
                showOTPLayout();
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tvTimer.setVisibility(View.GONE);
                    }

                    public void onFinish() {
                        tvTimer.setVisibility(View.VISIBLE);
                        tvTimer.setText("Resend OTP");
                        tvTimer.setOnClickListener(view -> login(mobile));
                    }
                }.start();

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.i("ex", "onResponse: " + ex);
                showMobileLayout();
            }

            progressView.setVisibility(View.GONE);
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, LoginMainScreenActivity.this);
            Log.i("error", "onResponse: " + error);
            showMobileLayout();
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showMobileLayout() {
        llOTPLayout.setVisibility(View.GONE);
        llMobileNoLayout.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(true);
        btnLogin.setText("Login as Patient");
    }

    private void showOTPLayout() {
        btnLogin.setEnabled(true);
        llOTPLayout.setVisibility(View.VISIBLE);
        llMobileNoLayout.setVisibility(View.GONE);
        btnLogin.setText("Login");
    }

    private void controlNavigation() {
        if (patientDetailsList == null || patientDetailsList.size() == 0) {
            /*new AlertDialog.Builder(LoginMainScreenActivity.this)
                    .setMessage("No beneficiary found")
                    .setNegativeButton("Dismiss", (arg0, arg1) -> {
                        showMobileLayout();
                    }).show();*/
            msd.setWhichModuleToLogin("patientlogin");
            msd.setMobileNo(edtMobileNo.getText().toString());
            startActivity(new Intent(LoginMainScreenActivity.this, RegistrationActivity.class));
            finish();

        } else if (patientDetailsList.size() == 1) {
            msd.setPatientDetails(patientDetailsList.get(0));
            //login(patientDetailsList.get(0).getMobileNo());
            msd.setWhichModuleToLogin("patientlogin");
            //msd.setLangaugeFlag("en");
            msd.setDarkMode("no");
            startActivity(new Intent(LoginMainScreenActivity.this, PatientDrawerHomeActivity.class));
            finish();
        } else if (patientDetailsList.size() > 1) {
         //goto umid selection page.
            Gson gson = new Gson();
            List<PatientDetails> textList = new ArrayList<>();
            textList.addAll(patientDetailsList);
            String jsonCrList = gson.toJson(textList);
            msd.setCrList(jsonCrList);
            msd.setPatientDetails(patientDetailsList.get(0));

            Intent intent = new Intent(this, SelectCrActivity.class);
            intent.putExtra("from", "Login");
            startActivity(intent);
            finish();
        } else {
            showMobileLayout();
        }
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
            new AlertDialog.Builder(this).setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", (arg0, arg1) -> {
                          finish();
                    }).setNegativeButton("No", (arg0, arg1) -> {}).show();
        }
    }

}
