package com.cdac.uphmis.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.DoctorDesk.DoctorRequestListActivity;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.DoctorDeskActivity;
import com.cdac.uphmis.EMRDeskActivity;
import com.cdac.uphmis.OPDEnquiry.Enquiry;
import com.cdac.uphmis.R;
import com.cdac.uphmis.SelectHospitalActitivy;
import com.cdac.uphmis.TariffEnquiry.TariffActivity;
import com.cdac.uphmis.bloodstock.BloodStockActivity;
import com.cdac.uphmis.drugavailability.DrugAvailabilityActivity;
import com.cdac.uphmis.labEnquiry.LabEnquiryActivity;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.prescriptionscanner.fragments.PatientFragment;
import com.cdac.uphmis.prescriptionscanner.fragments.ViewPrescriptionSelectEpisodeFragment;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.bohush.geometricprogressview.GeometricProgressView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.nikartm.support.ImageBadgeView;

import static com.cdac.uphmis.util.AppConstants.DRUG_AVAILABILITY;
import static com.cdac.uphmis.util.AppConstants.LAB_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.OPD_ENQUIRY;
import static com.cdac.uphmis.util.AppConstants.TARIFF_ENQUIRY;


public class DoctorHomeFragment extends Fragment {

    private static final String TAG = "doctorhomefragment";
    RequestQueue requestQueue;
    ManagingSharedData msd;

    GeometricProgressView progressView;

    ScrollView svModules;

    CardView cardStats;
    LinearLayout  eConsultation, labReports, DoctorsDesk, prescriptionScanner, viewPrescription, cardOPDWebPrescription, cardDrugAvailability, cardBloodStockEnquiry, cardEmr;


    LinearLayout cardEnquiry, cardTariff, cardLabEnquiry;

    TextView tvTodays, tvUpcomming, tvPast;

    BottomNavigationView navigation;


    TextView tvTodaysUnattended, tvTodaysApproved, tvTodaysCompleted, tvTodaysRejected;
    TextView tvUpcomingUnaatended, tvUpcomingRejected;
    TextView tvPastCompleted, tvPastRejected;

    LinearLayout llStatsDetails;


    TextView tvShowDetails;


    ImageBadgeView pendingRequestCount;

    private ArrayList<String> deskTypeArrayList;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        deskTypeArrayList = new ArrayList<>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        progressView = view.findViewById(R.id.progress_view);


        svModules = view.findViewById(R.id.sv_modules);
        navigation = (BottomNavigationView) getActivity().findViewById(R.id.doctor_navigation);
        navigation.setVisibility(View.VISIBLE);

        cardStats = view.findViewById(R.id.card_stats);
        eConsultation = view.findViewById(R.id.card_e_consultation);
        pendingRequestCount = view.findViewById(R.id.pending_request_badge_count);


        requestQueue = Volley.newRequestQueue(getActivity());
        msd = new ManagingSharedData(getActivity());


        tvTodays = view.findViewById(R.id.tv_todays);
        tvUpcomming = view.findViewById(R.id.tv_upcomming);
        tvPast = view.findViewById(R.id.tv_past);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        eConsultation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DoctorRequestListActivity.class);
            intent.putExtra("desk", deskTypeArrayList);
            startActivity(intent);

        });

        DoctorsDesk = view.findViewById(R.id.card_speech_to_text);
        DoctorsDesk.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), DoctorDeskActivity.class)));
        initializeStatsData(view);



      /*  cardEnquiry = view.findViewById(R.id.card_enquiry);
        cardEnquiry.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), Enquiry.class)));

        cardTariff = view.findViewById(R.id.card_tariff);
        cardTariff.setOnClickListener(view13 -> startActivity(new Intent(getActivity(), TariffActivity.class)));

        cardLabEnquiry = view.findViewById(R.id.card_lab_enquiry);
        cardLabEnquiry.setOnClickListener(view14 -> startActivity(new Intent(getActivity(), LabEnquiryActivity.class)));

        cardDrugAvailability = view.findViewById(R.id.card_drug_availability);
        cardDrugAvailability.setOnClickListener((View.OnClickListener) view111 -> startActivity(new Intent(getActivity(), DrugAvailabilityActivity.class)));
*/

        cardEnquiry = view.findViewById(R.id.card_enquiry);
        cardEnquiry.setOnClickListener(view16 -> {
            Intent intent = new Intent(getActivity(), Enquiry.class);
           // intent.putExtra("module", OPD_ENQUIRY);
            startActivity(intent);
        });

        cardLabEnquiry = view.findViewById(R.id.card_lab_enquiry);
        cardLabEnquiry.setOnClickListener(view15 -> {
            Intent intent = new Intent(getActivity(), LabEnquiryActivity.class);
            //intent.putExtra("module", LAB_ENQUIRY);
            startActivity(intent);

        });

        cardTariff = view.findViewById(R.id.card_tariff);
        cardTariff.setOnClickListener(view14 -> {
                    Intent intent = new Intent(getActivity(), TariffActivity.class);
                   // intent.putExtra("module", TARIFF_ENQUIRY);
                    startActivity(intent);
                }
        );

        cardDrugAvailability = view.findViewById(R.id.card_drug_availability);
        cardDrugAvailability.setOnClickListener((View.OnClickListener) view111 -> {
                    Intent intent = new Intent(getActivity(), DrugAvailabilityActivity.class);
                    //intent.putExtra("module", DRUG_AVAILABILITY);
                    startActivity(intent);
                }
        );


        cardBloodStockEnquiry = view.findViewById(R.id.card_blood_stock_enquiry);
        cardBloodStockEnquiry.setOnClickListener(view16 -> startActivity(new Intent(getActivity(), BloodStockActivity.class)));


        labReports = view.findViewById(R.id.card_labReports);
        labReports.setOnClickListener(view15 -> {
            if (msd.getCrNo() == null) {
                DoctorNewCr enterNewCrFragment = new DoctorNewCr();
                Bundle args = new Bundle();
                args.putInt("navigateTo", 1);
                enterNewCrFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, enterNewCrFragment);
                transaction.commit();
            } else {
                // navigation.setVisibility(View.GONE);
               /* DoctorLabReportFragment doctorLabReportFragment = new DoctorLabReportFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, doctorLabReportFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
                Intent intent = new Intent(getActivity(), PrescriptionListActivity.class);
                intent.putExtra("list", 2);
                startActivity(intent);
            }
        });


        cardEmr = view.findViewById(R.id.card_emr);
        cardEmr.setOnClickListener(view15 -> {
            if (msd.getCrNo() == null) {
                DoctorNewCr enterNewCrFragment = new DoctorNewCr();
                Bundle args = new Bundle();
                args.putInt("navigateTo", 5);
                enterNewCrFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, enterNewCrFragment);
                transaction.commit();
            } else {
                Intent intent = new Intent(getActivity(), EMRDeskActivity.class);
                startActivity(intent);
            }
        });


        TextView tvUpdateMyApp = view.findViewById(R.id.tv_update_my_app);
        tvUpdateMyApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityFunctions.gotoPlayStore(getContext());
            }
        });


        prescriptionScanner = view.findViewById(R.id.card_upload_prescription);
        prescriptionScanner.setOnClickListener(view18 -> {
            if (msd.getCrNo() == null) {
                DoctorNewCr enterNewCrFragment = new DoctorNewCr();
                Bundle args = new Bundle();
                args.putInt("navigateTo", 2);
                enterNewCrFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, enterNewCrFragment);
                transaction.commit();
            } else {
                navigation.setVisibility(View.GONE);
                PatientFragment upload = new PatientFragment();
                Bundle args = new Bundle();
                args.putString("crno", msd.getCrNo());
                upload.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.container, upload).commit();
            }

        });
        viewPrescription = view.findViewById(R.id.card_view_prescription);
        viewPrescription.setOnClickListener(view16 -> {
            if (msd.getCrNo() == null) {
                DoctorNewCr enterNewCrFragment = new DoctorNewCr();
                Bundle args = new Bundle();
                args.putInt("navigateTo", 3);
                enterNewCrFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, enterNewCrFragment);
                transaction.commit();
            } else {
                navigation.setVisibility(View.GONE);
                ViewPrescriptionSelectEpisodeFragment viewPrescriptionSelectEpisodeFragment = new ViewPrescriptionSelectEpisodeFragment();
                Bundle args = new Bundle();
                args.putString("crno", msd.getCrNo());
                viewPrescriptionSelectEpisodeFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.container, viewPrescriptionSelectEpisodeFragment).commit();
            }

        });

        cardOPDWebPrescription = view.findViewById(R.id.card_opd_web_prescriptions);
        cardOPDWebPrescription.setOnClickListener(view17 -> {
            if (msd.getCrNo() == null) {
                DoctorNewCr enterNewCrFragment = new DoctorNewCr();
                Bundle args = new Bundle();
                args.putInt("navigateTo", 4);
                enterNewCrFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, enterNewCrFragment);
                transaction.commit();
            } else {
//                startActivity(new Intent(getActivity(), PatientEpisodeListActivity.class));
                Intent intent = new Intent(getActivity(), PrescriptionListActivity.class);
                intent.putExtra("list", 1);
                startActivity(intent);
            }

        });

//        cardFeedback = view.findViewById(R.id.card_feedback);
//        cardFeedback.setOnClickListener(view19 -> startActivity(new Intent(getActivity(), ViewFeedback.class)));

        getRequestList();
//        progressView.setVisibility(View.GONE);
//        svModules.setVisibility(View.VISIBLE);
        checkVisibleModules();

        ImageView btnRefreshStats = view.findViewById(R.id.btn_refresh_stats);
        btnRefreshStats.setOnClickListener(view17 -> getRequestList());
        return view;
    }

    private void initializeStatsData(View view) {

        //todays stats
        tvTodaysUnattended = view.findViewById(R.id.tv_todays_unattended);
        tvTodaysApproved = view.findViewById(R.id.tv_todays_approved);
        tvTodaysCompleted = view.findViewById(R.id.tv_todays_completed);
        tvTodaysRejected = view.findViewById(R.id.tv_todays_rejected);

        //upcoming stats
        tvUpcomingRejected = view.findViewById(R.id.tv_upcoming_rejected);
        tvUpcomingUnaatended = view.findViewById(R.id.tv_upcoming_unattended);

        //past stats

        tvPastCompleted = view.findViewById(R.id.tv_past_completed);
        tvPastRejected = view.findViewById(R.id.tv_past_rejected);
        llStatsDetails = view.findViewById(R.id.ll_stats_details);

        tvShowDetails = view.findViewById(R.id.tv_show_details);

        tvShowDetails.setOnClickListener(view1 -> {
            if (llStatsDetails.getVisibility() == View.VISIBLE) {
                llStatsDetails.setVisibility(View.GONE);
                tvShowDetails.setText(R.string.show_details);
            } else {
                llStatsDetails.setVisibility(View.VISIBLE);
                tvShowDetails.setText(R.string.hide_details);
            }
        });
    }


    private void getRequestList() {
//             NukeSSLCerts.nuke(getActivity());

        Log.i(TAG, "getRequestList: " + ServiceUrl.viewRequestListByEmployeeCode + msd.getEmployeeCode() + "&hospCode=" + msd.getHospCode());
        final ArrayList<DoctorReqListDetails> todaysArraylist = new ArrayList<>();
        final ArrayList<DoctorReqListDetails> upcommingArrayList = new ArrayList<>();
        final ArrayList<DoctorReqListDetails> pastArrayList = new ArrayList<>();

//todays
        final int[] todaysUnattended = {0};
        final int[] todaysCompleted = {0};
        final int[] todaysApproved = {0};
        final int[] todaysRejected = {0};

//ucoming
        final int[] upComingUnattended = {0};
        final int[] upComingRejected = {0};


        //past
        final int[] pastCompleted = {0};
        final int[] pastRejected = {0};


//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.viewRequestListByEmployeeCode + msd.getEmployeeCode() + "&hospCode=" + msd.getHospCode(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //  Log.i(TAG, "onResponse: " + response);
                try {
                    String strResponse = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    //    Log.i(TAG, "decodedresponse: " + strResponse);


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String requestID = c.getString("requestID");
                        String crNo = c.getString("CRNo");
                        String scrResponse = c.getString("scrResponse");
                        String consName = c.getString("consName");
                        String deptUnitCode = c.getString("deptUnitCode");
                        String deptUnitName = c.getString("deptUnitName");
                        String hospCode = c.getString("hospCode");
                        String requestStatus = c.getString("requestStatus");
                        String patMobileNo = c.getString("patMobileNo");
                        String consMobileNo = c.getString("consMobileNo");
                        String patDocs = c.getString("patDocs");
                        String docMessage = c.getString("docMessage");
                        String cnsltntId = c.getString("cnsltntId");
                        String patName = c.getString("patName");
                        String patAge = c.getString("patAge");
                        String patGender = c.getString("patGender");
                        String rmrks = c.getString("rmrks");
                        String email = c.getString("email");
                        String date = c.getString("date");
                        String patWeight = c.getString("patWeight");
                        String patHeight = c.getString("patHeight");
                        String patMedication = c.getString("patMedication");
                        String patPastDiagnosis = c.getString("patPastDiagnosis");
                        String patAllergies = c.getString("patAllergies");
                        String deptCode = c.getString("deptCode");
                        String deptName = c.getString("deptName");
                        String isDocUploaded = c.getString("isDocUploaded");
                        String patientToken = c.getString("patientToken");
                        String doctorToken = c.getString("doctorToken");


                        String appointmentNo = c.getString("appointmentNo");
                        String apptStartTime = c.getString("apptStartTime");
                        String apptEndTime = c.getString("apptEndTime");
                        String apptDate = c.getString("apptDate");
                        String hospitalName = c.getString("hospitalName");
                        String isEpisodeExist = c.getString("isEpisodeExist");
                        String episodeCode = c.getString("episodeCode");
                        String episodeVisitNo = c.getString("episodeVisitNo");

                        String requestStatusCompleteDate = c.getString("requestStatusCompleteDate");
                        String requestStatusCompleteMode = c.getString("requestStatusCompleteMode");


                        patName = AppUtilityFunctions.capitalizeString(patName);


                        Date appointmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(apptDate);
                        Date todaysDate = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(todaysDate));

                        Log.i(TAG, "compareTo" + appointmentDate.compareTo(date2));

                        //upcoming data
                        if (appointmentDate.compareTo(date2) > 0) {
                            if (requestStatus.equalsIgnoreCase("0") || requestStatus.equalsIgnoreCase("4")) {
                                upcommingArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo, requestStatusCompleteDate, requestStatusCompleteMode));
                            }


                            if (requestStatus.equalsIgnoreCase("0")) {
                                upComingUnattended[0] = upComingUnattended[0] + 1;

                            } else if (requestStatus.equalsIgnoreCase("4")) {
                                upComingRejected[0] = upComingRejected[0] + 1;

                            }

                        }
                        //todays data
                        else if (appointmentDate.compareTo(date2) == 0) {
//                            if (requestStatus.equalsIgnoreCase("0") || requestStatus.equalsIgnoreCase("1")) {
                            todaysArraylist.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo, requestStatusCompleteDate, requestStatusCompleteMode));
//                            }

                            if (requestStatus.equalsIgnoreCase("0")) {
                                todaysUnattended[0] = todaysUnattended[0] + 1;

                            } else if (requestStatus.equalsIgnoreCase("1")) {
                                todaysApproved[0] = todaysApproved[0] + 1;

                            } else if (requestStatus.equalsIgnoreCase("2")) {
                                todaysCompleted[0] = todaysCompleted[0] + 1;
                            } else if (requestStatus.equalsIgnoreCase("4")) {
                                todaysRejected[0] = todaysRejected[0] + 1;
                            }


                        }
                        //past data
                        if (appointmentDate.compareTo(date2) < 0) {
                            if (requestStatus.equalsIgnoreCase("2") || requestStatus.equalsIgnoreCase("4")) {
                                pastArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo, requestStatusCompleteDate, requestStatusCompleteMode));
                            }


                            if (requestStatus.equalsIgnoreCase("2")) {
                                pastCompleted[0] = pastCompleted[0] + 1;

                            } else if (requestStatus.equalsIgnoreCase("4")) {
                                pastRejected[0] = pastRejected[0] + 1;

                            }

                        }
                    }


                    Log.i(TAG, "todays" + todaysArraylist.size());
                    Log.i(TAG, "upcomming" + upcommingArrayList.size());
                    Log.i(TAG, "past" + pastArrayList.size());

                    Log.i(TAG, "todays unattended" + todaysUnattended[0]);
                    Log.i(TAG, "todaysApproved" + todaysApproved[0]);
                    Log.i(TAG, "todaysCompleted" + todaysCompleted[0]);


                    tvTodaysUnattended.setText("" + todaysUnattended[0]);
                    try {
                        pendingRequestCount.setBadgeValue(todaysUnattended[0]);
                    } catch (Exception ex) {
                    }

                    tvTodaysApproved.setText("" + todaysApproved[0]);
                    tvTodaysCompleted.setText("" + todaysCompleted[0]);
                    tvTodaysRejected.setText("" + todaysRejected[0]);

                    tvUpcomingUnaatended.setText("" + upComingUnattended[0]);
                    tvUpcomingRejected.setText("" + upComingRejected[0]);


                    tvPastCompleted.setText("" + pastCompleted[0]);
                    tvPastRejected.setText("" + pastRejected[0]);

                    Log.i(TAG, "upcomingunattended" + upComingUnattended[0]);
                    Log.i(TAG, "upcomingrejected" + upComingRejected[0]);


                    Log.i(TAG, "pastcompleted" + pastCompleted[0]);
                    Log.i(TAG, "pastrejected" + pastRejected[0]);


                    tvTodays.setText("" + todaysArraylist.size());
                    tvUpcomming.setText("" + upcommingArrayList.size());
                    tvPast.setText("" + pastArrayList.size());


                } catch (Exception ex) {
                    Log.i(TAG, "exception" + ex);
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AppUtilityFunctions.handleExceptions(error, getActivity());

            }
        });
        request.setRetryPolicy(new

                DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


    void checkVisibleModules() {
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.checkUpdateurl + "d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response is ", "onResponse: " + response);

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    /*String appId = jsonObj.getString("appId");
                    String hospName = jsonObj.getString("hospName");
                    String appName = jsonObj.getString("appName");
                    String appVersion = jsonObj.getString("appVersion");
                    String appUpdatedOn = jsonObj.getString("appUpdatedOn");
                    String forceUpdate = jsonObj.getString("forceUpdate");
                    String appPlatform = jsonObj.getString("appPlatform");
*/

                    if (jsonObj.has("features")) {
                        JSONArray jsonArray = jsonObj.getJSONArray("features");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String featureId = c.getString("featureId");
//                            String featureName = c.getString("featureName");
//                            String userId = c.getString("user_id");


                            if (featureId.equalsIgnoreCase("PS")) {
                                hideView(prescriptionScanner);
                            }
                            if (featureId.equalsIgnoreCase("PV")) {
                                hideView(viewPrescription);
                            }

                            if (featureId.equalsIgnoreCase("TR")) {
                                hideView(cardTariff);
                            }
                            if (featureId.equalsIgnoreCase("OE")) {
                                hideView(cardEnquiry);
                            }
                            if (featureId.equalsIgnoreCase("LE")) {
                                hideView(cardLabEnquiry);
                            }
                            if (featureId.equalsIgnoreCase("DB")) {
//                                hideView();
                                cardStats.setVisibility(View.GONE);
                            }
                            if (featureId.equalsIgnoreCase("IV")) {
                                hideView(labReports);
                            }
                            if (featureId.equalsIgnoreCase("EC")) {
                                hideView(eConsultation);
                            }
                            if (featureId.equalsIgnoreCase("DD")) {
                                hideView(DoctorsDesk);
                            }
                           /* if (featureId.equalsIgnoreCase("FD")) {
                                hideView(cardFeedback);
                            }*/
                            if (featureId.equalsIgnoreCase("RX")) {
                                hideView(cardOPDWebPrescription);
                            }

                            if (featureId.equalsIgnoreCase("DA")) {
                                hideView(cardDrugAvailability);
                            }
                            if (featureId.equalsIgnoreCase("BS")) {
                                hideView(cardBloodStockEnquiry);
                            }
                            if (featureId.equalsIgnoreCase("EM")) {
                                hideView(cardEmr);
                            }
                            if (featureId.equalsIgnoreCase("OD")) {
                                deskTypeArrayList.add("OD");
                            }
                            if (featureId.equalsIgnoreCase("ND")) {
                                deskTypeArrayList.add("ND");
                            }
                        }
                    }
                    progressView.setVisibility(View.GONE);
                    svModules.setVisibility(View.VISIBLE);
                } catch (final Exception e) {
                    Log.i("error", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                }


            }
        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, getActivity());

        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void hideView(View view) {
        if ((ViewGroup) view.getParent() != null)
            ((ViewGroup) view.getParent()).removeView(view);
    }

}
