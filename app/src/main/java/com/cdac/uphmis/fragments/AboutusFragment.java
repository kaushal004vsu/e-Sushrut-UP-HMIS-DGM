package com.cdac.uphmis.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.cdac.uphmis.R;


public class AboutusFragment extends Fragment {


    public AboutusFragment() {
    }


    TextView tv_aboutus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_aboutus, container, false);

        TextView tvMail=view.findViewById(R.id.tv_mail);

        tvMail.setText(getString(R.string.support_text));
        tvMail.setMovementMethod(LinkMovementMethod.getInstance());




        TextView tv_aboutus=  view.findViewById(R.id.tv_aboutus);

        tv_aboutus.setText("The Hospital Management Information System (HMIS) in "+getString(R.string.hosp_name)+" has been developed by CDAC NOIDA The objective of the HMIS is to provide a single window of clearance of hospital administration activity such as clinical, diagnostics, pharmacy, examinations, industrial health etc. The primary objectives of envisaged solution are:" +

                "\n\n1. Effectively manage all the health facilities & its resources"+
                "\n\n2. Monitor performance of hospitals across the administrative channel" +
                "\n\n3. Impart quality health care services to its beneficiaries" +
                "\n\n4. Improve the patient turn-around time" +
                "\n\n5. Generate and maintain EMR (electronic medical records) of all patients" +

                "\n\nThis mobile app provides various facilities for patients to access their health data across various hospitals. Patients can login using their registered mobile number to access services." +

                "\n\nThis app aims at providing timely and effective medical treatment to Patients. This mobile app is a pilot effort in this direction to streamline healthcare services delivery to its users." +
                "\n\n\n\n");


        return view;
    }

}
