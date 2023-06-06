
package com.cdac.uphmis;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class RegistrationSummaryActivity extends AppCompatActivity {
    private TextView tvRegistrationDetails;
    private ImageView imgQRCode;
    private String crno = "";
    private TextView tvCrno;


    private Button btnDone;
    private TextView btnSavereceipt;
    private TextView tvNdhmId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_summary);
        
        initializeViews();
        ActivityCompat.requestPermissions(RegistrationSummaryActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                1);


        ManagingSharedData msd = new ManagingSharedData(RegistrationSummaryActivity.this);
        String name = "",  age = "", gender = "",hospCode="",ndhmId="";
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            crno = intent.getStringExtra("crno");
            gender = intent.getStringExtra("gender");
            age = intent.getStringExtra("age");
            hospCode = intent.getStringExtra("hospCode");
            ndhmId = intent.getStringExtra("ndhmId");

            if (!ndhmId.isEmpty())
            tvNdhmId.setText("ABHA : "+ndhmId);
        }
        if (msd.getPatientDetails().getPatHealthId() == null||msd.getPatientDetails().getPatHealthId().equals("")) {

        }
        String barcodeContents = "{\n" +
                "\t\"name\": \"" + name + "\",\n" +
                "\t\"age\": \"" + age + "\",\n" +
                "\t\"Gender\": \"" + gender + "\",\n" +
                "\t\"crno\": \"" + crno + "\",\n" +
                "\t\"hospCode\": \"" + hospCode + "\"\n" +
                "}";

//         String hospName=msd.getHospName();
//         if (hospName==null)

             String hospName=getResources().getString(R.string.hosp_name);


        String message = name + " (" + age + "/" + gender + ") is successfully registered at  "+hospName+".\nYour CR Number is " ;
        tvRegistrationDetails.setText(message);
        tvCrno.setText("CRN: "+crno);
        generateQrCode(barcodeContents);
    }

    private void initializeViews() {
        tvRegistrationDetails = findViewById(R.id.tv_registration_details);
        tvNdhmId = findViewById(R.id.tv_ndhm_id);
        imgQRCode = findViewById(R.id.img_qr_code);
        tvCrno=findViewById(R.id.tv_crno);
        btnSavereceipt=findViewById(R.id.btn_save_receipt);
        btnDone=findViewById(R.id.btn_done);
    }

    private void generateQrCode(String barcodeContents) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            Map<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = writer.encode(barcodeContents, BarcodeFormat.QR_CODE, 512, 512, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imgQRCode.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void btnDone(View view) {
        ManagingSharedData msd = new ManagingSharedData(RegistrationSummaryActivity.this);
        if (msd.getWhichModuleToLogin().equalsIgnoreCase("healthworkerlogin")) {
            Intent intent = new Intent(RegistrationSummaryActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(RegistrationSummaryActivity.this, PatientDrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void btnSavereceipt(View view) {
        takeScreenshot();
    }

    private void takeScreenshot() {
        try {

            btnSavereceipt.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);



            // image naming and path  to include sd card  appending name you choose for file
            String mPath = getFilesDir().toString() + "/" + crno + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();



            MediaScannerConnection.scanFile(this, new String[] { imageFile.getPath() }, new String[] { "image/jpeg" }, null);
            Toast.makeText(this, "QR Code saved to gallery.", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            Toast.makeText(this, "Error while downloading receipt.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        btnSavereceipt.setVisibility(View.VISIBLE);
        btnDone.setVisibility(View.VISIBLE);
    }


}