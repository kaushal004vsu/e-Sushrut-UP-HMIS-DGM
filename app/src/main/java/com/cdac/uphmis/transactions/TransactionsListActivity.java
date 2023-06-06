package com.cdac.uphmis.transactions;

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
import com.cdac.uphmis.EstampingActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.adapter.MyRecycleViewClickListener;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.SessionServicecall;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class TransactionsListActivity extends AppCompatActivity {
    private GeometricProgressView progressView;
    private ManagingSharedData msd;
    private LinearLayout llNoResultFound;
    private TransactionsListAdapter transactionsListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);
        
        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);

        msd = new ManagingSharedData(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }

        getTransactionsList(msd.getPatientDetails().getCrno());


    }


    private void getTransactionsList(String crno) {

        progressView.setVisibility(View.VISIBLE);
        List<TransactionListDetails> transactionListDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.transactionsUrl + crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response", "onResponse: " + response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TransactionListDetails transactionListDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), TransactionListDetails.class);
                            transactionListDetailsList.add(transactionListDetails);

                        }

                    }
                    setUpRecyclerView(transactionListDetailsList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, TransactionsListActivity.this);
                setUpRecyclerView(transactionListDetailsList);
                Log.i("error", "onErrorResponse: " + error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void setUpRecyclerView(List<TransactionListDetails> transactionList) {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        transactionsListAdapter = null;
        transactionsListAdapter = new TransactionsListAdapter(transactionList,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(transactionsListAdapter);

        if (transactionList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            llNoResultFound.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            llNoResultFound.setVisibility(View.VISIBLE);
        }

        recyclerView.addOnItemTouchListener(new MyRecycleViewClickListener(this, new MyRecycleViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    TransactionListDetails transactionListDetails = (TransactionListDetails) transactionList.get(position);
                    Log.i("recyclerviewclick", "onItemClick: " + transactionListDetails.getTransNo());
//                    getPastWebPrescription(prescriptionListDetails, progressView);

                    downloadBill(msd.getPatientDetails().getCrno(),transactionListDetails.getTransNo(),transactionListDetails.getHospCode(),transactionListDetails.getRecieptNo());
                    try {
                        SessionServicecall sessionServicecall = new SessionServicecall(TransactionsListActivity.this);
                        sessionServicecall.saveSession(msd.getPatientDetails().getCrno(), msd.getPatientDetails().getMobileNo(),msd.getPatientDetails().getHospitalCode(), "Transaction Bill View", "", "", "", "");
                    }catch(Exception ex){}

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }));
    }

    private void downloadBill(String crno, String billNo, String hospCode,String receiptNo) {
        progressView.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.downloadBillUrl + crno + "&billNo=" + billNo + "&hospcode=" + hospCode+"&receiptNo="+receiptNo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(response);
                    String status=jsonObj.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String data = jsonObj.getString("BillBase64");

                        saveBase64Pdf(TransactionsListActivity.this, "myTransactions", billNo, data);
                    }else
                    {
                        Toast.makeText(TransactionsListActivity.this, "Something went wrong.Unable to download bill.", Toast.LENGTH_SHORT).show();
                    }

            } catch(
            JSONException e)

            {
                Toast.makeText(TransactionsListActivity.this, "Could not fetch report.Try again later.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    },error ->

    {
        progressView.setVisibility(View.GONE);
//                Snackbar.make(progressView, "Unable to connect", Snackbar.LENGTH_SHORT).show();
        AppUtilityFunctions.handleExceptions(error, TransactionsListActivity.this);
        Log.i("error", "onErrorResponse: " + error);
    });

        request.setRetryPolicy(new

    DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(TransactionsListActivity .this).

    addToRequestQueue(request);

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
                if (transactionsListAdapter != null) {
                    transactionsListAdapter.getFilter().filter(newText);
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