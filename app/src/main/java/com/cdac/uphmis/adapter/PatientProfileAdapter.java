package com.cdac.uphmis.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.cdac.uphmis.R;
import com.cdac.uphmis.model.PatientProfileDetails;

import java.util.ArrayList;
import java.util.List;

public class PatientProfileAdapter  extends RecyclerView.Adapter<PatientProfileAdapter.ExampleViewHolder> {
        private List<PatientProfileDetails> exampleList;


        Context context;

        class ExampleViewHolder extends RecyclerView.ViewHolder {
//            ImageView imageView;
            TextView tvTitle,tvDescription;
//            CardView cardRootView;

            ExampleViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvDescription = itemView.findViewById(R.id.tv_description);
            }

        }

        public PatientProfileAdapter(Context context, List<PatientProfileDetails> exampleList) {
            this.exampleList = exampleList;
            this.context=context;
        }

        @NonNull
        @Override
        public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_profile_row, parent, false);
            return new ExampleViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
            PatientProfileDetails currentItem = exampleList.get(position);
            holder.tvTitle.setText(currentItem.getTitle());
            holder.tvDescription.setText(Html.fromHtml(currentItem.getDescription()));
        }

        @Override
        public int getItemCount() {
            return exampleList.size();
        }


    }