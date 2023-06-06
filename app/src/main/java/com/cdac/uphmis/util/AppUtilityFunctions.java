package com.cdac.uphmis.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.VERSION.SDK_INT;

public class AppUtilityFunctions {
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


    public static void shareApp(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "I recommend using " + context.getResources().getString(R.string.app_name) + " App. Please download and share it from Google Play Store: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }


    public static void saveBase64Pdf(Context context, String directoryName, String fileName, String data) {
        try {
            String path = getReportPath(context, directoryName, fileName, "pdf");
            Log.i("path", "saveBase64Pdf: " + path);
            new File(path);
            byte[] pdfAsBytes = Base64.decode(data, 0);
            FileOutputStream os;
            os = new FileOutputStream(path, false);
            os.write(pdfAsBytes);
            viewPdf(context, directoryName, fileName);
            os.flush();
            os.close();

        } catch (Exception ex) {
            Log.i("pdf exception", "saveBase64Pdf: " + ex);
        }
    }

    public static void viewPdf(Context context, String directoryName, String fileName) {

        // Setting the intent for pdf reader
        String path = getReportPath(context, directoryName, fileName, "pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        if (SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(path);
            Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "application/pdf");
            intent = Intent.createChooser(intent, "Open File");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

        public static void viewImage (Context context, String directoryName, String fileName, String
        extension){
            try {
                // Setting the intent for image reader
                String path = getReportPath(context, directoryName, fileName, extension);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(path), "image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                if (SDK_INT >= Build.VERSION_CODES.N) {
                    File file = new File(path);
                    Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);

                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(path), "image/*");
                    intent = Intent.createChooser(intent, "Open File");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            } catch (ActivityNotFoundException ex) {
                showErrorDialog(context, "No app found to view document.Please download respective app from play store to view document.");
            }
        }

        public static String getReportPath (Context context, String directoryName, String
        filename, String extension){
            File file = new File(context.getFilesDir().getPath(), directoryName);
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/" + filename + "." + extension);

            Log.i("view path", "saveBase64Pdf: " + uriSting);
            return uriSting;

        }

        public static Bitmap changeOrientation (String file){
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file, bounds);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(file, opts);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(file);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
            return rotatedBitmap;
        }

        //TODO check sha encryption
        public static String SHA1 (String text) throws
        NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] textBytes = text.getBytes("iso-8859-1");
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        }

        private static String convertToHex ( byte[] data){
            StringBuilder buf = new StringBuilder();
            for (byte b : data) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        }

        public static void gotoPlayStore (Context context){

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
            context.startActivity(intent);

        }

        public static void showInfoLinksDialog (Context context){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.info_links_dialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setAttributes(lp);
            TextView tvUserVideo = dialog.findViewById(R.id.tv_user_video);
            TextView tvFeatures = dialog.findViewById(R.id.tv_features);
            Button btnDismiss = dialog.findViewById(R.id.btn_dismiss);
            TextView tvHelpContents = dialog.findViewById(R.id.tv_help_contents);

            String features = context.getString(R.string.features_txt);

            String text = context.getString(R.string.manual_and_training_txt);

            tvUserVideo.setText(Html.fromHtml(text));
            tvFeatures.setText(features);

            tvUserVideo.setMovementMethod(LinkMovementMethod.getInstance());


            btnDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });

            tvHelpContents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            dialog.show();
        }


        public static String changeDateFormat (String rawDate, String fromFormat, String toFormat){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat(fromFormat);
                Date date1 = format1.parse(rawDate);
                SimpleDateFormat format2 = new SimpleDateFormat(toFormat);
                String formattedDate = format2.format(date1);
                return formattedDate;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return rawDate;
        }


        public static AlertDialog showErrorDialog (Context context, String message){
            AlertDialog dialog = new MaterialAlertDialogBuilder(context).setTitle(R.string.error).setMessage(message).setNegativeButton(R.string.ok, null).show();
            return dialog;
        }

        public static AlertDialog showMessageDialog (Context context, String title, String message){
            AlertDialog dialog = new MaterialAlertDialogBuilder(context).setTitle(title).setMessage(message).setNegativeButton("Ok", null).show();
            return dialog;
        }


        public static void handleExceptions (VolleyError error, Context c){
            try {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(c, "Could not Connect to Server.", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(c, "Authentication Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(c, "Internal Server Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(c, "Network Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(c, "Data Parsing Error.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static void statusBartheme (Activity activity){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.patient_header_background));
        }

        public static byte[] readBytes (Context context, Uri uri) throws IOException {
            // this dynamically extends to take the bytes you read
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            // and then we can return your byte array.
            return byteBuffer.toByteArray();
        }

        public static int getIndex (Spinner spinner, String myString){
            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                    return i;
                }
            }

            return 0;
        }




    public static void printPdfMedication(Context context,String directoryName,String fileName, RecyclerView mRecyclerView) {
        Bitmap recycler_view_bm =     getScreenshotFromRecyclerView(mRecyclerView);

        try {


            String path =AppUtilityFunctions.getReportPath(context, directoryName, fileName, "pdf");
            File pdfFile=new File(path);
            pdfFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(pdfFile);

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(recycler_view_bm.getWidth(), recycler_view_bm.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            recycler_view_bm.prepareToDraw();
            Canvas c;
            c = page.getCanvas();
            c.drawBitmap(recycler_view_bm,0,0,null);
            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

            AppUtilityFunctions.viewPdf(context, directoryName,fileName);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void blikImage (ImageView imageButton){
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        imageButton.startAnimation(animation); //to start animation
    }


    public static Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }

                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }
        return bigBitmap;
    }

    }
