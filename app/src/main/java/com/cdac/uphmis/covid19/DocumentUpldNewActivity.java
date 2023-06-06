package com.cdac.uphmis.covid19;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.VolleyMultipartRequest;
import com.cdac.uphmis.covid19.adapter.CustomGridAdapter;
import com.cdac.uphmis.covid19.model.DocumentDetails;
import com.cdac.uphmis.covid19.model.ScreeningDetails;
import com.cdac.uphmis.opdLite.util.ParseSpeechInput;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.facebook.react.common.LifecycleState;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentUpldNewActivity extends AppCompatActivity {



    ActivityResultLauncher<Intent> selectImagesActivityResult;

    private ArrayList<DocumentDetails> documentDetailsArrayList;
    private GridView gridView;
    private CustomGridAdapter adapter;

    int success=0, fail=0, imageCount = 0;
    private GeometricProgressView progressView;


    private String requestId, crno, appointmentDetails;
    private ScreeningDetails screeningDetails;
    Button up;
    private void initVar() {
        progressView = findViewById(R.id.progress_view);
        up = findViewById(R.id.up);
        progressView.setVisibility(View.GONE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upld_new);
        
        initVar();

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
        documentDetailsArrayList = new ArrayList<>();

        gridView = findViewById(R.id.grid_view);

        adapter = new CustomGridAdapter(this, documentDetailsArrayList);
        gridView.setAdapter(adapter);
        selectImagesActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (null != data) { // checking empty selection
                            if (null != data.getClipData()) { // checking multiple selection or not
                                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                    Uri uri = data.getClipData().getItemAt(i).getUri();

                                    String mimeType = getMimeType(uri);

                                    Log.i("path", "onCreate: " + uri.getPath());

                                    //if (!documentDetailsArrayList.contains(uri.toString()))
                                    documentDetailsArrayList.add(new DocumentDetails(uri, mimeType));
                                    Log.i("aa", "onCreate: " + uri);

                                }


                            } else {
                                Uri uri = data.getData();
                                String mimeType = getMimeType(uri);

                                documentDetailsArrayList.add(new DocumentDetails(uri, mimeType));

                                Log.i("aa", "onCreate: " + uri);
                            }
                            adapter.notifyDataSetChanged();

                        }




                    }
                });


    }

    public void btnSelectFiles(View view) {
        if (documentDetailsArrayList.size() > 10) {
            Toast.makeText(this, "Maximum of 10 documents can be uploaded.", Toast.LENGTH_SHORT).show();
        } else {
            getFileChooserIntent();
        }
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        selectImagesActivityResult.launch(intent);
        // return intent;
    }

    public void btnUpload(View view) {
        if (documentDetailsArrayList.size() > 10) {
            Toast.makeText(this, "Maximum of 10 documents can be uploaded.", Toast.LENGTH_SHORT).show();
            return;
        }else if(documentDetailsArrayList.size()==0)
        {
            new AlertDialog.Builder(this)
                    .setTitle("No Document selected")
                    .setMessage("Do you want to continue without uploading document?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(DocumentUpldNewActivity.this, AppointmentSuccefullActivity.class);
                        intent.putExtra("requestId", requestId);
                        intent.putExtra("crno", crno);
                        intent.putExtra("screeningDetails", screeningDetails);
                        intent.putExtra("apptDetails", appointmentDetails);
                        startActivity(intent);
                        finish();
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm?")
                    .setMessage("Do you want to upload selected documents?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            for (int i = 0; i < documentDetailsArrayList.size(); i++) {
                                if (documentDetailsArrayList.get(i).getMimeType().startsWith("image")) {
                                    uploadImage(documentDetailsArrayList.get(i).getUri(), "page" + i + ".png", String.valueOf(i));
                                } else {
                                    uploadImage(documentDetailsArrayList.get(i).getUri(), "page" + i + ".pdf", String.valueOf(i));
                                }

                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }

    }

    private void uploadImage(Uri uri, String fileName, String sNo) {
        progressView.setVisibility(View.VISIBLE);
        up.setEnabled(false);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ServiceUrl.testurl + "econsultation/uploadDocument",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        imageCount++;
                        Log.i("TAG", "onResponse: " + response);
                        if (response.statusCode == 200) {
                            success++;
                        } else {
                            fail++;
                        }
                        getImageCount();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", "onErrorResponse: " + error);
                        imageCount++;
                        fail++;
                        progressView.setVisibility(View.GONE);
                        up.setEnabled(true);
                        AppUtilityFunctions.handleExceptions(error,DocumentUpldNewActivity.this);
                        getImageCount();

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ImageData", "1");
                params.put("hospCode",screeningDetails.getHospCode());
                params.put("requestID", requestId);
                params.put("slno", sNo);
                params.put("fileExtension", "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

//                byte[] file = convert(uri.getPath());
                byte[] file = new byte[0];
                try {
                    file = readBytes(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("bytes", "convert: " + file.toString());

                params.put("file", new DataPart(fileName, file));

                params.put("file", new DataPart(fileName, file));


                return params;
            }
        };

        int socketTimeout = 50000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(volleyMultipartRequest);
    }




    public byte[] readBytes(Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = getContentResolver().openInputStream(uri);
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


    private void alertDilog(String ok, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DocumentUpldNewActivity.this);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                success = 0;
                fail = 0;

                Intent intent = new Intent(DocumentUpldNewActivity.this, AppointmentSuccefullActivity.class);
                intent.putExtra("requestId", requestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                startActivity(intent);
                finish();
            }
        });
        builder1.setNegativeButton(
                "",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        builder1.create();
    }


    private void getImageCount()
    {
        if(imageCount==documentDetailsArrayList.size()) {
            if (success == documentDetailsArrayList.size()) {
                progressView.setVisibility(View.GONE);
                alertDilog("Close", success + " Document Successfully Uploaded.\n");

            } else {
                progressView.setVisibility(View.GONE);
                alertDilog("Close", fail + " Document upload failed, please try again.");

            }
        }
    }


}

