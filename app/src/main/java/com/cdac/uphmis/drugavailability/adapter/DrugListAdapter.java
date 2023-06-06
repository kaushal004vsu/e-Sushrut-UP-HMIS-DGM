package com.cdac.uphmis.drugavailability.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.drugavailability.model.expandableList.Medication;

import java.util.List;

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder> {
    Context context;
    List<Medication> list;

    public DrugListAdapter(Context context, List<Medication> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_of_drug,parent,false);
        return new DrugViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugViewHolder holder, int position) {
        holder.drug_name.setText(""+list.get(position).getDrugName());
        holder.advice_date.setText(""+list.get(position).getAdviseDate());
        holder.advice_qty.setText(""+list.get(position).getAdvicedQty());
        holder.issue_qty.setText(""+list.get(position).getIssueQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DrugViewHolder extends RecyclerView.ViewHolder {
        TextView drug_name,advice_date,advice_qty,issue_qty;
        public DrugViewHolder(@NonNull View itemView) {
            super(itemView);
            drug_name=itemView.findViewById(R.id.tv_drug_name);
            advice_date=itemView.findViewById(R.id.tv_advice_date);
            advice_qty=itemView.findViewById(R.id.tv_advice_qty);
            issue_qty=itemView.findViewById(R.id.tv_issue_qty);

        }
    }
}
