package com.cdac.uphmis.reimbursement;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.model.DivisionDetails;
import com.cdac.uphmis.covid19.model.ZoneDetails;
import com.cdac.uphmis.model.HospitalDetails;
import com.cdac.uphmis.reimbursement.model.ReimbursementType;
import com.cdac.uphmis.reimbursement.model.SaveReimbursementDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SaveReimbursementActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private LinearLayout[] llSections;
    private ManagingSharedData msd;

    private TextView tvName, tvcrno;

    private TextInputEditText edthospName, edtAdmDate, edtDischDate, edtDiagnosis, edthospBill, edtAmtClaimed;
    //section a
    private AutoCompleteTextView tvReimbursementType;
    //section b
    private AutoCompleteTextView tvctsemember, tvIsEmergency, tv6months, tvHealthScheme, tvInsurance;

    private Button btnSecaNext, bSecbBack, btnSecbNext, cSecbBack, btnSeccNext;


    private static String ADM_DATE_TAG = "admDatePicker";
    private static String DISCH_DATE_TAG = "dischDatePicker";


    private ScrollView scrollView;
    ArrayList<String> yesNoArrayList;

    private LinearLayout llInsurance;

    private TextView tvEmergencyError, tv6MonthsError;

    private String zoneId = "0";
    private String divisionId = "0";
    private Spinner zoneSpinner, divisionSpinner, hospSpinner;
    private View zoneSpinnerView, divisionSpinnerView, hospSpinnerView;

    String hospCode = "-1";
    String hospName = "";

    TextInputEditText edtDischSummaryPage, edtOrgBillPage, edtCashVoucherPage, edtOuterOuchPage, edtAnyOtherPage;
    private SaveReimbursementDetails saveReimbursementDetails;

    private CheckBox agreeCheckbox,cbSecC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_reimbursement);
        

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Note")
//                .setMessage("a)"+getString(R.string.note_medi_claim)+"\nb) Treatment taken under Emergency would only be submitted here.\nc) Keep your salary bank account detail ready for cross verifications on form\n")
                .setMessage("a)"+getString(R.string.note_medi_claim)+"\n\nb) "+getString(R.string.treatment_taken_emrgency)+"\n\nc) "+getString(R.string.slary_details)+"\n")
                .setNegativeButton("OK", null);
        alert.show();

        saveReimbursementDetails = new SaveReimbursementDetails();
        initializeViews();
    }

    private void initializeViews() {
        msd = new ManagingSharedData(this);
        saveReimbursementDetails.setCrno(msd.getPatientDetails().getCrno());
//        saveReimbursementDetails.setUmid(msd.getPatientDetails().getUmidData().getUmidNo());

        scrollView = findViewById(R.id.sscrollview_main);
        TextView tvcrno = findViewById(R.id.cr);
        TextView tvName = findViewById(R.id.name);
        tvcrno.setText(msd.getPatientDetails().getCrno());
        tvName.setText(msd.getPatientDetails().getFirstname());
        llSections = new LinearLayout[3];
        llSections[0] = findViewById(R.id.ll_section_a);
        llSections[1] = findViewById(R.id.ll_section_b);
        llSections[2] = findViewById(R.id.ll_section_c);

        llSections[1].setVisibility(View.GONE);
        llSections[2].setVisibility(View.GONE);

        initializeSectionAViews();
        initializeSectionBViews();
        initializeSectionCViews();

        edtAdmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(ADM_DATE_TAG);
            }
        });
        edtDischDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(DISCH_DATE_TAG);
            }
        });
    }

        private void initializeSectionAViews() {
        edthospName = findViewById(R.id.edt_hosp_name);
        edtAdmDate = findViewById(R.id.edt_adm_date);
        edtDischDate = findViewById(R.id.edt_disch_date);
        edtDiagnosis = findViewById(R.id.edt_diagnosis);
        edthospBill = findViewById(R.id.edt_total_hosp_bill);
        edtAmtClaimed = findViewById(R.id.edt_claimed_bill);


        btnSecaNext = findViewById(R.id.btn_next_sec_a);

        //   String[] COUNTRIES = new String[]{"IPD Admission", "Diagnostic Test", "OPD Consultation", "Clinical Procedures"};

        ArrayList<ReimbursementType> reimbursementTypeArrayList = new ArrayList<>();
        reimbursementTypeArrayList.add(new ReimbursementType("1", "IPD Admission"));
        reimbursementTypeArrayList.add(new ReimbursementType("2", "Diagnostic Test"));
        reimbursementTypeArrayList.add(new ReimbursementType("3", "OPD Consultation"));
        reimbursementTypeArrayList.add(new ReimbursementType("4", "Clinical Procedures"));
        ArrayAdapter<ReimbursementType> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, reimbursementTypeArrayList);

        tvReimbursementType = findViewById(R.id.tv_reimburesement_type);
        tvReimbursementType.setAdapter(adapter);

        tvReimbursementType.setText(tvReimbursementType.getAdapter().getItem(0).toString(), false);
        saveReimbursementDetails.setReimbursementType("1");

        tvReimbursementType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReimbursementType reimbursementType = (ReimbursementType) tvReimbursementType.getAdapter().getItem(position);

                saveReimbursementDetails.setReimbursementType(reimbursementType.getId());
            }
        });


        btnSecaNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSectionAValid()) {
                    saveReimbursementDetails.setInstituteName(edthospName.getText().toString().trim());
                    saveReimbursementDetails.setAdmssionDate(edtAdmDate.getText().toString().trim());
                    saveReimbursementDetails.setDischargeDate(edtDischDate.getText().toString().trim());
                    saveReimbursementDetails.setDiagnosis(edtDiagnosis.getText().toString());
                    saveReimbursementDetails.setDiagnosisDtl(edtDiagnosis.getText().toString());
                    saveReimbursementDetails.setBilledAmount(edthospBill.getText().toString());
                    saveReimbursementDetails.setClaimedAmount(edtAmtClaimed.getText().toString());

                    llSections[0].setVisibility(View.GONE);
                    llSections[2].setVisibility(View.GONE);
                    llSections[1].setVisibility(View.VISIBLE);
                }
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    private void initializeSectionBViews() {
        yesNoArrayList = new ArrayList<>();
        // yesNoArrayList.add("Select");
        yesNoArrayList.add(getString(R.string.yes));
        yesNoArrayList.add(getString(R.string.no));

        tvctsemember = findViewById(R.id.tv_ctse);
        tvIsEmergency = findViewById(R.id.tv_emergency);
        tv6months = findViewById(R.id.tv_6months);
        tvHealthScheme = findViewById(R.id.tv_health_scheme);
        tvInsurance = findViewById(R.id.tv_insurance);

        llInsurance = findViewById(R.id.ll_insurance);
        tvEmergencyError = findViewById(R.id.tv_emergency_error);
        tv6MonthsError = findViewById(R.id.tv_6months_error);
        agreeCheckbox = findViewById(R.id.checkbox_agree);

        tvctsemember.setAdapter(new ArrayAdapter(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yesNoArrayList));
        tvIsEmergency.setAdapter(new ArrayAdapter(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yesNoArrayList));
        tv6months.setAdapter(new ArrayAdapter(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yesNoArrayList));
        tvHealthScheme.setAdapter(new ArrayAdapter(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yesNoArrayList));
        tvInsurance.setAdapter(new ArrayAdapter(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yesNoArrayList));


//        tvctsemember.setText(tvctsemember.getAdapter().getItem(0).toString(), false);
//        tvIsEmergency.setText(tvIsEmergency.getAdapter().getItem(0).toString(), false);
//        tv6months.setText(tv6months.getAdapter().getItem(0).toString(), false);
//        tvHealthScheme.setText(tvHealthScheme.getAdapter().getItem(0).toString(), false);
//        tvInsurance.setText(tvInsurance.getAdapter().getItem(0).toString(), false);

        getDropdownListner();


        bSecbBack = findViewById(R.id.btn_back_sec_b);
        btnSecbNext = findViewById(R.id.btn_next_sec_b);
        isSectionBValid();
        btnSecbNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSectionBValid()) {

                    saveReimbursementDetails.setIsAccidentalVerified("1");
                    llSections[0].setVisibility(View.GONE);
                    llSections[1].setVisibility(View.GONE);
                    llSections[2].setVisibility(View.VISIBLE);

                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            }
        });
        bSecbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSections[0].setVisibility(View.VISIBLE);
                llSections[1].setVisibility(View.GONE);
                llSections[2].setVisibility(View.GONE);

                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void getDropdownListner() {
        tvctsemember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isCtse = tvctsemember.getAdapter().getItem(position).toString();

                saveReimbursementDetails.setIsCtse((isCtse.equalsIgnoreCase("Yes") ? "1" : "0"));
                isSectionBValid();
            }
        });
        tvIsEmergency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isEmergency = tvIsEmergency.getAdapter().getItem(position).toString();
                if (isEmergency.equalsIgnoreCase("Yes")) {
                    tvEmergencyError.setVisibility(View.GONE);
                    saveReimbursementDetails.setIsEmergency("1");
                } else {
                    tvEmergencyError.setVisibility(View.VISIBLE);
                    saveReimbursementDetails.setIsEmergency("0");
                }
                isSectionBValid();

            }


        });
        tv6months.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isClaimOlder = tv6months.getAdapter().getItem(position).toString();
                if (isClaimOlder.equalsIgnoreCase("Yes")) {
                    tv6MonthsError.setVisibility(View.GONE);
                    saveReimbursementDetails.setIsOldClaim("1");
                } else {
                    tv6MonthsError.setVisibility(View.VISIBLE);
                    saveReimbursementDetails.setIsOldClaim("0");
                }
                isSectionBValid();
            }
        });

        tvHealthScheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isHealthScheme = tvHealthScheme.getAdapter().getItem(position).toString();
                if (isHealthScheme.equalsIgnoreCase("Yes")) {
                    llInsurance.setVisibility(View.VISIBLE);
                    saveReimbursementDetails.setIsHavingInsurance("1");
                } else {
                    llInsurance.setVisibility(View.GONE);
                    saveReimbursementDetails.setIsHavingInsurance("0");
                }
                isSectionBValid();

            }
        });
        tvInsurance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isInsuranceClaimed = tvInsurance.getAdapter().getItem(position).toString();
                saveReimbursementDetails.setIsInsuranceClaimed((isInsuranceClaimed.equalsIgnoreCase("Yes") ? "1" : "0"));
                isSectionBValid();
            }
        });

        agreeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSectionBValid();
            }
        });

    }
    private void initializeSectionCViews() {
        initializeHospSpinners();
        cSecbBack = findViewById(R.id.btn_back_sec_c);
        btnSeccNext = findViewById(R.id.btn_next_sec_c);
        edtDischSummaryPage = findViewById(R.id.edt_disch_page);
        edtOrgBillPage = findViewById(R.id.edt_bill_page);
        edtCashVoucherPage = findViewById(R.id.edt_voucher_page);
        edtOuterOuchPage = findViewById(R.id.edt_stent_page);
        edtAnyOtherPage = findViewById(R.id.edt_other_enclosure_page);
        cbSecC = findViewById(R.id.cb_sec_c);
        btnSeccNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSectionCValid()) {
                    saveReimbursementDetails.setDischargeSummaryPno(edtDischSummaryPage.getText().toString().trim());
                    saveReimbursementDetails.setBillSummaryPno(edtOrgBillPage.getText().toString().trim());
                    saveReimbursementDetails.setBillSummaryPno(edtOrgBillPage.getText().toString().trim());
                    saveReimbursementDetails.setCashVoucherPno(edtCashVoucherPage.getText().toString().trim());
                    saveReimbursementDetails.setOuterPouchPno(edtOuterOuchPage.getText().toString().trim());
                    saveReimbursementDetails.setOtherEnclosePno(edtAnyOtherPage.getText().toString().trim());

                    submitClaim();
                }
            }
        });
        cSecbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSections[0].setVisibility(View.GONE);
                llSections[1].setVisibility(View.VISIBLE);
                llSections[2].setVisibility(View.GONE);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private boolean isSectionAValid() {
        if (TextUtils.isEmpty(edthospName.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.enter_name_of_hospital));
            return false;
        }
        if (TextUtils.isEmpty(edtAdmDate.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.enter_name_of_admission));
            return false;
        }
        if (TextUtils.isEmpty(edtDischDate.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.enter_dischatge_date));
            return false;
        }
        if (TextUtils.isEmpty(edtDiagnosis.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.enter_diagonsis));
            return false;
        }
        if (TextUtils.isEmpty(edthospBill.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.enter_hosp_bill_amt));
            return false;
        }
        if (TextUtils.isEmpty(edtAmtClaimed.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.enter_total_claim_amt));
            return false;
        }


        return true;
    }

    private boolean isSectionBValid() {
        btnSecbNext.setVisibility(View.GONE);
        if (!tvctsemember.getText().toString().equalsIgnoreCase(getString(R.string.yes))) {
            return false;
        }
        if (!tvIsEmergency.getText().toString().equalsIgnoreCase(getString(R.string.yes))) {
            return false;
        }

        if (!tv6months.getText().toString().equalsIgnoreCase(getString(R.string.yes))) {
            return false;
        }
        if (!tvHealthScheme.getText().toString().equalsIgnoreCase(getString(R.string.yes))) {
            return false;
        }
        if (!tvInsurance.getText().toString().equalsIgnoreCase(getString(R.string.yes))) {
            return false;
        }
        if (!agreeCheckbox.isChecked()) {
            return false;
        }
        btnSecbNext.setVisibility(View.VISIBLE);
        return true;
    }

    private boolean isSectionCValid() {
        if (!cbSecC.isChecked())
        {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.select_checkbox));
            return false;
        }
        if (hospCode.equalsIgnoreCase("-1")) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.select_hospital));
            return false;
        }
        if (TextUtils.isEmpty(edtDischSummaryPage.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.discharge_summray));
            return false;
        }
        if (TextUtils.isEmpty(edtOrgBillPage.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.origina_bill_page_number));
            return false;
        }
        if (TextUtils.isEmpty(edtCashVoucherPage.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.cash_boucher_page_number));
            return false;
        }
        if (TextUtils.isEmpty(edtOuterOuchPage.getText().toString())) {
            AppUtilityFunctions.showErrorDialog(this, getString(R.string.outer_pouch_number));
            return false;
        }
//        if (TextUtils.isEmpty(edtAnyOtherPage.getText().toString())) {
//            AppUtilityFunctions.showErrorDialog(this, "Please enter any other Page No");
//            return false;
//        }

        return true;
    }


    private void showDate(String tag) {

        Calendar calendar = Calendar.getInstance();

        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                SaveReimbursementActivity.this,
                Year, // Initial year selection
                Month, // Initial month selection
                Day // Inital day selection
        );
        //datePickerDialog.setMinDate(calendar);


        // Setting Min Date to today date
        Calendar min_date_c = Calendar.getInstance();
        //datePickerDialog.setMinDate(min_date_c);


        // Setting Max Date to next 2 years
        //  Calendar max_date_c = Calendar.getInstance();
        //  max_date_c.set(Calendar.YEAR, Year + 2);
        datePickerDialog.setMaxDate(min_date_c);


        datePickerDialog.setOnCancelListener(dialogInterface -> {
            if (tag.equalsIgnoreCase(ADM_DATE_TAG))
                edtAdmDate.getText().clear();
            else if (tag.equalsIgnoreCase(DISCH_DATE_TAG))
                edtDischDate.getText().clear();
        });

        datePickerDialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String dateString = String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year);
        Log.i("date", "onDateSet: " + dateString);


        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate = newFormat.format(date);
        Log.i("final string", "onDateSet: " + strDate);

        if (view.getTag().equalsIgnoreCase(ADM_DATE_TAG)) {
            edtAdmDate.setText(strDate);
        } else if (view.getTag().equalsIgnoreCase(DISCH_DATE_TAG)) {
            edtDischDate.setText(strDate);
        }

    }

    private void initializeHospSpinners() {
        zoneSpinner = findViewById(R.id.zone_spinner);
        zoneSpinnerView = findViewById(R.id.zone_spinner_view);

        divisionSpinner = findViewById(R.id.division_spinner);
        divisionSpinnerView = findViewById(R.id.division_spinner_view);

        hospSpinner = findViewById(R.id.hosp_spinner);
        hospSpinnerView = findViewById(R.id.hosp_spinner_view);


        getZoneList();


        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                ZoneDetails zoneDetails = (ZoneDetails) zoneSpinner.getSelectedItem();
                zoneId = zoneDetails.getZoneId();
                hospCode = "-1";
                hospName = "";
                if (zoneId.equalsIgnoreCase("-1")) {
                    divisionSpinner.setVisibility(View.GONE);
                    divisionSpinnerView.setVisibility(View.GONE);
                    hospSpinner.setVisibility(View.GONE);
                    hospSpinnerView.setVisibility(View.GONE);

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
                hospCode = "-1";
                hospName = "";
                divisionId = divisionDetails.getDivisionId();
                if (divisionId.equalsIgnoreCase("-1")) {
                    hospSpinner.setVisibility(View.GONE);
                    hospSpinnerView.setVisibility(View.GONE);

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
                HospitalDetails hospitalDetails = (HospitalDetails) hospSpinner.getSelectedItem();

                String hosCode = hospitalDetails.getHospCode();

                if (!hosCode.equalsIgnoreCase("-1")) {
                    hospCode = hosCode;
                    hospName = hospitalDetails.getHospName();
                } else {
                    hospName = "";
                    Toast.makeText(SaveReimbursementActivity.this, getString(R.string.select_hospital), Toast.LENGTH_SHORT).show();
                }

            }


            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });
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
                        ArrayAdapter adapter = new ArrayAdapter(SaveReimbursementActivity.this, R.layout.new_patient_custom_layout, zoneArrayList);
                        zoneSpinner.setAdapter(adapter);

//    zoneSpinner.setSelection(setDefaultZone());


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
                AppUtilityFunctions.handleExceptions(error, SaveReimbursementActivity.this);
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
                    ArrayAdapter adapter = new ArrayAdapter(SaveReimbursementActivity.this, R.layout.new_patient_custom_layout, divisionArrayList);
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
                AppUtilityFunctions.handleExceptions(error, SaveReimbursementActivity.this);
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
        final ArrayList<HospitalDetails> hospitalDetailsArrayList = new ArrayList<HospitalDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.divisionAndZoneWiseHospitalList + "?zoneId=" + zoneId + "&divisionId=" + divisionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                    hospSpinner.setAdapter(new ArrayAdapter(SaveReimbursementActivity.this, R.layout.new_patient_custom_layout, hospitalDetailsArrayList));


                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, SaveReimbursementActivity.this);
//                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void submitClaim() {
        JSONObject jsonbObject = new JSONObject();

        try {
            jsonbObject.put("crno", msd.getPatientDetails().getCrno());

//            jsonbObject.put("umid", msd.getPatientDetails().getUmidData().getUmidNo());
            jsonbObject.put("treatmentDtl", "");
            jsonbObject.put("instituteCode", null);
            jsonbObject.put("instituteName", saveReimbursementDetails.getInstituteName());
            jsonbObject.put("claimNo", "1");
            jsonbObject.put("reqSlNo", "2");
            jsonbObject.put("RefNo", null);
            jsonbObject.put("lstModDate", null);
            jsonbObject.put("status", "0");
            jsonbObject.put("submitToHospCode", hospCode);
            jsonbObject.put("reimbursementType", saveReimbursementDetails.getReimbursementType());
            jsonbObject.put("remarks", null);
            jsonbObject.put("diagnosis", saveReimbursementDetails.getDiagnosis());
            jsonbObject.put("settlementDate", null);
            jsonbObject.put("rejectionDate", null);
            jsonbObject.put("claimRejectionDate", null);
            jsonbObject.put("claimRevocationDate", null);
            jsonbObject.put("approvedBy", null);
            jsonbObject.put("billedAmount", saveReimbursementDetails.getBilledAmount());
            jsonbObject.put("claimedAmount", saveReimbursementDetails.getClaimedAmount());
            jsonbObject.put("billNo", null);
            jsonbObject.put("admssionDate", saveReimbursementDetails.getAdmssionDate());
            jsonbObject.put("dischargeDate", saveReimbursementDetails.getDischargeDate());
            jsonbObject.put("completionDate", null);
            jsonbObject.put("diagnosisDtl", saveReimbursementDetails.getDiagnosis());
            //section b
            jsonbObject.put("isCtse", saveReimbursementDetails.getIsCtse());
            jsonbObject.put("isClaimResubmitted", "0");
            jsonbObject.put("isEmergency", saveReimbursementDetails.getIsEmergency());
            jsonbObject.put("isOldClaim", saveReimbursementDetails.getIsOldClaim());
            jsonbObject.put("isHavingInsurance", saveReimbursementDetails.getIsHavingInsurance());
            jsonbObject.put("isInsuranceClaimed", saveReimbursementDetails.getIsInsuranceClaimed());
            jsonbObject.put("isAccidentalVerified", saveReimbursementDetails.getIsAccidentalVerified());
            //section c
            jsonbObject.put("dischargeSummaryPno", saveReimbursementDetails.getDischargeSummaryPno());
            jsonbObject.put("billSummaryPno", saveReimbursementDetails.getBillSummaryPno());
            jsonbObject.put("cashVoucherPno", saveReimbursementDetails.getCashVoucherPno());
            jsonbObject.put("outerEnclosePno", "");
            jsonbObject.put("otherEnclosePno", saveReimbursementDetails.getOtherEnclosePno());
            jsonbObject.put("outerPouchPno", saveReimbursementDetails.getOuterPouchPno());
            jsonbObject.put("seatId", "10001");
            jsonbObject.put("lastModSeatId", "10001");
            jsonbObject.put("empty3", "");
            jsonbObject.put("empty4", "");
            jsonbObject.put("empty5", "");
            jsonbObject.put("gnum_sanctioned_amt", "0");
            jsonbObject.put("gnum_forward_to_user", "0");
            Log.i("TAG", "submitClaim: " + jsonbObject.toString());

            saveClaim(jsonbObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveClaim(JSONObject requestbody) {


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl + "railtelService/saveReimbursement?modeval=1", requestbody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                AlertDialog dialog = new MaterialAlertDialogBuilder(SaveReimbursementActivity.this).setTitle("Claim Submitted").setMessage("Claim Submitted Successfully").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                            } else {
                                Toast.makeText(SaveReimbursementActivity.this, "Unable to submit claim.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
//        requestQueue.add(jsObjRequest);

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
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
}