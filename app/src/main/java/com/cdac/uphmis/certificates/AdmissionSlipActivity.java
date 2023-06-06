package com.cdac.uphmis.certificates;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.certificates.adapter.AdmissionSlipAdapter;
import com.cdac.uphmis.certificates.model.AdmissionSlipDetails;
import com.cdac.uphmis.sickLeave.SickLeaveAdapter;
import com.cdac.uphmis.sickLeave.SickLeaveDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdmissionSlipActivity extends AppCompatActivity {
    private GeometricProgressView progressView;
    private ManagingSharedData msd;
    private LinearLayout llNoResultFound;
    private RecyclerView recyclerView;
    private AdmissionSlipAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_slip);

        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);

        msd = new ManagingSharedData(AdmissionSlipActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }

        recyclerView = findViewById(R.id.rv);

        getAdmissionSlipList(msd.getPatientDetails().getCrno());
    }

    private void getAdmissionSlipList(String crno) {

        progressView.setVisibility(View.VISIBLE);
        List<AdmissionSlipDetails> prescripionList = new ArrayList<>();

        Log.d("admission", ServiceUrl.testurl + "admissionSlipService/List?crNo=" + crno);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "admissionSlipService/List?crNo=" + crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response", "onResponse: " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            AdmissionSlipDetails admissionSlipDetails = gson.fromJson(String.valueOf(c), AdmissionSlipDetails.class);
                            prescripionList.add(admissionSlipDetails);
                        }
                    }
                    setUpRecyclerView(prescripionList);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    llNoResultFound.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, AdmissionSlipActivity.this);
                Log.e("error", "onErrorResponse: ", error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }

    private void setUpRecyclerView(List<AdmissionSlipDetails> QMSDetailsList) {
        if (QMSDetailsList != null && QMSDetailsList.size() > 0) {

            llNoResultFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new AdmissionSlipAdapter(AdmissionSlipActivity.this, QMSDetailsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            llNoResultFound.setVisibility(View.VISIBLE);
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
}