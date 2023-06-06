package com.cdac.uphmis.phr;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.ByteArrayRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.phr.adapter.PhrViewDocNewAdapter;
import com.cdac.uphmis.phr.model.PhrViewDocNewModel;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhrViewDocNewActivity extends AppCompatActivity {

    PhrViewDocNewAdapter adapter;
    List<PhrViewDocNewModel> list;

    private GeometricProgressView progressView;
    private LinearLayout llNoResultFound;
    private RecyclerView recyclerView;



    private void initVar() {
        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);
        recyclerView = findViewById(R.id.rv);
        list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phr_view_doc_new);


        initVar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Documents");
        getDocumentList();
    }


    private void getDocumentList() {
        ManagingSharedData msd = new ManagingSharedData(this);
        String url = ServiceUrl.testurl + "AppOpdService/retrievePhrDocumentList?modeval=2&crno=" + msd.getPatientDetails().getCrno() + "&vitalId="+ AppConstants.DOCUMENT_ID;


        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            progressView.setVisibility(View.GONE);
            //Log.i("response", "onResponse: " + response);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        PhrViewDocNewModel admissionSlipDetails = gson.fromJson(String.valueOf(c), PhrViewDocNewModel.class);
                        list.add(admissionSlipDetails);
                    }
                }
                setUpRecyclerView(list);
            } catch (JSONException ex) {
                ex.printStackTrace();
                llNoResultFound.setVisibility(View.VISIBLE);
            }
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, PhrViewDocNewActivity.this);
            //Log.e("error", "onErrorResponse: ", error);
        });
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }

    private void setUpRecyclerView(List<PhrViewDocNewModel> QMSDetailsList) {
        if (QMSDetailsList != null && QMSDetailsList.size() > 0) {

            llNoResultFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
            adapter = new PhrViewDocNewAdapter(PhrViewDocNewActivity.this, QMSDetailsList, position -> {
                String docsUrl = list.get(position).getFtpPath();
                String extension = list.get(position).getFileType();
                getDocument(docsUrl, extension);
            });
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            llNoResultFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    public void saveToFile(byte[] byteArray, String pFileName, String extension) {
        File f = new File(getFilesDir() + "/documents");
        if (!f.isDirectory()) {
            f.mkdir();
        }

        String fileName = getFilesDir() + "/documents/" + pFileName;

        try {
            FileOutputStream fPdf = new FileOutputStream(fileName);
            fPdf.write(byteArray);
            fPdf.flush();
            fPdf.close();
            if (extension.equalsIgnoreCase("pdf"))
                AppUtilityFunctions.viewPdf(this, "documents", "document");
            else
                AppUtilityFunctions.viewImage(this, "documents", "document", extension);
            //Log.i("TAG", "saveToFile: ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File create error", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File write error", Toast.LENGTH_LONG).show();
        }

    }
    private void getDocument(String docsUrl, String extension) {
        progressView.setVisibility(View.VISIBLE);
        String url = ServiceUrl.downloadDocument + docsUrl;

        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url,
                response -> {
                  //  Log.i("getBilletCard", response.toString());
                    progressView.setVisibility(View.GONE);
                    try {
                        saveToFile(response, "document." + extension, extension);
                    } catch (Exception e) {
                        Toast.makeText(PhrViewDocNewActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressView.setVisibility(View.GONE);
                    error.printStackTrace();

                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == 404) {

                        Toast.makeText(PhrViewDocNewActivity.this, "Document not found.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AppUtilityFunctions.handleExceptions(error, PhrViewDocNewActivity.this);
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}