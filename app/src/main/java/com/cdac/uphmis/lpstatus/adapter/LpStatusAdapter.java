package com.cdac.uphmis.lpstatus.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.lpstatus.model.LPSTatusDetails;
import com.cdac.uphmis.precriptionView.adapter.PrescriptionAdapter;

import java.util.List;

public class LpStatusAdapter extends RecyclerView.Adapter<LpStatusAdapter.MyHolder> {
    Context context;
    List<LPSTatusDetails> list;

    public LpStatusAdapter(Context context, List<LPSTatusDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lps_list_row, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.s_no.setText("" + list.get(position).getS_no() + ". ");
        holder.item_name.setText("" + list.get(position).getItem_name());
        holder.date.setText("Req Date: " + list.get(position).getDate() + "");
        holder.app_qty.setText("QTY : " + list.get(position).getApp_qty());
        holder.tv_hospital_name.setText(list.get(position).getHosp_name() + "");
        holder.status_tv.setText(Html.fromHtml(list.get(position).getStatuss().trim()));
        Log.d("sttats",holder.status_tv.getText().toString());
        if (holder.status_tv.getText().toString().trim().equalsIgnoreCase("Approved")) {
            holder.status_image.setImageResource(R.drawable.check);
        } else if (holder.status_tv.getText().toString().trim().equalsIgnoreCase("Processed")) {
         //   holder.status_image.setImageResource(R.drawable.processing);
        } else {
           // holder.status_image.setImageResource(R.drawable.unchecked);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView s_no, item_name, date, app_qty, tv_hospital_name, status_tv;
        ImageView status_image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            s_no = itemView.findViewById(R.id.s_no);
            item_name = itemView.findViewById(R.id.item_name);
            date = itemView.findViewById(R.id.date);
            app_qty = itemView.findViewById(R.id.app_qty);
            tv_hospital_name = itemView.findViewById(R.id.tv_hospital_name);
            status_tv = itemView.findViewById(R.id.status_tv);
            status_image = itemView.findViewById(R.id.status_image);
        }
    }
}
