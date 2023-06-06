package com.cdac.uphmis.QMSSlip;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.QMSSlip.model.QMSDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.reimbursement.SaveReimbursementActivity;
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

public class QMSSlipActivity extends AppCompatActivity {
    private QMSSlipAdapter adapter;
    private RecyclerView recyclerView;
    private GeometricProgressView progressView;
    private LinearLayout llNoRecordFound;
    private TextView tvQnoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_m_s_slip);
        
        ManagingSharedData msd = new ManagingSharedData(this);
        progressView = findViewById(R.id.progress_view);
        llNoRecordFound = findViewById(R.id.ll_no_record_found);
        tvQnoStatus = findViewById(R.id.tv_qno_status);

        TextView tvName=findViewById(R.id.name);
        TextView tvCrno=findViewById(R.id.cr);

        tvName.setText(msd.getPatientDetails().getFirstname());
        tvCrno.setText(msd.getPatientDetails().getCrno());
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        recyclerView = findViewById(R.id.rv);
        //getCrList();
        getEpisodeList(msd.getPatientDetails().getCrno());
    }

    private void getEpisodeList(String crno) {
        progressView.setVisibility(View.VISIBLE);
        ArrayList<QMSDetails> qmsDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.qmsListUrl + "hospCode=0&patCrNo=" + crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("EpisodesList", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("episode_list");
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        QMSDetails qmsDetails = gson.fromJson(String.valueOf(c), QMSDetails.class);
                        qmsDetailsArrayList.add(qmsDetails);
                    }
                    setUpRecyclerView(qmsDetailsArrayList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, QMSSlipActivity.this);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(QMSSlipActivity.this).addToRequestQueue(request);
    }
    private void setUpRecyclerView(List<QMSDetails> QMSDetailsList) {
        if (QMSDetailsList != null && QMSDetailsList.size() > 0) {
            llNoRecordFound.setVisibility(View.GONE);
          //  tvQnoStatus.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new QMSSlipAdapter(QMSSlipActivity.this, QMSDetailsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            llNoRecordFound.setVisibility(View.VISIBLE);
            tvQnoStatus.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
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
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
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
    public void btnLiveStatus(View view) {
        startActivity(new Intent(QMSSlipActivity.this, LiveQnoStatus.class));
    }
}