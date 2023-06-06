package com.cdac.uphmis.tracker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdac.uphmis.R;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusAdapter extends ArrayAdapter<OrderStatusModel> {

    Context context;
    ArrayList<OrderStatusModel> order_status;
    boolean isOn = false;
    List<InvestigationDetail> current_item;
    boolean date_flag= false;
    int pos_flag ;
    public OrderStatusAdapter(Context context, ArrayList<OrderStatusModel> order_status, List<InvestigationDetail> current_item) {
        super(context, 0, order_status);
        this.context = context;
        this.order_status = order_status;
        this.current_item = current_item;
    }

    @Override
    public int getCount() {
        return order_status.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw, parent, false);
        OrderStatusModel order_status_data = getItem(position);
        ImageView iv_upper_line = convertView.findViewById(R.id.iv_upper_line);
        ImageView iv_lower_line = convertView.findViewById(R.id.iv_lower_line);
        final ImageView iv_circle = convertView.findViewById(R.id.iv_circle);
        TextView tv_status = convertView.findViewById(R.id.tv_status);
        TextView tv_orderstatus_time = convertView.findViewById(R.id.tv_orderstatus_time);
        LinearLayout ly_orderstatus_time = convertView.findViewById(R.id.ly_orderstatus_time);
        LinearLayout ly_status = convertView.findViewById(R.id.ly_status);
        LinearLayout circle_ll = convertView.findViewById(R.id.circle_ll);

        tv_status.setText(order_status_data.getTv_status());
        iv_circle.setBackgroundResource(order_status_data.getImage());
        if (order_status_data.getTv_orderstatus_time() == null || order_status_data.getTv_orderstatus_time().equals("")) {
            tv_orderstatus_time.setText("--");
            tv_orderstatus_time.setTextColor(Color.RED);
        }else {
           // blinkImage(iv_circle);
            tv_orderstatus_time.setText(order_status_data.getTv_orderstatus_time());
            tv_orderstatus_time.setTextColor(Color.DKGRAY);
            iv_upper_line.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv_lower_line.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv_upper_line.getLayoutParams().width = 35;
            iv_lower_line.getLayoutParams().width = 35;
        }

        if (position == 0) {
            iv_upper_line.setVisibility(View.INVISIBLE);
        }else {
            iv_upper_line.setVisibility(View.VISIBLE);
            iv_lower_line.setVisibility(View.VISIBLE);
        }
        if(position==getCount()-1){
            iv_lower_line.setVisibility(View.INVISIBLE);
        }else {
            iv_lower_line.setVisibility(View.VISIBLE);
        }
        Log.d("position",""+position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "You Clicked at item" + position, Toast.LENGTH_SHORT).show();
            }

        });
        // Return the completed view to render on screen
        return convertView;
    }

    private int getItemPos(String item) {
        return order_status.indexOf(item);
    }

    private void blinkImage(ImageView onWayCircle) {
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        onWayCircle.startAnimation(animation); //to start animation
    }
}

