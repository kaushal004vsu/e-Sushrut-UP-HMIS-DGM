package com.cdac.uphmis.labEnquiry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cdac.uphmis.R;

import java.util.ArrayList;

public class LabEnquiryAdapter extends BaseAdapter implements Filterable {
    Context c;
    //    ArrayList<LabEnquiryDetails> labEnquiryDetailsArrayList;
    private ArrayList<LabEnquiryDetails> mOriginalValues; // Original Values
    private ArrayList<LabEnquiryDetails> mDisplayedValues;
    LayoutInflater inflater;


    public LabEnquiryAdapter(Context c, ArrayList<LabEnquiryDetails> labEnquiryDetailsArrayList) {
        this.c = c;
//        this.labEnquiryDetailsArrayList = labEnquiryDetailsArrayList;

        this.mOriginalValues = labEnquiryDetailsArrayList;
        this.mDisplayedValues = labEnquiryDetailsArrayList;
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
        TextView tvTestName, tvLabName, tvTestCharge, tvIsApptBased;
//        Button btnBookAppointment;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lab_enquiry_model, null);

            //final LabEnquiryDetails labEnquiryDetails = (LabEnquiryDetails) this.getItem(i);
            holder.tvTestName = view.findViewById(R.id.tv_test_name);
            holder.tvLabName = view.findViewById(R.id.tv_lab_name);
            holder.tvTestCharge = view.findViewById(R.id.tv_test_charge);
            holder.tvIsApptBased = view.findViewById(R.id.tv_is_appt_based);

//            holder.btnBookAppointment = view.findViewById(R.id.btn_book_appointment);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        holder.btnBookAppointment.setVisibility(View.GONE);
        holder.tvTestName.setText(mDisplayedValues.get(i).getTestName());
        holder.tvLabName.setText(mDisplayedValues.get(i).getLabName());
        holder.tvIsApptBased.setText(mDisplayedValues.get(i).getIsApptBased());
//        holder.tvTestCharge.setTextSize(20.0f);
//        if (Float.parseFloat(mDisplayedValues.get(i).getTestCharge()) == 0) {
//            holder.tvTestCharge.setText("₹ NA");
//        } else {
//
//            holder.tvTestCharge.setText("₹ " + mDisplayedValues.get(i).getTestCharge());
//        }
        if (mDisplayedValues.get(i).getIsApptBased().equalsIgnoreCase("Appointment Mandatory*") && mDisplayedValues.get(i).getIsApptBooking().equalsIgnoreCase("1")) {
//            holder.btnBookAppointment.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<LabEnquiryDetails>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<LabEnquiryDetails> FilteredArrList = new ArrayList<LabEnquiryDetails>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<LabEnquiryDetails>(mDisplayedValues); // saves the original data in mOriginalValues
                }


                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getTestName();
                        String data2 = mOriginalValues.get(i).getLabName();
                        if (data.toLowerCase().contains(constraint.toString()) || data2.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new LabEnquiryDetails(mOriginalValues.get(i).getTestId(), mOriginalValues.get(i).getTestName(), mOriginalValues.get(i).getLabCode(), mOriginalValues.get(i).getLabName(), mOriginalValues.get(i).getIsApptBased(), mOriginalValues.get(i).getIsApptBooking(), mOriginalValues.get(i).getTestCharge()));
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

