package com.cdac.uphmis.precriptionView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.covid19.adapter.MyRecycleViewClickListener;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.precriptionView.adapter.LabReportListAdapter;
import com.cdac.uphmis.precriptionView.adapter.PrescriptionAdapter;
import com.cdac.uphmis.precriptionView.model.LabReportListDetails;
import com.cdac.uphmis.precriptionView.model.PrescriptionListDetails;
import com.cdac.uphmis.tracker.InvestigationDetail;
import com.cdac.uphmis.tracker.OrderStatusModel;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.SessionServicecall;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class PrescriptionListActivity extends AppCompatActivity {
    private PrescriptionAdapter prescriptionAdapter;
    private LabReportListAdapter reportAdapter;

    private GeometricProgressView progressView;
    private ManagingSharedData msd;
    private LinearLayout llNoResultFound;

    Integer toShow=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);

        msd = new ManagingSharedData(PrescriptionListActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            Integer select = intent.getIntExtra("list", 1);
            switch (select) {
                case 1: {
                    getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
                    toShow=1;
                    getPrescripitonListData(msd.getCrNo());
                    break;
                }
                case 2: {
                    getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
                    toShow=2;
                    getLabReportlistData(msd.getCrNo());
                    break;
                }
            }


        }


    }

    private void getLabReportlistData(String crno) {
        progressView.setVisibility(View.VISIBLE);
        List<InvestigationDetail> reportList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.investigationTracker + msd.getPatientDetails().getHospitalCode() + "&crno="+crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response", "onResponse: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("INVESTIGATION_DETAILS");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            String testname = c.optString("TESTNAME");
                            String reqdate = c.optString("HIVTNUM_REQ_DNO");

                            String reportDate = c.optString("REQDATE");
                            String hospitalCode = c.optString("HOSPITALCODE");
                            String hospName = c.optString("HOSPITAL_NAME");
                            String status_no = c.optString("STATUS_NO");
                            String status_covered = c.optString("STATUS");

                            String srNo = c.optString("SR_NO");
                            String deptName = c.optString("DEPT_NAME");

                            String testResults = c.optString("TEST_RESULTS");
                            String hivtnumReqDno = c.optString("HIVTNUM_REQ_DNO");
                            String samplenoLabno = c.optString("SAMPLENO_LABNO");
                            String labname = c.optString("LABNAME");
                            String sampleCollectionDate = c.optString("SAMPLE_COLLECTION_DATE");
                            String patname = c.optString("PATNAME");
                            String patcrno = c.optString("PATCRNO");
                            String packingListDate = c.optString("PACKING_LIST_DATE");
                            String requisitionDate = c.optString("REQUISITION_DATE");
                            String resultEntryDate = c.optString("RESULT_ENTRY_DATE");
                            String resultValidationDate = c.optString("RESULT_VALIDATION_DATE");
                            String reportGenerationDate = c.optString("REPORT_GENERATION_DATE");
                            String accpetanceDate = c.optString("ACCPETANCE_DATE");
                            String gnumSampleCode = c.optString("GNUM_SAMPLE_CODE");
                            String reportPrintDate = c.optString("REPORT_PRINT_DATE");

//                            reportList.add(new LabReportListDetails(testName, reqDno, reportDate,hospitalCode,hospName,status_no,status_covered));
                            reportList.add(new InvestigationDetail(srNo, reqdate, deptName, testname, status_covered, testResults,
                                    hivtnumReqDno, samplenoLabno, labname, sampleCollectionDate, patname, patcrno,
                                    packingListDate, requisitionDate, resultEntryDate, resultValidationDate,
                                    reportGenerationDate, accpetanceDate, gnumSampleCode, reportPrintDate,reportDate,hospitalCode,hospName,status_no
                            ));
                        }

                    }
                    setUpRecyclerViewForReport(reportList);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    llNoResultFound.setVisibility(View.VISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, PrescriptionListActivity.this);
                Log.e("error", "onErrorResponse: ", error);
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void getPrescripitonListData(String crno) {

        progressView.setVisibility(View.VISIBLE);
        List<PrescriptionListDetails> prescripionList = new ArrayList<>();

        Log.i("prescriptionlisturl", "getPrescripitonListData: " + ServiceUrl.PrescriptionListUrl + "crno=" + crno + "&hosCode=0");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.PrescriptionListUrl + "crno=" + crno + "&hosCode=0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response", "onResponse: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("pat_details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String crno = c.optString("HRGNUM_PUK");
                            String episodeCode = c.optString("HRGNUM_EPISODE_CODE");
                            String deptCode = c.optString("GNUM_DEPT_CODE");
                            String visitDate = c.optString("HRGDT_VISIT_DATE");
                            String hospitalCode = c.optString("GNUM_HOSPITAL_CODE");
                            String patientName = c.optString("HRGSTR_PATIENT_NAME");
                            String deptName = c.optString("GSTR_DEPT_NAME");
                            String unitName = c.optString("HGSTR_UNITNAME");
                            String visitNo = c.optString("HRGNUM_VISIT_NO");
                            String patientAge = c.optString("HRGNUM_PATIENT_AGE");
                            String genderCode = c.optString("GSTR_GENDER_CODE");
                            String hospName = c.optString("HOSP_NAME");
                            String entryDate = c.optString("ENTRY_DATE");

                            prescripionList.add(new PrescriptionListDetails(crno, episodeCode, deptCode, visitDate, hospitalCode, patientName, deptName, unitName, visitNo, patientAge, genderCode, hospName,entryDate));
                        }

                    }

                    setUpRecyclerViewForPrescription(prescripionList);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    llNoResultFound.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, PrescriptionListActivity.this);
                Log.e("error", "onErrorResponse: ", error);
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);


    }

    private void setUpRecyclerViewForPrescription(List<PrescriptionListDetails> prescripionList) {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        androidx.core.view.ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        prescriptionAdapter=null;
        prescriptionAdapter = new PrescriptionAdapter(prescripionList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(prescriptionAdapter);

        if (prescripionList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            llNoResultFound.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            llNoResultFound.setVisibility(View.VISIBLE);
        }

        recyclerView.addOnItemTouchListener(new MyRecycleViewClickListener(this, new MyRecycleViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    PrescriptionListDetails prescriptionListDetails = prescripionList.get(position);
                    Log.i("recyclerviewclick", "onItemClick: " + prescriptionListDetails.getDeptName());
                    getPastWebPrescription(prescriptionListDetails, progressView);
                    try {
                        SessionServicecall sessionServicecall = new SessionServicecall(PrescriptionListActivity.this);
                        sessionServicecall.saveSession(prescriptionListDetails.getCrno(),(msd.getPatientDetails().getMobileNo()==null)?"":msd.getPatientDetails().getMobileNo(), prescriptionListDetails.getHospCode(), "Prescription View", "", "", "", "");
                    }catch(Exception ex){}

                }catch(Exception ex){ex.printStackTrace();}
            }
        }));
    }

    private void setUpRecyclerViewForReport(List<InvestigationDetail> labReportListDetailsList) {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reportAdapter=null;
        reportAdapter = new LabReportListAdapter(this, labReportListDetailsList, new LabReportListAdapter.OnReportClick() {
            @Override
            public void OnReportClick(int position) {
                InvestigationDetail labReportListDetails = (InvestigationDetail) labReportListDetailsList.get(position);
                Log.i("recyclerviewclick", "onItemClick: " + labReportListDetails.getTestname());
                downloadReport(msd.getCrNo(), labReportListDetails.getReqdate(), labReportListDetails.getHospitalCode());
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(reportAdapter);


        if (labReportListDetailsList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            llNoResultFound.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            llNoResultFound.setVisibility(View.VISIBLE);
        }




       /* recyclerView.addOnItemTouchListener(new MyRecycleViewClickListener(this, new MyRecycleViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    InvestigationDetail labReportListDetails = (InvestigationDetail) labReportListDetailsList.get(position);
                    Log.i("recyclerviewclick", "onItemClick: " + labReportListDetails.getTestname());
                    downloadReport(msd.getCrNo(), labReportListDetails.getReqdate(), labReportListDetails.getHospitalCode());
                }catch(Exception ex){ex.printStackTrace();}
            }

        }));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (prescriptionAdapter != null) {
                    prescriptionAdapter.getFilter().filter(newText);
                } else if (reportAdapter != null) {
                    reportAdapter.getFilter().filter(newText);
                }


                return false;
            }
        });
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getPastWebPrescription(PrescriptionListDetails patientDetails, GeometricProgressView progressBar) {
        progressView.setVisibility(View.VISIBLE);
        //        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.i("TAG", "getPastPrescription: " + ServiceUrl.getPastWebPrescription+"hosp_code=" + patientDetails.getHospCode() + "&Modval=5&CrNo=" + patientDetails.getCrno() + "&episodeCode=" + patientDetails.getEpisodeCode() + "&visitNo=" + patientDetails.getVisitNo() + "&seatId=0&Entrydate=%22%22");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPastWebPrescription + "hosp_code=" + patientDetails.getHospCode() + "&Modval=5&CrNo=" + patientDetails.getCrno() + "&episodeCode=" + patientDetails.getEpisodeCode() + "&visitNo=" + patientDetails.getVisitNo() + "&seatId=0&Entrydate="+patientDetails.getEntryDate(), response -> {
            progressView.setVisibility(View.GONE);
            Log.i("response is ", "onResponse: " + response);
            try {

                if (response.length() <= 15) {
                    Toast.makeText(PrescriptionListActivity.this, "Prescription not found for this visit.", Toast.LENGTH_SHORT).show();
                } else {
                    saveBase64Pdf(PrescriptionListActivity.this, "prescription", "prescription", response);
                }
                progressBar.setVisibility(View.GONE);
            } catch (final Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                progressBar.setVisibility(View.GONE);
            }


        }, error -> {
            progressView.setVisibility(View.GONE);
            Log.i("error", "onErrorResponse: " + error);
//            Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, PrescriptionListActivity.this);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(PrescriptionListActivity.this).addToRequestQueue(request);
    }


    public void downloadReport(String crno, String reqDno, String hospCode) {
        progressView.setVisibility(View.VISIBLE);

//        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getReportpdf + msd.getCrNo() + "&reqDNo=" + reqDno + "&hosCode=" + ServiceUrl.hospId, new Response.Listener<String>() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getReportpdf + crno + "&reqDNo=" + reqDno + "&hosCode=" + hospCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);
                JSONArray jsonObj;
                try {
                    jsonObj = new JSONArray(response);

                    for (int i = 0; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        String data = c.getString("PDFDATA");

                        saveBase64Pdf(PrescriptionListActivity.this, "myReports", reqDno, data);
                        try {
                            SessionServicecall sessionServicecall = new SessionServicecall(PrescriptionListActivity.this);
                            sessionServicecall.saveSession(msd.getCrNo(), msd.getPatientDetails().getMobileNo(),hospCode, "Lab Report View", "", "", "", "");
                        }catch(Exception ex){}
                    }
                } catch (JSONException e) {
                    Toast.makeText(PrescriptionListActivity.this, "Could not fetch report.Try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, error -> {
            progressView.setVisibility(View.GONE);
//                Snackbar.make(progressView, "Unable to connect", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(PrescriptionListActivity.this, "Unable to connect", Toast.LENGTH_SHORT).show();
            Log.i("error", "onErrorResponse: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(PrescriptionListActivity.this).addToRequestQueue(request);


    }


}