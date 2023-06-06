package com.cdac.uphmis.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cdac.uphmis.EstampingActivity;
import com.cdac.uphmis.QMSSlip.QMSSlipActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.SelectHospitalActitivy;
import com.cdac.uphmis.StatusActivity;
import com.cdac.uphmis.appointment.OPDAppointmentActivity;
import com.cdac.uphmis.bloodstock.BloodStockActivity;
import com.cdac.uphmis.certificates.AdmissionSlipActivity;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.lpstatus.LPStatusActivity;
import com.cdac.uphmis.ndhm.NDHMGenerateOTPActivity;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.qms.IssueTokenActivity;
import com.cdac.uphmis.reimbursement.ClaimEnquiryActivity;
import com.cdac.uphmis.sickLeave.SickLeaveActivity;
import com.cdac.uphmis.transactions.TransactionsListActivity;

import java.util.ArrayList;
import java.util.List;

public class PatientHomeFragmentAdapter extends RecyclerView.Adapter<PatientHomeFragmentAdapter.MyViewHolder> implements Filterable {
    Context context;
    OnActivityClick onActivityClick;

    private List<PatientHomeFragmentModel> exampleList;
    private List<PatientHomeFragmentModel> exampleListFull;
   // private List<String> fetaureIdArrayList;

    public PatientHomeFragmentAdapter(Context context, List<PatientHomeFragmentModel> exampleList, OnActivityClick onActivityClick) {
        this.context = context;
        this.exampleList = exampleList;
      //  this.fetaureIdArrayList = fetaureIdArrayList;
        exampleListFull = new ArrayList<>(exampleList);
        this.onActivityClick = onActivityClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fragment_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        boolean cardClick = false;

        holder.icon.setImageDrawable(exampleList.get(position).getDrawable());
        // or use DEFAULT

        holder.title.setText(exampleList.get(position).getName());
        // generate random color
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(position);
        holder.bg_color_ll.setBackgroundColor(color);
//        holder.card_cv.setBackgroundColor(color);
        holder.bg_color_ll.setOnClickListener(view -> onActivityClick.onActivityClick(position, holder.bg_color_ll, true));
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
            List<PatientHomeFragmentModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PatientHomeFragmentModel item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getId().toLowerCase().contains(filterPattern)) {
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


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        LinearLayout bg_color_ll;
        CardView card_cv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            bg_color_ll = itemView.findViewById(R.id.bg_color_ll);
            card_cv = itemView.findViewById(R.id.card_cv);
        }
    }

    public interface OnActivityClick {
        void onActivityClick(int position, LinearLayout bg_color_ll, boolean cardClick);
    }
}
