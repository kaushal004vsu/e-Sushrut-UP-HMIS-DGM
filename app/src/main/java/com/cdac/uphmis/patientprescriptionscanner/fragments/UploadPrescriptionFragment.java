package com.cdac.uphmis.patientprescriptionscanner.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class UploadPrescriptionFragment extends Fragment {

    TextView tvName, tvCrno, tvDeptName, tvEpisodeDate;


    Bitmap bmp1, bmp2, bmp3;
    File imagepath;
    Intent i, ii, iii;
    ImageButton img1, img2, img3;
    int response_code = 0;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    RequestQueue requestQueue;
    String strDeptValue = "";
    String tempurl = "";
    TextView tvStatus;
    String imagedata;
    String strCrno, strEpisodeCode,strVisitNo;
    int updateImage1 = 0;
    int updateImage2 = 0;
    int updateImage3 = 0;




    public UploadPrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_prescription, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


//        final BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.doctor_navigation);
//        navigation.setVisibility(View.GONE);
        requestQueue = Volley.newRequestQueue(getActivity());
        img1 = (ImageButton) view.findViewById(R.id.img_1);
        img2 = (ImageButton) view.findViewById(R.id.img_2);
        img3 = (ImageButton) view.findViewById(R.id.img_3);




        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvCrno = (TextView) view.findViewById(R.id.tv_crno);
        tvDeptName = (TextView) view.findViewById(R.id.tv_deptname);
        tvEpisodeDate = (TextView) view.findViewById(R.id.tv_episode_date);
        tvStatus = (TextView) view.findViewById(R.id.tv_status);


        strDeptValue = getArguments().getString("deptValue");
        tempurl = strDeptValue;
        Log.i("tempurl", "onCreateView: " + tempurl);
        String temp = strDeptValue.replaceAll("\\^", "#");
        String val[] = temp.split("#");
        strVisitNo=val[3];
        tvName.setText("Name: \t" + val[9]);
        tvCrno.setText("CR No: \t" + val[0]);
        tvDeptName.setText("Dept Name: \t" + val[7]);
        tvEpisodeDate.setText("Date: \t" + val[10].substring(0, 10));
        strCrno = val[0];
        strEpisodeCode = val[1];
        checkPrescriptionStatus();


        Button btnBack = (Button) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = strDeptValue.replaceAll("\\^", "#");
                String val[] = temp.split("#");


                PatientFragment hello = new PatientFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, hello).commit();
//                navigation.setVisibility(View.VISIBLE);
            }
        });


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Prescription already uploaded. you do not have authority to replace prescription.");
        builder1.setTitle("Warning!");
        builder1.setCancelable(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            }
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        if (updateImage1 == 1) {

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

                            imagepath = new File(getContext().getFilesDir().toString(), "/page1.jpg");
                            Log.i("imagepath", "onClick: " + imagepath);
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity().getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    imagepath));
                            startActivityForResult(cameraIntent, 1);
                            // startActivityForResult(i, 1);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), "please give camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ii = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {


                        if (updateImage2 == 1) {
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


                            imagepath = new File(getContext().getFilesDir().toString(), "/page2.jpg");
                            Log.i("imagepath", "onClick: " + imagepath);


                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity().getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    imagepath));
                            startActivityForResult(cameraIntent, 2);
                            //startActivityForResult(ii, 2);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), "please give camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iii = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {


                        if (updateImage3 == 1) {

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

                            imagepath = new File(getContext().getFilesDir().toString(), "/page3.jpg");
                            Log.i("imagepath", "onClick: " + imagepath);


                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity().getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    imagepath));
                            startActivityForResult(cameraIntent, 3);
                            // startActivityForResult(ii, 3);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), "please give camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }





        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    @SuppressLint("ResourceAsColor")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case 1:
                if (requestCode == 1 && resultCode == RESULT_OK) {


                    bmp1=changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp1=getResizedBitmap(bmp1,1000);
                    img1.setImageBitmap(bmp1);
                    Log.i("filesize", "onActivityResult: "+imagepath.length()/1024);

                    img2.setEnabled(false);
                    img3.setEnabled(false);

                    if (bmp1 == null) {
                        Toast.makeText(getActivity(), "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
//                        btnImage1.setVisibility(View.GONE);
                        strDeptValue = tempurl + "^1";
                        Log.i("strDeptValue", "onClick: " + strDeptValue);
                        imagedata = getImgStringfromBitmap(bmp1);
                        uploadImage();
                    }


                }
                break;
            case 2:
                if (requestCode == 2 && resultCode == RESULT_OK) {

                    bmp2=changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp2=getResizedBitmap(bmp2,1000);
                    img2.setImageBitmap(bmp2);

                    img1.setEnabled(false);
                    img3.setEnabled(false);
                    if (bmp2 == null) {
                        Toast.makeText(getActivity(), "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
//                        btnImage2.setVisibility(View.GONE);
                        strDeptValue = "";
                        strDeptValue = tempurl + "^2";
                        imagedata = getImgStringfromBitmap(bmp2);
                        uploadImage();
                    }
                }
                break;

            case 3:
                if (requestCode == 3 && resultCode == RESULT_OK) {

                    bmp3=changeOrientation(imagepath.getAbsolutePath().toString());
                    bmp3=getResizedBitmap(bmp3,1000);
                    img3.setImageBitmap(bmp3);


                    img1.setEnabled(false);
                    img2.setEnabled(false);
                    if (bmp3 == null) {
                        Toast.makeText(getActivity(), "please click image first", Toast.LENGTH_SHORT).show();
                    } else {
                        tvStatus.setText("uploading");
                        tvStatus.setTextColor(Color.parseColor("#FFF40303"));
                        //    btnImage3.setVisibility(View.GONE);
                        strDeptValue = "";
                        strDeptValue = tempurl + "^3";
                        imagedata = getImgStringfromBitmap(bmp3);
                        uploadImage();
                    }
                }
                break;
        }


    }





    public void uploadImage() {

        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.uploadPrescriptionurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                img1.setEnabled(true);
                img2.setEnabled(true);
                img3.setEnabled(true);

                Log.i("uploadresponse", "onResponse: " + response);
                tvStatus.setText("Successfully Uploaded.");
                tvStatus.setTextColor(Color.parseColor("#FF089C03"));
                checkPrescriptionStatus();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error response", "onErrorResponse: " + error);
                response_code = 0;
                tvStatus.setText("Image upload failed, please try again.");

                img1.setEnabled(true);
                img2.setEnabled(true);
                img3.setEnabled(true);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                strDeptValue=strDeptValue+'^'+"0";
                data.put("ImageData", imagedata);
                data.put("DeptValue", strDeptValue);
                return data;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public String getImgStringfromBitmap(Bitmap bmp) {
        String stringimage = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imagearray = baos.toByteArray();
        long lengthbmp = imagearray.length;
        Log.i("after compression", "getImgStringfromBitmap: "+lengthbmp);
        stringimage = Base64.encodeToString(imagearray, Base64.DEFAULT);
        Log.i("image size in kb", "getImgStringfromBitmap: " + stringimage);
        return stringimage;
    }

    public void checkPrescriptionStatus() {

        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.checkPrescriptionStatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray patDetails = jsonObj.getJSONArray("pat_details");

                    for (int i = 0; i < patDetails.length(); i++) {
                        JSONObject c = patDetails.getJSONObject(i);
                        //  Toast.makeText(getActivity(), "" + c.getString("HRGT_PAGE_COUNT"), Toast.LENGTH_SHORT).show();
                        if (c.getString("HRGT_PAGE_COUNT").equalsIgnoreCase("1")) {
                            updateImage1 = 1;
                        } else if (c.getString("HRGT_PAGE_COUNT").equalsIgnoreCase("2")) {
                            updateImage2 = 1;
                        } else if (c.getString("HRGT_PAGE_COUNT").equalsIgnoreCase("3")) {
                            updateImage3 = 1;
                        }
                        Log.i("pagecount", "onResponse: " + c.getString("HRGT_PAGE_COUNT"));


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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("hosp_code", ServiceUrl.hospId);
                data.put("CrNo", strCrno);
                data.put("episode_code", strEpisodeCode);
                data.put("visit_no",strVisitNo);
                return data;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(getActivity()).addToRequestQueue(request);


    }

    public void setUpdateStatus() {
        if (updateImage1 == 1) {
            img1.setImageResource(R.drawable.successicon);
        }
        if (updateImage2 == 1) {
            img2.setImageResource(R.drawable.successicon);
        }
        if (updateImage3 == 1) {
            img3.setImageResource(R.drawable.successicon);
        }

    }

    public static Bitmap changeOrientation(String file)
    {
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
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

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

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
