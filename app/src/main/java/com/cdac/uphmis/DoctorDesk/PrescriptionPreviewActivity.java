package com.cdac.uphmis.DoctorDesk;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.DoctorDesk.model.CompleteHistoryJaonArray;
import com.cdac.uphmis.DoctorDesk.model.DiagnosisJsonArray;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.DoctorDesk.model.DrugJsonArray;
import com.cdac.uphmis.DoctorDesk.model.EHRJsonDetails;
import com.cdac.uphmis.DoctorDesk.model.EMRJsonDetails;
import com.cdac.uphmis.DoctorDesk.model.FollowUp;
import com.cdac.uphmis.DoctorDesk.model.PiccleArray;
import com.cdac.uphmis.DoctorDesk.model.PlannedVisit;
import com.cdac.uphmis.DoctorDesk.model.StrCompleteHistory;
import com.cdac.uphmis.DoctorDesk.model.StrSystematicExamniation;
import com.cdac.uphmis.DoctorDesk.model.Strpiccle;
import com.cdac.uphmis.DoctorDesk.model.SystematicExamniationArray;
import com.cdac.uphmis.DoctorDesk.savePrescription.JSON_DATA;
import com.cdac.uphmis.DoctorDesk.savePrescription.Pres_data;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.sql.DriverManager.println;


public class PrescriptionPreviewActivity extends AppCompatActivity {
    EditText tvComplaints, tvHopi, tvExamination, tvDiagnosis, tvDrugs, tvTests, tvProcedures, tvTreatmentAdvice,tvClinicalNotes, tvVitals;
    TextView tvFollowUp;
    String imageName = "";
    DoctorReqListDetails i;
    String strChiefComplaints = "",  strDiagnosis = "", strTests = "", strMedications = "", strVitals = "", strSwStatus = "",strClinicalNotes="";
    boolean isImageTaken = false;


    TextView tvDiagnosisHeader;

    String strSnomedCodes = "";

    String snomeCodesDiagnosis = "";

    GeometricProgressView progressView;
    Button btnOk, btnCancel;


    private String followUpDate = "", revisitDisplayDate = "";
    ManagingSharedData msd;


    private String PatCompleteGeneralDtlData = "";
    private String fatherName = "";
    private String designation = "";
    private String customerUnit = "";
    private String umidNo = "";
    private String catName = "";
    String decoded = "";

    ImageButton btnScanImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_preview);
        
        msd = new ManagingSharedData(this);

        progressView = (GeometricProgressView) findViewById(R.id.progress_view);

         btnScanImage = findViewById(R.id.scan_prescription);

        initViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        i = (DoctorReqListDetails) getIntent().getSerializableExtra("patientdetails");
        strVitals = getIntent().getStringExtra("strVitals");
        strChiefComplaints = getIntent().getStringExtra("strChiefComplaints");
        //strHopi = getIntent().getStringExtra("strHopi");
        //strExamination = getIntent().getStringExtra("strExamination");
        strDiagnosis = getIntent().getStringExtra("strDiagnosis");
        strClinicalNotes = getIntent().getStringExtra("strClinicalNotes");

        strTests = getIntent().getStringExtra("strTests");
      //  strProcedures = getIntent().getStringExtra("strProcedures");
        strMedications = getIntent().getStringExtra("strMedications");
        strSwStatus = getIntent().getStringExtra("swStatus");

        Log.i("str", "onCreate: " + strDiagnosis);


        strSnomedCodes = getIntent().getStringExtra("snomedcodes");
        snomeCodesDiagnosis = getIntent().getStringExtra("snomedcodesdiagnosis");

        followUpDate = getIntent().getStringExtra("revisitDate");
        revisitDisplayDate = getIntent().getStringExtra("revisitDisplayDate");


        Log.i("patientdetais", "onCreate: " + i.getPatName());


        tvComplaints.setText("" + strChiefComplaints);
//        tvHopi.setText("" + strHopi);
        //tvExamination.setText("" + strExamination);
        tvDiagnosis.setText("" + strDiagnosis);


        tvTests.setText("" + strTests);
       // tvProcedures.setText("" + strProcedures);
        tvTreatmentAdvice.setText("" + strMedications);
        tvVitals.setText("" + strVitals);
        tvClinicalNotes.setText("" + strClinicalNotes);

        tvFollowUp.setText("" + revisitDisplayDate);
        tvFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        Log.i("stt", "onCreate: " + strDiagnosis);
        tvDiagnosis.setText(strDiagnosis);


        diagnosisCreateView();
        btnOk = findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveOkPrescription();


            }
        });


        btnCancel = findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        setToolbar();

        tvVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == tvVitals.getId()) {
                    tvVitals.setCursorVisible(true);
                }

            }
        });

        //check();

        getPatientDetail();


      //  String progressNote = "History: " + strHopi + ", \nVitals: " + strVitals + ", \nTreatment Adviced: " + strMedications + ", \nExaminations: " + strExamination + ", \nProcedures: " + strProcedures + ", \nTests: " + strTests + ", \nChief Complaints: " + strChiefComplaints + ", \nDiagnosis: " + strDiagnosis;

       // Log.i("createJsonEMR", "onCreate: "+createJsonEMR(i,progressNote));
    }

    public void saveOkPrescription() {
      //  String data = "Complaints:\n" + strChiefComplaints + "\n\nHOPI:\n" + strHopi + "\n\nExamination:\n" + strExamination + "\n\nDiagnosis:\n" + strDiagnosis + "\n\nDrugs :\n" + "\n\nTests :\n" + strTests + "\n\nProcedures :\n" + strProcedures + "\n\nTreatment Adviced :\n" + strMedications;

        strVitals = tvVitals.getText().toString();
        strChiefComplaints = tvComplaints.getText().toString();
       // strHopi = tvHopi.getText().toString();
        //strExamination = tvExamination.getText().toString();
        strDiagnosis = tvDiagnosis.getText().toString();

        strTests = tvTests.getText().toString();
        //strProcedures = tvProcedures.getText().toString();
        strMedications = tvTreatmentAdvice.getText().toString();

        new AlertDialog.Builder(this)
                .setMessage("Do you want to save prescription?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        progressView.setVisibility(View.VISIBLE);
                        btnOk.setEnabled(false);
                        btnCancel.setEnabled(false);
                        saveEhrJsonData();
                        //sendPrescriptionData();

                    }
                })
                .setNegativeButton("No", null)
                .show();


    }

    private void diagnosisCreateView() {


//        autoCompleteTextView = findViewById(R.id.auto_complete_final_diagnosis);


        Log.i("previewSwStatus", "onCreate: " + strSwStatus);
        Log.i("previewSwStatus", "onCreate: " + strDiagnosis);
        if (strSwStatus.equalsIgnoreCase("final")) {
//            autoCompleteTextView.setVisibility(View.VISIBLE);
            tvDiagnosisHeader.setText(" (Final Diagnosis):");
            tvDiagnosis.setText("");
            tvDiagnosis.append(strDiagnosis + " ");
            tvDiagnosis.setEnabled(false);
        } else {
//            autoCompleteTextView.setVisibility(View.GONE);
            tvDiagnosisHeader.setText(" (Provisional Diagnosis):");
            String s = tvDiagnosis.getText().toString().replace("Final Diagnosis:", "Provisional Diagnosis:\n");
            tvDiagnosis.setText("");
            tvDiagnosis.append(strDiagnosis + " ");
            tvDiagnosis.setEnabled(true);
        }


       /* autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("lasttext", "onTextChanged: " + lastText);
                Log.i("s.tostring", "onTextChanged: " + s.toString().length());
                if (s.toString().length() >= 3) {
                    getDiagnosis(s.toString());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof DiagnosisDetails) {
                    DiagnosisDetails selectedItem = (DiagnosisDetails) item;
                    autoCompleteTextView.dismissDropDown();
                    Log.i("student", "onItemClick: " + selectedItem.getTerm());

                    autoCompleteTextView.setText("");
                    tvDiagnosis.append(selectedItem + ", ");

                    strSnomedCodes = strSnomedCodes + selectedItem.getTerm() + "|" + selectedItem.getId() + ",";

                    snomeCodesDiagnosis = selectedItem.getId() + ",";

                }
            }
        });*/


    }

    private void setToolbar() {
        ImageView imageToolBar = findViewById(R.id.img_toolbar);
        TextView tvPatientname = findViewById(R.id.tv_patient_name);
        TextView tvAgeGender = findViewById(R.id.tv_age_gender);


        tvPatientname.setText(i.getPatName());
        tvAgeGender.setText(i.getPatAge());
        if (i.getPatGender().equalsIgnoreCase("F")) {

            Drawable res = getResources().getDrawable(R.drawable.female);
            imageToolBar.setImageDrawable(res);

        } else {
            Drawable res = getResources().getDrawable(R.drawable.male);
            imageToolBar.setImageDrawable(res);
        }
    }


   /* public static String getReportPath(String filename, String extension) {

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + filename + "." + extension);

        return uriSting;

    }*/

    private void initViews() {


        tvVitals = findViewById(R.id.tv_vitals);
        tvComplaints = findViewById(R.id.tv_complaints);
//        tvHopi = findViewById(R.id.tv_hopi);
        tvExamination = findViewById(R.id.tv_examinations);
        tvDiagnosis = findViewById(R.id.tv_diagnosis);
//        tvDrugs = findViewById(R.id.tv_drugs);
        tvDiagnosisHeader = findViewById(R.id.tv_diagnosis_header);
        tvTests = findViewById(R.id.tv_tests);
        tvProcedures = findViewById(R.id.tv_procedures);
        tvTreatmentAdvice = findViewById(R.id.tv_treatment_advice);
        tvClinicalNotes = findViewById(R.id.tv_clinical_notes);

        tvFollowUp = findViewById(R.id.tv_follow_up);
        MovableFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
            }
        });
    }

    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }


    public void clickPresCription(View view) {
   selectImage(this);
    }

    private void openCamera() {
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());

        imageName = fileName;

        File imagepath = new File(getFilesDir(), "/" + imageName + ".jpg");

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this.getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imagepath));

        startActivityForResult(i, 123);

    }
    private void pickFromGallery1()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 40);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                    openCamera();
            } else if (options[item].equals("Choose from Gallery")) {
                    pickFromGallery1();
            } else if (options[item].equals("Cancel")) {
                decoded="";
                isImageTaken = false;
                btnScanImage.setImageResource(android.R.drawable.ic_menu_camera);
                dialog.dismiss();
            }
        });
        builder.show();
    }











    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {
            isImageTaken = true;
            btnScanImage.setImageResource(R.drawable.right);
        }
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.get(0).toLowerCase().contains("ok") || result.get(0).toLowerCase().contains("ok")) {
                        saveOkPrescription();
                    }
                    break;
                }



                //pick from gallery
            case 40:
                if (requestCode == 40 && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);

                         Bitmap bitmap = changeOrientation(picturePath);
                            bitmap = getResizedBitmap(bitmap, 1000);
                            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream3);
                            decoded = Base64.encodeToString(stream3.toByteArray(),
                                    Base64.NO_WRAP);
                            cursor.close();

                            isImageTaken =false;
                            btnScanImage.setImageResource(R.drawable.right);
                            }
                        }

                }
                break;
        }
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
        return getResizedBitmap(rotatedBitmap, 1000);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            //handle click
            getSpeechInput();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String check() {
        String imageData = "";


        String strDiagnosisType = "";
        if (strSwStatus.equalsIgnoreCase("final")) {
            strDiagnosisType = "Final";
            strDiagnosis = strSnomedCodes;

        } else {
            strDiagnosisType = "Provisional";
        }
        JSON_DATA json_data = new JSON_DATA();
        json_data.setPat_data(i);
        json_data.setPres_data(new Pres_data("", "", strVitals, strMedications, "", "", strTests, strDiagnosisType, "", strChiefComplaints, strDiagnosis));

        Gson gson = new Gson();


        String s = gson.toJson(json_data);


//        Log.i("s", "check: " + s);

        String directoryPath = getFilesDir().toString();

        if (isImageTaken) {
            Bitmap bitmap = changeOrientation(directoryPath + "/" + imageName + ".jpg");
            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            decoded = Base64.encodeToString(stream3.toByteArray(),
                    Base64.NO_WRAP);
            println(new String(decoded));

        }


        imageData = ",{\n" +
                " \t\t\"image\": {\n" +
                " \t\t\t\"image_data\": \"" + decoded + "\"\n" +
                " \t\t}}";

        String snomedDataDiagnosis = ",{\n" +
                " \t\t\"snomed\": {\n" +
                " \t\t\t\"snomed_diagnosis\": \"" + snomeCodesDiagnosis + "\"\n" +
                " \t\t}}";


        String revisitDate = ",{\n" +
                " \t\t\"revisit\": {\n" +
                " \t\t\t\"revisitDate\": \"" + followUpDate + "\"\n" +
                " \t\t}}";
        ;


        s = "{\n" +
                "\t\"JSON_DATA\": [" + s + imageData + snomedDataDiagnosis + revisitDate + "]\n" +
                "}";

        Log.i("s", "check: " + s);
        return s;
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
    public void sendPrescriptionData() {
        Toast.makeText(this, "Please wait while saving prescription.", Toast.LENGTH_SHORT).show();

        try {


            String URL = ServiceUrl.savePrescriptionUrl;


            final String requestBody = check();
            Log.i("requestBody", "sendPrescriptionData: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("response", "onResponse: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String data = jsonObject.getString("PRES_PDF");


                        saveBase64Pdf(data);

                    } catch (Exception ex) {
                        Log.i("json exception", "onResponse: " + ex);
                        progressView.setVisibility(View.GONE);
                        btnOk.setEnabled(true);
                        btnCancel.setEnabled(true);
                        Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    progressView.setVisibility(View.GONE);
                    btnOk.setEnabled(true);
                    btnCancel.setEnabled(true);

                    AppUtilityFunctions.handleExceptions(error, PrescriptionPreviewActivity.this);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(PrescriptionPreviewActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            btnOk.setEnabled(true);
            btnCancel.setEnabled(true);
            Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void saveBase64Pdf(String data) {
        try {

            updateRequestStatus(i.getRequestID(), "2", i.getDocMessage());

            Intent intent = new Intent(this, DoctorRequestListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {
            Log.i("pdf exception", "saveBase64Pdf: " + ex);

        }
    }

    private void updateRequestStatus(String requestId, String requestStatus, String docMessage) {


//        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateRequestStatus + "requestID=" + requestId + "&reqStatus=" + requestStatus, new Response.Listener<String>() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateRequestStatus + msd.getHospCode() + "&requestID=" + requestId + "&reqStatus=" + requestStatus + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&consltMobNo=" + msd.getPatientDetails().getMobileNo() + "&docMessage=" + docMessage + "&doctorToken=", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaaa", "onResponse: " + response);
                //send prescription
                ManagingSharedData msd = new ManagingSharedData(PrescriptionPreviewActivity.this);
                String title = "eConsultation Completed";
                String message = "Your prescription for eConsultation with " + msd.getUsername() + " has been generated. You can view the PDF from \"Consultation and Status\" page.";
                sendFCMPush(title, message);

                progressView.setVisibility(View.GONE);
                btnOk.setEnabled(true);
                btnCancel.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                btnOk.setEnabled(true);
                btnCancel.setEnabled(true);
                Toast.makeText(PrescriptionPreviewActivity.this, "Unable to update status.Try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void sendFCMPush(String notificationTitle, String message) {


//        String Legacy_SERVER_KEY = getResources().getString(R.string.server_legacy_key);
        String title = notificationTitle;
        String token = i.getPatientToken();

        JSONObject obj = null;


        try {


            obj = new JSONObject();
            JSONObject objData1 = new JSONObject();

            // objData.put("data", msg);
            objData1.put("title", title);
            objData1.put("content", message);
            objData1.put("navigateTo", "");


            obj.put("data", objData1);
            obj.put("to", token);

            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl+"callPushNotfication/call", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
//                params.put("Content-Type", "application/json");
                return params;
            }
        };

        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private Object createJsonEHR(DoctorReqListDetails patientData, String progressNote) {
        ManagingSharedData msd = new ManagingSharedData(this);
        try {
            ArrayList plannedArrayList = new ArrayList();
            plannedArrayList.add(new PlannedVisit());
            FollowUp follow = new FollowUp("0", plannedArrayList, progressNote);
            ArrayList<FollowUp> followUpArrayList = new ArrayList<>();
            followUpArrayList.add(follow);


            List<Object> invTestCode = new ArrayList();
            List<Object> invTestCodeToPrint = new ArrayList();
            List<Object> drugCodeCat = new ArrayList();
            List<Object> reasonOfVisit = new ArrayList();
            List<Object> diagnosis = new ArrayList();
            List<FollowUp> followUp = followUpArrayList;
            String patName = patientData.getPatName();
            String cRNo = patientData.getCRNo();
            String episodeCode = patientData.getEpisodeCode();
            String episodeVisitNo = patientData.getEpisodeVisitNo();
            String currentVisitDate = patientData.getpVisitdate();
            String patVisitType = "";
            String lastVisitDate = "";
            String patGender = patientData.getPatGender();
            String patAge = patientData.getPatAge();
            String patCat = this.catName;
            String patQueueNo = "";
            String hospCode = msd.getHospCode();
            String seatId = msd.getEmployeeCode();
            Integer hrgnumIsDocuploaded = 0;
            String patConsultantName = msd.getUsername();
            String patDept = patientData.getDeptUnitName();
            String patGaurdianName = this.fatherName;

//           String PatCompleteGeneralDtlData="--#General Medicine ( General Unit) #NA#Tr#1#10511#105#10-Jun-2021 #General Medicine#General Unit#11#1#2021/06/10#2021/06/10#9874561200#1#0#-#0#0#^^^null";
//           String PatCompleteGeneralDtlData="--#General Medicine ( General Unit) #NA#Tr#1#10511#105#10-Jun-2021 #General Medicine#General Unit#11#1#2021/06/10#2021/06/10#9874561200#1#0#-#0#0#^^^null";
//                                              ==#deptunitname#NA#Tr#1#deptunitcode#deptcode#unitcode#<pata nahin kaunsi date>#deptname#unitname


            String patCompleteGeneralDtl = this.PatCompleteGeneralDtlData + "^^^null";


            StrCompleteHistory strCompleteHistory = new StrCompleteHistory();
            StrSystematicExamniation strSystematicExamniation = new StrSystematicExamniation();
            List<Object> strChronicDisease = new ArrayList();
            String strHistoryOfPresentIllNess = "";
            String strDiagnosisNote = "";
            List<Object> strDrugAllergy = new ArrayList();
            String strInvestgationNote = "";
            String strotherAllergies = "";
            List<Object> strClinicalProcedure = new ArrayList();
            String strtreatmentAdvice = "";
            String strVitalsChart = "";
            Strpiccle strpiccle = new Strpiccle();
            String strConfidentialsInfo = "";
            List<Object> strReferal = new ArrayList();
            String strDeptIdflg = "";
            String strAllDeptIdflg = "";
            String strPresCriptionBookmarkNameval = "";
            String strPresCriptionBookmarkDescVal = "";
            String strUmidNo = this.umidNo;
            String admissionadviceDeptName = "";
            String admissionadviceWardName = "";
            String admissionadviceNotes = "";
            String strDesignation = this.designation;
            String strStation = this.customerUnit;

            EHRJsonDetails ehrJsonDetails = new EHRJsonDetails(invTestCode, invTestCodeToPrint, drugCodeCat, reasonOfVisit, diagnosis, followUp, patName, cRNo, episodeCode, episodeVisitNo, currentVisitDate, patVisitType, lastVisitDate, patGender, patAge, patCat, patQueueNo, hospCode, seatId, hrgnumIsDocuploaded, patConsultantName, patDept, patGaurdianName, patCompleteGeneralDtl, strCompleteHistory, strSystematicExamniation, strChronicDisease, strHistoryOfPresentIllNess, strDiagnosisNote, strDrugAllergy, strInvestgationNote, strotherAllergies, strClinicalProcedure, strtreatmentAdvice, strVitalsChart, strpiccle, strConfidentialsInfo, strReferal, strDeptIdflg, strAllDeptIdflg, strPresCriptionBookmarkNameval, strPresCriptionBookmarkDescVal, strUmidNo, admissionadviceDeptName, admissionadviceWardName, admissionadviceNotes, strDesignation, strStation);

//            Gson gson=new Gson();
            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.create();
            //  String property =

            return gson.toJson(ehrJsonDetails);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }


    public void saveEhrJsonData() {
       // Toast.makeText(this, "Please wait while saving prescription.", Toast.LENGTH_SHORT).show();

        try {


            String URL = ServiceUrl.saveEhrJsonData;
           // String progressNote = "History: " + strHopi + ", \nVitals: " + strVitals + ", \nTreatment Adviced: " + strMedications + ", \nExaminations: " + strExamination + ", \nProcedures: " + strProcedures + ", \nTests: " + strTests + ", \nChief Complaints: " + strChiefComplaints + ", \nDiagnosis: " + strDiagnosis;
            String progressNote ="\n\nVitals: "+strVitals+"\n\nChief Complaints: "+strChiefComplaints+"\n\nDiagnosis: "+strDiagnosis+"\n\nInvestigation Advised:"+strTests+"\n\nMedications:\n"+strMedications+"\n\nTreatment/Clinical Note:"+strClinicalNotes;

            final String requestBody = (String) createJsonEHR(i, progressNote);
            Log.i("requestBody", "sendPrescriptionData: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.i("saveEhrJsonData", "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        callFollowUpService(progressNote);
                    }else
                    {
                        progressView.setVisibility(View.GONE);
                        btnOk.setEnabled(true);
                        btnCancel.setEnabled(true);
                        Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.i("json exception", "onResponse: " + ex);
                    progressView.setVisibility(View.GONE);
                    btnOk.setEnabled(true);
                    btnCancel.setEnabled(true);
                    Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                }

            }, error -> {
                Log.e("VOLLEY", error.toString());

                progressView.setVisibility(View.GONE);
                btnOk.setEnabled(true);
                btnCancel.setEnabled(true);

                AppUtilityFunctions.handleExceptions(error, PrescriptionPreviewActivity.this);
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(PrescriptionPreviewActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            btnOk.setEnabled(true);
            btnCancel.setEnabled(true);
            Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void callFollowUpService(String progressNote) {

       // Toast.makeText(this, "Please wait while saving prescription.", Toast.LENGTH_SHORT).show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CR_No", i.getCRNo());
            jsonObject.put("episodeCode", i.getEpisodeCode());
            jsonObject.put("hosp_code", msd.getHospCode());
            jsonObject.put("visitNo", i.getEpisodeVisitNo());
            jsonObject.put("progressNote", progressNote);
            jsonObject.put("PlannedVisitDate", followUpDate);

            String URL = ServiceUrl.followUpService;


            final String requestBody = jsonObject.toString();
            Log.i("requestBody", "callFollowUpService: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("callFollowUpService", "onResponse: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            saveEmrJsonData();
                        }else
                        {
                            progressView.setVisibility(View.GONE);
                            btnOk.setEnabled(true);
                            btnCancel.setEnabled(true);
                            Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Log.i("json exception", "onResponse: " + ex);
                        progressView.setVisibility(View.GONE);
                        btnOk.setEnabled(true);
                        btnCancel.setEnabled(true);
                        Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    progressView.setVisibility(View.GONE);
                    btnOk.setEnabled(true);
                    btnCancel.setEnabled(true);

                    AppUtilityFunctions.handleExceptions(error, PrescriptionPreviewActivity.this);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(PrescriptionPreviewActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            btnOk.setEnabled(true);
            btnCancel.setEnabled(true);
            Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }



    public void saveEmrJsonData() {

        try {


            String URL = ServiceUrl.saveEmrJsonData;
     //       String progressNote = "History: " + strHopi + ", \nVitals: " + strVitals + ", \nTreatment Adviced: " + strMedications + ", \nExaminations: " + strExamination + ", \nProcedures: " + strProcedures + ", \nTests: " + strTests + ", \nChief Complaints: " + strChiefComplaints + ", \nDiagnosis: " + strDiagnosis;

            String progressNote ="\n\nVitals: "+strVitals+"\n\nChief Complaints: "+strChiefComplaints+"\n\nDiagnosis: "+strDiagnosis+"\n\nInvestigation Advised:"+strTests+"\n\nMedications:\n"+strMedications+"\n\nTreatment/Clinical Note:"+strClinicalNotes;





            final String requestBody = (String) createJsonEMR(i, progressNote);
            Log.i("requestBody", "saveEmrJsonData: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("saveEhrJsonData", "onResponse: " + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            //callFollowUpService(progressNote);
                            sendPrescriptionData();
                        }
                    } catch (Exception ex) {
                        Log.i("json exception", "onResponse: " + ex);
                        progressView.setVisibility(View.GONE);
                        btnOk.setEnabled(true);
                        btnCancel.setEnabled(true);
                        Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    progressView.setVisibility(View.GONE);
                    btnOk.setEnabled(true);
                    btnCancel.setEnabled(true);

                    AppUtilityFunctions.handleExceptions(error, PrescriptionPreviewActivity.this);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", "RMLCDACUSER");


                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(PrescriptionPreviewActivity.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            progressView.setVisibility(View.GONE);
            btnOk.setEnabled(true);
            btnCancel.setEnabled(true);
            Toast.makeText(PrescriptionPreviewActivity.this, "Unable to save prescription.Please check your internet connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    private void getPatientDetail() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetailSavePrescription + "?hosp_code=" + msd.getHospCode() + "&crNo=" + i.getCRNo() + "&visitNo=" + i.getEpisodeVisitNo() + "&episodeCode=" + i.getEpisodeCode(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getPatientDetail", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = null;

                    status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        PatCompleteGeneralDtlData = jsonObject.getString("Pat_details");
                        umidNo = jsonObject.getString("umid_no");
                        designation = jsonObject.getString("designation");
                        fatherName= jsonObject.getString("fatherName");
                        customerUnit = jsonObject.getString("customUnit");
                        catName = jsonObject.getString("catName");
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getPatientDetail", "onErrorResponse: ", error);
            }
        });

        MySingleton.getInstance(PrescriptionPreviewActivity.this).addToRequestQueue(request);
    }


    private Object createJsonEMR(DoctorReqListDetails patientData, String progressNote) {
        ManagingSharedData msd = new ManagingSharedData(this);
        try {
            ArrayList plannedArrayList = new ArrayList();
            plannedArrayList.add(new PlannedVisit());
            FollowUp follow = new FollowUp("0", plannedArrayList, progressNote);
            ArrayList<FollowUp> followUpArrayList = new ArrayList<>();
            followUpArrayList.add(follow);


            DiagnosisJsonArray diagnosisJsonArrayDetails = new DiagnosisJsonArray("", "", "", "", "", "", "", "");
            ArrayList<DiagnosisJsonArray> diagnosisJsonArrayArrayList = new ArrayList<>();
//            diagnosisJsonArrayArrayList.add(diagnosisJsonArrayDetails);


            //DrugJsonArray drugJsonArrayDetails=new DrugJsonArray("","","","","","","","","","","");
           DrugJsonArray drugJsonArrayDetails=new DrugJsonArray();
            List < DrugJsonArray > drugJsonArrayArrayList=new ArrayList<>();
            //drugJsonArrayArrayList.add(drugJsonArrayDetails);

//            List<Object> invTestCode = new ArrayList();
//            List<Object> invTestCodeToPrint = new ArrayList();
//            List<Object> drugCodeCat = new ArrayList();
//            List<Object> reasonOfVisit = new ArrayList();
//            List<Object> diagnosis = new ArrayList();
//            List<FollowUp> followUp = followUpArrayList;

            List<DiagnosisJsonArray> diagnosisJsonArray = diagnosisJsonArrayArrayList;
            List<DrugJsonArray> drugJsonArray = drugJsonArrayArrayList;


            String patName = patientData.getPatName();
            String cRNo = patientData.getCRNo();
            String episodeCode = patientData.getEpisodeCode();
            String episodeVisitNo = patientData.getEpisodeVisitNo();
            String currentVisitDate = patientData.getpVisitdate();
            String patVisitType = "";
            String lastVisitDate = "";
            String patGender = patientData.getPatGender();
            String patAge = patientData.getPatAge();
            String patCat = this.catName;
            String patQueueNo = "";
            String hospCode = msd.getHospCode();
            String seatId = msd.getEmployeeCode();
            Integer hrgnumIsDocuploaded = 0;
            String patConsultantName = msd.getUsername();
            String patDept = patientData.getDeptUnitName();
            String patGaurdianName = this.fatherName;


            String patCompleteGeneralDtl = this.PatCompleteGeneralDtlData + "^^^null";


//            StrCompleteHistory strCompleteHistory = new StrCompleteHistory();
//            StrSystematicExamniation strSystematicExamniation = new StrSystematicExamniation();
//            List<Object> strChronicDisease = new ArrayList();
//            String strHistoryOfPresentIllNess = "";
//            String strDiagnosisNote = "";
//            List<Object> strDrugAllergy = new ArrayList();
//            String strInvestgationNote = "";
//            String strotherAllergies = "";
//            List<Object> strClinicalProcedure = new ArrayList();
            String strtreatmentAdvice = "";
            String strVitalsChart = "";
            Strpiccle strpiccle = new Strpiccle();
            String strConfidentialsInfo = "";
        //    List<Object> strReferal = new ArrayList();
            String strDeptIdflg = "";
            String strAllDeptIdflg = "";
            String strPresCriptionBookmarkNameval = "";
            String strPresCriptionBookmarkDescVal = "";
            String strUmidNo = this.umidNo;
            String admissionadviceDeptName = "";
            String admissionadviceWardName = "";
            String admissionadviceNotes = "";
            String strDesignation = this.designation;
            String strStation = this.customerUnit;
            String historyOfPresentIllNess = "";
            String diagnosisNote = "";
            String investgationNote = "";
            String otherAllergies = "";
            List<Object> reasonOfVisitJsonArray = new ArrayList<>();
            List<Object> investigationJsonArray = new ArrayList<>();
            CompleteHistoryJaonArray completeHistoryJaonArray = new CompleteHistoryJaonArray("", "", "", "", "");
            SystematicExamniationArray systematicExamniationArray = new SystematicExamniationArray("", "", "", "", "", "", "");

            List<Object> chronicDiseaseArray = new ArrayList<>();
            PiccleArray piccleArray = new PiccleArray("0", "0", "0", "0", "0", "0");
            List < Object > clinicalProcedureJsonArray=new ArrayList<>();
            List < Object > patientRefrel=new ArrayList<>();

            EMRJsonDetails emrJsonDetails = new EMRJsonDetails(patName, cRNo, episodeCode, episodeVisitNo, currentVisitDate, patVisitType, lastVisitDate, patGender, patAge, patCat, patQueueNo, hospCode, hrgnumIsDocuploaded, patConsultantName, patDept, patGaurdianName, patCompleteGeneralDtl, seatId, historyOfPresentIllNess, diagnosisNote, investgationNote, otherAllergies, reasonOfVisitJsonArray, diagnosisJsonArray, investigationJsonArray, completeHistoryJaonArray, systematicExamniationArray, chronicDiseaseArray,  piccleArray,  clinicalProcedureJsonArray,  drugJsonArray,  patientRefrel, strpiccle, strtreatmentAdvice, strConfidentialsInfo, strVitalsChart,  followUpArrayList, strDeptIdflg, strAllDeptIdflg, strPresCriptionBookmarkNameval, strPresCriptionBookmarkDescVal, strUmidNo, admissionadviceDeptName, admissionadviceWardName, admissionadviceNotes, strDesignation, strStation);


            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.create();
            //  String property =

            return gson.toJson(emrJsonDetails);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }

}