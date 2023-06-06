package com.cdac.uphmis.opdLite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.DoctorDesk.DoctorRequestListActivity;
import com.cdac.uphmis.covid19.ViewDocNewActivity;
import com.cdac.uphmis.opdLite.model.CompleteHistoryJaonArray;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.opdLite.model.EHRJsonDetails;
import com.cdac.uphmis.opdLite.model.EMRJsonDetails;
import com.cdac.uphmis.opdLite.model.FollowUp;
import com.cdac.uphmis.opdLite.model.PiccleArray;
import com.cdac.uphmis.opdLite.model.PlannedVisit;
import com.cdac.uphmis.opdLite.model.StrCompleteHistory;
import com.cdac.uphmis.opdLite.model.StrSystematicExamniation;
import com.cdac.uphmis.opdLite.model.Strpiccle;
import com.cdac.uphmis.opdLite.model.SystematicExamniationArray;
import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.adaper.DrugJsonAdapter;
import com.cdac.uphmis.opdLite.model.ComplaintsDetails;
import com.cdac.uphmis.opdLite.model.ComplaintsJsonArray;
import com.cdac.uphmis.opdLite.model.Diagnosis;
import com.cdac.uphmis.opdLite.model.DiagnosisDetails;
import com.cdac.uphmis.opdLite.model.DiagnosisICDDetails;
import com.cdac.uphmis.opdLite.model.DiagnosisJsonArray;
import com.cdac.uphmis.opdLite.model.DrugJsonArray;
import com.cdac.uphmis.opdLite.model.InvestigationDetails;
import com.cdac.uphmis.opdLite.model.InvestigationJsonArray;
import com.cdac.uphmis.opdLite.model.ProcedureDetails;
import com.cdac.uphmis.opdLite.model.ProceduresJsonArray;
import com.cdac.uphmis.opdLite.util.ParseSpeechInput;
import com.cdac.uphmis.opdLite.util.SpeechUtility;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.SessionServicecall;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static android.content.ContentValues.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;


public class DeskHomeActivity extends AppCompatActivity {

    private static String FONT_COLOR = "<font color='#00695C'>";
    boolean doubleBackToExitPressedOnce = false;

    private ArrayList<ComplaintsJsonArray> complaintsJsonArrayList;
    private ArrayList<InvestigationJsonArray> investigationsJsonArrayList;
    private ArrayList<ProceduresJsonArray> proceduresJsonArrayList;
    private ArrayList<DiagnosisJsonArray> diagnosisJsonArrayList;
    private ArrayList<DrugJsonArray> drugJsonArrayArrayList;

    private Dialog addComplaintsDialog, addDiagnosisDialog, addnvestigationsDialog, addRxDialog, addProcedureDialog;


    private TextView tvFinalComplaints;
    private ActivityResultLauncher<Intent> historyResultLauncher, examinationResultLauncher, drugsResultLauncher, vitalsResultLauncher;

    private TextView tvFinalHistory, tvFinalExaminations, tvFinalRx, tvFinalInvestigation, tvFinalProcedure, tvFinalDiagnosis, tvFinalVitals;


    private String hopi = "", past = "", personal = "", family = "", treatment = "", surgical = "";

    private String cvs = "", rs = "", cns = "", pa = "", general = "", muscular = "", local = "";

    private String weight="", height="", bmi="", bmiStatus="", temperature="", pulse="0", low="", high="", fast="", pp="", hba="", hgb="", bloodGroup="", description="";
    private boolean disability, smoking, anemic;



    private AutoCompleteTextView invetigationAutoCompleteTextView, procedureAutoCompleteTextView, complaintsAutoCompleteTextView, autoCompleteICDTextView, autoCompleteDiseaseTextView, diagnosisSnomedAutoCompleteTextView;
    private String rx = "";


    private boolean isSmonedChecked = true;


    private ActivityResultLauncher<Intent> rxSpeechResultLauncher, proceduresSpeechResultLauncher, investigationsSpeechResultLauncher, complaintsSpeechResultLauncher, diagnosisSpeechResultLauncher, homeSpeechResultLauncher;


    private Button btnRxDone, btnProceduresAdd, btnProceduresClose, btnInvestigationsAdd, btnInvestigationsClose, btnComplaintsAdd, btnComplaintsClose, btnDiagnosisAdd, btnDiagnosisClose;
    private EditText edtRx, edtProceduresDescription, edtInvestigationsDescription, edtComplaintsDescription, tvComplaintsNumber, edtDiagnosisDescription;

    private String[] proceduresSide, investigationsSide, complaintsSide, diagnosisSide, diagnosisType;
    private TextView[] arProceduresTextViews, arInvestigationsTextViews, arComplaintsTextViews, arDiagnosisSideTextViews, arDiagnosisTypeTextViews;


    private RadioButton radioButtonComplaintsDays, radioButtonComplaintsWeeks, radioButtonComplaintsMonths;

    private Switch switchDiagnosis;


    TextView tvComplaints, tvHistory, tvDiagnoisis, tvExaminations, tvInvestigaitons, tvProcedures, tvDrugs, tvRx, tvVitals;


    private ManagingSharedData msd;
    private String PatCompleteGeneralDtlData = "";
    private String fatherName = "";
    private String designation = "";
    private String customerUnit = "";
    private String umidNo = "";
    private String catName = "";
   


    DoctorReqListDetails patientdetails;

    GeometricProgressView progressView;
    private Button btnSave;


    List<String> reasonOfVisit, InvTestCode, InvTestCodeToPrint, diagnosisArray, proceduresArray, drugsArray;
    private String vitalsChart = "";



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk_home);
        
        Toolbar toolbar =  findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drugJsonArrayArrayList=new ArrayList<>();
        msd = new ManagingSharedData(this);
        initHomeViews();

        patientdetails = (DoctorReqListDetails) getIntent().getSerializableExtra("patientdetails");
        getPatientDetail();

        initComplaintsDialog();
        initDiagnosisDialog();
        initInvestigationsDialog();
        initRxDialog();
        initProceduresDialog();


        historyOnActivityResult();
        examinationsOnActivityResult();
        drugsOnActivityResult();
        vitalsOnActivityResult();
        tvFinalComplaints = findViewById(R.id.tv_final_complaints);
        tvFinalExaminations = findViewById(R.id.tv_final_examinations);
        tvFinalRx = findViewById(R.id.tv_final_rx);
        tvFinalInvestigation = findViewById(R.id.tv_final_investigations);
        tvFinalProcedure = findViewById(R.id.tv_final_procedures);
        tvFinalDiagnosis = findViewById(R.id.tv_final_diagnosis);


        rxSpeechListner();
        proceduresSpeechListner();
        investgationsSpeechListner();
        complaintsSpeechListner();
        diagnosisSpeechListner();

        homeSpeechListner();
        btnSave.setOnClickListener(v ->
        {

new AlertDialog.Builder(this)
        .setTitle("Confirm")
        .setMessage("Do you want to save Prescription?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                savePrescription();
            }
        })
        .setNegativeButton("Cancel",null).show();



           // final String requestBody = (String) createJsonEHR(patientdetails);
           // Log.i("requestBody", "sendPrescriptionData: " + requestBody);
          //  Log.i("complaintsJsonArrayList", "onCreate: "+complaintsJsonArrayList);
          //  Log.i("createJsonEHR", "onCreate: " + createJsonEHR(patientdetails).toString());
//            Log.i("createJsonEMR", "onCreate: " + createJsonEMR(patientdetails).toString());
//            Log.i("complaintsJsonArrayList", "onCreate: " + complaintsJsonArrayList);
//            Log.i("diagnosisJsonArrayList", "onCreate: " + diagnosisJsonArrayList);
//            Log.i("drugsJsonArrayList", "onCreate: " + drugsArray);
        });
    }
private void savePrescription()
{
    reasonOfVisit = new ArrayList<>();


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        reasonOfVisit = complaintsJsonArrayList.stream().map(e -> e.toString()).collect(Collectors.toList());
        InvTestCodeToPrint = investigationsJsonArrayList.stream().map(e -> e.toString()).collect(Collectors.toList());
        InvTestCode = investigationsJsonArrayList.stream().filter(e -> e.isBoolExternal() == false).map(e -> e.toString()).collect(Collectors.toList());
        // diagnosisArray = diagnosisJsonArrayList.stream().map(e -> e.toString()).collect(Collectors.toList());
        proceduresArray = proceduresJsonArrayList.stream().map(e -> e.toString()).collect(Collectors.toList());
        drugsArray = drugJsonArrayArrayList.stream().map(e -> e.toString()).collect(Collectors.toList());
        diagnosisArray = diagnosisJsonArrayList.stream().map(e -> e.toString()).collect(Collectors.toList());

        Log.i("stream api", "onCreate: " + complaintsJsonArrayList.stream().map(e -> e.toString()).collect(Collectors.toList()));
    } else {
        for (ComplaintsJsonArray s : complaintsJsonArrayList) {
            reasonOfVisit.add(s.toString());
        }
    }

    Log.i("TAG", "onCreate: "+drugsArray);
    Log.i("TAG", "onCreate: "+drugJsonArrayArrayList);

    saveVitalsData(createVitealsJson(PatCompleteGeneralDtlData));
    saveEhrJsonData(patientdetails);
    saveEmrJsonData();
}
    private void initHomeViews() {
        progressView = findViewById(R.id.progress_view);
        tvComplaints = findViewById(R.id.tv_complaints);
        tvHistory = findViewById(R.id.tv_history);
        tvDiagnoisis = findViewById(R.id.tv_diagnosis);
        tvExaminations = findViewById(R.id.tv_examinations);
        tvInvestigaitons = findViewById(R.id.tv_investigations);
        tvProcedures = findViewById(R.id.tv_procedures);
        tvDrugs = findViewById(R.id.tv_drugs);
        tvRx = findViewById(R.id.tv_rx);
        tvVitals = findViewById(R.id.tv_vitals);

        btnSave = findViewById(R.id.btn_save);

        progressView.setColor(ContextCompat.getColor(this, R.color.white));
    }


    private void rxSpeechListner() {
        rxSpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word = result1.get(0).toLowerCase();
                        ParseSpeechInput.parseRxData(word, btnRxDone, edtRx);


                    }
                });
    }

    private void proceduresSpeechListner() {
        proceduresSpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word = result1.get(0).toLowerCase();
                        ParseSpeechInput.parseProceduresData(word, btnProceduresAdd, btnProceduresClose, procedureAutoCompleteTextView, edtProceduresDescription, arProceduresTextViews);


                    }
                });
    }

    private void investgationsSpeechListner() {
        investigationsSpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word = result1.get(0).toLowerCase();
                        ParseSpeechInput.parseInvestigationsData(word, btnInvestigationsAdd, btnInvestigationsClose, invetigationAutoCompleteTextView, edtInvestigationsDescription, arInvestigationsTextViews);


                    }
                });
    }

    private void complaintsSpeechListner() {
        complaintsSpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word = result1.get(0).toLowerCase();
                        ParseSpeechInput.parseComplaintsData(word, btnComplaintsAdd, btnComplaintsClose, complaintsAutoCompleteTextView, edtComplaintsDescription, arComplaintsTextViews, tvComplaintsNumber, radioButtonComplaintsDays, radioButtonComplaintsWeeks, radioButtonComplaintsMonths);


                    }
                });
    }

    private void diagnosisSpeechListner() {
        diagnosisSpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word = result1.get(0).toLowerCase();
                        ParseSpeechInput.parseDiagnosisData(word, btnDiagnosisAdd, btnDiagnosisClose, diagnosisSnomedAutoCompleteTextView, autoCompleteICDTextView, autoCompleteDiseaseTextView, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews, switchDiagnosis, isSmonedChecked);


                    }
                });
    }

    private void homeSpeechListner() {
        homeSpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word = result1.get(0).toLowerCase();
                        ParseSpeechInput.parseHomeData(word, tvComplaints, tvHistory, tvDiagnoisis, tvExaminations, tvInvestigaitons, tvProcedures, tvDrugs, tvRx, tvVitals);


                    }
                });
    }

    private void initProceduresDialog() {
        addProcedureDialog = new Dialog(this);
        addProcedureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addProcedureDialog.setContentView(R.layout.add_procedures_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addProcedureDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        addProcedureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addProcedureDialog.getWindow().setAttributes(lp);
        addProcedureDialog.setCancelable(true);


        procedureAutoCompleteTextView = addProcedureDialog.findViewById(R.id.autocomplete_procedures);
        getProcedures(procedureAutoCompleteTextView);

        proceduresJsonArrayList = new ArrayList<>();
    }

    private void initRxDialog() {
        addRxDialog = new Dialog(this);
        addRxDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addRxDialog.setContentView(R.layout.add_rx_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addRxDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        addRxDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addRxDialog.getWindow().setAttributes(lp);
        addRxDialog.setCancelable(true);

    }


    private void initComplaintsDialog() {
        addComplaintsDialog = new Dialog(this);
        addComplaintsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addComplaintsDialog.setContentView(R.layout.add_complaints_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addComplaintsDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        addComplaintsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addComplaintsDialog.getWindow().setAttributes(lp);
        addComplaintsDialog.setCancelable(true);

        complaintsJsonArrayList = new ArrayList<>();
    }


    private void initDiagnosisDialog() {
        addDiagnosisDialog = new Dialog(this);
        addDiagnosisDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addDiagnosisDialog.setContentView(R.layout.add_diagnosis_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addDiagnosisDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        addDiagnosisDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addDiagnosisDialog.getWindow().setAttributes(lp);
        addDiagnosisDialog.setCancelable(true);

        diagnosisJsonArrayList = new ArrayList<>();
    }


    private void initInvestigationsDialog() {
        addnvestigationsDialog = new Dialog(this);
        addnvestigationsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addnvestigationsDialog.setContentView(R.layout.add_investigations_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addnvestigationsDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        addnvestigationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addnvestigationsDialog.getWindow().setAttributes(lp);
        addnvestigationsDialog.setCancelable(true);

        invetigationAutoCompleteTextView = addnvestigationsDialog.findViewById(R.id.autocomplete_investigations);
        invetigationAutoCompleteTextView.setThreshold(3);
        getInvestigations(invetigationAutoCompleteTextView);

        investigationsJsonArrayList = new ArrayList<>();
    }


    public void btnComplaints(View view) {
        showComplaintsDialog();
    }

    private void showComplaintsDialog() {

        FlexboxLayout llChips = addComplaintsDialog.findViewById(R.id.ll_chips);
        tvComplaintsNumber = addComplaintsDialog.findViewById(R.id.tv_number);
        edtComplaintsDescription = addComplaintsDialog.findViewById(R.id.edt_description);


        arComplaintsTextViews = new TextView[5];

        arComplaintsTextViews[0] = addComplaintsDialog.findViewById(R.id.tv_side);
        arComplaintsTextViews[1] = addComplaintsDialog.findViewById(R.id.tv_nr);
        arComplaintsTextViews[2] = addComplaintsDialog.findViewById(R.id.tv_left);
        arComplaintsTextViews[3] = addComplaintsDialog.findViewById(R.id.tv_right);
        arComplaintsTextViews[4] = addComplaintsDialog.findViewById(R.id.tv_bilateral);

        complaintsSide = new String[]{setSelected(0, arComplaintsTextViews)};
        arComplaintsTextViews[0].setOnClickListener(v -> complaintsSide[0] = setSelected(0, arComplaintsTextViews));
        arComplaintsTextViews[1].setOnClickListener(v -> complaintsSide[0] = setSelected(1, arComplaintsTextViews));
        arComplaintsTextViews[2].setOnClickListener(v -> complaintsSide[0] = setSelected(2, arComplaintsTextViews));
        arComplaintsTextViews[3].setOnClickListener(v -> complaintsSide[0] = setSelected(3, arComplaintsTextViews));
        arComplaintsTextViews[4].setOnClickListener(v -> complaintsSide[0] = setSelected(4, arComplaintsTextViews));


        btnComplaintsAdd = addComplaintsDialog.findViewById(R.id.btn_add);
        btnComplaintsClose = addComplaintsDialog.findViewById(R.id.btn_close);

        complaintsAutoCompleteTextView = addComplaintsDialog.findViewById(R.id.autocomplete_complaints);

        complaintsAutoCompleteTextView.requestFocus();

        complaintsAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.i("s.tostring", "onTextChanged: " + s.toString().length());
                if (s.toString().length() >= 3) {
                    getComplaints(s.toString(), complaintsAutoCompleteTextView);
                }

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final ComplaintsDetails[] selectedItem = new ComplaintsDetails[1];
        complaintsAutoCompleteTextView.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            selectedItem[0] = (ComplaintsDetails) item;


            Log.i("text", "onItemClick: " + selectedItem[0].getTerm());
        });
        btnComplaintsClose.setOnClickListener(v -> {
            addComplaintsDialog.dismiss();

        });

        radioButtonComplaintsDays = addComplaintsDialog.findViewById(R.id.radio_days);
        radioButtonComplaintsWeeks = addComplaintsDialog.findViewById(R.id.radio_weeks);
        radioButtonComplaintsMonths = addComplaintsDialog.findViewById(R.id.radio_months);


        btnComplaintsAdd.setOnClickListener(v -> {
            String selectedTimePeriod = "1";
            if (radioButtonComplaintsDays.isChecked()) {
                selectedTimePeriod = "1";
            } else if (radioButtonComplaintsWeeks.isChecked()) {
                selectedTimePeriod = "2";
            } else if (radioButtonComplaintsMonths.isChecked()) {
                selectedTimePeriod = "3";
            }
            String finalSelectedTimePeriod = selectedTimePeriod;


            String number = tvComplaintsNumber.getText().toString();
            String remarks = edtComplaintsDescription.getText().toString();
            if (isDuplicateComplaint(complaintsAutoCompleteTextView.getText().toString().trim())) {
                complaintsAutoCompleteTextView.getText().clear();
                selectedItem[0] = null;
                complaintsSide[0] = setSelected(0, arComplaintsTextViews);
                tvComplaintsNumber.getText().clear();
                radioButtonComplaintsDays.setChecked(true);
                edtComplaintsDescription.getText().clear();

                complaintsAutoCompleteTextView.requestFocus();
                new AlertDialog.Builder(this)
                        .setMessage(complaintsAutoCompleteTextView.getText().toString().trim() + " already added.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            if (selectedItem[0] != null) {

                addComplaints(selectedItem[0], llChips, complaintsSide[0], number, finalSelectedTimePeriod, remarks);

//refresh views

            } else {

//external drug
                ComplaintsDetails complaintsDetails = new ComplaintsDetails();
                complaintsDetails.setConceptId("0");
                complaintsDetails.setTerm(complaintsAutoCompleteTextView.getText().toString().trim());
                addComplaints(complaintsDetails, llChips, complaintsSide[0], number, finalSelectedTimePeriod, remarks);

//refresh views

                // Toast.makeText(DeskHomeActivity.this, "Please select complaints from dropdown.", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "showComplaintsDialog: dlkhnaksd");
            }
            complaintsAutoCompleteTextView.getText().clear();
            selectedItem[0] = null;
            complaintsSide[0] = setSelected(0, arComplaintsTextViews);
            tvComplaintsNumber.getText().clear();
            radioButtonComplaintsDays.setChecked(true);
            edtComplaintsDescription.getText().clear();

            complaintsAutoCompleteTextView.requestFocus();
        });


        FloatingActionButton investigationsSpeechInput = addComplaintsDialog.findViewById(R.id.fab_complaints);

        investigationsSpeechInput.setOnClickListener(v -> {
            SpeechUtility.getSpeechInput(this, complaintsSpeechResultLauncher);
        });

        addComplaintsDialog.show();

    }


    private boolean isDuplicateComplaint(String term) {
        for (ComplaintsJsonArray complaintsJsonArray : complaintsJsonArrayList) {
            if (complaintsJsonArray.getComplaintsDetails().getTerm().trim().equalsIgnoreCase(term)) {
                Toast.makeText(this, complaintsJsonArray.getComplaintsDetails().getTerm().trim() + " Already Added", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicateDiagnosis(String term) {
        for (DiagnosisJsonArray diagnosisJsonArray : diagnosisJsonArrayList) {
            Diagnosis diagnosis = diagnosisJsonArray.getDiagnosisDetails();
            if (diagnosis instanceof DiagnosisDetails) {
                if (((DiagnosisDetails) diagnosis).getTerm().equalsIgnoreCase(term)) {
                    Toast.makeText(this, ((DiagnosisDetails) diagnosis).getTerm().trim() + " Already Added", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else {
                if (((DiagnosisICDDetails) diagnosis).getDiseaseName().equalsIgnoreCase(term)) {
                    Toast.makeText(this, ((DiagnosisICDDetails) diagnosis).getDiseaseName().trim() + " Already Added", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

        }
        return false;
    }

    private boolean isDuplicateInvestigation(String term) {
        for (InvestigationJsonArray investigationJsonArray : investigationsJsonArrayList) {
            if (investigationJsonArray.getInvestigationDetails().getTESTNAME().trim().equalsIgnoreCase(term)) {
                Toast.makeText(this, investigationJsonArray.getInvestigationDetails().getTESTNAME().trim() + " Already Added", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicateProcedure(String term) {
        for (ProceduresJsonArray proceduresJsonArray : proceduresJsonArrayList) {
            if (proceduresJsonArray.getProcedureDetails().getProcedureName().trim().equalsIgnoreCase(term)) {
                Toast.makeText(this, proceduresJsonArray.getProcedureDetails().getProcedureName().trim() + " Already Added", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void updateFinalComplaints() {
        String text = "";
        for (ComplaintsJsonArray complaintsJsonArray : complaintsJsonArrayList) {
            text = text + complaintsJsonArray.getComplaintsDetails().getTerm() + ", ";

        }
        tvFinalComplaints.setText(text);
    }


    private void addComplaints(ComplaintsDetails complaintsDetails, FlexboxLayout chipGroup, String side, String number, String selectedTimePeriod, String remarks) {

        Chip chip = SpeechUtility.createChip(DeskHomeActivity.this);
        chip.setText(complaintsDetails.getTerm());
        chip.setCloseIconVisible(true);


        chipGroup.addView(chip, chipGroup.getChildCount());
        chip.setOnCloseIconClickListener(view -> {
                    chipGroup.removeView(chip);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        complaintsJsonArrayList.removeIf(p -> p.getComplaintsDetails().getTerm().equals(chip.getText().toString()));

                        Log.i("remove", "addComplaints: " + complaintsJsonArrayList);
                        updateFinalComplaints();
                    }
                }
        );

        complaintsJsonArrayList.add(new ComplaintsJsonArray(complaintsDetails, side, number, selectedTimePeriod, remarks));
        updateFinalComplaints();
        Log.i("add", "addComplaints: " + complaintsJsonArrayList);
    }


    private String setSelected(int selected, TextView[] arTextViews) {
        for (int i = 0; i < arTextViews.length; i++) {
            if (i == selected) {
                arTextViews[selected].setBackground(ContextCompat.getDrawable(this,
                        R.drawable.selected_date_bacground));

            } else {
                arTextViews[i].setBackground(ContextCompat.getDrawable(this, R.drawable.date_background));
            }
        }
        return String.valueOf(selected);
    }


    private String setSelectedInvestigations(int selected, TextView[] arTextViews) {
        for (int i = 0; i < arTextViews.length; i++) {
            if (i == selected) {
                arTextViews[selected].setBackground(ContextCompat.getDrawable(this,
                        R.drawable.selected_date_bacground));

            } else {
                arTextViews[i].setBackground(ContextCompat.getDrawable(this, R.drawable.date_background));
            }
        }
        return arTextViews[selected].getText().toString();
    }


    private String setSelectedProcedures(int selected, TextView[] arTextViews) {
        for (int i = 0; i < arTextViews.length; i++) {
            if (i == selected) {
                arTextViews[selected].setBackground(ContextCompat.getDrawable(this,
                        R.drawable.selected_date_bacground));

            } else {
                arTextViews[i].setBackground(ContextCompat.getDrawable(this, R.drawable.date_background));
            }
        }
        return selected + "#" + arTextViews[selected].getText().toString();
    }


    private void getComplaints(String s, AutoCompleteTextView autoCompleteComplaints) {
        ArrayList<ComplaintsDetails> complaintsDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.railtel_uat_ip + "csnoserv/api/search/search?term=" + s + "&state=active&semantictag=finding&acceptability=synonyms&returnlimit=10&refsetid=null", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("TAG", "onResponse: " + response);


                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ComplaintsDetails complaintsDetails = null;

                        complaintsDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), ComplaintsDetails.class);

                        complaintsDetailsArrayList.add(complaintsDetails);


                    }

                    ArrayAdapter adapter = new ArrayAdapter<ComplaintsDetails>(DeskHomeActivity.this, android.R.layout.select_dialog_singlechoice, complaintsDetailsArrayList);


                    //Set the adapter
                    autoCompleteComplaints.setAdapter(adapter);
                    autoCompleteComplaints.setThreshold(3);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> Log.i("TAG", "onErrorResponse: " + error));
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }


    public void btnHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("hopi", hopi);
        intent.putExtra("personal", personal);
        intent.putExtra("past", past);
        intent.putExtra("family", family);
        intent.putExtra("treatment", treatment);
        intent.putExtra("surgical", surgical);
        historyResultLauncher.launch(intent);


    }


    private void historyOnActivityResult() {
        tvFinalHistory = findViewById(R.id.tv_final_history);
        historyResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        hopi = result.getData().getStringExtra("hopi");
                        personal = result.getData().getStringExtra("personal");
                        past = result.getData().getStringExtra("past");
                        family = result.getData().getStringExtra("family");
                        treatment = result.getData().getStringExtra("treatment");
                        surgical = result.getData().getStringExtra("surgical");

                        String finalHistory = "";
                        if (!hopi.isEmpty())
                            finalHistory += FONT_COLOR + "History of Present Illness: </font>" + hopi + "<br><br>";

                        if (!past.isEmpty())
                            finalHistory += FONT_COLOR + "Past History: </font>" + past + "<br><br>";

                        if (!personal.isEmpty())
                            finalHistory += FONT_COLOR + "Personal History: </font>" + personal + "<br><br>";

                        if (!family.isEmpty())
                            finalHistory += FONT_COLOR + "Family History: </font>" + family + "<br><br>";

                        if (!treatment.isEmpty())
                            finalHistory += FONT_COLOR + "Treatment History: </font>" + treatment + "<br><br>";

                        if (!surgical.isEmpty())
                            finalHistory += FONT_COLOR + "Surgical History: </font>" + surgical + "<br><br>";


                        tvFinalHistory.setText(Html.fromHtml(finalHistory));
                    }
                });
    }

    public void btnExaminations(View view) {
        Intent intent = new Intent(this, ExaminationsActivity.class);
        intent.putExtra("cvs", cvs);
        intent.putExtra("rs", rs);
        intent.putExtra("cns", cns);
        intent.putExtra("pa", pa);
        intent.putExtra("general", general);
        intent.putExtra("muscular", muscular);
        intent.putExtra("local", local);
        examinationResultLauncher.launch(intent);
    }

    private void examinationsOnActivityResult() {


        tvFinalExaminations = findViewById(R.id.tv_final_examinations);
        examinationResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        cvs = result.getData().getStringExtra("cvs");
                        rs = result.getData().getStringExtra("rs");
                        cns = result.getData().getStringExtra("cns");
                        pa = result.getData().getStringExtra("pa");
                        general = result.getData().getStringExtra("general");
                        muscular = result.getData().getStringExtra("muscular");
                        local = result.getData().getStringExtra("local");

                        String finalExamination = "";
                        if (!cvs.isEmpty())
                            finalExamination += FONT_COLOR + "CVS </font>" + cvs + "<br><br>";

                        if (!rs.isEmpty())
                            finalExamination += FONT_COLOR + "RS: </font>" + rs + "<br><br>";

                        if (!cns.isEmpty())
                            finalExamination += FONT_COLOR + "CNS: </font>" + cns + "<br><br>";

                        if (!pa.isEmpty())
                            finalExamination += FONT_COLOR + "PA: </font>" + pa + "<br><br>";

                        if (!general.isEmpty())
                            finalExamination += FONT_COLOR + "General Examination: </font>" + general + "<br><br>";

                        if (!muscular.isEmpty())
                            finalExamination += FONT_COLOR + "Muscular Examination: </font>" + muscular + "<br><br>";

                        if (!local.isEmpty())
                            finalExamination += FONT_COLOR + "Local Examination: </font>" + local + "<br><br>";


                        tvFinalExaminations.setText(Html.fromHtml(finalExamination));
                    }
                });
    }

    public void btnDrugs(View view) {

        Intent intent = new Intent(this, DrugsActivity.class);
        intent.putExtra("drugJsonArrayArrayList", drugJsonArrayArrayList);
        intent.putExtra("patientDetails", patientdetails);
        drugsResultLauncher.launch(intent);
    }

    private void drugsOnActivityResult() {
        drugsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        drugJsonArrayArrayList = (ArrayList<DrugJsonArray>) result.getData().getSerializableExtra("drugJsonArrayArrayList");
                        setUpRecyclerView();
                    }

                });
    }

    private void setUpRecyclerView() {
        RecyclerView rvDrugs = findViewById(R.id.rv_drugs);
        rvDrugs.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        DrugJsonAdapter drugJsonAdapter = null;
        drugJsonAdapter = new DrugJsonAdapter(this, drugJsonArrayArrayList,patientdetails.getEpisodeCode(),patientdetails.getEpisodeVisitNo());
        rvDrugs.setLayoutManager(layoutManager);
        rvDrugs.setAdapter(drugJsonAdapter);


    }

    public void btnRx(View view) {
        showRxDialog();
    }

    private void showRxDialog() {


        btnRxDone = addRxDialog.findViewById(R.id.btn_done);
        edtRx = addRxDialog.findViewById(R.id.edt_rx);
        edtRx.requestFocus();
        btnRxDone.setOnClickListener(v -> {
            rx = edtRx.getText().toString();
            tvFinalRx.setText(rx);
            addRxDialog.dismiss();
        });


        FloatingActionButton rxSpeechInput = addRxDialog.findViewById(R.id.fab_rx);

        rxSpeechInput.setOnClickListener(v -> {
            SpeechUtility.getSpeechInput(this, rxSpeechResultLauncher);
        });


        addRxDialog.show();
    }


    private void showInvestigationsDialog() {

        FlexboxLayout llChips = addnvestigationsDialog.findViewById(R.id.ll_chips);
        edtInvestigationsDescription = addnvestigationsDialog.findViewById(R.id.edt_description);

        arInvestigationsTextViews = new TextView[5];

        arInvestigationsTextViews[0] = addnvestigationsDialog.findViewById(R.id.tv_side);
        arInvestigationsTextViews[1] = addnvestigationsDialog.findViewById(R.id.tv_nr);
        arInvestigationsTextViews[2] = addnvestigationsDialog.findViewById(R.id.tv_left);
        arInvestigationsTextViews[3] = addnvestigationsDialog.findViewById(R.id.tv_right);
        arInvestigationsTextViews[4] = addnvestigationsDialog.findViewById(R.id.tv_bilateral);

        investigationsSide = new String[]{setSelectedInvestigations(0, arInvestigationsTextViews)};
        arInvestigationsTextViews[0].setOnClickListener(v -> investigationsSide[0] = setSelectedInvestigations(0, arInvestigationsTextViews));
        arInvestigationsTextViews[1].setOnClickListener(v -> investigationsSide[0] = setSelectedInvestigations(1, arInvestigationsTextViews));
        arInvestigationsTextViews[2].setOnClickListener(v -> investigationsSide[0] = setSelectedInvestigations(2, arInvestigationsTextViews));
        arInvestigationsTextViews[3].setOnClickListener(v -> investigationsSide[0] = setSelectedInvestigations(3, arInvestigationsTextViews));
        arInvestigationsTextViews[4].setOnClickListener(v -> investigationsSide[0] = setSelectedInvestigations(4, arInvestigationsTextViews));


        btnInvestigationsAdd = addnvestigationsDialog.findViewById(R.id.btn_add);
        btnInvestigationsClose = addnvestigationsDialog.findViewById(R.id.btn_close);


        final InvestigationDetails[] selectedItem = new InvestigationDetails[1];
        invetigationAutoCompleteTextView.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            selectedItem[0] = (InvestigationDetails) item;
            Log.i("text", "onItemClick: " + selectedItem[0].getTESTNAME());
        });
        btnInvestigationsClose.setOnClickListener(v -> {
            addnvestigationsDialog.dismiss();

        });


        btnInvestigationsAdd.setOnClickListener(v -> {
            if (invetigationAutoCompleteTextView.getText().toString().trim().isEmpty()) {

                invetigationAutoCompleteTextView.getText().clear();
                selectedItem[0] = null;
                investigationsSide[0] = setSelectedInvestigations(0, arInvestigationsTextViews);
                edtInvestigationsDescription.getText().clear();

                invetigationAutoCompleteTextView.requestFocus();

                new AlertDialog.Builder(this)
                        .setMessage("Please select Test")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            String remarks = edtInvestigationsDescription.getText().toString();
            if (isDuplicateInvestigation(invetigationAutoCompleteTextView.getText().toString().trim())) {
                invetigationAutoCompleteTextView.getText().clear();
                selectedItem[0] = null;
                investigationsSide[0] = setSelectedInvestigations(0, arInvestigationsTextViews);
                edtInvestigationsDescription.getText().clear();
                invetigationAutoCompleteTextView.requestFocus();

                new AlertDialog.Builder(this)
                        .setMessage(invetigationAutoCompleteTextView.getText().toString().trim() + " already added.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            if (selectedItem[0] != null) {

                addInvestigations(selectedItem[0], llChips, investigationsSide[0], remarks, false);
//refresh views

            } else {

//external drug
                InvestigationDetails investigationDetails = new InvestigationDetails();
                investigationDetails.setLabname("0");
                investigationDetails.setTestDetail("0^0^0^0^^" + invetigationAutoCompleteTextView.getText().toString());
                investigationDetails.setTESTNAME(invetigationAutoCompleteTextView.getText().toString());
                addInvestigations(investigationDetails, llChips, investigationsSide[0], remarks, true);


            }
            invetigationAutoCompleteTextView.getText().clear();
            selectedItem[0] = null;
            investigationsSide[0] = setSelectedInvestigations(0, arInvestigationsTextViews);
            edtInvestigationsDescription.getText().clear();
            invetigationAutoCompleteTextView.requestFocus();

        });

        FloatingActionButton investigationsSpeechInput = addnvestigationsDialog.findViewById(R.id.fab_investigations);

        investigationsSpeechInput.setOnClickListener(v -> {
            SpeechUtility.getSpeechInput(this, investigationsSpeechResultLauncher);
        });


        addnvestigationsDialog.show();

    }


    private void getInvestigations(AutoCompleteTextView autoCompleteTextView) {
        //   showLoading();
        ArrayList<InvestigationDetails> investigationDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/investigationList?hosp_code=33201", response1 -> {
            Log.i("TAG", "onResponse: " + response1);
            try {
                //   hideLoading();
                //   progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(response1);

                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("investigation_details");


                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        InvestigationDetails drugsDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), InvestigationDetails.class);
                        investigationDetailsArrayList.add(drugsDetails);


                    }

                    autoCompleteTextView.setAdapter(new ArrayAdapter<InvestigationDetails>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, investigationDetailsArrayList));

                } else {
                    //     hideLoading();
                    Toast.makeText(DeskHomeActivity.this, "Cannot load investigations", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception ex) {
                //  hideLoading();
                ex.printStackTrace();
            }

        }, error ->
        {
            //hideLoading();
            Log.i("TAG", "onErrorResponse: " + error);
        });


        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    public void btnInvestigations(View view) {
        showInvestigationsDialog();
    }

    private void addInvestigations(InvestigationDetails investigationDetails, FlexboxLayout chipGroup, String side, String remarks, boolean isExternal) {

        Chip chip = SpeechUtility.createChip(DeskHomeActivity.this);


        chip.setText(investigationDetails.getTESTNAME());
        chip.setCloseIconVisible(true);


        chipGroup.addView(chip, chipGroup.getChildCount());
        chip.setOnCloseIconClickListener(view -> {
                    chipGroup.removeView(chip);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        investigationsJsonArrayList.removeIf(p -> p.getInvestigationDetails().getTESTNAME().equals(chip.getText().toString()));

                        Log.i("remove", "addComplaints: " + investigationsJsonArrayList);
                        updateFinalInvestigations();
                    }
                }
        );

        investigationsJsonArrayList.add(new InvestigationJsonArray(investigationDetails, side, remarks, isExternal,patientdetails.getEpisodeCode(),patientdetails.getEpisodeVisitNo()));
        updateFinalInvestigations();
        Log.i("add", "addInvestigations: " + investigationsJsonArrayList);
    }

    private void updateFinalInvestigations() {
        String text = "";
        for (InvestigationJsonArray investigationJsonArray : investigationsJsonArrayList) {
            text = text + investigationJsonArray.getInvestigationDetails().getTESTNAME() + ", ";

        }
        tvFinalInvestigation.setText(text);
    }

    public void btnProcedures(View view) {
        showProceduresDialog();
    }

    private void addProcedures(ProcedureDetails procedureDetails, FlexboxLayout chipGroup, String side, String remarks, boolean isExternal) {

        Chip chip = SpeechUtility.createChip(DeskHomeActivity.this);


        chip.setText(procedureDetails.getProcedureName());
        chip.setCloseIconVisible(true);


        chipGroup.addView(chip, chipGroup.getChildCount());
        chip.setOnCloseIconClickListener(view -> {
                    chipGroup.removeView(chip);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        proceduresJsonArrayList.removeIf(p -> p.getProcedureDetails().getProcedureName().equals(chip.getText().toString()));

                        Log.i("remove", "addProcedures: " + proceduresJsonArrayList);
                        updateFinalProcedures();
                    }
                }
        );

        proceduresJsonArrayList.add(new ProceduresJsonArray(procedureDetails, side, remarks, isExternal));
        updateFinalProcedures();
        Log.i("add", "addProcedures: " + proceduresJsonArrayList);
    }


    private void updateFinalProcedures() {
        String text = "";
        for (ProceduresJsonArray proceduresJsonArray : proceduresJsonArrayList) {
            text = text + proceduresJsonArray.getProcedureDetails().getProcedureName() + ", ";

        }
        tvFinalProcedure.setText(text);
    }


    private void getProcedures(AutoCompleteTextView autoCompleteTextView) {
        ArrayList<ProcedureDetails> investigationDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/procedureListing?hosp_code=33201", response1 -> {
            Log.i("TAG", "onResponse: " + response1);
            try {
                JSONObject jsonObject = new JSONObject(response1);

                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("procedure_listing_details");


                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProcedureDetails procedureDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), ProcedureDetails.class);
                        investigationDetailsArrayList.add(procedureDetails);


                    }

                    autoCompleteTextView.setAdapter(new ArrayAdapter<ProcedureDetails>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, investigationDetailsArrayList));

                } else {
                    //     hideLoading();
                    Toast.makeText(DeskHomeActivity.this, "Cannot load procedures", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }, error ->
        {
            Log.i("TAG", "onErrorResponse: " + error);
        });


        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        MySingleton.getInstance(this).addToRequestQueue(request);

    }


    private void showProceduresDialog() {

        FlexboxLayout llChips = addProcedureDialog.findViewById(R.id.ll_chips);
        edtProceduresDescription = addProcedureDialog.findViewById(R.id.edt_description);


        arProceduresTextViews = new TextView[5];

        arProceduresTextViews[0] = addProcedureDialog.findViewById(R.id.tv_side);
        arProceduresTextViews[1] = addProcedureDialog.findViewById(R.id.tv_nr);
        arProceduresTextViews[2] = addProcedureDialog.findViewById(R.id.tv_left);
        arProceduresTextViews[3] = addProcedureDialog.findViewById(R.id.tv_right);
        arProceduresTextViews[4] = addProcedureDialog.findViewById(R.id.tv_bilateral);

        proceduresSide = new String[]{setSelectedProcedures(0, arProceduresTextViews)};
        arProceduresTextViews[0].setOnClickListener(v -> proceduresSide[0] = setSelectedProcedures(0, arProceduresTextViews));
        arProceduresTextViews[1].setOnClickListener(v -> proceduresSide[0] = setSelectedProcedures(1, arProceduresTextViews));
        arProceduresTextViews[2].setOnClickListener(v -> proceduresSide[0] = setSelectedProcedures(2, arProceduresTextViews));
        arProceduresTextViews[3].setOnClickListener(v -> proceduresSide[0] = setSelectedProcedures(3, arProceduresTextViews));
        arProceduresTextViews[4].setOnClickListener(v -> proceduresSide[0] = setSelectedProcedures(4, arProceduresTextViews));


        btnProceduresAdd = addProcedureDialog.findViewById(R.id.btn_add);
        btnProceduresClose = addProcedureDialog.findViewById(R.id.btn_close);


        final ProcedureDetails[] selectedItem = new ProcedureDetails[1];
        procedureAutoCompleteTextView.requestFocus();
        procedureAutoCompleteTextView.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            selectedItem[0] = (ProcedureDetails) item;

            procedureAutoCompleteTextView.clearFocus();

           // SpeechUtility.getSpeechInput(this, proceduresSpeechResultLauncher);
            Log.i("text", "onItemClick: " + selectedItem[0].getProcedureName());
        });
        btnProceduresClose.setOnClickListener(v -> {
            addProcedureDialog.dismiss();

        });


        btnProceduresAdd.setOnClickListener(v -> {
            Log.i("TAG", "showProceduresDialog: " + procedureAutoCompleteTextView.getText().toString());

            if (procedureAutoCompleteTextView.getText().toString().trim().isEmpty()) {
                procedureAutoCompleteTextView.getText().clear();
                selectedItem[0] = null;
                proceduresSide[0] = setSelectedProcedures(0, arProceduresTextViews);
                edtProceduresDescription.getText().clear();
                procedureAutoCompleteTextView.requestFocus();

                new AlertDialog.Builder(this)
                        .setMessage("Please select Procedure")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }

            if (isDuplicateProcedure(procedureAutoCompleteTextView.getText().toString().trim())) {
                procedureAutoCompleteTextView.getText().clear();
                selectedItem[0] = null;
                proceduresSide[0] = setSelectedProcedures(0, arProceduresTextViews);
                edtProceduresDescription.getText().clear();
                procedureAutoCompleteTextView.requestFocus();

                new AlertDialog.Builder(this)
                        .setMessage(procedureAutoCompleteTextView.getText().toString().trim() + " already added.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }

            String remarks = edtProceduresDescription.getText().toString();
            if (selectedItem[0] != null) {
                addProcedures(selectedItem[0], llChips, proceduresSide[0], remarks, false);


            } else {

//external procedure
                ProcedureDetails procedureDetails = new ProcedureDetails();
                procedureDetails.setProcedureDetail("0^0^0^0^^" + procedureAutoCompleteTextView.getText().toString());
                procedureDetails.setProcedureName(procedureAutoCompleteTextView.getText().toString());

                addProcedures(procedureDetails, llChips, proceduresSide[0], remarks, true);


            }
//refresh views
            procedureAutoCompleteTextView.getText().clear();
            selectedItem[0] = null;
            proceduresSide[0] = setSelectedProcedures(0, arProceduresTextViews);
            edtProceduresDescription.getText().clear();
            Log.i("TAG", "showProceduressDialog: dlkhnaksd");

            procedureAutoCompleteTextView.requestFocus();
        });


        FloatingActionButton proceduresSpeechInput = addProcedureDialog.findViewById(R.id.fab_procedures);

        proceduresSpeechInput.setOnClickListener(v -> {
            SpeechUtility.getSpeechInput(this, proceduresSpeechResultLauncher);
        });


        addProcedureDialog.show();

    }

    public void btnDiagnosis(View view) {
        showDiagnosisDialog();
    }

    private void showDiagnosisDialog() {
        FlexboxLayout llChips = addDiagnosisDialog.findViewById(R.id.ll_chips);
        edtDiagnosisDescription = addDiagnosisDialog.findViewById(R.id.edt_description);


        diagnosisSnomedAutoCompleteTextView = addDiagnosisDialog.findViewById(R.id.autocomplete_diagnosis_snomed);
        autoCompleteICDTextView = addDiagnosisDialog.findViewById(R.id.autocomplete_diagnosis_icd_code);
        autoCompleteDiseaseTextView = addDiagnosisDialog.findViewById(R.id.autocomplete_diagnosis_disease);
        switchDiagnosis = addDiagnosisDialog.findViewById(R.id.switch_diagnosis);

        getDiagnosisFromIcd(autoCompleteICDTextView, autoCompleteDiseaseTextView);
        switchDiagnosis.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //showsnomed

                isSmonedChecked = true;
                autoCompleteICDTextView.setVisibility(View.GONE);
                autoCompleteDiseaseTextView.setVisibility(View.GONE);
                diagnosisSnomedAutoCompleteTextView.setVisibility(View.VISIBLE);
                diagnosisSnomedAutoCompleteTextView.requestFocus();


            } else {
                //show icd

                isSmonedChecked = false;
                autoCompleteICDTextView.setVisibility(View.VISIBLE);
                autoCompleteDiseaseTextView.setVisibility(View.VISIBLE);
                autoCompleteDiseaseTextView.requestFocus();
                diagnosisSnomedAutoCompleteTextView.setVisibility(View.GONE);
            }
            diagnosisSnomedAutoCompleteTextView.getText().clear();
            autoCompleteICDTextView.getText().clear();
            autoCompleteDiseaseTextView.getText().clear();
        });


        arDiagnosisSideTextViews = new TextView[5];
        arDiagnosisTypeTextViews = new TextView[3];

        arDiagnosisSideTextViews[0] = addDiagnosisDialog.findViewById(R.id.tv_side);
        arDiagnosisSideTextViews[1] = addDiagnosisDialog.findViewById(R.id.tv_nr);
        arDiagnosisSideTextViews[2] = addDiagnosisDialog.findViewById(R.id.tv_left);
        arDiagnosisSideTextViews[3] = addDiagnosisDialog.findViewById(R.id.tv_right);
        arDiagnosisSideTextViews[4] = addDiagnosisDialog.findViewById(R.id.tv_bilateral);


        arDiagnosisTypeTextViews[0] = addDiagnosisDialog.findViewById(R.id.tv_provisional);
        arDiagnosisTypeTextViews[1] = addDiagnosisDialog.findViewById(R.id.tv_differential);
        arDiagnosisTypeTextViews[2] = addDiagnosisDialog.findViewById(R.id.tv_final);


        diagnosisSide = new String[]{setSelected(0, arDiagnosisSideTextViews)};
        diagnosisType = new String[]{setSelectedDiagnosisType(0, arDiagnosisTypeTextViews)};
        arDiagnosisSideTextViews[0].setOnClickListener(v -> diagnosisSide[0] = setSelected(0, arDiagnosisSideTextViews));
        arDiagnosisSideTextViews[1].setOnClickListener(v -> diagnosisSide[0] = setSelected(1, arDiagnosisSideTextViews));
        arDiagnosisSideTextViews[2].setOnClickListener(v -> diagnosisSide[0] = setSelected(2, arDiagnosisSideTextViews));
        arDiagnosisSideTextViews[3].setOnClickListener(v -> diagnosisSide[0] = setSelected(3, arDiagnosisSideTextViews));
        arDiagnosisSideTextViews[4].setOnClickListener(v -> diagnosisSide[0] = setSelected(4, arDiagnosisSideTextViews));


        arDiagnosisTypeTextViews[0].setOnClickListener(v -> diagnosisType[0] = setSelectedDiagnosisType(0, arDiagnosisTypeTextViews));
        arDiagnosisTypeTextViews[1].setOnClickListener(v -> diagnosisType[0] = setSelectedDiagnosisType(1, arDiagnosisTypeTextViews));
        arDiagnosisTypeTextViews[2].setOnClickListener(v -> diagnosisType[0] = setSelectedDiagnosisType(2, arDiagnosisTypeTextViews));


        btnDiagnosisAdd = addDiagnosisDialog.findViewById(R.id.btn_add);
        btnDiagnosisClose = addDiagnosisDialog.findViewById(R.id.btn_close);


        diagnosisSnomedAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.i("s.tostring", "onTextChanged: " + s.toString().length());
                if (s.toString().length() >= 3) {
                    getDiagnosisFromSnomed(s.toString(), diagnosisSnomedAutoCompleteTextView);
                }

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final Diagnosis[] selectedItem = new Diagnosis[1];


        diagnosisSnomedAutoCompleteTextView.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            selectedItem[0] = (DiagnosisDetails) item;
            // Log.i("text", "onItemClick: " + selectedItem[0].getTerm());
        });
        btnDiagnosisClose.setOnClickListener(v -> {
            addDiagnosisDialog.dismiss();
        });

        //final DiagnosisICDDetails[] selectedItemICd = new DiagnosisICDDetails[1];
        autoCompleteICDTextView.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            selectedItem[0] = (DiagnosisICDDetails) item;
            autoCompleteDiseaseTextView.setText(((DiagnosisICDDetails) selectedItem[0]).getDiseaseName());
            Log.i("text", "onItemClick: " + ((DiagnosisICDDetails) selectedItem[0]).getDiseaseName());
        });

        autoCompleteDiseaseTextView.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            selectedItem[0] = (DiagnosisICDDetails) item;
            autoCompleteICDTextView.setText(((DiagnosisICDDetails) selectedItem[0]).getDiseaseCode());
            Log.i("text", "onItemClick: " + ((DiagnosisICDDetails) selectedItem[0]).getDiseaseName());
        });

        btnDiagnosisAdd.setOnClickListener(v -> {
            Log.i("TAG", "showDiagnosisDialog: " + diagnosisSnomedAutoCompleteTextView.getText().toString());
            String remarks = edtDiagnosisDescription.getText().toString();
            if (isDuplicateDiagnosis(diagnosisSnomedAutoCompleteTextView.getText().toString().trim())) {
                refreshDiagnosis(selectedItem, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews);
                new AlertDialog.Builder(this)
                        .setMessage(diagnosisSnomedAutoCompleteTextView.getText().toString().trim() + " already added.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            if (isDuplicateDiagnosis(autoCompleteICDTextView.getText().toString().trim())) {
                refreshDiagnosis(selectedItem, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews);
                new AlertDialog.Builder(this)
                        .setMessage(autoCompleteICDTextView.getText().toString().trim() + " already added.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            if (isDuplicateDiagnosis(autoCompleteDiseaseTextView.getText().toString().trim())) {
                refreshDiagnosis(selectedItem, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews);
                new AlertDialog.Builder(this)
                        .setMessage(autoCompleteDiseaseTextView.getText().toString().trim() + " already added.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            Log.i("TAG", "showDiagnosisDialog: " + diagnosisSnomedAutoCompleteTextView.getText().toString());
            if (isSmonedChecked && diagnosisSnomedAutoCompleteTextView.getText().toString().isEmpty()) {
                refreshDiagnosis(selectedItem, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews);
                new AlertDialog.Builder(this)
                        .setMessage("Please enter diagnosis.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }
            if (!isSmonedChecked && autoCompleteDiseaseTextView.getText().toString().isEmpty()) {
                refreshDiagnosis(selectedItem, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews);
                new AlertDialog.Builder(this)
                        .setMessage("Please enter disease.")
                        .setNegativeButton("Ok", null)
                        .show();
                return;
            }


            if (selectedItem[0] != null) {

                addDiagnosis(selectedItem[0], llChips, diagnosisSide[0], diagnosisType[0], remarks, false);

//refresh views

            } else {

//external diagnosis


                if (isSmonedChecked) {
                    DiagnosisDetails diagnosisDetails = new DiagnosisDetails();
                    diagnosisDetails.setConceptId("0");
                    diagnosisDetails.setTerm(diagnosisSnomedAutoCompleteTextView.getText().toString().trim());
                    addDiagnosis(diagnosisDetails, llChips, diagnosisSide[0], diagnosisType[0], remarks, true);
                } else {
                    if (!autoCompleteDiseaseTextView.getText().toString().trim().isEmpty()) {
                        DiagnosisICDDetails diagnosisICDDetails = new DiagnosisICDDetails();
                        diagnosisICDDetails.setDiseaseCode("0");
                        diagnosisICDDetails.setDiseaseName(autoCompleteDiseaseTextView.getText().toString().trim());
                        addDiagnosis(diagnosisICDDetails, llChips, diagnosisSide[0], diagnosisType[0], remarks, true);
                    }
                }
//refresh views


                Log.i("TAG", "showDiagnosisDialog: dlkhnaksd");
            }

            refreshDiagnosis(selectedItem, edtDiagnosisDescription, arDiagnosisSideTextViews, arDiagnosisTypeTextViews);
        });


        FloatingActionButton diagnosisSpeechInput = addDiagnosisDialog.findViewById(R.id.fab_diagnosis);

        diagnosisSpeechInput.setOnClickListener(v -> {
            SpeechUtility.getSpeechInput(this, diagnosisSpeechResultLauncher);
        });


        addDiagnosisDialog.show();
    }

    private String setSelectedDiagnosisType(int selected, TextView[] arDiagnosisTypeTextViews) {
        for (int i = 0; i < arDiagnosisTypeTextViews.length; i++) {
            if (i == selected) {
                arDiagnosisTypeTextViews[selected].setBackground(ContextCompat.getDrawable(this,
                        R.drawable.selected_date_bacground));

            } else {
                arDiagnosisTypeTextViews[i].setBackground(ContextCompat.getDrawable(this, R.drawable.date_background));
            }
        }
        if (selected == 1) {
            return "12";
        }
        if (selected == 2) {
            return "14";
        } else {
            return "11";
        }

    }


    private void refreshDiagnosis(Diagnosis[] selectedItem, EditText edtDescription, TextView[] arSideTextViews, TextView[] arTypeTextViews) {
        diagnosisSnomedAutoCompleteTextView.getText().clear();
        autoCompleteICDTextView.getText().clear();
        autoCompleteDiseaseTextView.getText().clear();
        selectedItem[0] = null;
        diagnosisSide[0] = setSelected(0, arSideTextViews);
        diagnosisType[0] = setSelected(0, arTypeTextViews);
        edtDescription.getText().clear();
        if (isSmonedChecked) {
            diagnosisSnomedAutoCompleteTextView.requestFocus();
        }
    }

    private void addDiagnosis(Diagnosis diagnosis, FlexboxLayout chipGroup, String side, String type, String remarks, boolean isExternal) {

        Chip chip = SpeechUtility.createChip(DeskHomeActivity.this);
        if (diagnosis instanceof DiagnosisDetails) {

            chip.setText(((DiagnosisDetails) diagnosis).getTerm());
        } else if (diagnosis instanceof DiagnosisICDDetails) {
            chip.setText(((DiagnosisICDDetails) diagnosis).getDiseaseName());
        }

        chip.setCloseIconVisible(true);


        chipGroup.addView(chip, chipGroup.getChildCount());
        chip.setOnCloseIconClickListener(view -> {
                    chipGroup.removeView(chip);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        diagnosisJsonArrayList.removeIf(p -> {
                            if (p.getDiagnosisDetails() instanceof DiagnosisDetails) {
                                return ((DiagnosisDetails) p.getDiagnosisDetails()).getTerm().equals(chip.getText().toString());
                            } else {
                                return ((DiagnosisICDDetails) p.getDiagnosisDetails()).getDiseaseName().equals(chip.getText().toString());
                            }
                        });

                        Log.i("remove", "addComplaints: " + diagnosisJsonArrayList);
                        updateFinalDiagnosis();
                    }
                }
        );

        diagnosisJsonArrayList.add(new DiagnosisJsonArray(diagnosis, side, type, remarks, isExternal));
        updateFinalDiagnosis();
        Log.i("add", "addDiagnosis: " + diagnosisJsonArrayList);
    }

    private void getDiagnosisFromSnomed(String s, AutoCompleteTextView autoCompleteComplaints) {
        ArrayList<DiagnosisDetails> diagnosisDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.railtel_uat_ip + "csnoserv/api/search/search?term=" + s + "&state=active&semantictag=all&acceptability=synonyms&returnlimit=10&refsetid=null", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("TAG", "onResponse: " + response);


                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        DiagnosisDetails diagnosisDetails = null;

                        diagnosisDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), DiagnosisDetails.class);

                        diagnosisDetailsArrayList.add(diagnosisDetails);


                    }

                    ArrayAdapter adapter = new ArrayAdapter<DiagnosisDetails>(DeskHomeActivity.this, android.R.layout.select_dialog_singlechoice, diagnosisDetailsArrayList);


                    //Set the adapter
                    autoCompleteComplaints.setAdapter(adapter);
                    autoCompleteComplaints.setThreshold(3);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> Log.i("TAG", "onErrorResponse: " + error));
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }


    private void getDiagnosisFromIcd(AutoCompleteTextView autoCompleteIcdTextView, AutoCompleteTextView autoCompleteDiseaseTextView) {
        Log.i("TAG", "getDiagnosisFromIcd: ");
        ArrayList<DiagnosisICDDetails> diagnosisDiseaseDetailsArrayList = new ArrayList<>();
        ArrayList<DiagnosisICDDetails> diagnosisCodeDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/procInvtestDtl?modeval=2&hospCode=&seatId=&deptcode=&roomNo=", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("getDiagnosisFromIcd", "onResponse: " + response);
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String diseaseCode = jsonObject1.optString("DISEASE_CODE");
                            String diseaseName = jsonObject1.optString("DISEASE_NAME");

                            DiagnosisICDDetails diagnosisICDDetails = new DiagnosisICDDetails(diseaseCode, diseaseName, false);
                            diagnosisDiseaseDetailsArrayList.add(diagnosisICDDetails);

                            DiagnosisICDDetails diagnosisICDDetails1 = new DiagnosisICDDetails(diseaseCode, diseaseName, true);
                            diagnosisCodeDetailsArrayList.add(diagnosisICDDetails1);


                        }

                        autoCompleteIcdTextView.setAdapter(new ArrayAdapter<DiagnosisICDDetails>(DeskHomeActivity.this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diagnosisCodeDetailsArrayList));
                        autoCompleteDiseaseTextView.setAdapter(new ArrayAdapter<DiagnosisICDDetails>(DeskHomeActivity.this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diagnosisDiseaseDetailsArrayList));

                    } else {
                        Toast.makeText(DeskHomeActivity.this, "Cannot load Diagnosis", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, error -> {
            Log.i("TAG", "getDiagnosisFromIcd: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void updateFinalDiagnosis() {
        String text = "";
        for (DiagnosisJsonArray diagnosisJsonArray : diagnosisJsonArrayList) {
            if (diagnosisJsonArray.getDiagnosisDetails() instanceof DiagnosisDetails) {
                text = text + ((DiagnosisDetails) diagnosisJsonArray.getDiagnosisDetails()).getTerm() + ", ";
            } else {
                text = text + ((DiagnosisICDDetails) diagnosisJsonArray.getDiagnosisDetails()).getDiseaseName() + ", ";
            }
        }
        tvFinalDiagnosis.setText(text);
    }

    public void btnVitals(View view) {
        Intent intent = new Intent(this, VitalsActivity.class);
        intent.putExtra("weight", weight);
        intent.putExtra("height", height);
        intent.putExtra("bmi", bmi);
        intent.putExtra("bmiStatus", bmiStatus);
        intent.putExtra("temperature", temperature);
        intent.putExtra("pulse", pulse);
        intent.putExtra("low", low);
        intent.putExtra("high", high);
        intent.putExtra("fast", fast);
        intent.putExtra("pp", pp);
        intent.putExtra("hba", hba);
        intent.putExtra("hgb", hgb);
        intent.putExtra("bloodGroup", bloodGroup);
        intent.putExtra("description", description);

        intent.putExtra("disability", disability);
        intent.putExtra("smoking", smoking);
        intent.putExtra("anemic", anemic);
        vitalsResultLauncher.launch(intent);
    }


    private void vitalsOnActivityResult() {
        tvFinalVitals = findViewById(R.id.tv_final_vitals);
        vitalsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        weight = result.getData().getStringExtra("weight");
                        height = result.getData().getStringExtra("height");
                        bmi = result.getData().getStringExtra("bmi");
                        bmiStatus = result.getData().getStringExtra("bmiStatus");
                        temperature = result.getData().getStringExtra("temperature");
                        pulse = result.getData().getStringExtra("pulse");
                        low = result.getData().getStringExtra("low");
                        high = result.getData().getStringExtra("high");
                        fast = result.getData().getStringExtra("fast");
                        pp = result.getData().getStringExtra("pp");
                        hba = result.getData().getStringExtra("hba");
                        hgb = result.getData().getStringExtra("hgb");
                        bloodGroup = result.getData().getStringExtra("bloodGroup");
                        description = result.getData().getStringExtra("description");


                        disability = result.getData().getBooleanExtra("disability", false);
                        smoking = result.getData().getBooleanExtra("smoking", false);
                        anemic = result.getData().getBooleanExtra("anemic", false);


                        String finalVitals = "";

                        String fontColor = "<font color='#00695C'>";

                        String strWeight = "";
                        if (!weight.isEmpty()) {
                            finalVitals += fontColor + "Weight: </font>" + weight + " (kgs)<br><br>";
                            strWeight = "Weight: " + weight + ", ";
                        }
                        String strHeight = "";
                        if (!height.isEmpty()) {
                            finalVitals += fontColor + "Height: </font>" + height + " (cms)<br><br>";
                            strHeight = "Height : " + height + "cms" + ", ";
                        }

                        String strBmi = "";
                        if (!bmi.isEmpty() && !bmiStatus.isEmpty()) {
                            finalVitals += fontColor + "BMI: </font>" + bmi + " (" + bmiStatus + ")" + "<br><br>";
                            strBmi = "BMI : " + bmi + "cms" + ", ";
                        }

                        String strTemp = "";
                        if (!temperature.isEmpty()) {
                            finalVitals += fontColor + "Temperature: </font>" + temperature + " F<br><br>";
                            strTemp = "Temperature : " + temperature + "F" + ", ";

                        }

                        String strPulse = "";
                        if (!pulse.isEmpty()) {
                            finalVitals += fontColor + "pulse: </font>" + pulse + "<br><br>";
                            strPulse = "Pulse Rate : " + pulse + "be/m" + ", ";
                        }

                        String strBp = "";
                        if (!low.isEmpty() && !high.isEmpty()) {
                            finalVitals += fontColor + "BP: </font>" + low + " / " + high + " (mmHg)<br><br>";
                            strBp = "BP :" + low + "/" + high + "mm/HG" + ", ";
                        }

                        String strFast = "";
                        if (!fast.isEmpty()) {
                            finalVitals += fontColor + "Fast: </font>" + fast + "<br><br>";
                            strFast = "B.S. Fast : " + fast + "mg/dL" + ", ";
                        }
                        String strpp = "";
                        if (!pp.isEmpty()) {
                            finalVitals += fontColor + "PP: </font>" + pp + "<br><br>";
                            strpp = "PP : " + pp + "mg/dL" + ", ";
                        }
                        String strHba = "";
                        if (!hba.isEmpty()) {
                            finalVitals += fontColor + "HBA: </font>" + hba + "<br><br>";
                            strHba = "HBA1C : " + hba + " %" + ", ";
                        }

                        String strhgb = "";
                        if (!hgb.isEmpty()) {
                            finalVitals += fontColor + "HGB: </font>" + hgb + "<br><br>";
                            strhgb = "Hemoglobin : " + hgb + "gm/dL" + ", ";
                        }

                        String strDisability = "";
                        if (disability) {
                            finalVitals += fontColor + "Disability: </font>Yes" + "<br><br>";
                            strDisability = "Disability : Yes" + ", ";
                        }
                        String strSmoking = "";
                        if (smoking) {
                            finalVitals += fontColor + "Smoking: </font>Yes" + "<br><br>";
                            strSmoking = "Smoking : Yes" + ", ";
                        }

                        String strAnemic = "";
                        if (anemic) {
                            finalVitals += fontColor + "Anemic: </font>Yes" + "<br><br>";
                            strAnemic = "Anemic : Yes" + ", ";
                        }

                        String strBloodGroup = "";
                        if (!bloodGroup.isEmpty()) {
                            finalVitals += fontColor + "Blood Group: </font>" + bloodGroup + "<br><br>";
                            strBloodGroup = "Blood Group : " + bloodGroup + ", ";
                        }


                        String strDescription = "";
                        if (!description.isEmpty()) {
                            finalVitals += fontColor + "Description: </font>" + description + "<br><br>";
                            strDescription = "Interpretation Remarks : " + description;
                        }

                        vitalsChart = strWeight + strHeight + strBp + strTemp + strhgb + strFast + strpp + strHba + strPulse + strBloodGroup + strDisability + strSmoking + strAnemic + strDescription;


                        tvFinalVitals.setText(Html.fromHtml(finalVitals));
                    }
                });
    }

    private void getPatientDetail() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetailSavePrescription + "?hosp_code=" + msd.getHospCode() + "&crNo=" + patientdetails.getCRNo() + "&visitNo=" + patientdetails.getEpisodeVisitNo() + "&episodeCode=" + patientdetails.getEpisodeCode(), response -> {
            Log.i("getPatientDetail", "onResponse: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = null;

                status = jsonObject.getString("status");

                if (status.equalsIgnoreCase("1")) {
                    PatCompleteGeneralDtlData = jsonObject.getString("Pat_details");
                    umidNo = jsonObject.getString("umid_no");
                    designation = jsonObject.getString("designation");
                    fatherName = jsonObject.getString("fatherName");
                    customerUnit = jsonObject.getString("customUnit");
                    catName = jsonObject.getString("catName");
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getPatientDetail", "onErrorResponse: ", error);
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void btnSpeech(View view) {
        SpeechUtility.getSpeechInput(this, homeSpeechResultLauncher);
    }


    private void callFollowUpService() {

        // Toast.makeText(this, "Please wait while saving prescription.", Toast.LENGTH_SHORT).show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CR_No", patientdetails.getCRNo());
            jsonObject.put("episodeCode", patientdetails.getEpisodeCode());
            jsonObject.put("hosp_code", msd.getHospCode());
            jsonObject.put("visitNo", patientdetails.getEpisodeVisitNo());
             jsonObject.put("progressNote", "");
               jsonObject.put("PlannedVisitDate", "");

            String URL = ServiceUrl.followUpService;


            final String requestBody = jsonObject.toString();
            Log.i("requestBody", "callFollowUpService: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("callFollowUpService", "onResponse: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            //saveEmrJsonData();
                        } else {
                            progressView.setVisibility(View.GONE);
                            btnSave.setEnabled(true);

                            Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Log.i("json exception", "onResponse: " + ex);

                        btnSave.setEnabled(true);
                        Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    progressView.setVisibility(View.GONE);
                    btnSave.setEnabled(true);

                    AppUtilityFunctions.handleExceptions(error, DeskHomeActivity.this);
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
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(DeskHomeActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            btnSave.setEnabled(true);
            Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

/*
    public void saveEmrJsonData() {

        try {


            String URL = ServiceUrl.saveEmrJsonData;
            //    String progressNote = "History: " + strHopi + ", \nVitals: " + strVitals + ", \nTreatment Adviced: " + strTreatmentAdvice + ", \nExaminations: " + strExamination + ", \nProcedures: " + strProcedures + ", \nTests: " + strTests + ", \nChief Complaints: " + strChiefComplaints + ", \nDiagnosis: " + strDiagnosis;


            final String requestBody = (String) createJsonEMR(patientdetails);
            Log.i("requestBody", "saveEmrJsonData: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("saveEhrJsonData", "onResponse: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            // sendPrescriptionData();
                        }
                    } catch (Exception ex) {
                        Log.i("json exception", "onResponse: " + ex);
                        progressView.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    progressView.setVisibility(View.GONE);
                    btnSave.setEnabled(true);

                    AppUtilityFunctions.handleExceptions(error, DeskHomeActivity.this);
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
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(DeskHomeActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            btnSave.setEnabled(true);

            Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }*/
public void saveEmrJsonData() {

    try {


        String URL = ServiceUrl.saveEmrJsonData;
        //    String progressNote = "History: " + strHopi + ", \nVitals: " + strVitals + ", \nTreatment Adviced: " + strTreatmentAdvice + ", \nExaminations: " + strExamination + ", \nProcedures: " + strProcedures + ", \nTests: " + strTests + ", \nChief Complaints: " + strChiefComplaints + ", \nDiagnosis: " + strDiagnosis;


        final String requestBody = (String) createJsonEMR(patientdetails);
        Log.i("requestBody", "saveEmrJsonData: " + requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("saveEhrJsonData", "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        Log.i("TAG", "onResponse: emr json data succesfull ");
                    }
                } catch (Exception ex) {
                    Log.i("json exception", "onResponse: " + ex);
                    progressView.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());

                progressView.setVisibility(View.GONE);
                btnSave.setEnabled(true);

                AppUtilityFunctions.handleExceptions(error, DeskHomeActivity.this);
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
                params.put("userName", "RMLCDACUSER");


                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(DeskHomeActivity.this).addToRequestQueue(stringRequest);
    } catch (Exception e) {
        progressView.setVisibility(View.GONE);
        btnSave.setEnabled(true);

        Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

}



    private Object createJsonEMR(DoctorReqListDetails patientData) {
        ManagingSharedData msd = new ManagingSharedData(this);
        try {
            ArrayList plannedArrayList = new ArrayList();
            plannedArrayList.add(new PlannedVisit());
            FollowUp follow = new FollowUp("0", plannedArrayList);
            ArrayList<FollowUp> followUpArrayList = new ArrayList<>();
            followUpArrayList.add(follow);



            List<DrugJsonArray> drugJsonArray = drugJsonArrayArrayList;

            List<DiagnosisJsonArray> diagnosisJsonArray = diagnosisJsonArrayList;
            List<FollowUp> followUp = followUpArrayList;



            String patName = patientData.getPatName();
            String cRNo = patientData.getCRNo();
            String episodeCode = patientData.getEpisodeCode();
            String episodeVisitNo = patientData.getEpisodeVisitNo();
            String currentVisitDate = AppUtilityFunctions.changeDateFormat(patientData.getpVisitdate(),"yyyy-MM-dd hh:mm:ss","d/MM/yyyy");
            String patVisitType = "";
            String lastVisitDate = "";
            String patGender = patientData.getPatGender();
            String patAge = patientData.getPatAge();
            String patCat = this.catName;
            String patQueueNo = "";
            String hospCode = msd.getHospCode();
            String seatId = msd.getEmployeeCode();
            Integer hrgnumIsDocuploaded = 0;
            String patConsultantName = msd.getUsername();
            String patDept = patientData.getDeptUnitName();
            String patGaurdianName = this.fatherName;


            String patCompleteGeneralDtl = this.PatCompleteGeneralDtlData + "^^^null";


            String strtreatmentAdvice = rx;
            String strVitalsChart = vitalsChart;
            Strpiccle strpiccle = new Strpiccle();
            String strConfidentialsInfo = "";
            String strDeptIdflg = "";
            String strAllDeptIdflg = "";
            String strPresCriptionBookmarkNameval = "";
            String strPresCriptionBookmarkDescVal = "";
            String strUmidNo = this.umidNo;
            String admissionadviceDeptName = "";
            String admissionadviceWardName = "";
            String admissionadviceNotes = "";
            String strDesignation = this.designation;
            String strStation = this.customerUnit;
            String historyOfPresentIllNess = hopi;
            String diagnosisNote = "";
            String investgationNote = "";
            String otherAllergies = "";
//            List<Object> reasonOfVisitJsonArray = new ArrayList<>();
//            List<Object> investigationJsonArray = new ArrayList<>();

            ArrayList<ComplaintsJsonArray> reasonOfVisitJsonArray = complaintsJsonArrayList;
            List<InvestigationJsonArray> investigationJsonArray = investigationsJsonArrayList;
            CompleteHistoryJaonArray completeHistoryJaonArray = new CompleteHistoryJaonArray(past, personal, family, treatment, surgical);
            SystematicExamniationArray systematicExamniationArray = new SystematicExamniationArray(cvs, rs, cns, pa, general, muscular, local);

            List<Object> chronicDiseaseArray = new ArrayList<>();
            PiccleArray piccleArray = new PiccleArray("0", "0", "0", "0", "0", "0");
            List<ProceduresJsonArray> clinicalProcedureJsonArray = proceduresJsonArrayList;
            List<Object> patientRefrel = new ArrayList<>();

            EMRJsonDetails emrJsonDetails = new EMRJsonDetails(patName, cRNo, episodeCode, episodeVisitNo, currentVisitDate, patVisitType, lastVisitDate, patGender, patAge, patCat, patQueueNo, hospCode, hrgnumIsDocuploaded, patConsultantName, patDept, patGaurdianName, patCompleteGeneralDtl, seatId, historyOfPresentIllNess, diagnosisNote, investgationNote, otherAllergies, reasonOfVisitJsonArray, diagnosisJsonArray, investigationJsonArray, completeHistoryJaonArray, systematicExamniationArray, chronicDiseaseArray, piccleArray, clinicalProcedureJsonArray, drugJsonArray, patientRefrel, strpiccle, strtreatmentAdvice, strConfidentialsInfo, strVitalsChart, followUpArrayList, strDeptIdflg, strAllDeptIdflg, strPresCriptionBookmarkNameval, strPresCriptionBookmarkDescVal, strUmidNo, admissionadviceDeptName, admissionadviceWardName, admissionadviceNotes, strDesignation, strStation);


            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder.create();


            return gson.toJson(emrJsonDetails);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }












    private Object createJsonEHR(DoctorReqListDetails patientData) {
        ManagingSharedData msd = new ManagingSharedData(this);
        try {
            ArrayList plannedArrayList = new ArrayList();
            plannedArrayList.add(new PlannedVisit());
            FollowUp follow = new FollowUp("0", plannedArrayList);
            ArrayList<FollowUp> followUpArrayList = new ArrayList<>();
            followUpArrayList.add(follow);


            List<String> invTestCode = InvTestCode;
            List<String> invTestCodeToPrint = InvTestCodeToPrint;



            if (drugsArray.size()==0||drugsArray.contains(null))
            {
                drugsArray=new ArrayList<>();
            }
            List<String> drugCodeCat = drugsArray;


            List<String> diagnosis = diagnosisArray;
            List<FollowUp> followUp = followUpArrayList;
            String patName = patientData.getPatName();
            String cRNo = patientData.getCRNo();
            String episodeCode = patientData.getEpisodeCode();
            String episodeVisitNo = patientData.getEpisodeVisitNo();
            String currentVisitDate = AppUtilityFunctions.changeDateFormat(patientData.getpVisitdate(),"yyyy-MM-dd hh:mm:ss","d/MM/yyyy");

            String patVisitType = "";
            String lastVisitDate = "";
            String patGender = patientData.getPatGender();
            String patAge = patientData.getPatAge();
            String patCat = this.catName;
            String patQueueNo = "";
            String hospCode = msd.getHospCode();
            String seatId = msd.getEmployeeCode();
            Integer hrgnumIsDocuploaded = 0;
            String patConsultantName = msd.getUsername();
            String patDept = patientData.getDeptUnitName();
            String patGaurdianName = this.fatherName;

            String patCompleteGeneralDtl = this.PatCompleteGeneralDtlData + "^^^null";


            StrCompleteHistory strCompleteHistory = new StrCompleteHistory(past, personal, family, treatment, surgical);

            StrSystematicExamniation strSystematicExamniation = new StrSystematicExamniation(cvs, rs, cns, pa, general, muscular, local);
            List<Object> strChronicDisease = new ArrayList();
            String strHistoryOfPresentIllNess = hopi;
            String strDiagnosisNote = "";
            List<Object> strDrugAllergy = new ArrayList();
            String strInvestgationNote = "";
            String strotherAllergies = "";
            List<String> strClinicalProcedure = proceduresArray;
            String strtreatmentAdvice = rx;
            String strVitalsChart = vitalsChart;
            Strpiccle strpiccle = new Strpiccle();
            String strConfidentialsInfo = "";
            List<Object> strReferal = new ArrayList();
            String strDeptIdflg = "";
            String strAllDeptIdflg = "";
            String strPresCriptionBookmarkNameval = "";
            String strPresCriptionBookmarkDescVal = "";
            String strUmidNo = this.umidNo;
            String admissionadviceDeptName = "";
            String admissionadviceWardName = "";
            String admissionadviceNotes = "";
            String strDesignation = this.designation;
            String strStation = this.customerUnit;

            EHRJsonDetails ehrJsonDetails = new EHRJsonDetails(invTestCode, invTestCodeToPrint, drugCodeCat, reasonOfVisit, diagnosis, followUp, patName, cRNo, episodeCode, episodeVisitNo, currentVisitDate, patVisitType, lastVisitDate, patGender, patAge, patCat, patQueueNo, hospCode, seatId, hrgnumIsDocuploaded, patConsultantName, patDept, patGaurdianName, patCompleteGeneralDtl, strCompleteHistory, strSystematicExamniation, strChronicDisease, strHistoryOfPresentIllNess, strDiagnosisNote, strDrugAllergy, strInvestgationNote, strotherAllergies, strClinicalProcedure, strtreatmentAdvice, strVitalsChart, strpiccle, strConfidentialsInfo, strReferal, strDeptIdflg, strAllDeptIdflg, strPresCriptionBookmarkNameval, strPresCriptionBookmarkDescVal, strUmidNo, admissionadviceDeptName, admissionadviceWardName, admissionadviceNotes, strDesignation, strStation);


            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.disableHtmlEscaping().create();


            return gson.toJson(ehrJsonDetails);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }




    public void saveEhrJsonData(DoctorReqListDetails patientData) {
        progressView.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        try {


            String URL = ServiceUrl.saveEhrJsonDataNew;
            // String progressNote = "History: " + strHopi + ", \nVitals: " + strVitals + ", \nTreatment Adviced: " + strTreatmentAdvice + ", \nExaminations: " + strExamination + ", \nProcedures: " + strProcedures + ", \nTests: " + strTests + ", \nChief Complaints: " + strChiefComplaints + ", \nDiagnosis: " + strDiagnosis;


            final String requestBody = (String) createJsonEHR(patientData);
            Log.i("requestBody", "sendPrescriptionData: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("saveEhrJsonData", "onResponse: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                             callFollowUpService();
                            saveBase64Pdf();
                        } else {
                            progressView.setVisibility(View.GONE);
                                 btnSave.setEnabled(true);
                            Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Log.i("json exception", "onResponse: " + ex);
                        progressView.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    progressView.setVisibility(View.GONE);
                      btnSave.setEnabled(true);
                    AppUtilityFunctions.handleExceptions(error, DeskHomeActivity.this);
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
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(DeskHomeActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            //  btnOk.setEnabled(true);
            //  btnCancel.setEnabled(true);
            Toast.makeText(DeskHomeActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    private String createVitealsJson(String strGeneral) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("strWeight", weight);
            jsonObject.put("strHeight", height);
            jsonObject.put("strBmid", bmi);
            jsonObject.put("strTempreature", temperature);
            jsonObject.put("strrespRate", "");
            jsonObject.put("strhaemoglobin", hgb);
            jsonObject.put("strdiastolic", low);
            jsonObject.put("strsystolic", high);
            jsonObject.put("strfasting", fast);
            jsonObject.put("strRateId", "");
            jsonObject.put("strhba1c", hba);
            jsonObject.put("strsymptoms", "");
            jsonObject.put("strPatdtls", patientdetails.getPatName()+"^"+patientdetails.getCRNo()+"^"+patientdetails.getEpisodeCode()+"^"+patientdetails.getPatVisitNo()+"^"+patientdetails.getHospCode()+"^First Visit^^"+msd.getSeatId());

            jsonObject.put("strGeneral", strGeneral);
            jsonObject.put("strbmiErrmsg", bmiStatus);
            jsonObject.put("strtemperatureErrmsg", "");
            jsonObject.put("strrespRateErrmsg", "");
            jsonObject.put("strhaemoglobinErrmsg", "");
            jsonObject.put("strbpErrmsg", "");
            jsonObject.put("strfastingErrmsg", "");
            jsonObject.put("strppRateErrmsg", "");
            jsonObject.put("strhba1cErrmsg", "");
            jsonObject.put("strpulseRate", (pulse.isEmpty())?"0":pulse);
            jsonObject.put("strbloodGroup", bloodGroup);
            jsonObject.put("strmuac", "0");
            jsonObject.put("strcurcumference", "0");
            jsonObject.put("strDisability", disability?"Yes":"No");
            jsonObject.put("strSmoking", smoking?"Yes":"No");
            jsonObject.put("strAnemic", anemic?"Yes":"No");
            jsonObject.put("strPregnancy", "");
            jsonObject.put("strdiastolic1", "0");
            jsonObject.put("strsystolic1", "0");
            jsonObject.put("strcancerScreening", "0");
        }catch(JSONException ex)
        {
            ex.printStackTrace();
        }


        Log.i("TAG", "createVitealsJson: "+jsonObject);
        return jsonObject.toString();
    }

    private void saveVitalsData(String requestBody) {
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.testurl + "AppOpdService/saveVitalData", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error);
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
                params.put("userName", "RMLCDACUSER");


                return params;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void saveBase64Pdf() {
        try {

            updateRequestStatus(patientdetails.getRequestID(), "2", patientdetails.getDocMessage());

            Intent intent = new Intent(this, DoctorRequestListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {
            Log.i("pdf exception", "saveBase64Pdf: " + ex);

        }
    }

    private void updateRequestStatus(String requestId, String requestStatus, String docMessage) {


   StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateRequestStatus + msd.getHospCode() + "&requestID=" + requestId + "&reqStatus=" + requestStatus + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&consltMobNo=" + msd.getPatientDetails().getMobileNo() + "&docMessage=" + docMessage + "&doctorToken=", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaaa", "onResponse: " + response);
                //send prescription
                ManagingSharedData msd = new ManagingSharedData(DeskHomeActivity.this);
                String title = "eConsultation Completed";
                String message = "Your prescription for eConsultation with " + msd.getUsername() + " has been generated. You can view the PDF from \"Consultation and Status\" page.";
          //      sendFCMPush(title, message);

                progressView.setVisibility(View.GONE);
//                btnOk.setEnabled(true);
//                btnCancel.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
//                btnOk.setEnabled(true);
//                btnCancel.setEnabled(true);
                Toast.makeText(DeskHomeActivity.this, "Unable to update status.Try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);

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



        if (patientdetails.getScrResponse().length() < 10) {
            patientdetails.setScrResponse(String.format("%-10s", patientdetails.getScrResponse()));
            Log.i(TAG, "btnpatientdetails: " + patientdetails.getScrResponse());
        }
        String fever = String.valueOf(patientdetails.getScrResponse().charAt(0));
        String cough = String.valueOf(patientdetails.getScrResponse().charAt(1));
        String soreThroat = String.valueOf(patientdetails.getScrResponse().charAt(2));
        String breathingDifficulty = String.valueOf(patientdetails.getScrResponse().charAt(3));
        String congestion = String.valueOf(patientdetails.getScrResponse().charAt(4));
        String bodyAche = String.valueOf(patientdetails.getScrResponse().charAt(5));
        String pinkEyes = String.valueOf(patientdetails.getScrResponse().charAt(6));
        String smell = String.valueOf(patientdetails.getScrResponse().charAt(7));
        String hearingImpairment = String.valueOf(patientdetails.getScrResponse().charAt(8));
        String gastroIntestinal = String.valueOf(patientdetails.getScrResponse().charAt(9));

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

        if ((patientdetails.getScrResponse() + patientdetails.getPatWeight() + patientdetails.getPatHeight() + patientdetails.getPatMedication() + patientdetails.getPatPastDiagnosis() + patientdetails.getPatAllergies() + patientdetails.getRmrks()).trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "No information submitted.", Toast.LENGTH_SHORT).show();
        } else {
            tvWeight.setText("Weight: " + patientdetails.getPatWeight());
            tvHeight.setText("Height: " + patientdetails.getPatHeight());
            tvPatMedications.setText("Medication: " + patientdetails.getPatMedication());
            tvPastDiagonsis.setText("Past Diagnosis: " + patientdetails.getPatPastDiagnosis());
            tvAllergies.setText("Allergies: " + patientdetails.getPatAllergies());
            tvDescription.setText("Problem Description: " + patientdetails.getRmrks());


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

            if (!patientdetails.getPatWeight().equalsIgnoreCase("")) {
                imgWeight.setVisibility(View.VISIBLE);
                tvWeight.setVisibility(View.VISIBLE);
            }
            if (!patientdetails.getPatHeight().equalsIgnoreCase("")) {
                imgHeight.setVisibility(View.VISIBLE);
                tvHeight.setVisibility(View.VISIBLE);
            }

            if (!patientdetails.getPatMedication().equalsIgnoreCase("")) {
                imgMedications.setVisibility(View.VISIBLE);
                tvPatMedications.setVisibility(View.VISIBLE);
            }
            if (!patientdetails.getPatPastDiagnosis().equalsIgnoreCase("")) {
                imgDiagnosis.setVisibility(View.VISIBLE);
                tvPastDiagonsis.setVisibility(View.VISIBLE);
            }
            if (!patientdetails.getPatAllergies().equalsIgnoreCase("")) {
                imgAllergies.setVisibility(View.VISIBLE);
                tvAllergies.setVisibility(View.VISIBLE);
            }
            if (!patientdetails.getRmrks().equalsIgnoreCase("")) {
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
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        String mobileNo = patientdetails.getPatMobileNo();
                        if (mobileNo.length() > 10) {
                            mobileNo = mobileNo.substring(2);
                        }
                        startDialActivity(mobileNo);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();


        //startDialActivity(patientdetails.getPatMobileNo().substring(2));

    }

    private void startDialActivity(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    public void btnViewDocument(View view) {

//        Intent intent = new Intent(DeskActivity.this, ViewDocumentActivity.class);
        Intent intent = new Intent(DeskHomeActivity.this, ViewDocNewActivity.class);
        intent.putExtra("requestId", patientdetails.getRequestID());
        startActivity(intent);
    }

    public void btnWhatsappCall(View view) {
        ManagingSharedData msd = new ManagingSharedData(this);
        sendFCMPush("eConsultation Doctor Call", msd.getUsername() + " is calling you for eConsultation. Please join the call using the “Join Video Call” link shown in the “Consultation and Status” page of the app.");
        jitsiVideoCall();

    }

    private void sendFCMPush(String notificationTitle, String message) {


//        String Legacy_SERVER_KEY = getResources().getString(R.string.server_legacy_key);
        String title = notificationTitle;
        String token = patientdetails.getPatientToken();
        JSONObject obj = null;


        try {


            obj = new JSONObject();
            JSONObject objData1 = new JSONObject();

            // objData.put("data", msg);
            objData1.put("title", title);
            objData1.put("content", message);
            objData1.put("navigateTo", patientdetails.getRequestID());


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


    private void jitsiVideoCall() {

        try {
            SessionServicecall sessionServicecall = new SessionServicecall(this);
            sessionServicecall.saveSession(patientdetails.getCRNo(), patientdetails.getPatMobileNo(), patientdetails.getHospCode(), "Video call initiated", "", "", "", "");
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
                .setSubject(patientdetails.getPatName())
                //.setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
//                .setRoom("https://jitsi.cdac.in/" + patientDetails.getRequestID())
                .setRoom("https://mconsultancy.uat.dcservices.in/" + patientdetails.getRequestID())
                .setFeatureFlag("pip.enabled", false)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(this, options);
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
}