package com.cdac.uphmis.opdLite;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.util.SpeechUtility;

import java.util.ArrayList;

import static java.lang.Float.parseFloat;

public class VitalsActivity extends AppCompatActivity {
    //  private EditText edtWeight, edtHeight, edtTemperature, edtPulse, edtBpSystolic, edtBpDiastolic, edtFast, edtPp, edtHba, edtHgb, edtDescription;
    private TextView tvBmi, tvBmiStatus;

    private CheckBox cbDisability, cbSmoking, cbAnemic;

    private String bloodGroup = "";


    private ActivityResultLauncher<Intent> vitalsActivitySpeechResultLauncher;


    private Button btnDone;

    private EditText[] vitalsEdittext;



   // TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);
        
        //initializeTTS();
        initializeViews();

        vitalsEdittext[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String bmi = calculateBmi(vitalsEdittext[0].getText().toString(), vitalsEdittext[1].getText().toString());
                if (!bmi.isEmpty()) {
                    tvBmiStatus.setText(bmi);
                } else {
                    tvBmi.setText("");
                    tvBmiStatus.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vitalsEdittext[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String bmi = calculateBmi(vitalsEdittext[0].getText().toString(), vitalsEdittext[1].getText().toString());
                if (!bmi.isEmpty()) {
                    tvBmiStatus.setText(bmi);
                } else {
                    tvBmi.setText("");
                    tvBmiStatus.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        vitalsActivitySpeechResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            // doSomeOperations();
                            ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            String word = result1.get(0).toLowerCase();
                            parseData(word);


                        }
                    }
                });
    }


   /* private void initializeTTS()
    {
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(new Locale("hi_IN"));
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                        SpeechUtility.getSpeechInput(VitalsActivity.this, vitalsActivitySpeechResultLauncher);
                    }
                    else{

                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }


        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.i("TextToSpeech","On Start");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.i("TextToSpeech","On Done");
                SpeechUtility.getSpeechInput(VitalsActivity.this, vitalsActivitySpeechResultLauncher);
            }

            @Override
            public void onError(String utteranceId) {
                Log.i("TextToSpeech","On Error");
                SpeechUtility.getSpeechInput(VitalsActivity.this, vitalsActivitySpeechResultLauncher);
            }
        });
    }*/

   /* private void ConvertTextToSpeech(String text) {


        if(text==null||text.isEmpty())
        {
            text = "Content not available";
        //    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "1");


        }
    }
*/
    private void initializeViews() {


        vitalsEdittext = new EditText[11];


        vitalsEdittext[0] = findViewById(R.id.edt_weight);
        vitalsEdittext[1] = findViewById(R.id.edt_height);
        vitalsEdittext[2] = findViewById(R.id.edt_temperature);
        vitalsEdittext[3] = findViewById(R.id.edtpulse);
        vitalsEdittext[4] = findViewById(R.id.bp_systolic);
        vitalsEdittext[5] = findViewById(R.id.bp_diastolic);
        vitalsEdittext[6] = findViewById(R.id.edt_fast);
        vitalsEdittext[7] = findViewById(R.id.edt_pp);
        vitalsEdittext[8] = findViewById(R.id.edt_hba);
        vitalsEdittext[9] = findViewById(R.id.edt_hgb);
        vitalsEdittext[10] = findViewById(R.id.edt_description);

        vitalsEdittext[0].requestFocus();
        /*edtWeight = findViewById(R.id.edt_weight);
        edtHeight = findViewById(R.id.edt_height);
        tvBmi = findViewById(R.id.tv_bmi_header);
        tvBmiStatus = findViewById(R.id.bmi_status);
        edtTemperature = findViewById(R.id.edt_temperature);
        edtPulse = findViewById(R.id.edtpulse);
        edtBpSystolic = findViewById(R.id.bp_systolic);
        edtBpDiastolic = findViewById(R.id.bp_diastolic);
        edtFast = findViewById(R.id.edt_fast);
        edtPp = findViewById(R.id.edt_pp);
        edtHba = findViewById(R.id.edt_hba);
        edtHgb = findViewById(R.id.edt_hgb);
        edtDescription = findViewById(R.id.edt_description);*/

        tvBmi = findViewById(R.id.tv_bmi_header);
        tvBmiStatus = findViewById(R.id.bmi_status);

        cbDisability = findViewById(R.id.cb_disability);
        cbSmoking = findViewById(R.id.cb_smoking);
        cbAnemic = findViewById(R.id.cb_anemic);


        btnDone = findViewById(R.id.btn_vitals_done);

        Spinner spBloodGroup = findViewById(R.id.sp_blood_group);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String weight = intent.getStringExtra("weight");
            String height = intent.getStringExtra("height");
            String bmi = intent.getStringExtra("bmi");
            String bmiStatus = intent.getStringExtra("bmiStatus");
            String temperature = intent.getStringExtra("temperature");
            String pulse = intent.getStringExtra("pulse");

            String low = intent.getStringExtra("low");
            String high = intent.getStringExtra("high");
            String fast = intent.getStringExtra("");
            String pp = intent.getStringExtra("pp");
            String hba = intent.getStringExtra("hba");


            String hgb = intent.getStringExtra("hgb");
            bloodGroup = intent.getStringExtra("bloodGroup");
            String description = intent.getStringExtra("description");

            boolean disability = intent.getBooleanExtra("disability", false);
            boolean smoking = intent.getBooleanExtra("smoking", false);
            boolean anemic = intent.getBooleanExtra("anemic", false);

            vitalsEdittext[0].setText(weight);
            vitalsEdittext[1].setText(height);
            tvBmi.setText(bmi);
            tvBmiStatus.setText(bmiStatus);
            vitalsEdittext[2].setText(temperature);
            vitalsEdittext[3].setText(pulse);

            vitalsEdittext[4].setText(low);
            vitalsEdittext[5].setText(high);
            vitalsEdittext[6].setText(fast);
            vitalsEdittext[7].setText(pp);
            vitalsEdittext[8].setText(hba);
            vitalsEdittext[9].setText(hgb);


            vitalsEdittext[10].setText(description);

            if (disability) {
                cbDisability.setChecked(true);
            }
            if (smoking) {
                cbSmoking.setChecked(true);
            }
            if (anemic) {
                cbAnemic.setChecked(true);
            }


            ArrayList<String> arrayListBloodGroup = new ArrayList<>();
            arrayListBloodGroup.add("Select");
            arrayListBloodGroup.add("A-");
            arrayListBloodGroup.add("B-");
            arrayListBloodGroup.add("O-");
            arrayListBloodGroup.add("AB-");
            arrayListBloodGroup.add("A+");
            arrayListBloodGroup.add("B+");
            arrayListBloodGroup.add("O+");
            arrayListBloodGroup.add("AB+");
            ArrayAdapter adapter = new ArrayAdapter(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayListBloodGroup);
            spBloodGroup.setAdapter(adapter);
            spBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);

                    bloodGroup = (String) parent.getItemAtPosition(position);
                    Log.i("TAG", "onItemSelected: " + bloodGroup);
                    if (bloodGroup.equalsIgnoreCase("Select")) {
                        bloodGroup = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //ConvertTextToSpeech("Enter "+vitalsEdittext[0].getHint().toString());
        }

    }

    public void btnDone(View view) {
        Intent data = new Intent();
        data.putExtra("weight", vitalsEdittext[0].getText().toString());
        data.putExtra("height", vitalsEdittext[1].getText().toString());
        data.putExtra("bmi", tvBmi.getText().toString());
        data.putExtra("bmiStatus", tvBmiStatus.getText().toString());
        data.putExtra("temperature", vitalsEdittext[2].getText().toString());
        data.putExtra("pulse", vitalsEdittext[3].getText().toString());
        data.putExtra("low", vitalsEdittext[4].getText().toString());
        data.putExtra("high", vitalsEdittext[5].getText().toString());
        data.putExtra("fast", vitalsEdittext[6].getText().toString());
        data.putExtra("pp", vitalsEdittext[7].getText().toString());
        data.putExtra("hba", vitalsEdittext[8].getText().toString());
        data.putExtra("hgb", vitalsEdittext[9].getText().toString());
        data.putExtra("description", vitalsEdittext[10].getText().toString());
        data.putExtra("bloodGroup", bloodGroup);


        data.putExtra("disability", cbDisability.isChecked());
        data.putExtra("smoking", cbSmoking.isChecked());
        data.putExtra("anemic", cbAnemic.isChecked());

        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private String calculateBmi(String weight, String height) {
        if (!weight.isEmpty() && !height.isEmpty()) {
            float temp = Float.parseFloat(height) / 100;

            float bmi = parseFloat(weight) / (temp * temp);
            tvBmi.setText(String.valueOf(bmi));
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


    public void btnSpeechInput(View view) {
        SpeechUtility.getSpeechInput(this, vitalsActivitySpeechResultLauncher);
    }


    private void parseData(String word) {
        if (word.contains("add") || word.contains("done") || word.contains("ad") || word.contains("close") || word.contains("back")) {
            btnDone.performClick();
        } else if (word.contains("next")) {
            word = SpeechUtility.replaceMultiple(word, "next");
            next(vitalsEdittext, word);
        } else if (word.contains("previous")) {
            word = SpeechUtility.replaceMultiple(word, "previous");
            previous(vitalsEdittext, word);
        } else if (word.contains("smoking yes")) {
            cbSmoking.setChecked(true);
        } else if (word.contains("smoking no")) {
            cbSmoking.setChecked(false);
        }else if (word.contains("anemic yes")) {
            cbAnemic.setChecked(true);
        } else if (word.contains("anemic no")) {
            cbAnemic.setChecked(false);
        } else if (word.contains("disable yes")||word.contains("disability yes")) {
            cbDisability.setChecked(true);
        } else if (word.contains("disable yes")||word.contains("disability yes")) {
            cbDisability.setChecked(false);
        } else {
            if (word.contains("weight") || word.contains("kg")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "weight", "kg");
                vitalsEdittext[0].requestFocus();
            } else if (word.contains("height") || word.contains("centimetre") || word.contains("cm") || word.contains("cms")) {
//            word = SpeechUtility.replaceMultiple(word.trim(), "height","centimetre","cm","cms");
                word = word.replaceAll("[^0-9]", "");
                vitalsEdittext[1].requestFocus();
            } else if (word.contains("temperature")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "temperature");
                vitalsEdittext[2].requestFocus();
            } else if (word.contains("pulse") || word.contains("rate")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "pulse", "rate");
                vitalsEdittext[3].requestFocus();
            } else if (word.contains("systolic")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "blood", "pressure", "mmhg", "systolic", "diastolic");
                vitalsEdittext[4].requestFocus();

            } else if (word.contains("diastolic")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "blood", "pressure", "mmhg", "systolic", "diastolic");

                vitalsEdittext[5].requestFocus();
            } else if (word.contains("fast")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "fast");

                vitalsEdittext[6].requestFocus();
            } else if (word.contains("pp")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "pp");

                vitalsEdittext[7].requestFocus();
            } else if (word.contains("hba1c")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "hba1c");

                vitalsEdittext[8].requestFocus();
            } else if (word.contains("hgb")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "hgb");

                vitalsEdittext[9].requestFocus();
            } else if (word.contains("description")) {
                word = SpeechUtility.replaceMultiple(word.trim(), "description");

                vitalsEdittext[10].requestFocus();
            }

            checkFocus(vitalsEdittext, word);
        }
    }


    private void next(EditText[] edtVitals, String word) {
        try {
            for (int i = 0; i < edtVitals.length; i++) {
                if (edtVitals[i].hasFocus()) {
                    if (i == edtVitals.length) {
                        edtVitals[i].append(word + " ");
                    } else {
                        edtVitals[i].setText(word);
                    }
                    edtVitals[i].clearFocus();
                    if (i == edtVitals.length - 1) {
                        edtVitals[0].requestFocus();
//                        ConvertTextToSpeech("Enter " + edtVitals[0].getHint().toString());
                    } else {
                        edtVitals[i + 1].requestFocus();

//                        ConvertTextToSpeech("Enter " + edtVitals[i + 1].getHint().toString());
                    }
                    break;
                }
            }
        }catch(Exception ex){ex.printStackTrace();}


        SpeechUtility.getSpeechInput(this, vitalsActivitySpeechResultLauncher);
    }

    private void previous(EditText[] edtVitals, String word) {
        try {
            for (int i = 0; i < edtVitals.length; i++) {
                if (edtVitals[i].hasFocus()) {

                    if (edtVitals[i].hasFocus()) {
                        if (i == edtVitals.length - 1) {
                            edtVitals[i].append(word + " ");
                        } else {
                            edtVitals[i].setText(word);
                        }
                        edtVitals[i].clearFocus();
                        if (i == 0) {
                            edtVitals[edtVitals.length - 1].requestFocus();
//                            ConvertTextToSpeech("Enter " + edtVitals[0].getHint().toString());
                        } else {
                            edtVitals[i - 1].requestFocus();
//                            ConvertTextToSpeech("Enter " + edtVitals[i - 1].getHint().toString());
                        }
                        break;
                    }
                }
                   SpeechUtility.getSpeechInput(this, vitalsActivitySpeechResultLauncher);
            }
        }catch (Exception ex){}
    }

    private void checkFocus(EditText[] edtVitals, String word) {
        for (int i = 0; i < edtVitals.length; i++) {
            if (edtVitals[i].hasFocus()) {
//                edtVitals[i].append(word + " ");
                if (i != 10)
                    edtVitals[i].setText(word);
                else
                    edtVitals[i].append(word + " ");

                break;
            }
        }

    }

   /* @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }*/
}