package com.cdac.uphmis.covid19.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdac.uphmis.R;

public class PatientHelpDetailsFragment extends Fragment {

    private TextView pageHeader, pageDetails;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.help_details_view, container, false);

        pageHeader = (TextView) view.findViewById(R.id.helptitle);
        String strHeading = getArguments().getString("textHeading");
        pageHeader.setText(Html.fromHtml(strHeading));

        pageDetails = (TextView) view.findViewById(R.id.helptext);
        String strDescription = getArguments().getString("textDescription");
        pageDetails.setMovementMethod(new ScrollingMovementMethod());
        pageDetails.setMovementMethod(new LinkMovementMethod());
        pageDetails.setText(Html.fromHtml(strDescription, new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {

                int resourceId = getResources().getIdentifier(source,"drawable",getActivity().getPackageName());
                Log.e("IMG",source+" "+resourceId+" "+getActivity().getPackageName());
                Drawable drawable = getResources().getDrawable(resourceId);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                drawable.setBounds(0, 0, 100,100);
                return drawable;
            }
        }, null));


        return view;

    }
}
