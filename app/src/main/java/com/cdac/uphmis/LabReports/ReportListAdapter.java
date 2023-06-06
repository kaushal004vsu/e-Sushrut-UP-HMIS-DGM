package com.cdac.uphmis.LabReports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ReportListAdapter extends BaseAdapter {

    Context c;
    ArrayList<ReportListDetails> reportListDetails;
    RequestQueue requestQueue;
    GeometricProgressView progressView;
    ManagingSharedData msd;

    public ReportListAdapter(Context c, ArrayList<ReportListDetails> reportListDetails, GeometricProgressView progressview) {
        this.c = c;
        this.reportListDetails = reportListDetails;
        this.progressView = progressview;
        requestQueue = Volley.newRequestQueue(c);
    }

    @Override
    public int getCount() {
        return reportListDetails.size();
    }

    @Override
    public Object getItem(int i) {
        return reportListDetails.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.report_list_view, viewGroup, false);
        }

        progressView.setVisibility(View.GONE);

        // final TariffDetails patientDetails = (TariffDetails) this.getItem(i);
        final ReportListDetails reportListDetails = (ReportListDetails) this.getItem(i);
        // ImageView img= (ImageView) view.findViewById(R.id.spacecraftImg);
        TextView testName = (TextView) view.findViewById(R.id.tv_test_name);
        TextView reqDno = (TextView) view.findViewById(R.id.tv_reqdno);
        TextView date = (TextView) view.findViewById(R.id.tv_date);


        testName.setText(reportListDetails.getName());
        reqDno.setText(reportListDetails.getReqNo());
        date.setText(reportListDetails.getDate());


        view.setOnClickListener(v -> {

            downloadPrdf(reportListDetails.getReqNo());
            progressView.setVisibility(View.VISIBLE);
        });

        return view;
    }


    public void downloadPrdf(String reqDno) {
        msd = new ManagingSharedData(c);
        Log.i("url", "downloadPrdf: " + ServiceUrl.getReportpdf + msd.getCrNo() + "&reqDNo=" + reqDno + "&hosCode=" + ServiceUrl.hospId);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getReportpdf + msd.getCrNo() + "&reqDNo=" + reqDno + "&hosCode=" + ServiceUrl.hospId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);
                JSONArray jsonObj;
                try {
                    jsonObj = new JSONArray(response);

                    for (int i = 0; i < jsonObj.length(); i++) {
                        JSONObject c = jsonObj.getJSONObject(i);
                        String data = c.getString("PDFDATA");
                        convertPdf(data, reqDno);


                    }
                } catch (JSONException e) {
                    Toast.makeText(c, "Could not fetch report.Try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, error -> {
            progressView.setVisibility(View.GONE);
//                Snackbar.make(progressView, "Unable to connect", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(c, "Unable to connect", Toast.LENGTH_SHORT).show();
            Log.i("error", "onErrorResponse: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(c).addToRequestQueue(request);


    }

    public void convertPdf(String data, String i) {
        File imagepath = new File(c.getFilesDir().toString(), "myReports");
        //dwldsPath = new File("/mnt/sdcard/download/Base64.pdf");
        String s = c.getFilesDir().toString() + "/myReports/" + i + ".pdf";
        byte[] pdfAsBytes = Base64.decode(data, 0);
        FileOutputStream os = null;
        try {
            if (!imagepath.exists()) {
                if (!imagepath.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }
            os = new FileOutputStream(s, false);

            os.write(pdfAsBytes);

            os.flush();
            os.close();
            Toast.makeText(c, "Report saved to " + s, Toast.LENGTH_SHORT).show();


            // AppUtilityFunctions.viewPdf(c,"myReports",i);

            Intent intent = new Intent(c, ViewPdfLabReportActivity.class);
            intent.putExtra("imagepath", s);
            c.startActivity(intent);


        } catch (Exception e) {
            Toast.makeText(c, "Please grant storage permission.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
