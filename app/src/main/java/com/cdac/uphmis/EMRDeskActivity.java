package com.cdac.uphmis;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.ServiceUrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class EMRDeskActivity extends AppCompatActivity {
    WebView wb;
    ManagingSharedData msd;
    String url = "";
    ProgressBar progressBar;
    private boolean doubleBackToExitPressedOnce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emrdesk);
        
        progressBar=findViewById(R.id.pb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        msd= new ManagingSharedData(EMRDeskActivity.this);
        String q= msd.getUsername();
        try {
            q= URLEncoder.encode(q, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ManagingSharedData msd=new ManagingSharedData(this);
        url= ServiceUrl.emrDesk+msd.getCrNo();
        Log.i("url", "onCreate: "+url);

        wb = findViewById(R.id.webview);
        setWebView();


    }



    private void setWebView() {
        Log.i("hello", "setWebView: ");

        wb.getSettings().setDomStorageEnabled(true);
        wb.getSettings().setSaveFormData(true);
        wb.getSettings().setAllowContentAccess(true);
        wb.getSettings().setAllowFileAccess(true);
        wb.getSettings().setAllowFileAccessFromFileURLs(true);
        wb.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wb.getSettings().setSupportZoom(true);


        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//                handler.proceed(); // Ignore SSL certificate errors
                AlertDialog.Builder builder = new AlertDialog.Builder(EMRDeskActivity.this);
                builder.setMessage("Confirm to proceed.");
                builder.setPositiveButton(getString(R.string.continue_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
//                        startActivity(new Intent(DoctorDeskActivity.this, DoctorDrawerHomeActivity.class));
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        wb.getSettings().setSaveFormData(true);
        wb.getSettings().setSupportZoom(false);
        wb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);


//        wb.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                myRequest = request;
//
//                for (String permission : request.getResources()) {
//                    switch (permission) {
//                        case "android.webkit.resource.AUDIO_CAPTURE": {
//                            askForPermission(request.getOrigin().toString(), Manifest.permission.RECORD_AUDIO, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
//                            break;
//                        }
//                    }
//                }
//            }
//
//        });

        Log.i("load url", "setWebView: "+url);
        wb.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, "Back button has been disabled due to security reason.", Toast.LENGTH_SHORT).show();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = getFilesDir().toString() + "/" + "emr_screenshot" + ".jpg";

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
            Toast.makeText(this, "Screenshot saved successfully.", Toast.LENGTH_SHORT).show();
//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public void takeScreenshot(View view) {
        takeScreenshot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EMRDeskActivity.this, DoctorDrawerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}