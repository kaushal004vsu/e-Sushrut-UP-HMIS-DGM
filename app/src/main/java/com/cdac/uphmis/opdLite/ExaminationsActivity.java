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

public class ExaminationsActivity extends AppCompatActivity {
    //private EditText edtCvs, edtRs, edtCns, edtPa, edtGeneral, edtMuscular, edtLocal;

    private ActivityResultLauncher<Intent> examinationsctivitySpeechResultLauncher;
    private Button btnExaminationsDone;

    private EditText[] edtExaminations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinations);
        
        initializeViews();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String cvs = intent.getStringExtra("cvs");
            String rs = intent.getStringExtra("rs");
            String cns = intent.getStringExtra("cns");
            String pa = intent.getStringExtra("pa");
            String general = intent.getStringExtra("general");
            String muscular = intent.getStringExtra("muscular");
            String local = intent.getStringExtra("local");


            edtExaminations[0].setText(cvs);
            edtExaminations[1].setText(rs);
            edtExaminations[2].setText(cns);
            edtExaminations[3].setText(pa);
            edtExaminations[4].setText(general);
            edtExaminations[5].setText(muscular);
            edtExaminations[6].setText(local);
        }


        btnExaminationsDone = findViewById(R.id.btn_examinations_done);
        FloatingActionButton historySpeechInput = findViewById(R.id.fab_examinations);

        historySpeechInput.setOnClickListener(v -> SpeechUtility.getSpeechInput(this, examinationsctivitySpeechResultLauncher));


        examinationsctivitySpeechResultLauncher = registerForActivityResult(
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
            checkFocus(edtExaminations, word);
            btnExaminationsDone.performClick();
        } else if (word.contains("next")) {
            word = SpeechUtility.replaceMultiple(word, "next");
            next(edtExaminations, word);
        }else if (word.contains("previous")) {
            word = SpeechUtility.replaceMultiple(word, "previous");
            previous(edtExaminations, word);
        }else
        {
            if (word.contains("cvs")) {
                word = SpeechUtility.replaceMultiple(word, "cvs");
                edtExaminations[0].requestFocus();
            } else if (word.contains("rs") ) {
                word = SpeechUtility.replaceMultiple(word, "rs");
                edtExaminations[1].requestFocus();
            }else if (word.contains("cns") ) {
                word = SpeechUtility.replaceMultiple(word, "cns");
                edtExaminations[2].requestFocus();
            }else if (word.contains("pa")) {
                word = SpeechUtility.replaceMultiple(word, "pa");
                edtExaminations[3].requestFocus();
            }else if (word.contains("general") ) {
                word = SpeechUtility.replaceMultiple(word, "general");
                edtExaminations[4].requestFocus();
            }else if (word.contains("muscular")) {
                word = SpeechUtility.replaceMultiple(word, "muscular");
                edtExaminations[5].requestFocus();
            }else if (word.contains("local")) {
                word = SpeechUtility.replaceMultiple(word, "local");
                edtExaminations[6].requestFocus();
            }
            checkFocus(edtExaminations, word);
        }


    }

    private void checkFocus(EditText[] edtExaminations, String word) {
        for (int i = 0; i < edtExaminations.length; i++) {
            if (edtExaminations[i].hasFocus()) {
                edtExaminations[i].append(word + " ");

                break;
            }
        }

    }

    private void next(EditText[] edtExaminations, String word) {
        for (int i = 0; i < edtExaminations.length; i++) {
            if (edtExaminations[i].hasFocus()) {
                edtExaminations[i].append(word + " ");

                if (i == edtExaminations.length - 1) {
                    edtExaminations[0].requestFocus();
                } else {
                    edtExaminations[i + 1].requestFocus();
                }
                break;
            }
        }
        SpeechUtility.getSpeechInput(this, examinationsctivitySpeechResultLauncher);
    }

    private void previous(EditText[] edtExaminations, String word) {
        for (int i = 0; i < edtExaminations.length; i++) {
            if (edtExaminations[i].hasFocus()) {
                edtExaminations[i].append(word + " ");

                if (i == 0) {
                    edtExaminations[edtExaminations.length-1].requestFocus();
                } else {
                    edtExaminations[i - 1].requestFocus();
                }
                break;
            }
        }
        SpeechUtility.getSpeechInput(this, examinationsctivitySpeechResultLauncher);
    }
    private void initializeViews() {
        edtExaminations=new EditText[7];
        edtExaminations[0] = findViewById(R.id.edt_cvs);
        edtExaminations[1] = findViewById(R.id.edt_rs);
        edtExaminations[2] = findViewById(R.id.edt_cns);
        edtExaminations[3] = findViewById(R.id.edt_pa);
        edtExaminations[4] = findViewById(R.id.edt_general);
        edtExaminations[5] = findViewById(R.id.edt_muscular);
        edtExaminations[6] = findViewById(R.id.edt_local);

        edtExaminations[0].requestFocus();
    }

    public void btnDone(View view) {

        Intent data = new Intent();
        data.putExtra("cvs", edtExaminations[0].getText().toString());
        data.putExtra("rs", edtExaminations[1].getText().toString());
        data.putExtra("cns", edtExaminations[2].getText().toString());
        data.putExtra("pa", edtExaminations[3].getText().toString());
        data.putExtra("general", edtExaminations[4].getText().toString());
        data.putExtra("muscular", edtExaminations[5].getText().toString());
        data.putExtra("local", edtExaminations[6].getText().toString());

        setResult(Activity.RESULT_OK, data);
        finish();
    }
}