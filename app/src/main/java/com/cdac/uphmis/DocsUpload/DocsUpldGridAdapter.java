package com.cdac.uphmis.DocsUpload;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.cdac.uphmis.R;
import com.cdac.uphmis.VolleyMultipartRequest;
import com.cdac.uphmis.DocsUpload.DocsUpldDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocsUpldGridAdapter extends BaseAdapter {
    private Context mContext;
    private static final int THUMBNAIL_SIZE = 1024;
    List<String> docsTypeList;
    public ArrayList<DocsUpldDetails> documentDetailsArrayList;
    DocsUpldGridAdapter adapter = this;
    List<Boolean> cnsentList = new ArrayList<>();
    public DocsUpldGridAdapter(Context context, ArrayList<DocsUpldDetails> documentDetailsArrayList) {
        this.mContext = context;
        this.documentDetailsArrayList = documentDetailsArrayList;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        docsTypeList = new ArrayList<>();
        DocsUpldDetails currentItem = documentDetailsArrayList.get(position);
        try {
//            Log.i("fileType", "getView: " + currentItem.getMimeType());
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.docs_grid_item, null);
                holder = new ViewHolder();
                holder.upload_tv = convertView.findViewById(R.id.upload_tv);
                holder.imageView = convertView.findViewById(R.id.imageView);
                holder.docs_type_tv = convertView.findViewById(R.id.docs_type_tv);
                holder.spinner = convertView.findViewById(R.id.spinner);
                holder.close = convertView.findViewById(R.id.close);
                holder.close.setTag(Integer.valueOf(position));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!currentItem.isUploaded()) {
                holder.spinner.setSelection(0);
                holder.upload_tv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.blue)));
                holder.upload_tv.setEnabled(true);
                holder.upload_tv.setText("Upload");
                holder.upload_tv.setEnabled(true);
                holder.spinner.setEnabled(true);
                currentItem.setDocName(0);
                holder.upload_tv.setAlpha(1);
                holder.close.setVisibility(View.VISIBLE);
            } else {
                holder.upload_tv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.green)));
                holder.upload_tv.setEnabled(true);
                holder.upload_tv.setText("Uploaded");
                holder.upload_tv.setEnabled(false);
                holder.upload_tv.setAlpha(0.5f);
                holder.spinner.setEnabled(false);
                currentItem.setDocName(currentItem.getDocName());
                holder.spinner.setSelection(currentItem.getDocName());
                holder.close.setVisibility(View.GONE);
            }
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentItem.setDocName(holder.spinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (currentItem.getMimeType().matches(".*image.*")) {
                holder.imageView.setImageBitmap(getThumbnail(currentItem.getUri()));
            } else {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pdf_icon));
            }
            docsTypeList.add(holder.spinner.getSelectedItem().toString());
            cnsentList.add(position, false);
            holder.upload_tv.setOnClickListener(v -> {

                if (holder.spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(mContext, "Select document type", Toast.LENGTH_SHORT).show();
                } else {
                    String DocName = holder.spinner.getSelectedItem().toString();
                    dialogConsent(position, DocName, holder, currentItem);

                }
            });
            holder.close.setOnClickListener(v -> {
//                Log.i("currentItemClick", "onClick:  " + currentItem.getUri() + "position " + position);
//                Log.i("currentItemClick", "onClick:  " + documentDetailsArrayList.get(position));
                documentDetailsArrayList.remove(currentItem);
                adapter.notifyDataSetChanged();
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }
    public static class ViewHolder {
        ImageView imageView, close;
        RelativeLayout docs_type_tv;
        TextView upload_tv;
        Spinner spinner;
    }
    public Bitmap getThumbnail(Uri uri) throws IOException {
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
    private void dialogConsent(int position, String DocName, ViewHolder holder, DocsUpldDetails currentItem) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.consent_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView upload_image = dialog.findViewById(R.id.upload_image);
        TextView back = dialog.findViewById(R.id.back);
        CheckBox concent_cb = dialog.findViewById(R.id.concent_cb);
        upload_image.setOnClickListener(v -> {
            String isConsent = "";
            if (concent_cb.isChecked()) {
                cnsentList.add(position, true);
                dialog.dismiss();
                isConsent = "1";
            } else {
                dialog.dismiss();
                isConsent = "0";
                cnsentList.add(position, false);
            }
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String fileName = s.format(new Date());
            if (documentDetailsArrayList.get(position).getMimeType().startsWith("image")) {
                uploadImage(holder, documentDetailsArrayList.get(position).getUri(), "document"+fileName  + ".png", "document" + (position + 1), DocName, isConsent, currentItem);
            } else {
                uploadImage(holder, documentDetailsArrayList.get(position).getUri(), "document"+fileName + ".pdf", "document" + (position + 1), DocName, isConsent, currentItem);
            }
        });
        back.setOnClickListener(v -> {
                    dialog.dismiss();
                    cnsentList.add(position, false);
                }
        );
        dialog.show();
    }
    private void uploadImage(ViewHolder holder, Uri uri, String fileName, String documentName, String docName, String isConsent, DocsUpldDetails currentItem) {

        Log.d("uriiii",uri.getPath().toString()+"----"+fileName+"-----"+documentName);
        holder.upload_tv.setText("Uploading..");
        holder.upload_tv.setEnabled(false);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ServiceUrl.UploadDocs, response -> {
//                    Log.i("TAG", "onResponse: " + response);
                    if (response.statusCode == 200) {
                        holder.upload_tv.setEnabled(false);
                        holder.upload_tv.setText("Uploaded");
                        holder.upload_tv.setAlpha(0.5f);
                        holder.upload_tv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.green)));
                        holder.spinner.setEnabled(false);
                        currentItem.setUploaded(true);
                        currentItem.setDocName(holder.spinner.getSelectedItemPosition());
                        holder.close.setVisibility(View.GONE);
                    } else {
                        holder.upload_tv.setText("Failed");
                        holder.upload_tv.setAlpha(1);
                        holder.upload_tv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                        holder.upload_tv.setEnabled(true);
                        currentItem.setUploaded(false);
                        holder.spinner.setEnabled(true);
                        currentItem.setDocName(holder.spinner.getSelectedItemPosition());
                        holder.close.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    Log.i("TAG", "onErrorResponse: " + error);
                    holder.upload_tv.setText("Failed");
                    holder.upload_tv.setAlpha(1);
                    holder.upload_tv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.red)));
                    holder.upload_tv.setEnabled(true);
                    holder.spinner.setEnabled(true);
                    currentItem.setUploaded(false);
                    holder.close.setVisibility(View.VISIBLE);
                    AppUtilityFunctions.handleExceptions(error, mContext);
                    currentItem.setDocName(holder.spinner.getSelectedItemPosition());
                }) {
            @Override
            protected Map<String, String> getParams() {
                ManagingSharedData msd = new ManagingSharedData(mContext);
                Map<String, String> params = new HashMap<>();


               // BitmapDrawable drawable = (BitmapDrawable) holder.imageView.getDrawable();
                //Bitmap bitmap = drawable.getBitmap();

                params.put("crno", msd.getPatientDetails().getCrno());
                params.put("hospCode", msd.getPatientDetails().getHospitalCode());
                params.put("docType", holder.spinner.getSelectedItemPosition()+"#"+ docName);

//                params.put("uploadFileBase64",  encodeImage(bitmap));
                params.put("uploadFileBase64",  getBase64FromUri(uri));

                Log.d("uri44iii",getBase64FromUri(uri));

                params.put("seatId", "0");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                byte[] file = new byte[0];

                try {
                    file = AppUtilityFunctions.readBytes(mContext, uri);
                    Log.i("TAG", "getByteData: "+file.length);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("aaaaaa    ",e.getMessage());

                }
//                Log.i("bytes", "convert: " + file.toString());
                params.put("file", new DataPart(fileName, file));
//                params.put("file", new DataPart(fileName, file));
//                params.put("file", new DataPart(fileName, file));


//                params.put("file", new DataPart(fileName, file));
                return params;
            }
        };
        int socketTimeout = 50000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
        MySingleton.getInstance(mContext).addToRequestQueue(volleyMultipartRequest);
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private String getBase64FromUri(Uri uri) {

        try {
            byte[] bytes = new byte[0];
            bytes = AppUtilityFunctions.readBytes(mContext, uri);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}