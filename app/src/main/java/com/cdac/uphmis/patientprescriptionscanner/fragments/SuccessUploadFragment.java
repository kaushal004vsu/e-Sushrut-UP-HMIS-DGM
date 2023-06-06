package com.cdac.uphmis.patientprescriptionscanner.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdac.uphmis.R;


public class SuccessUploadFragment extends Fragment {

    TextView successStatus;

    public SuccessUploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_success_upload, container, false);

        String strDeptValue = getArguments().getString("deptValue");
        Log.i("strcrno", "onCreateView: "+strDeptValue);

        String temp=strDeptValue.replaceAll("\\^", "#");
        final String val[]=temp.split("#");


        successStatus=(TextView)view.findViewById(R.id.card_text);


        successStatus.setText("Prescription of CR Number "+val[0]+" for your visit to "+val[7]+" on "+val[10].substring(0,10)+" has been successfully uploaded.\n");
       // Log.i("upload image respone", "onResponse: "+response);

        return view;
    }

}
