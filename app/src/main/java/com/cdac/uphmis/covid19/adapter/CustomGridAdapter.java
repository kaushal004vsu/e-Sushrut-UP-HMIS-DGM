package com.cdac.uphmis.covid19.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.model.DocumentDetails;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CustomGridAdapter extends BaseAdapter {
    private Context mContext;
    private static final int THUMBNAIL_SIZE = 1024;
    // Keep all Images in array
    public ArrayList<DocumentDetails> documentDetailsArrayList;
    ImageView close;
  //  OnClick onClick;
    CustomGridAdapter adapter = this;

    // Constructor
    public CustomGridAdapter(Context context, ArrayList<DocumentDetails> documentDetailsArrayList) {
        this.mContext = context;
        this.documentDetailsArrayList = documentDetailsArrayList;
     //   this.onClick = onClick;
    }

    @Override
    public int getCount() {
        return documentDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return documentDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = null;

        DocumentDetails currentItem = documentDetailsArrayList.get(position);
        try {
            Log.i("fileType", "getView: " + currentItem.getMimeType());

           // LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);

                holder = new ViewHolder();
                holder.imageView =  convertView.findViewById(R.id.imageView);
                holder.close =  convertView.findViewById(R.id.close);
                holder.close.setTag(Integer.valueOf(position));
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (currentItem.getMimeType().matches(".*image.*")) {
                holder.imageView.setImageBitmap(getThumbnail(currentItem.getUri()));
            } else {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdf_icon));
            }
            holder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("currentItemClick", "onClick:  "+currentItem.getUri()+"position "+position);
                    documentDetailsArrayList.remove(currentItem);
//                    documentDetailsArrayList.remove((Integer) v.getTag());

//                         onClick.onClick(position);
                    adapter.notifyDataSetChanged();
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }
     public interface OnClick{
         void onClick(int position);
     }
    public static class ViewHolder
    {
        ImageView imageView,close;
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = mContext.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = mContext.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }
    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }
}