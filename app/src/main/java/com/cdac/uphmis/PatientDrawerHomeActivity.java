package com.cdac.uphmis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.covid19.ChatBotActivity;
import com.cdac.uphmis.home.PatientHomeFragment;
import com.cdac.uphmis.home.language.LocaleHelper;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.ndhm.NDHMGenerateOTPActivity;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NetworkStats;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import com.cdac.uphmis.fragments.AboutusFragment;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION.SDK_INT;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import net.bohush.geometricprogressview.GeometricProgressView;

public class PatientDrawerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ManagingSharedData msd;
    TextView tvName;
    CircleImageView image;
    NavigationView navigationView;
    Menu menu;
    public static final int OVERLAY_PERMISSION_REQUEST_CODE = (int) (Math.random() * Short.MAX_VALUE);
    private GeometricProgressView progressView;
    private SwitchCompat drawerSwitch;
    TextView tv_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_drawer_home);
        msd = new ManagingSharedData(this);

        progressView = findViewById(R.id.progress_view);
        progressView.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        navigationView = findViewById(R.id.patient_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);

        askPermission();
        drawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.theme).getActionView();
        checkInitialTheme();
        getCurrentTheme();

        if (msd.getLanguageFlag().equals("en")) {
            setLocale("hi");
        } else {
            setLocale("en");
        }
        tv_toolbar.setText(getString(R.string.up_hmis));
        menu.findItem(R.id.patient_home).setTitle(R.string.home);
        menu.findItem(R.id.my_account).setTitle(R.string.my_account);
        menu.findItem(R.id.aboutus).setTitle(R.string.about_us);
        menu.findItem(R.id.language).setTitle(R.string.language_name);
        menu.findItem(R.id.theme).setTitle(R.string.dark_mode);
        menu.findItem(R.id.self_assess).setTitle(R.string.self_assess);
        menu.findItem(R.id.switch_user).setTitle(R.string.switch_user);
        menu.findItem(R.id.logout).setTitle(R.string.logout);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
////                Log.i("appearontop", "onCreate: ");
//                String message = "Permission could be given on <b>Settings->Apps->"+getString(R.string.app_name)+"->App info>Appear on top</b> should be <b>ON</b> , click ok to proceed";
//                displayOverAppsPermission(message);
//            }
//        }

        DrawerLayout drawer = findViewById(R.id.patient_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView navUsername = headerView.findViewById(R.id.header_crno);
        TextView tvMobileNumber = headerView.findViewById(R.id.tv_mobile_no);
        tvName = headerView.findViewById(R.id.tv_name);
        tvName.setText("");
        if (msd.getPatientDetails() != null) {
            navUsername.setText(getString(R.string.crno_nav) + msd.getCrNo());
        }
        tvMobileNumber.setText(getString(R.string.nav_mobile_no) + msd.getPatientDetails().getMobileNo().substring(6, 10));
        image = navigationView.getHeaderView(0).findViewById(R.id.profile_nav_header);

        try {
            File f = new File(getFilesDir(), "/userpicture.jpg");
            Bitmap bmp = AppUtilityFunctions.changeOrientation(f.getAbsolutePath().toString());
            if (bmp != null) {
                image.setImageBitmap(bmp);
            } else {
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.defaultiocon);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PatientHomeFragment patientHomeFragment = new PatientHomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, patientHomeFragment);
        transaction.commit();
        try {
            if (msd.getPatientDetails().getCrno() != null)
                getPatientProfileImage();
        } catch (Exception ex) {
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(PatientDrawerHomeActivity.this,
                new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA, READ_PHONE_STATE}, 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.patient_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.action_network_stats);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                AppUtilityFunctions.shareApp(PatientDrawerHomeActivity.this);
                return true;
            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;
            case R.id.action_network_stats:
                if (checkSelfPermission(READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please give phone state permission.", Toast.LENGTH_SHORT).show();
                } else {
                    if (SDK_INT >= Build.VERSION_CODES.N) {
                        NetworkStats.appUpdateDialog(this);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.patient_home) {
//            recreate();
            Fragment patientHomeFragment = new PatientHomeFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, patientHomeFragment);
            transaction.commit();
        } else if (id == R.id.my_account) {
            startActivity(new Intent(PatientDrawerHomeActivity.this, PatientProfileActivity.class));
        } else if (id == R.id.aboutus) {
            Fragment aboutusFragment = new AboutusFragment();
            FragmentTransaction aboutusTransaction = getFragmentManager().beginTransaction();
            aboutusTransaction.replace(R.id.container, aboutusFragment);
            aboutusTransaction.addToBackStack(null);
            aboutusTransaction.commit();
        } else if (id == R.id.language) {
            getCurrentLanguage();
            //  recreate();
        } else if (id == R.id.theme) {
            if (drawerSwitch.isChecked()) {
                drawerSwitch.setChecked(false);
            } else {
                drawerSwitch.setChecked(true);
            }
        } else if (id == R.id.self_assess) {
            startActivity(new Intent(this, ChatBotActivity.class));
        } else if (id == R.id.switch_user) {
            getCrList();
        } else if (id == R.id.logout) {
            String msg = msd.logOut();
            Toast.makeText(PatientDrawerHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginMainScreenActivity.class));
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.patient_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setLocale(String locale) {
        Resources resources = PatientDrawerHomeActivity.this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);

       /* Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = PatientDrawerHomeActivity.this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());*/
    }

    private void getCrList() {
        progressView.setVisibility(View.VISIBLE);
        final ArrayList<PatientDetails> patientDetailsList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPatientDetails + msd.getPatientDetails().getMobileNo(), response -> {
            Log.i("response", "onResponse: " + response);
            progressView.setVisibility(View.GONE);
            try {
                response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                if (jsonArray.length() > 1) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String umidData = jsonArray.getJSONObject(i).optString("UMID_DATA");
                        if (umidData.isEmpty()) {
                            jsonArray.getJSONObject(i).remove("UMID_DATA");
                        } else {
                            JSONObject jsonObject1 = new JSONObject(umidData);
                            jsonArray.getJSONObject(i).put("UMID_DATA", jsonObject1);
                        }
                        PatientDetails patientDetails = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), PatientDetails.class);
                        patientDetailsList.add(patientDetails);
                    }
                    msd.setWhichModuleToLogin("patientlogin");
                    List<PatientDetails> textList = new ArrayList<>(patientDetailsList);
                    String jsonCrList = gson.toJson(textList);
                    msd.setCrList(jsonCrList);

                    Intent intent = new Intent(this, SelectCrActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                   // finish();
                } else {
                    Toast.makeText(this, "No more patients found.", Toast.LENGTH_SHORT).show();
                }

            } catch (final Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                progressView.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
                , error -> {
            Log.i("error", "onErrorResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, this);
            progressView.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void checkInitialTheme() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                drawerSwitch.setChecked(true);
                msd.setDarkMode("yes");
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                drawerSwitch.setChecked(false);
                msd.setDarkMode("no");
                break;
        }
    }

    private void getCurrentLanguage() {
        final PatientHomeFragment[] patientHomeFragment = {null};
        final FragmentTransaction[] transaction = new FragmentTransaction[1];

        if (msd.getLanguageFlag().equals("en")) {
            setLocale("en");
            msd.setLangaugeFlag("hi");
        } else {
            setLocale("hi");
            msd.setLangaugeFlag("en");

        }
        tv_toolbar.setText(getString(R.string.up_hmis));
        menu.findItem(R.id.patient_home).setTitle(R.string.home);
        menu.findItem(R.id.my_account).setTitle(R.string.my_account);
        menu.findItem(R.id.aboutus).setTitle(R.string.about_us);
        menu.findItem(R.id.language).setTitle(R.string.language_name);
        menu.findItem(R.id.theme).setTitle(R.string.dark_mode);
        menu.findItem(R.id.self_assess).setTitle(R.string.self_assess);
        menu.findItem(R.id.switch_user).setTitle(R.string.switch_user);
        menu.findItem(R.id.logout).setTitle(R.string.logout);
        patientHomeFragment[0] = new PatientHomeFragment();
        transaction[0] = getFragmentManager().beginTransaction();
        transaction[0].replace(R.id.container, patientHomeFragment[0]);
        transaction[0].commit();
    }

    private void getCurrentTheme() {
        final PatientHomeFragment[] patientHomeFragment = {null};
        final FragmentTransaction[] transaction = new FragmentTransaction[1];

        drawerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            patientHomeFragment[0] = new PatientHomeFragment();
            transaction[0] = getFragmentManager().beginTransaction();
            transaction[0].replace(R.id.container, patientHomeFragment[0]);
            transaction[0].commit();
        });



       /* PatientHomeFragment patientHomeFragment=null;
        FragmentTransaction transaction;
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                patientHomeFragment = new PatientHomeFragment();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, patientHomeFragment);
                transaction.commit();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                patientHomeFragment = new PatientHomeFragment();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, patientHomeFragment);
                transaction.commit();
                break;
        }*/
    }

    private void displayOverAppsPermission(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.draw_over_model);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        TextView tvNotNow = dialog.findViewById(R.id.tv_not_now);
        TextView tvAllow = dialog.findViewById(R.id.tv_allow);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        tvMessage.setText(Html.fromHtml(message));

        tvAllow.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        });
        tvNotNow.setOnClickListener(view -> dialog.dismiss());
        // dialog.dismiss();
        dialog.show();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            // do something when the button is clicked
            // do something when the button is clicked
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", (arg0, arg1) -> {
                        finishAffinity();
                        System.exit(0);
                        //close();
                    })
                    .setNegativeButton("No", (arg0, arg1) -> {
                    })
                    .show();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    private void getPatientProfileImage() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.testurl + "AppOpdService/getImageByCrNoAndUmid?crNo=" + msd.getPatientDetails().getCrno() + "&episodeCode=&hospCode=&seatid=&umid=", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("resopnse", "onResponse: ");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("profilePicBase64");
                    if (status.equalsIgnoreCase("1")) {
                        if (jsonArray.length() > 0) {
                            String profileBase64 = jsonArray.getJSONObject(0).optString("IMAGEDATA");
                            byte[] decodedString = Base64.decode(profileBase64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            image.setImageBitmap(decodedByte);
                        }else
                        {
                            if (msd.getPatientDetails().getGender().equalsIgnoreCase("F")) {
                                image.setImageDrawable(getDrawable(R.drawable.woman));
                            } else {
                                image.setImageDrawable(getDrawable(R.drawable.man));
                            }
                        }


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
               /* try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        byte[] decodedString = Base64.decode(jsonObject.getString("profilePicBase64"), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        image.setImageBitmap(decodedByte);

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtilityFunctions.handleExceptions(error, PatientDrawerHomeActivity.this);
                Log.i("error", "onResponse: ");
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == PERMISSION_REQUEST_CODE) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE
                && canRequestOverlayPermission()) {
            if (Settings.canDrawOverlays(this)) {

                return;
            }

            throw new RuntimeException("Overlay permission is required when running in Debug mode.");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean canRequestOverlayPermission() {
        return
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M;
    }
}
