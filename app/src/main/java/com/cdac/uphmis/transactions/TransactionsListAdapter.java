package com.cdac.uphmis.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionsListAdapter.ViewHolder> implements Filterable {

    private List<TransactionListDetails> filteredList;
    private List<TransactionListDetails> fullList;


    private Context context;
    public TransactionsListAdapter(List<TransactionListDetails> filteredList,Context context) {
        this.filteredList = filteredList;
        fullList = new ArrayList<>(filteredList);
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        TransactionListDetails currentItem = filteredList.get(position);
        holder.imageView.setImageResource(R.drawable.transactions_icon);
        holder.tvBillNo.setText("Bill No: "+currentItem.getTransNo());
        holder.tvTransactionDate.setText("Transaction Date: "+currentItem.getTransDate());
        holder.tvHospitalName.setText(currentItem.getHospName());
        if (currentItem.getDeposit().equalsIgnoreCase("0.00"))
        {
            holder.tvAmount.setText("₹ "+currentItem.getDeducted());
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

        }else
        {
            holder.tvAmount.setText("₹ "+currentItem.getDeposit());
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

        }

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TransactionListDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TransactionListDetails item : fullList) {
                    if (item.getTransNo().toLowerCase().contains(filterPattern)||item.getHospName().toLowerCase().contains(filterPattern)) {
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
            filteredList.clear();
            filteredList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvBillNo;
        TextView tvTransactionDate;
        TextView tvAmount;
        TextView tvHospitalName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            tvBillNo = itemView.findViewById(R.id.tv_bill_no);
            tvTransactionDate = itemView.findViewById(R.id.tv_transaction_date);
            tvHospitalName = itemView.findViewById(R.id.tv_hospital_name);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
