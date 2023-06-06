package com.cdac.uphmis.drugavailability.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.drugavailability.model.DrugAvailabilityDetails;

import java.util.ArrayList;


public class DrugAvailabilityAdapter extends BaseAdapter implements Filterable{
    Context c;
    private ArrayList<DrugAvailabilityDetails> mOriginalValues; // Original Values
    private ArrayList<DrugAvailabilityDetails> mDisplayedValues;
    LayoutInflater inflater;
    public DrugAvailabilityAdapter(Context c, ArrayList<DrugAvailabilityDetails> patientDetails) {
        this.c = c;
        this.mOriginalValues=patientDetails;
        this.mDisplayedValues=patientDetails;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView tvDrugName,tvDrugQuantity;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.drug_availability_model, null);


            holder.tvDrugName = (TextView) convertView.findViewById(R.id.tv_drug_name);
            holder.tvDrugQuantity = (TextView) convertView.findViewById(R.id.tv_drug_quantity);
             convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvDrugName.setText(mDisplayedValues.get(position).getStoreName());
        holder.tvDrugQuantity.setText(mDisplayedValues.get(position).getDrugQuantity());

       /* convertView.setOnClickListener(v -> {

        });*/

        return convertView;
    }



    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<DrugAvailabilityDetails>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<DrugAvailabilityDetails> FilteredArrList = new ArrayList<DrugAvailabilityDetails>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<DrugAvailabilityDetails>(mDisplayedValues); // saves the original data in mOriginalValues
                }


                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getDrugName();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new DrugAvailabilityDetails(mOriginalValues.get(i).getStoreName(),mOriginalValues.get(i).getDrugName(),mOriginalValues.get(i).getDrugQuantity()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
