package com.cdac.uphmis.covid19;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.OPDAppointmentActivity;
import com.cdac.uphmis.covid19.model.DepartmentDetails;
import com.cdac.uphmis.covid19.model.DistrictDetails;
import com.cdac.uphmis.covid19.model.DivisionDetails;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.covid19.model.ScreeningDetails;
import com.cdac.uphmis.covid19.model.StateDetails;
import com.cdac.uphmis.covid19.model.ZoneDetails;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NetworkStats;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.Validation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.android.volley.VolleyLog.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.getIndex;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class ScreeningActivity extends AppCompatActivity {

    private Spinner forSpinner, zoneSpinner, divisionSpinner, hospSpinner, departmentSpinner;

    private String responseHOF = " ", responseHOC = " ", responseHOS = " ", responseBD = " ";//, responseFT = " ";
    private String responsegastro = " ", responseHearingImpairment = " ", responseBodyAche = " ", responsePinkEyes = " ", responseCongestion = " ", responseSmell = " ";

    ManagingSharedData msd;

    private String fatherName = "", motherName = "", spouseName = "";

    private EditText efirstname, elastname, eage, efmsname, emobile, eemail;
    private Spinner sspinner, districtSpinner;
    private Spinner sgender, srelation;

    String genderId, stateId, districtId;
    LinearLayout llRegistrationForm;

    View forSpinnerView;
    //New Parameters
    private EditText edtRemarks;
    private TextView edtWeight, edtHeight;
    private EditText edtMedications, edtPreviouslyDiagnosed, edtAllergies;
    ImageButton btnShowMedications, btnShowPreviousDiagnosed, btnShowAllergies, btnHideMedications, btnHidePreviousDiagnosed, btnHideAllergies;
    TextView tvCovideScreeningParam, tvPreExistingParam;
    LinearLayout llCovidScreen;

    GridLayout glPreExistingSymptoms;


    TextView tvShowMedications, tvShowPreviousDiagnosed, tvShowAllergies;

    boolean istvFeverSelected = false, istvCoughSelected = false, istvSoreThroatSelected = false, istvBreathingDifficultySelected = false;//, istvFreignTravelSelected = false;
    boolean istvCongestionSelected = false, istvBodyAcheSelected = false, istvPinkEyesSelected = false, istvSmellSelected = false, istvHearingImpairmentSelected = false, isTvGastro = false;
    private LinearLayout llMedicalDetails;
    private String unitcode, guardianName;
    GeometricProgressView progressView;
    String mToken = "";
    int byDefaultState = 0;
    private String zoneId = "0";
    private String divisionId = "0";
    private View zoneSpinnerView, divisionSpinnerView, hospSpinnerView, departmentSpinnerView;

    private Button btnSubmit;

    private String preExistingSymptoms = "";
    boolean istvDiabetesSelected = false, istvHypertensionSelected = false, istvLungDiseaseSelected = false, istvHeartDiseaseSelected = false, istvKidneyDisorderSelected = false, isAsthmaSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);

        //NukeSSLCerts.nuke(this);
        msd = new ManagingSharedData(this);
        
        //todo firebase
        if (msd.getToken() == null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    mToken = instanceIdResult.getToken();
                    Log.e("Token", "" + mToken);
                    msd.setToken(mToken);
                }
            });
        }
        mToken = msd.getToken();
        Log.e("mToken", "" + mToken);
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }

        progressView = (GeometricProgressView) findViewById(R.id.progress_view);
        llRegistrationForm = findViewById(R.id.ll_registration_form);
        llMedicalDetails = findViewById(R.id.ll_medical_details);
        // requestQueue = Volley.newRequestQueue(this);
        initializeViews();
        initializeSpinners();
        registrationFormInitializeViews();
        getHospitals();

    }

    private void initializeViews() {
        edtWeight = findViewById(R.id.edt_weight);
        edtHeight = findViewById(R.id.edt_height);

        edtRemarks = findViewById(R.id.edt_remarks);
        edtMedications = findViewById(R.id.edt_medications);
        edtPreviouslyDiagnosed = findViewById(R.id.edt_diagnosed_condtitions);
        edtAllergies = findViewById(R.id.edt_allergies);

        btnShowMedications = findViewById(R.id.btn_show_medications);
        btnShowPreviousDiagnosed = findViewById(R.id.btn_show_diagnosed_conditions);
        btnShowAllergies = findViewById(R.id.btn_show_allergies);

        btnHideMedications = findViewById(R.id.btn_hide_medications);
        btnHidePreviousDiagnosed = findViewById(R.id.btn_hide_diagnosed_conditions);
        btnHideAllergies = findViewById(R.id.btn_show_hide_allergies);

        tvShowMedications = findViewById(R.id.tv_show_medications);
        tvShowPreviousDiagnosed = findViewById(R.id.tv_show_diagnosed_conditions);
        tvShowAllergies = findViewById(R.id.tv_show_allergies);


        llCovidScreen = findViewById(R.id.llcovid_screen);
        tvCovideScreeningParam = findViewById(R.id.tv_covid_screening_param);
        tvCovideScreeningParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llCovidScreen.getVisibility() == View.VISIBLE) {
                    llCovidScreen.setVisibility(View.GONE);
                } else {
                    llCovidScreen.setVisibility(View.VISIBLE);
                }
            }
        });


        glPreExistingSymptoms = findViewById(R.id.gl_pre_condition);
        tvPreExistingParam = findViewById(R.id.tv_pre_existing_param);
        tvPreExistingParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (glPreExistingSymptoms.getVisibility() == View.VISIBLE) {
                    glPreExistingSymptoms.setVisibility(View.GONE);
                } else {
                    glPreExistingSymptoms.setVisibility(View.VISIBLE);
                }
            }
        });


        tvShowMedications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMedications.setVisibility(View.VISIBLE);
                btnShowMedications.setImageResource(R.drawable.right);
                btnHideMedications.setImageResource(R.drawable.close);
            }
        });
        btnShowMedications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMedications.setVisibility(View.VISIBLE);
                btnShowMedications.setImageResource(R.drawable.right);
                btnHideMedications.setImageResource(R.drawable.close);
            }
        });

        btnHideMedications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMedications.setVisibility(View.GONE);
                btnShowMedications.setImageResource(R.drawable.covid_right);
                btnHideMedications.setImageResource(R.drawable.covid_close_red);

            }
        });

        tvShowPreviousDiagnosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPreviouslyDiagnosed.setVisibility(View.VISIBLE);
                btnShowPreviousDiagnosed.setImageResource(R.drawable.right);
                btnHidePreviousDiagnosed.setImageResource(R.drawable.close);
            }
        });
        btnShowPreviousDiagnosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPreviouslyDiagnosed.setVisibility(View.VISIBLE);
                btnShowPreviousDiagnosed.setImageResource(R.drawable.right);
                btnHidePreviousDiagnosed.setImageResource(R.drawable.close);
            }
        });

        btnHidePreviousDiagnosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPreviouslyDiagnosed.setVisibility(View.GONE);
                btnShowPreviousDiagnosed.setImageResource(R.drawable.covid_right);
                btnHidePreviousDiagnosed.setImageResource(R.drawable.covid_close_red);

            }
        });


        tvShowAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAllergies.setVisibility(View.VISIBLE);
                btnShowAllergies.setImageResource(R.drawable.right);
                btnHideAllergies.setImageResource(R.drawable.close);
            }
        });
        btnShowAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAllergies.setVisibility(View.VISIBLE);
                btnShowAllergies.setImageResource(R.drawable.right);
                btnHideAllergies.setImageResource(R.drawable.close);
            }
        });
        btnHideAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAllergies.setVisibility(View.GONE);
                btnShowAllergies.setImageResource(R.drawable.covid_right);
                btnHideAllergies.setImageResource(R.drawable.covid_close_red);

            }
        });


        edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterWeight();
            }
        });

        edtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterHeight();
            }
        });
        covidParamResponse();
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
//                hospSpinner.setAdapter(new ArrayAdapter(ScreeningActivity.this, R.layout.for_layout, hospitalDetailsArrayList));

                hospSpinner.setAdapter(new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, hospitalDetailsArrayList));
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
    private void covidParamResponse() {
        final TextView tvFever = findViewById(R.id.tv_fever);
        final TextView tvCough = findViewById(R.id.tv_cough);
        final TextView tvSoreThroat = findViewById(R.id.tv_sore_throat);
        final TextView tvBreathingDifficulty = findViewById(R.id.tv_breathing_diffculty);

        final TextView tvGastroIntestinal = findViewById(R.id.tv_gastro_intestial);
        final TextView tvHearing = findViewById(R.id.tv_hearing_impairement);
        final TextView tvPinkEyes = findViewById(R.id.tv_pink_eyes);
        final TextView tvSmell = findViewById(R.id.tv_smell_taste);
        final TextView tvBodyAche = findViewById(R.id.tv_body_ache);
        final TextView tvChestCongestion = findViewById(R.id.tv_chest_conjestion);
//        final TextView tvFroeignTravel = findViewById(R.id.tv_foreign_travel);


        tvFever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvFeverSelected) {
                    deSelectCovidParam(tvFever);
                    istvFeverSelected = false;
                    responseHOF = "N";
                } else {
                    selectScreeningParam(tvFever);
                    istvFeverSelected = true;
                    responseHOF = "Y";
                }
            }
        });
        tvCough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvCoughSelected) {
                    deSelectCovidParam(tvCough);
                    istvCoughSelected = false;
                    responseHOC = "N";
                } else {
                    selectScreeningParam(tvCough);
                    istvCoughSelected = true;
                    responseHOC = "Y";
                }
            }
        });
        tvSoreThroat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvSoreThroatSelected) {
                    deSelectCovidParam(tvSoreThroat);
                    istvSoreThroatSelected = false;
                    responseHOS = "N";
                } else {
                    selectScreeningParam(tvSoreThroat);
                    istvSoreThroatSelected = true;
                    responseHOS = "Y";
                }
            }
        });
        tvBreathingDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvBreathingDifficultySelected) {
                    deSelectCovidParam(tvBreathingDifficulty);
                    istvBreathingDifficultySelected = false;
                    responseBD = "N";
                } else {
                    selectScreeningParam(tvBreathingDifficulty);
                    istvBreathingDifficultySelected = true;
                    responseBD = "Y";
                }
            }
        });


        //new symptoms
        tvChestCongestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvCongestionSelected) {
                    deSelectCovidParam(tvChestCongestion);
                    istvCongestionSelected = false;
                    responseCongestion = "N";
                } else {
                    selectScreeningParam(tvChestCongestion);
                    istvCongestionSelected = true;
                    responseCongestion = "Y";
                }
            }
        });

        tvBodyAche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvBodyAcheSelected) {
                    deSelectCovidParam(tvBodyAche);
                    istvBodyAcheSelected = false;
                    responseBodyAche = "N";
                } else {
                    selectScreeningParam(tvBodyAche);
                    istvBodyAcheSelected = true;
                    responseBodyAche = "Y";
                }
            }
        });

        tvPinkEyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvPinkEyesSelected) {
                    deSelectCovidParam(tvPinkEyes);
                    istvPinkEyesSelected = false;
                    responsePinkEyes = "N";
                } else {
                    selectScreeningParam(tvPinkEyes);
                    istvPinkEyesSelected = true;
                    responsePinkEyes = "Y";
                }
            }
        });

        tvSmell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvSmellSelected) {
                    deSelectCovidParam(tvSmell);
                    istvSmellSelected = false;
                    responseSmell = "N";
                } else {
                    selectScreeningParam(tvSmell);
                    istvSmellSelected = true;
                    responseSmell = "Y";
                }
            }
        });

        tvHearing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvHearingImpairmentSelected) {
                    deSelectCovidParam(tvHearing);
                    istvHearingImpairmentSelected = false;
                    responseHearingImpairment = "N";
                } else {
                    selectScreeningParam(tvHearing);
                    istvHearingImpairmentSelected = true;
                    responseHearingImpairment = "Y";
                }
            }
        });

        tvGastroIntestinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTvGastro) {
                    deSelectCovidParam(tvGastroIntestinal);
                    isTvGastro = false;
                    responsegastro = "N";
                } else {
                    selectScreeningParam(tvGastroIntestinal);
                    isTvGastro = true;
                    responsegastro = "Y";
                }
            }
        });
    /*    tvFroeignTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvFreignTravelSelected) {
                    deSelectCovidParam(tvFroeignTravel);
                    istvFreignTravelSelected = false;
                    responseFT = "N";
                } else {
                    selectScreeningParam(tvFroeignTravel);
                    istvFreignTravelSelected = true;
                    responseFT = "Y";
                }
            }
        });*/

        preExistingConditions();
    }

    private void preExistingConditions() {
        final TextView tvDiabetes = findViewById(R.id.tv_diabetes);
        final TextView tvHyperTension = findViewById(R.id.tv_hypertension);
        final TextView tvLungDisease = findViewById(R.id.tv_lung_disease);
        final TextView tvHeartDisease = findViewById(R.id.tv_heart_disease);
        final TextView tvKidneyDisorder = findViewById(R.id.tv_kidney_disorder);
        final TextView tvAsthma = findViewById(R.id.tv_asthma);


        tvDiabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvDiabetesSelected) {
                    istvDiabetesSelected = false;
                    deSelectCovidParam(tvDiabetes);
//                    preExistingSymptoms=preExistingSymptoms+"Diabetes";
                } else {
                    selectScreeningParam(tvDiabetes);
                    istvDiabetesSelected = true;
//                    preExistingSymptoms=preExistingSymptoms.replaceAll("Diabetes","");
                }
            }
        });
        tvHyperTension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvHypertensionSelected) {
                    deSelectCovidParam(tvHyperTension);
                    istvHypertensionSelected = false;

                } else {
                    selectScreeningParam(tvHyperTension);
                    istvHypertensionSelected = true;

                }
            }
        });
        tvLungDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvLungDiseaseSelected) {
                    deSelectCovidParam(tvLungDisease);
                    istvLungDiseaseSelected = false;

                } else {
                    selectScreeningParam(tvLungDisease);
                    istvLungDiseaseSelected = true;

                }
            }
        });
        tvHeartDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvHeartDiseaseSelected) {
                    deSelectCovidParam(tvHeartDisease);
                    istvHeartDiseaseSelected = false;

                } else {
                    selectScreeningParam(tvHeartDisease);
                    istvHeartDiseaseSelected = true;

                }
            }
        });
        tvKidneyDisorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istvKidneyDisorderSelected) {
                    deSelectCovidParam(tvKidneyDisorder);
                    istvKidneyDisorderSelected = false;

                } else {
                    selectScreeningParam(tvKidneyDisorder);
                    istvKidneyDisorderSelected = true;

                }
            }
        });

        tvAsthma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAsthmaSelected) {
                    deSelectCovidParam(tvAsthma);
                    isAsthmaSelected = false;

                } else {
                    selectScreeningParam(tvAsthma);
                    isAsthmaSelected = true;

                }
            }
        });


    }

    private void deSelectCovidParam(TextView tv) {
        tv.setTextColor(Color.parseColor("#FF03A9F4"));
        tv.setBackgroundResource(R.drawable.textbox_background);
    }

    private void selectScreeningParam(TextView tv) {
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundResource(android.R.color.holo_green_light);
    }

    private void enterWeight() {
        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.weight_dialog, null);
//        d.setTitle("Select Weight").set(Gravity.CENTER);
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(200);
        numberPicker.setValue(65);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: " + numberPicker.getValue());
            }
        });
        d.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: " + numberPicker.getValue());
                edtWeight.setText(String.valueOf(numberPicker.getValue()));

            }
        });
        d.setNegativeButton( getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtWeight.setText("");
                Log.i(TAG, "onCreateVieww: " + edtWeight.getText().toString());
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }


    private void enterHeight() {
        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.height_dialog, null);
        d.setView(dialogView);

        final NumberPicker numberPickerfeet = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPickerfeet.setMaxValue(9);
        numberPickerfeet.setValue(5);
        numberPickerfeet.setMinValue(1);
        numberPickerfeet.setWrapSelectorWheel(false);

        numberPickerfeet.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "feet: " + numberPicker.getValue());
            }
        });


        final NumberPicker numberPickerInches = (NumberPicker) dialogView.findViewById(R.id.dialog_inches);
        numberPickerInches.setMaxValue(11);
        numberPickerInches.setValue(5);
        numberPickerInches.setMinValue(0);
        numberPickerInches.setWrapSelectorWheel(false);

        numberPickerInches.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "feet: " + numberPicker.getValue());
            }
        });

        d.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.d(TAG, "onClick: " + numberPickerfeet.getValue());
                Log.d(TAG, "onClick: " + String.valueOf(numberPickerfeet.getValue() + " ' " + numberPickerInches.getValue() + "'"));
                edtHeight.setText(String.valueOf(numberPickerfeet.getValue() + "' " + numberPickerInches.getValue() + "''"));

            }
        });
        d.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtHeight.setText("");
                Log.i(TAG, "onCreateVieww: " + edtWeight.getText().toString());
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }

    private void registrationFormInitializeViews() {
        efirstname = findViewById(R.id.efname);
        elastname = findViewById(R.id.elname);
        eage = findViewById(R.id.eage);
        efmsname = findViewById(R.id.ername);
//        ecity = findViewById(R.id.ecity);
//        epincode = findViewById(R.id.epincode);
        emobile = findViewById(R.id.emobile);
        eemail = findViewById(R.id.eemail);


        sgender = findViewById(R.id.sgender);
        emobile.setEnabled(false);
        emobile.setText(msd.getPatientDetails().getMobileNo());
        final String arGender[] = {"Male", "Female", "Transgender"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScreeningActivity.this,
                android.R.layout.simple_spinner_item, arGender);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sgender.setAdapter(adapter);
        genderId = "M";
        sgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (arGender[position].equalsIgnoreCase("Male")) {
                    genderId = "M";
                } else if (arGender[position].equalsIgnoreCase("Female")) {
                    genderId = "F";
                } else if (arGender[position].equalsIgnoreCase("Transgender")) {
                    genderId = "T";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        srelation = findViewById(R.id.srelation);

        final String arRelation[] = {"Father", "Mother", "Spouse"};
        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(ScreeningActivity.this,
                android.R.layout.simple_spinner_item, arRelation);

        adapterRelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srelation.setAdapter(adapterRelation);

        srelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (arRelation[position].equalsIgnoreCase("Father")) {
                    efmsname.setHint("Father's Name*");
                    fatherName = efmsname.getText().toString();
                    motherName = "";
                    spouseName = "";
                } else if (arRelation[position].equalsIgnoreCase("Mother")) {
                    efmsname.setHint("Mother's Name*");
                    fatherName = "";
                    motherName = efmsname.getText().toString();
                    spouseName = "";
                } else if (arRelation[position].equalsIgnoreCase("Spouse")) {
                    efmsname.setHint("Spouse's Name*");
                    fatherName = "";
                    motherName = "";
                    spouseName = efmsname.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void initializeSpinners() {
        forSpinner = findViewById(R.id.for_spinner);
        forSpinnerView = findViewById(R.id.for_spinner_view);

        zoneSpinner = findViewById(R.id.zone_spinner);
        zoneSpinnerView = findViewById(R.id.zone_spinner_view);

        divisionSpinner = findViewById(R.id.division_spinner);
        divisionSpinnerView = findViewById(R.id.division_spinner_view);

        hospSpinnerView = findViewById(R.id.hosp_spinner_view);
        departmentSpinnerView = findViewById(R.id.department_spinner_view);

        hospSpinner = findViewById(R.id.hosp_spinner);
        departmentSpinner = findViewById(R.id.department_spinner);
        sspinner = findViewById(R.id.sstate);
        districtSpinner = findViewById(R.id.sdistrict);

        btnSubmit = findViewById(R.id.btn_submit);


        getCrList();
//        getHospitals();
        getStates();

        getZoneList();


//        Gson gson = new Gson();
//        String response=msd.getCrList();
//        ArrayList<PatientDetails> PatientDetailsArrayList = gson.fromJson(response,
//                new TypeToken<List<PatientDetails>>(){}.getType());
//
//        PatientDetailsArrayList.add(new PatientDetails(null, null, "Someone else", null, null, fatherName, motherName,null, null, null, null));
//        ArrayAdapter adapter = new ArrayAdapter(ScreeningActivity.this, R.layout.for_layout, PatientDetailsArrayList);

//        forSpinner.setAdapter(adapter);
        forSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                PatientDetails PatientDetails = (PatientDetails) forSpinner.getSelectedItem();
                if (PatientDetails.getFirstname().equalsIgnoreCase("someone else")) {
                    llRegistrationForm.setVisibility(View.VISIBLE);
                } else {

                    llRegistrationForm.setVisibility(View.GONE);
//                    String hospCode=PatientDetails.getCrno().substring(0,5);
//                    Log.i(TAG, "onItemSelected: "+PatientDetails.getCrno().substring(0,5));
//getHospitals(hospCode);
                }
            }
            public void onNothingSelected(
                    AdapterView<?> adapterView) {
            }
        });
        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                ZoneDetails zoneDetails = (ZoneDetails) zoneSpinner.getSelectedItem();
                zoneId = zoneDetails.getZoneId();
                if (zoneId.equalsIgnoreCase("-1")) {
                    // getHospitals("0", "0");
                    divisionSpinner.setVisibility(View.GONE);
                    divisionSpinnerView.setVisibility(View.GONE);
                    hospSpinner.setVisibility(View.GONE);
                    hospSpinnerView.setVisibility(View.GONE);
                    departmentSpinner.setVisibility(View.GONE);
                    departmentSpinnerView.setVisibility(View.GONE);

                    btnSubmit.setVisibility(View.GONE);
                } else {
                    divisionSpinner.setVisibility(View.VISIBLE);
                    divisionSpinnerView.setVisibility(View.VISIBLE);
                    getDivisionList(zoneDetails.getZoneId());
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                DivisionDetails divisionDetails = (DivisionDetails) divisionSpinner.getSelectedItem();

                divisionId = divisionDetails.getDivisionId();
                if (divisionId.equalsIgnoreCase("-1")) {
                    hospSpinner.setVisibility(View.GONE);
                    hospSpinnerView.setVisibility(View.GONE);
                    departmentSpinner.setVisibility(View.GONE);
                    departmentSpinnerView.setVisibility(View.GONE);

                    btnSubmit.setVisibility(View.GONE);
                } else {
                    hospSpinner.setVisibility(View.VISIBLE);
                    hospSpinnerView.setVisibility(View.VISIBLE);
                    getHospitals(zoneId, divisionId);
                }
            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        hospSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
//                progressView.setVisibility(View.GONE);
                HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();

                String hospCode = hospitalDetails.getHospCode();

                if (!hospCode.equalsIgnoreCase("-1")) {
                    msd.setHospCode(hospCode);

                    getDepartments(hospCode);
                    departmentSpinner.setVisibility(View.VISIBLE);
                    departmentSpinnerView.setVisibility(View.VISIBLE);
                } else {
                    departmentSpinner.setVisibility(View.GONE);
                    departmentSpinnerView.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);
                    Toast.makeText(ScreeningActivity.this, "Please select hospital.", Toast.LENGTH_SHORT).show();
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                DepartmentDetails departmentDetails = (DepartmentDetails) departmentSpinner.getSelectedItem();
                if (departmentDetails.getUnitcode().equalsIgnoreCase("-1")) {
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    btnSubmit.setVisibility(View.VISIBLE);
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        sspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                if (byDefaultState == 0) {
                    sspinner.setSelection(28);

                    byDefaultState++;

                }
                StateDetails stateDetails = (StateDetails) sspinner.getSelectedItem();
                getDistricts(stateDetails.getId());
                stateId = stateDetails.getId();
            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                DistrictDetails districtDetails = (DistrictDetails) districtSpinner.getSelectedItem();
                districtId = districtDetails.getId();
            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });


    }


    private void getCrList() {

        final ArrayList<PatientDetails> PatientDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + msd.getPatientDetails().getMobileNo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    PatientDetails setDefaultPatient = null;
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
                        PatientDetailsArrayList.add(patientDetails);
                        if (msd.getPatientDetails() != null) {
                            if (msd.getPatientDetails().toString().equals(PatientDetailsArrayList.get(i).toString())) {
                                setDefaultPatient = PatientDetailsArrayList.get(i);
                            }
                        }
                    }


                    ArrayAdapter adapter = new ArrayAdapter(ScreeningActivity.this, R.layout.for_layout, PatientDetailsArrayList);
                    forSpinner.setAdapter(adapter);


                    if (msd.getPatientDetails() != null) {

                        Log.i(TAG, "onResponse: " + msd.getPatientDetails());
                        if (setDefaultPatient != null) {
                            int spinnerPosition = adapter.getPosition(setDefaultPatient);

                            forSpinner.setSelection(spinnerPosition);

                        }

                    }
                    if (PatientDetailsArrayList.size() == 1) {
                        forSpinner.setVisibility(View.GONE);
                        forSpinnerView.setVisibility(View.GONE);
//                        llRegistrationForm.setVisibility(View.VISIBLE);
                    }
                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, ScreeningActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    /**
     * get zones
     */
    private void getZoneList() {

        final ArrayList<ZoneDetails> zoneArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.zoneListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("zoneresponse is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("zone_details");

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1") && jsonArray.length() > 0) {
                        zoneArrayList.add(new ZoneDetails("-1", "Select Zone"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String zoneId = c.getString("ZONE_ID");
                            String zoneName = c.getString("ZONE_NAME");

                            zoneArrayList.add(new ZoneDetails(zoneId, zoneName));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, zoneArrayList);
                        zoneSpinner.setAdapter(adapter);

//    zoneSpinner.setSelection(setDefaultZone());


                        progressView.setVisibility(View.GONE);
                    } else {

                        getHospitals("0", "0");
                    }

                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                    getHospitals("0", "0");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getHospitals("0", "0");
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, ScreeningActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showZoneAndDivisionSpinners() {
        zoneSpinner.setVisibility(View.VISIBLE);
        divisionSpinner.setVisibility(View.VISIBLE);
        zoneSpinnerView.setVisibility(View.VISIBLE);
        divisionSpinnerView.setVisibility(View.VISIBLE);
    }


    private void hideZoneAndDivisionSpinners() {
        zoneSpinner.setVisibility(View.GONE);
        divisionSpinner.setVisibility(View.GONE);
        zoneSpinnerView.setVisibility(View.GONE);
        divisionSpinnerView.setVisibility(View.GONE);
        hospSpinner.setVisibility(View.VISIBLE);
        hospSpinnerView.setVisibility(View.VISIBLE);
    }


    /**
     * get divisions
     */
    private void getDivisionList(String zoneId) {

        final ArrayList<DivisionDetails> divisionArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionListUrl + "?zoneId=" + zoneId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("division_details");

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1") && jsonArray.length() > 0) {
                        divisionArrayList.add(new DivisionDetails("-1", "Select Division"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String divisionId = c.getString("DIVISION_ID");
                            String divisionName = c.getString("DIVISION_NAME");

                            divisionArrayList.add(new DivisionDetails(divisionId, divisionName));
                        }

                    } else {
                        divisionArrayList.add(new DivisionDetails("-1", "No Division Available"));
//                        getHospitals("0","0");
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, divisionArrayList);
                    divisionSpinner.setAdapter(adapter);

//                    divisionSpinner.setSelection(setDefaultDivision());
                } catch (final Exception e) {
                    getHospitals("0", "0");
                    Log.i("jsonexception", "onResponse: " + e);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getHospitals("0", "0");
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, ScreeningActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void getHospitals(String zoneId, String divisionId) {
        if (zoneId.equalsIgnoreCase("0") || divisionId.equalsIgnoreCase("0")) {
            hideZoneAndDivisionSpinners();
        } else {
            showZoneAndDivisionSpinners();
        }
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<HospitalDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionAndZoneWiseHospitalList + "?zoneId=" + zoneId + "&divisionId=" + divisionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray hospitalList = jsonObject.getJSONArray("hospital_details");
                    if (hospitalList.length() == 0) {
                        hospitalDetailsArrayList.add(new HospitalDetails("-1", "No Hospital Available"));
                    } else {
                        for (int i = 0; i < hospitalList.length(); i++) {
                            JSONObject c = hospitalList.getJSONObject(i);

                            String hospCode = c.getString("CODE");
                            String hospName = c.getString("NAME");
                            String opdtimings = c.optString("OPDTIMINGS");

//if (hospitalCode.equalsIgnoreCase(hospCode)) {
                            hospitalDetailsArrayList.add(new HospitalDetails(hospCode, hospName, opdtimings));
//}

                        }
                    }
                    hospSpinner.setAdapter(new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, hospitalDetailsArrayList));
                    hospSpinner.setSelection(getIndex(hospSpinner, AppConstants.BOKARO_GENERAL_HOSPITAL));

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, ScreeningActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void getDepartments(String hospCode) {
        btnSubmit.setVisibility(View.GONE);
        final ArrayList<DepartmentDetails> departmentArrayList = new ArrayList();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.teleconsultationsDepartments + hospCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.i("department response", "onResponse: " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray deptList = jsonObject.getJSONArray("dept_list");

                    if (deptList.length() == 0) {
                        Toast.makeText(ScreeningActivity.this, "No departments found.", Toast.LENGTH_SHORT).show();
                        departmentArrayList.add(new DepartmentDetails("-1", "", "", "THIS HOSPITAL DOES NOT PROVIDE TELE-CONSULTATION FACILITY. PLEASE CONTACT HOSPITAL ADMINISTRATION", "", "", "", "", "", "", "", ""));
                        departmentSpinner.setAdapter(new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, departmentArrayList));

                        //llMedicalDetails.setVisibility(View.GONE);


                        new AlertDialog.Builder(ScreeningActivity.this)
                                .setTitle("Info")
                                .setMessage("THIS HOSPITAL DOES NOT PROVIDE TELE-CONSULTATION FACILITY. PLEASE CONTACT HOSPITAL ADMINISTRATION")
                                .setNegativeButton("Ok", null)
                                .show();
//                        departmentSpinner.setVisibility(View.GONE);
//                        departmentSpinnerView.setVisibility(View.GONE);
//                        llRegistrationForm.setVisibility(View.GONE);

                    } else {
                        departmentArrayList.add(new DepartmentDetails("-1", "", "", "Select Department", "", "", "", "", "", "", "", ""));
                        for (int i = 0; i < deptList.length(); i++) {
                            JSONObject c = deptList.getJSONObject(i);
                            unitcode = c.getString("UNITCODE");
                            String loCode = c.getString("LOCCODE");
                            String loName = c.getString("LOCNAME");
                            String deptname = c.getString("DEPTNAME");
                            String workingDays = c.getString("WORKINGDAYS");
                            String newPatPortalLimit = c.getString("NEWPATPORTALLIMIT");
                            String oldpatportallimit = c.getString("OLDPATPORTALLIMIT");
                            String loweragelimit = c.getString("LOWERAGELIMIT");
                            String isrefer = c.getString("ISREFER");
                            String actualparameterreferenceid = c.getString("ACTUALPARAMETERREFERENCEID");
                            String maxagelimit = "125";
                            if (c.has("MAXAGELIMIT")) {
                                maxagelimit = c.getString("MAXAGELIMIT");
                            }
                            String boundGenderCode = "";
                            if (c.has("BOUNDGENDERCODE")) {
                                boundGenderCode = c.getString("BOUNDGENDERCODE");
                            }
                            departmentArrayList.add(new DepartmentDetails(unitcode, loCode, loName, deptname, workingDays, newPatPortalLimit, oldpatportallimit, loweragelimit, maxagelimit, isrefer, actualparameterreferenceid, boundGenderCode));
//                            Collections.sort(departmentArrayList, new Comparator<DepartmentDetails>() {
//                                public int compare(DepartmentDetails v1, DepartmentDetails v2) {
//                                    return v1.getDeptname().compareTo(v2.getDeptname());
//                                }
//                            });
                        }
                        departmentSpinner.setAdapter(new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, departmentArrayList));
                        llMedicalDetails.setVisibility(View.VISIBLE);
                        departmentSpinner.setVisibility(View.VISIBLE);
//                        llRegistrationForm.setVisibility(View.VISIBLE);
                    }
                } catch (final JSONException e) {
                    llMedicalDetails.setVisibility(View.GONE);
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                if (ScreeningActivity.this != null) {
                    llMedicalDetails.setVisibility(View.GONE);
                    AppUtilityFunctions.handleExceptions(error, ScreeningActivity.this);
//                    Toast.makeText(ScreeningActivity.this, "Unable to connect server please try again later.", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(getView(), "Unable to connect server please try again later.", Snackbar.LENGTH_SHORT).show();
                }
                //                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void btnSelectSlot(View view) {
        preExistingSymptoms = "";
        preExistingSymptoms = (istvDiabetesSelected && !preExistingSymptoms.contains("Diabetes")) ? preExistingSymptoms + " Diabetes," : preExistingSymptoms.replaceAll("Diabetes,", "");
        preExistingSymptoms = (istvHypertensionSelected && !preExistingSymptoms.contains("Hypertension")) ? preExistingSymptoms + " Hypertension," : preExistingSymptoms.replaceAll("Hypertension,", "");
        preExistingSymptoms = (istvLungDiseaseSelected && !preExistingSymptoms.contains("Lung Disease")) ? preExistingSymptoms + " Lung Disease," : preExistingSymptoms.replaceAll("Lung Disease,", "");
        preExistingSymptoms = (istvHeartDiseaseSelected && !preExistingSymptoms.contains("Heart Disease")) ? preExistingSymptoms + " Heart Disease," : preExistingSymptoms.replaceAll("Heart Disease,", "");
        preExistingSymptoms = (istvKidneyDisorderSelected && !preExistingSymptoms.contains("Kidney Disorder")) ? preExistingSymptoms + " Kidney Disorder," : preExistingSymptoms.replaceAll("Kidney Disorder,", "");
        preExistingSymptoms = (isAsthmaSelected && !preExistingSymptoms.contains("Asthma")) ? preExistingSymptoms + " Asthma" : preExistingSymptoms.replaceAll("Asthma", "");


        preExistingSymptoms = (edtPreviouslyDiagnosed.getText().toString().isEmpty()) ? preExistingSymptoms : preExistingSymptoms + " " + edtPreviouslyDiagnosed.getText().toString();
        Log.i("preexistingsymptoms", "btnSelectSlot: " + preExistingSymptoms);


//        final String scrResponse = responseHOF + responseHOC + responseHOS + responseBD + responseFT;
        final String scrResponse = responseHOF + responseHOC + responseHOS + responseBD + responseCongestion + responseBodyAche + responsePinkEyes + responseSmell + responseHearingImpairment + responsegastro;
        String weight = edtWeight.getText().toString();
        String height = edtHeight.getText().toString();
        if (!weight.trim().equalsIgnoreCase("")) {
            weight = edtWeight.getText().toString() + " (Kg)";
        }
        if (!height.trim().equalsIgnoreCase("")) {
            height = edtHeight.getText().toString();
        }

        PatientDetails PatientDetails = (PatientDetails) forSpinner.getSelectedItem();
        DepartmentDetails departmentDetails = (DepartmentDetails) departmentSpinner.getSelectedItem();
        HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();

        Intent intent = new Intent(ScreeningActivity.this, SlotSelectionActivity.class);

//        if (PatientDetails.getFirstName().equalsIgnoreCase("Someone else")) {

        String strRequestId = "P";
        if (msd.getWhichModuleToLogin().equalsIgnoreCase("healthworkerlogin")) {
            strRequestId = "H";
        } else {
            strRequestId = "P";
        }
        /*For new patients*/
        if (llRegistrationForm.getVisibility() == View.VISIBLE) {
            final String firstname = efirstname.getText().toString();
            final String lastname = elastname.getText().toString();
            final String age = eage.getText().toString();
            final String genderid = genderId;
            final String fmsname = efmsname.getText().toString();
            final String stateid = stateId;
            final String districtid = districtId;

            final String mobile = emobile.getText().toString();
            final String email = eemail.getText().toString();


            Validation validation = new Validation(ScreeningActivity.this, firstname, lastname, age, genderid, fmsname, stateid, districtid, "", mobile, email, "", unitcode, "", "");


            int validationcheck = validation.checkValidation();
            if (validationcheck == 1) {
                if (edtRemarks.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter problem description.", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra("PatientDetails", new ScreeningDetails(strRequestId, "0", scrResponse, "", departmentDetails.getUnitcode(), departmentDetails.getDeptname(), "0", msd.getPatientDetails().getMobileNo(), "", "", "", "", AppUtilityFunctions.capitalizeString(firstname + " " + lastname), age + " Yr", genderid, email, edtRemarks.getText().toString(), weight, height, edtMedications.getText().toString(), preExistingSymptoms, edtAllergies.getText().toString(), "0", stateid, districtid, departmentDetails.getUnitcode() + "@" + departmentDetails.getLoCode() + "@" + departmentDetails.getLoName(), fmsname, mToken, hospitalDetails.getHospCode(), hospitalDetails.getHospName(), hospitalDetails.getOPDTimings()));
                    startActivity(intent);
                }
            }
        }
        /*For registered patients*/
        else {
            if (!PatientDetails.getFathername().equalsIgnoreCase("")) {
                guardianName = PatientDetails.getFathername();
            } else if (!PatientDetails.getMotherName().equalsIgnoreCase("")) {
                guardianName = PatientDetails.getMotherName();
            } else {
                guardianName = PatientDetails.getSPOUSE_NAME();
            }

            if (edtRemarks.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter problem description.", Toast.LENGTH_SHORT).show();
            } else {
//                Log.i(TAG, "btnSelectSlot: " + PatientDetails.getStateId() + " , " + PatientDetails.getDistrictId());
                Log.i(TAG, "btnSelectSlot: " + PatientDetails.getStateCode() + " , " + PatientDetails.getDistrictCode());
                intent.putExtra("PatientDetails", new ScreeningDetails(strRequestId, PatientDetails.getCrno(), scrResponse, "", departmentDetails.getUnitcode(), departmentDetails.getDeptname(), "0", msd.getPatientDetails().getMobileNo(), "", "", "", "", AppUtilityFunctions.capitalizeString(PatientDetails.getFirstname()), PatientDetails.getAge(), PatientDetails.getGender(), PatientDetails.getEmailId(), edtRemarks.getText().toString(), weight, height, edtMedications.getText().toString(), preExistingSymptoms, edtAllergies.getText().toString(), "0", PatientDetails.getStateCode(), PatientDetails.getDistrictCode(), departmentDetails.getUnitcode() + "@" + departmentDetails.getLoCode() + "@" + departmentDetails.getLoName(), guardianName, mToken, hospitalDetails.getHospCode(), hospitalDetails.getHospName(), hospitalDetails.getOPDTimings()));
                startActivity(intent);
            }
        }

    }


    private void getStates() {
        final ArrayList<StateDetails> statesArrayList = new ArrayList<StateDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.urlstates, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String id = c.getString("ID");
                        String name = c.getString("NAME");

                        statesArrayList.add(new StateDetails(id, name));
                    }

                    sspinner.setAdapter(new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, statesArrayList));


                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);

//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void getDistricts(String stateId) {
        final ArrayList<DistrictDetails> districtDetailsArrayList = new ArrayList<DistrictDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.urldistricts + stateId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String id = c.getString("ID");
                        String name = c.getString("NAME");

                        districtDetailsArrayList.add(new DistrictDetails(id, name));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(ScreeningActivity.this, R.layout.new_patient_custom_layout, districtDetailsArrayList);
                    districtSpinner.setAdapter(adapter);


                    districtSpinner.setSelection(setDefaultDistrict(districtSpinner, "Bathinda"));
                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);

//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_network_stats);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(ScreeningActivity.this);
                return true;

            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;
            case R.id.action_network_stats:
                if (checkSelfPermission(READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Please give phone state permission.", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkStats.appUpdateDialog(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //private method of your class
    private int setDefaultDistrict(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }


}
