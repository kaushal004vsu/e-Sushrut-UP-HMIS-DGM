package com.cdac.uphmis.covid19;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.VolleyMultipartRequest;
import com.cdac.uphmis.covid19.model.ScreeningDetails;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;


public class DocumentUploadActivity extends AppCompatActivity {
    ImageButton img1, img2, img3;
    int updateImage1 = 0;
    int updateImage2 = 0;
    int updateImage3 = 0;


    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    Bitmap bmp1, bmp2, bmp3;
    File imagepath;

    private TextView tvStatus;
    private String imagedata;
    private String requestId, crno, appointmentDetails;


    private ScreeningDetails screeningDetails;
    RequestQueue requestQueue;

    String strSlNo;


    Button btnDone;

    private String imageSize="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        
        tvStatus = findViewById(R.id.tv_status);
        btnDone = findViewById(R.id.btn_done);

        NukeSSLCerts.nuke(this);

        requestQueue = Volley.newRequestQueue(this);
        try {

            Intent intent = getIntent();
            requestId = intent.getStringExtra("requestId");
            Log.i("requestId ", "onCreate: " + requestId);
            crno = getIntent().getStringExtra("crno");
            appointmentDetails = getIntent().getStringExtra("apptDetails");
            screeningDetails = (ScreeningDetails) getIntent().getSerializableExtra("screeningDetails");
        } catch (Exception e) {
            e.printStackTrace();
        }
        img1 = (ImageButton) findViewById(R.id.img_1);
        img2 = (ImageButton) findViewById(R.id.img_2);
        img3 = (ImageButton) findViewById(R.id.img_3);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to replace this page?");
        builder1.setTitle("Warning!");
        builder1.setCancelable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_CAMERA_PERMISSION_CODE);
            }
            img1.setOnClickListener(view -> {
                try {
                    if (updateImage1 == 1) {

                        builder1.setPositiveButton(
                                getString(R.string.ok),
                                (dialog, id) -> {
//replace image
//                                    captureImage1();
                                    selectImage(DocumentUploadActivity.this,1);
                                });
                        builder1.setNegativeButton(
                                getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                        Log.i("already1", "already uploaded do you want to continue?");
                    } else {
//upload image
                        selectImage(DocumentUploadActivity.this,1);
//                        captureImage1();

                    }
                } catch (Exception ex) {
                    Toast.makeText(DocumentUploadActivity.this, "please give camera permission", Toast.LENGTH_SHORT).show();
                }
            });

            img2.setOnClickListener(view -> {

                try {


                    if (updateImage2 == 1) {
                        builder1.setPositiveButton(
                                getString(R.string.ok),
                                (dialog, id) -> {
                                    //                        captureImage2();
                                    selectImage(DocumentUploadActivity.this,2);
                                });
                        builder1.setNegativeButton(
                                getString(R.string.cancel),
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {

//                        captureImage2();
                        selectImage(DocumentUploadActivity.this,2);
                    }
                } catch (Exception ex) {
                    Toast.makeText(DocumentUploadActivity.this, "please give camera permission", Toast.LENGTH_SHORT).show();
                }
            });
            img3.setOnClickListener(view -> {

                try {
                    if (updateImage3 == 1) {
                        builder1.setPositiveButton(
                                getString(R.string.ok),
                                (dialog, id) -> {

//                                    captureImage3();
                                    selectImage(DocumentUploadActivity.this,3);
                                });

                        builder1.setNegativeButton(
                                getString(R.string.cancel),
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
//                        captureImage3();
                        selectImage(DocumentUploadActivity.this,3);
                    }
                } catch (Exception ex) {
                    Toast.makeText(DocumentUploadActivity.this, "please give camera permission", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void selectImage(Context context, int imageNo) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                if (imageNo==1)
                {
                    captureImage1();
                }else if (imageNo==2)
                {
                    captureImage2();
                }else if (imageNo==3)
                {
                    captureImage3();
                }

            } else if (options[item].equals("Choose from Gallery")) {

                if (imageNo==1)
                {
                    pickFromGallery1();
                }else if (imageNo==2)
                {
                    pickFromGallery2();
                }else if (imageNo==3)
                {
                    pickFromGallery3();
                }
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void captureImage1() {
        imagepath = new File(getFilesDir(), "/page1.jpg");
        Log.i("imagepath", "onClick: " + imagepath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imagepath));
        startActivityForResult(cameraIntent, 1);
    }

    private void captureImage2() {


        imagepath = new File(getFilesDir(), "/page2.jpg");
        Log.i("imagepath", "onClick: " + imagepath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imagepath));
        startActivityForResult(cameraIntent, 2);
    }

    private void captureImage3() {
        imagepath = new File(getFilesDir(), "/page3.jpg");
        Log.i("imagepath", "onClick: " + imagepath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imagepath));
        startActivityForResult(cameraIntent, 3);
    }


    private void pickFromGallery1()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 4);
    }
    private void pickFromGallery2()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 5);
    }
    private void pickFromGallery3()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 6);
    }

    @SuppressLint("ResourceAsColor")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (requestCode == 1 && resultCode == RESULT_OK) {


                    bmp1 = changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp1 = getResizedBitmap(bmp1, 1000);
                    img1.setImageBitmap(bmp1);
                    Log.i("filesize", "onActivityResult: " + imagepath.length() / 1024);

                    img2.setEnabled(false);
                    img3.setEnabled(false);

                    if (bmp1 == null) {
                        Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                    } else {

                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        strSlNo = "1";
                        imagedata = getImgStringfromBitmap(bmp1);
                        btnDone.setEnabled(false);
                        uploadImage("page1.jpg");
                    }


                }
                break;
            case 2:
                if (requestCode == 2 && resultCode == RESULT_OK) {

                    bmp2 = changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp2 = getResizedBitmap(bmp2, 1000);
                    img2.setImageBitmap(bmp2);

                    img1.setEnabled(false);
                    img3.setEnabled(false);
                    if (bmp2 == null) {
                        Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        strSlNo = "2";
                        imagedata = getImgStringfromBitmap(bmp2);
                        btnDone.setEnabled(false);
                        uploadImage("page2.jpg");
                    }
                }
                break;

            case 3:
                if (requestCode == 3 && resultCode == RESULT_OK) {

                    bmp3 = changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp3 = getResizedBitmap(bmp3, 1000);
                    img3.setImageBitmap(bmp3);


                    img1.setEnabled(false);
                    img2.setEnabled(false);
                    if (bmp3 == null) {
                        Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        strSlNo = "3";
                        imagedata = getImgStringfromBitmap(bmp3);
                        btnDone.setEnabled(false);
                        uploadImage("page3.jpg");
                    }
                }
                break;



            //pick from gallery
            case 4:
                if (requestCode == 4 && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);

                            bmp1 = changeOrientation(picturePath);
                            bmp1 = getResizedBitmap(bmp1, 1000);
                            img1.setImageBitmap(bmp1);

                            img2.setEnabled(false);
                            img3.setEnabled(false);

                            cursor.close();

                            if (bmp1 == null) {
                                Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                            } else {

                                tvStatus.setText("uploading");
                                tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                                strSlNo = "1";
                                imagedata = getImgStringfromBitmap(bmp1);
                                btnDone.setEnabled(false);
                                uploadImage("page1.jpg");
                            }
                        }
                    }





                }
                break;
            case 5:
                if (requestCode == 5 && resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            bmp2 = changeOrientation(picturePath);
                            bmp2 = getResizedBitmap(bmp2, 1000);
                            img2.setImageBitmap(bmp2);

                            img1.setEnabled(false);
                            img3.setEnabled(false);
                            if (bmp2 == null) {
                                Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                            } else {
                                tvStatus.setText("uploading");
                                tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                                strSlNo = "2";
                                imagedata = getImgStringfromBitmap(bmp2);
                                btnDone.setEnabled(false);
                                uploadImage("page2.jpg");
                            }

                        }
                    }


                }
                break;
            case 6:
                if (requestCode == 6 && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            bmp3 = changeOrientation(picturePath);
                            bmp3 = getResizedBitmap(bmp3, 1000);
                            img3.setImageBitmap(bmp3);


                            img1.setEnabled(false);
                            img2.setEnabled(false);
                            if (bmp3 == null) {
                                Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                            } else {
                                tvStatus.setText("uploading");
                                tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                                strSlNo = "3";
                                imagedata = getImgStringfromBitmap(bmp3);
                                btnDone.setEnabled(false);
                                uploadImage("page3.jpg");
                            }
                        }
                    }
                }
                break;
        }


    }

    /*private void uploadImage() {
        try {
            SessionServicecall sessionServicecall = new SessionServicecall(this);
            sessionServicecall.saveSession(screeningDetails.getCrno(), screeningDetails.getPatMobileNo(), screeningDetails.getHospCode(), "upload_document", "", "", "", imageSize + " KB");
        }catch(Exception ex){}

        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.uploadDocument, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                btnDone.setEnabled(true);
                img1.setEnabled(true);
                img2.setEnabled(true);
                img3.setEnabled(true);

                Log.i("uploadresponse", "onResponse: " + response);
                tvStatus.setText("Successfully Uploaded.");
                tvStatus.setTextColor(Color.parseColor("#FF089C03"));
                checkPrescriptionStatus(requestId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error response", "onErrorResponse: " + error);
                // response_code = 0;
                btnDone.setEnabled(true);
                tvStatus.setText("Image upload failed, please try again.");

                img1.setEnabled(true);
                img2.setEnabled(true);
                img3.setEnabled(true);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("ImageData", imagedata);
                data.put("hospCode", screeningDetails.getHospCode());
                data.put("requestID", requestId);
                data.put("slno", strSlNo);
                return data;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }*/




    private void uploadImage(String fileName) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ServiceUrl.testurl+"econsultation/uploadDocument",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        btnDone.setEnabled(true);
                        img1.setEnabled(true);
                        img2.setEnabled(true);
                        img3.setEnabled(true);

                        Log.i("uploadresponse", "onResponse: " + response);
                        tvStatus.setText("Successfully Uploaded.");
                        tvStatus.setTextColor(Color.parseColor("#FF089C03"));
                        checkPrescriptionStatus(requestId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnDone.setEnabled(true);
                        tvStatus.setText("Image upload failed, please try again.");

                        img1.setEnabled(true);
                        img2.setEnabled(true);
                        img3.setEnabled(true);
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                ManagingSharedData msd = new ManagingSharedData(DocumentUploadActivity.this);
                Map<String, String> params = new HashMap<>();
                params.put("ImageData", "1");
                params.put("hospCode", screeningDetails.getHospCode());
                params.put("requestID", requestId);
                params.put("slno", strSlNo);
                params.put("fileExtension", "jpg");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                ManagingSharedData msd = new ManagingSharedData(DocumentUploadActivity.this);
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                byte[] file = convert(imagepath.getAbsolutePath().toString());
                Log.i("bytes", "convert: " + file);

                if (file == null) {
                    Toast.makeText(DocumentUploadActivity.this, "Please select document before uploading.", Toast.LENGTH_SHORT).show();
                    return null;
                }
                params.put("file", new DataPart(fileName, file));

                params.put("file", new DataPart(fileName, file));


                return params;
            }
        };

        //adding the request to volley
      //  RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 5000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(volleyMultipartRequest);
    }



    public byte[] convert(String path) {
        try {

            File file = new File(path);
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[(int) file.length()];

            for (int readNum; (readNum = fis.read(b)) != -1; ) {
                bos.write(b, 0, readNum);
            }

            byte[] bytes = bos.toByteArray();
            String base = Base64.encodeToString(bytes, Base64.DEFAULT);

            return bytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Please record file first.", Toast.LENGTH_SHORT).show();


        }
        return null;
    }












    public static Bitmap changeOrientation(String file) {
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


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getImgStringfromBitmap(Bitmap bmp) {
        String stringimage = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imagearray = baos.toByteArray();
        long lengthbmp = imagearray.length;

        imageSize=String.valueOf(lengthbmp/1024);
        Log.i("after compression", "getImgStringfromBitmap: " + imageSize);
        stringimage = Base64.encodeToString(imagearray, Base64.DEFAULT);
        Log.i("image size in kb", "getImgStringfromBitmap: " + stringimage);
        return stringimage;
    }

    public void setUpdateStatus() {
        if (updateImage1 == 1) {
            img1.setImageResource(R.drawable.right);
        }
        if (updateImage2 == 1) {
            img2.setImageResource(R.drawable.right);
        }
        if (updateImage3 == 1) {
            img3.setImageResource(R.drawable.right);
        }

    }

    public void checkPrescriptionStatus(final String requestId) {

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getDocumentStatus + requestId + "&hospCode=" + screeningDetails.getHospCode(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("documentuploadresponse", "onResponse: " + response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray patDetails = jsonObj.getJSONArray(requestId);

                    for (int i = 0; i < patDetails.length(); i++) {
                        JSONObject c = patDetails.getJSONObject(i);
                        String slno = c.getString("slno");
                        String isDocUploaded = c.getString("isDocUploaded");


                        if (slno.equalsIgnoreCase("1")) {
                            updateImage1 = 1;
                        }
                        if (slno.equalsIgnoreCase("2")) {
                            updateImage2 = 1;
                        }
                        if (slno.equalsIgnoreCase("3")) {
                            updateImage3 = 1;
                        }


                    }

                    setUpdateStatus();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error response", "onErrorResponse: " + error);

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }


    public void btnDone(View view) {

        Intent intent = new Intent(this, AppointmentSuccefullActivity.class);
        intent.putExtra("requestId", requestId);
        intent.putExtra("crno", crno);
        intent.putExtra("screeningDetails", screeningDetails);
        intent.putExtra("apptDetails", appointmentDetails);
        startActivity(intent);
        finish();
    }

    private void showSuccessFulDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.successful_e_consultation_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        TextView tvOk = dialog.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });


        dialog.show();
    }
}

/*
public class DocumentUploadActivity extends AppCompatActivity {
    ImageButton img1, img2, img3;
    int updateImage1 = 0;
    int updateImage2 = 0;
    int updateImage3 = 0;


    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    Bitmap bmp1, bmp2, bmp3;
    File imagepath;

    private TextView tvStatus;
    private String imagedata;
    private String requestId,crno, appointmentDetails;


    private ScreeningDetails screeningDetails;
    RequestQueue requestQueue;

    String strSlNo;


    Button btnDone;

    ManagingSharedData msd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        tvStatus = findViewById(R.id.tv_status);
        btnDone = findViewById(R.id.btn_done);

        msd = new ManagingSharedData(this);
        //NukeSSLCerts.nuke(this);

       // requestQueue = Volley.newRequestQueue(this);
        try {

            Intent intent = getIntent();
            requestId = intent.getStringExtra("requestId");
            Log.i("requestId ", "onCreate: " + requestId);
            crno = getIntent().getStringExtra("crno");
            appointmentDetails = getIntent().getStringExtra("apptDetails");
            screeningDetails = (ScreeningDetails) getIntent().getSerializableExtra("screeningDetails");
        } catch (Exception e) {
            e.printStackTrace();
        }
        img1 = (ImageButton) findViewById(R.id.img_1);
        img2 = (ImageButton) findViewById(R.id.img_2);
        img3 = (ImageButton) findViewById(R.id.img_3);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to replace this page?");
        builder1.setTitle("Warning!");
        builder1.setCancelable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            }
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        if (updateImage1 == 1) {


                            */
/*builder1.setNegativeButton(
                                    "cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });*//*

                            builder1.setPositiveButton(
                                    "ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                            Date c = Calendar.getInstance().getTime();
//                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss");
//                                            String formattedDate = df.format(c);
                                            imagepath = new File(Environment.getExternalStorageDirectory(), "/page1.jpg");
                                            Log.i("imagepath", "onClick: " + imagepath);
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DocumentUploadActivity.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    imagepath));
                                            startActivityForResult(cameraIntent, 1);
                                        }
                                    });
                            builder1.setNegativeButton(
                                    "cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();


                            Log.i("already1", "already uploaded do you want to continue?");
                        } else {

                            imagepath = new File(Environment.getExternalStorageDirectory(), "/page1.jpg");
                            Log.i("imagepath", "onClick: " + imagepath);
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    imagepath));
                            startActivityForResult(cameraIntent, 1);

                        }
                    } catch (Exception ex) {
                        Toast.makeText(DocumentUploadActivity.this, "please give camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ii = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {


                        if (updateImage2 == 1) {


                            builder1.setPositiveButton(
                                    "ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                            Date c = Calendar.getInstance().getTime();
//                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss");
//                                            String formattedDate = df.format(c);
                                            imagepath = new File(Environment.getExternalStorageDirectory(), "/page2.jpg");
                                            Log.i("imagepath", "onClick: " + imagepath);


                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DocumentUploadActivity.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    imagepath));
                                            startActivityForResult(cameraIntent, 2);
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        } else {


                            imagepath = new File(Environment.getExternalStorageDirectory(), "/page2.jpg");
                            Log.i("imagepath", "onClick: " + imagepath);


                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    imagepath));
                            startActivityForResult(cameraIntent, 2);

                        }
                    } catch (Exception ex) {
                        Toast.makeText(DocumentUploadActivity.this, "please give camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iii = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {


                        if (updateImage3 == 1) {


                            builder1.setPositiveButton(
                                    "ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                            Date c = Calendar.getInstance().getTime();
//                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss");
//                                            String formattedDate = df.format(c);
                                            imagepath = new File(Environment.getExternalStorageDirectory(), "/page3.jpg");
                                            Log.i("imagepath", "onClick: " + imagepath);


                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DocumentUploadActivity.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    imagepath));
                                            startActivityForResult(cameraIntent, 3);
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        } else {

                            imagepath = new File(Environment.getExternalStorageDirectory(), "/page3.jpg");
                            Log.i("imagepath", "onClick: " + imagepath);


                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    imagepath));
                            startActivityForResult(cameraIntent, 3);
                            // startActivityForResult(ii, 3);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(DocumentUploadActivity.this, "please give camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @SuppressLint("ResourceAsColor")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (requestCode == 1 && resultCode == RESULT_OK) {


                    bmp1 = changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp1 = getResizedBitmap(bmp1, 1000);
                    img1.setImageBitmap(bmp1);
                    Log.i("filesize", "onActivityResult: " + imagepath.length() / 1024);

                    img2.setEnabled(false);
                    img3.setEnabled(false);

                    if (bmp1 == null) {
                        Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                    } else {

                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        strSlNo = "1";
                        //strDeptValue = tempurl + "^1";
                        //Log.i("strDeptValue", "onClick: " + strDeptValue);
                        imagedata = getImgStringfromBitmap(bmp1);
                        // img1.setEnabled(false);

                        btnDone.setEnabled(false);
                        uploadImage();
                    }


                }
                break;
            case 2:
                if (requestCode == 2 && resultCode == RESULT_OK) {

                    bmp2 = changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp2 = getResizedBitmap(bmp2, 1000);
                    img2.setImageBitmap(bmp2);

                    img1.setEnabled(false);
                    img3.setEnabled(false);
                    if (bmp2 == null) {
                        Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        strSlNo = "2";
                        // strDeptValue = "";
                        //strDeptValue = tempurl + "^2";
                        imagedata = getImgStringfromBitmap(bmp2);
                        //      img2.setEnabled(false);
                        btnDone.setEnabled(false);
                        uploadImage();
                    }
                }
                break;

            case 3:
                if (requestCode == 3 && resultCode == RESULT_OK) {

                    bmp3 = changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp3 = getResizedBitmap(bmp3, 1000);
                    img3.setImageBitmap(bmp3);


                    img1.setEnabled(false);
                    img2.setEnabled(false);
                    if (bmp3 == null) {
                        Toast.makeText(this, "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        strSlNo = "3";
                        //  strDeptValue = "";
                        //strDeptValue = tempurl + "^3";
                        imagedata = getImgStringfromBitmap(bmp3);
                        btnDone.setEnabled(false);
                        uploadImage();
                    }
                }
                break;
        }


    }

    private void uploadImage() {
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.uploadDocument, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                btnDone.setEnabled(true);
                img1.setEnabled(true);
                img2.setEnabled(true);
                img3.setEnabled(true);

                Log.i("uploadresponse", "onResponse: " + response);
                tvStatus.setText("Successfully Uploaded.");
                tvStatus.setTextColor(Color.parseColor("#FF089C03"));
                checkPrescriptionStatus(requestId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error response", "onErrorResponse: " + error);
                // response_code = 0;
                btnDone.setEnabled(true);
                tvStatus.setText("Image upload failed, please try again.");

                img1.setEnabled(true);
                img2.setEnabled(true);
                img3.setEnabled(true);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                ManagingSharedData msd = new ManagingSharedData(DocumentUploadActivity.this);
                Map<String, String> data = new HashMap<>();
                data.put("ImageData", imagedata);
                data.put("hospCode", screeningDetails.getHospCode());
                data.put("requestID", requestId);
                data.put("slno", strSlNo);
                return data;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);

    }


    public static Bitmap changeOrientation(String file) {
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


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getImgStringfromBitmap(Bitmap bmp) {
        String stringimage = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imagearray = baos.toByteArray();
        long lengthbmp = imagearray.length;
        Log.i("after compression", "getImgStringfromBitmap: " + lengthbmp);
        stringimage = Base64.encodeToString(imagearray, Base64.DEFAULT);
        Log.i("image size in kb", "getImgStringfromBitmap: " + stringimage);
        return stringimage;
    }

    public void setUpdateStatus() {
        if (updateImage1 == 1) {
            img1.setImageResource(R.drawable.right);
        }
        if (updateImage2 == 1) {
            img2.setImageResource(R.drawable.right);
        }
        if (updateImage3 == 1) {
            img3.setImageResource(R.drawable.right);
        }

    }

    public void checkPrescriptionStatus(final String requestId) {

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getDocumentStatus + requestId + "&hospCode=" +screeningDetails.getHospCode(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("documentuploadresponse", "onResponse: " + response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray patDetails = jsonObj.getJSONArray(requestId);

                    for (int i = 0; i < patDetails.length(); i++) {
                        JSONObject c = patDetails.getJSONObject(i);
                        String slno = c.getString("slno");
                        String isDocUploaded = c.getString("isDocUploaded");


                        if (slno.equalsIgnoreCase("1")) {
                            updateImage1 = 1;
                        }
                        if (slno.equalsIgnoreCase("2")) {
                            updateImage2 = 1;
                        }
                        if (slno.equalsIgnoreCase("3")) {
                            updateImage3 = 1;
                        }
//                        Log.i("pagecount", "onResponse: " + c.getString("HRGT_PAGE_COUNT"));


                    }

                    setUpdateStatus();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error response", "onErrorResponse: " + error);

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);


    }


    public void btnDone(View view) {

        //showSuccessFulDialog();
//        finish();

        Intent intent = new Intent(this, AppointmentSuccefullActivity.class);
        intent.putExtra("requestId", requestId);
        intent.putExtra("crno", crno);
        intent.putExtra("screeningDetails", screeningDetails);
        intent.putExtra("apptDetails", appointmentDetails);
        startActivity(intent);
        finish();
    }

    private void showSuccessFulDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.successful_e_consultation_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        TextView tvOk = dialog.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });


        dialog.show();
    }
}
*/
