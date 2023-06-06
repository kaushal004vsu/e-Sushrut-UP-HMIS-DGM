package com.cdac.uphmis.DoctorDesk.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cdac.uphmis.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClinicalNotesFragment extends Fragment {


    public ClinicalNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clinical_notes, container, false);

        final EditText edtClinicalNotes = view.findViewById(R.id.edt_clinical_notes);
        TextView tvClear = view.findViewById(R.id.tv_clear_hopi);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtClinicalNotes.setText("");
            }
        });


        return view;
    }

}
