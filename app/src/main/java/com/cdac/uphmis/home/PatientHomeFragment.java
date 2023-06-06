package com.cdac.uphmis.home;


import static com.cdac.uphmis.PatientDrawerHomeActivity.OVERLAY_PERMISSION_REQUEST_CODE;
import static com.cdac.uphmis.util.AppConstants.ANNOUNCEMENT;
import static com.cdac.uphmis.util.AppConstants.APPOINTMENT;
import static com.cdac.uphmis.util.AppConstants.BLOOD_STOCK;
import static com.cdac.uphmis.util.AppConstants.CREATE_YOUR_ABHA;
import static com.cdac.uphmis.util.AppConstants.DRUG_AVAILABILITY;
import static com.cdac.uphmis.util.AppConstants.DRUG_AVAILABILTY;
import static com.cdac.uphmis.util.AppConstants.DRUG_RX;
import static com.cdac.uphmis.util.AppConstants.FAMILY_QR;
import static com.cdac.uphmis.util.AppConstants.FEEDBACK;
import static com.cdac.uphmis.util.AppConstants.GENERAL_CATEGORY_CODE;
import static com.cdac.uphmis.util.AppConstants.IPD_DETAILS;
import static com.cdac.uphmis.util.AppConstants.LAB_BASED_APPOINTMENT;
import static com.cdac.uphmis.util.AppConstants.LAB_ENQUAIRY;
import static com.cdac.uphmis.util.AppConstants.LAB_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.LAB_REPORTS;
import static com.cdac.uphmis.util.AppConstants.LP_STATUS;
import static com.cdac.uphmis.util.AppConstants.MY_CRN;
import static com.cdac.uphmis.util.AppConstants.OPD_ENQRY;
import static com.cdac.uphmis.util.AppConstants.OPD_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.PHR;
import static com.cdac.uphmis.util.AppConstants.QUES_NO_SLIP;
import static com.cdac.uphmis.util.AppConstants.REGISTRATION;
import static com.cdac.uphmis.util.AppConstants.REIMBURSEMENT_CLAIM;
import static com.cdac.uphmis.util.AppConstants.REQUEST_STATUS;
import static com.cdac.uphmis.util.AppConstants.RX_SCAN;
import static com.cdac.uphmis.util.AppConstants.RX_VIEW;
import static com.cdac.uphmis.util.AppConstants.SELF_REGISTRATION;
import static com.cdac.uphmis.util.AppConstants.SICK_CERTIFICATE;
import static com.cdac.uphmis.util.AppConstants.TARIFF_DETAILS;
import static com.cdac.uphmis.util.AppConstants.TARIFF_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.TELECONSULTancy_REQUEST;
import static com.cdac.uphmis.util.AppConstants.TOKEN_GENERATION;
import static com.cdac.uphmis.util.AppConstants.TRANSACTION;
import static com.cdac.uphmis.util.AppConstants.UPLOAD_DOCS;
import static com.cdac.uphmis.util.AppConstants.VIEW_PRESCRIPTION;
import static com.cdac.uphmis.util.ServiceUrl.ndhmip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.DocsUpload.DocsViewDocNewActivity;
import com.cdac.uphmis.EstampingActivity;
import com.cdac.uphmis.LoginMainScreenActivity;
import com.cdac.uphmis.OPDEnquiry.Enquiry;
import com.cdac.uphmis.QMSSlip.QMSSlipActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.RegistrationActivity;
import com.cdac.uphmis.StatusActivity;
import com.cdac.uphmis.TariffEnquiry.TariffActivity;
import com.cdac.uphmis.announcement.AnnouncementActivity;
import com.cdac.uphmis.appointment.OPDAppointmentActivity;
import com.cdac.uphmis.bloodstock.BloodStockActivity;
import com.cdac.uphmis.certificates.AdmissionSlipActivity;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.drugavailability.DrugAvailabilityActivity;
import com.cdac.uphmis.drugavailability.DrugRxActivity;
import com.cdac.uphmis.home.slider.SliderImageModel;
import com.cdac.uphmis.home.slider.SlidingImageAdapter;
import com.cdac.uphmis.labBasedAppointment.LabBasedAppointmentActivity;
import com.cdac.uphmis.labEnquiry.LabEnquiryActivity;
import com.cdac.uphmis.lpstatus.LPStatusActivity;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.ndhm.NDHMGenerateOTPActivity;
import com.cdac.uphmis.patientprescriptionscanner.fragments.PatientFragment;
import com.cdac.uphmis.patientprescriptionscanner.fragments.ViewPrescriptionSelectEpisodeFragment;
import com.cdac.uphmis.phr.PHRActivity;
import com.cdac.uphmis.DocsUpload.DocsUpldNewActivity;
import com.cdac.uphmis.phr.PhrViewDocNewActivity;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.qms.IssueTokenActivity;
import com.cdac.uphmis.reimbursement.ClaimEnquiryActivity;
import com.cdac.uphmis.reimbursement.model.ClaimEnquiryDetails;
import com.cdac.uphmis.sickLeave.SickLeaveActivity;
import com.cdac.uphmis.transactions.TransactionsListActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.UpdateFcmToken;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientHomeFragment extends Fragment {
    ManagingSharedData msd;

    RequestQueue requestQueue;
    //This was Nested scroll
    LinearLayout svModules;
    TextView cr;
    private GeometricProgressView progressView;
    public static ArrayList<String> fetaureIdArrayList;

    RecyclerView recyclerView;
    PatientHomeFragmentAdapter adapter;
    List<PatientHomeFragmentModel> list;
    //Search
    private EditText etSearch;
    //Slider
    private ViewPager circleviewPager;
    private SlidingImageAdapter slidingImageAdapter;
    private ArrayList<SliderImageModel> ImagesArray;
    private static int NUM_PAGES = 0;
    int pos = 0;
    LinearLayout etSearch_ll;
    ImageView iv_search;
    boolean search_flag=true;

    public PatientHomeFragment() {
    }

    static int MODE;

    int rating;
    ImageView poorEmoji, averageEmoji, goodEmoji;
    ImageView checked;
    TextView poor_tv, avg_tv, good_tv;
    TextView rating_error;
    TextView remainText, reached_limit_tv;
    TextInputLayout feedback_tv;
    TextView success_tv;
    EditText feedback_et;
    Button skip, submit_btn;
    Button close;
    LinearLayout feedback_parent;
    LinearLayout ll_data;
    LinearLayout rl_successful;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_home, container, false);

        msd = new ManagingSharedData(getActivity());
        /*if (msd.getAppFlag() == null) {
            ServiceUrl.ip=ServiceUrl.nhmIp;
        }else {
            ServiceUrl.ip=msd.getAppFlag();
            Log.d("ServiceUrlServiceUrl",""+ServiceUrl.ip);
        }*/


        if (msd.getPatientDetails().getCrno() == null) {
            msd.logOut();
            startActivity(new Intent(getContext(), LoginMainScreenActivity.class));
            getActivity().finish();
        }
        fetaureIdArrayList = new ArrayList<>();

        iv_search = view.findViewById(R.id.iv_search);
        etSearch_ll = view.findViewById(R.id.etSearch_ll);
        progressView = view.findViewById(R.id.progress_view);
        recyclerView = view.findViewById(R.id.recyclerView);
        etSearch_ll.setVisibility(View.GONE);

        if (msd.getPatientDetails().getPatHealthId() == null || msd.getPatientDetails().getPatHealthId().isEmpty()) {
            if (msd.getAbhaReminderCount() < 5) {
                //  abhaNotFoundDialog();
            }
        }

        SearchHome(view);
        //  setUpRecycler();
        //  BottomNavigationView navigation = view.findViewById(R.id.patient_navigation);
        //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ImageView tvUpdateMyApp = view.findViewById(R.id.tv_update_my_app);
        tvUpdateMyApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityFunctions.gotoPlayStore(getContext());
            }
        });
        requestQueue = Volley.newRequestQueue(getActivity());
        svModules = view.findViewById(R.id.sv_modules);
        cr = view.findViewById(R.id.cr);
        svModules.setVisibility(View.GONE);
        TextView tvName = view.findViewById(R.id.name);
        tvName.setText(getString(R.string.hi) + ", " + msd.getPatientDetails().getFirstname());
        cr.setText(getString(R.string.cr_no) + " " + msd.getPatientDetails().getCrno());
        msd.writeSeatNo("");
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search_flag){
                    search_flag=false;
                    etSearch_ll.setVisibility(View.VISIBLE);
                }else {
                    search_flag=true;
                    etSearch_ll.setVisibility(View.GONE);
                }
            }
        });

        checkVisibleModules(view);

        try {
            UpdateFcmToken.updateToken(getContext(), msd.getPatientDetails());

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return view;
    }

    private void findABHAId(Dialog dialog, ProgressBar pbAbha, String userName) {
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

                    if ((healthIdNumber != null && !healthIdNumber.isEmpty() && !healthIdNumber.equalsIgnoreCase("null")) || (healthId != null && !healthId.isEmpty() && !healthId.equalsIgnoreCase("null"))) {
                        //  if (healthId!=null && !healthId.isEmpty() && !healthId.equalsIgnoreCase("null"))
                        generateOTP(dialog, pbAbha, healthId, healthIdNumber);
                        // else
                        //   generateOTP(dialog,healthIdNumber,healthIdNumber);
                    } else {
                        Toast.makeText(getContext(), "Invalid health ID", Toast.LENGTH_SHORT).show();
                    }

                    //updateHealthId(dialog,healthId,healthIdNumber);
                    pbAbha.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    pbAbha.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Invalid Health ID", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    pbAbha.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            }, error -> {
                pbAbha.setVisibility(View.GONE);
                Toast.makeText(getContext(), "ABHA ID Not Found", Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", error.toString());
                AppUtilityFunctions.handleExceptions(error, getContext());
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
            MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateOTP(Dialog dialog, ProgressBar pbAbha, String healthId, String healthIdNumber) throws Exception {

        String URL = ndhmip + "hip/v0.5/users/auth/hisinit1";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("patHospitalHealthId", msd.getPatientDetails().getCrno());
        jsonObject.put("isKYC", "0");
        jsonObject.put("patNdhmHealthId", healthId);
        jsonObject.put("authenticationMode", "MOBILE_OTP");
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
                        message = "Invalid Health ID";
                    AppUtilityFunctions.showErrorDialog(getContext(), message);

                } else {
                    LinearLayout llEnterOtp = dialog.findViewById(R.id.ll_enter_otp);

                    EditText edtOtp = dialog.findViewById(R.id.edt_otp);
                    Button btnSubmit = dialog.findViewById(R.id.btn_submit);
                    LinearLayout llEnterId = dialog.findViewById(R.id.ll_enter_id);
                    llEnterId.setVisibility(View.GONE);
                    llEnterOtp.setVisibility(View.VISIBLE);

                    btnSubmit.setOnClickListener(v -> {
                        if (edtOtp.getText().toString().length() != 6)
                            Toast.makeText(getContext(), "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                        else {
                            verifyOTP(dialog, pbAbha, healthId, healthIdNumber, txnId, edtOtp.getText().toString());


                        }
                    });

                }

            } catch (JSONException e) {
                pbAbha.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(getContext(), "Invalid health ID", Toast.LENGTH_SHORT).show();

            }
        }, error -> {
            pbAbha.setVisibility(View.GONE);
            Log.e("VOLLEY", error.toString());
//            AppUtilityFunctions.handleExceptions(error, getContext());
            Toast.makeText(getContext(), "Invalid health ID", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void verifyOTP(Dialog dialog, ProgressBar pbAbha, String healthId, String healthIdNo, String txnId, String otp) {
        String URL = ndhmip + "hip/v0.5/users/auth/hisconfirm1";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hospitalcode", msd.getPatientDetails().getHospitalCode());
            JSONObject identifierObj = new JSONObject();
            identifierObj.put("type", "HEALTH_ID");
            identifierObj.put("value", healthId);
            jsonObject.put("identifier", identifierObj);
            jsonObject.put("patHospitalHealthId", msd.getPatientDetails().getCrno());
            jsonObject.put("ndhmHealthIDCode", "");
            jsonObject.put("otp", otp);
            jsonObject.put("patNdhmHealthId", healthId);
            jsonObject.put("authenticationMode", "MOBILE_OTP");
            jsonObject.put("transactionId", txnId);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        Log.i("TAG", "verifyOTP: " + jsonObject.toString());

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
                        message = "Invalid Health ID";
                    AppUtilityFunctions.showErrorDialog(getContext(), message);

                } else {
                    updateHealthId(dialog, healthId, healthIdNo);
                    //getCrList(patNdhmHealthId, healthIdNo);
                }
            } catch (JSONException e) {
                pbAbha.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(getContext(), "Invalid health ID", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            pbAbha.setVisibility(View.GONE);
            Log.e("VOLLEY", error.toString());
            // AppUtilityFunctions.handleExceptions(error, getContext());
            Toast.makeText(getContext(), "Invalid health ID", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void updateHealthId(Dialog dialog, String ndhmId, String ndhmPatHealthId) {
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
                        //commented by kk
                        //getCrList(ndhmId, ndhmPatHealthId);
                    } else {
                        Toast.makeText(getContext(), "Unable to Link ABHA Number.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, getContext());
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void SearchHome(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        Selection.setSelection(etSearch.getText(), etSearch.getText().length());
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null)
                    adapter.getFilter().filter(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Selection.setSelection(etSearch.getText(), etSearch.getText().length());
            }
        });
    }
    private void slider(View view) {
        circleviewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tabDots);
        circleviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pos = position;
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        ImagesArray = new ArrayList<>();
        if (!fetaureIdArrayList.contains(APPOINTMENT))
            if (!msd.getPatientDetails().getPatientCatCode().equals(GENERAL_CATEGORY_CODE)) {
                ImagesArray.add(new SliderImageModel(ContextCompat.getDrawable(getContext(), R.drawable.appoinment_banner), APPOINTMENT));
            }
        if (!fetaureIdArrayList.contains(TELECONSULTancy_REQUEST))
            ImagesArray.add(new SliderImageModel(ContextCompat.getDrawable(getContext(), R.drawable.teleconsult_banner),TELECONSULTancy_REQUEST));
        if (!fetaureIdArrayList.contains(LAB_REPORTS))
            ImagesArray.add(new SliderImageModel(ContextCompat.getDrawable(getContext(), R.drawable.lab_report_banner),LAB_REPORTS));
        if (!fetaureIdArrayList.contains(RX_VIEW))
            ImagesArray.add(new SliderImageModel(ContextCompat.getDrawable(getContext(), R.drawable.prescription_view),RX_VIEW));
        if (!fetaureIdArrayList.contains(SELF_REGISTRATION))
            ImagesArray.add(new SliderImageModel(ContextCompat.getDrawable(getContext(), R.drawable.self_registration_banner),SELF_REGISTRATION));
        if (!fetaureIdArrayList.contains(CREATE_YOUR_ABHA))
            ImagesArray.add(new SliderImageModel(ContextCompat.getDrawable(getContext(), R.drawable.abha_banner),CREATE_YOUR_ABHA));
        slidingImageAdapter = new SlidingImageAdapter(getActivity(), ImagesArray);
        circleviewPager.setAdapter(slidingImageAdapter);
        tabLayout.setupWithViewPager(circleviewPager, true);
        NUM_PAGES = ImagesArray.size();
        slidingImageAdapter.notifyDataSetChanged();

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (pos == NUM_PAGES) {
                pos = 0;
            }
            circleviewPager.setCurrentItem(pos++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }
    private void listData() {
        list = new ArrayList<>();
//        Log.i("TAG", "listData: " + fetaureIdArrayList);
        if (!fetaureIdArrayList.contains(MY_CRN))
            list.add(new PatientHomeFragmentModel(MY_CRN, getString(R.string.QR_title), ContextCompat.getDrawable(getContext(), R.drawable.crn), "#fecb00"));
        if (!fetaureIdArrayList.contains(APPOINTMENT))
            if (!msd.getPatientDetails().getPatientCatCode().equals(GENERAL_CATEGORY_CODE)) {
                list.add(new PatientHomeFragmentModel(APPOINTMENT, getString(R.string.AP_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_appointment), "#1dd5ef"));
            }
        if (!fetaureIdArrayList.contains(TELECONSULTancy_REQUEST))
            list.add(new PatientHomeFragmentModel(TELECONSULTancy_REQUEST, getString(R.string.EC_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_teleconsultancy), "#7754f8"));
        if (!fetaureIdArrayList.contains(REQUEST_STATUS))
            if (!msd.getPatientDetails().getPatientCatCode().equals(GENERAL_CATEGORY_CODE)) {
                list.add(new PatientHomeFragmentModel(REQUEST_STATUS, getString(R.string.VR_title), ContextCompat.getDrawable(getContext(), R.drawable.request_status), "#11a5ed"));
            }
        if (!fetaureIdArrayList.contains(RX_VIEW))
            list.add(new PatientHomeFragmentModel(RX_VIEW, getString(R.string.RX_title), ContextCompat.getDrawable(getContext(), R.drawable.rx_view), "#fecb00"));
        if (!fetaureIdArrayList.contains(LAB_REPORTS))
            list.add(new PatientHomeFragmentModel(LAB_REPORTS, getString(R.string.IV_tit), ContextCompat.getDrawable(getContext(), R.drawable.lab_report), "#7cd420"));
        if (!fetaureIdArrayList.contains(QUES_NO_SLIP))
            list.add(new PatientHomeFragmentModel(QUES_NO_SLIP, getString(R.string.QS_title), ContextCompat.getDrawable(getContext(), R.drawable.queue__no__slip), "#b10080"));
        if (!fetaureIdArrayList.contains(SELF_REGISTRATION))
            list.add(new PatientHomeFragmentModel(SELF_REGISTRATION, getString(R.string.ES_title), ContextCompat.getDrawable(getContext(), R.drawable.self_registration), "#fe8500"));
        if (!fetaureIdArrayList.contains(TOKEN_GENERATION))
            list.add(new PatientHomeFragmentModel(TOKEN_GENERATION, getString(R.string.TG_title), ContextCompat.getDrawable(getContext(), R.drawable.token_generation), "#b3c100"));
        if (!fetaureIdArrayList.contains(CREATE_YOUR_ABHA))
            list.add(new PatientHomeFragmentModel(CREATE_YOUR_ABHA, getString(R.string.HI_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_ndhm_logo), "#1f3f49"));
        if (!fetaureIdArrayList.contains(TRANSACTION))
            list.add(new PatientHomeFragmentModel(TRANSACTION, getString(R.string.MT_title), ContextCompat.getDrawable(getContext(), R.drawable.transaction), "#6ab187"));
        if (!fetaureIdArrayList.contains(SICK_CERTIFICATE))
            list.add(new PatientHomeFragmentModel(SICK_CERTIFICATE, getString(R.string.SC_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_sick_certi), "#1dd5ef"));
        if (!fetaureIdArrayList.contains(IPD_DETAILS))
            list.add(new PatientHomeFragmentModel(IPD_DETAILS, getString(R.string.AC_title), ContextCompat.getDrawable(getContext(), R.drawable.ipd), "#7754f8"));
        if (!fetaureIdArrayList.contains(LP_STATUS))
            list.add(new PatientHomeFragmentModel(LP_STATUS, getString(R.string.LP_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_lp_status), "#7754f8"));
        if (!fetaureIdArrayList.contains(REIMBURSEMENT_CLAIM))
            list.add(new PatientHomeFragmentModel(REIMBURSEMENT_CLAIM, getString(R.string.CL_title), ContextCompat.getDrawable(getContext(), R.drawable.reimbursement_claim), "#11a5ed"));
        if (!fetaureIdArrayList.contains(OPD_ENQRY))
            list.add(new PatientHomeFragmentModel(OPD_ENQRY, getString(R.string.OE_title), ContextCompat.getDrawable(getContext(), R.drawable.opd_enquiry), "#7cd420"));
        if (!fetaureIdArrayList.contains(TARIFF_DETAILS))
            list.add(new PatientHomeFragmentModel(TARIFF_DETAILS, getString(R.string.TR_title), ContextCompat.getDrawable(getContext(), R.drawable.tariff_details), "#b10080"));
        if (!fetaureIdArrayList.contains(LAB_ENQUAIRY))
            list.add(new PatientHomeFragmentModel(LAB_ENQUAIRY, getString(R.string.LE_title), ContextCompat.getDrawable(getContext(), R.drawable.lab_enquiry), "#fe8500"));
        if (!fetaureIdArrayList.contains(DRUG_AVAILABILTY))
            list.add(new PatientHomeFragmentModel(DRUG_AVAILABILTY, getString(R.string.DA_title), ContextCompat.getDrawable(getContext(), R.drawable.drug_availability), "#b3c100"));
        if (!fetaureIdArrayList.contains(PHR))
            list.add(new PatientHomeFragmentModel(PHR, getString(R.string.PH_title), ContextCompat.getDrawable(getContext(), R.drawable.vitalicon), "#b3c100"));
        if (!fetaureIdArrayList.contains(BLOOD_STOCK))
            list.add(new PatientHomeFragmentModel(BLOOD_STOCK, getString(R.string.BS_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_blood_stock), "#1f3f49"));
        if (!fetaureIdArrayList.contains(RX_SCAN))
            list.add(new PatientHomeFragmentModel(RX_SCAN, getString(R.string.PS_title), ContextCompat.getDrawable(getContext(), R.drawable.rx_scan), "#6ab187"));
        if (!fetaureIdArrayList.contains(VIEW_PRESCRIPTION))
        list.add(new PatientHomeFragmentModel(VIEW_PRESCRIPTION, getString(R.string.PV_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_view_docs), "#ea6a47"));
        if (!fetaureIdArrayList.contains(LAB_BASED_APPOINTMENT))
            list.add(new PatientHomeFragmentModel(LAB_BASED_APPOINTMENT, getString(R.string.LB_title), ContextCompat.getDrawable(getContext(), R.drawable.lab_based_appointment_ic), "#1dd5ef"));
        if (!fetaureIdArrayList.contains(FAMILY_QR))
        list.add(new PatientHomeFragmentModel(FAMILY_QR, getString(R.string.FM_title), ContextCompat.getDrawable(getContext(), R.drawable.family_qr), "#1dd5ef"));
        if (!fetaureIdArrayList.contains(DRUG_RX))
            list.add(new PatientHomeFragmentModel(DRUG_RX, getString(R.string.DR_title), ContextCompat.getDrawable(getContext(), R.drawable.drugs_rx_ic), "#1dd5ef"));
        if (!fetaureIdArrayList.contains(ANNOUNCEMENT))
            list.add(new PatientHomeFragmentModel(ANNOUNCEMENT, getString(R.string.AN_title), ContextCompat.getDrawable(getContext(), R.drawable.announce_ic), "#1dd5ef"));
        if (!fetaureIdArrayList.contains(FEEDBACK))
            list.add(new PatientHomeFragmentModel(FEEDBACK, getString(R.string.FB_title), ContextCompat.getDrawable(getContext(), R.drawable.feedback_ic), "#1dd5ef"));
        if (!fetaureIdArrayList.contains(REGISTRATION))
            list.add(new PatientHomeFragmentModel(REGISTRATION, getString(R.string.CR_title), ContextCompat.getDrawable(getContext(), R.drawable.ic_patient_registration), "#1dd5ef"));
    }


    private void setUpRecycler() {
        listData();
        adapter = new PatientHomeFragmentAdapter(getActivity(), list, (position, bg_color_ll, cardClick) -> {
            if (list.get(position).getId().equalsIgnoreCase(TELECONSULTancy_REQUEST)) {
                if (!Settings.canDrawOverlays(getActivity())) {
                    Log.i("appearontop", "onCreate: ");
                    String message = "Permission could be given on <b>Settings->Apps->"+getString(R.string.app_name)+"->App info>Appear on top</b> should be <b>ON</b> , click ok to proceed";
                    displayOverAppsPermission(message);
                } else {
                    gotToActivity(ScreeningActivity.class, getString(R.string.EC_title));
                }
            } else if (list.get(position).getId().equalsIgnoreCase(APPOINTMENT)) {
                //AppUtilityFunctions.showMessageDialog(getActivity(),"Info","This feature will be available soon.");
                //comment by kk
                gotToActivity(OPDAppointmentActivity.class, getString(R.string.AP_title));
            } else if (list.get(position).getId().equalsIgnoreCase(REQUEST_STATUS)) {
               // AppUtilityFunctions.showMessageDialog(getActivity(),"Info","This feature will be available soon.");

                //comment by kk
                Intent intent = new Intent(getActivity(), StatusActivity.class);
                intent.putExtra("fetaureIdArrayList", fetaureIdArrayList);
                intent.putExtra("title", getString(R.string.VR_title));
                startActivity(intent);
            } else if (list.get(position).getId().equalsIgnoreCase(RX_VIEW)) {
                MODE = 1;
                gotToActivity(PrescriptionListActivity.class, getString(R.string.RX_title));
            } else if (list.get(position).getId().equalsIgnoreCase(LAB_REPORTS)) {
                MODE = 2;
                gotToActivity(PrescriptionListActivity.class, getString(R.string.IV_tit));
            } else if (list.get(position).getId().equalsIgnoreCase(QUES_NO_SLIP)) {
                gotToActivity(QMSSlipActivity.class, getString(R.string.QS_title));
            } else if (list.get(position).getId().equalsIgnoreCase(SELF_REGISTRATION)) {
                gotToActivity(EstampingActivity.class, getString(R.string.ES_title));
            } else if (list.get(position).getId().equalsIgnoreCase(TOKEN_GENERATION)) {
                gotToActivity(IssueTokenActivity.class, getString(R.string.TG_title));
            } else if (list.get(position).getId().equalsIgnoreCase(CREATE_YOUR_ABHA)) {
                if (!msd.getPatientDetails().getHealthId().isEmpty()) {
                    String message = "You already have ABHA  *" + msd.getPatientDetails().getHealthId() + "* generated on your mobile number, you can use same id for all references.Press \"Continue With Existing Id\".If you want to generate new ndhm id ,press \"Cancel\"";
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                            .setTitle("Alert")
                            .setMessage(message)
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
//                                    Intent intent = new Intent(getContext(), NDHMGenerateOTPActivity.class);
//                                    startActivity(intent);
                                    gotToActivity(NDHMGenerateOTPActivity.class, getString(R.string.HI_title));
                                    // showStateUTAlert(stateUtmessage);
                                }
                            }).setNegativeButton("Continue With Existing Id", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //dialog.dismiss();
                                    return;
                                }
                            });
                    dialog.show();
                } else {
//                showStateUTAlert(stateUtmessage);
//                    Intent intent = new Intent(getContext(), NDHMGenerateOTPActivity.class);
//                    startActivity(intent);
                    gotToActivity(NDHMGenerateOTPActivity.class, getString(R.string.HI_title));
                }


            } else if (list.get(position).getId().equalsIgnoreCase(TRANSACTION)) {
                gotToActivity(TransactionsListActivity.class, getString(R.string.MT_title));
            } else if (list.get(position).getId().equalsIgnoreCase(SICK_CERTIFICATE)) {
                gotToActivity(SickLeaveActivity.class, getString(R.string.SC_title));
            } else if (list.get(position).getId().equalsIgnoreCase(IPD_DETAILS)) {
               // AppUtilityFunctions.showMessageDialog(getActivity(),"Info","This feature will be available soon.");
                //comment by kk
                gotToActivity(AdmissionSlipActivity.class, getString(R.string.AC_title));
            } else if (list.get(position).getId().equalsIgnoreCase(LP_STATUS)) {
//                AppUtilityFunctions.showMessageDialog(getActivity(),"Info","This feature will be available soon.");
                //comment by kk
                gotToActivity(LPStatusActivity.class, getString(R.string.LP_title));
            } else if (list.get(position).getId().equalsIgnoreCase(REIMBURSEMENT_CLAIM)) {
                gotToActivity(ClaimEnquiryActivity.class, getString(R.string.CL_title));
            } else if (list.get(position).getId().equalsIgnoreCase(MY_CRN)) {
                myCrn();
            } else if (list.get(position).getId().equalsIgnoreCase(OPD_ENQRY)) {
                gotoEnquiryActivity(Enquiry.class, OPD_ENQUIRY, getString(R.string.OE_title));
            } else if (list.get(position).getId().equalsIgnoreCase(TARIFF_DETAILS)) {
                gotoEnquiryActivity(TariffActivity.class, TARIFF_ENQUIRY, getString(R.string.TR_title));
            } else if (list.get(position).getId().equalsIgnoreCase(LAB_ENQUAIRY)) {
                gotoEnquiryActivity(LabEnquiryActivity.class, LAB_ENQUIRY, getString(R.string.LE_title));
            } else if (list.get(position).getId().equalsIgnoreCase(DRUG_AVAILABILTY)) {
                gotoEnquiryActivity(DrugAvailabilityActivity.class, DRUG_AVAILABILITY, getString(R.string.DA_title));
            } else if (list.get(position).getId().equalsIgnoreCase(PHR)) {
                gotToActivity(PHRActivity.class, getString(R.string.PH_title));
            } else if (list.get(position).getId().equalsIgnoreCase(BLOOD_STOCK)) {
                gotToActivity(BloodStockActivity.class, getString(R.string.BS_title));
            } else if (list.get(position).getId().equalsIgnoreCase(RX_SCAN)) {
                rxScan();
            } else if (list.get(position).getId().equalsIgnoreCase(VIEW_PRESCRIPTION)) {
                gotToActivity(DocsViewDocNewActivity.class, getString(R.string.PV_title));
            } else if (list.get(position).getId().equalsIgnoreCase(LAB_BASED_APPOINTMENT)) {
                gotToActivity(LabBasedAppointmentActivity.class, getString(R.string.LB_title));
            } else if (list.get(position).getId().equalsIgnoreCase(UPLOAD_DOCS)) {
                gotToActivity(DocsUpldNewActivity.class, getString(R.string.UD_title));
            }else if (list.get(position).getId().equalsIgnoreCase(FAMILY_QR)) {
                downloadFamilyQRPdf(progressView);
            } else if (list.get(position).getId().equalsIgnoreCase(DRUG_RX)) {
                gotToActivity(DrugRxActivity.class, getString(R.string.DR_title));
            } else if (list.get(position).getId().equalsIgnoreCase(ANNOUNCEMENT)) {
                gotToActivity(AnnouncementActivity.class, getString(R.string.AN_title));
            }  else if (list.get(position).getId().equalsIgnoreCase(REGISTRATION)) {
                gotToActivity(RegistrationActivity.class, getString(R.string.CR_title));
            }else if (list.get(position).getId().equalsIgnoreCase(FEEDBACK)) {
                askForPopup();
            }
        });


        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void gotToActivity(Class classActivity, String title) {
        Intent intent = new Intent(getActivity(), classActivity);
        intent.putExtra("list", MODE);
        intent.putExtra("title", title);
        intent.putExtra("crno", msd.getPatientDetails().getCrno());
        startActivity(intent);
    }

    private void myCrn() {
        String barcodeContents = msd.getPatientDetails().getCrno();
        qrCodeDialog(barcodeContents);
    }

    private void gotoEnquiryActivity(Class classActivity, String module, String title) {
        Intent intent = new Intent(getActivity(), classActivity);
        intent.putExtra("module", module);
        intent.putExtra("title", title);
        intent.putExtra("hospCode", "100");
        startActivity(intent);
    }

    private void rxScan() {
        PatientFragment upload = new PatientFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //  transaction.addToBackStack(null);
        transaction.replace(R.id.container, upload).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(getActivity())) {
                startActivity(new Intent(getActivity(), ScreeningActivity.class));
                return;
            }

            throw new RuntimeException("Overlay permission is required when running in Debug mode.");
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void downloadFamilyQRPdf(GeometricProgressView progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getFamilyQrPdf+msd.getPatientDetails().getMobileNo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String data = jsonObject.getString("BillBase64");
                        AppUtilityFunctions.saveBase64Pdf(getActivity(), "family_qr", "family_qr", data);
                    } else {
                        Toast.makeText(getActivity(), "Pdf Not Found.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtilityFunctions.handleExceptions(error, getActivity());
                progressBar.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    void checkVisibleModules(View view) {
        progressView.setVisibility(View.VISIBLE);

        fetaureIdArrayList.clear();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.checkUpdateurl + "p", response -> {
//                Log.i("response is ", "onResponse: " + response);
            try {
                JSONObject jsonObj = new JSONObject(response);

                if (jsonObj.has("features")) {
                    JSONArray jsonArray = jsonObj.getJSONArray("features");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String featureId = c.getString("featureId");
                        fetaureIdArrayList.add(featureId);
                    }
                    slider(view);
                    setUpRecycler();
                }
                progressView.setVisibility(View.GONE);
                svModules.setVisibility(View.VISIBLE);
            } catch (final Exception e) {
                e.printStackTrace();
                progressView.setVisibility(View.GONE);
            }
        }, error -> {
//            Log.i("error", "onErrorResponse: " + error);

            AppUtilityFunctions.handleExceptions(error, getContext());
            if (getActivity() != null) {
                progressView.setVisibility(View.GONE);
                onError();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void onError() {
        new LovelyStandardDialog(getActivity(), LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.no_internet_connection)
                .setTitle(R.string.no_internet_connect)
                .setMessage(R.string.no_inernet_connect_msg)
                .setNegativeButton(getString(R.string.exit), v -> {

                    getActivity().finish();
                    System.exit(0);
                })
                .setPositiveButton(getString(R.string.retry), v -> {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }).show();
    }

    private void qrCodeDialog(String barcodeContents) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.qr_code_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        ImageView imgQrCode = dialog.findViewById(R.id.img_qr_code);
        ImageButton btnClose = dialog.findViewById(R.id.img_close);
        TextView tvName = dialog.findViewById(R.id.tv_patient_name);
        TextView tvCrno = dialog.findViewById(R.id.tv_crno);
        tvName.setText(msd.getPatientDetails().getFirstname() + " (" + msd.getPatientDetails().getAge() + "/" + msd.getPatientDetails().getGender() + ")");
        tvCrno.setText(getString(R.string.cr_no) + msd.getPatientDetails().getCrno());
        Bitmap bm = generateQrCode(barcodeContents);
        if (bm != null) {
            imgQrCode.setScaleType(ImageView.ScaleType.FIT_XY);
            imgQrCode.setImageBitmap(bm);
        } else {
            Toast.makeText(getContext(), "Sorry unable to generate QR code.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        btnClose.setOnClickListener(view -> dialog.cancel());
        dialog.show();
    }

    private Bitmap generateQrCode(String barcodeContents) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            Map<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

            BitMatrix bitMatrix = writer.encode(barcodeContents, BarcodeFormat.QR_CODE, screenWidth, screenWidth, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void displayOverAppsPermission(String message) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.draw_over_model);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        TextView tvNotNow = dialog.findViewById(R.id.tv_not_now);
        TextView tvAllow = dialog.findViewById(R.id.tv_allow);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        tvMessage.setText(Html.fromHtml(message));

        tvAllow.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, 0);
        });
        tvNotNow.setOnClickListener(view -> dialog.dismiss());
        // dialog.dismiss();
        dialog.show();
    }

    public void askForPopup() {
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.FeedbackUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                   progressView.setVisibility(View.GONE);
                // Log.i("register user", "onResponse: " + response);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("status").equals("1")) {
                            if (jsonObj.getString("msg").equals("1")) {
                                feedDialog();
                            } else {
                              alertInfo();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Could not save data.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.i("error hai ", "onErrorResponse: " + error.getMessage());
                //  progress_view.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, getActivity());

            }
        }) {
            @Override
            //  protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String, String>
                HashMap<String, String> data = new HashMap<>();

                data.put("empNo", "");
                data.put("userId", "");
                data.put("crno", msd.getPatientDetails().getCrno());
                data.put("mobileNo", msd.getPatientDetails().getMobileNo());
                data.put("umidNo", "");
                data.put("raiting", String.valueOf(rating));
                data.put("hospcode", msd.getPatientDetails().getHospitalCode());
                data.put("entrySource", "1");
                data.put("remarks", "");
                data.put("userType", "1");
                data.put("modeval", "2");

                // Log.i("hashmap", "getParams: " + data);

                return data;

            }

        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void alertInfo() {
        String message = "Feedback already given.";
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle("Info")
                .setMessage(message)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void initVar(Dialog dialog) {
        poorEmoji = dialog.findViewById(R.id.poorEmoji);
        averageEmoji = dialog.findViewById(R.id.averageEmoji);
        goodEmoji = dialog.findViewById(R.id.goodEmoji);

        poor_tv = dialog.findViewById(R.id.poor_tv);
        avg_tv = dialog.findViewById(R.id.avg_tv);
        good_tv = dialog.findViewById(R.id.good_tv);

        rating_error = dialog.findViewById(R.id.rating_error);
        feedback_tv = dialog.findViewById(R.id.feedback_tv);
        success_tv = dialog.findViewById(R.id.success_tv);
        feedback_et = dialog.findViewById(R.id.feedback_et);

        submit_btn = dialog.findViewById(R.id.submit_btn);
        skip = dialog.findViewById(R.id.skip);
        ll_data = dialog.findViewById(R.id.ll_data);
        feedback_parent = dialog.findViewById(R.id.feedback_parent);
        rl_successful = dialog.findViewById(R.id.rl_successful);
        close = dialog.findViewById(R.id.close);
        checked = dialog.findViewById(R.id.checked);
        remainText = dialog.findViewById(R.id.remainText);
        reached_limit_tv = dialog.findViewById(R.id.reached_limit_tv);

        feedback_parent.setVisibility(View.GONE);
        reached_limit_tv.setVisibility(View.GONE);


    }
    private void feedDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_feed_new);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        rating = 0;
        initVar(dialog);
        ///Optional String

        String enter_feedback = "Any comments (Optional)";

        SpannableString spannable = new SpannableString(enter_feedback);
        spannable.setSpan(new RelativeSizeSpan(1.25f), 12, 23, 0); //size
        spannable.setSpan(new ForegroundColorSpan(Color.LTGRAY), 12, 23, 0);//Color
        feedback_et.setHint(spannable);

        feedback_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                remainText.setText(0 + s.toString().length() + " / 1000");
                if (feedback_et.getText().length() == 1000) {
                    reached_limit_tv.setVisibility(View.VISIBLE);
                } else {
                    reached_limit_tv.setVisibility(View.GONE);
                }
            }
        });
        GeometricProgressView progress_view = dialog.findViewById(R.id.progress_view);

        poorEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(poorEmoji);
                rating = 1;
                poorEmoji.setImageResource(R.drawable.poor_emoji);
                averageEmoji.setImageResource(R.drawable.emogi_icon21);
                goodEmoji.setImageResource(R.drawable.emogi_icon31);

                poor_tv.setTextSize(20);
                avg_tv.setTextSize(16);
                good_tv.setTextSize(16);

                poor_tv.setTextColor(Color.parseColor("#000000"));
                avg_tv.setTextColor(Color.parseColor("#8a8a8a"));
                good_tv.setTextColor(Color.parseColor("#8a8a8a"));
                visibleData();
                hideData();
            }
        });
        averageEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(averageEmoji);
                rating = 2;

                poorEmoji.setImageResource(R.drawable.emogi_icon11);
                averageEmoji.setImageResource(R.drawable.smile_emoji);
                goodEmoji.setImageResource(R.drawable.emogi_icon31);

                poor_tv.setTextSize(16);
                avg_tv.setTextSize(20);
                good_tv.setTextSize(16);

                poor_tv.setTextColor(Color.parseColor("#8a8a8a"));
                avg_tv.setTextColor(Color.parseColor("#000000"));
                good_tv.setTextColor(Color.parseColor("#8a8a8a"));

                visibleData();
                hideData();
            }
        });
        goodEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(goodEmoji);
                rating = 3;
                poorEmoji.setImageResource(R.drawable.emogi_icon11);
                averageEmoji.setImageResource(R.drawable.emogi_icon21);
                goodEmoji.setImageResource(R.drawable.good_emoji);

                poor_tv.setTextSize(16);
                avg_tv.setTextSize(16);
                good_tv.setTextSize(20);

                poor_tv.setTextColor(Color.parseColor("#8a8a8a"));
                avg_tv.setTextColor(Color.parseColor("#8a8a8a"));
                good_tv.setTextColor(Color.parseColor("#000000"));

                visibleData();
                hideData();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 0) {
                    rating_error.setVisibility(View.VISIBLE);
                } else {
                    //Hide the soft keyboard
                    save(rating, progress_view, dialog);
                    IBinder token = feedback_et.getWindowToken();
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(token, 0);

                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void animation(ImageView animateEmoji) {
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        animateEmoji.startAnimation(scale);

    }

    private void visibleData() {
        feedback_parent.setVisibility(View.VISIBLE);
    }

    private void hideData() {
        rating_error.setVisibility(View.GONE);
    }

    public void save(int rating, GeometricProgressView progress_view, Dialog dialog) {
        progress_view.setVisibility(View.VISIBLE);
        submit_btn.setEnabled(false);
//        String url = "http://10.226.21.136:8380/HISServices/service/railtelService/submitFeedback";
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.FeedbackUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.setCancelable(true);
                progress_view.setVisibility(View.GONE);
                // Log.i("register user", "onResponse: " + response);
//                progressView.setVisibility(GONE);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("status").equals("1")) {
                            submit_btn.setEnabled(true);
                            ll_data.setVisibility(View.GONE);
                            rl_successful.setVisibility(View.VISIBLE);
                            success_tv.setText("" + jsonObj.getString("msg"));
                            animation(checked);
                        } else {
                            dialog.setCancelable(true);
                            submit_btn.setEnabled(true);
                            Toast.makeText(getActivity(), "Could not save data.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //      tv.setText(error.getMessage());
                dialog.setCancelable(false);
                submit_btn.setEnabled(true);
                //Log.i("error hai ", "onErrorResponse: " + error.getMessage());
                progress_view.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, getActivity());
            }
        }) {
            @Override
            //  protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String, String>
                HashMap<String, String> data = new HashMap<>();

                data.put("empNo", "");
                data.put("userId", "");
                data.put("crno", msd.getPatientDetails().getCrno());
                data.put("mobileNo", msd.getPatientDetails().getMobileNo());
                data.put("umidNo","");
                data.put("raiting", String.valueOf(rating));
                data.put("hospcode", msd.getPatientDetails().getHospitalCode());
                data.put("entrySource", "1");

                if (feedback_et.getText().length() == 0) {
                    data.put("remarks", "");
                } else {
                    data.put("remarks", feedback_et.getText().toString());
                }
                data.put("userType", "1");
                data.put("modeval", "1");

                Log.i("hashmap", "getParams: " + data);

                return data;

            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0
                ,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


}
