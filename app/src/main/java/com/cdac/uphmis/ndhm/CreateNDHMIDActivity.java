package com.cdac.uphmis.ndhm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.QMSSlip.QMSSlipActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.model.UMIDData;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;
import static com.cdac.uphmis.util.ServiceUrl.ndhmip;

public class CreateNDHMIDActivity extends AppCompatActivity {
    private TextInputEditText edtName, edtUserName, edtConfirmPassword, edtStateUt, edtAddress, edtDayOfBirth, edtMonthOfBirth, edtYearOfBirth, edtGender;

    TextInputLayout tlUsername, tlPassword, tlConfirmPassword;
    TextInputEditText edtPassword;
    private UMIDData umidData;

    private ManagingSharedData msd;
    private GeometricProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_n_d_h_m_i_d);
        
        msd = new ManagingSharedData(this);
        try {
            initializeViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initializeViews() throws JSONException {
        progressView = findViewById(R.id.progress_view);
        edtName = findViewById(R.id.edt_name);
        edtUserName = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        edtStateUt = findViewById(R.id.edt_state_ut);
        edtAddress = findViewById(R.id.edt_address);
        edtDayOfBirth = findViewById(R.id.edt_day_of_birth);
        edtMonthOfBirth = findViewById(R.id.edt_month_of_birth);
        edtYearOfBirth = findViewById(R.id.edt_year_of_birth);
        edtGender = findViewById(R.id.edt_gender);


        tlUsername = findViewById(R.id.tl_username);
        tlPassword = findViewById(R.id.tl_password);
        tlConfirmPassword = findViewById(R.id.tl_confirm_password);

        ManagingSharedData msd = new ManagingSharedData(this);
        umidData = msd.getPatientDetails().getUmidData();
        edtName.setText(umidData.getName());
        edtStateUt.setText(umidData.getStateName());
        edtAddress.setText(umidData.getResidentialAddress());
        edtGender.setText(umidData.getGender());

        String dateOfBirth = umidData.getDob();

        String dob[] = dateOfBirth.split("-");
        edtDayOfBirth.setText(dob[0]);
        edtMonthOfBirth.setText(dob[1]);
        edtYearOfBirth.setText(dob[2]);


//        edtUserName.setText(msd.getUMIDData().getUmidNo());


        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = edtUserName.getText().toString().trim();
                try {

                    Pattern p = Pattern.compile("^[A-Za-z\\d.]{4,}$");
                    Matcher m = p.matcher(edtUserName.getText().toString());
                    if (m.matches()) {
                        tlUsername.setErrorEnabled(false);
                        checkHealthID(userName);
                        Log.i("inside", "onTextChanged: ");
                    } else {
                        tlUsername.setErrorEnabled(true);
                        tlUsername.setError("Must contain atleast 4 letters. Please note, we allow alphabets and numbers in the ABHA and do not allow special character except dot (.)");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                edtConfirmPassword.getText().clear();
                //here is your code
                Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
                Matcher m = p.matcher(edtPassword.getText().toString());
                if (m.matches()) {
                    tlPassword.setErrorEnabled(false);
                    Log.i("inside", "onTextChanged: ");
                } else {
                    tlPassword.setErrorEnabled(true);
                    tlPassword.setError("Must contain an uppercase, a lowercase, a number, a special character and at least 8 or more characters");
                }

                if (!(edtPassword.getText().toString().trim().equalsIgnoreCase(edtConfirmPassword.getText().toString()) && !edtConfirmPassword.getText().toString().isEmpty())) {
                    tlConfirmPassword.setError("Confirm password do not match");
                    tlConfirmPassword.setErrorEnabled(true);
                } else {
                    tlConfirmPassword.setErrorEnabled(false);
                }
                Log.i("outside", "onTextChanged: ");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edtPassword.getText().toString().trim().equalsIgnoreCase(edtConfirmPassword.getText().toString())) {
                    tlConfirmPassword.setError("Confirm password do not match");
                    tlConfirmPassword.setErrorEnabled(true);
                } else {
                    tlConfirmPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void checkHealthID(String userName) throws JSONException {

        //     isHealthIdExists = false;

        String URL = ndhmip + "hip/v1/search/existsByHealthId";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("healthId", userName);
        final String requestBody = jsonObject.toString();
        Log.i("TAG", "checkHealthID: " + jsonObject.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    //  isHealthIdExists = status;
                    if (status) {
                        tlUsername.setError("ABHA Address " + userName + " is already taken, try another one");
                        tlUsername.setErrorEnabled(true);
                    } else {
                        tlUsername.setErrorEnabled(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateNDHMIDActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                }


            }


        }, error -> {
            Log.e("VOLLEY", error.toString());
            AppUtilityFunctions.handleExceptions(error, CreateNDHMIDActivity.this);
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


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("HIS-AUTH-KEY", "fbdcf45645fgnG34534FvdfFvdfbNytHERgSdbsdvsdvs3");


                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CreateNDHMIDActivity.this).addToRequestQueue(stringRequest);
    }


    public void btnSubmit(View view) {
        if (!(tlUsername.getError() == null)) {
            showErrorDialog("ABHA (number): " + tlUsername.getError().toString());
            return;
        }
        if (!(tlPassword.getError() == null)) {
            showErrorDialog("Password: " + tlPassword.getError().toString());
            return;
        }
        if (!(tlConfirmPassword.getError() == null)) {
            showErrorDialog("Confirm Password: " + tlConfirmPassword.getError().toString());
            return;
        }
        if (edtUserName.getText().toString().trim().isEmpty()) {
            showErrorDialog("Please enter ABHA Adress");
            return;
        }
        if (edtPassword.getText().toString().trim().isEmpty()) {
            showErrorDialog("Please enter password");
            return;
        }
        if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
            showErrorDialog("Please enter Confirm password");
            return;
        }
        try {
            String dateOfBirth = umidData.getDob();

            String dob[] = dateOfBirth.split("-");

            Intent intent = getIntent();
            String token = "", txnId = "", stateId = "", districtId = "";
            if (intent.getExtras() != null) {
                token = intent.getStringExtra("token");
                txnId = intent.getStringExtra("txnId");
                //stateId = intent.getStringExtra("stateId");
                //districtId = intent.getStringExtra("districtId");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("overridePatDtlExistCheck", "true");
            jsonObject.put("address", umidData.getResidentialAddress());
            jsonObject.put("dayOfBirth", dob[0]);
            jsonObject.put("districtCode", "");
            jsonObject.put("email", umidData.getEmailId());
            jsonObject.put("firstName", umidData.getName());
            jsonObject.put("gender", umidData.getGender());
            jsonObject.put("healthId", edtUserName.getText().toString().trim());
            jsonObject.put("lastName", umidData.getLastName());
            jsonObject.put("middleName", umidData.getMiddleName());
            try {
                if (dob[1].startsWith("0")) {
                    dob[1] = dob[1].replace("0", "");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            jsonObject.put("monthOfBirth", dob[1]);
            jsonObject.put("name", umidData.getName());
            jsonObject.put("password", edtPassword.getText().toString());
            jsonObject.put("pincode", umidData.getPincode());
            jsonObject.put("profilePhoto", "");
            jsonObject.put("restrictions", "");
            jsonObject.put("stateCode", "");
            jsonObject.put("subdistrictCode", "");
            jsonObject.put("townCode", "");
            jsonObject.put("villageCode", "");
            jsonObject.put("wardCode", "");
            jsonObject.put("yearOfBirth", dob[2]);
            jsonObject.put("mobile", umidData.getMobileNo());
            jsonObject.put("token", token);
            jsonObject.put("txnId", txnId);
            callCreateHealthId(jsonObject);

        } catch (Exception ex) {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }

    }

    private void callCreateHealthId(JSONObject jsonObject) {
        progressView.setVisibility(View.VISIBLE);

        String URL = ndhmip + "hip/v1/registration/mobile/createHealthId";


        final String requestBody = jsonObject.toString();
        Log.i("TAG", "verifyOTP: " + jsonObject.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response", "onResponse: " + response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    //Integer isSuccess=jsonObject.getInt("isSuccess");
                    String token = jsonObject.optString("token");
                    if (!token.isEmpty()) {

                        String ndhmHealthId = jsonObject.getString("healthId");
                        String ndhmPatHealthId = jsonObject.getString("healthIdNumber");
                        updateHealthId(ndhmHealthId, ndhmPatHealthId);

                    } else {
                        String error = jsonObject.optString("error");
                        if (jsonObject.has("isSuccess")) {
                            if (jsonObject.getInt("isSuccess") == 0) {
                                if (jsonObject.has("Error")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("Error");
                                    JSONArray jsonArray = jsonObject1.getJSONArray("details");
                                    JSONObject c = jsonArray.getJSONObject(0);
                                    error = c.getString("message");
                                    if (error.contains("8")) {
                                        error = "Password: " + error;
                                    }
                                } else {
                                    if (!jsonObject.optString("error").isEmpty()) {
                                        error = jsonObject.optString("error");
                                    }
                                }
                            }

                        }


                        if (error.isEmpty()) {
                            error = "Something went wrong.could not create ABHA Number.";
                        }

                        showErrorDialog(error);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateNDHMIDActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, CreateNDHMIDActivity.this);
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


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("HIS-AUTH-KEY", "fbdcf45645fgnG34534FvdfFvdfbNytHERgSdbsdvsdvs3");


                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CreateNDHMIDActivity.this).addToRequestQueue(stringRequest);

    }

    private void updateHealthId(String ndhmId, String ndhmPatHealthId) {
        progressView.setVisibility(View.VISIBLE);
        PatientDetails patientDetails = msd.getPatientDetails();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateNdhmId + "?hospCode=" + patientDetails.getHospitalCode() + "&ndhmId=" + ndhmId + "&ndhmPatHealthId=" + ndhmPatHealthId + "&crno=" + patientDetails.getCrno() + "&mobileNo=" + patientDetails.getMobileNo() + "&umid=" + msd.getPatientDetails().getUmidData().getUmidNo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("updateHealthId", "onResponse: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        getCrList(ndhmId, ndhmPatHealthId);
                        String mToken = "";
                        //todo firebase
                        if (msd.getToken() == null) {
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(CreateNDHMIDActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String mToken = instanceIdResult.getToken();
                                    Log.e("Token", "" + mToken);

                                    msd.setToken(mToken);
                                    sendFCMPush("ABHA created", "Your ABHA address " + ndhmId + " and your password is " + edtPassword.getText().toString(), mToken);
                                }
                            });
                        } else {
                            mToken = msd.getToken();
                            sendFCMPush("ABHA created", "Your ABHA Address is " + ndhmId + " and your password is " + edtPassword.getText().toString(), mToken);

                        }

                    } else {
                        Toast.makeText(CreateNDHMIDActivity.this, "Unable to update ABHA Number.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, CreateNDHMIDActivity.this);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(CreateNDHMIDActivity.this).addToRequestQueue(request);
    }

    private void showErrorDialog(String message) {
        AlertDialog materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this).setTitle("Error").setMessage(message).setNegativeButton("Ok", null).show();
    }


    private void getCrList(String ndhmId, String ndhmPatHealthId) {
        progressView.setVisibility(View.VISIBLE);
        ManagingSharedData msd = new ManagingSharedData(this);
        String umid = msd.getPatientDetails().getUmidData().getUmidNo();
        final ArrayList<PatientDetails> PatientDetailsArrayList = new ArrayList<PatientDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getRegisteredPatientPreviousRegistrations + msd.getPatientDetails().getMobileNo() + "&smsFlag=0&umid=" + umid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("patientdetails");

                    PatientDetails setDefaultPatient = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String crno = "", mobileNo = "", firstName = "", lastName = "", age = "", motherName = "", gender = "", fatherName = "", email = "", stateId = "", districtId = "", spouseName = "";
                        JSONObject c = jsonArray.getJSONObject(i);
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
                        }
                        String hospCode = c.optString("hospCode");
                        String ndhmId = c.optString("ndhmHealthId");
                        String ndhmPatHealthId = c.optString("ndhmPatHealthId");
                        String name = AppUtilityFunctions.capitalizeString(firstName + " " + lastName);
//                        PatientDetailsArrayList.add(new PatientDetails(crno, mobileNo, name, age, gender, fatherName, motherName, spouseName, stateId, districtId, email, hospCode, ndhmId, ndhmPatHealthId));
                        if (msd.getPatientDetails() != null) {
                            if (msd.getPatientDetails().toString().equals(PatientDetailsArrayList.get(i).toString())) {
                                setDefaultPatient = PatientDetailsArrayList.get(i);
                            }
                        }
                    }


                    if (PatientDetailsArrayList.size() == 1) {
                        msd.setPatientDetails(PatientDetailsArrayList.get(0));
                    }
                    Intent intent = new Intent(CreateNDHMIDActivity.this, NDHMIdSummaryActivity.class);
                    intent.putExtra("healthId", ndhmId);
                    intent.putExtra("healthIdNo", ndhmPatHealthId);
                    startActivity(intent);
                    finish();

                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, CreateNDHMIDActivity.this);
                //  progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void sendFCMPush(String title, String message, String patientToken) {


        String token = patientToken;
        JSONObject obj = null;


        try {


            obj = new JSONObject();
            JSONObject objData1 = new JSONObject();

            // objData.put("data", msg);
            objData1.put("title", title);
            objData1.put("content", message);
            objData1.put("navigateTo", "");


            obj.put("data", objData1);
            obj.put("to", token);

            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl+"callPushNotfication/call", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
//                params.put("Content-Type", "application/json");
                return params;
            }
        };

        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}



