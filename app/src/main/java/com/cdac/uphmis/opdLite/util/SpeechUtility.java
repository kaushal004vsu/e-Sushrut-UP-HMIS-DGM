package com.cdac.uphmis.opdLite.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.material.chip.Chip;

import java.util.Locale;
import java.util.Random;

public class SpeechUtility {
    public static void getSpeechInput(Context context, ActivityResultLauncher<Intent> SpeechResultLauncher) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            SpeechResultLauncher.launch(intent);
        } else {
            Toast.makeText(context, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    public static Chip createChip(Context context) {
        Chip chip = new Chip(context);

        //   chip.setChipCornerRadius(50.0f);
        Random rnd = new Random();

        int color = Color.argb(50, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        chip.setChipBackgroundColor(ColorStateList.valueOf(color));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);
        chip.setLayoutParams(params);

        return chip;
    }
  public static String replaceMultiple(String baseString, String... replaceParts) {
        for (String s : replaceParts) {
            baseString = baseString.replaceAll(s, "");
        }
        return baseString;
    }





}
