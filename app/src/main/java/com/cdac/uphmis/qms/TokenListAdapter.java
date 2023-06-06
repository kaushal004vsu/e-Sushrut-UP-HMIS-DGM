package com.cdac.uphmis.qms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cdac.uphmis.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TokenListAdapter  extends RecyclerView.Adapter<TokenListAdapter.ExampleViewHolder> implements Filterable {
    private List<TokenListDetails> exampleList;
    private List<TokenListDetails> exampleListFull;

    Context context;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvHospitalName, tvtokenStatus, tvcounterName;
        CardView cardRootView;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            tvHospitalName = itemView.findViewById(R.id.tv_hospital_name);
            //tvtokenNo = itemView.findViewById(R.id.tv_token_no);
            tvtokenStatus = itemView.findViewById(R.id.tv_token_status);
            tvcounterName = itemView.findViewById(R.id.tv_counter_name);

            cardRootView = itemView.findViewById(R.id.root_view);
        }

    }

    public TokenListAdapter(Context context, List<TokenListDetails> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        this.context=context;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.token_list_row, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        TokenListDetails currentItem = exampleList.get(position);
        holder.imageView.setImageResource(R.drawable.tests);
        holder.tvHospitalName.setText(currentItem.getHOSP_NAME());
     //   holder.tvtokenNo.setText("Token Number: "+currentItem.getTOKEN_NO());
        holder.tvtokenStatus.setText(currentItem.getTOKEN_STATUS());
        holder.tvcounterName.setText(currentItem.getCOUNTER_NAME());


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(position);
        TextDrawable drawable = TextDrawable.builder()
                .roundRect(10)
                .build(currentItem.getTOKEN_NO(), color);
             //   .buildRound(currentItem.getTOKEN_NO(), color);

        holder.imageView.setImageDrawable(drawable);

        holder.cardRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DisplayScreenActivity.class);
                intent.putExtra("tokenDetails", (Serializable) currentItem);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TokenListDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TokenListDetails item : exampleListFull) {
                    if (item.getHOSP_NAME().toLowerCase().contains(filterPattern) || item.getSERVICE_ID().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

