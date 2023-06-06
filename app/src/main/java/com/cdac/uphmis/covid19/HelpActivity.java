package com.cdac.uphmis.covid19;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.adapter.PatientHelpListAdapter;
import com.cdac.uphmis.covid19.fragments.PatientHelpDetailsFragment;

public class HelpActivity extends AppCompatActivity {
    ListView list;

    String[] maintitle = {
            "Login",
            "Quick Steps",
            "Teleconsultancy Request",
            "Document Upload",
            "Consultation and Status",
            "ePrescription",
            "Permissions",
            "Others",
    };



    String[] subtitle = {
            "How do I login?",
            "Quick Steps for Teleconsultancy",
            "How to raise a new request?",
            "How to upload supporting documents?",
            "How to check Consultation and Status?",
            "How to view prescription?",
            "What permissions does the app need?",
            "Any other issue?",
    };

    Integer[] imgid = {
            R.drawable.ic_keyboard_arrow_right_gray_24dp,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PatientHelpListAdapter adapter = new PatientHelpListAdapter(this, maintitle, subtitle, imgid);
        list =findViewById(R.id.list);
        list.setAdapter(adapter);

        final Bundle bundle = new Bundle();
        list.setOnItemClickListener((parent, view, position, id) -> {

            if (position == 0) {
                //  Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
                bundle.putString("textHeading", getResources().getString(R.string.how_to_login));
                bundle.putString("textDescription", getResources().getString(R.string.how_to_login_description));
            } else if (position == 1) {
                bundle.putString("textHeading", getResources().getString(R.string.quick_steps));
                bundle.putString("textDescription", getResources().getString(R.string.quick_steps_description));
            } else if (position == 2) {
                bundle.putString("textHeading", getResources().getString(R.string.how_to_raise_a_new_request));
                bundle.putString("textDescription", getResources().getString(R.string.how_to_raise_a_new_request_description));
            } else if (position == 3) {
                bundle.putString("textHeading", getResources().getString(R.string.how_to_upload_documents));
                bundle.putString("textDescription", getResources().getString(R.string.how_to_upload_documents_description));
            } else if (position == 4) {
                bundle.putString("textHeading", getResources().getString(R.string.how_to_track_request));
                bundle.putString("textDescription", getResources().getString(R.string.how_to_track_request_description));
            } else if (position == 5) {
                bundle.putString("textHeading", getResources().getString(R.string.how_to_view_prescription));
                bundle.putString("textDescription", getResources().getString(R.string.how_to_view_prescription_description));
            }
            /*else if (position == 6) {
                bundle.putString("textHeading", getResources().getString(R.string.other_issue));
                bundle.putString("textDescription", getResources().getString(R.string.other_issue_description));
            }*/


            else if(position == 6) {
                bundle.putString("textHeading", getResources().getString(R.string.permission_needed));
                bundle.putString("textDescription", getResources().getString(R.string.permission_needed_description));
            }
            else if(position == 7) {
                bundle.putString("textHeading", getResources().getString(R.string.other_issue));
                bundle.putString("textDescription", getResources().getString(R.string.other_issue_description));
            }

            PatientHelpDetailsFragment patientHelpDetailsFragment = new PatientHelpDetailsFragment();
            patientHelpDetailsFragment.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.helpmainactivitylayout, patientHelpDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void helpBackButton(View view) {
        getFragmentManager().popBackStackImmediate();
    }
}