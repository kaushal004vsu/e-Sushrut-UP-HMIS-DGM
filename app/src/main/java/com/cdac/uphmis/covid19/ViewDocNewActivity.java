package com.cdac.uphmis.covid19;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.cdac.uphmis.covid19.adapter.ViewDocNewAdapter;
import com.cdac.uphmis.covid19.model.ViewDocNewModel;
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

public class ViewDocNewActivity extends AppCompatActivity {

    ViewDocNewAdapter adapter;
    List<ViewDocNewModel> list;

    private GeometricProgressView progressView;
    private LinearLayout llNoResultFound;
    private RecyclerView recyclerView;


    private String requestId;
    private ManagingSharedData msd;
    private void initVar() {
        progressView = findViewById(R.id.progress_view);
        llNoResultFound = findViewById(R.id.ll_no_record_found);
        recyclerView = findViewById(R.id.rv);
        list = new ArrayList<>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doc_new);
        
         msd=new ManagingSharedData(this);
        try {

            Intent intent = getIntent();
            requestId = intent.getStringExtra("requestId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        initVar();
       Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Document list");
        getDocumentList();
    }


    private void getDocumentList() {

        String url = ServiceUrl.getDocumentList+requestId+"&pHospcode="+msd.getHospCode()+"&pSlno=1";
        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
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
                        ViewDocNewModel admissionSlipDetails = gson.fromJson(String.valueOf(c), ViewDocNewModel.class);
                        list.add(admissionSlipDetails);
                    }
                }
                setUpRecyclerView(list);
            } catch (JSONException ex) {
                ex.printStackTrace();
                llNoResultFound.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, ViewDocNewActivity.this);
                Log.e("error", "onErrorResponse: ", error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);


    }

    private void setUpRecyclerView(List<ViewDocNewModel> QMSDetailsList) {
        if (QMSDetailsList != null && QMSDetailsList.size() > 0) {

            llNoResultFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
            adapter = new ViewDocNewAdapter(ViewDocNewActivity.this, QMSDetailsList, new ViewDocNewAdapter.OnClick() {
                @Override
                public void onClick(int position) {
                    String docsUrl = list.get(position).getHRGSTR_FTP_PATH();
                    String extension = list.get(position).getHRGSTR_FILE_TYPE();
                    getDocument(docsUrl, extension);
                }
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
                            Toast.makeText(ViewDocNewActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
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

                            Toast.makeText(ViewDocNewActivity.this, "Document not found.", Toast.LENGTH_SHORT).show();
                        }
                        /*if(error!= null && error.networkResponse.statusCode==404)
                        {
                            Toast.makeText(ViewDocNewActivity.this, "Document not found.", Toast.LENGTH_SHORT).show();
                        }*/
                       else {
                            AppUtilityFunctions.handleExceptions(error, ViewDocNewActivity.this);
                        }
                    }
                });


        MySingleton.getInstance(this).addToRequestQueue(request);

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