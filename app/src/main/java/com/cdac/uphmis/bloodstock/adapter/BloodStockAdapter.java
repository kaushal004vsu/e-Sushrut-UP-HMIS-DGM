package com.cdac.uphmis.bloodstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.bloodstock.model.BloodStockDetails;

import java.util.ArrayList;


/**
 * Created by sudeep on 26-01-2019.
 */

public class BloodStockAdapter extends BaseAdapter {

    Context c;
    ArrayList<BloodStockDetails> reportListDetails;

    public BloodStockAdapter(Context c, ArrayList<BloodStockDetails> reportListDetails) {
        this.c = c;
        this.reportListDetails = reportListDetails;
    }

    @Override
    public int getCount() {
        return reportListDetails.size();
    }

    @Override
    public Object getItem(int i) {
        return reportListDetails.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.blood_stock_model, viewGroup, false);
        }

        final BloodStockDetails bloodStockDetails = (BloodStockDetails) this.getItem(i);
        TextView bloodComponent=(TextView)view.findViewById(R.id.blood_component);
        TextView tvOneg=(TextView)view.findViewById(R.id.tv_oneg);
        TextView tvAneg=(TextView)view.findViewById(R.id.tv_aneg);
        TextView tvOpos=(TextView)view.findViewById(R.id.tv_opos);
        TextView tvApos=(TextView)view.findViewById(R.id.tv_apos);
        TextView tvAbpos=(TextView)view.findViewById(R.id.tv_abpos);
        TextView tvBpos=(TextView)view.findViewById(R.id.tv_bpos);
        TextView tvBneg=(TextView)view.findViewById(R.id.tv_bneg);
        TextView tvAbneg=(TextView)view.findViewById(R.id.tv_abneg);

        TextView tvStockNotAvailable=(TextView)view.findViewById(R.id.tv_stock_not_available);
        tvStockNotAvailable.setVisibility(View.GONE);


        LinearLayout llOneg=view.findViewById(R.id.ll_oneg);
        LinearLayout llAneg=view.findViewById(R.id.ll_aneg);
        LinearLayout llOpos=view.findViewById(R.id.ll_opos);
        LinearLayout llApos=view.findViewById(R.id.ll_apos);
        LinearLayout llAbpos=view.findViewById(R.id.ll_abpos);
        LinearLayout llBpos=view.findViewById(R.id.ll_bpos);
        LinearLayout llBneg=view.findViewById(R.id.ll_bneg);
        LinearLayout llAbneg=view.findViewById(R.id.ll_abneg);

        llOneg.setVisibility(View.VISIBLE);
        llAneg.setVisibility(View.VISIBLE);
        llOpos.setVisibility(View.VISIBLE);
        llApos.setVisibility(View.VISIBLE);
        llAbpos.setVisibility(View.VISIBLE);
        llBpos.setVisibility(View.VISIBLE);
        llBneg.setVisibility(View.VISIBLE);
        llAbneg.setVisibility(View.VISIBLE);



        String oneg=bloodStockDetails.getOneg();
        String aneg=bloodStockDetails.getAneg();
        String opos=bloodStockDetails.getOpos();
        String apos=bloodStockDetails.getApos();
        String abpos=bloodStockDetails.getABpos();
        String bpos=bloodStockDetails.getBpos();
        String bneg=bloodStockDetails.getBneg();
        String abneg=bloodStockDetails.getABneg();
        bloodComponent.setText(bloodStockDetails.getBloodComponent());
        if (bloodStockDetails.getOneg().equalsIgnoreCase("0"))
        {
            oneg="";
            llOneg.setVisibility(View.GONE);
        }
        if (bloodStockDetails.getAneg().equalsIgnoreCase("0"))
        {
            aneg="";
            llAneg.setVisibility(View.GONE);
        }
        if (bloodStockDetails.getOpos().equalsIgnoreCase("0"))
        {
            opos="";
            llOpos.setVisibility(View.GONE);
        }
        if (bloodStockDetails.getApos().equalsIgnoreCase("0"))
        {
            apos="";
            llApos.setVisibility(View.GONE);
        }
        if (bloodStockDetails.getABpos().equalsIgnoreCase("0"))
        {
            abpos="";
            llAbpos.setVisibility(View.GONE);
        }
        if (bloodStockDetails.getBpos().equalsIgnoreCase("0"))
        {
            bpos="";
            llBpos.setVisibility(View.GONE);
        }
        if (bloodStockDetails.getBneg().equalsIgnoreCase("0"))
        {
            bneg="";
            llBneg.setVisibility(View.GONE);
        }
        if ((bloodStockDetails.getABneg().equalsIgnoreCase("0"))||bloodStockDetails.getABneg().equalsIgnoreCase(""))
        {
            abneg="";
           llAbneg.setVisibility(View.GONE);
        }

        String displayText=" "+oneg+"  "+aneg+"  "+opos+"   "+apos+"   "+abpos+"   "+bpos+"   "+bneg+"   "+abneg;
        if (displayText.trim().equalsIgnoreCase("")||displayText==null)
        {
           // displayText="Stock Not Available";
            tvStockNotAvailable.setVisibility(View.VISIBLE);
           // availability.setBackgroundResource(R.color.nostockavailable);
          //  availability.setText(displayText);
        }
        else
        {
            //availability.setBackgroundResource(android.R.color.holo_green_dark);
            //availability.setText(displayText);
            tvOneg.setText(oneg);
            tvAneg.setText(aneg);
            tvOpos.setText(opos);
            tvApos.setText(apos);
            tvAbpos.setText(abpos);
            tvBpos.setText(bpos);
            tvBneg.setText(bneg);
            tvAbneg.setText(abneg);
        }


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                downloadPrdf(reportListDetails.getReqNo());
//                progressView.setVisibility(View.VISIBLE);
//            }
//        });

        return view;
    }





}
