package com.cdac.uphmis.drugavailability;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.DocsUpload.DocsViewDocNewActivity;
import com.cdac.uphmis.DocsUpload.adapter.DocsViewDocNewAdapter;
import com.cdac.uphmis.R;
import com.cdac.uphmis.drugavailability.adapter.DrugRxAdapter;
//import com.cdac.uphmis.drugavailability.model.DrugRxModel;
import com.cdac.uphmis.drugavailability.model.expandableList.AllDatum;
//import com.cdac.uphmis.drugavailability.model.expandableList.DrugRxModel;
import com.cdac.uphmis.transactions.TransactionListDetails;
import com.cdac.uphmis.transactions.TransactionsListActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrugRxActivity extends AppCompatActivity {
    DrugRxAdapter adapter;
    List<AllDatum> list;

    private GeometricProgressView progressView;
    private LinearLayout llNoResultFound;
    private RecyclerView recyclerView;
    ManagingSharedData msd;
    List<AllDatum> drugRxDetailsList;
    private void initVar() {
        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);
        recyclerView = findViewById(R.id.rv);
        list = new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_rx);
        initVar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( getString(R.string.medication_against_rx));
        msd = new ManagingSharedData(this);

        TextView tvName=findViewById(R.id.name);
        TextView tvCrno=findViewById(R.id.cr);

        tvName.setText(msd.getPatientDetails().getFirstname());
        tvCrno.setText(msd.getPatientDetails().getCrno());

        getEhrjsonPatMedication(msd.getCrNo());
    }

    private void getEhrjsonPatMedication(String crno) {

        progressView.setVisibility(View.VISIBLE);
        drugRxDetailsList = new ArrayList<>();
        String url = ServiceUrl.ip+"eSushrutEMRServices/service/ehr/get/patient/medication/all?crNo="+crno;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("response", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject medication_detail = jsonObject.getJSONObject("medication_detail");
                    JSONArray jsonArray = medication_detail.getJSONArray("all_data");
                    if (jsonArray.length() !=0) {
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AllDatum list = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), AllDatum.class);
                            drugRxDetailsList.add(list);
                        }
                    }
                    setUpRecyclerView(drugRxDetailsList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, DrugRxActivity.this);
                setUpRecyclerView(drugRxDetailsList);
                Log.i("error", "onErrorResponse: " + error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void setUpRecyclerView(List<AllDatum> allData) {
        if (allData != null && allData.size() > 0) {

            llNoResultFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
            adapter = new DrugRxAdapter(DrugRxActivity.this,allData);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else {
            llNoResultFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    public Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }

                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }
        return bigBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.print_medications, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_pdf_action:
                AppUtilityFunctions.printPdfMedication(DrugRxActivity.this,"medication",msd.getCrNo()+"_Medications",recyclerView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}