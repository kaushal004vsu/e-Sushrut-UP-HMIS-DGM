package com.cdac.uphmis.QMSSlip;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.QMSSlip.model.LiveqnoDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveQnoStatus extends AppCompatActivity {
   // String data="";
private TextView tvCurrentQno,tvHospitalName,tvDepartmentName,tvWaitingTime;

private Spinner spDepartment;

private    ManagingSharedData msd;
private GeometricProgressView progressView;
//    CountDownTimer ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_qno_status);
        
         tvCurrentQno=findViewById(R.id.tv_current_qno);
        tvHospitalName=findViewById(R.id.tv_hospital_name);
        tvDepartmentName=findViewById(R.id.tv_dept_name);
        tvWaitingTime=findViewById(R.id.tv_waiting_time);
        progressView=findViewById(R.id.progress_view);
        progressView.setColor(ContextCompat.getColor(this,R.color.white));

        spDepartment=findViewById(R.id.sp_department);
         msd=new ManagingSharedData(this);

         callQnoService(msd.getPatientDetails().getCrno());


         spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(LiveQnoStatus.this,R.color.vitals_blue));
                 ((TextView) parent.getChildAt(0)).setTextSize(16);
                 LiveqnoDetails liveqnoDetails =  (LiveqnoDetails) spDepartment.getSelectedItem();
                 tvCurrentQno.setText(liveqnoDetails.getQueueNo());
                 tvHospitalName.setText(liveqnoDetails.getHospitalName());
                 tvDepartmentName.setText(liveqnoDetails.getDeptName()+" ("+liveqnoDetails.getUnitName()+")");
                 tvWaitingTime.setText("Average per person waiting time is : "+liveqnoDetails.getWaitingTime()+" minute(s).");
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });





    }
/*    public void refreshAllContent(final long timetoupdate) {

       ct=  new CountDownTimer(timetoupdate, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                callQnoService(msd.getPatientDetails().getCrno());
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "onDestroy: ");
        if (ct!=null) {
            ct.cancel();
            ct = null;
        }
        //ct.onFinish();

    }*/

    private void callQnoService(String crno)
    {
progressView.setVisibility(View.VISIBLE);

        ArrayList<LiveqnoDetails> liveqnoDetailsArrayList=new ArrayList<>();
        Log.i("TAG", "callQnoService: "+ServiceUrl.testurl+"genericAppointment/getQNoStatus/3?hospCode=0&patCrNo="+crno);
        StringRequest request= new StringRequest(Request.Method.GET, ServiceUrl.testurl+"genericAppointment/getQNoStatus/3?hospCode=0&patCrNo="+crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("reposne", "onResponse: "+response);
                try {


                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String data = jsonObject.getString("qnodata");

                    if (data.trim().isEmpty())
                    {
                        //AlertDialog dialog=AppUtilityFunctions.showErrorDialog(LiveQnoStatus.this,"It seems you don't have any visit today.");

//                        dialog.dismiss();
                        finish();
                    }else {


                        Log.i("data", "onResponse: " + data);
                        JSONArray jsonArray1 = new JSONArray(data);

                        if (jsonArray1.length() > 1) {
                            spDepartment.setVisibility(View.VISIBLE);
                        }
                        if (jsonArray1.length()==0)
                        {
                            finish();
                            return;
                        }
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject dataObject = jsonArray1.getJSONObject(i);

                            String hospitalName = dataObject.getString("hosp_name");
                            String deptName = dataObject.getString("dept_name");
                            String unitName = dataObject.getString("unit_name");
                            String queueNo = dataObject.getString("hrgnum_que_no");
                            String waitingTime = dataObject.getString("per_person_waiting_time_mins");

                            tvCurrentQno.setText(queueNo);
                            tvHospitalName.setText(hospitalName);
                            tvDepartmentName.setText(deptName + " (" + unitName + ")");
                            tvWaitingTime.setText("Average per person waiting time is : " + waitingTime + " minute(s).");

                            liveqnoDetailsArrayList.add(new LiveqnoDetails(hospitalName, deptName, unitName, queueNo, waitingTime));


                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(LiveQnoStatus.this, android.R.layout.simple_dropdown_item_1line, liveqnoDetailsArrayList);
                        spDepartment.setAdapter(arrayAdapter);
//                        refreshAllContent(3000);
                    }
                }catch(Exception ex){ex.printStackTrace();
                    Toast.makeText(LiveQnoStatus.this, "It seems you don't have any visit for today.", Toast.LENGTH_SHORT).show();
                finish();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                Log.i("error", "onErrorResponse: "+error);
                AppUtilityFunctions.handleExceptions(error,LiveQnoStatus.this);
                finish();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);




    }

    public void btnRefresh(View view) {
        callQnoService(msd.getPatientDetails().getCrno());
    }
}