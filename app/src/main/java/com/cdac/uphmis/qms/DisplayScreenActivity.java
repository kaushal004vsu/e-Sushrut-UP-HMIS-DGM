package com.cdac.uphmis.qms;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ServiceUrl;

public class DisplayScreenActivity extends AppCompatActivity {
    private WebView wb;
    private String url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);
        
        Intent intent=getIntent();
        if (intent.getExtras()!=null)
        {
            TokenListDetails tokenListDetails = (TokenListDetails) intent.getSerializableExtra("tokenDetails");
            String counter=tokenListDetails.getCOUNTER_NO();
            String hospCode=tokenListDetails.getHOSP_CODE();
            String serviceId=tokenListDetails.getSERVICE_ID();
            url= ServiceUrl.ip+"HISServices/jsp/MobileTokenDisplay.jsp?counter="+counter+"&hosp_code="+hospCode+"&service_id="+serviceId;
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayScreenActivity.this);
                builder.setMessage("Confirm to proceed.");
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
//                        startActivity(new Intent(DoctorDeskActivity.this, DoctorDrawerHomeActivity.class));
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
                // progressBar.setVisibility(View.GONE);
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
}
