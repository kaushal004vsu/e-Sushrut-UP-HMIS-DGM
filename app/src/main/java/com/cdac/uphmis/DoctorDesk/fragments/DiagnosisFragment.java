package com.cdac.uphmis.DoctorDesk.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.cdac.uphmis.DoctorDesk.DeskActivity;
import com.cdac.uphmis.DoctorDesk.model.DiagnosisDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.DeskHomeActivity;
import com.cdac.uphmis.opdLite.model.DiagnosisICDDetails;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosisFragment extends Fragment {

    String[] terms;
    AutoCompleteTextView autoCompleteTextView,autoCompleteIcd,autoCompleteDisease;
    ArrayAdapter<DiagnosisDetails> adapter;
    String lastText = "";


    public DeskActivity deskActivity;

    private LinearLayout llIcd;
    public DiagnosisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diagnosis, container, false);
        deskActivity = (DeskActivity) getActivity();
        final EditText edtDiagnosis = view.findViewById(R.id.edt_diagnosis);
        autoCompleteTextView = view.findViewById(R.id.auto_complete_final_diagnosis);
        autoCompleteTextView.setVisibility(View.GONE);


        llIcd=view.findViewById(R.id.ll_icd);
        autoCompleteIcd = view.findViewById(R.id.autocomplete_icd_code);
        autoCompleteDisease = view.findViewById(R.id.auto_complete_disease);

        //Set the number of characters the user must type before the drop down list is shown


        Switch switchFinalDiagnosis = view.findViewById(R.id.switch_final_diagnosis);
        TextView tvClear = view.findViewById(R.id.tv_clear_diagnosis);

        switchFinalDiagnosis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    llIcd.setVisibility(View.GONE);
                    edtDiagnosis.setEnabled(true);
//                    edtDiagnosis.setText(" \n");


                } else {
                    edtDiagnosis.setEnabled(true);
                    String s = edtDiagnosis.getText().toString().replace("Final Diagnosis:", "Provisional Diagnosis:\n");
                   // edtDiagnosis.setText("");
                    edtDiagnosis.append("\n");
//                    edtDiagnosis.append(s);

                    llIcd.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.GONE);
                }

                autoCompleteDisease.getText().clear();
                autoCompleteIcd.getText().clear();
                autoCompleteTextView.getText().clear();
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getActivity(), "key pressed" + s, Toast.LENGTH_SHORT).show();
                //if (s.toString().equalsIgnoreCase(lastText)) {
                Log.i("lasttext", "onTextChanged: " + lastText);
                Log.i("s.tostring", "onTextChanged: " + s.toString().length());
                if (s.toString().length() >= 3) {
                    getDiagnosis(s.toString());
                }
                // }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof DiagnosisDetails) {
                    DiagnosisDetails selectedItem = (DiagnosisDetails) item;
                    autoCompleteTextView.dismissDropDown();
                    Log.i("student", "onItemClick: " + selectedItem.getTerm());

                    autoCompleteTextView.setText("");
                    edtDiagnosis.append(selectedItem + ", ");

                    deskActivity.snomedCodes=deskActivity.snomedCodes+selectedItem.getTerm()+"|"+selectedItem.getId()+",";
                    deskActivity.snomeCodesDiagnosis=deskActivity.snomeCodesDiagnosis+selectedItem.getId()+",";
                }
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDiagnosis.setText("");
            }
        });




        getDiagnosisFromIcd(autoCompleteIcd, autoCompleteDisease);



        autoCompleteIcd.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            DiagnosisICDDetails  selectedItem = (DiagnosisICDDetails) item;
            autoCompleteDisease.setText(((DiagnosisICDDetails) selectedItem).getDiseaseName());
            edtDiagnosis.append(((DiagnosisICDDetails) selectedItem).getDiseaseName()+", ");
            Log.i("text", "onItemClick: " + ((DiagnosisICDDetails) selectedItem).getDiseaseName());
        });

        autoCompleteDisease.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            DiagnosisICDDetails  selectedItem = (DiagnosisICDDetails) item;
            autoCompleteIcd.setText(((DiagnosisICDDetails) selectedItem).getDiseaseCode());
            edtDiagnosis.append(((DiagnosisICDDetails) selectedItem).getDiseaseName()+", ");
            Log.i("text", "onItemClick: " + ((DiagnosisICDDetails) selectedItem).getDiseaseName());
        });

        return view;
    }

    public void getDiagnosis(String text) {
        terms = new String[10];
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final ArrayList<DiagnosisDetails> arrayList = new ArrayList<DiagnosisDetails>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.snomedUl + text + "&state=active&semantictag=all&acceptability=synonyms&returnlimit=10&refsetid=null", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response is ", "onResponse: " + response);

                try {


                    JSONArray jsonArray = new JSONArray(response);


                    if (jsonArray.length() == 0) {
                        Toast.makeText(getActivity(), "No Results Found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject c = patDetails.getJSONObject(i);
                            JSONObject c = jsonArray.getJSONObject(i);
                            String term = c.getString("term");
                            String id = c.getString("id");

                            arrayList.add(new DiagnosisDetails(term,id));

//                            terms[i] = term;


                        }
                        // terms = new String[10];
//                        terms=(String)arrayList.toArray();
                        adapter = new ArrayAdapter<DiagnosisDetails>(getActivity(), android.R.layout.select_dialog_singlechoice, arrayList);
//                        autoCompleteTextView.setThreshold(3);

                        //Set the adapter
                        autoCompleteTextView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (autoCompleteTextView != null) {
                            autoCompleteTextView.showDropDown();


                        }
                        Log.i("term", "onResponse: " + terms);
                    }
                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                Toast.makeText(getActivity(), "Error in fetching SNOMED terms.", Toast.LENGTH_SHORT).show();

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(getActivity()).addToRequestQueue(request);


    }





    private void getDiagnosisFromIcd(AutoCompleteTextView autoCompleteIcdTextView, AutoCompleteTextView autoCompleteDiseaseTextView) {
        Log.i("TAG", "getDiagnosisFromIcd: ");
        ArrayList<DiagnosisICDDetails> diagnosisDiseaseDetailsArrayList = new ArrayList<>();
        ArrayList<DiagnosisICDDetails> diagnosisCodeDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/procInvtestDtl?modeval=2&hospCode=&seatId=&deptcode=&roomNo=", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("getDiagnosisFromIcd", "onResponse: " + response);
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String diseaseCode = jsonObject1.optString("DISEASE_CODE");
                            String diseaseName = jsonObject1.optString("DISEASE_NAME");

                            DiagnosisICDDetails diagnosisICDDetails = new DiagnosisICDDetails(diseaseCode, diseaseName, false);
                            diagnosisDiseaseDetailsArrayList.add(diagnosisICDDetails);

                            DiagnosisICDDetails diagnosisICDDetails1 = new DiagnosisICDDetails(diseaseCode, diseaseName, true);
                            diagnosisCodeDetailsArrayList.add(diagnosisICDDetails1);


                        }

                        autoCompleteIcdTextView.setAdapter(new ArrayAdapter<DiagnosisICDDetails>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diagnosisCodeDetailsArrayList));
                        autoCompleteDiseaseTextView.setAdapter(new ArrayAdapter<DiagnosisICDDetails>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diagnosisDiseaseDetailsArrayList));

                    } else {
                        Toast.makeText(getContext(), "Cannot load Diagnosis", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, error -> {
            Log.i("TAG", "getDiagnosisFromIcd: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }

}
