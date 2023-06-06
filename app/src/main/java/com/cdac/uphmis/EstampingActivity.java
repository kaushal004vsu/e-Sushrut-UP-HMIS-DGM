package com.cdac.uphmis;


import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import com.cdac.uphmis.util.SessionServicecall;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstampingActivity extends AppCompatActivity {
    private ManagingSharedData msd;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    int isFlashOn = 0;

    ImageButton btnFlash;
    GeometricProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estamping);
        
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
                Log.i("lastText", "barcodeResult: "+lastText);
           //     Toast.makeText(EstampingActivity.this, ""+result.getText(), Toast.LENGTH_SHORT).show();
                return;
            }


            lastText = result.getText();


            //Toast.makeText(EstampingActivity.this, lastText, Toast.LENGTH_SHORT).show();
            Log.i("TAG", "barcodeResult: " + lastText);
            beepManager.playBeepSoundAndVibrate();

                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(lastText);

                    String hospCode="",deptcode="",unitCode="",token="",lat="",lon="";
                    if (obj.has("hospcode")) {
                         hospCode = obj.getString("hospcode");
                         deptcode = obj.getString("deptcode");
                         unitCode = obj.getString("unitcode");
                         token = obj.optString("token");
                         lat = obj.optString("lat");
                         lon = obj.optString("lon");
                    }else if (obj.has("key"))
                    {

                        String key=obj.optString("key");
                        String[] ar=key.split("\\$");
                         hospCode=ar[0];
                         deptcode=ar[1];
                         unitCode=ar[2];
                    }else
                    {
                        Toast.makeText(EstampingActivity.this, "Invalid QR Code"+lastText, Toast.LENGTH_LONG).show();
                        return;
                    }

                    String deptUnitCode = unitCode;
                    if (unitCode.length() < 5)
                        deptUnitCode = deptcode + unitCode;

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("deptcode", deptcode);
                    jsonObject.put("deptunitcode", deptUnitCode);
                    jsonObject.put("hospitalcode", hospCode);
                    jsonObject.put("patcrno", msd.getPatientDetails().getCrno());
                    jsonObject.put("latitude", lat);
                    jsonObject.put("longitude", lon);
                    jsonObject.put("token", token);
                    jsonObject.put("iskiosk", "0");



                    callStampingService(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(EstampingActivity.this, lastText, Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(EstampingActivity.this, "Invalid QR Code "+lastText, Toast.LENGTH_LONG).show();
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






    private void callStampingService(JSONObject jsonObject) {
        progressView.setVisibility(View.VISIBLE);
        Log.i("jsonData", "callStampingService: " + jsonObject);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.qrStampingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    SessionServicecall sessionServicecall = new SessionServicecall(EstampingActivity.this);
                    sessionServicecall.saveSession(msd.getPatientDetails().getCrno(), msd.getPatientDetails().getMobileNo(),msd.getPatientDetails().getHospitalCode(), "E Stamping", "", "", "", "");
                }catch(Exception ex){}


                progressView.setVisibility(View.GONE);
                Log.i("eStampingResponse", "onResponse: " + response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("details");
                        String message = jsonArray.getJSONObject(0).getString("MSG");


                        showBottomSheetDialog(message);

                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        String message = jsonObject1.getString("msg");


                        showBottomSheetDialog(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, EstampingActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //Map<String, String>
                HashMap<String, String> data = new HashMap<>();
                data.put("jsonData", jsonObject.toString());
                return data;
            }


        };
        MySingleton.getInstance(EstampingActivity.this).addToRequestQueue(request);
    }


    private void showBottomSheetDialog(String message) {

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.e_stamping_dialog, null);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        //FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
        //BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);


        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        tvMessage.setText(message);

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastText="";
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }





}