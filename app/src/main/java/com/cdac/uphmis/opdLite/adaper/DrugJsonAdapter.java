package com.cdac.uphmis.opdLite.adaper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cdac.uphmis.R;
import com.cdac.uphmis.opdLite.model.DrugJsonArray;
import com.cdac.uphmis.opdLite.model.DrugsDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DrugJsonAdapter extends RecyclerView.Adapter<DrugJsonAdapter.viewHolder> {

    Context context;
    ArrayList<DrugJsonArray> arrayList;
    private String episodeCode;
    private String visitNo;


    public DrugJsonAdapter(Context context, ArrayList<DrugJsonArray> arrayList, String episodeCode, String visitNo) {
        this.context = context;
        this.arrayList = arrayList;
        this.episodeCode = episodeCode;
        this.visitNo = visitNo;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.drug_json_row, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, final int position) {

        DrugJsonArray selectedItem = (DrugJsonArray) arrayList.get(position);
        viewHolder.tvDrugName.setText(selectedItem.getDrugName());
        viewHolder.tvDrugfrequency.setText(selectedItem.getFrequencyName());

        viewHolder.tvDrugQuantity.setText("Qty: " + selectedItem.getDrugQuantity());
        if (selectedItem.getDrugInstruction().isEmpty()) {
            viewHolder.tvSpecialInstructions.setVisibility(View.GONE);
        } else {
            viewHolder.tvSpecialInstructions.setVisibility(View.VISIBLE);
        }
        viewHolder.tvSpecialInstructions.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Special Instructions")
                .setMessage(selectedItem.getDrugInstruction())
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show());
        viewHolder.imgDelete.setOnClickListener(v -> removeItem(selectedItem));
        viewHolder.imgEdit.setOnClickListener(v -> ediSelectedItem(selectedItem, selectedItem.getDrugDetails()));


    }

    private void removeItem(DrugJsonArray selectedItem) {
        arrayList.remove(selectedItem);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView tvDrugName, tvDrugfrequency, tvDrugQuantity, tvSpecialInstructions;
        ImageView imgDelete, imgEdit;

        public viewHolder(View itemView) {
            super(itemView);
            tvDrugName = (TextView) itemView.findViewById(R.id.tv_drug_name);
            tvDrugfrequency = (TextView) itemView.findViewById(R.id.tv_drugs_frequency);
            // tvDrugStartDate = (TextView) itemView.findViewById(R.id.tv_drug_start_date);
            tvDrugQuantity = (TextView) itemView.findViewById(R.id.tv_drug_qty);
            tvSpecialInstructions = (TextView) itemView.findViewById(R.id.tv_special_instructions);


            imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_edit);


        }
    }


    private void ediSelectedItem(DrugJsonArray selectedItem, DrugsDetails drugsDetails) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_drugs_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(true);


        TextView tvDrugName = dialog.findViewById(R.id.tv_drug_name);
        TextView tvLetterAvatar = dialog.findViewById(R.id.tv_letter_avatar);
        tvLetterAvatar.setText(drugsDetails.getTypeShortName());

        Button btnAdd = dialog.findViewById(R.id.btn_add);
        Button btnClose = dialog.findViewById(R.id.btn_close);

        tvDrugName.setText(selectedItem.getDrugName());

        EditText tvMorning = dialog.findViewById(R.id.tv_m);
        EditText tvAfternoon = dialog.findViewById(R.id.tv_a);
        EditText tvEvening = dialog.findViewById(R.id.tv_e);
        EditText tvNight = dialog.findViewById(R.id.tv_n);
        EditText tvDays = dialog.findViewById(R.id.tv_days);
        tvDays.setText(selectedItem.getDrugDays());

       /* tvMorning.setOnClickListener(v -> getDoseDialog("Morning Dose", tvMorning));
        tvAfternoon.setOnClickListener(v -> getDoseDialog("Afternoon Dose", tvAfternoon));
        tvEvening.setOnClickListener(v -> getDoseDialog("Evening Dose", tvEvening));
        tvNight.setOnClickListener(v -> getDoseDialog("Night Dose", tvNight));*/

        tvMorning.setText(selectedItem.getMoring());
        tvAfternoon.setText(selectedItem.getAfternoon());
        tvEvening.setText(selectedItem.getEvening());
        tvNight.setText(selectedItem.getNight());

        MultiAutoCompleteTextView auto_compplete_special_condition = dialog.findViewById(R.id.auto_compplete_special_condition);
        auto_compplete_special_condition.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        auto_compplete_special_condition.setText(selectedItem.getDrugInstruction());

        getSpecialInstructions(auto_compplete_special_condition);
        auto_compplete_special_condition.setOnItemClickListener((parent, arg1, position, arg3) -> {
            //   Object item = parent.getItemAtPosition(position);

        });

        FloatingActionButton addDrugsSpeechInput = dialog.findViewById(R.id.fab_add_drugs);
        addDrugsSpeechInput.setVisibility(View.GONE);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);


        btnClose.setOnClickListener(v -> dialog.dismiss());


        btnAdd.setOnClickListener(v -> {

            float morning = 0, afternoon = 0, evening = 0, night = 0;


            if (!tvMorning.getText().toString().isEmpty()) {
                morning = Float.parseFloat(tvMorning.getText().toString());
            }

            if (!tvAfternoon.getText().toString().isEmpty()) {
                afternoon = Float.parseFloat(tvAfternoon.getText().toString());
            }


            if (!tvEvening.getText().toString().isEmpty()) {
                evening = Float.parseFloat(tvEvening.getText().toString());
            }


            if (!tvNight.getText().toString().isEmpty()) {
                night = Float.parseFloat(tvNight.getText().toString());
            }
            int days = 0;
            if (!tvDays.getText().toString().isEmpty()) {
                days = Integer.parseInt(tvDays.getText().toString());
            }
            final float[] qty = {(morning + afternoon + evening + night) * days};

            RadioButton radioButtonDays = dialog.findViewById(R.id.radio_days);
            RadioButton radioButtonWeeks = dialog.findViewById(R.id.radio_weeks);
            RadioButton radioButtonMonths = dialog.findViewById(R.id.radio_months);
            String selectedTimePeriod = "Days";
            if (radioButtonDays.isChecked()) {
                qty[0] = qty[0];
                selectedTimePeriod = "Days";
            } else if (radioButtonWeeks.isChecked()) {
                qty[0] = qty[0] * 7;
                selectedTimePeriod = "Weeks";
            } else if (radioButtonMonths.isChecked()) {
                qty[0] = qty[0] * 30;
                selectedTimePeriod = "Months";
            }

            int quantity = (int) Math.ceil(qty[0]);
            arrayList.remove(selectedItem);

            String strMorning = String.valueOf(morning).replaceFirst("\\.0+$", "");
            String strAfternoon = String.valueOf(afternoon).replaceFirst("\\.0+$", "");
            String strEvening = String.valueOf(evening).replaceFirst("\\.0+$", "");
            String strNight = String.valueOf(night).replaceFirst("\\.0+$", "");
            arrayList.add(new DrugJsonArray("0", selectedItem.getDrugName(), selectedItem.getDrugCode(), "--", "0", "" + strMorning + " - " + strAfternoon + " - " + strEvening + " - " + strNight + " X " + days + " " + selectedTimePeriod, "0", formattedDate, String.valueOf(days), String.valueOf(quantity), auto_compplete_special_condition.getText().toString(), drugsDetails, strMorning, strAfternoon, strEvening, strNight, episodeCode, visitNo));
            notifyDataSetChanged();
            dialog.dismiss();

        });


        dialog.show();
    }


    private void getSpecialInstructions(MultiAutoCompleteTextView auto_compplete_special_condition) {
        ArrayList<String> specialInstructionStringArrayList = new ArrayList<>();
        specialInstructionStringArrayList.add("After Breakfast");
        specialInstructionStringArrayList.add("After Dinner");
        specialInstructionStringArrayList.add("After Food");
        specialInstructionStringArrayList.add("After Lunch");
        specialInstructionStringArrayList.add("Alternate Day");
        specialInstructionStringArrayList.add("Apply Externally");
        specialInstructionStringArrayList.add("Apply Internally");
        specialInstructionStringArrayList.add("At Bed Time");
        specialInstructionStringArrayList.add("Before Breakfast");

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (context, android.R.layout.select_dialog_item, specialInstructionStringArrayList);

        auto_compplete_special_condition.setThreshold(1);
        auto_compplete_special_condition.setAdapter(adapter);
    }
}