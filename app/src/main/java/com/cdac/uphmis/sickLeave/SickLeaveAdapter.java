package com.cdac.uphmis.sickLeave;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.EstampingActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.precriptionView.model.PrescriptionListDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.SessionServicecall;


import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;


public class SickLeaveAdapter extends RecyclerView.Adapter<SickLeaveAdapter.ExampleViewHolder> implements Filterable {
    private List<SickLeaveDetails> exampleList;
    private List<SickLeaveDetails> exampleListFull;

    private ManagingSharedData msd;
    private Context context;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        private TextView tvDepartment, tvCategory, tvRequestDate, tvStartDate, tvEndDate, tvPeriod, tvSickType;
        private CardView cardRootView;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            cardRootView = itemView.findViewById(R.id.root_view);

            tvDepartment = itemView.findViewById(R.id.tv_department);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvRequestDate = itemView.findViewById(R.id.tv_request_date);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tvEndDate = itemView.findViewById(R.id.tv_end_date);
            tvPeriod = itemView.findViewById(R.id.tv_period);
            tvSickType = itemView.findViewById(R.id.tv_sick_type);


        }
    }


    public SickLeaveAdapter(Context context, List<SickLeaveDetails> exampleList) {
        this.context = context;
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        msd = new ManagingSharedData(context);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sick_leave_list_row, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        SickLeaveDetails currentItem = exampleList.get(position);
        holder.imageView.setImageResource(R.drawable.certificate);


        holder.tvDepartment.setText(currentItem.getDepartment());
        holder.tvCategory.setText("Category: " + currentItem.getCategory());
        holder.tvRequestDate.setText("Sick Period From: ");
        holder.tvStartDate.setText(currentItem.getSickStart());
        holder.tvEndDate.setText(currentItem.getSickEnd());

        holder.tvPeriod.setText(currentItem.getSickPeriod());
        holder.tvPeriod.setVisibility(View.GONE);
        holder.tvSickType.setText("Sick Type: "+currentItem.getSickType());


        holder.cardRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] arPrint = currentItem.getPrint().split("#");
                String certno = arPrint[0];
//                   arPrint[1];
//                   arPrint[2];
                String slno = arPrint[3];
                String reqno = arPrint[4];

                    downloadReport(msd.getPatientDetails().getHospitalCode(),msd.getPatientDetails().getCrno(),certno,reqno,slno);

                try {
                    SessionServicecall sessionServicecall = new SessionServicecall(context);
                    sessionServicecall.saveSession(msd.getPatientDetails().getCrno(), msd.getPatientDetails().getMobileNo(),msd.getPatientDetails().getHospitalCode(), "Sick Certificate Download", "", "", "", "");
                }catch(Exception ex){}
//                downloadReport();
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
            List<SickLeaveDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SickLeaveDetails item : exampleListFull) {
                    if (item.getDepartment().toLowerCase().contains(filterPattern)) {
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


    public void downloadReport(String hospCode,String crno,String certNo,String reqNo,String slno) {
//        progressView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl+"sickLeaveService/printSickDtl?hospcode="+hospCode+"&crno="+crno+"&certno="+certNo+"&reqno="+reqNo+"&slno="+slno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressView.setVisibility(View.GONE);
//                Log.i("response is ", "onResponse: " + response);
//                progressView.setVisibility(View.GONE);
                JSONObject jsonObj;

                try {
                    jsonObj = new JSONObject(response);
                    if (jsonObj.getString("status").equalsIgnoreCase("1")) {
                        String data = jsonObj.getString("BillBase64");

                        saveBase64Pdf(context, "sickcertificate", "certificate", data);
                    } else {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, error -> {
            //     progressView.setVisibility(View.GONE);
//                Snackbar.make(progressView, "Unable to connect", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(context, "Unable to connect", Toast.LENGTH_SHORT).show();
            Log.i("error", "onErrorResponse: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(request);


    }


}
