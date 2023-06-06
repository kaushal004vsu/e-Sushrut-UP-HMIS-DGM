package com.cdac.uphmis.lpstatus;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.adapter.MyRecycleViewClickListener;
import com.cdac.uphmis.lpstatus.adapter.LpStatusAdapter;
import com.cdac.uphmis.lpstatus.model.LPSTatusDetails;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.precriptionView.adapter.PrescriptionAdapter;
import com.cdac.uphmis.precriptionView.model.PrescriptionListDetails;
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

public class LPStatusActivity extends AppCompatActivity {
    LpStatusAdapter adapter;
    private GeometricProgressView progressView;
    private ManagingSharedData msd;
    private LinearLayout llNoResultFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpstatus);
        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);
        msd = new ManagingSharedData(LPStatusActivity.this);

        TextView tvcrno = findViewById(R.id.cr);
        TextView tvName = findViewById(R.id.name);
        tvcrno.setText(msd.getPatientDetails().getCrno());
        tvName.setText(msd.getPatientDetails().getFirstname());


        Toolbar toolbar =  findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        getLpStatus(msd.getCrNo());
    }
    private void getLpStatus(String crno) {

        progressView.setVisibility(View.VISIBLE);
        List<LPSTatusDetails> prescripionList = new ArrayList<>();

        String lpsUrl=ServiceUrl.lpStatusurl+crno+"&hospCode=100";
        StringRequest request = new StringRequest(Request.Method.GET, lpsUrl, response -> {
            progressView.setVisibility(View.GONE);
            Log.i("response", "onResponse: " + response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String s_no = c.optString("SNO");
                        String statuss = c.optString("STATUS");
                        String item_name = c.optString("ITEM_NAME");
                        String date = c.optString("DATE");
                        String app_qty = c.optString("APP_QTY");
                        String hosp_name = c.optString("HOSP_NAME");

                        prescripionList.add(new LPSTatusDetails(s_no, statuss, item_name, date, app_qty, hosp_name));

                    }

                }

                setUpRecyclerViewForPrescription(prescripionList);
            } catch (JSONException ex) {
                ex.printStackTrace();
                llNoResultFound.setVisibility(View.VISIBLE);
            }
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, LPStatusActivity.this);
            Log.e("error", "onErrorResponse: ", error);
        });

        MySingleton.getInstance(this).addToRequestQueue(request);


    }

    private void setUpRecyclerViewForPrescription(List<LPSTatusDetails> prescripionList) {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter=null;
        adapter = new LpStatusAdapter(LPStatusActivity.this,prescripionList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (prescripionList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            llNoResultFound.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            llNoResultFound.setVisibility(View.VISIBLE);
        }

        recyclerView.addOnItemTouchListener(new MyRecycleViewClickListener(this, (view, position) -> {
        }));
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