package com.cdac.uphmis.covid19;


import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.cdac.uphmis.util.MySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.ServiceUrl;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewDocumentActivity extends AppCompatActivity {
    ImageView image, image2, image3;


    RequestQueue requestQueue;

    LinearLayout progressBar;
    GeometricProgressView progressView;
    Button btnPage1, btnPage2, btnPage3;
    PhotoViewAttacher photoAttacher1, photoAttacher2, photoAttacher3;
    int isVisible = 0;

    Bitmap decodedByte;

    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document);
        
        try {

            Intent intent = getIntent();
            requestId = intent.getStringExtra("requestId");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        getSupportActionBar().hide();

       // requestQueue = Volley.newRequestQueue(this);

        isVisible = 1;
        image = (ImageView) findViewById(R.id.view_image);
        image2 = (ImageView) findViewById(R.id.view_image2);
        image3 = (ImageView) findViewById(R.id.view_image3);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_rotate);

        // progressBar = (GoogleProgressBar) view.findViewById(R.id.pb);

        photoAttacher1 = new PhotoViewAttacher(image);


        photoAttacher2 = new PhotoViewAttacher(image2);

        photoAttacher3 = new PhotoViewAttacher(image3);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isVisible == 1) {
                    photoAttacher1.setRotationBy(90f);
                } else if (isVisible == 2) {
                    photoAttacher2.setRotationBy(90f);
                } else if (isVisible == 3) {
                    photoAttacher3.setRotationBy(90f);
                }
            }
        });
        photoAttacher1.update();
        photoAttacher2.update();
        photoAttacher3.update();


        progressBar = (LinearLayout) findViewById(R.id.pb);
        progressView = (GeometricProgressView) findViewById(R.id.progress_view);



        btnPage1 = (Button) findViewById(R.id.btn_page1);
        btnPage2 = (Button) findViewById(R.id.btn_page2);
        btnPage3 = (Button) findViewById(R.id.btn_page3);


        btnPage1.setBackgroundResource(R.color.colorPrimary);
        btnPage1.setTextColor(Color.WHITE);

/*

        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        image.setVisibility(View.GONE);

        btnPage1.setEnabled(false);
        btnPage2.setEnabled(false);
        btnPage3.setEnabled(false);
*/

        retrieveImage("1", image);
    /*    image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);*/
        btnPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible = 1;
                retrieveImage("1", image);
                btnPage1.setBackgroundResource(R.color.colorPrimary);
                btnPage1.setTextColor(Color.WHITE);
                btnPage2.setBackgroundResource(R.color.btnBackground);
                btnPage2.setTextColor(Color.BLACK);
                btnPage3.setBackgroundResource(R.color.btnBackground);
                btnPage3.setTextColor(Color.BLACK);

                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);

            }
        });

        btnPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible = 2;
                retrieveImage("2", image2);
                btnPage2.setBackgroundResource(R.color.colorPrimary);
                btnPage2.setTextColor(Color.WHITE);

                btnPage1.setBackgroundResource(R.color.btnBackground);
                btnPage1.setTextColor(Color.BLACK);

                btnPage3.setBackgroundResource(R.color.btnBackground);
                btnPage3.setTextColor(Color.BLACK);


                image3.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
            }
        });


        btnPage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible = 3;
                retrieveImage("3", image3);
                btnPage3.setBackgroundResource(R.color.colorPrimary);
                btnPage3.setTextColor(Color.WHITE);

                btnPage1.setBackgroundResource(R.color.btnBackground);
                btnPage1.setTextColor(Color.BLACK);

                btnPage2.setBackgroundResource(R.color.btnBackground);
                btnPage2.setTextColor(Color.BLACK);

                image2.setVisibility(View.GONE);
                image.setVisibility(View.GONE);

            }
        });
//        for (int i=1;i<=3;i++)
//        {
//            retrieveImage(""+i);
//        }
    }


    public void retrieveImage(final String slno, final ImageView image) {
        btnPage1.setEnabled(false);
        btnPage2.setEnabled(false);
        btnPage3.setEnabled(false);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.viewDocument, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BitmapFactory.Options options;
                Log.i("response", "onResponse: " + response);
                btnPage1.setEnabled(true);
                btnPage2.setEnabled(true);
                btnPage3.setEnabled(true);

                String jsonStr = response;
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //JSONArray jsonArray = jsonObj.getJSONArray("ImageData");
                    // Getting JSON Array node
                    JSONArray imageData = jsonObj.getJSONArray("ImageData");

                    if (imageData.length() == 0) {
                        Toast.makeText(ViewDocumentActivity.this, "No document found.", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < imageData.length(); i++) {
                            JSONObject c = imageData.getJSONObject(i);

                            String requestId = c.getString("HRGNUM_REQ_ID");
                            final String imageString = c.getString("GBYTE_DOC_DATA");


                            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            long lengthbmp = decodedString.length;
                            Log.i("after compression", "getImgStringfromBitmap: " + lengthbmp);

                            Bitmap converetdImage = getResizedBitmap(decodedByte, 1000);
                            image.setImageBitmap(converetdImage);
//                            image.setVisibility(View.VISIBLE);
                        /*if (i == 0) {
                            image.setImageBitmap(converetdImage);
                            image.setVisibility(View.VISIBLE);
                        } else if (i == 1) {
                            image2.setImageBitmap(converetdImage);
                            btnPage2.setVisibility(View.VISIBLE);
                        } else if (i == 2) {

                            image3.setImageBitmap(converetdImage);
                            btnPage3.setVisibility(View.VISIBLE);

                        }*/

                        }
                    }
                    image.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                } catch (final Exception e) {
                    image.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnPage1.setEnabled(true);
                btnPage2.setEnabled(true);
                btnPage3.setEnabled(true);
                Log.i("error", "onErrorResponse: " + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                ManagingSharedData msd=new ManagingSharedData(ViewDocumentActivity.this);

                Map<String, String> data = new HashMap<>();
                data.put("hospCode", msd.getHospCode());
                data.put("requestID", requestId);
                data.put("slno", slno);
                return data;
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

         MySingleton.getInstance(this).addToRequestQueue(request);
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
}
