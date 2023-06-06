package com.cdac.uphmis.reimbursement;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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
import com.cdac.uphmis.reimbursement.adapter.ReimbursementEnquiryAdapter;
import com.cdac.uphmis.reimbursement.model.ClaimEnquiryDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClaimEnquiryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GeometricProgressView progressView;
    private LinearLayout llNoRecordFound;


    private ManagingSharedData msd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_enquiry);
        
        msd = new ManagingSharedData(this);
        progressView = findViewById(R.id.progress_view);
        llNoRecordFound = findViewById(R.id.ll_no_record_found);


        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        recyclerView = findViewById(R.id.rv);
        getClaimEnquiries(msd.getPatientDetails().getCrno());
    }

    private void getClaimEnquiries(String crno) {
        progressView.setVisibility(View.VISIBLE);
        ArrayList<ClaimEnquiryDetails> claimEnquiryDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.reimbursementClaimEnquiry + "modeval=1&claimReqNo=&crno=" + crno + "&hospCode=&slNo=" + crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("EpisodesList", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        ClaimEnquiryDetails claimEnquiryDetails = gson.fromJson(String.valueOf(c), ClaimEnquiryDetails.class);
                        claimEnquiryDetailsArrayList.add(claimEnquiryDetails);
                    }
                    setUpRecyclerView(claimEnquiryDetailsArrayList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, ClaimEnquiryActivity.this);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ClaimEnquiryActivity.this).addToRequestQueue(request);
    }

    private void setUpRecyclerView(List<ClaimEnquiryDetails> claimEnquiryDetailsArrayList) {
        if (claimEnquiryDetailsArrayList != null && claimEnquiryDetailsArrayList.size() > 0) {

            llNoRecordFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
           ReimbursementEnquiryAdapter adapter = new ReimbursementEnquiryAdapter(ClaimEnquiryActivity.this, msd.getPatientDetails().getCrno(),claimEnquiryDetailsArrayList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            llNoRecordFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    public void btnNewClaim(View view) {
        startActivity(new Intent(this, SaveReimbursementActivity.class));
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