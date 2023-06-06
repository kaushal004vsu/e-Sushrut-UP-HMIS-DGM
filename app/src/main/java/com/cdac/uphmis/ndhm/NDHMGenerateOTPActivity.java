package com.cdac.uphmis.ndhm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.cdac.uphmis.util.ServiceUrl.ndhmip;

public class NDHMGenerateOTPActivity extends AppCompatActivity {
    private TextView tvHeader, tvResendOtp;
    private EditText edtOtp;

    private ScrollView svConsent;
    private CheckBox checkBox;
    private LinearLayout llOtp;
    private String txnId;
    private String mobileNo = "";
    TextInputEditText edtMobileNo;
    ManagingSharedData msd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_d_h_m_generate_o_t_p);
        

        initializeViews();
         msd = new ManagingSharedData(this);
        Intent intent = getIntent();
       /* if (intent.getExtras() != null) {
            String stateId = intent.getStringExtra("stateId");
            // districtId=intent.getStringExtra("districtId");

            if (!stateId.equalsIgnoreCase("94")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("Info")
                        .setCancelable(false)
                        .setMessage("Your address register with UMID is showing state as \"" + msd.getUMIDData().getStateName() + "\" , Currently NDHM Health ID generation facility is applicable for UTs only")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        });
                dialog.show();
            }
        }*/

    }


    private void initializeViews() {
        tvHeader = findViewById(R.id.tv_header);

        edtOtp = findViewById(R.id.edt_otp);
        edtMobileNo = findViewById(R.id.edt_mobile_no);
        tvResendOtp = findViewById(R.id.tv_resend_otp);
        svConsent = findViewById(R.id.sv_consent);
        checkBox = findViewById(R.id.i_agree_checkbox);
        llOtp = findViewById(R.id.ll_otp);

        ManagingSharedData msd = new ManagingSharedData(this);
        mobileNo = msd.getPatientDetails().getMobileNo();


        edtMobileNo.setText(msd.getPatientDetails().getMobileNo());
        tvResendOtp.setOnClickListener(view -> {
            try {
                generateOTP(mobileNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void btnGetOtp(View view) {
        if (checkBox.isChecked()) {
            svConsent.setVisibility(View.GONE);
            tvHeader.setText("Enter 6 digit code received on your mobile number");
            llOtp.setVisibility(View.VISIBLE);
            //call generate otp service
            try {
                generateOTP(mobileNo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Please check I agree.", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnSubmitOtp(View view) {
        try {
            verifyOTP(edtOtp.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void generateOTP(String mobileNo) throws Exception {
        //txnId="";
        String URL = ndhmip + "hip/v1/registration/mobile/generateOtp";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("patMobileNo", mobileNo);
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.i("response", "onResponse: " + response);

            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = new JSONObject(response);
                txnId = jsonObject1.optString("txnId");

                if (txnId.isEmpty()) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Error");
                    JSONArray jsonArray = jsonObject2.getJSONArray("details");
                    JSONObject c = jsonArray.getJSONObject(0);
                    String message = c.getString("message");
                    AppUtilityFunctions.showErrorDialog(NDHMGenerateOTPActivity.this, message);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(NDHMGenerateOTPActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

            }


        }, error -> {
            Log.e("VOLLEY", error.toString());
            AppUtilityFunctions.handleExceptions(error, NDHMGenerateOTPActivity.this);
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
        MySingleton.getInstance(NDHMGenerateOTPActivity.this).addToRequestQueue(stringRequest);


    }


    private void verifyOTP(String mobileNo) throws JSONException {
        String URL = ServiceUrl.ndhmip + "hip/v1/registration/mobile/verifyOtp";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("otp", mobileNo);
        jsonObject.put("txnId", txnId);
        final String requestBody = jsonObject.toString();
        Log.i("TAG", "verifyOTP: " + jsonObject.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.i("response", "onResponse: " + response);

            JSONObject jsonObject12 = null;
            try {
                jsonObject12 = new JSONObject(response);
                //Integer isSuccess=jsonObject.getInt("isSuccess");
                String token = jsonObject12.optString("token");
                if (!token.isEmpty()) {
                    Intent intent = new Intent(NDHMGenerateOTPActivity.this, CreateNDHMIDActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("txnId", txnId);
//                    intent.putExtra("stateId",stateId);
//                    intent.putExtra("districtId",districtId);
                    startActivity(intent);
                    finish();
                } else {
                    JSONObject jsonObject1 = jsonObject12.getJSONObject("Error");
                    JSONArray jsonArray = jsonObject1.getJSONArray("details");
                    JSONObject c = jsonArray.getJSONObject(0);
                    String message = c.getString("message");
                    AppUtilityFunctions.showErrorDialog(NDHMGenerateOTPActivity.this, message);
                    //AlertDialog alertDialogBuilder=new AlertDialog.Builder(NDHMGenerateOTPActivity.this).setTitle("Error").setMessage(message).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(NDHMGenerateOTPActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

            }


        }, error -> {
            Log.e("VOLLEY", error.toString());
            AppUtilityFunctions.handleExceptions(error, NDHMGenerateOTPActivity.this);
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
        MySingleton.getInstance(NDHMGenerateOTPActivity.this).addToRequestQueue(stringRequest);

    }

    public void btnLinkABha(View view) {
        try {
            abhaNotFoundDialog();
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void abhaNotFoundDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.abha_id_not_found_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        TextView title = dialog.findViewById(R.id.title);
        TextView create_abha = dialog.findViewById(R.id.create_abha);
        TextView reminder_later = dialog.findViewById(R.id.reminder_later);
        TextInputLayout tlUsername = dialog.findViewById(R.id.tl_username);
        TextView btnFindAbha = dialog.findViewById(R.id.btn_find_abha);
        TextInputEditText edtUserName = dialog.findViewById(R.id.edt_username);
        LinearLayout llIhaveAbha = dialog.findViewById(R.id.ll_i_have_abha);
        ProgressBar pbAbha = dialog.findViewById(R.id.pb_abha);


        LinearLayout llNewId = dialog.findViewById(R.id.ll_new_id);
        LinearLayout llEnterId = dialog.findViewById(R.id.ll_enter_id);

        CheckBox cbNewId = dialog.findViewById(R.id.cb_enter_id);
        ImageView imgClose = dialog.findViewById(R.id.img_close);


        imgClose.setOnClickListener(v -> dialog.dismiss());

        cbNewId.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                llEnterId.setVisibility(View.VISIBLE);
                llIhaveAbha.setVisibility(View.VISIBLE);
                llNewId.setVisibility(View.GONE);
                reminder_later.setVisibility(View.GONE);
            }else
            {
                llEnterId.setVisibility(View.GONE);
                llIhaveAbha.setVisibility(View.GONE);
                llNewId.setVisibility(View.VISIBLE);
                reminder_later.setVisibility(View.VISIBLE);
            }
        });

        title.setText(getString(R.string.dear) + msd.getPatientDetails().getFirstname() + "\n(" + getString(R.string.cr_no) + msd.getPatientDetails().getCrno() + "),");


        btnFindAbha.setOnClickListener(v -> {
            if (edtUserName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter a valid ABHA ID", Toast.LENGTH_SHORT).show();
            } else {
                //  msd.setAbhaReminderCount();
                findABHAId(dialog,pbAbha,edtUserName.getText().toString().trim());
            }
        });
        create_abha.setOnClickListener(view -> {
            dialog.cancel();
            // msd.setAbhaReminderCount();
//            Intent intent = new Intent(this, NDHMGenerateOTPActivity.class);
//            startActivity(intent);

        });
        reminder_later.setOnClickListener(view -> {
            dialog.cancel();
            //   msd.setAbhaReminderCount();
        });
        dialog.show();
    }
    private void findABHAId(Dialog dialog,ProgressBar pbAbha,String userName) {
        try {
            String URL = ndhmip + "hip/heathid/verify";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("healthId", userName);
            final String requestBody = jsonObject.toString();
            Log.i("TAG", "checkHealthID: " + jsonObject.toString());

            pbAbha.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.i("response", "onResponse: " + response);

                JSONObject jsonObject1 = null;
                try {

                    jsonObject1 = new JSONObject(response);
                    String healthId = jsonObject1.optString("healthId");
                    String healthIdNumber = jsonObject1.optString("healthIdNumber");

                    if((healthIdNumber!=null && !healthIdNumber.isEmpty() && !healthIdNumber.equalsIgnoreCase("null"))|| (healthId!=null && !healthId.isEmpty() && !healthId.equalsIgnoreCase("null")))
                    {
                        //  if (healthId!=null && !healthId.isEmpty() && !healthId.equalsIgnoreCase("null"))
                        generateOTP(dialog,pbAbha,healthId,healthIdNumber);
                        // else
                        //   generateOTP(dialog,healthIdNumber,healthIdNumber);
                    }else
                    {
                        Toast.makeText(this, "Invalid health ID", Toast.LENGTH_SHORT).show();
                    }

                    //updateHealthId(dialog,healthId,healthIdNumber);
                    pbAbha.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    pbAbha.setVisibility(View.GONE);
                    Toast.makeText(this, "Invalid Health ID", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    pbAbha.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            }, error -> {
                pbAbha.setVisibility(View.GONE);
                Toast.makeText(this, "ABHA ID Not Found", Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", error.toString());
                AppUtilityFunctions.handleExceptions(error, this);
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
                    params.put("HIS-AUTH-KEY", getString(R.string.HIS_AUTH_KEY));


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHealthId(Dialog dialog,ProgressBar progressView,String ndhmId, String ndhmPatHealthId) {
        progressView.setVisibility(View.VISIBLE);
        PatientDetails patientDetails = msd.getPatientDetails();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateNdhmId + "?hospCode=" + patientDetails.getHospitalCode() + "&ndhmId=" + ndhmId + "&ndhmPatHealthId=" + ndhmPatHealthId + "&crno=" + patientDetails.getCrno() + "&mobileNo=" + patientDetails.getMobileNo() + "&umid=" + msd.getPatientDetails().getCrno(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("updateHealthId", "onResponse: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    dialog.dismiss();
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        getCrList(ndhmId,progressView, ndhmPatHealthId);
                    } else {
                        Toast.makeText(NDHMGenerateOTPActivity.this, "Unable to Link ABHA Number.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, this);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void getCrList(String ndhmId,ProgressBar progressView, String ndhmPatHealthId) {
        progressView.setVisibility(View.VISIBLE);
        ManagingSharedData msd = new ManagingSharedData(this);
        String umid = msd.getPatientDetails().getCrno();
        final ArrayList<PatientDetails> registeredPatientDetailsArrayList = new ArrayList<PatientDetails>();
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
                        registeredPatientDetailsArrayList.add(new PatientDetails(crno, mobileNo, name, age, gender, fatherName, motherName, spouseName, stateId, districtId, email, hospCode, ndhmId, ndhmPatHealthId));

                    }


                    if (registeredPatientDetailsArrayList.size() == 1) {
                        msd.setPatientDetails(registeredPatientDetailsArrayList.get(0));
                    }
                    Intent intent = new Intent(NDHMGenerateOTPActivity.this, NDHMIdSummaryActivity.class);
                    intent.putExtra("healthId", ndhmId);
                    intent.putExtra("healthIdNo", ndhmPatHealthId);
                    startActivity(intent);
                    //getActivity().finish();
                    //getActivity().finish();

                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, this);
            //  progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void generateOTP(Dialog dialog,ProgressBar pbAbha,String healthId,String healthIdNumber) throws Exception {

        String URL = ndhmip + "hip/v0.5/users/auth/hisinit1";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("patHospitalHealthId",msd.getPatientDetails().getCrno() );
        jsonObject.put("isKYC","0" );
        jsonObject.put("patNdhmHealthId", healthId);
        jsonObject.put("authenticationMode","MOBILE_OTP" );
        jsonObject.put("hospitalCode", msd.getPatientDetails().getHospitalCode());
        jsonObject.put("isVerify", "2");

        final String requestBody = jsonObject.toString();

        pbAbha.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.i("response", "onResponse: " + response);
            pbAbha.setVisibility(View.GONE);
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String txnId = jsonObject1.optString("transactionId");


                if (txnId.isEmpty()) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Error");
                    JSONArray jsonArray = jsonObject2.getJSONArray("details");
                    JSONObject c = jsonArray.getJSONObject(0);
                    String message = c.getString("message");
                    if (message.isEmpty())
                        message="Invalid Health ID";
                    AppUtilityFunctions.showErrorDialog(this, message);

                }else
                {
                    LinearLayout llEnterOtp = dialog.findViewById(R.id.ll_enter_otp);

                    EditText edtOtp = dialog.findViewById(R.id.edt_otp);
                    Button btnSubmit = dialog.findViewById(R.id.btn_submit);
                    LinearLayout llEnterId = dialog.findViewById(R.id.ll_enter_id);
                    llEnterId.setVisibility(View.GONE);
                    llEnterOtp.setVisibility(View.VISIBLE);

                    btnSubmit.setOnClickListener(v -> {
                        if (edtOtp.getText().toString().length()!=6)
                            Toast.makeText(this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                        else {
                            verifyOTP(dialog, pbAbha,healthId, healthIdNumber, txnId, edtOtp.getText().toString());


                        }
                    });

                }

            } catch (JSONException e) {
                pbAbha.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(this, "Invalid health ID", Toast.LENGTH_SHORT).show();

            }
        }, error -> {
            pbAbha.setVisibility(View.GONE);
            Log.e("VOLLEY", error.toString());
//            AppUtilityFunctions.handleExceptions(error, this);
            Toast.makeText(this, "Invalid health ID", Toast.LENGTH_SHORT).show();
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
                params.put("HIS-AUTH-KEY", getString(R.string.HIS_AUTH_KEY));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void verifyOTP(Dialog dialog,ProgressBar pbAbha,String healthId,String healthIdNo, String txnId,String otp)
    {
        String URL = ndhmip + "hip/v0.5/users/auth/hisconfirm1";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("hospitalcode",msd.getPatientDetails().getHospitalCode() );
            JSONObject identifierObj = new JSONObject();
            identifierObj.put("type", "HEALTH_ID");
            identifierObj.put("value", healthId);
            jsonObject.put("identifier",identifierObj);
            jsonObject.put("patHospitalHealthId",msd.getPatientDetails().getCrno() );
            jsonObject.put("ndhmHealthIDCode", "");
            jsonObject.put("otp", otp);
            jsonObject.put("patNdhmHealthId", healthId);
            jsonObject.put("authenticationMode", "MOBILE_OTP");
            jsonObject.put("transactionId", txnId);
        }catch(JSONException ex)
        {
            ex.printStackTrace();
        }
        Log.i("TAG", "verifyOTP: "+jsonObject.toString());

        final String requestBody = jsonObject.toString();
        pbAbha.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.i("response", "onResponse: " + response);
            pbAbha.setVisibility(View.GONE);
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = new JSONObject(response);
                String status = jsonObject1.optString("status");
                String patNdhmHealthId = jsonObject1.optString("patNdhmHealthId");
                String patName = jsonObject1.optString("patName");
                String patGender = jsonObject1.optString("patGender");
                String patDob = jsonObject1.optString("patDob");
                String patAddress = jsonObject1.optString("patAddress");
                String hospitalcode = jsonObject1.optString("hospitalcode");
                String mobileno = jsonObject1.optString("mobileno");
                String patHospitalHealthId = jsonObject1.optString("patHospitalHealthId");
                String line = jsonObject1.optString("line");
                String district = jsonObject1.optString("district");
                String state = jsonObject1.optString("state");
                String pincode = jsonObject1.optString("pincode");
                String accessToken = jsonObject1.optString("accessToken");


                if (!status.equalsIgnoreCase("1")) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Error");
                    JSONArray jsonArray = jsonObject2.getJSONArray("details");
                    JSONObject c = jsonArray.getJSONObject(0);
                    String message = c.getString("message");
                    if (message.isEmpty())
                        message="Invalid Health ID";
                    AppUtilityFunctions.showErrorDialog(this, message);

                }else
                {
                    updateHealthId( dialog,pbAbha, healthId,  healthIdNo);
                    //getCrList(patNdhmHealthId, healthIdNo);
                }
            } catch (JSONException e) {
                pbAbha.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(this, "Invalid health ID", Toast.LENGTH_SHORT).show();

            }
        }, error -> {
            pbAbha.setVisibility(View.GONE);
            Log.e("VOLLEY", error.toString());
            // AppUtilityFunctions.handleExceptions(error, this);
            Toast.makeText(this, "Invalid health ID", Toast.LENGTH_SHORT).show();
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
                params.put("HIS-AUTH-KEY", getString(R.string.HIS_AUTH_KEY));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}