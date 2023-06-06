package com.cdac.uphmis.LabReports;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReportListFragment extends Fragment {
    RequestQueue requestQueue;
    ReportListAdapter adapter;
    ListView lv;
    GeometricProgressView progressView;
    ManagingSharedData msd;
    TextView tvcrno, tvname;
    TextView noRecordFound;

    public ReportListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        lv = view.findViewById(R.id.lv_report_list);
//             NukeSSLCerts.nuke(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        progressView = view.findViewById(R.id.progress_view);

        msd = new ManagingSharedData(getActivity());
        noRecordFound = view.findViewById(R.id.no_record_found);

        tvcrno = view.findViewById(R.id.cr);
        tvname = view.findViewById(R.id.name);

        if (msd.getPatientDetails() != null) {
//            cardPatDetails.setVisibility(View.VISIBLE);
            tvcrno.setText(msd.getCrNo());
            tvname.setText(msd.getPatientDetails().getFirstname());
        }
        getReportList();
        // submitCr();


        return view;
    }


    public void getReportList() {
        final ArrayList<ReportListDetails> reportListDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getReportList + msd.getCrNo() + "&hosCode=" + ServiceUrl.hospId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response is ", "onResponse: " + response);
                progressView.setVisibility(View.GONE);

                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String testName = c.getString("TESTNAME");
                        String reqDno = c.getString("REQDNO");
                        String reportDate = "";
                        if (c.has("REPORTDATE")) {
                            reportDate = c.getString("REPORTDATE");
                        } else {
                            reportDate = "";
                        }
                        reportListDetailsList.add(new ReportListDetails(testName, reqDno, reportDate));
                    }
                    adapter = new ReportListAdapter(getActivity(), reportListDetailsList, progressView);
                    if (adapter.getCount() == 0) {

                        noRecordFound.setVisibility(View.VISIBLE);
                        lv.setAdapter(adapter);
                        lv.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(), "No registration done for today.", Toast.LENGTH_SHORT).show();
                    } else {
                        lv.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("error", "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }



    /*public void submitCr() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatentDetails + msd.getCrNo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // progressBar.setVisibility(View.GONE);
                Log.i("response is ", "onResponse: " + response);
                // String jsonStr = response;
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
                    JSONArray patDetails = jsonObj.getJSONArray("pat_details");
                    if (patDetails.length() == 0) {
//                        DoctorNewCr enterNewCrFragment = new DoctorNewCr();
//                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                        transaction.replace(R.id.container, enterNewCrFragment); // give your fragment container id in first parameter
//                        //  transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                        transaction.commit();
                        getFragmentManager().popBackStack();
                        Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < patDetails.length(); i++) {
                            JSONObject c = patDetails.getJSONObject(i);
                            if (c.getString("FIRSTNAME").equalsIgnoreCase("NA")) {
                                getFragmentManager().popBackStack();
                                Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                            } else {
                                String crNo = c.getString("CRNO");
                                String age = c.getString("AGE");
                                String gender = c.getString("GENDER");
                                String patName = c.getString("FIRSTNAME");

                                tvcrno.setText(crNo);
                                tvname.setText(patName + " ( " + age + "/" + gender + " )");
                            }
                        }

                    }

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                if (getActivity() != null) {
                    AppUtilityFunctions.handleExceptions(error,getActivity());

                }
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);


    }*/
}
