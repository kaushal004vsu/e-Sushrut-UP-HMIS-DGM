package com.cdac.uphmis.phr;

import static com.cdac.uphmis.util.AppConstants.BLOOD_DYSTOLIC_ID;
import static com.cdac.uphmis.util.AppConstants.BLOOD_GROUP_ID;
import static com.cdac.uphmis.util.AppConstants.BLOOD_SYSTOLIC_ID;
import static com.cdac.uphmis.util.AppConstants.BMI_ID;
import static com.cdac.uphmis.util.AppConstants.HEIGHT_ID;
import static com.cdac.uphmis.util.AppConstants.O2_ID;
import static com.cdac.uphmis.util.AppConstants.PULSE_RATE_ID;
import static com.cdac.uphmis.util.AppConstants.RESP_RATE_ID;
import static com.cdac.uphmis.util.AppConstants.WEIGHT_ID;
import static java.lang.Float.parseFloat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PHRActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tollbar_tv, save;
    GeometricProgressView progressView;
    ImageView back;

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;
    TextView name1, name2, name3, name4, name5, name6, name7, name8, name9;
    TextView integer_number1, integer_number2, integer_number3, integer_number4, integer_number5, integer_number6, integer_number7, integer_number8;
    TextView measure1, measure2, measure3, measure4, measure5, measure6, measure7, measure8;
    TextView bmi_status;
    TextView bp_range;
    TextView resp_rate;
    TextView spo2;
    ImageView weight_plus, hieght_plus, bmi_plus, resp_rate_plus, o2_plus, pluse_rate_plus, bp_sys_plus, bp_distolic_plus;
    ImageView weight_minus, hieght_minus, bmi_minus, resp_rate_minus, o2_minus, pluse_rate_minus, bp_sys_minus, bp_distolic_minus;
    LinearLayout statics1, statics2, statics3, statics4, statics5, statics6, statics7, statics8, view_image;
    String bmi;
    JSONArray jsonArray;
    Spinner spinner;

    JSONObject jsonObject1, jsonObject2, jsonObject3, jsonObject4, jsonObject5, jsonObject6, jsonObject7, jsonObject8, jsonObject9;
    private ManagingSharedData msd;
//    private ScreeningDetails screeningDetails;

    private void initVar() {

        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        save = findViewById(R.id.save);
        progressView = findViewById(R.id.progress_view);
        bmi_status = findViewById(R.id.bmi_status);
        bp_range = findViewById(R.id.bp_range);
        resp_rate = findViewById(R.id.resp_rate);
        spo2 = findViewById(R.id.spo2);

        imageView1 = findViewById(R.id.imageView1);
        name1 = findViewById(R.id.name1);
        integer_number1 = findViewById(R.id.integer_number1);
        measure1 = findViewById(R.id.measure1);
        weight_plus = findViewById(R.id.plus1);
        weight_minus = findViewById(R.id.minus1);
        statics1 = findViewById(R.id.statics1);

        imageView2 = findViewById(R.id.imageView2);
        name2 = findViewById(R.id.name2);
        integer_number2 = findViewById(R.id.integer_number2);
        measure2 = findViewById(R.id.measure2);
        hieght_plus = findViewById(R.id.plus2);
        hieght_minus = findViewById(R.id.minus2);
        statics2 = findViewById(R.id.statics2);

        imageView3 = findViewById(R.id.imageView3);
        name3 = findViewById(R.id.name3);
        integer_number3 = findViewById(R.id.integer_number3);
        measure3 = findViewById(R.id.measure3);
        bmi_plus = findViewById(R.id.plus3);
        bmi_minus = findViewById(R.id.minus3);
        statics3 = findViewById(R.id.statics3);

        imageView4 = findViewById(R.id.imageView4);
        name4 = findViewById(R.id.name4);
        integer_number4 = findViewById(R.id.integer_number4);
        measure4 = findViewById(R.id.measure4);
        resp_rate_plus = findViewById(R.id.plus4);
        resp_rate_minus = findViewById(R.id.minus4);
        statics4 = findViewById(R.id.statics4);

        imageView5 = findViewById(R.id.imageView5);
        name5 = findViewById(R.id.name5);
        integer_number5 = findViewById(R.id.integer_number5);
        measure5 = findViewById(R.id.measure5);
        o2_plus = findViewById(R.id.plus5);
        o2_minus = findViewById(R.id.minus5);
        statics5 = findViewById(R.id.statics5);

        imageView6 = findViewById(R.id.imageView6);
        name6 = findViewById(R.id.name6);
        integer_number6 = findViewById(R.id.integer_number6);
        measure6 = findViewById(R.id.measure6);
        pluse_rate_plus = findViewById(R.id.plus6);
        pluse_rate_minus = findViewById(R.id.minus6);
        statics6 = findViewById(R.id.statics6);

        imageView7 = findViewById(R.id.imageView7);
        name7 = findViewById(R.id.name7);
        integer_number7 = findViewById(R.id.integer_number7);
        measure7 = findViewById(R.id.measure7);
        bp_sys_plus = findViewById(R.id.plus7);
        bp_sys_minus = findViewById(R.id.minus7);
        statics7 = findViewById(R.id.statics7);

        imageView8 = findViewById(R.id.imageView8);
        name8 = findViewById(R.id.name8);
        integer_number8 = findViewById(R.id.integer_number8);
        measure8 = findViewById(R.id.measure8);
        bp_distolic_plus = findViewById(R.id.plus8);
        bp_distolic_minus = findViewById(R.id.minus8);
        statics8 = findViewById(R.id.statics8);

        spinner = findViewById(R.id.spinner);

        name9 = findViewById(R.id.name9);
        imageView9 = findViewById(R.id.imageView9);
        view_image = findViewById(R.id.view_image);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phractivity);

        initVar();
        tollbar_tv = findViewById(R.id.tollbar_tv);
        tollbar_tv.setText("Personal Health Records");
        setSupportActionBar(toolbar);

        msd = new ManagingSharedData(PHRActivity.this);

       String bloodGroup =  msd.getBloodGroup();
        if(bloodGroup != null && !bloodGroup.trim().isEmpty()){
            spinner.setSelection(Integer.parseInt(bloodGroup));
           // spinner.setEnabled(false);
        }else{
           // spinner.setEnabled(true);
        }
        save.setOnClickListener(v -> saveApi());
        back.setOnClickListener(v -> finish());

        bmi = calculateBmi(integer_number1.getText().toString(), integer_number2.getText().toString());
        bmi_status.setText(bmi);
        bpCalc(Float.parseFloat(integer_number8.getText().toString()), Float.parseFloat(integer_number7.getText().toString()));
        respRateCalc(Float.parseFloat(integer_number4.getText().toString()));
        o2Level(Float.parseFloat(integer_number5.getText().toString()));

        statics1.setOnClickListener(v -> toActivity(WEIGHT_ID, name1.getText().toString(), v));
        statics2.setOnClickListener(v -> toActivity(HEIGHT_ID, name2.getText().toString(), v));
        statics3.setOnClickListener(v -> toActivity(BMI_ID, name3.getText().toString(), v));
        statics4.setOnClickListener(v -> toActivity(PULSE_RATE_ID, name4.getText().toString(), v));
        statics5.setOnClickListener(v -> toActivity(RESP_RATE_ID, name5.getText().toString(), v));
        statics6.setOnClickListener(v -> toActivity(O2_ID, name6.getText().toString(), v));
        statics7.setOnClickListener(v -> toActivity(BLOOD_SYSTOLIC_ID, name7.getText().toString(), v));
        statics8.setOnClickListener(v -> toActivity(BLOOD_DYSTOLIC_ID, name8.getText().toString(), v));
        view_image.setOnClickListener(v -> {
            v.playSoundEffect(SoundEffectConstants.CLICK);
//            Intent intent = new Intent(getApplicationContext(), ImageViewActivity.class);
            Intent intent = new Intent(getApplicationContext(), PhrViewDocNewActivity.class);
            intent.putExtra("name", name9.getText().toString());

            startActivity(intent);
        });

        weight_plus.setOnClickListener(v -> {
            //Weight
            if (Double.parseDouble(integer_number1.getText().toString().trim())<250) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number1.setText(String.valueOf(Double.parseDouble(integer_number1.getText().toString().trim()) + 1));
                bmi = calculateBmi(integer_number1.getText().toString(), integer_number2.getText().toString());
                bmi_status.setText(bmi);
                createJsonArray(WEIGHT_ID, name1.getText().toString(), integer_number1.getText().toString(), measure1.getText().toString());
            }

        });
        hieght_plus.setOnClickListener(v -> {
            //Height
            if (Double.parseDouble(integer_number2.getText().toString().trim())<250) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number2.setText(String.valueOf(Double.parseDouble(integer_number2.getText().toString().trim()) + 1));
                bmi = calculateBmi(integer_number1.getText().toString(), integer_number2.getText().toString());
                bmi_status.setText(bmi);
                createJsonArray(HEIGHT_ID, name2.getText().toString(), integer_number2.getText().toString(), measure2.getText().toString());
            }
        });
        bmi_plus.setOnClickListener(v -> {
            //BMI
            v.playSoundEffect(SoundEffectConstants.CLICK);
            integer_number3.setText(String.valueOf(Double.parseDouble(integer_number3.getText().toString().trim()) + 1));
            createJsonArray(BMI_ID, name3.getText().toString(), integer_number3.getText().toString(), measure3.getText().toString());
        });
        resp_rate_plus.setOnClickListener(v -> {
            ////Respiration Rate-
            if (Double.parseDouble(integer_number4.getText().toString().trim())<99){
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number4.setText(String.valueOf(Double.parseDouble(integer_number4.getText().toString().trim()) + 1));
                createJsonArray(RESP_RATE_ID, name4.getText().toString(), integer_number4.getText().toString(), measure4.getText().toString());
                respRateCalc(Float.parseFloat(integer_number4.getText().toString()));
            }


        });
        o2_plus.setOnClickListener(v -> {
            //o2
            if (Double.parseDouble(integer_number5.getText().toString().trim())<100) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number5.setText(String.valueOf(Double.parseDouble(integer_number5.getText().toString().trim()) + 1));
                createJsonArray(O2_ID, name5.getText().toString(), integer_number5.getText().toString(), measure5.getText().toString());
                o2Level(Float.parseFloat(integer_number5.getText().toString()));
            }else {
                Toast.makeText(this, "O2 cannot be greater then 100.", Toast.LENGTH_SHORT).show();
            }

        });
        pluse_rate_plus.setOnClickListener(v -> {
            //pulse rate
            v.playSoundEffect(SoundEffectConstants.CLICK);
            integer_number6.setText(String.valueOf(Double.parseDouble(integer_number6.getText().toString().trim()) + 1));
            createJsonArray(PULSE_RATE_ID, name6.getText().toString(), integer_number6.getText().toString(), measure6.getText().toString());
        });
        bp_sys_plus.setOnClickListener(v -> {
            //Blood Pressure systolic
            v.playSoundEffect(SoundEffectConstants.CLICK);
            integer_number7.setText(String.valueOf(Double.parseDouble(integer_number7.getText().toString().trim()) + 1));
            createJsonArray(BLOOD_SYSTOLIC_ID, name7.getText().toString(), integer_number7.getText().toString(), measure7.getText().toString());
            bpCalc(Float.parseFloat(integer_number8.getText().toString()), Float.parseFloat(integer_number7.getText().toString()));
        });
        bp_distolic_plus.setOnClickListener(v -> {
            //Blood Pressure Diastolic
            v.playSoundEffect(SoundEffectConstants.CLICK);
            integer_number8.setText(String.valueOf(Double.parseDouble(integer_number8.getText().toString().trim()) + 1));
            createJsonArray(BLOOD_DYSTOLIC_ID, name8.getText().toString(), integer_number8.getText().toString(), measure8.getText().toString());
            bpCalc(Float.parseFloat(integer_number8.getText().toString()), Float.parseFloat(integer_number7.getText().toString()));
        });
        weight_minus.setOnClickListener(v -> {
            //Weight
            if (Double.parseDouble(integer_number1.getText().toString().trim())>1) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number1.setText(String.valueOf(Double.parseDouble(integer_number1.getText().toString().trim()) - 1));
                bmi = calculateBmi(integer_number1.getText().toString(), integer_number2.getText().toString());
                bmi_status.setText(bmi);
                createJsonArray(WEIGHT_ID, name1.getText().toString(), integer_number1.getText().toString(), measure1.getText().toString());

            }

        });
        hieght_minus.setOnClickListener(v -> {
            //Hieght
            if (Double.parseDouble(integer_number2.getText().toString().trim())>30) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number2.setText(String.valueOf(Double.parseDouble(integer_number2.getText().toString().trim()) - 1));
                bmi = calculateBmi(integer_number1.getText().toString(), integer_number2.getText().toString());
                bmi_status.setText(bmi);
                createJsonArray(HEIGHT_ID, name2.getText().toString(), integer_number2.getText().toString(), measure2.getText().toString());

            }
        });
        bmi_minus.setOnClickListener(v -> {
            //BMI
            v.playSoundEffect(SoundEffectConstants.CLICK);
            integer_number3.setText(String.valueOf(Double.parseDouble(integer_number3.getText().toString().trim()) - 1));
            createJsonArray(BMI_ID, name3.getText().toString(), integer_number3.getText().toString(), measure3.getText().toString());
        });
        resp_rate_minus.setOnClickListener(v -> {
            //Respartion Rate
            if (Double.parseDouble(integer_number4.getText().toString().trim())>1) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number4.setText(String.valueOf(Double.parseDouble(integer_number4.getText().toString().trim()) - 1));
                createJsonArray(RESP_RATE_ID, name4.getText().toString(), integer_number4.getText().toString(), measure4.getText().toString());
                respRateCalc(Float.parseFloat(integer_number4.getText().toString()));
            }else {
                resp_rate.setText("Respiratory Rate cannot be 0.");
            }
        });
        o2_minus.setOnClickListener(v -> {
            //o2
            if (Double.parseDouble(integer_number5.getText().toString().trim())>1) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number5.setText(String.valueOf(Double.parseDouble(integer_number5.getText().toString().trim()) - 1));
                createJsonArray(O2_ID, name5.getText().toString(), integer_number5.getText().toString(), measure5.getText().toString());
                o2Level(Float.parseFloat(integer_number5.getText().toString()));
            }
        });
        pluse_rate_minus.setOnClickListener(v -> {
            //pulse rate
            if (Double.parseDouble(integer_number6.getText().toString().trim())>1) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number6.setText(String.valueOf(Double.parseDouble(integer_number6.getText().toString().trim()) - 1));
                createJsonArray(PULSE_RATE_ID, name6.getText().toString(), integer_number6.getText().toString(), measure6.getText().toString());
            }
        });
        bp_sys_minus.setOnClickListener(v -> {
            //Blood Pressure Systolic
            if (Double.parseDouble(integer_number7.getText().toString().trim())>1) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number7.setText(String.valueOf(Double.parseDouble(integer_number7.getText().toString().trim()) - 1));
                createJsonArray(BLOOD_SYSTOLIC_ID, name7.getText().toString(), integer_number7.getText().toString(), measure7.getText().toString());
                bpCalc(Float.parseFloat(integer_number8.getText().toString()), Float.parseFloat(integer_number7.getText().toString()));
            }
        });
        bp_distolic_minus.setOnClickListener(v -> {
            //Blood Pressure Diastolic
            if (Double.parseDouble(integer_number8.getText().toString().trim())>1) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                integer_number8.setText(String.valueOf(Double.parseDouble(integer_number8.getText().toString().trim()) - 1));
                createJsonArray(BLOOD_DYSTOLIC_ID, name8.getText().toString(), integer_number8.getText().toString(), measure8.getText().toString());
                bpCalc(Float.parseFloat(integer_number8.getText().toString()), Float.parseFloat(integer_number7.getText().toString()));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select blood group")) {
                } else {
                    createJsonArray(BLOOD_GROUP_ID, name9.getText().toString(), spinner.getSelectedItem().toString(), "");
                    msd.setBloodGroup(String.valueOf(spinner.getSelectedItemPosition()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.view_imag_ll).setOnClickListener(v -> {
            Intent intent = new Intent(PHRActivity.this, PhrUpldNewActivity.class);
            intent.putExtra("crno", msd.getPatientDetails().getCrno());
            startActivity(intent);

        });
    }

    private void createJsonArray(String id, String name, String value, String unit) {
        String crno = msd.getPatientDetails().getCrno();
        try {
            if (id.equals(WEIGHT_ID)) {
                //Weight
                if (jsonObject1 == null)
                    jsonObject1 = new JSONObject();
                jsonObject1.put("crno", crno);
                jsonObject1.put("recordDate", "");
                jsonObject1.put("vitalName", name);
                jsonObject1.put("vitalId", id);
                jsonObject1.put("vitalValue", value);
                jsonObject1.put("vitalUnit", unit);
                jsonObject1.put("docName", "");
                jsonObject1.put("imageData", "");

            } else if (id.equals(HEIGHT_ID)) {
                //Height
                if (jsonObject2 == null)
                    jsonObject2 = new JSONObject();
                jsonObject2.put("crno", crno);
                jsonObject2.put("recordDate", "");
                jsonObject2.put("vitalName", name);
                jsonObject2.put("vitalId", id);
                jsonObject2.put("vitalValue", value);
                jsonObject2.put("vitalUnit", unit);
                jsonObject2.put("docName", "");
                jsonObject2.put("imageData", "");

            } else if (id.equals(RESP_RATE_ID)) {
                //Respiration Rate
                jsonObject3 = new JSONObject();
                jsonObject3.put("crno", crno);
                jsonObject3.put("recordDate", "");
                jsonObject3.put("vitalName", name);
                jsonObject3.put("vitalId", id);
                jsonObject3.put("vitalValue", value);
                jsonObject3.put("vitalUnit", unit);
                jsonObject3.put("docName", "");
                jsonObject3.put("imageData", "");

            } else if (id.equals(PULSE_RATE_ID)) {
                //Heart Rate
                jsonObject4 = new JSONObject();
                jsonObject4.put("crno", crno);
                jsonObject4.put("recordDate", "");
                jsonObject4.put("vitalName", name);
                jsonObject4.put("vitalId", id);
                jsonObject4.put("vitalValue", value);
                jsonObject4.put("vitalUnit", unit);
                jsonObject4.put("docName", "");
                jsonObject4.put("imageData", "");

            } else if (id.equals(BLOOD_SYSTOLIC_ID)) {
                //Blood Pressure Systolic
                jsonObject5 = new JSONObject();
                jsonObject5.put("crno", crno);
                jsonObject5.put("recordDate", "");
                jsonObject5.put("vitalName", name);
                jsonObject5.put("vitalId", id);
                jsonObject5.put("vitalValue", value);
                jsonObject5.put("vitalUnit", unit);
                jsonObject5.put("docName", "");
                jsonObject5.put("imageData", "");
            } else if (id.equals(BLOOD_DYSTOLIC_ID)) {
                //Blood Pressure Diastolic
                jsonObject6 = new JSONObject();
                jsonObject6.put("crno", crno);
                jsonObject6.put("recordDate", "");
                jsonObject6.put("vitalName", name);
                jsonObject6.put("vitalId", id);
                jsonObject6.put("vitalValue", value);
                jsonObject6.put("vitalUnit", unit);
                jsonObject6.put("docName", "");
                jsonObject6.put("imageData", "");
            } else if (id.equals(O2_ID)) {
                //O2
                jsonObject7 = new JSONObject();
                jsonObject7.put("crno", crno);
                jsonObject7.put("recordDate", "");
                jsonObject7.put("vitalName", name);
                jsonObject7.put("vitalId", id);
                jsonObject7.put("vitalValue", value);
                jsonObject7.put("vitalUnit", unit);
                jsonObject7.put("docName", "");
                jsonObject7.put("imageData", "");

            } else if (id.equals(BMI_ID)) {
                //Body Mass Index
                jsonObject8 = new JSONObject();
                jsonObject8.put("crno", crno);
                jsonObject8.put("recordDate", "");
                jsonObject8.put("vitalName", name);
                jsonObject8.put("vitalId", id);
                jsonObject8.put("vitalValue", value);
                jsonObject8.put("vitalUnit", unit);
                jsonObject8.put("docName", "");
                jsonObject8.put("imageData", "");
            } else if (id.equals(BLOOD_GROUP_ID)) {
                //Blood Group
                jsonObject9 = new JSONObject();
                jsonObject9.put("crno", crno);
                jsonObject9.put("recordDate", "");
                jsonObject9.put("vitalName", name);
                jsonObject9.put("vitalId", id);
                jsonObject9.put("vitalValue", value);
                jsonObject9.put("vitalUnit", "");
                jsonObject9.put("docName", "");
                jsonObject9.put("imageData", "");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveApi() {
        jsonArray = new JSONArray();
        String url = ServiceUrl.savPhrUrl;
        try {
            if (jsonObject1 != null) {
                jsonArray.put(jsonObject1);
            }
            if (jsonObject2 != null) {
                jsonArray.put(jsonObject2);
            }
            if (jsonObject3 != null) {
                jsonArray.put(jsonObject3);
            }
            if (jsonObject4 != null) {
                jsonArray.put(jsonObject4);
            }
            if (jsonObject5 != null) {
                jsonArray.put(jsonObject5);
            }
            if (jsonObject6 != null) {
                jsonArray.put(jsonObject6);
            }
            if (jsonObject7 != null) {
                jsonArray.put(jsonObject7);
            }
            if (jsonObject8 != null) {
                jsonArray.put(jsonObject8);
            }
            if (jsonObject9 != null) {
                jsonArray.put(jsonObject9);
            }
            if (jsonArray.length() == 0) {
                showAlertDialogButtonClicked("Make some changes before saving data !", "2");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("json_array", String.valueOf(jsonArray));
        progressView.setVisibility(View.VISIBLE);
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                response -> {
                    progressView.setVisibility(View.GONE);
//                    Log.d("json_responce", String.valueOf(response));
                    String s = String.valueOf(response);

                    JsonElement root = new JsonParser().parse(s);
                    JsonArray jsonArray = root.getAsJsonArray();
                    JsonObject jsonObject1 = jsonArray.get(0).getAsJsonObject();
                    String status = jsonObject1.get("status").toString();
                    String msg = jsonObject1.get("msg").toString();
                    status = status.replace("\"", "");
                    msg = msg.replace("\"", "");
                    if (status.equals("1")) {
                        showAlertDialogButtonClicked(msg, "1");
                        for (JsonElement i : jsonArray) {
                            jsonArray.remove(i);
                        }
                    } else {
                        showAlertDialogButtonClicked(msg, "0");
                        for (JsonElement i : jsonArray) {
                            jsonArray.remove(i);
                        }
                    }


                }, volleyError -> {
            ;
            VolleyLog.e("Error: ", volleyError.getMessage());
            progressView.setVisibility(View.GONE);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                String responseString;
                JSONArray array = new JSONArray();
                if (response != null) {
                    try {
                        responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        JSONObject obj = new JSONObject(responseString);
                        (array).put(obj);
                    } catch (Exception ex) {
                    }
                }
                //return array;
                return Response.success(array, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request_json.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request_json);
    }

    public void toActivity(String vitalId, String name, View v) {
        v.playSoundEffect(SoundEffectConstants.CLICK);
        Intent intent = new Intent(getApplicationContext(), LineChartActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("vital_id", vitalId);
        startActivity(intent);


    }

    public void showAlertDialogButtonClicked(String msg, String mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setCancelable(false);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_message_dialog, null);
        TextView success_text = customLayout.findViewById(R.id.success_text);
        success_text.setText(msg);
        if (mode.equals("2")) {
            success_text.setTextColor(getResources().getColor(R.color.red));
        }
        builder.setView(customLayout);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            if (mode.equals("2")) {
            } else {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String calculateBmi(String weight, String height) {
        if (!weight.isEmpty() && !height.isEmpty()) {
            float temp = parseFloat(height) / 100;
            float bmi = parseFloat(weight) / (temp * temp);
            integer_number3.setText(String.format("%.2f", bmi));
            createJsonArray(BMI_ID, name3.getText().toString(), integer_number3.getText().toString(), measure3.getText().toString());
            if (bmi > 30) {
                return "Obese";
            } else if (bmi < 18.5) {
                return "Underweight";
            } else if (bmi >= 18.5 && bmi <= 24.9) {
                return "Normal";
            } else if (bmi >= 25.0 && bmi <= 29.9) {
                return "Overweight";
            } else {
                return "";
            }
        }
        return "";
    }

    public void bpCalc(float temp, float temp1) {
        if (temp != 0 && temp1 != 0) {
            bp_range.setVisibility(View.VISIBLE);
            if ((temp <= 60) && (temp1 <= 90)) {
                bp_range.setText("Low BP");
            }
            if ((temp <= 60) && (temp1 > 90)) {
                bp_range.setText("Ideal BP");
            } else if ((temp > 60 && temp <= 80) && (temp1 > 90 && temp1 <= 120)) {
                bp_range.setText("Ideal BP");
            } else if ((temp > 60 && temp <= 80) && (temp1 > 120 && temp1 <= 140)) {
                bp_range.setText("Hypertension Stage 1");
            } else if ((temp > 80 && temp <= 90) && (temp1 > 120 && temp1 <= 140)) {
                bp_range.setText("Hypertension Stage 1");
            } else if ((temp > 80 && temp <= 90) && temp1 > 140) {
                bp_range.setText("Hypertension Stage 2");
            } else if (temp > 90 && temp1 > 140) {
                bp_range.setText("Hypertension Stage 2");
            } else if (temp1 == 0) {
                bp_range.setText("Systolic cannot be 0.");
            } else {
                bp_range.setVisibility(View.GONE);
                bp_range.setText("Normal");
            }
        } else {
            bp_range.setText("Systolic cannot be 0.");
        }
    }

    public void respRateCalc(float temp) {
        if (temp == 0) {
            resp_rate.setText("Respiratory Rate cannot be 0.");
        } else if (temp > 0 && temp < 9) {
            resp_rate.setText("Low RR");
        } else if (temp > 20) {
            resp_rate.setText("High RR");
        } else {
            resp_rate.setText("Normal");
        }
    }
    public void o2Level(float temp) {
        if (temp == 0) {
            spo2.setText("O2 cannot be 0.");
        } else if (temp > 95 && temp < 100) {
            spo2.setText("Normal");
        } else if (temp > 90 && temp <95) {
            spo2.setText("Concerning");
        } else if (temp<=90){
            spo2.setText("Critical");
        }
    }

}