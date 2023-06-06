package com.cdac.uphmis.covid19.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.model.DateDetails;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.viewHolder> {

    Context context;
    ArrayList<DateDetails> arrayList;
    int row_index;

    public DateAdapter(Context context, ArrayList<DateDetails> arrayList,int row_index) {
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
    public void onBindViewHolder(viewHolder viewHolder, int position) {


            viewHolder.tvDate.setText(arrayList.get(position).getDisplayDate());

        Log.i("rowindex", "onBindViewHolder: "+row_index);
        viewHolder.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();

                Log.i("rowindex", "onBindViewHolder: "+row_index);
            }
        });
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
//            tvSlotsAvailable = (TextView) itemView.findViewById(R.id.tv_available_slots);
            llDate = itemView.findViewById(R.id.ll_date);


        }
    }

}