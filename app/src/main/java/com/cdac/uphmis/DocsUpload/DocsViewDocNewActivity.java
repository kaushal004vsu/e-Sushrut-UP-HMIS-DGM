package com.cdac.uphmis.DocsUpload;

import static com.cdac.uphmis.util.ServiceUrl.ViewDocs;
import static com.cdac.uphmis.util.ServiceUrl.testurl2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.cdac.uphmis.DocsUpload.adapter.DocsViewDocNewAdapter;
import com.cdac.uphmis.DocsUpload.model.AllDatum;
import com.cdac.uphmis.LabReports.ViewPdfLabReportActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.phr.adapter.PhrViewDocNewAdapter;
import com.cdac.uphmis.phr.model.PhrViewDocNewModel;
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

public class DocsViewDocNewActivity extends AppCompatActivity {

    DocsViewDocNewAdapter adapter;
    List<AllDatum> list;

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
        String url =ServiceUrl.ViewDocs+ msd.getPatientDetails().getCrno();
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            progressView.setVisibility(View.GONE);
            //Log.i("response", "onResponse: " + response);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                JSONObject documents_detail = jsonObject.getJSONObject("documents_detail");

                JSONArray jsonArray = documents_detail.getJSONArray("all_data");
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    AllDatum allData = gson.fromJson(String.valueOf(c), AllDatum.class);
                    list.add(allData);
                }

                setUpRecyclerView(list);
            } catch (JSONException ex) {
                ex.printStackTrace();
                llNoResultFound.setVisibility(View.VISIBLE);
            }
        }, error -> {
            progressView.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, DocsViewDocNewActivity.this);
            //Log.e("error", "onErrorResponse: ", error);
        });
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setUpRecyclerView(List<AllDatum> allData) {
        if (allData != null && allData.size() > 0) {

            llNoResultFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
            adapter = new DocsViewDocNewAdapter(DocsViewDocNewActivity.this, allData, position -> {
                String base64 = list.get(position).getDocumentBase64();
                String extension = list.get(position).getDocumentContentType();
                byte[] data = Base64.decode(base64, Base64.DEFAULT);

                saveToFile(data, "myDocument", extension);

            });
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            llNoResultFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    public void saveToFile(byte[] byteArray, String pFileName, String contentType) {
        File f = new File(getFilesDir() + "/documents");
        if (!f.isDirectory()) {
            f.mkdir();
        }

        String fileName = "";
        if (contentType.equalsIgnoreCase("application/pdf"))
            fileName += getFilesDir() + "/documents/" + pFileName + ".pdf";
        else
            fileName += getFilesDir() + "/documents/" + pFileName + ".png";


        try {
            FileOutputStream fPdf = new FileOutputStream(fileName);
            fPdf.write(byteArray);
            fPdf.flush();
            fPdf.close();
            if (contentType.equalsIgnoreCase("application/pdf"))
                AppUtilityFunctions.viewPdf(this, "documents", pFileName);
            else
                AppUtilityFunctions.viewImage(this, "documents", pFileName, "png");
            //Log.i("TAG", "saveToFile: ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File create error", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File write error", Toast.LENGTH_LONG).show();
        }

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