package com.cdac.uphmis.announcement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.announcement.model.Datum;
import com.cdac.uphmis.util.AppUtilityFunctions;


import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {
    Context context;
    List<Datum> list;
   OnClick onClick;
    public AnnouncementAdapter(Context context, List<Datum> list,OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_announcement_list,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (list.get(position).getIS_NEW().equals("1")){
            holder.is_new_iv.setVisibility(View.VISIBLE);
            holder.is_new_iv.setImageResource(R.drawable.is_new_ic);
            AppUtilityFunctions.blikImage(holder.is_new_iv);
        }else{
            holder.is_new_iv.setVisibility(View.GONE);
        }
        if (position % 2 == 0){
            holder.cardView.setBackgroundColor(Color.parseColor("#ECECEC"));
        }else{
            holder.cardView.setBackgroundColor(Color.WHITE);
        }
        holder.sno.setText(""+(position+1)+".");
        holder.date.setText(list.get(position).getPublishDate());
     //   holder.topic.setText(list.get(position).getSubject());

        holder.topic.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks( holder.topic, Linkify.ALL);
        SpannableString content = new SpannableString(list.get(position).getSubject());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.topic.setText(content);

        holder.topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sno,date,topic;
        ImageView is_new_iv;
        LinearLayout cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.tv_sno);
            date = itemView.findViewById(R.id.tv_date);
            topic = itemView.findViewById(R.id.tv_topic);
            is_new_iv = itemView.findViewById(R.id.is_new_iv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
    public  interface OnClick {
        public  void onClick(int position);
    }
}
