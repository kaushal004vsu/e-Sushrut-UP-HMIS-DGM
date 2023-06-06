package com.cdac.uphmis.LabReports;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.ScreeningActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.pdfview.PDFView;
//import com.pdfview.PDFView;

import java.io.File;

public class ViewPdfLabReportActivity extends AppCompatActivity {
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf_lab_report);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if( getIntent().getExtras() != null)
        {
             filePath = intent.getStringExtra("imagepath");
            File file = new File(filePath);
            PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
            pdfView.fromFile(file).show();


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
//                AppUtilityFunctions.shareApp(ViewPdfLabReportActivity.this);
                try {
                    if (filePath != null || !filePath.isEmpty()) {
                        shareFile(filePath);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.share_file), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    private void shareFile(String myFilePath)
    {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(myFilePath);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File file = new File(myFilePath);
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File..");
                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            } else {
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(myFilePath));
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File..");
                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }





        }
    }
}