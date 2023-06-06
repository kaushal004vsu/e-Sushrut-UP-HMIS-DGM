package com.cdac.uphmis.prescriptionscanner.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.MySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ServiceUrl;
import uk.co.senab.photoview.PhotoViewAttacher;


//public class ViewPrescriptionImageFragment extends Fragment {
//    ImageView image, image2, image3;
//    //GoogleProgressBar progressBar;
//    String strDeptValue;
//    RequestQueue requestQueue;
//
//    LinearLayout progressBar;
//    GeometricProgressView progressView;
//    Button btnPage1,btnPage2,btnPage3;
//    PhotoViewAttacher photoAttacher1, photoAttacher2, photoAttacher3;
//    int isVisible=0;
//    public ViewPrescriptionImageFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_view_prescription_image, container, false);
//
//        requestQueue = Volley.newRequestQueue(getActivity());
//        strDeptValue = getArguments().getString("deptValue");
//        Log.i("department value is", "onCreateView: " + strDeptValue);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        final BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.doctor_navigation);
//        navigation.setVisibility(View.GONE);
//
//        Button btnBack = (Button) view.findViewById(R.id.btn_back);
//        isVisible=1;
//        image = (ImageView) view.findViewById(R.id.view_image);
//        image2 = (ImageView) view.findViewById(R.id.view_image2);
//        image3 = (ImageView) view.findViewById(R.id.view_image3);
//
//
//        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.btn_rotate);
//
//        // progressBar = (GoogleProgressBar) view.findViewById(R.id.pb);
//
//        photoAttacher1 = new PhotoViewAttacher(image);
//
//
//        photoAttacher2 = new PhotoViewAttacher(image2);
//
//        photoAttacher3 = new PhotoViewAttacher(image3);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                angleImage1 = angleImage1 + 90f;
////                angleImage1 = angleImage1 + 90f;
////                angleImage1 = angleImage1 + 90f;
////                image.setRotation(angleImage1);
////                image2.setRotation(angleImage2);
////                image3.setRotation(angleImage3);
//                if(isVisible==1) {
//                    photoAttacher1.setRotationBy(90f);
//                }
//                else if(isVisible==2) {
//                    photoAttacher2.setRotationBy(90f);
//                }
//                else if(isVisible==3) {
//                    photoAttacher3.setRotationBy(90f);
//                }
//            }
//        });
//        photoAttacher1.update();
//        photoAttacher2.update();
//        photoAttacher3.update();
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String temp = strDeptValue.replaceAll("\\^", "#");
//                String val[] = temp.split("#");
//
//
//                ViewPrescriptionSelectEpisodeFragment hello = new ViewPrescriptionSelectEpisodeFragment();
//                Bundle args = new Bundle();
//                args.putString("crno", val[1]);
//                hello.setArguments(args);
//                getFragmentManager().beginTransaction().replace(R.id.container, hello).commit();
//                navigation.setVisibility(View.VISIBLE);
//            }
//        });
//        progressBar = (LinearLayout) view.findViewById(R.id.pb);
//        progressView = (GeometricProgressView) view.findViewById(R.id.progress_view);
//        progressView.setType(TYPE.KITE);
//        progressView.setNumberOfAngles(10);
//        progressView.setColor(Color.parseColor("#122d4a"));
//
//        btnPage1 = (Button) view.findViewById(R.id.btn_page1);
//        btnPage2 = (Button) view.findViewById(R.id.btn_page2);
//        btnPage3 = (Button) view.findViewById(R.id.btn_page3);
//
//
//        btnPage1.setBackgroundResource(R.color.colorPrimary);
//        btnPage1.setTextColor(Color.WHITE);
//
//
//        image2.setVisibility(View.GONE);
//        image3.setVisibility(View.GONE);
//        image.setVisibility(View.GONE);
//
//        btnPage1.setEnabled(false);
//        btnPage2.setEnabled(false);
//        btnPage3.setEnabled(false);
//
//
//        btnPage1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isVisible=1;
//                btnPage1.setBackgroundResource(R.color.colorPrimary);
//                btnPage1.setTextColor(Color.WHITE);
//                btnPage2.setBackgroundResource(R.color.btnBackground);
//                btnPage2.setTextColor(Color.BLACK);
//                btnPage3.setBackgroundResource(R.color.btnBackground);
//                btnPage3.setTextColor(Color.BLACK);
//
//                image2.setVisibility(View.GONE);
//                image3.setVisibility(View.GONE);
//                image.setVisibility(View.VISIBLE);
//            }
//        });
//
//        btnPage2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isVisible=2;
//                btnPage2.setBackgroundResource(R.color.colorPrimary);
//                btnPage2.setTextColor(Color.WHITE);
//
//                btnPage1.setBackgroundResource(R.color.btnBackground);
//                btnPage1.setTextColor(Color.BLACK);
//
//                btnPage3.setBackgroundResource(R.color.btnBackground);
//                btnPage3.setTextColor(Color.BLACK);
//
//
//                image2.setVisibility(View.VISIBLE);
//                image3.setVisibility(View.GONE);
//                image.setVisibility(View.GONE);
//            }
//        });
//
//
//        btnPage3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isVisible=3;
//                btnPage3.setBackgroundResource(R.color.colorPrimary);
//                btnPage3.setTextColor(Color.WHITE);
//
//                btnPage1.setBackgroundResource(R.color.btnBackground);
//                btnPage1.setTextColor(Color.BLACK);
//
//                btnPage2.setBackgroundResource(R.color.btnBackground);
//                btnPage2.setTextColor(Color.BLACK);
//
//                image2.setVisibility(View.GONE);
//                image3.setVisibility(View.VISIBLE);
//                image.setVisibility(View.GONE);
//            }
//        });
//        retrieveImage();
//
//        return view;
//    }
//
//    public void retrieveImage() {
//
//        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.ViewPrescriptionImageurl/*"https://220.156.189.222/HBIMS/services/restful/UserService/retriveImageData"*/ /* "http://10.226.21.46:8080/HBIMS/services/restful/UserService/retriveImageData"*/, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                btnPage1.setEnabled(true);
//                btnPage2.setEnabled(true);
//                btnPage3.setEnabled(true);
//                Log.i("response is ", "onResponse: " + response);
//                //    tv.setText(response);
//                String jsonStr = response;
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    JSONArray imageData = jsonObj.getJSONArray("ImageData");
//                    //   String arEpisodeCode[]=new String[imageData.length()];
//                    if (imageData.length() == 0) {
//                        String temp = strDeptValue.replaceAll("\\^", "#");
//                        String val[] = temp.split("#");
//
//
//                        ViewPrescriptionSelectEpisodeFragment hello = new ViewPrescriptionSelectEpisodeFragment();
//                        Bundle args = new Bundle();
//                        args.putString("crno", val[1]);
//                        hello.setArguments(args);
//                        getFragmentManager().beginTransaction().replace(R.id.container, hello).commit();
//
//
//                        Toast.makeText(getActivity(), "No Prescription found for this episode", Toast.LENGTH_SHORT).show();
//                    }
//
//                    if(imageData.length()<3)
//                    {
//                        btnPage3.setVisibility(View.GONE);
//                        if (imageData.length()<2)
//                        {
//                            btnPage2.setVisibility(View.GONE);
//                        }
//                    }
//
//                    for (int i = 0; i < imageData.length(); i++) {
//                        JSONObject c = imageData.getJSONObject(i);
//
//                        String crnumber = c.getString("HRGNUM_PUK");
//                        String imageString = c.getString("IMG_DOCUMENT");
//
//                        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        //  image.setImageBitmap(decodedByte);
//
//                        // progressBar.setVisibility(View.VISIBLE);
////                        PhotoViewAttacher pAttacher;
////                        pAttacher = new PhotoViewAttacher(image);
////                        pAttacher.update();
//
//
//                        Log.i("fethed strings", "onResponse: " + crnumber + "  " + "  " + imageString);
//                        if (i == 0) {
//                            image.setImageBitmap(decodedByte);
//                            image.setVisibility(View.VISIBLE);
//                        } else if (i == 1) {
//                            image2.setImageBitmap(decodedByte);
//                        } else if (i == 2) {
//
//                            image3.setImageBitmap(decodedByte);
//
//                        }
//
//                    }
//
//                    //    image.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
//
//                } catch (final JSONException e) {
//                    image.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("error", "onErrorResponse: " + error);
//                FailureUploadFragment failureUploadFragment = new FailureUploadFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.container, failureUploadFragment); // give your fragment container id in first parameter
//                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();
//            }
//        })
//
//
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                data.put("DeptValue", strDeptValue);
//                return data;
//            }
//        };
//
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//         MySingleton.getInstance(this).addToRequestQueue(request);
//    }
//
//}




public class ViewPrescriptionImageFragment extends Fragment {
    ImageView image, image2, image3;
    //GoogleProgressBar progressBar;
    String strDeptValue;
    RequestQueue requestQueue;

    LinearLayout progressBar;
    GeometricProgressView progressView;
    Button btnPage1,btnPage2,btnPage3;
    PhotoViewAttacher photoAttacher1, photoAttacher2, photoAttacher3;
    int isVisible=0;

    Bitmap decodedByte;
    View view;
    public ViewPrescriptionImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_prescription_image, container, false);
        strDeptValue = getArguments().getString("deptValue");
        Log.i("department value is", "onCreateView: " + strDeptValue);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.doctor_navigation);
        navigation.setVisibility(View.GONE);
        requestQueue = Volley.newRequestQueue(getActivity());
        Button btnBack = (Button) view.findViewById(R.id.btn_back);
        isVisible=1;
        image = (ImageView) view.findViewById(R.id.view_image);
        image2 = (ImageView) view.findViewById(R.id.view_image2);
        image3 = (ImageView) view.findViewById(R.id.view_image3);


        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.btn_rotate);

        // progressBar = (GoogleProgressBar) view.findViewById(R.id.pb);

        photoAttacher1 = new PhotoViewAttacher(image);


        photoAttacher2 = new PhotoViewAttacher(image2);

        photoAttacher3 = new PhotoViewAttacher(image3);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                angleImage1 = angleImage1 + 90f;
//                angleImage1 = angleImage1 + 90f;
//                angleImage1 = angleImage1 + 90f;
//                image.setRotation(angleImage1);
//                image2.setRotation(angleImage2);
//                image3.setRotation(angleImage3);
                if(isVisible==1) {
                    photoAttacher1.setRotationBy(90f);
                }
                else if(isVisible==2) {
                    photoAttacher2.setRotationBy(90f);
                }
                else if(isVisible==3) {
                    photoAttacher3.setRotationBy(90f);
                }
            }
        });
        photoAttacher1.update();
        photoAttacher2.update();
        photoAttacher3.update();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = strDeptValue.replaceAll("\\^", "#");
                String val[] = temp.split("#");


                ViewPrescriptionSelectEpisodeFragment hello = new ViewPrescriptionSelectEpisodeFragment();
                Bundle args = new Bundle();
                args.putString("crno", val[1]);
                hello.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.container, hello).commit();
                navigation.setVisibility(View.VISIBLE);

                if(decodedByte!=null)
                {
                    view=null;
                    decodedByte.recycle();
                    decodedByte=null;
                    requestQueue=null;
                    System.gc();
                }
            }
        });
        progressBar = (LinearLayout) view.findViewById(R.id.pb);
        progressView = (GeometricProgressView) view.findViewById(R.id.progress_view);



        btnPage1 = (Button) view.findViewById(R.id.btn_page1);
        btnPage2 = (Button) view.findViewById(R.id.btn_page2);
        btnPage3 = (Button) view.findViewById(R.id.btn_page3);


        btnPage1.setBackgroundResource(R.color.colorPrimary);
        btnPage1.setTextColor(Color.WHITE);


        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        image.setVisibility(View.GONE);

        btnPage1.setEnabled(false);
        btnPage2.setEnabled(false);
        btnPage3.setEnabled(false);


        btnPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible=1;
                btnPage1.setBackgroundResource(R.color.colorPrimary);
                btnPage1.setTextColor(Color.WHITE);
                btnPage2.setBackgroundResource(R.color.btnBackground);
                btnPage2.setTextColor(Color.BLACK);
                btnPage3.setBackgroundResource(R.color.btnBackground);
                btnPage3.setTextColor(Color.BLACK);

                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }
        });

        btnPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible=2;
                btnPage2.setBackgroundResource(R.color.colorPrimary);
                btnPage2.setTextColor(Color.WHITE);

                btnPage1.setBackgroundResource(R.color.btnBackground);
                btnPage1.setTextColor(Color.BLACK);

                btnPage3.setBackgroundResource(R.color.btnBackground);
                btnPage3.setTextColor(Color.BLACK);


                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
            }
        });


        btnPage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible=3;
                btnPage3.setBackgroundResource(R.color.colorPrimary);
                btnPage3.setTextColor(Color.WHITE);

                btnPage1.setBackgroundResource(R.color.btnBackground);
                btnPage1.setTextColor(Color.BLACK);

                btnPage2.setBackgroundResource(R.color.btnBackground);
                btnPage2.setTextColor(Color.BLACK);

                image2.setVisibility(View.GONE);
                image3.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            }
        });
        retrieveImage();

        return view;
    }

    public void retrieveImage() {

        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.ViewPrescriptionImageurl/*"https://220.156.189.222/HBIMS/services/restful/UserService/retriveImageData"*/ /* "http://10.226.21.46:8080/HBIMS/services/restful/UserService/retriveImageData"*/, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BitmapFactory.Options options;

                btnPage1.setEnabled(true);
                btnPage2.setEnabled(true);
                btnPage3.setEnabled(true);
//                Log.i("response is ", "onResponse: " + response);
                //    tv.setText(response);
                String jsonStr = response;
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray imageData = jsonObj.getJSONArray("ImageData");
                    //   String arEpisodeCode[]=new String[imageData.length()];
                    if (imageData.length() == 0) {
                        String temp = strDeptValue.replaceAll("\\^", "#");
                        String val[] = temp.split("#");


                        ViewPrescriptionSelectEpisodeFragment hello = new ViewPrescriptionSelectEpisodeFragment();
                        Bundle args = new Bundle();
                        args.putString("crno", val[1]);
                        hello.setArguments(args);
                        getFragmentManager().beginTransaction().replace(R.id.container, hello).commit();


                        Toast.makeText(getActivity(), "No Prescription found for this episode", Toast.LENGTH_SHORT).show();
                    }

                    if(imageData.length()<3)
                    {
                        btnPage3.setVisibility(View.GONE);
                        if (imageData.length()<2)
                        {
                            btnPage2.setVisibility(View.GONE);
                        }
                    }


                    for (int i = 0; i < imageData.length(); i++) {
                        JSONObject c = imageData.getJSONObject(i);

                        String crnumber = c.getString("HRGNUM_PUK");
                        final String imageString = c.getString("IMG_DOCUMENT");






                        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //  options.inSampleSize = calculateInSampleSize(options, 100,100);
                        // options.inJustDecodeBounds = true;
                        Bitmap converetdImage = getResizedBitmap(decodedByte, 1000);
                        //  image.setImageBitmap(decodedByte);

                        // progressBar.setVisibility(View.VISIBLE);
//                        PhotoViewAttacher pAttacher;
//                        pAttacher = new PhotoViewAttacher(image);
//                        pAttacher.update();


                        Log.i("fethed strings", "onResponse: " + crnumber + "  " + "  " + imageString);
                        if (i == 0) {
                            image.setImageBitmap(converetdImage);
                            image.setVisibility(View.VISIBLE);
                        } else if (i == 1) {
                            image2.setImageBitmap(converetdImage);
                        } else if (i == 2) {

                            image3.setImageBitmap(converetdImage);

                        }

                    }

                    //    image.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                } catch (final Exception e) {
                    image.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
               /* FailureUploadFragment failureUploadFragment = new FailureUploadFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, failureUploadFragment); // give your fragment container id in first parameter
                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();*/

                AppUtilityFunctions.handleExceptions(error,getActivity());
            }
        })


        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
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

    @Override
    public void onDestroy() {
        //android.os.Process.killProcess(android.os.Process.myPid());

        super.onDestroy();

        if(decodedByte!=null)
        {
            view=null;
            decodedByte.recycle();
            decodedByte=null;
        }

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
