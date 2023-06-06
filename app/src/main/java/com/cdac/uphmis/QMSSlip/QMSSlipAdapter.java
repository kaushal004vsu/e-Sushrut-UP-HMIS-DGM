package com.cdac.uphmis.QMSSlip;

import android.content.Context;
import android.content.Intent;
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

import com.cdac.uphmis.QMSSlip.model.QMSDetails;
import com.cdac.uphmis.R;

import java.util.ArrayList;
import java.util.List;

class QMSSlipAdapter extends RecyclerView.Adapter<QMSSlipAdapter.ExampleViewHolder> implements Filterable {
    private List<QMSDetails> exampleList;
    private List<QMSDetails> exampleListFull;
    Context context;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvHospitalName, tvPatName, tvCrno, tvDeptUnit, tvVisitDate, tvVisitNo,tvGeneratedOn;
        CardView cardRootView;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            tvHospitalName = itemView.findViewById(R.id.tv_hospital_name);
            tvPatName = itemView.findViewById(R.id.tv_pat_name);
            tvCrno = itemView.findViewById(R.id.tv_crno);
            tvDeptUnit = itemView.findViewById(R.id.tv_dept_unit);
            tvVisitDate = itemView.findViewById(R.id.tv_visit_date);
            tvVisitNo = itemView.findViewById(R.id.tv_visit_no);
            cardRootView = itemView.findViewById(R.id.root_view);
            tvGeneratedOn = itemView.findViewById(R.id.tv_token_generation);
        }

    }

    public QMSSlipAdapter(Context context, List<QMSDetails> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        this.context=context;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qms_layout_row, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        QMSDetails currentItem = exampleList.get(position);
        holder.imageView.setImageResource(R.drawable.tests);
        holder.tvHospitalName.setText(currentItem.getHospname());
        holder.tvPatName.setText(currentItem.getPatname());
        holder.tvCrno.setText("CR No : "+currentItem.getPatcrno());
        holder.tvDeptUnit.setText(currentItem.getDeptunitname());
        holder.tvVisitDate.setText("Visit Date: " + currentItem.getPrintedon());
        holder.tvVisitNo.setText("Visit No: " + currentItem.getVisitno());
        holder.tvGeneratedOn.setText("Token Generated on: "+currentItem.getEntryDate());


        holder.cardRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,QMSBarCodeActivity.class);
                intent.putExtra("qmsDetails",currentItem);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<QMSDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (QMSDetails item : exampleListFull) {
                    if (item.getHospname().toLowerCase().contains(filterPattern) || item.getDeptname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}