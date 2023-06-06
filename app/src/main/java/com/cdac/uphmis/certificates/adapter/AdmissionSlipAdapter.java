package com.cdac.uphmis.certificates.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.certificates.model.AdmissionSlipDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.cdac.uphmis.util.SessionServicecall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;

public class AdmissionSlipAdapter extends RecyclerView.Adapter<AdmissionSlipAdapter.ExampleViewHolder> implements Filterable {
    private List<AdmissionSlipDetails> exampleList;
    private List<AdmissionSlipDetails> exampleListFull;

    private ManagingSharedData msd;
    private Context context;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        private TextView tvDepartment, tvWard, tvRoom, tvUnit, tvHospital, tvAdmNo, tvDownloadAdmSlip, tvDownloadDischargeSlip;

        private ProgressBar progressBar;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
           // cardRootView = itemView.findViewById(R.id.root_view);

            tvDepartment = itemView.findViewById(R.id.tv_dept_name);
            tvUnit = itemView.findViewById(R.id.tv_unit_name);
            tvWard = itemView.findViewById(R.id.tv_ward_name);
            tvRoom = itemView.findViewById(R.id.tv_room_name);
            tvAdmNo = itemView.findViewById(R.id.tv_admission_no);
            tvHospital = itemView.findViewById(R.id.tv_hospital_name);
            tvDownloadAdmSlip = itemView.findViewById(R.id.tv_download_adm_slip);
            tvDownloadDischargeSlip = itemView.findViewById(R.id.tv_download_discharge_slip);
            progressBar = itemView.findViewById(R.id.progressbar);


        }
    }


    public AdmissionSlipAdapter(Context context, List<AdmissionSlipDetails> exampleList) {
        this.context = context;
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        msd = new ManagingSharedData(context);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admission_slip_list_row, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        AdmissionSlipDetails currentItem = exampleList.get(position);
        holder.imageView.setImageResource(R.drawable.ic_admission_slip);


        holder.tvHospital.setText(currentItem.getHospName());
        holder.tvDepartment.setText(currentItem.getDeptName());

        holder.tvWard.setText(currentItem.getWardName());
        holder.tvRoom.setText(currentItem.getRoomName());
        holder.tvUnit.setText(currentItem.getUnitName());


        holder.tvAdmNo.setText(currentItem.getAdmNo());


     /*   holder.cardRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                downloadAdmSlip(msd.getPatientDetails().getCrno(), currentItem.getAdmNo());

            }
        });*/
        try{
        if (currentItem.getProfileId().equalsIgnoreCase("0")) {
            holder.tvDownloadDischargeSlip.setVisibility(View.GONE);
        } else {
            holder.tvDownloadDischargeSlip.setVisibility(View.VISIBLE);
        }
        }
        catch(Exception ex){}

        holder.tvDownloadAdmSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAdmSlip(holder.progressBar,msd.getPatientDetails().getCrno(), currentItem.getAdmNo());
                try {
                    SessionServicecall sessionServicecall = new SessionServicecall(context);
                    sessionServicecall.saveSession(msd.getPatientDetails().getCrno(), msd.getPatientDetails().getMobileNo(), msd.getPatientDetails().getHospitalCode(), "Admission Certificate Download", "", "", "", "");
                } catch (Exception ex) {
                }
            }
        });

        holder.tvDownloadDischargeSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowmloadDischargeSlip(holder.progressBar,msd.getPatientDetails().getCrno(), currentItem.getAdmNo());


                try {
                    SessionServicecall sessionServicecall = new SessionServicecall(context);
                    sessionServicecall.saveSession(msd.getPatientDetails().getCrno(), msd.getPatientDetails().getMobileNo(), msd.getPatientDetails().getHospitalCode(), "Discharge Certificate Download", "", "", "", "");
                } catch (Exception ex) {
                }
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
            List<AdmissionSlipDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AdmissionSlipDetails item : exampleListFull) {
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


    public void downloadAdmSlip(ProgressBar progressBar,String crno, String admno) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "admissionSlipService/Print?crNo=" + crno + "&admno=" + admno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObj;

                try {
                    jsonObj = new JSONObject(response);
                    if (jsonObj.getString("status").equalsIgnoreCase("1")) {
                        String data = jsonObj.getString("AdmissionSlipBase64");

                        saveBase64Pdf(context, "certificate", "AdmissionSlip", data);
                    } else {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Something went wrong.Try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, context);
            Log.i("error", "onErrorResponse: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(request);


    }


    public void dowmloadDischargeSlip(ProgressBar progressBar,String crno, String admno) {
        progressBar.setVisibility(View.VISIBLE);

        Log.d("discharge_slip",ServiceUrl.downloadDischargeSlip + "patCrNo=" + crno + "&patAdmNo=" + admno + "&hmode=VIEWDISCHARGESUMMARYPDF_MOBILEAPP");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.downloadDischargeSlip + "patCrNo=" + crno + "&patAdmNo=" + admno + "&hmode=VIEWDISCHARGESUMMARYPDF_MOBILEAPP", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response.length() > 50) {
                    saveBase64Pdf(context, "certificate", "DischargeSlip", response);
                } else {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }


            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, context);
            Log.i("error", "onErrorResponse: " + error);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(request);


    }


}
