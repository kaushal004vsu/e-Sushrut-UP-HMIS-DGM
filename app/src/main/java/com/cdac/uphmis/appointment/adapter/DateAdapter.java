package com.cdac.uphmis.appointment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.model.DateDetails;
import com.cdac.uphmis.appointment.model.DateSlotDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.viewHolder> {

    Context context;
    ArrayList<DateSlotDetails> arrayList;
    public int row_index;

    public DateAdapter(Context context, ArrayList<DateSlotDetails> arrayList, int row_index) {
        this.context = context;
        this.arrayList = arrayList;
        this.row_index=row_index;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_items_model, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, final int position) {
//        viewHolder.tvDate.setText(arrayList.get(position).getApp_date());]
        if(position == 0){
            viewHolder.tvDate.setText("Today, "+AppUtilityFunctions.changeDateFormat(""+arrayList.get(position).getApp_date(),"dd-MMM-yyyy","EEE, dd MMM"));
        }else {
            viewHolder.tvDate.setText(AppUtilityFunctions.changeDateFormat(""+arrayList.get(position).getApp_date(),"dd-MMM-yyyy","EEE, dd MMM"));
        }

        Log.i("rowindex", "onBindViewHolder: "+row_index);
    /*    viewHolder.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();

                Log.i("rowindex", "onBindViewHolder: "+row_index);
            }
        });*/
        Log.i("rowindex", "onBindViewHolder: "+row_index);
        if (row_index == position) {
            viewHolder.llDate.setBackgroundResource(R.drawable.selected_date_bacground);

        } else {
            viewHolder.llDate.setBackgroundResource(R.drawable.date_background);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        LinearLayout llDate;

        public viewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            llDate = itemView.findViewById(R.id.ll_date);
        }
    }

}