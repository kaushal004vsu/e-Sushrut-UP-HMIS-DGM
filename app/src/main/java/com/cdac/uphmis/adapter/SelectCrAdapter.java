package com.cdac.uphmis.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.LoginMainScreenActivity;
import com.cdac.uphmis.PatientDrawerHomeActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.RegistrationActivity;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.model.UMIDData;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectCrAdapter extends BaseAdapter {
    private Context c;
    private ArrayList<PatientDetails> selectCrList;
    private ManagingSharedData msd;

    public SelectCrAdapter(Context c, ArrayList<PatientDetails> selectCrList) {
        this.c = c;
        this.selectCrList = selectCrList;
    }

    @Override
    public int getCount() {
        return selectCrList.size();
    }

    @Override
    public Object getItem(int i) {
        return selectCrList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.select_cr_list_model, viewGroup, false);
        }

        final PatientDetails s = (PatientDetails) this.getItem(i);

        // ImageView img= (ImageView) view.findViewById(R.id.spacecraftImg);
        TextView tvCrno = view.findViewById(R.id.tv_crno);
        TextView tvName = view.findViewById(R.id.tv_patient_name);
        ProgressBar progressBar = view.findViewById(R.id.progressbar);
        LinearLayout llData = view.findViewById(R.id.ll_data);
        tvCrno.setText(c.getString(R.string.cr_no) + s.getCrno());


        tvName.setText(s.getFirstname() + " (" + s.getGender() + "/" + s.getAge() + ") ");
        if (s.getGender().equalsIgnoreCase("F")) {
            llData.setBackgroundColor(ContextCompat.getColor(c, R.color.female_background));
        } else {

            llData.setBackgroundColor(ContextCompat.getColor(c, R.color.male_background));
        }


        msd = new ManagingSharedData(c);
        view.setOnClickListener(view1 -> {
            msd.setWhichModuleToLogin("patientlogin");
            msd.setCrNo(s.getCrno());
            msd.setPatientDetails(s);
//            msd.setLangaugeFlag("en");
            msd.setDarkMode("no");
            c.startActivity(new Intent(c, PatientDrawerHomeActivity.class));
            ((Activity) c).finish();

//            login(progressBar));
        });


        return view;
    }


    private void login(ProgressBar progressView) {
        progressView.setVisibility(View.VISIBLE);

        //  String mobileNo=umidData.getMobileNo();
        //  String umid=umidData.getUmidNo();
        ArrayList<PatientDetails> PatientDetailsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + msd.getPatientDetails().getMobileNo(), response -> {

            Log.i("response", "onResponse: " + response);
            try {
                response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                JSONObject jsonObject = new JSONObject(response);


                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() == 0) {
                    //  msd.setUMIDData(umidData);
                    c.startActivity(new Intent(c, RegistrationActivity.class));
                } else {
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String umidData = jsonArray.getJSONObject(i).optString("UMID_DATA");
                        if (umidData.isEmpty())
                        {
                            jsonArray.getJSONObject(i).remove("UMID_DATA");
                        }
                        else
                        {
                            JSONObject jsonObject1=new JSONObject(umidData);
                            jsonArray.getJSONObject(i).put("UMID_DATA",jsonObject1);
                        }

                        PatientDetails patientDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), PatientDetails.class);
                        PatientDetailsArrayList.add(patientDetails);
                        msd.setLangaugeFlag("hi");
                        msd.setDarkMode("no");
                        msd.setWhichModuleToLogin("patientlogin");
                        msd.setCrNo(patientDetails.getCrno());
                        msd.setPatientDetails(PatientDetailsArrayList.get(i));

                    }

                    Intent intent = new Intent(new Intent(c, PatientDrawerHomeActivity.class));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    c.startActivity(intent);
                    ((Activity) c).finish();


                }


            } catch (Exception ex) {
                Log.i("ex", "onResponse: " + ex);

            }

            progressView.setVisibility(View.GONE);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, c);
                Log.i("error", "onResponse: " + error);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(c).addToRequestQueue(request);
    }
}

