package com.cdac.uphmis.patientprescriptionscanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.patientprescriptionscanner.fragments.ViewPrescriptionImageFragment;
import com.cdac.uphmis.patientprescriptionscanner.model.TariffDetails;

import java.util.ArrayList;

/**
 * Created by sudeep on 26-09-2018.
 */

public class CustomAdapterViewPrescription extends BaseAdapter implements Filterable {
    Context c;
   // ArrayList<TariffDetails> patientDetails;
    String deptvalue;
    private ArrayList<TariffDetails> mOriginalValues; // Original Values
    private ArrayList<TariffDetails> mDisplayedValues;
    LayoutInflater inflater;

    public CustomAdapterViewPrescription(Context c, ArrayList<TariffDetails> patientDetails) {
        this.c = c;
        //this.patientDetails = patientDetails;
        this.mOriginalValues=patientDetails;
        this.mDisplayedValues=patientDetails;
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
        TextView nameTxt,propTxt,tvVisitNo;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
      ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.prescription_scanner_model, null);

            holder.llContainer = convertView.findViewById(R.id.llContainer);
            // final TariffDetails patientDetails = (TariffDetails) this.getItem(i);

            // ImageView img= (ImageView) view.findViewById(R.id.spacecraftImg);
            holder.nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
            holder.propTxt = (TextView) convertView.findViewById(R.id.propellantTxt);
            holder.tvVisitNo = (TextView) convertView.findViewById(R.id.tv_visit_no);
           // holder.imgIsUploaded = (ImageView) convertView.findViewById(R.id.img_is_uploaded);



            //   img.setImageResource(R.drawable.logo2);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

       // final TariffDetails patientDetails= (TariffDetails) this.getItem(i);

        String date=mDisplayedValues.get(position).getEpisodeDate().substring(0,10);
        String val[]=date.split("-");
        String finaldate=val[2]+"-"+val[1]+"-"+val[0];
        holder.nameTxt.setText(mDisplayedValues.get(position).getDeptName() + " ( " + mDisplayedValues.get(position).getUnitName() + " )");
        holder.propTxt.setText("Uploaded on: "+finaldate);
        holder.tvVisitNo.setText(mDisplayedValues.get(position).getVisitNo());
        //   img.setImageResource(R.drawable.logo2);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Toast.makeText(c, patientDetails.getPatName(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(c, patientDetails.getVisitNo(), Toast.LENGTH_SHORT).show();
                deptvalue= "hello^"+mDisplayedValues.get(position).getCrNo()+"^"+mDisplayedValues.get(position).getVisitNo()+"^"+mDisplayedValues.get(position).getEpisodeCode()+"^"+mDisplayedValues.get(position).getHospitalCode();
                //deptvalue=patientDetails.getCrNo()+'^'+patientDetails.getEpisodeCode()+'^' + patientDetails.getDeptCode() + '^' + patientDetails.getVisitNo() + '^' + patientDetails.getUnitCode() + '^' + patientDetails.getHospitalCode() + '^' + patientDetails.getUnitName() + '^' + patientDetails.getDeptName() + '^' + patientDetails.getPatAddress() + '^' + patientDetails.getPatName()+'^'+patientDetails.getEpisodeDate();
                ViewPrescriptionImageFragment viewPrescriptionImageFragment = new ViewPrescriptionImageFragment();
                Bundle args = new Bundle();
                args.putString("deptValue",deptvalue);
                viewPrescriptionImageFragment.setArguments(args);
                ((Activity) c).getFragmentManager().beginTransaction().replace(R.id.container, viewPrescriptionImageFragment).commit();
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
                        String data = mOriginalValues.get(i).getDeptName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new TariffDetails(mOriginalValues.get(i).getUnitName(),mOriginalValues.get(i).getVisitNo(),mOriginalValues.get(i).getCrNo(),mOriginalValues.get(i).getDeptCode(),mOriginalValues.get(i).getDeptName(),mOriginalValues.get(i).getEpisodeCode(),mOriginalValues.get(i).getEpisodeDate(),mOriginalValues.get(i).getPatAddress(),mOriginalValues.get(i).getHospitalCode(),mOriginalValues.get(i).getUnitCode(),mOriginalValues.get(i).getPatName(),mOriginalValues.get(i).getIsUploaded()));
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
