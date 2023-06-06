package com.cdac.uphmis.appointment;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.AppUtilityFunctions;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NDHMLoginActivity extends AppCompatActivity {
    private EditText edtHealthId, edtOTP;
    private Button btnGetOTP, btnLogin;
    private LinearLayout llEnterHealthId, llEnterOTP;
    private RequestQueue requestQueue;


    private String transactionId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_d_h_m_login);
        
       // requestQueue = Volley.newRequestQueue(this);
        initializeViews();
    }

    private void initializeViews() {
        edtHealthId = findViewById(R.id.edt_health_id);
        edtOTP = findViewById(R.id.edt_otp);
        llEnterHealthId = findViewById(R.id.ll_enter_healthid);
        llEnterOTP = findViewById(R.id.ll_enter_otp);

    }

    public void btnGetOtp(View view) {

        getOtp();
    }

    private void getOtp() {

        final String requestBody = "{patNdhmHealthId:\"raisudeep66@sbx\", ndhmHealthIDCode:\"\", patHospitalHealthId:\"\", \n" +
                " authenticationModeCode:\"\", authenticationMode:\"\",    hospitalCode :\"10911\" }\n";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://aiml01.uat.dcservices.in/hip/v0.5/users/auth/hisinit1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        llEnterOTP.setVisibility(View.VISIBLE);
                        llEnterHealthId.setVisibility(View.GONE);

                        transactionId = jsonObject.getString("transactionId");
                        jsonObject.getString("requestId");
                    } else {
                        Toast.makeText(NDHMLoginActivity.this, "Invalid ABHA Number.", Toast.LENGTH_SHORT).show();
                        llEnterOTP.setVisibility(View.GONE);
                        llEnterHealthId.setVisibility(View.VISIBLE);
                    }


                } catch (Exception ex) {
                    Log.i("exception", "onResponse: " + ex);
                    llEnterOTP.setVisibility(View.GONE);
                    llEnterHealthId.setVisibility(View.VISIBLE);
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());

                llEnterOTP.setVisibility(View.GONE);
                llEnterHealthId.setVisibility(View.VISIBLE);
                AppUtilityFunctions.handleExceptions(error, NDHMLoginActivity.this);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                return params;
//            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void btnLogin(View view) {

        String otp = edtOTP.getText().toString();
        String requestBody = "{patNdhmHealthId:\"raisudeep66@sbx\", ndhmHealthIDCode:\"\", patHospitalHealthId:\"\", otp:\"" + otp + "\", hospitalCode :\"19102\" ,transactionId : \"" + transactionId + "\"}";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://aiml01.uat.dcservices.in/hip/v0.5/users/auth/hisconfirm1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String patNdhmHealthId= jsonObject.getString("patNdhmHealthId");
                        String patName=jsonObject.getString("patName");
                        String patGender=jsonObject.getString("patGender");
                        String patDob=jsonObject.getString("patDob");
                        String patAddress= jsonObject.getString("patAddress");
                        String hospitalcode=jsonObject.getString("hospitalcode");
                        String mobileno= jsonObject.getString("mobileno");
                        String patHospitalHealthId=jsonObject.getString("patHospitalHealthId");
                        String line= jsonObject.getString("line");
                        String district=jsonObject.getString("district");
                        String state= jsonObject.getString("state");
                        String pincode=jsonObject.getString("pincode");
                        String accessToken=jsonObject.getString("accessToken");

                    } else {
                        Toast.makeText(NDHMLoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    Log.i("exception", "onResponse: " + ex);
                    llEnterOTP.setVisibility(View.GONE);
                    llEnterHealthId.setVisibility(View.VISIBLE);
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());

                llEnterOTP.setVisibility(View.GONE);
                llEnterHealthId.setVisibility(View.VISIBLE);
                AppUtilityFunctions.handleExceptions(error, NDHMLoginActivity.this);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                return params;
//            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}