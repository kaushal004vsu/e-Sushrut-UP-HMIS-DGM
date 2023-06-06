package com.cdac.uphmis.DoctorDesk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.cdac.uphmis.DoctorDesk.fragments.ChiefComplaintFragment;
import com.cdac.uphmis.DoctorDesk.fragments.ClinicalNotesFragment;
import com.cdac.uphmis.DoctorDesk.fragments.DiagnosisFragment;
import com.cdac.uphmis.DoctorDesk.fragments.InvestigationFragment;
import com.cdac.uphmis.DoctorDesk.fragments.TreatmentAdviceFragment;
import com.cdac.uphmis.DoctorDesk.fragments.VitalsFragment;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.ViewDocNewActivity;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.SessionServicecall;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import static android.content.ContentValues.TAG;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.COMPLAINTS_INDEX;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.DIAGNOSIS_INDEX;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.FRAGMENT_SIZE;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.MEDICATIONS_INDEX;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.NOTES_INDEX;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.TABS_SIZE;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.TESTS_INDEX;
import static com.cdac.uphmis.DoctorDesk.DeskUtil.VITALS_INDEX;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;


public class DeskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    EditText edtVitals, edtChiefCoplaint, edtDiagnosis, edtInvestigation, edtMedications, edtClinicalNotes;

    Fragment active;
    FragmentManager fm;


    Fragment[] fragment;
    TextView[] btnSelect;

    TextView tvNext;
    int i = 0;

    Switch sw, swDiagnosis;
    boolean doubleBackToExitPressedOnce = false;
    String strVitals = "", strChiefComplaints = "", strDiagnosis = "", strTests = "", strMedications = "", strClinicalNotes = "";


    DoctorReqListDetails patientDetails;
    AutoCompleteTextView autoCompleteTextView;


    TextView[] indicators;
    TextView tvPercentageFilled;
    int percentageFilled = 0, vitalsCount = 0, compaintsCount = 0, historyCount = 0, examinationsCount = 0, diagnosisCount = 0, drugsCount = 0, testsCount = 0, proceduresCount = 0, treatmentAdvicedCount = 0, clinicalNotesCount = 0;


    String swStatus = "provisional";

    public String snomedCodes = "";
    public String snomeCodesDiagnosis = "";


    private TextView btnFollowUp;
    private String followUpDate = "";
    private String revisitDisplayDate = "";
    private List<Calendar> datesArrayList;


    //SimboAlphaHelper helper;
    MovableFloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk);
        
        btnFollowUp = findViewById(R.id.btn_follow_up);
        getHolidayDates();


        myCreateView();
        initIndiactors();
        Log.i(TAG, "requestid: " + patientDetails.getRequestID());

    }

    @Override
    protected void onStart() {
        super.onStart();
        edtChiefCoplaint = findViewById(R.id.edt_chief_complaint);
        autoCompleteTextView = findViewById(R.id.auto_complete_final_diagnosis);
        swDiagnosis = (Switch) findViewById(R.id.switch_final_diagnosis);

    }

    public void myCreateView() {

        indicators = new TextView[6];
        ImageView imageToolBar = findViewById(R.id.img_toolbar);
        final ImageView imgMenu = findViewById(R.id.img_menu);
        TextView tvPatientname = findViewById(R.id.tv_patient_name);
        TextView tvAgeGender = findViewById(R.id.tv_age_gender);
        final Dialog dialog = new Dialog(DeskActivity.this);
        dialog.setContentView(R.layout.switch_layout);
        dialog.setTitle("");
        patientDetails = (DoctorReqListDetails) getIntent().getSerializableExtra("patientdetails");

        if (patientDetails.getPatGender().startsWith("F")) {

            Drawable res = getResources().getDrawable(R.drawable.female_transparent);
            imageToolBar.setImageDrawable(res);

        } else {
            Drawable res = getResources().getDrawable(R.drawable.male);
            imageToolBar.setImageDrawable(res);
        }
        tvPatientname.setText(patientDetails.getPatName());
        tvAgeGender.setText(patientDetails.getPatAge());
        Log.i("gender", "onCreate: " + patientDetails.getPatGender());
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sw = (Switch) dialog.findViewById(R.id.switch_id);
                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Toast.makeText(DeskActivity.this, "on", Toast.LENGTH_SHORT).show();

                            // The toggle is enabled
                        } else {
                            Toast.makeText(DeskActivity.this, "off", Toast.LENGTH_SHORT).show();

                            // The toggle is disabled
                        }
                    }
                });


                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        tvNext = findViewById(R.id.tv_next);

        btnSelect = new TextView[TABS_SIZE];


         fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
            }
        });

        btnSelect[VITALS_INDEX] = findViewById(R.id.btn_vitals);
        btnSelect[VITALS_INDEX].setOnClickListener(this);

        btnSelect[COMPLAINTS_INDEX] = findViewById(R.id.btn_chief_complaint);
        btnSelect[COMPLAINTS_INDEX].setOnClickListener(this);


        btnSelect[DIAGNOSIS_INDEX] = findViewById(R.id.btn_diagnosis);
        btnSelect[DIAGNOSIS_INDEX].setOnClickListener(this);

        btnSelect[TESTS_INDEX] = findViewById(R.id.btn_investigation);
        btnSelect[TESTS_INDEX].setOnClickListener(this);

        btnSelect[MEDICATIONS_INDEX] = findViewById(R.id.btn_medications);
        btnSelect[MEDICATIONS_INDEX].setOnClickListener(this);

        btnSelect[NOTES_INDEX] = findViewById(R.id.btn_clinical_notes);
        btnSelect[NOTES_INDEX].setOnClickListener(this);


        fragment = new Fragment[FRAGMENT_SIZE];


        fragment[VITALS_INDEX] = new VitalsFragment();
        fragment[COMPLAINTS_INDEX] = new ChiefComplaintFragment();
        fragment[DIAGNOSIS_INDEX] = new DiagnosisFragment();
        fragment[TESTS_INDEX] = new InvestigationFragment();
        fragment[MEDICATIONS_INDEX] = new TreatmentAdviceFragment();
        fragment[NOTES_INDEX] = new ClinicalNotesFragment();


        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, fragment[5], "6").hide(fragment[5]).commit();
        fm.beginTransaction().add(R.id.container, fragment[4], "5").hide(fragment[4]).commit();
        fm.beginTransaction().add(R.id.container, fragment[3], "4").hide(fragment[3]).commit();
        fm.beginTransaction().add(R.id.container, fragment[2], "3").hide(fragment[2]).commit();
        fm.beginTransaction().add(R.id.container, fragment[1], "2").hide(fragment[1]).commit();
        fm.beginTransaction().add(R.id.container, fragment[0], "1").commit();


        active = fragment[0];
        edtChiefCoplaint = findViewById(R.id.edt_chief_complaint);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButtonClicked();
            }
        });

        TextView tvPrevious = findViewById(R.id.tv_previous);
        tvPrevious.setText("<<");


    }


    private void nextButtonClicked() {
        i = i + 1;
        if (i >= TABS_SIZE) {
            i = 0;
        }
        fm.beginTransaction().hide(active).show(fragment[i]).commit();
        active = fragment[i];
        highlightBtn(btnSelect[i]);
        if (sw == null || sw.isChecked()) {
            getSpeechInput();
        }
        checkIndicatorStatus();
    }

    public void previousButtonClicked() {
        i = i - 1;
        if (i < 0) {
            i = TABS_SIZE - 1;
        }
        fm.beginTransaction().hide(active).show(fragment[i]).commit();
        active = fragment[i];
        highlightBtn(btnSelect[i]);
        if (sw == null || sw.isChecked()) {
            getSpeechInput();
        }
        checkIndicatorStatus();
    }

    @Override
    public void onClick(View view) {
        Log.i("hfdiuhdfijuhnfr", "onClick: ");
        switch (view.getId()) {
            case R.id.btn_vitals:
                onTabButtonClicked(VITALS_INDEX);
                break;
            case R.id.btn_chief_complaint:
                onTabButtonClicked(COMPLAINTS_INDEX);
                break;
            case R.id.btn_diagnosis:
                onTabButtonClicked(DIAGNOSIS_INDEX);
                break;

            case R.id.btn_investigation:
                onTabButtonClicked(TESTS_INDEX);
                break;

            case R.id.btn_medications:
                onTabButtonClicked(MEDICATIONS_INDEX);
                break;

            case R.id.btn_clinical_notes:
                onTabButtonClicked(NOTES_INDEX);
                break;


        }


    }


    private void onTabButtonClicked(int i) {
        edtVitals = findViewById(R.id.edt_vitals);

        edtChiefCoplaint = findViewById(R.id.edt_chief_complaint);

        edtDiagnosis = findViewById(R.id.edt_diagnosis);
        autoCompleteTextView = findViewById(R.id.auto_complete_final_diagnosis);

        edtInvestigation = findViewById(R.id.edt_investigations);

        edtMedications = findViewById(R.id.edt_treatment_advice);

        edtClinicalNotes = findViewById(R.id.edt_clinical_notes);

        fm.beginTransaction().hide(active).show(fragment[i]).commit();
        active = fragment[i];
        highlightBtn(btnSelect[i]);

        checkIndicatorStatus();



    }

  /*  private void initSimboHelper() {

        if (helper!=null) {
            helper.disConnectHelper();
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,android.R.color.holo_blue_dark)));

        helper=null;
        return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("devId","demo-api-000029-intg-gp-CDAC");
        map.put("authKey","c161bc17a4b3b2386763e7faf39c1103b19a391f3df20f05fbdc650d2b745870");
        map.put("wssUrl1","wss://test.alpha.phit.in/asr");
        helper = new SimboAlphaHelper(map);
        helper.connectWSSandAutenticate();
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.red)));
        helper.onWSSMessage(s -> {
            Log.i(TAG, "onWSSMessage: "+s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject stt=jsonObject.optJSONObject("stt");
                String data=stt.optString("txt");
                parseData(data);
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        });
        helper.onWSSError(s -> {
            Log.i(TAG, "onWSSError: "+s);
            return null;
        });
        helper.onError(s -> {
            Log.i(TAG, "onError: "+s);
            return null;
        });
        helper.onAlertMsg(s -> {
            Log.i(TAG, "onAlertMsg: "+s);
            return null;
        });
      *//*  helper.onVolumeLevelChange(s -> {
            Log.i(TAG, "onVolumeLevelChange: "+s);
            return null;
        });
        helper.onUpdateGetFullEMR(s -> {
            Log.i(TAG, "onUpdateGetFullEMR: "+s);
            return null;
        });*//*


    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                Log.i("request code", "onActivityResult: " + requestCode);
                Log.i("result code", "onActivityResult: " + resultCode);
                Log.i("data", "onActivityResult: " + data);
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String word = result.get(0).toLowerCase();
                    Log.i("result", "onActivityResult: " + result);
                    if (active == fragment[COMPLAINTS_INDEX]) {
                        edtChiefCoplaint = findViewById(R.id.edt_chief_complaint);

                        if (result.get(0).toLowerCase().contains("next")) {
                            edtChiefCoplaint.append(" " + result.get(0).replace("next", ""));
                            nextButtonClicked();

                        } else {
                            speechNavigation(edtChiefCoplaint, word, result);
                        }


                        strChiefComplaints = edtChiefCoplaint.getText().toString();
                        break;
                    }


                    if (active == fragment[DIAGNOSIS_INDEX]) {
                        edtDiagnosis = findViewById(R.id.edt_diagnosis);
                        autoCompleteTextView = findViewById(R.id.auto_complete_final_diagnosis);

                        if (result.get(0).toLowerCase().contains("next")) {
                            edtDiagnosis.append(" " + result.get(0).replace("next", ""));
                            nextButtonClicked();

                        } else if (word.equalsIgnoreCase("complaint") || word.equalsIgnoreCase("complaints") || word.equalsIgnoreCase("cheif") || word.equalsIgnoreCase("chief") || word.equalsIgnoreCase("cheif complaint")) {
                            edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                            onTabButtonClicked(COMPLAINTS_INDEX);
                        } else if (word.equalsIgnoreCase("diagnosis")) {
                            edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                            onTabButtonClicked(DIAGNOSIS_INDEX);
                        } else if (word.equalsIgnoreCase("test") || word.equalsIgnoreCase("tests")) {
                            edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                            onTabButtonClicked(TESTS_INDEX);
                        } else if (word.equalsIgnoreCase("advice") || word.equalsIgnoreCase("treatment") || word.equalsIgnoreCase("treat")) {
                            edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                            onTabButtonClicked(MEDICATIONS_INDEX);
                        } else if (word.equalsIgnoreCase("vital") || word.equalsIgnoreCase("vitals")) {
                            edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                            onTabButtonClicked(VITALS_INDEX);
                        } else if (result.get(0).toLowerCase().contains("previous")) {
                            edtDiagnosis.append(" " + result.get(0).replace("previous", ""));
                            previousButtonClicked();
                        } else if (result.get(0).toLowerCase().contains("save") || result.get(0).toLowerCase().contains("submit")) {
                            edtDiagnosis.append(" " + result.get(0).replace("save", ""));
                            savePrescription();
                        } else {

                            if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
                                autoCompleteTextView.setText("");
                                autoCompleteTextView.append(result.get(0));

                            } else {
                                edtDiagnosis.append(" " + result.get(0));
                            }
                        }
                        strDiagnosis = edtDiagnosis.getText().toString();
                        break;
                    }

                    if (active == fragment[TESTS_INDEX]) {
                        edtInvestigation = findViewById(R.id.edt_investigations);
                        if (result.get(0).toLowerCase().contains("next")) {
                            edtInvestigation.append(" " + result.get(0).replace("next", ""));
                            nextButtonClicked();

                        } else {
                            speechNavigation(edtInvestigation, word, result);
                        }

                        strTests = edtInvestigation.getText().toString();
                        break;
                    }

                    if (active == fragment[MEDICATIONS_INDEX]) {
                        edtMedications = findViewById(R.id.edt_treatment_advice);
                        if (result.get(0).toLowerCase().contains("next")) {
                            edtMedications.append(" " + result.get(0).replace("next", ""));
                            nextButtonClicked();

                        } else {
                            speechNavigation(edtMedications, word, result);
                        }


                        strMedications = edtMedications.getText().toString();
                        break;
                    }

                    if (active == fragment[VITALS_INDEX]) {
                        edtVitals = findViewById(R.id.edt_vitals);
                        if (result.get(0).toLowerCase().contains("next")) {
                            edtVitals.append(" " + result.get(0).replace("next", ""));
                            nextButtonClicked();

                        } else {
                            speechNavigation(edtVitals, word, result);
                        }


                        strVitals = edtVitals.getText().toString();
                        break;
                    }

                    if (active == fragment[NOTES_INDEX]) {
                        edtClinicalNotes = findViewById(R.id.edt_clinical_notes);
                        if (result.get(0).toLowerCase().contains("next")) {
                            edtClinicalNotes.append(" " + result.get(0).replace("next", ""));
                            nextButtonClicked();

                        } else {
                            speechNavigation(edtClinicalNotes, word, result);
                        }


                        strClinicalNotes = edtClinicalNotes.getText().toString();
                        break;
                    }

                }

                break;
            case 123:

                if (requestCode == 123 && resultCode == RESULT_OK) {
                    //  isImageTaken = true;

                }


                break;
        }
    }


    public void highlightBtn(TextView tv) {
        for (TextView b : btnSelect) {

            if (b == tv) {

                setBtnActive(tv);

            } else {
                setBtnDefault(b);
            }
        }
    }

    private void setBtnActive(TextView tv) {
        tv.setBackgroundResource(R.color.select_button);
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void setBtnDefault(TextView tv) {

        tv.setBackgroundResource(R.color.desk_tab_background);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }


    public void getSpeechInput() {
        Log.i(TAG, "getSpeechInput: ");
      /*  try {
            SessionServicecall sessionServicecall = new SessionServicecall(this);
            sessionServicecall.saveSession(patientDetails.getCRNo(), patientDetails.getPatMobileNo(), patientDetails.getHospCode(), "Speech_To_Text", "", "", "", "");
        } catch (Exception ex) {
        }*/


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
        checkIndicatorStatus();
        //initSimboHelper();
    }


    public void btnSavePrsCription(View view) {


        savePrescription();

    }

    private void savePrescription() {

        if (edtVitals != null) {
            strVitals = edtVitals.getText().toString();
        }
        if (edtChiefCoplaint != null) {
            strChiefComplaints = edtChiefCoplaint.getText().toString();
        }

        if (edtDiagnosis != null) {
            strDiagnosis = edtDiagnosis.getText().toString();
        }

        if (edtInvestigation != null) {
            strTests = edtInvestigation.getText().toString();
        }

        if (edtMedications != null) {
            strMedications = edtMedications.getText().toString();
        }

        if (edtClinicalNotes != null) {
            strClinicalNotes = edtClinicalNotes.getText().toString();
        }
        if (swDiagnosis != null) {
            if (swDiagnosis.isChecked()) {
                swStatus = "final";
            } else {
                swStatus = "provisional";
            }
        }

        Log.i(TAG, "strDiagnosis: " + strDiagnosis);

        Intent intent = new Intent(DeskActivity.this, PrescriptionPreviewActivity.class);
        intent.putExtra("strVitals", strVitals);
        intent.putExtra("strChiefComplaints", strChiefComplaints);
        intent.putExtra("strHopi", "");
        intent.putExtra("strExamination", "");
        intent.putExtra("strDiagnosis", strDiagnosis);
        intent.putExtra("strTests", strTests);
        intent.putExtra("strProcedures", "");
        intent.putExtra("strMedications", strMedications);
        intent.putExtra("strClinicalNotes", strClinicalNotes);
        Log.i("swStatus", "savePrescription: " + swStatus);
        intent.putExtra("swStatus", swStatus);

        intent.putExtra("patientdetails", patientDetails);

        intent.putExtra("snomedcodes", snomedCodes);

        intent.putExtra("snomedcodesdiagnosis", snomeCodesDiagnosis);


        intent.putExtra("revisitDate", followUpDate);
        intent.putExtra("revisitDisplayDate", revisitDisplayDate);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void btnListPage(View view) {
        finish();
    }


    public void btnPrevious(View view) {
        previousButtonClicked();
    }


    public void initIndiactors() {
        tvPercentageFilled = findViewById(R.id.btn_percentage_filled);

        indicators[VITALS_INDEX] = findViewById(R.id.btn_vitals_indicator);
        indicators[COMPLAINTS_INDEX] = findViewById(R.id.btn_chief_complaint_indicator);
//        indicators[1] = findViewById(R.id.btn_hopi_indicator);
//        indicators[2] = findViewById(R.id.btn_examination_indicator);
        indicators[DIAGNOSIS_INDEX] = findViewById(R.id.btn_diagnosis_indicator);
        indicators[TESTS_INDEX] = findViewById(R.id.btn_investigation_indicator);
//        indicators[5] = findViewById(R.id.btn_procedures_indicator);
        indicators[MEDICATIONS_INDEX] = findViewById(R.id.btn_treatment_advice_indicator);

        indicators[NOTES_INDEX] = findViewById(R.id.btn_notes_indicator);


        checkIndicatorStatus();
    }

    public void checkIndicatorStatus() {
        percentageFilled = 0;
        if (edtVitals != null) {
            if (!edtVitals.getText().toString().trim().equals("")) {

                vitalsCount = 15;
            } else {
                vitalsCount = 0;
            }

        }
        if (edtChiefCoplaint != null) {
            if (!edtChiefCoplaint.getText().toString().trim().equals("")) {
                compaintsCount = 15;


            } else {
                compaintsCount = 0;

            }

        }

        if (edtDiagnosis != null) {
            if (!edtDiagnosis.getText().toString().trim().equals("")) {
                diagnosisCount = 15;
            } else {
                diagnosisCount = 0;
            }

        }

        if (edtInvestigation != null) {
            if (!edtInvestigation.getText().toString().trim().equals("")) {
                testsCount = 15;
            } else {
                testsCount = 0;
            }

        }

        if (edtMedications != null) {
            if (!edtMedications.getText().toString().trim().equals("")) {

                treatmentAdvicedCount = 15;
            } else {
                treatmentAdvicedCount = 0;
            }

        }
        if (edtClinicalNotes != null) {
            if (!edtClinicalNotes.getText().toString().trim().equals("")) {

                clinicalNotesCount = 15;
            } else {
                clinicalNotesCount = 0;
            }

        }


        percentageFilled = percentageFilled + vitalsCount + compaintsCount + diagnosisCount + testsCount + treatmentAdvicedCount + clinicalNotesCount;
        int count = percentageFilled / 15;

        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(android.R.color.darker_gray);
        }
        for (int i = 0; i < count; i++) {
            indicators[i].setBackgroundResource(android.R.color.holo_green_dark);
        }


        Log.i("percentage", "checkIndicatorStatus: " + percentageFilled + "%");
        tvPercentageFilled.setText(percentageFilled + 10 + "%");
    }

    public void btnInfo(View view) {

        new AlertDialog.Builder(this)
                .setTitle("For best results:")
                .setMessage("\n" +
                        "\n" +
                        "Use a good microphone in a quiet place\n" +
                        "\n" +
                        "Speak clearly, not necessarily slowly\n" +
                        "\n" +
                        "Prepare what you're going to say\n" +
                        "\n" +
                        "Flowing sentences are easier to transcribe than short disjointed phrases.")

                .setPositiveButton(android.R.string.yes, null)

                .show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            //handle click
            getSpeechInput();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void btnWhatsappCall(View view) {
        ManagingSharedData msd = new ManagingSharedData(this);
        sendFCMPush("eConsultation Doctor Call", msd.getUsername() + " is calling you for eConsultation. Please join the call using the “Join Video Call” link shown in the “Consultation and Status” page of the app.");
        jitsiVideoCall();

    }


    private void jitsiVideoCall() {

        try {
            SessionServicecall sessionServicecall = new SessionServicecall(this);
            sessionServicecall.saveSession(patientDetails.getCRNo(), patientDetails.getPatMobileNo(), patientDetails.getHospCode(), "Video call initiated", "", "", "", "");
        } catch (Exception ex) {
        }
        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
//            serverURL = new URL("https://meet.jit.si");
            serverURL = new URL("https://mconsultancy.uat.dcservices.in");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        ManagingSharedData msd = new ManagingSharedData(this);
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setSubject(patientDetails.getPatName())
             //   .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
//                .setRoom("https://jitsi.cdac.in/" + patientDetails.getRequestID())
                .setRoom("https://mconsultancy.uat.dcservices.in/" + patientDetails.getRequestID())
                .setFeatureFlag("pip.enabled", false)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(this, options);
    }


    public void btnViewDocument(View view) {

//        Intent intent = new Intent(DeskActivity.this, ViewDocumentActivity.class);
        Intent intent = new Intent(DeskActivity.this, ViewDocNewActivity.class);
        intent.putExtra("requestId", patientDetails.getRequestID());
        startActivity(intent);
    }

    public void btnPatientDetails(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.patient_history_details_model);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        final TextView tvWeight = (TextView) dialog.findViewById(R.id.tv_weight);
        final TextView tvHeight = (TextView) dialog.findViewById(R.id.tv_height);
        final TextView tvPatMedications = (TextView) dialog.findViewById(R.id.tv_pat_medications);
        final TextView tvPastDiagonsis = (TextView) dialog.findViewById(R.id.tv_past_diagonosis);
        final TextView tvAllergies = (TextView) dialog.findViewById(R.id.tv_allergies);
        final TextView tvDescription = (TextView) dialog.findViewById(R.id.tv_description);
        final TextView tvCovidScreeningResponse = (TextView) dialog.findViewById(R.id.tv_covid_screening_response);

        ImageView imgWeight = (ImageView) dialog.findViewById(R.id.img_weight);
        ImageView imgHeight = (ImageView) dialog.findViewById(R.id.img_height);
        ImageView imgMedications = (ImageView) dialog.findViewById(R.id.img_medications);
        ImageView imgDiagnosis = (ImageView) dialog.findViewById(R.id.img_diagnosis);
        ImageView imgAllergies = (ImageView) dialog.findViewById(R.id.img_allergies);
        ImageView imgDescription = (ImageView) dialog.findViewById(R.id.img_description);
        ImageView imgCovidScreeningResponse = (ImageView) dialog.findViewById(R.id.img_covid_screening_response);


        if (patientDetails.getScrResponse().length() < 10) {
            patientDetails.setScrResponse(String.format("%-10s", patientDetails.getScrResponse()));
            Log.i(TAG, "btnPatientDetails: " + patientDetails.getScrResponse());
        }
        String fever = String.valueOf(patientDetails.getScrResponse().charAt(0));
        String cough = String.valueOf(patientDetails.getScrResponse().charAt(1));
        String soreThroat = String.valueOf(patientDetails.getScrResponse().charAt(2));
        String breathingDifficulty = String.valueOf(patientDetails.getScrResponse().charAt(3));
        String congestion = String.valueOf(patientDetails.getScrResponse().charAt(4));
        String bodyAche = String.valueOf(patientDetails.getScrResponse().charAt(5));
        String pinkEyes = String.valueOf(patientDetails.getScrResponse().charAt(6));
        String smell = String.valueOf(patientDetails.getScrResponse().charAt(7));
        String hearingImpairment = String.valueOf(patientDetails.getScrResponse().charAt(8));
        String gastroIntestinal = String.valueOf(patientDetails.getScrResponse().charAt(9));

        if (fever.equalsIgnoreCase("N")) {
            fever = "Fever : " + "No, ";
        } else if (fever.equalsIgnoreCase("Y")) {
            fever = "Fever : " + "Yes, ";
        } else {
            fever = "";
        }

        if (cough.equalsIgnoreCase("N")) {
            cough = "Cough : " + "No, ";
        } else if (cough.equalsIgnoreCase("Y")) {
            cough = "Cough : " + "Yes, ";
        } else {
            cough = "";
        }

        if (soreThroat.equalsIgnoreCase("N")) {
            soreThroat = "Sore Throat : " + "No, ";
        } else if (soreThroat.equalsIgnoreCase("Y")) {
            soreThroat = "Sore Throat : " + "Yes, ";
        } else {
            soreThroat = "";
        }

        if (breathingDifficulty.equalsIgnoreCase("N")) {
            breathingDifficulty = "Breathing Difficulty : " + "No, ";
        } else if (breathingDifficulty.equalsIgnoreCase("Y")) {
            breathingDifficulty = "Breathing Difficulty : " + "Yes, ";
        } else {
            breathingDifficulty = "";
        }

//new parameters
        if (congestion.equalsIgnoreCase("N")) {
            congestion = "Chest Congestion or Runny Nose : " + "No";
        } else if (congestion.equalsIgnoreCase("Y")) {
            congestion = "Chest Congestion or Runny Nose : " + "Yes, ";
        } else {
            congestion = "";
        }

        if (bodyAche.equalsIgnoreCase("N")) {
            bodyAche = "Body Ache : " + "No";
        } else if (bodyAche.equalsIgnoreCase("Y")) {
            bodyAche = "Body Ache : " + "Yes, ";
        } else {
            bodyAche = "";
        }

        if (pinkEyes.equalsIgnoreCase("N")) {
            pinkEyes = "Pink Eyes : " + "No";
        } else if (pinkEyes.equalsIgnoreCase("Y")) {
            pinkEyes = "Pink Eyes : " + "Yes, ";
        } else {
            pinkEyes = "";
        }

        if (smell.equalsIgnoreCase("N")) {
            smell = "Loss of Senses of Smell and Taste : " + "No";
        } else if (smell.equalsIgnoreCase("Y")) {
            smell = "Loss of Senses of Smell and Taste : " + "Yes, ";
        } else {
            smell = "";
        }

        if (hearingImpairment.equalsIgnoreCase("N")) {
            hearingImpairment = "Hearing Impairment : " + "No";
        } else if (hearingImpairment.equalsIgnoreCase("Y")) {
            hearingImpairment = "Hearing Impairment : " + "Yes, ";
        } else {
            hearingImpairment = "";
        }

        if (gastroIntestinal.equalsIgnoreCase("N")) {
            gastroIntestinal = "Gastrointestinal Symptoms : " + "No";
        } else if (gastroIntestinal.equalsIgnoreCase("Y")) {
            gastroIntestinal = "Gastrointestinal Symptoms : " + "Yes, ";
        } else {
            gastroIntestinal = "";
        }


//        tvCovidScreeningResponse.setText(fever + cough + soreThroat + breathingDifficulty + foreignTravel);
        tvCovidScreeningResponse.setText(fever + cough + soreThroat + breathingDifficulty + congestion + bodyAche + pinkEyes + smell + hearingImpairment + gastroIntestinal);
        if ((fever + cough + soreThroat + breathingDifficulty + congestion + bodyAche + pinkEyes + smell + hearingImpairment + gastroIntestinal).trim().equalsIgnoreCase("")) {
            imgCovidScreeningResponse.setVisibility(View.GONE);
        } else {
            imgCovidScreeningResponse.setVisibility(View.VISIBLE);
        }

        if ((patientDetails.getScrResponse() + patientDetails.getPatWeight() + patientDetails.getPatHeight() + patientDetails.getPatMedication() + patientDetails.getPatPastDiagnosis() + patientDetails.getPatAllergies() + patientDetails.getRmrks()).trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "No information submitted.", Toast.LENGTH_SHORT).show();
        } else {
            tvWeight.setText("Weight: " + patientDetails.getPatWeight());
            tvHeight.setText("Height: " + patientDetails.getPatHeight());
            tvPatMedications.setText("Medication: " + patientDetails.getPatMedication());
            tvPastDiagonsis.setText("Past Diagnosis: " + patientDetails.getPatPastDiagnosis());
            tvAllergies.setText("Allergies: " + patientDetails.getPatAllergies());
            tvDescription.setText("Problem Description: " + patientDetails.getRmrks());


            Button btnOk = dialog.findViewById(R.id.btn_ok);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            imgWeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvWeight.getText().toString());
                }
            });
            imgHeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvHeight.getText().toString());
                }
            });
            imgMedications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvPatMedications.getText().toString());
                }
            });
            imgDiagnosis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvPastDiagonsis.getText().toString());
                }
            });
            imgAllergies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvAllergies.getText().toString());
                }
            });
            imgDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvDescription.getText().toString());
                }
            });

            imgCovidScreeningResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipboard(tvCovidScreeningResponse.getText().toString());
                }
            });

            if (!patientDetails.getPatWeight().equalsIgnoreCase("")) {
                imgWeight.setVisibility(View.VISIBLE);
                tvWeight.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getPatHeight().equalsIgnoreCase("")) {
                imgHeight.setVisibility(View.VISIBLE);
                tvHeight.setVisibility(View.VISIBLE);
            }

            if (!patientDetails.getPatMedication().equalsIgnoreCase("")) {
                imgMedications.setVisibility(View.VISIBLE);
                tvPatMedications.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getPatPastDiagnosis().equalsIgnoreCase("")) {
                imgDiagnosis.setVisibility(View.VISIBLE);
                tvPastDiagonsis.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getPatAllergies().equalsIgnoreCase("")) {
                imgAllergies.setVisibility(View.VISIBLE);
                tvAllergies.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getRmrks().equalsIgnoreCase("")) {
                imgDescription.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.VISIBLE);
            }

            dialog.show();
        }
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text copied to clipboard.", Toast.LENGTH_SHORT).show();
    }

    public void btnCall(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Call made through this facility would be through telecom service operator. Caller number would be visible to the patient, if you don't want to display your number we recommend to use the Video Call facility provided (you may disable video during the call). However, in case of emergency or low internet bandwidth, this facility can be used to call patients.")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        String mobileNo = patientDetails.getPatMobileNo();
                        if (mobileNo.length() > 10) {
                            mobileNo = mobileNo.substring(2);
                        }
                        startDialActivity(mobileNo);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();


        //startDialActivity(patientDetails.getPatMobileNo().substring(2));

    }

    private void startDialActivity(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    private void sendFCMPush(String notificationTitle, String message) {


//        String Legacy_SERVER_KEY = getResources().getString(R.string.server_legacy_key);
        String title = notificationTitle;
        String token = patientDetails.getPatientToken();
        JSONObject obj = null;


        try {


            obj = new JSONObject();
            JSONObject objData1 = new JSONObject();

            // objData.put("data", msg);
            objData1.put("title", title);
            objData1.put("content", message);
            objData1.put("navigateTo", patientDetails.getRequestID());


            obj.put("data", objData1);
            obj.put("to", token);

            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl + "callPushNotfication/call", obj,
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

    public void fabVideoCall(View view) {
        ManagingSharedData msd = new ManagingSharedData(this);
        sendFCMPush("eConsultation Doctor Call", msd.getUsername() + " is calling you for eConsultation. Please join the call using the “Join Video Call” link shown in the “Consultation and Status” page of the app.");
        jitsiVideoCall();
    }


    public void btnFollowUp(View view) {
//        getHolidayDates();

        if (datesArrayList != null) {
            showDate(datesArrayList.toArray(new Calendar[datesArrayList.size()]));
        } else {
            getHolidayDates();
        }
    }


    private void showDate(Calendar[] disabledDates) {

        Calendar calendar = Calendar.getInstance();

        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                DeskActivity.this,
                Year, // Initial year selection
                Month, // Initial month selection
                Day // Inital day selection
        );
        datePickerDialog.setMinDate(calendar);


        // Setting Min Date to today date
        Calendar min_date_c = Calendar.getInstance();
        datePickerDialog.setMinDate(min_date_c);


        // Setting Max Date to next 2 years
        Calendar max_date_c = Calendar.getInstance();
        max_date_c.set(Calendar.YEAR, Year + 2);
        datePickerDialog.setMaxDate(max_date_c);

        //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
        for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY) {
                Calendar[] disabledDays = new Calendar[1];
                disabledDays[0] = loopdate;
//                Log.i("aa", "showDate: " + disabledDates[0]);
                datePickerDialog.setDisabledDays(disabledDays);
            }
        }

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialogInterface) {
                followUpDate = "";
                revisitDisplayDate = "";
                btnFollowUp.setText("Follow up");

            }
        });

        datePickerDialog.setDisabledDays(disabledDates);
        datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

        String dateString = String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year);
        Log.i("date", "onDateSet: " + dateString);


        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(dateString);

            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat displayFormat = new SimpleDateFormat("EEE, dd MMM");
            String strDate = newFormat.format(date);
            Log.i("final string", "onDateSet: " + strDate);

            followUpDate = strDate;
            revisitDisplayDate = displayFormat.format(date);
            btnFollowUp.setText(revisitDisplayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Calendar[] getHolidayDates() {

        Toast.makeText(this, "Please wait while loading calendar.", Toast.LENGTH_SHORT).show();
        ManagingSharedData msd = new ManagingSharedData(this);
//        List<Calendar> dates = new ArrayList<>();
        datesArrayList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getHolidayList + msd.getHospCode(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (status.equalsIgnoreCase("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String holidayDate = c.getString("VARHOLIDAYDATE");
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

//                            holidayDate = holidayDate.replace("2019", "2020");

                            Date date = format1.parse(holidayDate);
                            System.out.println(format2.format(date));
                            Calendar calendar = dateToCalendar(date);
//                            dates.add(calendar);
                            datesArrayList.add(calendar);
                        }

//                        showDate(dates.toArray(new Calendar[dates.size()]));
                        Log.i("dateList", "onResponse: " + datesArrayList);
                    }
                } catch (Exception ex) {
                    Log.i("exception", "exception: " + ex);
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);

        return datesArrayList.toArray(new Calendar[datesArrayList.size()]);

    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    private void speechNavigation(EditText editText, String word, ArrayList<String> result) {
        if (word.equalsIgnoreCase("complaint") || word.equalsIgnoreCase("complaints") || word.equalsIgnoreCase("cheif") || word.equalsIgnoreCase("chief") || word.equalsIgnoreCase("cheif complaint")) {
            editText.append(" " + result.get(0).replace(word, ""));

            onTabButtonClicked(COMPLAINTS_INDEX);
        } else if (word.equalsIgnoreCase("diagnosis")) {
            editText.append(" " + result.get(0).replace(word, ""));
            onTabButtonClicked(DIAGNOSIS_INDEX);
        } else if (word.equalsIgnoreCase("test") || word.equalsIgnoreCase("tests")) {
            editText.append(" " + result.get(0).replace(word, ""));
            onTabButtonClicked(TESTS_INDEX);
        } else if (word.equalsIgnoreCase("advice") || word.equalsIgnoreCase("treatment") || word.contains("treat")) {
            editText.append(" " + result.get(0).replace(word, ""));
            onTabButtonClicked(MEDICATIONS_INDEX);
        } else if (word.equalsIgnoreCase("vital") || word.equalsIgnoreCase("vitals")) {
            editText.append(" " + result.get(0).replace(word, ""));
            onTabButtonClicked(VITALS_INDEX);
        } else if (result.get(0).toLowerCase().contains("previous")) {
            editText.append(" " + result.get(0).replace("previous", ""));
            previousButtonClicked();
        } else if (result.get(0).toLowerCase().contains("save") || result.get(0).toLowerCase().contains("submit")) {
            editText.append(" " + result.get(0).replace("save", ""));
            savePrescription();
        } else {
            editText.append(" " + result.get(0));
        }
    }








    private void parseData(String data)
    {
        ArrayList<String> result = new ArrayList();
        result.add(data);
        String word = result.get(0).toLowerCase();
      //  word.replaceAll("\\\\n", "\n");

        if (active == fragment[COMPLAINTS_INDEX]) {
            edtChiefCoplaint = findViewById(R.id.edt_chief_complaint);

            if (result.get(0).toLowerCase().contains("next")) {
                edtChiefCoplaint.append(" " + result.get(0).replace("next", ""));
                nextButtonClicked();

            } else {
                speechNavigation(edtChiefCoplaint, word, result);
            }


            strChiefComplaints = edtChiefCoplaint.getText().toString();

        }


        if (active == fragment[DIAGNOSIS_INDEX]) {
            edtDiagnosis = findViewById(R.id.edt_diagnosis);
            autoCompleteTextView = findViewById(R.id.auto_complete_final_diagnosis);

            if (result.get(0).toLowerCase().contains("next")) {
                edtDiagnosis.append(" " + result.get(0).replace("next", ""));
                nextButtonClicked();

            } else if (word.equalsIgnoreCase("complaint") || word.equalsIgnoreCase("complaints") || word.equalsIgnoreCase("cheif") || word.equalsIgnoreCase("chief") || word.equalsIgnoreCase("cheif complaint")) {
                edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                onTabButtonClicked(COMPLAINTS_INDEX);
            } else if (word.equalsIgnoreCase("diagnosis")) {
                edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                onTabButtonClicked(DIAGNOSIS_INDEX);
            } else if (word.equalsIgnoreCase("test") || word.equalsIgnoreCase("tests")) {
                edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                onTabButtonClicked(TESTS_INDEX);
            } else if (word.equalsIgnoreCase("advice") || word.equalsIgnoreCase("treatment") || word.equalsIgnoreCase("treat")) {
                edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                onTabButtonClicked(MEDICATIONS_INDEX);
            } else if (word.equalsIgnoreCase("vital") || word.equalsIgnoreCase("vitals")) {
                edtDiagnosis.append(" " + result.get(0).replace(word, ""));
                onTabButtonClicked(VITALS_INDEX);
            } else if (result.get(0).toLowerCase().contains("previous")) {
                edtDiagnosis.append(" " + result.get(0).replace("previous", ""));
                previousButtonClicked();
            } else if (result.get(0).toLowerCase().contains("save") || result.get(0).toLowerCase().contains("submit")) {
                edtDiagnosis.append(" " + result.get(0).replace("save", ""));
                savePrescription();
            } else {

                if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.append(result.get(0));

                } else {
                    edtDiagnosis.append(" " + result.get(0));
                }
            }
            strDiagnosis = edtDiagnosis.getText().toString();

        }

        if (active == fragment[TESTS_INDEX]) {
            edtInvestigation = findViewById(R.id.edt_investigations);
            if (result.get(0).toLowerCase().contains("next")) {
                edtInvestigation.append(" " + result.get(0).replace("next", ""));
                nextButtonClicked();

            } else {
                speechNavigation(edtInvestigation, word, result);
            }

            strTests = edtInvestigation.getText().toString();

        }

        if (active == fragment[MEDICATIONS_INDEX]) {
            edtMedications = findViewById(R.id.edt_treatment_advice);
            if (result.get(0).toLowerCase().contains("next")) {
                edtMedications.append(" " + result.get(0).replace("next", ""));
                nextButtonClicked();

            } else {
                speechNavigation(edtMedications, word, result);
            }


            strMedications = edtMedications.getText().toString();

        }

        if (active == fragment[VITALS_INDEX]) {
            edtVitals = findViewById(R.id.edt_vitals);
            if (result.get(0).toLowerCase().contains("next")) {
                edtVitals.append(" " + result.get(0).replace("next", ""));
                nextButtonClicked();

            } else {
                speechNavigation(edtVitals, word, result);
            }


            strVitals = edtVitals.getText().toString();

        }

        if (active == fragment[NOTES_INDEX]) {
            edtClinicalNotes = findViewById(R.id.edt_clinical_notes);
            if (result.get(0).toLowerCase().contains("next")) {
                edtClinicalNotes.append(" " + result.get(0).replace("next", ""));
                nextButtonClicked();

            } else {
                speechNavigation(edtClinicalNotes, word, result);
            }


            strClinicalNotes = edtClinicalNotes.getText().toString();

        }

    }


}