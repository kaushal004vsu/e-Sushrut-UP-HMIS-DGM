package com.cdac.uphmis.DocsUpload.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.DocsUpload.model.AllDatum;

import java.util.List;

public class DocsViewDocNewAdapter extends RecyclerView.Adapter<DocsViewDocNewAdapter.MyHolder> {
    Context context;
    List<AllDatum> list;
    OnClick onClick;
    public DocsViewDocNewAdapter(Context context, List<AllDatum> list, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_document_list,parent,false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(""+(position+1)+" - "+list.get(position).getDocumentTitle());
        if (list.get(position).getDocumentContentType().equalsIgnoreCase("application/pdf"))
            holder.download.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pdf_icon));
        else
            holder.download.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.gallery_pic));
        holder.parent.setOnClickListener(v -> onClick.onClick(position));
        holder.download.setOnClickListener(v -> onClick.onClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton download;
        LinearLayout parent;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            download=itemView.findViewById(R.id.download);
            parent=itemView.findViewById(R.id.parent);
        }
    }
    public  interface OnClick {
        public  void onClick(int position);
    }
}
