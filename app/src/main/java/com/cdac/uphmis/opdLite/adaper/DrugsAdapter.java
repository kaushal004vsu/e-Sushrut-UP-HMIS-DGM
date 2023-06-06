package com.cdac.uphmis.opdLite.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.model.DrugsDetails;

import java.util.ArrayList;
import java.util.List;


public class DrugsAdapter extends ArrayAdapter<DrugsDetails> {

    private Context context;
    private List<DrugsDetails> products = new ArrayList<DrugsDetails>();
    private List<DrugsDetails> filteredProducts = new ArrayList<DrugsDetails>();

    public DrugsAdapter(Context context, List<DrugsDetails> products) {
        super(context, R.layout.drug_auto_complete_row, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return filteredProducts.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
//            return super.getFilter();
        return new ProductFilter(this, products);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        DrugsDetails product = filteredProducts.get(position);
        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.drug_auto_complete_row, parent, false);
        }
        TextView textViewName = (TextView) view.findViewById(R.id.autoCompleteItem);
        TextView tvLetterAvatar = (TextView) view.findViewById(R.id.tv_letter_avatar);
        textViewName.setText(product.getLabel());
        tvLetterAvatar.setText(product.getTypeShortName());
        return view;
    }

    @Override
    public DrugsDetails getItem(int position) {
        //return super.getItemId(position);
        return filteredProducts.get(position);
    }

    private class ProductFilter extends Filter {

        DrugsAdapter productListAdapter;
        List<DrugsDetails> originalList;
        List<DrugsDetails> filteredList;

        public ProductFilter(DrugsAdapter productListAdapter, List<DrugsDetails> originalList) {
            super();
            this.productListAdapter = productListAdapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final DrugsDetails product : originalList) {
                    if (product.getLabel().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productListAdapter.filteredProducts.clear();
            productListAdapter.filteredProducts.addAll((List) results.values);
            productListAdapter.notifyDataSetChanged();


        }
    }

}

