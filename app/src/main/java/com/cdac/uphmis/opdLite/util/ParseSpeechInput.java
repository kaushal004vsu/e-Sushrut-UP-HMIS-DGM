package com.cdac.uphmis.opdLite.util;


import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;


public class ParseSpeechInput {


    public static void parseInvestigationsData(String word, Button btnAdd, Button btnClose, AutoCompleteTextView autoCompleteTextview, EditText edtDescription, TextView[] artextviews) {
        if (word.contains("add") || word.contains("ad")) {
            word = SpeechUtility.replaceMultiple(word, "add", "ad");

            if (autoCompleteTextview.hasFocus()) {
                autoCompleteTextview.setText(word);
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
            }
            btnAdd.performClick();
        } else if (word.contains("close") || word.contains("done")) {
            word = SpeechUtility.replaceMultiple(word, "close", "done");

            if (autoCompleteTextview.hasFocus()) {
                autoCompleteTextview.setText(word);
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
            }
            btnClose.performClick();
        } else if (word.contains("side")) {
            artextviews[0].performClick();
        } else if (word.contains("nr")) {
            artextviews[1].performClick();
        } else if (word.contains("left")) {
            artextviews[2].performClick();
        } else if (word.contains("right")) {
            artextviews[3].performClick();
        } else if (word.contains("bilateral")) {
            artextviews[4].performClick();
        } else {

            if (word.contains("description")) {
                word = SpeechUtility.replaceMultiple(word, "description");
                edtDescription.append(word + " ");
                autoCompleteTextview.clearFocus();
            }

            if (autoCompleteTextview.hasFocus()) {
                autoCompleteTextview.setText(word);
                edtDescription.clearFocus();
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
                autoCompleteTextview.clearFocus();
            }
        }

    }

    public static void parseComplaintsData(String word, Button btnAdd, Button btnClose, AutoCompleteTextView autoCompleteTextview, EditText edtDescription, TextView[] artextviews, EditText tvNumber, RadioButton radioButtonComplaintsDays, RadioButton radioButtonComplaintsWeeks, RadioButton radioButtonComplaintsMonths) {
        boolean radioSelectCondition = word.contains("day") || word.contains("days") || word.contains("week") || word.contains("weeks") || word.contains("month") || word.contains("months");

        if (word.contains("add") || word.contains("ad")) {
            word = SpeechUtility.replaceMultiple(word, "add", "ad");

            if (autoCompleteTextview.hasFocus()) {
                autoCompleteTextview.setText(word);
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
            }
            btnAdd.performClick();
        } else if (word.contains("close") || word.contains("done")) {
            word = SpeechUtility.replaceMultiple(word, "close", "done");

            if (autoCompleteTextview.hasFocus()) {
                autoCompleteTextview.setText(word);
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
            }
            btnClose.performClick();
        } else if (word.contains("side")) {
            artextviews[0].performClick();
        } else if (word.contains("nr")) {
            artextviews[1].performClick();
        } else if (word.contains("left")) {
            artextviews[2].performClick();
        } else if (word.contains("right")) {
            artextviews[3].performClick();
        } else if (word.contains("bilateral")) {
            artextviews[4].performClick();
        } else if (radioSelectCondition) {
            String number = word.replaceAll("[^0-9]", "");
            if (!number.isEmpty()) {
                tvNumber.setText(String.valueOf(number).trim());
            }
            if (radioSelectCondition) {
                if (word.contains("days") || word.contains("days")) {
                    radioButtonComplaintsDays.setChecked(true);
                } else if (word.contains("week") || word.contains("weeks")) {
                    radioButtonComplaintsWeeks.setChecked(true);
                } else if (word.contains("month") || word.contains("months")) {
                    radioButtonComplaintsMonths.setChecked(true);
                }
            }
        } else {

            if (word.contains("description")) {
                word = SpeechUtility.replaceMultiple(word, "description");
                edtDescription.append(word + " ");
                autoCompleteTextview.clearFocus();
            }

            if (autoCompleteTextview.hasFocus()) {
                autoCompleteTextview.setText(word);
                edtDescription.clearFocus();
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
                autoCompleteTextview.clearFocus();
            }
        }

    }


    public static void parseRxData(String word, Button btnRxDone, EditText edtRx) {
        if (word.contains("add") || word.contains("done") || word.contains("ad")) {
            word = SpeechUtility.replaceMultiple(word, "add", "done", "ad");
            edtRx.append(word + " ");
            btnRxDone.performClick();
        } else {
            edtRx.append(word + " ");
        }

    }


    public static void parseProceduresData(String word, Button btnProceduresAdd, Button btnProceduresClose, AutoCompleteTextView autoCompleteProcedures, EditText edtProcedureDescription, TextView[] arProceduresTextViews) {
        if (word.contains("add") || word.contains("ad")) {
            word = SpeechUtility.replaceMultiple(word, "add", "ad");

            if (autoCompleteProcedures.hasFocus()) {
                autoCompleteProcedures.setText(word);
            } else if (edtProcedureDescription.hasFocus()) {
                edtProcedureDescription.append(word + " ");
            }
            btnProceduresAdd.performClick();
        } else if (word.contains("close") || word.contains("done")) {
            word = SpeechUtility.replaceMultiple(word, "close", "done");

            if (autoCompleteProcedures.hasFocus()) {
                autoCompleteProcedures.setText(word);
            } else if (edtProcedureDescription.hasFocus()) {
                edtProcedureDescription.append(word + " ");
            }
            btnProceduresClose.performClick();
        } else if (word.contains("side")) {
            arProceduresTextViews[0].performClick();
        } else if (word.contains("nr")) {
            arProceduresTextViews[1].performClick();
        } else if (word.contains("left")) {
            arProceduresTextViews[2].performClick();
        } else if (word.contains("right")) {
            arProceduresTextViews[3].performClick();
        } else if (word.contains("bilateral")) {
            arProceduresTextViews[4].performClick();
        } else {

            if (word.contains("description")) {
                word = SpeechUtility.replaceMultiple(word, "description");
                edtProcedureDescription.append(word + " ");
                autoCompleteProcedures.clearFocus();
            }

            if (autoCompleteProcedures.hasFocus()) {
                autoCompleteProcedures.setText(word);
                edtProcedureDescription.clearFocus();
            } else if (edtProcedureDescription.hasFocus()) {
                edtProcedureDescription.append(word + " ");
                autoCompleteProcedures.clearFocus();
            }
        }

    }


    public static void parseDiagnosisData(String word, Button btnAdd, Button btnClose, AutoCompleteTextView snomedAutoCompleteTextview, AutoCompleteTextView icdAutoCompleteTextview, AutoCompleteTextView diseaseAutoCompleteTextview, EditText edtDescription, TextView[] arSidetextviews, TextView[] arTypetextviews, Switch switchDiagnosis, boolean isSnomedChecked) {

        if (word.contains("add") || word.contains("ad")) {
            word = SpeechUtility.replaceMultiple(word, "add", "ad");
            if (word.equals("add") || word.equals("ad")) {
                btnAdd.performClick();
                return;
            }
            if (snomedAutoCompleteTextview.hasFocus()) {
                snomedAutoCompleteTextview.setText(word);
            } else if (icdAutoCompleteTextview.hasFocus()) {
                icdAutoCompleteTextview.setText(word);
            } else if (diseaseAutoCompleteTextview.hasFocus()) {
                diseaseAutoCompleteTextview.setText(word);
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
            }

            btnAdd.performClick();
            return;
        } else if (word.contains("close") || word.contains("done")) {
            word = SpeechUtility.replaceMultiple(word, "close", "done");

            if (snomedAutoCompleteTextview.hasFocus()) {
                snomedAutoCompleteTextview.setText(word);
            } else if (icdAutoCompleteTextview.hasFocus()) {
                icdAutoCompleteTextview.setText(word);
            } else if (diseaseAutoCompleteTextview.hasFocus()) {
                diseaseAutoCompleteTextview.setText(word);
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
            }

            btnClose.performClick();
            return;
        } else if (word.contains("icd")) {
            switchDiagnosis.setChecked(false);
            snomedAutoCompleteTextview.clearFocus();
            icdAutoCompleteTextview.clearFocus();
        } else if (word.contains("snomed")) {
            switchDiagnosis.setChecked(true);
            icdAutoCompleteTextview.clearFocus();
            diseaseAutoCompleteTextview.clearFocus();
        } else if (word.contains("side")) {
            arSidetextviews[0].performClick();
        } else if (word.contains("nr")) {
            arSidetextviews[1].performClick();
        } else if (word.contains("left")) {
            arSidetextviews[2].performClick();
        } else if (word.contains("right")) {
            arSidetextviews[3].performClick();
        } else if (word.contains("bilateral")) {
            arSidetextviews[4].performClick();
        } else if (word.contains("provisional")) {
            arTypetextviews[0].performClick();
        } else if (word.contains("differential")) {
            arTypetextviews[1].performClick();
        } else if (word.contains("final")) {
            arTypetextviews[2].performClick();
        } else {

            if (word.contains("description")) {
                word = SpeechUtility.replaceMultiple(word, "description");
                edtDescription.append(word + " ");
                snomedAutoCompleteTextview.clearFocus();
                icdAutoCompleteTextview.clearFocus();
                diseaseAutoCompleteTextview.clearFocus();
            } else if (snomedAutoCompleteTextview.hasFocus()) {
                snomedAutoCompleteTextview.setText(word);
                edtDescription.clearFocus();
            } else if (icdAutoCompleteTextview.hasFocus()) {
                icdAutoCompleteTextview.setText(word);
                edtDescription.clearFocus();
            } else if (diseaseAutoCompleteTextview.hasFocus()) {
                diseaseAutoCompleteTextview.setText(word);
                edtDescription.clearFocus();
            } else if (edtDescription.hasFocus()) {
                edtDescription.append(word + " ");
                snomedAutoCompleteTextview.clearFocus();
                icdAutoCompleteTextview.clearFocus();
                diseaseAutoCompleteTextview.clearFocus();
            }
        }

    }

    public static void parseHomeData(String word,TextView tvComplaints,TextView tvHistory,TextView tvDiagnoisis,TextView tvExaminations,TextView tvInvestigaitons,TextView tvProcedures,TextView tvDrugs,TextView tvRx,TextView tvVitals) {
        if (word.contains("complaints")||word.contains("complaint"))
        {
            tvComplaints.performClick();
        } else if (word.contains("diagnosis")){
            tvDiagnoisis.performClick();
        }else if (word.contains("history")){
            tvHistory.performClick();
        }else if (word.contains("examinations")||word.contains("examination")){
            tvExaminations.performClick();
        }else if (word.contains("investigations")||word.contains("investigation")){
            tvInvestigaitons.performClick();
        }else if (word.contains("procedures")||word.contains("procedure")){
            tvProcedures.performClick();
        }else if (word.contains("drugs")||word.contains("drug")){
            tvDrugs.performClick();
        }else if (word.contains("rx")){
            tvRx.performClick();
        }else if (word.contains("vitals")||word.contains("vital")){
            tvVitals.performClick();
        }
    }
}
