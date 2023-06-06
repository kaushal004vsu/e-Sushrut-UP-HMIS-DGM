package com.cdac.uphmis;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cdac.uphmis.fragments.AboutusFragment;
import com.cdac.uphmis.fragments.DoctorHomeFragment;
import com.cdac.uphmis.fragments.DoctorNewCr;
import com.cdac.uphmis.fragments.DoctorProfileFragment;
import com.cdac.uphmis.home.PatientHomeFragment;
import com.cdac.uphmis.util.ManagingSharedData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;


public class DoctorDrawerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ManagingSharedData msd;
    CircleImageView image;
    private SwitchCompat drawerSwitch;
    static int PERMISSION_REQUEST_CODE = 2296;
    Menu menu;
    TextView tv_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_drawer_home);
        msd = new ManagingSharedData(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        tv_toolbar = findViewById(R.id.tv_toolbar);



        //TODO firebase
        if (msd.getToken() == null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(DoctorDrawerHomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String mToken = instanceIdResult.getToken();
                    ManagingSharedData msd = new ManagingSharedData(DoctorDrawerHomeActivity.this);
                    Log.e("Token", mToken);
                    msd.setToken(mToken);
                }
            });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.doctor_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.doctor_nav_view);
        navigationView.setNavigationItemSelectedListener(DoctorDrawerHomeActivity.this);
        View headerView = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        drawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.doctor_theme).getActionView();
        TextView navUsername = headerView.findViewById(R.id.tv_profile_name);
        askPermission();
        checkInitialTheme();
        getCurrentTheme();

        if (msd.getLanguageFlag().equals("en")) {
            setLocale("hi");
        } else {
            setLocale("en");
        }
        tv_toolbar.setText(getString(R.string.up_hmis));
        menu.findItem(R.id.doctor_home).setTitle(R.string.home);
        menu.findItem(R.id.doctor_my_account).setTitle(R.string.my_account);
        menu.findItem(R.id.doctor_aboutus).setTitle(R.string.about_us);
        menu.findItem(R.id.doctor_language).setTitle(R.string.language_name);
        menu.findItem(R.id.doctor_theme).setTitle(R.string.dark_mode);
        menu.findItem(R.id.doctor_logout).setTitle(R.string.logout);
        navUsername.setText(msd.getUsername());


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.doctor_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        image = navigationView.getHeaderView(0).findViewById(R.id.profile_nav_header);
        menu = navigationView.getMenu();
        try {
            File f = new File(getFilesDir().toString(), "/doctorpicture.jpg");
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            if (bmp != null) {
                image.setImageBitmap(bmp);
            } else {
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.defaultiocon);
            }
        } catch (Exception ex) {
            Log.i("error", "onCreate: " + ex);
            //Toast.makeText(DoctorDrawerHomeActivity.this, "error:" + ex, Toast.LENGTH_SHORT).show();
        }
//        navigation.setVisibility(View.VISIBLE);
        msd = new ManagingSharedData(this);

        DoctorHomeFragment doctorHomeFragment = new DoctorHomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, doctorHomeFragment);
        transaction.commit();
//        }

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
    private void getCurrentLanguage() {
        final DoctorHomeFragment[] doctorHomeFragments = {null};
        final FragmentTransaction[] transaction = new FragmentTransaction[1];

        if (msd.getLanguageFlag().equals("en")) {
            setLocale("en");
            msd.setLangaugeFlag("hi");
        } else {
            setLocale("hi");
            msd.setLangaugeFlag("en");

        }
        tv_toolbar.setText(getString(R.string.up_hmis));
        menu.findItem(R.id.doctor_home).setTitle(R.string.home);
        menu.findItem(R.id.doctor_my_account).setTitle(R.string.my_account);
        menu.findItem(R.id.doctor_aboutus).setTitle(R.string.about_us);
        menu.findItem(R.id.doctor_language).setTitle(R.string.language_name);
        menu.findItem(R.id.doctor_theme).setTitle(R.string.dark_mode);
        menu.findItem(R.id.doctor_logout).setTitle(R.string.logout);
        doctorHomeFragments[0] = new DoctorHomeFragment();
        transaction[0] = getFragmentManager().beginTransaction();
        transaction[0].replace(R.id.container, doctorHomeFragments[0]);
        transaction[0].commit();
    }

    private void getCurrentTheme() {
        final DoctorHomeFragment[] doctorHomeFragments = {null};
        final FragmentTransaction[] transaction = new FragmentTransaction[1];

        drawerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            doctorHomeFragments[0] = new DoctorHomeFragment();
            transaction[0] = getFragmentManager().beginTransaction();
            transaction[0].replace(R.id.container, doctorHomeFragments[0]);
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
    private void askPermission() {
        ActivityCompat.requestPermissions(DoctorDrawerHomeActivity.this,
                new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA},
                1);

   /* if (SDK_INT >= Build.VERSION_CODES.R) {
        if(!Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
        }
    }*/
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.doctor_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.doctor_home) {
            FragmentManager fm = getFragmentManager(); // or 'getSupportFragmentManager();'
            int count = fm.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fm.popBackStack();
            }
            Fragment doctorHomeFragment = new DoctorHomeFragment();
            FragmentTransaction transactions = getFragmentManager().beginTransaction();
            transactions.replace(R.id.container, doctorHomeFragment);
            transactions.commit();

        }
        if (id == R.id.doctor_my_account) {
            DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, doctorProfileFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.doctor_aboutus) {
            Fragment aboutusFragment = new AboutusFragment();
            FragmentTransaction aboutusTransaction = getFragmentManager().beginTransaction();
            aboutusTransaction.replace(R.id.container, aboutusFragment);
            aboutusTransaction.addToBackStack(null);
            aboutusTransaction.commit();


        } else if (id == R.id.doctor_language) {
            getCurrentLanguage();
            //  recreate();
        } else if (id == R.id.doctor_theme) {
            if (drawerSwitch.isChecked()) {
                drawerSwitch.setChecked(false);
            } else {
                drawerSwitch.setChecked(true);
            }
        }  else if (id == R.id.doctor_logout) {
            String msg = msd.logOut();
            Toast.makeText(DoctorDrawerHomeActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginMainScreenActivity.class));
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.doctor_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // mTextMessage.setText(R.string.title_home);
                    DoctorHomeFragment doctorHomeFragment = new DoctorHomeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, doctorHomeFragment); // give your fragment container id in first parameter
                    //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                    return true;

                case R.id.navigation_cr:
                    // mTextMessage.setText(R.string.title_home);
                    DoctorNewCr doctorNewCr = new DoctorNewCr();
                    FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                    transaction1.replace(R.id.container, doctorNewCr); // give your fragment container id in first parameter
                    //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction1.commit();
                    return true;

            }
            return false;
        }
    };

    public void toolbarHome(View view) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(DoctorDrawerHomeActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
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
//            super.onBackPressed();
            //additional code

            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                            //close();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
        } else {
            getFragmentManager().popBackStack();
        }

    }



}
