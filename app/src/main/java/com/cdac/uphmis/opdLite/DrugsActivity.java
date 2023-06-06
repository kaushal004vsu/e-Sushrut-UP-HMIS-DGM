package com.cdac.uphmis.opdLite;import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;import android.app.Activity;import android.app.Dialog;import android.content.Intent;import android.graphics.Color;import android.graphics.drawable.ColorDrawable;import android.os.Bundle;import android.speech.RecognizerIntent;import android.util.Log;import android.view.Gravity;import android.view.View;import android.view.Window;import android.view.WindowManager;import android.widget.ArrayAdapter;import android.widget.AutoCompleteTextView;import android.widget.Button;import android.widget.EditText;import android.widget.LinearLayout;import android.widget.MultiAutoCompleteTextView;import android.widget.ProgressBar;import android.widget.RadioButton;import android.widget.TextView;import android.widget.Toast;import androidx.activity.result.ActivityResult;import androidx.activity.result.ActivityResultCallback;import androidx.activity.result.ActivityResultLauncher;import androidx.activity.result.contract.ActivityResultContracts;import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.LinearLayoutManager;import androidx.recyclerview.widget.RecyclerView;import com.android.volley.DefaultRetryPolicy;import com.android.volley.Request;import com.android.volley.toolbox.StringRequest;import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;import com.cdac.uphmis.R;import com.cdac.uphmis.opdLite.adaper.DrugJsonAdapter;import com.cdac.uphmis.opdLite.adaper.DrugsAdapter;import com.cdac.uphmis.opdLite.model.DrugJsonArray;import com.cdac.uphmis.opdLite.model.DrugsDetails;import com.cdac.uphmis.util.MySingleton;import com.cdac.uphmis.util.ServiceUrl;import com.google.android.material.floatingactionbutton.FloatingActionButton;import com.google.gson.Gson;import org.json.JSONArray;import org.json.JSONObject;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Arrays;import java.util.Calendar;import java.util.Date;import java.util.List;import java.util.Locale;public class DrugsActivity extends AppCompatActivity {    private AutoCompleteTextView autoCompleteTextView;    private ArrayList<DrugsDetails> drugsDetailsArrayList;    private ArrayList<DrugJsonArray> drugJsonArrayArrayList;    private DrugJsonAdapter drugJsonAdapter;    ProgressBar progressBar;    LinearLayout llLayout;    ActivityResultLauncher<Intent> drugsActivitySpeechResultLauncher;    ActivityResultLauncher<Intent> drugsDialogSpeechResultLauncher;    Dialog addDrugsDialog;    Button btnAdd;    MultiAutoCompleteTextView auto_compplete_special_condition;    EditText tvMorning, tvAfternoon, tvEvening, tvNight, tvDays;    RadioButton radioButtonDays, radioButtonWeeks, radioButtonMonths;    List<String> allowedStrings;    DoctorReqListDetails patientDetails;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_drugs);                autoCompleteTextView = findViewById(R.id.auto_complete_drugs);        progressBar = findViewById(R.id.pb);        llLayout = findViewById(R.id.ll_layout);        drugJsonArrayArrayList = new ArrayList<>();        drugsDetailsArrayList = new ArrayList<>();        getDrugs();        Intent intent = getIntent();        if (intent != null) {             drugJsonArrayArrayList = (ArrayList<DrugJsonArray>) intent.getSerializableExtra("drugJsonArrayArrayList");            patientDetails = (DoctorReqListDetails) getIntent().getSerializableExtra("patientDetails");            if (drugJsonArrayArrayList==null) {                 drugJsonArrayArrayList = new ArrayList<>();             }        }        setUpRecyclerView();  allowedStrings = Arrays.asList                (                        "zero","one","two","three","four","five","six","seven",                        "eight","nine","ten","eleven","twelve","thirteen","fourteen",                        "fifteen","sixteen","seventeen","eighteen","nineteen","twenty",                        "thirty","forty","fifty","sixty","seventy","eighty","ninety",                        "hundred","thousand","million","billion","trillion"                );        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {            DrugsDetails selectedItem = (DrugsDetails) parent.getItemAtPosition(position);            autoCompleteTextView.dismissDropDown();            Log.i("student", "onItemClick: " + selectedItem.getLabel());            autoCompleteTextView.setText("");            addDrugDialog(selectedItem);        });        drugsActivitySpeechResultLauncher = registerForActivityResult(                new ActivityResultContracts.StartActivityForResult(),                result -> {                    if (result.getResultCode() == Activity.RESULT_OK) {                        // There are no request codes                        Intent data = result.getData();                        // doSomeOperations();                        ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);                        String word = result1.get(0).toLowerCase();                        parseData(word);                    }                });        drugsDialogSpeechResultLauncher = registerForActivityResult(                new ActivityResultContracts.StartActivityForResult(),                new ActivityResultCallback<ActivityResult>() {                    @Override                    public void onActivityResult(ActivityResult result) {                        if (result.getResultCode() == Activity.RESULT_OK) {                            // There are no request codes                            Intent data = result.getData();                            ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);                            String word = result1.get(0).toLowerCase();                            parseData(word);                        }                    }                });    }    private void parseData(String word) {        Log.i("TAG", "parseData: " + word);        for (String w:word.split(" "))        {            if (allowedStrings.contains(w))            {                word=word.replaceAll(w,convertNumberToWords(w));            }        }        //for searching drug        if (addDrugsDialog == null || !addDrugsDialog.isShowing()) {            if (word.contains("search drugs")) {                autoCompleteTextView.requestFocus();                autoCompleteTextView.setText("");                autoCompleteTextView.append(word.replaceAll("search drugs", ""));            } else if (word.contains("add drugs")) {                autoCompleteTextView.setText("");                autoCompleteTextView.requestFocus();                autoCompleteTextView.setText("");                autoCompleteTextView.append(word.replaceAll("add drugs", ""));            } else if (autoCompleteTextView.hasFocus()) {                autoCompleteTextView.requestFocus();                autoCompleteTextView.setText("");                autoCompleteTextView.append(word);            } else {                autoCompleteTextView.requestFocus();                autoCompleteTextView.setText("");                autoCompleteTextView.append(word);            }            //for handling dialog        } else {            if (word.contains("add") || word.contains("done")||word.contains("ad")) {                btnAdd.performClick();            } else if (word.contains("cancel")) {                addDrugsDialog.dismiss();            } else if (word.contains("dismiss")) {                addDrugsDialog.dismiss();            } else if (word.contains("morning")) {                tvMorning.setText(word.replaceAll("[^0-9]", ""));            } else if (word.contains("afternoon")) {                tvAfternoon.setText(word.replaceAll("[^0-9]", ""));            } else if (word.contains("evening")) {                tvEvening.setText(word.replaceAll("[^0-9]", ""));            } else if (word.contains("night")) {                tvNight.setText(word.replaceAll("[^0-9]", ""));            } else if (word.replaceAll("[^0-9]", "").length() == 4) {                word=word.replaceAll("[^0-9]", "");                tvMorning.setText(String.valueOf(word.charAt(0)));                tvAfternoon.setText("" + word.charAt(1));                tvEvening.setText("" + word.charAt(2));                tvNight.setText("" + word.charAt(3));            }            else {                boolean radioSelectCondition = word.contains("day") || word.contains("days") || word.contains("week") || word.contains("weeks") || word.contains("month") || word.contains("months");                if (word.replaceAll("[^0-9]", "").length() == 5) {                    String tempWord=word.replaceAll("[^0-9]", "");                    tvMorning.setText(String.valueOf(tempWord.charAt(0)));                    tvAfternoon.setText(String.valueOf( tempWord.charAt(1)));                    tvEvening.setText(String.valueOf( tempWord.charAt(2)));                    tvNight.setText(String.valueOf( tempWord.charAt(3)));                    tvDays.setText(String.valueOf(tempWord.charAt(4)));                    if (radioSelectCondition) {                        if (word.contains("days") || word.contains("days")) {                            radioButtonDays.setChecked(true);                        } else if (word.contains("week") || word.contains("weeks")) {                            radioButtonWeeks.setChecked(true);                        } else if (word.contains("month") || word.contains("months")) {                            radioButtonMonths.setChecked(true);                        }                    }                }               else if (radioSelectCondition) {                    tvDays.setText(word.replaceAll("[^0-9]", ""));                    if (word.contains("days") || word.contains("days")) {                        radioButtonDays.setChecked(true);                    } else if (word.contains("week") || word.contains("weeks")) {                        radioButtonWeeks.setChecked(true);                    } else if (word.contains("month") || word.contains("months")) {                        radioButtonMonths.setChecked(true);                    }                }            }            if(word.contains("instructions")||word.contains("instruction"))            {                auto_compplete_special_condition.requestFocus();            }            if(auto_compplete_special_condition.hasFocus())            {                auto_compplete_special_condition.append(word.replaceAll("instructions","").replaceAll("instruction","").replaceAll("special","")+" ");            }        }    }    private String convertNumberToWords(String input)    {        boolean isValidInput = true;        long result = 0;        long finalResult = 0;//        String input="One hundred two thousand and thirty four";        if(input != null && input.length()> 0)        {            input = input.replaceAll("-", " ");            input = input.toLowerCase().replaceAll(" and", " ");            String[] splittedParts = input.trim().split("\\s+");            for(String str : splittedParts)            {                if(!allowedStrings.contains(str))                {                    isValidInput = false;                    System.out.println("Invalid word found : "+str);                    break;                }            }            if(isValidInput)            {                for(String str : splittedParts)                {                    if(str.equalsIgnoreCase("zero")) {                        result += 0;                    }                    else if(str.equalsIgnoreCase("one")) {                        result += 1;                    }                    else if(str.equalsIgnoreCase("two")) {                        result += 2;                    }                    else if(str.equalsIgnoreCase("three")) {                        result += 3;                    }                    else if(str.equalsIgnoreCase("four")) {                        result += 4;                    }                    else if(str.equalsIgnoreCase("five")) {                        result += 5;                    }                    else if(str.equalsIgnoreCase("six")) {                        result += 6;                    }                    else if(str.equalsIgnoreCase("seven")) {                        result += 7;                    }                    else if(str.equalsIgnoreCase("eight")) {                        result += 8;                    }                    else if(str.equalsIgnoreCase("nine")) {                        result += 9;                    }                    else if(str.equalsIgnoreCase("ten")) {                        result += 10;                    }                    else if(str.equalsIgnoreCase("eleven")) {                        result += 11;                    }                    else if(str.equalsIgnoreCase("twelve")) {                        result += 12;                    }                    else if(str.equalsIgnoreCase("thirteen")) {                        result += 13;                    }                    else if(str.equalsIgnoreCase("fourteen")) {                        result += 14;                    }                    else if(str.equalsIgnoreCase("fifteen")) {                        result += 15;                    }                    else if(str.equalsIgnoreCase("sixteen")) {                        result += 16;                    }                    else if(str.equalsIgnoreCase("seventeen")) {                        result += 17;                    }                    else if(str.equalsIgnoreCase("eighteen")) {                        result += 18;                    }                    else if(str.equalsIgnoreCase("nineteen")) {                        result += 19;                    }                    else if(str.equalsIgnoreCase("twenty")) {                        result += 20;                    }                    else if(str.equalsIgnoreCase("thirty")) {                        result += 30;                    }                    else if(str.equalsIgnoreCase("forty")) {                        result += 40;                    }                    else if(str.equalsIgnoreCase("fifty")) {                        result += 50;                    }                    else if(str.equalsIgnoreCase("sixty")) {                        result += 60;                    }                    else if(str.equalsIgnoreCase("seventy")) {                        result += 70;                    }                    else if(str.equalsIgnoreCase("eighty")) {                        result += 80;                    }                    else if(str.equalsIgnoreCase("ninety")) {                        result += 90;                    }                    else if(str.equalsIgnoreCase("hundred")) {                        result *= 100;                    }                    else if(str.equalsIgnoreCase("thousand")) {                        result *= 1000;                        finalResult += result;                        result=0;                    }                    else if(str.equalsIgnoreCase("million")) {                        result *= 1000000;                        finalResult += result;                        result=0;                    }                    else if(str.equalsIgnoreCase("billion")) {                        result *= 1000000000;                        finalResult += result;                        result=0;                    }                    else if(str.equalsIgnoreCase("trillion")) {                        result *= 1000000000000L;                        finalResult += result;                        result=0;                    }                }                finalResult += result;                result=0;                System.out.println(finalResult);                return String.valueOf(finalResult);            }        }        return "";    }    private void setUpRecyclerView() {        RecyclerView rvDrugs = findViewById(R.id.rv_drugs);        rvDrugs.setHasFixedSize(true);        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);        drugJsonAdapter = null;        drugJsonAdapter = new DrugJsonAdapter(this, drugJsonArrayArrayList,patientDetails.getEpisodeCode(),patientDetails.getPatVisitNo());        rvDrugs.setLayoutManager(layoutManager);        rvDrugs.setAdapter(drugJsonAdapter);    }    private void addDrugDialog(DrugsDetails drugsDetails) {        addDrugsDialog = new Dialog(this);        addDrugsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);        addDrugsDialog.setContentView(R.layout.add_drugs_dialog);        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();        lp.copyFrom(addDrugsDialog.getWindow().getAttributes());        lp.width = WindowManager.LayoutParams.MATCH_PARENT;        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;        lp.gravity = Gravity.CENTER;        addDrugsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));        addDrugsDialog.getWindow().setAttributes(lp);        addDrugsDialog.setCancelable(true);        TextView tvDrugName = addDrugsDialog.findViewById(R.id.tv_drug_name);        TextView tvLetterAvatar = addDrugsDialog.findViewById(R.id.tv_letter_avatar);        tvLetterAvatar.setText(drugsDetails.getTypeShortName());        btnAdd = addDrugsDialog.findViewById(R.id.btn_add);        Button btnClose = addDrugsDialog.findViewById(R.id.btn_close);        tvDrugName.setText(drugsDetails.getLabel());        tvMorning = addDrugsDialog.findViewById(R.id.tv_m);        tvAfternoon = addDrugsDialog.findViewById(R.id.tv_a);        tvEvening = addDrugsDialog.findViewById(R.id.tv_e);        tvNight = addDrugsDialog.findViewById(R.id.tv_n);        tvDays = addDrugsDialog.findViewById(R.id.tv_days);        radioButtonDays = addDrugsDialog.findViewById(R.id.radio_days);        radioButtonWeeks = addDrugsDialog.findViewById(R.id.radio_weeks);        radioButtonMonths = addDrugsDialog.findViewById(R.id.radio_months);        FloatingActionButton addDrugsSpeechInput = addDrugsDialog.findViewById(R.id.fab_add_drugs);        addDrugsSpeechInput.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                getSpeechInput(drugsDialogSpeechResultLauncher);            }        });         auto_compplete_special_condition = addDrugsDialog.findViewById(R.id.auto_compplete_special_condition);        auto_compplete_special_condition.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());        getSpecialInstructions(auto_compplete_special_condition);        auto_compplete_special_condition.setOnItemClickListener((parent, arg1, position, arg3) -> {            //Object item = parent.getItemAtPosition(position);        });        Date c = Calendar.getInstance().getTime();        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());        String formattedDate = df.format(c);        btnClose.setOnClickListener(v -> {            addDrugsDialog.dismiss();        });        btnAdd.setOnClickListener(v -> {            float morning = 0, afternoon = 0, evening = 0, night = 0;            if (!tvMorning.getText().toString().isEmpty()) {                morning = Float.parseFloat(tvMorning.getText().toString());            }            if (!tvAfternoon.getText().toString().isEmpty()) {                afternoon = Float.parseFloat(tvAfternoon.getText().toString());            }            if (!tvEvening.getText().toString().isEmpty()) {                evening = Float.parseFloat(tvEvening.getText().toString());            }            if (!tvNight.getText().toString().isEmpty()) {                night = Float.parseFloat(tvNight.getText().toString());            }            int days = 0;            if (!tvDays.getText().toString().isEmpty()) {                days = Integer.parseInt(tvDays.getText().toString());            }            final float[] qty = {(morning + afternoon + evening + night) * days};            String selectedTimePeriod = "Days";            if (radioButtonDays.isChecked()) {                qty[0] = qty[0];                selectedTimePeriod = "Days";            } else if (radioButtonWeeks.isChecked()) {                qty[0] = qty[0] * 7;                selectedTimePeriod = "Weeks";            } else if (radioButtonMonths.isChecked()) {                qty[0] = qty[0] * 30;                selectedTimePeriod = "Months";            }            int quantity = (int) Math.ceil(qty[0]);            String strMorning = String.valueOf(morning).replaceFirst("\\.0+$", "");            String strAfternoon = String.valueOf(afternoon).replaceFirst("\\.0+$", "");            String strEvening = String.valueOf(evening).replaceFirst("\\.0+$", "");            String strNight = String.valueOf(night).replaceFirst("\\.0+$", "");            drugJsonArrayArrayList.add(new DrugJsonArray("0", drugsDetails.getLabel(), drugsDetails.getValue(), "--", "0", "" + strMorning + " - " + strAfternoon + " - " + strEvening + " - " + strNight + " X " + days + " " + selectedTimePeriod, "0", formattedDate, String.valueOf(days), String.valueOf(quantity), auto_compplete_special_condition.getText().toString(), drugsDetails,strMorning,strAfternoon,strEvening,strNight,patientDetails.getEpisodeCode(),patientDetails.getEpisodeVisitNo()));            drugJsonAdapter.notifyDataSetChanged();            addDrugsDialog.dismiss();        });        addDrugsDialog.show();    }    private void getSpecialInstructions(MultiAutoCompleteTextView auto_compplete_special_condition) {        ArrayList<String> specialInstructionStringArrayList = new ArrayList<>();        specialInstructionStringArrayList.add("After Breakfast");        specialInstructionStringArrayList.add("After Dinner");        specialInstructionStringArrayList.add("After Food");        specialInstructionStringArrayList.add("After Lunch");        specialInstructionStringArrayList.add("Alternate Day");        specialInstructionStringArrayList.add("Apply Externally");        specialInstructionStringArrayList.add("Apply Internally");        specialInstructionStringArrayList.add("At Bed Time");        specialInstructionStringArrayList.add("Before Breakfast");        ArrayAdapter<String> adapter = new ArrayAdapter<>                (this, android.R.layout.select_dialog_item, specialInstructionStringArrayList);        auto_compplete_special_condition.setThreshold(1);        auto_compplete_special_condition.setAdapter(adapter);    }    private void getDrugs() {        showLoading();        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/drugListStandard?hosp_code=33201", response1 -> {            Log.i("TAG", "onResponse: " + response1);            try {                hideLoading();                progressBar.setVisibility(View.GONE);                JSONObject jsonObject = new JSONObject(response1);                String status = jsonObject.getString("status");                if (status.equalsIgnoreCase("1")) {                    JSONArray jsonArray = jsonObject.getJSONArray("drug_list_standard");                    Gson gson = new Gson();                    for (int i = 0; i < jsonArray.length(); i++) {                        DrugsDetails drugsDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), DrugsDetails.class);                        drugsDetailsArrayList.add(drugsDetails);                    }                    autoCompleteTextView.setThreshold(3);                    autoCompleteTextView.setAdapter(new DrugsAdapter(this, drugsDetailsArrayList));                } else {                    hideLoading();                    Toast.makeText(DrugsActivity.this, "Cannot load drugs", Toast.LENGTH_SHORT).show();                }            } catch (Exception ex) {                hideLoading();                ex.printStackTrace();            }        }, error ->        {            hideLoading();            Log.i("TAG", "onErrorResponse: " + error);        });        request.setRetryPolicy(new DefaultRetryPolicy(50000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(this).addToRequestQueue(request);    }    private void showLoading() {        progressBar.setVisibility(View.VISIBLE);        llLayout.setVisibility(View.GONE);    }    private void hideLoading() {        progressBar.setVisibility(View.GONE);        llLayout.setVisibility(View.VISIBLE);    }    public void btnSpeechInput(View view) {        getSpeechInput(drugsActivitySpeechResultLauncher);    }    public void getSpeechInput(ActivityResultLauncher<Intent> SpeechResultLauncher) {        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());        if (intent.resolveActivity(this.getPackageManager()) != null) {//            startActivityForResult(intent, 10);            SpeechResultLauncher.launch(intent);        } else {            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();        }    }    public void btnDone(View view) {        Intent data = new Intent();        data.putExtra("drugJsonArrayArrayList", drugJsonArrayArrayList);        setResult(Activity.RESULT_OK, data);        finish();    }}