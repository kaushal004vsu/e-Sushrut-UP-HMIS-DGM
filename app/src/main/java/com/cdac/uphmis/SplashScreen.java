package com.cdac.uphmis;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.onboarding.OnBoardingActivity;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class SplashScreen extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 3000;
    //    ImageView imgBackground;
    ManagingSharedData msd;
    TextView appname;
    ImageView logo_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        msd = new ManagingSharedData(SplashScreen.this);

        langaugeSharedPref();
        themeSharedPref();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appname = findViewById(R.id.appname);
        logo_iv = findViewById(R.id.logo_iv);

        PackageInfo pInfo = null;
        try {
            pInfo = SplashScreen.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            TextView tvVersionName = findViewById(R.id.tv_version_name);
            tvVersionName.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkUpdate();
    }

    private void setLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    void langaugeSharedPref(){
        // Log.d("darkMode1",msd.getLanguageFlag());
        if (msd.getLanguageFlag() != null) {
            if (msd.getLanguageFlag().equals("en")) {
                setLocale("hi");
            } else {
                setLocale("en");
            }
        }else {
            msd.setLangaugeFlag("hi");
        }
    }
    void themeSharedPref() {
        //Log.d("darkMode2",msd.getDarkMode());
        if (msd.getDarkMode() != null) {
            if (msd.getDarkMode().equals("yes")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    void checkUpdate() {
        Log.i("TAG", "checkUpdate: "+ServiceUrl.checkUpdateurl+"p");
        // requestQueue = Volley.newRequestQueue(this);
        ArrayList<String> fetaureIdArrayList = new ArrayList<>();
//        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.checkUpdateurl + "p", response -> {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.ip+"HISServices/service/"+ServiceUrl.checkUpdateGenericurl + "p", response -> {

            Log.i("response is ", "aaaa" + response);

            try {

                JSONObject jsonObj = new JSONObject(response);

                /*String appId = jsonObj.getString("appId");
                String hospName = jsonObj.getString("hospName");
                String appName = jsonObj.getString("appName");
                String appUpdatedOn = jsonObj.getString("appUpdatedOn");
                String appPlatform = jsonObj.getString("appPlatform");*/
                String appVersion = jsonObj.getString("appVersion");
                String forceUpdate = jsonObj.getString("forceUpdate");


                PackageInfo pInfo = SplashScreen.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                int code = pInfo.versionCode;
                Log.i("name", "onResponse: " + version);
                Log.i("code", "onResponse: " + code);

                boolean showOnboardingScreen = true;
                if (jsonObj.has("features")) {
                    JSONArray jsonArray = jsonObj.getJSONArray("features");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        if (c.has("featureId")) {
                            String featureId = c.getString("featureId");

                            fetaureIdArrayList.add(featureId);

                            if (featureId.equalsIgnoreCase("OB")) {
                                showOnboardingScreen = false;
                                // break;
                            }
                        }
                    }
                }

//                ManagingSharedData msd = new ManagingSharedData(SplashScreen.this);

                if (!appVersion.equalsIgnoreCase(version)) {

                    if (forceUpdate.equalsIgnoreCase("1")) {

                        appUpdateDialog(getString(R.string.update_conti), 1, showOnboardingScreen, fetaureIdArrayList);

                    } else {

                        appUpdateDialog(getString(R.string.update_google_play_store), 0, showOnboardingScreen, fetaureIdArrayList);

                    }
                } else {
                    boolean finalShowOnboardingScreen = showOnboardingScreen;
                    new Handler().postDelayed(() -> {
                        String moduleName = msd.getWhichModuleToLogin();
                        if (moduleName == null) {
                            if (finalShowOnboardingScreen) {
                                Intent intent = new Intent(SplashScreen.this, OnBoardingActivity.class);
                                intent.putExtra("fetaureIdArrayList", fetaureIdArrayList);
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(SplashScreen.this, LoginMainScreenActivity.class));
                            }
                        } else {
                            Intent i = new Intent(SplashScreen.this, LoginMainScreenActivity.class);
                            startActivity(i);
                        }

                        // close this activity
                        finish();
                    }, SPLASH_TIME_OUT);
                }

            } catch (final Exception e) {
                Log.i("error", "onResponse: " + e);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            try {
                onError(handleExceptions(error));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void onError(String message) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.no_internet_connection)
                .setTitle("Something went wrong.")
                .setMessage(message)
                .setNegativeButton(getString(R.string.exit), v -> {

                    SplashScreen.this.finish();
                    System.exit(0);
                })
                .setPositiveButton(getString(R.string.retry), v -> {
                    finish();
                    startActivity(getIntent());
                }).show();
    }


    private void appUpdateDialog(String message, int isForceUpdate, boolean showOnboardingScreen, ArrayList<String> fetaureIdArrayList) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.app_update_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        TextView tvExit = dialog.findViewById(R.id.tv_exit);
        TextView tvUpdate = dialog.findViewById(R.id.tv_update);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        if (isForceUpdate != 1) {
            tvExit.setText(getString(R.string.skip));

        }
        tvExit.setOnClickListener(view -> {
            //themeSharedPref();
            if (isForceUpdate == 1) {
                finish();
            } else {
//                ManagingSharedData msd = new ManagingSharedData(SplashScreen.this);
                String moduleName = msd.getWhichModuleToLogin();
                if (moduleName == null) {
                    if (showOnboardingScreen) {
                        Intent intent = new Intent(SplashScreen.this, OnBoardingActivity.class);
                        intent.putExtra("fetaureIdArrayList", fetaureIdArrayList);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(SplashScreen.this, LoginMainScreenActivity.class));
                    }
                } else {
                    Intent i = new Intent(SplashScreen.this, LoginMainScreenActivity.class);
                    startActivity(i);
                }
                finish();
            }
            dialog.dismiss();

        });
        tvUpdate.setOnClickListener(view -> {
            play();
            dialog.dismiss();
            finish();
        });


        dialog.show();
    }

    public void play() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
        startActivity(intent);
    }

    private String handleExceptions(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            return "Unable to connect.";
        } else if (error instanceof AuthFailureError) {
            return "Authentication Error.";
        } else if (error instanceof ServerError) {
            return "App server is presently unavailable, please try after some time.";
        } else if (error instanceof NetworkError) {
            return "Network Error.";
        } else if (error instanceof ParseError) {
            return "Data Parsing Error.";
        }
        return "Unable to connect.";

    }

}
