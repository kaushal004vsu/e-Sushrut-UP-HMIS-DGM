package com.cdac.uphmis.precriptionView.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.DoctorDesk.DeskActivity;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.DeskHomeActivity;
import com.cdac.uphmis.precriptionView.model.LabReportListDetails;
import com.cdac.uphmis.tracker.InvestigationDetail;
import com.cdac.uphmis.tracker.OrderStatusAdapter;
import com.cdac.uphmis.tracker.OrderStatusModel;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.ManagingSharedData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabReportListAdapter extends RecyclerView.Adapter<LabReportListAdapter.ExampleViewHolder> implements Filterable {
    private List<InvestigationDetail> exampleList;
    private List<InvestigationDetail> exampleListFull;
    Context context;
    private RequestQueue requestQueue;
    OnReportClick onReportClick;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTestName;
        TextView tvReportDate;
        TextView tvHospitalName;
        TextView tv_status;


        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            tvTestName = itemView.findViewById(R.id.tv_test_name);
            tvReportDate = itemView.findViewById(R.id.tv_report_date);
            tvHospitalName = itemView.findViewById(R.id.tv_hospital_name);
            tv_status = itemView.findViewById(R.id.tv_status);
        }

    }

    public LabReportListAdapter(Context context, List<InvestigationDetail> exampleList,OnReportClick onReportClick) {
        this.exampleList = exampleList;
        this.context = context;
        this.onReportClick = onReportClick;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_report_list_row, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        InvestigationDetail currentItem = exampleList.get(position);
        holder.imageView.setImageResource(R.drawable.tests);
        holder.tvTestName.setText(currentItem.getTestname());
        holder.tvReportDate.setText("Visit Date: " + currentItem.getReportDate());
        holder.tvHospitalName.setText(currentItem.getHospName());
        //holder.tv_status.setText(currentItem.getStatus_covered());

        if (currentItem.getStatus_no().equals("6") && currentItem.getGnumSampleCode().equals("-1")){
            holder.tv_status.setText("Patient Accepted");
        }else {
            holder.tv_status.setText(currentItem.getStatus_covered());
        }
      /* if (currentItem.getGnumSampleCode().equals("-1")){
            holder.tv_status.setText("Patient Accepted");
        }else {
            holder.tv_status.setText(currentItem.getStatus_covered());
        }*/

        if (currentItem.getStatus_no().equals("14") || currentItem.getStatus_no().equals("26")) {
            holder.tv_status.setTextColor(Color.parseColor("#00735C"));
        } else {
            holder.tv_status.setTextColor(Color.parseColor("#808080"));
        }

     /*   holder.tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getStatus_no().equals("14") || currentItem.getStatus_no().equals("26")) {
                onReportClick.OnReportClick(position);
            } else {
                trackingDialog(currentItem);
            }
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.getStatus_no().equals("14") || currentItem.getStatus_no().equals("26")) {
                    onReportClick.OnReportClick(position);
                } else {
                    trackingDialog(currentItem);
                }
            }
        });
    }

    private void trackingDialog(InvestigationDetail currentItem) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout.tracker_dialog, null);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        ListView listView = dialog.findViewById(R.id.list);

        getInvestigationList(listView, currentItem);

        dialog.show();

    }

    private void getInvestigationList(ListView listView, InvestigationDetail currentItem) {

        ArrayList<OrderStatusModel> list = new ArrayList<>();

        Log.d("getStatus_covered", "" + currentItem.getStatus_no());
        if (currentItem.getStatus_no().equals("56")) {
            list.add(new OrderStatusModel(AppConstants.REQUISTION_RAISED + "(Appointment Pending)", currentItem.getRequisitionDate(), currentItem.getStatus_covered(),R.drawable.online_requisition));
        } else {
            list.add(new OrderStatusModel(AppConstants.REQUISTION_RAISED, currentItem.getRequisitionDate(), currentItem.getStatus_covered(),R.drawable.online_requisition));
        }
        if (currentItem.getGnumSampleCode().equals("-1")) {
            list.add(new OrderStatusModel("Patient Accepted", currentItem.getSampleCollectionDate(), currentItem.getStatus_covered(),R.drawable.sample_collection));
            list.add(new OrderStatusModel(AppConstants.RESULT_ENTERED, currentItem.getResultEntryDate(), currentItem.getStatus_covered(),R.drawable.result_entry));
            list.add(new OrderStatusModel(AppConstants.RESULT_VALIDTED, currentItem.getResultValidationDate(), currentItem.getStatus_covered(),R.drawable.result_validation));
            list.add(new OrderStatusModel(AppConstants.REPORT_GENERATED, currentItem.getReportGenerationDate(), currentItem.getStatus_covered(),R.drawable.report_generation));
            if (currentItem.getStatus_no().equals("14")) {
                list.add(new OrderStatusModel(AppConstants.REPORT_PRINTED, currentItem.getReportPrintDate(), currentItem.getStatus_covered(),R.drawable.check_mark_ic));
            }else{
                list.add(new OrderStatusModel(AppConstants.REPORT_PRINTED, currentItem.getReportPrintDate(), currentItem.getStatus_covered(),R.drawable.report_printing));
            }
        } else {
            list.add(new OrderStatusModel(AppConstants.SAMPLE_COLLECTED, currentItem.getSampleCollectionDate(), currentItem.getStatus_covered(),R.drawable.sample_collection));
            list.add(new OrderStatusModel(AppConstants.PACKING_LIST_GENERATED, currentItem.getPackingListDate(), currentItem.getStatus_covered(),R.drawable.packing_list_generation));
            list.add(new OrderStatusModel(AppConstants.SAMPLE_ACCEPTED, currentItem.getAccpetanceDate(), currentItem.getStatus_covered(),R.drawable.sample_acceptance));
            list.add(new OrderStatusModel(AppConstants.RESULT_ENTERED, currentItem.getResultEntryDate(), currentItem.getStatus_covered(),R.drawable.result_entry));
            list.add(new OrderStatusModel(AppConstants.RESULT_VALIDTED, currentItem.getResultValidationDate(), currentItem.getStatus_covered(),R.drawable.result_validation));
            list.add(new OrderStatusModel(AppConstants.REPORT_GENERATED, currentItem.getReportGenerationDate(), currentItem.getStatus_covered(),R.drawable.report_generation));
            if (currentItem.getStatus_no().equals("14")) {
                list.add(new OrderStatusModel(AppConstants.REPORT_PRINTED, currentItem.getReportPrintDate(), currentItem.getStatus_covered(),R.drawable.check_mark_ic));
            }else{
                list.add(new OrderStatusModel(AppConstants.REPORT_PRINTED, currentItem.getReportPrintDate(), currentItem.getStatus_covered(),R.drawable.report_printing));
            }
        }

        OrderStatusAdapter adapter = new OrderStatusAdapter(context, list,exampleList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public int getItemViewType(int position) {
        return position;
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
            List<InvestigationDetail> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (InvestigationDetail item : exampleListFull) {
                    if (item.getTestname().toLowerCase().contains(filterPattern)) {
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

    public interface OnReportClick{
        void OnReportClick(int position);
    }
}