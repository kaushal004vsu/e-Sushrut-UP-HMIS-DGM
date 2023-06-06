package com.cdac.uphmis.opdLite;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.util.SpeechUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    //private EditText edtHopi, edtPersonal, edtPast, edtFamily, edtTreatment, edtSurgical;

    private ActivityResultLauncher<Intent> historyActivitySpeechResultLauncher;
    private Button btnHistoryDone;


    private EditText[] edtHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        initializeViews();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String hopi = intent.getStringExtra("hopi");
            String personal = intent.getStringExtra("personal");
            String past = intent.getStringExtra("past");
            String family = intent.getStringExtra("family");
            String treatment = intent.getStringExtra("treatment");
            String surgical = intent.getStringExtra("surgical");


            edtHistory[0].setText(hopi);
            edtHistory[1].setText(past);
            edtHistory[2].setText(personal);
            edtHistory[3].setText(family);
            edtHistory[4].setText(treatment);
            edtHistory[5].setText(surgical);
        }

        btnHistoryDone = findViewById(R.id.btn_history_done);
        FloatingActionButton historySpeechInput = findViewById(R.id.fab_history);

        historySpeechInput.setOnClickListener(v -> SpeechUtility.getSpeechInput(this, historyActivitySpeechResultLauncher));


        historyActivitySpeechResultLauncher = registerForActivityResult(
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

    private void parseData(String word) {


        if (word.contains("add") || word.contains("done") || word.contains("ad")) {
            word = SpeechUtility.replaceMultiple(word, "add", "done", "ad");
            checkFocus(edtHistory, word);
            btnHistoryDone.performClick();
        } else if (word.contains("next")) {
            word = SpeechUtility.replaceMultiple(word, "next");
            next(edtHistory, word);
        }else if (word.contains("previous")) {
            word = SpeechUtility.replaceMultiple(word, "previous");
            previous(edtHistory, word);
        } else {


            if (word.contains("present")) {
                word = SpeechUtility.replaceMultiple(word, "present", "illness");
                edtHistory[0].requestFocus();
            } else if (word.contains("past")) {
                word = SpeechUtility.replaceMultiple(word, "past", "past history");
                edtHistory[1].requestFocus();
            } else if (word.contains("personal")) {
                word = SpeechUtility.replaceMultiple(word, "personal", "history");
                edtHistory[2].requestFocus();
            } else if (word.contains("family")) {
                word = SpeechUtility.replaceMultiple(word, "family", "history");
                edtHistory[3].requestFocus();
            } else if (word.contains("treatment")) {
                word = SpeechUtility.replaceMultiple(word, "treatment", "history");
                edtHistory[4].requestFocus();
            } else if (word.contains("surgical")) {
                word = SpeechUtility.replaceMultiple(word, "surgical", "history");
                edtHistory[5].requestFocus();
            }
            checkFocus(edtHistory, word);
        }
    }

    private void checkFocus(EditText[] edtHistory, String word) {
        for (int i = 0; i < edtHistory.length; i++) {
            if (edtHistory[i].hasFocus()) {
                edtHistory[i].append(word + " ");

                break;
            }
        }

    }

    private void next(EditText[] edtHistory, String word) {
        for (int i = 0; i < edtHistory.length; i++) {
            if (edtHistory[i].hasFocus()) {
                edtHistory[i].append(word + " ");

                if (i == edtHistory.length - 1) {
                    edtHistory[0].requestFocus();
                } else {
                    edtHistory[i + 1].requestFocus();
                }
                break;
            }
        }
        SpeechUtility.getSpeechInput(this, historyActivitySpeechResultLauncher);
    }

    private void previous(EditText[] edtHistory, String word) {
        for (int i = 0; i < edtHistory.length; i++) {
            if (edtHistory[i].hasFocus()) {
                edtHistory[i].append(word + " ");

                if (i == 0) {
                    edtHistory[edtHistory.length-1].requestFocus();
                } else {
                    edtHistory[i - 1].requestFocus();
                }
                break;
            }
        }
        SpeechUtility.getSpeechInput(this, historyActivitySpeechResultLauncher);
    }

    private void initializeViews() {
        edtHistory = new EditText[6];

        edtHistory[0] = findViewById(R.id.edt_hopi);
        edtHistory[1] = findViewById(R.id.edt_past);
        edtHistory[2] = findViewById(R.id.edt_personal);
        edtHistory[3] = findViewById(R.id.edt_family);
        edtHistory[4] = findViewById(R.id.edt_treatment);
        edtHistory[5] = findViewById(R.id.edt_surgical);


        edtHistory[0].requestFocus();
    }

    public void btnDone(View view) {
        Intent data = new Intent();
        data.putExtra("hopi", edtHistory[0].getText().toString());
        data.putExtra("past", edtHistory[1].getText().toString());
        data.putExtra("personal", edtHistory[2].getText().toString());
        data.putExtra("family", edtHistory[3].getText().toString());
        data.putExtra("treatment", edtHistory[4].getText().toString());
        data.putExtra("surgical", edtHistory[5].getText().toString());

        setResult(Activity.RESULT_OK, data);
        finish();
    }
}