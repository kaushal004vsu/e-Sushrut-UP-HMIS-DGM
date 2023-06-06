package com.cdac.uphmis.DoctorDesk.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import com.cdac.uphmis.R;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChiefComplaintFragment extends Fragment {

EditText edtChiefComplaint;
    public ChiefComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chief_complaint, container, false);

        edtChiefComplaint=view.findViewById(R.id.edt_chief_complaint);

        TextView tvClear=view.findViewById(R.id.tv_clear_chief_complaint);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtChiefComplaint.setText("");
            }
        });
//        getSpeechInput();


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                   edtChiefComplaint.append("\n"+result.get(0));

                }

                break;

        }
    }



    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() .toString());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getActivity(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

}
