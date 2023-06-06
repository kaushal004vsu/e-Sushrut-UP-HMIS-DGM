package com.cdac.uphmis.QMSSlip;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.cdac.uphmis.QMSSlip.model.QMSDetails;
import com.cdac.uphmis.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Arrays;

public class QMSBarCodeActivity extends AppCompatActivity {
    private AppCompatTextView tvCrno, tvPatName, tvDeptUnit, tvQueueNo, tvVisiDate, tvPrintedOn, tvHopitalName,tvUMIDNo,tvGeneratedOn;
    private AppCompatImageView imgBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_m_s_bar_code);
        
        initializeViews();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            QMSDetails qmsDetails = (QMSDetails) intent.getSerializableExtra("qmsDetails");
            Log.i("TAG", "onCreate: " + qmsDetails.getDeptname());
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                int height = displayMetrics.widthPixels%10;
                int width = displayMetrics.widthPixels;

                imgBarCode.setImageBitmap(createBarcodeBitmap(qmsDetails.getPatcrno(), width, 1000));

            } catch (Exception ex) {
                Toast.makeText(this, "Could not generate barcode.", Toast.LENGTH_SHORT).show();
            }
            setData(qmsDetails);
        }
    }
    private void initializeViews() {
        tvCrno = findViewById(R.id.tv_crno);
        tvPatName = findViewById(R.id.tv_pat_name);
        tvDeptUnit = findViewById(R.id.tv_dept_unit);
        tvQueueNo = findViewById(R.id.tv_queue_no);
        tvVisiDate = findViewById(R.id.tv_visit_date);
        tvPrintedOn = findViewById(R.id.tv_printed_on);
        tvHopitalName = findViewById(R.id.tv_hospital_name);
        tvUMIDNo = findViewById(R.id.tv_umid_no);
        imgBarCode = findViewById(R.id.img_bar_code);
        tvGeneratedOn = findViewById(R.id.tv_token_generation);
    }

    private void setData(QMSDetails qmsDetails) {
        tvCrno.setText( qmsDetails.getPatcrno());
        tvPatName.setText(qmsDetails.getPatname() + " (" + qmsDetails.getGendercode() + ")");
        tvDeptUnit.setText("Dept/Unit: " + qmsDetails.getDeptunitname());
        tvQueueNo.setText( qmsDetails.getQueueno());
        tvVisiDate.setText(qmsDetails.getEpisodestartdate());
        tvPrintedOn.setText( qmsDetails.getPrintedon());
        tvHopitalName.setText( qmsDetails.getHospname());
        tvUMIDNo.setText("UMID No: " + qmsDetails.getUmid());
        if (qmsDetails.getEntryDate()!=null)
        tvGeneratedOn.setText("Token Generated on: "+qmsDetails.getEntryDate());

    }
    private Bitmap createBarcodeBitmap(String data, int width, int height) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        String finalData = Uri.encode(data);
        // Use 1 as the height of the matrix as this is a 1D Barcode.
        BitMatrix bm = writer.encode(finalData, BarcodeFormat.CODE_128, width, 1);
        int bmWidth = bm.getWidth();
        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bmWidth; i++) {
            // Paint columns of width 1
            int[] column = new int[height];
            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
            imageBitmap.setPixels(column, 0, 1, i, 0, 1, height);
        }
        return imageBitmap;
    }



}