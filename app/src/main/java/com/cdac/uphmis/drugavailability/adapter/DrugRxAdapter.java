package com.cdac.uphmis.drugavailability.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.drugavailability.DrugRxActivity;
import com.cdac.uphmis.drugavailability.model.expandableList.AllDatum;
//import com.cdac.uphmis.drugavailability.model.expandableList.DrugRxModel;

import java.util.List;

public class DrugRxAdapter extends RecyclerView.Adapter<DrugRxAdapter.MyViewHolder> {
    Context context;
    List<AllDatum> list;
    DrugListAdapter adapter;
    public DrugRxAdapter(Context context, List<AllDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drug_rx,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try{
            holder.hosp_name.setText(""+list.get(position).getEncounterDetail().getDepartment()
                    +" ("+list.get(position).getEncounterDetail().getUnit()+")"
                    +" "+list.get(position).getEncounterDetail().getHospitalName()
                    +" Visit Type: "+list.get(position).getEncounterDetail().getEncounterType()+" "
                    +" Visit Date: "+list.get(position).getEncounterDetail().getEncounterDate()
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.drug_recylerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
        adapter = new DrugListAdapter(context,list.get(position).getMedications());
        holder.drug_recylerview.setLayoutManager(layoutManager);
        holder.drug_recylerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hosp_name;
        RecyclerView drug_recylerview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hosp_name=itemView.findViewById(R.id.tv_hosp_name);
            drug_recylerview=itemView.findViewById(R.id.drug_recylerview);
        }
    }

}
