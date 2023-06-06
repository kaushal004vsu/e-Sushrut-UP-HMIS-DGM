package com.cdac.uphmis.covid19.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdac.uphmis.R;

public class PatientHelpListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] mainTitle;
    private final String[] subTitle;
    private final Integer[] imgId;


    public PatientHelpListAdapter(Activity context, String[] mainTitle, String[] subTitle, Integer[] imgId) {

        super(context, R.layout.help_list_view, mainTitle);

        this.context=context;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.imgId = imgId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.help_list_view, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(mainTitle[position]);
        imageView.setImageResource(imgId[0]);
        subtitleText.setText(subTitle[position]);

        return rowView;

    };
}
