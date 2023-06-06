package com.cdac.uphmis.home.slider;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.cdac.uphmis.EstampingActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.OPDAppointmentActivity;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.ndhm.NDHMGenerateOTPActivity;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.util.AppConstants;
import com.cdac.uphmis.util.AppUtilityFunctions;


import java.util.ArrayList;


/**
 * Created by silence12 on 26/10/17.
 */

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<SliderImageModel> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, ArrayList<SliderImageModel> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.fragment_mainviewpager, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.addload_iv);
        SliderImageModel sliderImageModel = IMAGES.get(position);
        Glide.with(context)
                .load(sliderImageModel.getmUrl())
                .into(imageView);

        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String id= IMAGES.get(position).getId() ;
                if (id.equalsIgnoreCase(AppConstants.APPOINTMENT) ) {
                    //comment by kk
                 // AppUtilityFunctions.showMessageDialog(context,"Info","This feature will be available soon.");
                    Intent intent = new Intent(context, OPDAppointmentActivity.class);
                    context.startActivity(intent);
                } else if (id.equalsIgnoreCase(AppConstants.TELECONSULTancy_REQUEST) ) {
                    Intent intent = new Intent(context, ScreeningActivity.class);
                    context.startActivity(intent);
                } else if (id.equalsIgnoreCase(AppConstants.RX_VIEW) ) {
                    Intent intent = new Intent(context, PrescriptionListActivity.class);
                    intent.putExtra("list",1);
                    context.startActivity(intent);
                } else if (id.equalsIgnoreCase(AppConstants.LAB_REPORTS) ) {
                    Intent intent = new Intent(context, PrescriptionListActivity.class);
                    intent.putExtra("list",2);
                    context.startActivity(intent);
                } else if (id.equalsIgnoreCase(AppConstants.SELF_REGISTRATION) ) {
                    Intent intent = new Intent(context, EstampingActivity.class);
                    context.startActivity(intent);
                } else if (id.equalsIgnoreCase(AppConstants.CREATE_YOUR_ABHA) ) {
                    Intent intent = new Intent(context, NDHMGenerateOTPActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}

