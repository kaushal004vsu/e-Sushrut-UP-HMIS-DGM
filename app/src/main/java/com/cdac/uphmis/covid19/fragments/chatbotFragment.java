package com.cdac.uphmis.covid19.fragments;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cdac.uphmis.R;


public class  chatbotFragment extends Fragment {

    View view;
    ArrayList<String> buttonSelectOptions;
    String buttonColor;
    ArrayList<String> tempButtonSelectedOptions;
    int quesCounter;
    int tempCounter1,tempCounter2,tempCounter5,tempCounter3,tempCounter4;
    ArrayList<String> questions;
    ArrayList<ArrayList<String>> options;
    ScrollView scroll;
    int riskFactor;
    private int animationSource;
    Button b1;
    Map<String,Button> allButtonInstances;

    public chatbotFragment() {
        // Required empty public constructor
    }



    @Override
    //on create layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chatbot, container, false);
        scroll = (ScrollView) view.findViewById(R.id.scroll);

        quesCounter = 0;
        tempCounter1=0;
        tempCounter2=0;
        tempCounter3=0;
        tempCounter4=0;
        tempCounter5=0;
        allButtonInstances = new HashMap<>();
        buttonSelectOptions = new ArrayList<>();
        questions = new ArrayList<>();
        options = new ArrayList<>();
        ArrayList<String> op;
        // First Ques
        // creating array of questions and options to be asked
        questions.add("The place where you lives falls in which zone");
        op = new ArrayList<>();
        op.add("Red Zone");
        op.add("Green Zone");
        op.add("Orange Zone");
        op.add("NEXT");
        options.add(op);


        questions.add("Are you a smoker or non smoker");
        op = new ArrayList<>();
        op.add("Smoker");
        op.add("Non Smoker");
        op.add("NEXT");
        options.add(op);

        // Second Ques
        questions.add("Are you experiencing any of the following symptoms");
        op = new ArrayList<>();
        op.add("Cough");
        op.add("Fever");
        op.add("Headache");
        op.add("Difficulty in breathing");
        op.add("None of the above");
        op.add("NEXT");
        options.add(op);

        // Define Further
        // 3rd question

        questions.add("Do you have any of following possible emergency symptoms");
        op = new ArrayList<>();
        op.add("Struggling for breath even when resting");
        op.add("Feeling about to collapse everytime I stand/sit up");
        op.add("I am experiencing none of these symptoms");
        op.add("NEXT");
        options.add(op);


        //4th
        questions.add("Have you any following underlying conditions");
        op = new ArrayList<>();
        op.add("Diabetes");
        op.add("Hypertension");
        op.add("Heart Disease");
        op.add("Lung Disease");
        op.add("None");
        op.add("NEXT");
        options.add(op);

        //5th
        questions.add("Are you related to any healthcare worker");
        op = new ArrayList<>();
        op.add("I am a doctor/Health Care Worker");
        op.add("Living with Health Care Worker");
        op.add("None of these descriptions applies to me");
        op.add("NEXT");
        options.add(op);


        questions.add("If anyone of the following applies to you");
        op = new ArrayList<>();
        op.add("I have Travellled Internationally in the last One month");
        op.add("Interacted with someone who tested positively for Covid 19");
        op.add("None of these described situations applies to me currently");
        op.add("NEXT");

        options.add(op);


        Button button = new Button(getContext());
        button.setTextColor(getResources().getColor(android.R.color.black));
        buttonColor = button.getTextColors().toString();



        AppendChat(questions.get(0));
        option_appender(options.get(0));

        return view;
    }
    //This function is appending questions one by one
    public void AppendChat(String text){

        LinearLayout linearLayout = view.findViewById(R.id.MainChatLayout);

        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setBackground(getResources().getDrawable(R.drawable.rounded_form));
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setTextSize(14);
        textView.setPadding(16,16,16,16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(46,16,270,0);
        textView.setLayoutParams(layoutParams);

        linearLayout.addView(textView);
        scroll.scrollTo(0, scroll.getBottom());
    }


    //this function is appending answers
    public void answer_appender(String text){
        LinearLayout linearLayout = view.findViewById(R.id.MainChatLayout);

        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setBackground(getResources().getDrawable(R.drawable.rounded_from2));
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setTextSize(14);
        textView.setPadding(16,16,16,16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(600,16,46,0);
        textView.setLayoutParams(layoutParams);

        linearLayout.addView(textView);
        scroll.scrollTo(0, scroll.getBottom());
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
    }


    //this function is appending options from array of options
    public void option_appender(final ArrayList<String> options){
        final LinearLayout mainLayout = view.findViewById(R.id.MainChatLayout);
        tempButtonSelectedOptions = new ArrayList<>();
        final LinearLayout mainButtonLayout = new LinearLayout(getContext());
        mainButtonLayout.setPadding(20,30,20,0);
        mainButtonLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainButtonLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        mainButtonLayout.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);

        for(int i=0;i<=Math.ceil(options.size()/3);i++){
            LinearLayout childLayout = new LinearLayout(getContext());
            childLayout.setLayoutParams(layoutParams);
            childLayout.setWeightSum(3);
            LinearLayout nextLayout = new LinearLayout(getContext());
            nextLayout.setLayoutParams(layoutParams);
            nextLayout.setWeightSum(3);
            for(int j=0;j<3;j++){
                if(3*i+j<options.size()){
                    final Button button = new Button(getContext());
                    LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(280,110);

                    button.setLayoutParams(buttonLayout);


                    button.setText(options.get(3*i+j));
                    button.setTextColor(getResources().getColor(android.R.color.black));
                    if(options.get(3*i+j).equals("NEXT")){
                        b1 = button;
                        nextLayout.addView(button);
                        allButtonInstances.put(options.get(3*i+j)+quesCounter,button);
                    }
                    else{
                        childLayout.addView(button);
                        allButtonInstances.put(options.get(3*i+j),button);
                    }

                    if(options.get(3*i+j).equals("NEXT")){
                        button.setFocusable(true);
                        button.setFocusableInTouchMode(true);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(tempButtonSelectedOptions.size() > 0){
                                    mainLayout.removeView(mainButtonLayout);
                                    String z = "";
                                    for(int i=0;i<tempButtonSelectedOptions.size();i++){
                                        z = z+tempButtonSelectedOptions.get(i)+" ";
                                    }


                                    answer_appender(z);
                                    quesCounter++;
                                    if(quesCounter<questions.size()){
                                        AppendChat(getQues(quesCounter));
                                        option_appender(getOp(quesCounter));
                                    }
                                    if(quesCounter == 6){
                                        ArrayList<String> op;

                                        if(riskFactor>=6)
                                        {
                                            questions.add("Thank you for taking this assessment.If the information provided by you is accurate , it indicates that you are either unwell or at risk.Please avoid contact with others.You are advised for testing as your risk infection is high.Please call the toll free help line 1075 to schedule your testing.");
                                            op = new ArrayList<>();
                                            addOp(op);

                                        }
                                        else if(riskFactor>=3)
                                        {
                                            questions.add("Your risk infection is moderate.If the symptoms persists contact doctor as early as possible.We recommend that you stay at home to avoid any chance of exposure to the Novel CoronaVirus.Retake the self assessment test if you develop symptoms or come in contact with Covid-19 confirned patient");
                                            op = new ArrayList<>();
                                            addOp(op);


                                        }
                                        else
                                        {
                                            questions.add("Your risk infection is low.We recommend that you stay at home to avoid any chance of exposure to the Novel CoronaVirus.Retake the self assessment test if you develop symptoms or come in contact with Covid-19 confirned patient");
                                            op = new ArrayList<>();
                                            addOp(op);



                                        }
                                    }
                                }
                            }
                        });
                        button.requestFocus();
                    }
                    else{
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String x = button.getTextColors().toString();
                                if(x.equals(buttonColor)){
                                    button.setTextColor(getResources().getColor(R.color.select_button));
                                    buttonSelectOptions.add(button.getText().toString());
                                    tempButtonSelectedOptions.add(button.getText().toString());
                                    if(button.getText().toString().equals("Red Zone")){
                                        riskFactor=riskFactor+3;
                                        allButtonInstances.get("Orange Zone").setEnabled(false);
                                        allButtonInstances.get("Green Zone").setEnabled(false);


                                        //   Log.i("Risk Factor", "onClick: "+riskFactor);
                                    }
                                    if(button.getText().toString().equals("Orange Zone")){
                                        riskFactor=riskFactor+1;
                                        allButtonInstances.get("Red Zone").setEnabled(false);
                                        allButtonInstances.get("Green Zone").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("Green Zone")){

                                        allButtonInstances.get("Red Zone").setEnabled(false);
                                        allButtonInstances.get("Orange Zone").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("Smoker")){
                                        riskFactor=riskFactor+1;
                                        allButtonInstances.get("Non Smoker").setEnabled(false);;

                                    }
                                    if(button.getText().toString().equals("Non Smoker")){
                                        allButtonInstances.get("Smoker").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("Cough")){
                                        riskFactor=riskFactor+1;
                                        tempCounter1++;
                                        allButtonInstances.get("None of the above").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("Fever")){
                                        riskFactor=riskFactor+1;
                                        tempCounter1++;
                                        allButtonInstances.get("None of the above").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("Headache")){
                                        riskFactor=riskFactor+1;
                                        tempCounter1++;
                                        allButtonInstances.get("None of the above").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("Difficulty in breathing")){
                                        riskFactor=riskFactor+1;
                                        tempCounter1++;
                                        allButtonInstances.get("None of the above").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("None of the above")){

                                        allButtonInstances.get("Headache").setEnabled(false);
                                        allButtonInstances.get("Fever").setEnabled(false);
                                        allButtonInstances.get("Cough").setEnabled(false);
                                        allButtonInstances.get("Difficulty in breathing").setEnabled(false);

                                    }
                                    //adjgajd
                                    if(button.getText().toString().equals("Struggling for breath even when resting")){
                                        riskFactor=riskFactor+2;
                                        tempCounter5++;
                                        allButtonInstances.get("I am experiencing none of these symptoms").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("Feeling about to collapse everytime I stand/sit up")){
                                        riskFactor=riskFactor+2;
                                        tempCounter5++;
                                        allButtonInstances.get("I am experiencing none of these symptoms").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("I am experiencing none of these symptoms")){

                                        allButtonInstances.get("Struggling for breath even when resting").setEnabled(false);
                                        allButtonInstances.get("Feeling about to collapse everytime I stand/sit up").setEnabled(false);


                                    }



                                    if(button.getText().toString().equals("Diabetes")){
                                        riskFactor=riskFactor+1;
                                        tempCounter2++;
                                        allButtonInstances.get("None").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("Hypertension")){
                                        riskFactor=riskFactor+1;
                                        tempCounter2++;
                                        allButtonInstances.get("None").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("Heart Disease")){
                                        riskFactor=riskFactor+1;
                                        tempCounter2++;
                                        allButtonInstances.get("None").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("Lung Disease")){
                                        riskFactor=riskFactor+1;
                                        tempCounter2++;
                                        allButtonInstances.get("None").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("None")){

                                        allButtonInstances.get("Lung Disease").setEnabled(false);
                                        allButtonInstances.get("Heart Disease").setEnabled(false);
                                        allButtonInstances.get("Hypertension").setEnabled(false);
                                        allButtonInstances.get("Diabetes").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("I am a doctor/Health Care Worker")){
                                        tempCounter3++;
                                        riskFactor=riskFactor+3;
                                        allButtonInstances.get("None of these descriptions applies to me").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("Living with Health Care Worker")){
                                        riskFactor=riskFactor+2;
                                        tempCounter3++;
                                        allButtonInstances.get("None of these descriptions applies to me").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("None of these descriptions applies to me")){

                                        allButtonInstances.get("Living with Health Care Worker").setEnabled(false);
                                        allButtonInstances.get("I am a doctor/Health Care Worker").setEnabled(false);

                                    }
                                    if(button.getText().toString().equals("I have Travellled Internationally in the last One month")){
                                        riskFactor=riskFactor+2;
                                        tempCounter4++;
                                        allButtonInstances.get("None of these described situations applies to me currently").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("Interacted with someone who tested positively for Covid 19")){
                                        riskFactor=riskFactor+3;
                                        tempCounter4++;
                                        allButtonInstances.get("None of these described situations applies to me currently").setEnabled(false);
                                    }
                                    if(button.getText().toString().equals("None of these described situations applies to me currently")){

                                        allButtonInstances.get("I have Travellled Internationally in the last One month").setEnabled(false);
                                        allButtonInstances.get("Interacted with someone who tested positively for Covid 19").setEnabled(false);

                                    }


                                }
                                else{
                                    button.setTextColor(getResources().getColor(android.R.color.black));
                                    buttonSelectOptions.remove(button.getText().toString());
                                    tempButtonSelectedOptions.remove(button.getText().toString());
                                    if(button.getText().toString().equals("Red Zone")){
                                        riskFactor=riskFactor-3;
                                        allButtonInstances.get("Orange Zone").setEnabled(true);
                                        allButtonInstances.get("Green Zone").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Orange Zone")){
                                        riskFactor=riskFactor-1;
                                        allButtonInstances.get("Red Zone").setEnabled(true);
                                        allButtonInstances.get("Green Zone").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Green Zone")){

                                        allButtonInstances.get("Red Zone").setEnabled(true);
                                        allButtonInstances.get("Orange Zone").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Smoker")){
                                        riskFactor=riskFactor-1;
                                        allButtonInstances.get("Non Smoker").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Non Smoker")){
                                        allButtonInstances.get("Smoker").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Cough")){
                                        riskFactor=riskFactor-1;
                                        tempCounter1--;
                                        if(tempCounter1==0)
                                            allButtonInstances.get("None of the above").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Fever")){
                                        riskFactor=riskFactor-1;
                                        tempCounter1--;
                                        if(tempCounter1==0)
                                            allButtonInstances.get("None of the above").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("Difficulty in breathing")){
                                        riskFactor=riskFactor-1;
                                        tempCounter1--;
                                        if(tempCounter1==0)
                                            allButtonInstances.get("None of the above").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Headache")){
                                        riskFactor=riskFactor-1;
                                        tempCounter1--;
                                        if(tempCounter1==0)
                                            allButtonInstances.get("None of the above").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("None of the above")){

                                        allButtonInstances.get("Headache").setEnabled(true);
                                        allButtonInstances.get("Fever").setEnabled(true);
                                        allButtonInstances.get("Cough").setEnabled(true);
                                        allButtonInstances.get("Difficulty in breathing").setEnabled(true);

                                    }
                                    //askjdhashda
                                    if(button.getText().toString().equals("Struggling for breath even when resting")){
                                        riskFactor=riskFactor-2;
                                        tempCounter5--;
                                        if(tempCounter5==0)
                                            allButtonInstances.get("I am experiencing none of these symptoms").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("Feeling about to collapse everytime I stand/sit up")){
                                        riskFactor=riskFactor-2;
                                        tempCounter5--;
                                        if(tempCounter5==0)
                                            allButtonInstances.get("I am experiencing none of these symptoms").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("I am experiencing none of these symptoms")){

                                        allButtonInstances.get("Struggling for breath even when resting").setEnabled(true);
                                        allButtonInstances.get("Feeling about to collapse everytime I stand/sit up").setEnabled(true);


                                    }

                                    if(button.getText().toString().equals("Diabetes")){
                                        riskFactor=riskFactor-1;
                                        tempCounter2--;
                                        if(tempCounter2==0)
                                            allButtonInstances.get("None").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("Hypertension")){
                                        riskFactor=riskFactor-1;
                                        tempCounter2--;
                                        if(tempCounter2==0)
                                            allButtonInstances.get("None").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("Heart Disease")){
                                        riskFactor=riskFactor-1;
                                        tempCounter2--;
                                        if(tempCounter2==0)
                                            allButtonInstances.get("None").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("Lung Disease")){
                                        riskFactor=riskFactor-1;
                                        tempCounter2--;
                                        if(tempCounter2==0)
                                            allButtonInstances.get("None").setEnabled(true);
                                    }

                                    if(button.getText().toString().equals("None")){

                                        allButtonInstances.get("Lung Disease").setEnabled(true);
                                        allButtonInstances.get("Heart Disease").setEnabled(true);
                                        allButtonInstances.get("Hypertension").setEnabled(true);
                                        allButtonInstances.get("Diabetes").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("I am a doctor/Health Care Worker")){
                                        riskFactor=riskFactor-3;
                                        tempCounter3--;
                                        if(tempCounter3==0)
                                            allButtonInstances.get("None of these descriptions applies to me").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("Living with Health Care Worker")){
                                        riskFactor=riskFactor-2;
                                        tempCounter3--;
                                        if(tempCounter3==0)
                                            allButtonInstances.get("None of these descriptions applies to me").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("None of these descriptions applies to me")){

                                        allButtonInstances.get("Living with Health Care Worker").setEnabled(true);
                                        allButtonInstances.get("I am a doctor/Health Care Worker").setEnabled(true);

                                    }
                                    if(button.getText().toString().equals("I have Travellled Internationally in the last One month")){
                                        riskFactor=riskFactor-2;
                                        tempCounter4--;
                                        if(tempCounter4==0)
                                            allButtonInstances.get("None of these described situations applies to me currently").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("Interacted with someone who tested positively for Covid 19")){
                                        riskFactor=riskFactor-3;
                                        tempCounter4--;
                                        if(tempCounter4==0)
                                            allButtonInstances.get("None of these described situations applies to me currently").setEnabled(true);
                                    }
                                    if(button.getText().toString().equals("None of these described situations applies to me currently")){

                                        allButtonInstances.get("I have Travellled Internationally in the last One month").setEnabled(true);
                                        allButtonInstances.get("Interacted with someone who tested positively for Covid 19").setEnabled(true);

                                    }


                                }

                            }
                        });

                        button.setLayoutParams(layoutParams1);



                    }
                }
            }
            mainButtonLayout.addView(childLayout);
            mainButtonLayout.addView(nextLayout);
        }

        mainLayout.addView(mainButtonLayout);
        scroll.scrollTo(0, scroll.getBottom());

    }

    ArrayList<String> getOp(int i){
        return options.get(i);
    }

    void addOp(ArrayList<String> op){
        options.add(op);
    }

    String getQues(int i){
        return questions.get(i);

    }
}