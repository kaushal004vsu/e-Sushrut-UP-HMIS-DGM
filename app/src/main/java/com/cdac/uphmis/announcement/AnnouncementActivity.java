package com.cdac.uphmis.announcement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.ByteArrayRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.announcement.model.Datum;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    RecyclerView recylerview;
    AnnouncementAdapter adapter;
    List<Datum> list;
    private LinearLayout progressView;
    private ManagingSharedData msd;
    private LinearLayout llNoResultFound;
    SwipeRefreshLayout swiperefresh;
    private void initVar() {
        recylerview=findViewById(R.id.recylerview);
        list=new ArrayList<>();
        progressView = findViewById(R.id.progressView);
        llNoResultFound = findViewById(R.id.ll_no_record_found);
        swiperefresh = findViewById(R.id.swiperefresh);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        initVar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        toolbar.setTitle(R.string.AN_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        msd=new ManagingSharedData(this);
        getAnnounceList();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAnnounceList();
                swiperefresh.setRefreshing(false);
            }
        });

    }

    private void getAnnounceList() {
        list.clear();
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.announcement + msd.getPatientDetails().getHospitalCode(), response -> {
            Log.i("response", "onResponse: " + response);
            progressView.setVisibility(View.GONE);
            try {
                response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                if (jsonArray.length() >= 1) {
                    llNoResultFound.setVisibility(View.GONE);
                    recylerview.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Datum patientDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Datum.class);
                        list.add(patientDetails);
                    }
                    adapter=new AnnouncementAdapter(AnnouncementActivity.this, list, new AnnouncementAdapter.OnClick() {
                        @Override
                        public void onClick(int position) {
                            String docsUrl = list.get(position).getDocumentFile();
                            String extension =getExt(docsUrl);
                            getDocument(docsUrl, extension);
                        }
                    });
                    recylerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    llNoResultFound.setVisibility(View.VISIBLE);
                    recylerview.setVisibility(View.GONE);
                }

            } catch (final Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                progressView.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
                , error -> {
            Log.i("error", "onErrorResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, this);
            progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public String getExt(String filePath){
        int strLength = filePath.lastIndexOf(".");
        if(strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }
    private void getDocument(String docsUrl, String extension) {
        progressView.setVisibility(View.VISIBLE);
        String url = ServiceUrl.downloadDocument + docsUrl;
            ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                    url,
                    new Response.Listener<byte[]>() {
                        @Override
                        public void onResponse(byte[] response) {
                            Log.i("getBilletCard", response.toString());
                            progressView.setVisibility(View.GONE);
                            try {
                                byte[] bytes = response;
                                saveToFile(bytes, "document." + extension, extension);
                            } catch (Exception e) {
                                Toast.makeText(AnnouncementActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressView.setVisibility(View.GONE);
                            error.printStackTrace();

                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 404) {

                                Toast.makeText(AnnouncementActivity.this, "Document not found.", Toast.LENGTH_SHORT).show();
                            }
                        /*if(error!= null && error.networkResponse.statusCode==404)
                        {
                            Toast.makeText(ViewDocNewActivity.this, "Document not found.", Toast.LENGTH_SHORT).show();
                        }*/
                            else {
                                AppUtilityFunctions.handleExceptions(error, AnnouncementActivity.this);
                            }
                        }
                    });


            MySingleton.getInstance(this).addToRequestQueue(request);

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
            // Toast.makeText(this, "File successfully saved", Toast.LENGTH_LONG).show();
//            AppUtilityFunctions.viewPdf(this,"myappname","card");
            if (extension.equalsIgnoreCase("pdf"))
                AppUtilityFunctions.viewPdf(this, "documents", "document");
            else
                AppUtilityFunctions.viewImage(this, "documents", "document", extension);
            Log.i("TAG", "saveToFile: ");
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


