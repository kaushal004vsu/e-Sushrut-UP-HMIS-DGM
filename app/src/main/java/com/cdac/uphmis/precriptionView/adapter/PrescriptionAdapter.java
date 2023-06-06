package com.cdac.uphmis.precriptionView.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.precriptionView.model.PrescriptionListDetails;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionAdapter extends
        RecyclerView.Adapter<PrescriptionAdapter.ExampleViewHolder> implements Filterable {
    private List<PrescriptionListDetails> exampleList;
    private List<PrescriptionListDetails> exampleListFull;
    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvDeptName;
        TextView tvVisiDate;
        TextView tvVisitNo;
        TextView tvHospitalName;
        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            tvDeptName = itemView.findViewById(R.id.tv_dept_name);
            tvVisiDate = itemView.findViewById(R.id.tv_visit_date);
            tvVisitNo = itemView.findViewById(R.id.tv_visit_no);
            tvHospitalName = itemView.findViewById(R.id.tv_hospital_name);
        }
    }




    public PrescriptionAdapter(List<PrescriptionListDetails> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_list_row, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        PrescriptionListDetails currentItem = exampleList.get(position);
        holder.imageView.setImageResource(R.drawable.ic_prescription);
        holder.tvDeptName.setText(currentItem.getDeptName());
        holder.tvVisiDate.setText("Visit Date: "+currentItem.getVisiDate());
        holder.tvVisitNo.setText("Visit No: "+currentItem.getVisitNo());
        holder.tvHospitalName.setText(currentItem.getHospName());
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
            List<PrescriptionListDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PrescriptionListDetails item : exampleListFull) {
                    if (item.getDeptName().toLowerCase().contains(filterPattern)) {
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