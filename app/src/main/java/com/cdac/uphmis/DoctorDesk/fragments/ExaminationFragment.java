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
public class ExaminationFragment extends Fragment {


    public ExaminationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_examination, container, false);
        final EditText edtExamination = view.findViewById(R.id.edt_examination);
        TextView tvClear = view.findViewById(R.id.tv_clear_examination);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtExamination.setText("");
            }
        });


        return view;
    }

}
