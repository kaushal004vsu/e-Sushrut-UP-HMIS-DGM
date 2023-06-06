package com.cdac.uphmis.labBasedAppointment;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.lpstatus.LPStatusActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class LabBasedAppointmentActivity extends AppCompatActivity {
    GeometricProgressView progressView;
    private ListView lvDepartments;
    private TextView tvNoRecordsFound;
    TextView name,cr;
    private ManagingSharedData msd;
    private void initVar() {
        progressView = (GeometricProgressView) findViewById(R.id.progress_view);
        tvNoRecordsFound = findViewById(R.id.tv_no_department_found);
        lvDepartments = findViewById(R.id.department_list);
        name = findViewById(R.id.name);
        cr = findViewById(R.id.cr);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_based_appointment);
        
        initVar();
        Toolbar toolbar =  findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        msd = new ManagingSharedData(LabBasedAppointmentActivity.this);

        name.setText(msd.getPatientDetails().getFirstname());
        cr.setText(msd.getPatientDetails().getCrno());
        getAllDepartments(msd.getPatientDetails().getCrno());
    }

    public void getAllDepartments(String crNo) {
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<LabBasedAppointmentDetails> departmentArrayList = new ArrayList();
//        String url= ServiceUrl.getLabBased+ crNo;
        String url= ServiceUrl.getLabBased+ crNo;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);

                Log.i("response is ", "onResponse: " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray deptList = jsonObject.getJSONArray("data");

                    if (deptList.length() == 0) {
                        Toast.makeText(LabBasedAppointmentActivity.this, "No departments found.", Toast.LENGTH_SHORT).show();
                        lvDepartments.setVisibility(View.GONE);
                        tvNoRecordsFound.setVisibility(View.VISIBLE);
                    } else {

                        for (int i = 0; i < deptList.length(); i++) {
                            JSONObject c = deptList.getJSONObject(i);
                            String testname = c.getString("TESTNAME");
                            String labname = c.getString("LABNAME");
                            String reqdate = c.getString("REQDATE");
                            String reqdno = c.getString("REQDNO");
                            String testcode = c.getString("TESTCODE");
                            String labcode = c.getString("LABCODE");
                            String deptname = c.getString("DEPTNAME");
                            String unitname = c.getString("UNITNAME");
                            String deptcode = c.getString("DEPTCODE");
                            String unitcode = c.getString("UNITCODE");
                            String genderCode = c.getString("GENDER_CODE");
                            String crno = c.getString("CRNO");
                            String patname = c.getString("PATNAME");
                            String episodecode = c.getString("EPISODECODE");
                            String age = c.getString("AGE").toString().trim();
                            String actualpararefid = c.getString("ACTUALPARAREFID").toString().trim();
                            String preffereddate = c.getString("PREFFEREDDATE").toString().trim();
                            String hospCode = c.getString("HOSP_CODE").toString().trim();
                            String hospName = c.getString("HOSP_NAME").toString().trim();
                            departmentArrayList.add(new LabBasedAppointmentDetails(testname, labname, reqdate, reqdno, testcode, labcode, deptname, unitname, deptcode,
                                    unitcode, genderCode, crno, patname, episodecode, age, actualpararefid, preffereddate,hospCode,hospName));


                        }


                        LabBasedAppointmentAdapter appointmentDepartmentAdapter = new LabBasedAppointmentAdapter(LabBasedAppointmentActivity.this, departmentArrayList);
                        lvDepartments.setAdapter(appointmentDepartmentAdapter);
                        if (departmentArrayList.size()==0)
                        {
                            lvDepartments.setVisibility(View.GONE);
                            tvNoRecordsFound.setVisibility(View.VISIBLE);
                        }else
                        {
                            lvDepartments.setVisibility(View.VISIBLE);
                            tvNoRecordsFound.setVisibility(View.GONE);

                        }

                    }
                } catch (final JSONException e) {
                    progressView.setVisibility(View.GONE);
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                if (LabBasedAppointmentActivity.this != null) {
                    progressView.setVisibility(View.GONE);
                    AppUtilityFunctions.handleExceptions(error, LabBasedAppointmentActivity.this);
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