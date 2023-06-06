package com.cdac.uphmis.qms;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class IssueTokenActivity extends AppCompatActivity {

    private ManagingSharedData msd;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    int isFlashOn = 0;

    ImageButton btnFlash;
    GeometricProgressView progressView;


    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_token);
        
        initializeTTS();
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        msd = new ManagingSharedData(this);

        progressView = findViewById(R.id.progress_view);


        barcodeView = findViewById(R.id.barcode_scanner);
        Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText("");
        barcodeView.setCameraDistance(500);


        beepManager = new BeepManager(this);
        btnFlash = findViewById(R.id.btn_flash);
        btnFlash.setOnClickListener(view12 -> {
            if (isFlashOn == 0) {
                isFlashOn = 1;
                barcodeView.setTorchOn();
            } else if (isFlashOn == 1) {
                isFlashOn = 0;
                barcodeView.setTorchOff();
            }

        });

    }


    @Override
    public void onPause() {
        super.onPause();

        barcodeView.pause();
        pause(barcodeView);
    }

    @Override
    public void onResume() {
        super.onResume();

        barcodeView.resume();
        resume(barcodeView);
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            if (result.getText() == null || result.getText().equals(lastText) || result.getText().length() < 20) {
                // Prevent duplicate scans
                Log.i("lastText", "barcodeResult: " + lastText);
                if (result.getText()!=null && !result.getText().equalsIgnoreCase(lastText))
                     Toast.makeText(IssueTokenActivity.this, "Invalid QR Code"+result.getText(), Toast.LENGTH_SHORT).show();

                return;
            }


            lastText = result.getText();


            Log.i("TAG", "barcodeResult: " + lastText);
            beepManager.playBeepSoundAndVibrate();

            //if qr contains data
            try {
                //converting the data to json
                JSONObject obj = new JSONObject(lastText);
                //  "key":"3320163001$0$0$0"}
                String hospCode = "", serviceId = "", tokenQty = "1", counterId = "", isGender = "0", isSeniorCitizen = "0", isFamily = "0";
                if (obj.has("key")) {

                    String key = obj.optString("key");
                    String[] ar = key.split("\\$");
                    counterId = ar[0];
                    hospCode = counterId.substring(0, 5);
                    serviceId = counterId.substring(5, 7);
                    isGender = ar[1];
                    isSeniorCitizen = ar[2];
                    isFamily = ar[3];

                    if (!isFamily.equalsIgnoreCase("0"))
                        tokenQty = "4";
                } else {
                    Toast.makeText(IssueTokenActivity.this, "Invalid QR Code" + lastText, Toast.LENGTH_LONG).show();
                    return;
                }


                callStampingService(msd.getPatientDetails().getCrno(), hospCode, counterId, serviceId, isGender, isFamily, isSeniorCitizen, tokenQty);


            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(IssueTokenActivity.this, "Invalid QR Code " + lastText, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(IssueTokenActivity.this, "Invalid QR Code " + lastText, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void callStampingService(String crno, String hospCode, String counterId, String serviceId, String isGender, String isFamily, String isSeniorCitizen, String familyCount) {
        progressView.setVisibility(View.VISIBLE);
        Log.i("jsonData", "callStampingService: " + hospCode + serviceId);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.ip + "HQMS/services/restful/QmsServiceMobile/generateToken?crno=" + crno + "&hospCode=" + hospCode + "&counterId=" + counterId + "&serviceId=" + serviceId + "&isGender=" + isGender + "&isFamily=" + isFamily + "&isSeniorCtz=" + isSeniorCitizen + "&familyCount=" + familyCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("eStampingResponse", "onResponse: " + response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message="";
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        //String message = jsonArray.getJSONObject(0).getString("MSG");
                        JSONObject c = jsonArray.getJSONObject(0);
                       // String isSuccess = c.getString("STATUS");
                        String msg=c.getString("MSG");
                        showBottomSheetDialog("Token Generation", msg);


                        ConvertTextToSpeech(msg);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    try {
                        //  JSONObject jsonObject1 = new JSONObject(response);
                        //String message = jsonObject1.getString("msg");

                        String message = "Sorry! unable to generate token.";
                        showBottomSheetDialog("Token Generation", message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, IssueTokenActivity.this);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0
                ,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(IssueTokenActivity.this).addToRequestQueue(request);
    }


    private void showBottomSheetDialog(String title, String message) {

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.e_stamping_dialog, null);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
     //   FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
      //  BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);


        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        tvMessage.setText(message);

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastText = "";
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }


    public void btnTokenStatus(View view) {
        startActivity(new Intent(IssueTokenActivity.this, TokenListActivity.class));
    }


    private void initializeTTS() {

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("hi_IN"));
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");

                    } else {

                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }


        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.i("TextToSpeech", "On Start");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.i("TextToSpeech", "On Done");
                //  SpeechUtility.getSpeechInput(IssueTokenActivity.this, vitalsActivitySpeechResultLauncher);
            }

            @Override
            public void onError(String utteranceId) {
                Log.i("TextToSpeech", "On Error");
                //SpeechUtility.getSpeechInput(IssueTokenActivity.this, vitalsActivitySpeechResultLauncher);
            }
        });
    }

    private void ConvertTextToSpeech(String text) {


        if (text == null || text.isEmpty()) {
            text = "Content not available";
            //    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "1");


        }
    }
}