package com.cdac.uphmis.TariffEnquiry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdac.uphmis.R;

import java.util.ArrayList;

public class TariffAdapter extends BaseAdapter implements Filterable {



    Context c;
    // ArrayList<TariffDetails> patientDetails;
    String deptvalue;
    private ArrayList<TariffDetails> mOriginalValues; // Original Values
    private ArrayList<TariffDetails> mDisplayedValues;
    LayoutInflater inflater;

    public TariffAdapter(Context c, ArrayList<TariffDetails> tariffDetailsList) {
        this.c = c;
        //this.patientDetails = patientDetails;
        this.mOriginalValues=tariffDetailsList;
        this.mDisplayedValues=tariffDetailsList;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class ViewHolder {
        RelativeLayout llContainer;
        TextView tariffName,patCategory,chargeType,tariffCharge;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tariff_model, null);


            holder.tariffName = (TextView) convertView.findViewById(R.id.tariff_name);
            holder.patCategory = (TextView) convertView.findViewById(R.id.pat_category);
           // holder.chargeType = (TextView) convertView.findViewById(R.id.tv_charge_type);
            holder.tariffCharge = (TextView) convertView.findViewById(R.id.tv_tariff_charge);




            //   img.setImageResource(R.drawable.logo2);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tariffName.setText(mDisplayedValues.get(position).getTariffName());
        holder.patCategory.setText(mDisplayedValues.get(position).getPatCategory());
       // holder.chargeType.setText(mDisplayedValues.get(position).getChargeType());
        holder.tariffCharge.setText("₹ "+mDisplayedValues.get(position).getTariffCharge());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return convertView;
    }



    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<TariffDetails>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<TariffDetails> FilteredArrList = new ArrayList<TariffDetails>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<TariffDetails>(mDisplayedValues); // saves the original data in mOriginalValues
                }
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getTariffName();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new TariffDetails(mOriginalValues.get(i).getPatCategory(),mOriginalValues.get(i).getTariffName(),mOriginalValues.get(i).getTariffCharge()));
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
