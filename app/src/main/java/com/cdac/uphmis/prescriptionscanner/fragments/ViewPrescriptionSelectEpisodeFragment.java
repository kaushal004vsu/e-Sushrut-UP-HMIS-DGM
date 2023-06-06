package com.cdac.uphmis.prescriptionscanner.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cdac.uphmis.fragments.DoctorHomeFragment;
import com.cdac.uphmis.fragments.DoctorNewCr;
import com.cdac.uphmis.prescriptionscanner.model.TariffDetails;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.R;
import com.cdac.uphmis.prescriptionscanner.adapters.CustomAdapterViewPrescription;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.ServiceUrl;


public class ViewPrescriptionSelectEpisodeFragment extends Fragment {
//    RequestQueue requestQueue;
    CustomAdapterViewPrescription adapter;
    ListView lv;
    String strCrno;
    TextView tvcrno, tvname;

    LinearLayout progressBar;
    GeometricProgressView progressView;
    ManagingSharedData msd;


    ArrayList<String> spinnerList;
    Spinner spinner;
    public ViewPrescriptionSelectEpisodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_view_prescription_select_episode, container, false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        final BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.doctor_navigation);
        navigation.setVisibility(View.GONE);
        new NukeSSLCerts().nuke(getActivity());
        msd=new ManagingSharedData(getActivity());
        progressBar=(LinearLayout)view.findViewById(R.id.pb);
        progressView = (GeometricProgressView)view.findViewById(R.id.progress_view);

        Button btnBack=(Button)view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment scanPrescriptionFragment=new ScanPrescriptionFragment();
//                FragmentTransaction transaction= getFragmentManager().beginTransaction();
//                transaction.replace(R.id.container,scanPrescriptionFragment);
//                transaction.commit();

                DoctorHomeFragment doctorHomeFragment =new DoctorHomeFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, doctorHomeFragment);
                transaction.commit();
//                getFragmentManager().popBackStack();
            }
        });

        tvcrno = (TextView) view.findViewById(R.id.cr);
        tvname = (TextView) view.findViewById(R.id.name);
        lv = (ListView) view.findViewById(R.id.lv);
//        strCrno = getArguments().getString("crno");
        strCrno = msd.getCrNo();
        Log.i("strcrno", "onCreateView: "+strCrno);
        submitCr();
        spinnerList=new ArrayList();
        spinner = view.findViewById(R.id.filter_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                String patientDetails = spinner.getSelectedItem().toString();
                if (!patientDetails.equalsIgnoreCase("All")) {
                    adapter.getFilter().filter(patientDetails);
                } else {
                    adapter.getFilter().filter("");
                }
            } public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public void submitCr() {

//        episodeCodeCard.setVisibility(View.VISIBLE);
        final ArrayList<TariffDetails> patDtlsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.getPatDetailsViewPrescriptionurl/*"https://220.156.189.222/HBIMS/services/restful/UserService/getPatData"*/ /*"http://10.226.21.46:8080/HBIMS/services/restful/UserService/getPatDataViewPrescription"*/, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                progressBar.setVisibility(View.GONE);


                Log.i("response is ", "onResponse: " + response);
                //    tv.setText(response);
                String jsonStr = response;
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray patDetails = jsonObj.getJSONArray("pat_details");
                    if(patDetails.length()==0)
                    {
                        DoctorHomeFragment doctorHomeFragment = new DoctorHomeFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, doctorHomeFragment); // give your fragment container id in first parameter
                        //  transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                        Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        spinnerList.add(getString(R.string.all));
                        for (int i = 0; i < patDetails.length(); i++) {
                            JSONObject c = patDetails.getJSONObject(i);

                            String unitName = c.getString("UNITNAME");
                            String visitNo = c.getString("VISITNO");
                            String crNo = c.getString("HRGNUM_PUK");
                            String deptCode = c.getString("DEPTCODE");
                            String ageSex = c.getString("AGE_SEX");
                            String deptName = c.getString("DEPTNAME");
                            String episodeCode = c.getString("EPISODECODE");
                            String episodeDate = c.getString("VISITDATE");
                            String patAddress = c.getString("PATADDRESS");
                            String hospitalCode = c.getString("HOSPITAL_CODE");
                            String unitCode = c.getString("UNITCODE");
                            String patName = c.getString("PATNAME");
                            Log.i("fethed strings", "onResponse: " + unitName + "  " + "  " + visitNo);

//String episodeDate="14-DEC-18 10:41:48";
                            //Log.i("deptvalue is", "onResponse: " + deptvalue);
                            tvcrno.setText(crNo);
                            String temp = ageSex.replaceAll("\\^", "#");
                            String val[] = temp.split("#");
                            tvname.setText(patName + " ( " + val[1] + " )");
                            //  tvage.setText(ageSex);
                            //  tvdeptname.setText(deptName + "  ( " + unitName + " )   ( Visit No " + visitNo + " )");


                            patDtlsList.add(new TariffDetails(unitName, visitNo, crNo, deptCode, deptName, episodeCode, episodeDate, patAddress, hospitalCode, unitCode, patName,""));
                            spinnerList.add(deptName);

                        }


//                    ArrayAdapter<TariffDetails> adapter = new ArrayAdapter<TariffDetails>(getActivity(), android.R.layout.simple_spinner_dropdown_item,patDtlsList);
//                    spinner.setAdapter(adapter);
                        if(getActivity()!=null) {
                            adapter = new CustomAdapterViewPrescription(getActivity(), patDtlsList);
                            lv.setAdapter(adapter);
                        }

                        Set<String> hs = new HashSet<>();
                        hs.addAll(spinnerList);
                        spinnerList.clear();
                        spinnerList.addAll(hs);

                        ArrayAdapter spinneradapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
                        spinner.setAdapter(spinneradapter);
                    }
                } catch (final JSONException e) {
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                if(getActivity()!=null) {
                  /*  FailureUploadFragment failureUploadFragment = new FailureUploadFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, failureUploadFragment); // give your fragment container id in first parameter
                    //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();*/


                    AppUtilityFunctions.handleExceptions(error,getActivity());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("hosp_code",ServiceUrl.hospId);
                data.put("CrNo", msd.getCrNo());
                data.put("seatid",msd.getSeatId());
                return data;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(getActivity()).addToRequestQueue(request);


    }
}
