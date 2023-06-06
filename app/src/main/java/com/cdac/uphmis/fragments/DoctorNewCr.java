package com.cdac.uphmis.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.EMRDeskActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.prescriptionscanner.fragments.PatientFragment;
import com.cdac.uphmis.prescriptionscanner.fragments.ViewPrescriptionSelectEpisodeFragment;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class DoctorNewCr extends Fragment {
    ManagingSharedData msd;
    EditText edtCrNo;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    int isFlashOn = 0;
    String strcrno;
    ImageButton btnFlash;

    private int navigateTo = 0;

    GeometricProgressView progressView;

    public DoctorNewCr() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_new_cr, container, false);

        msd = new ManagingSharedData(getActivity());

        progressView = view.findViewById(R.id.progress_view);


        edtCrNo =  view.findViewById(R.id.edtcr);
        final RelativeLayout scanner =  view.findViewById(R.id.container);
        final Button proceed =  view.findViewById(R.id.proceed_button);

        Bundle bundle = getArguments();
        if (bundle != null)
            navigateTo = getArguments().getInt("navigateTo");


        final BottomNavigationView navigation =  getActivity().findViewById(R.id.doctor_navigation);
        navigation.setVisibility(View.VISIBLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        edtCrNo.setOnTouchListener((v, event) -> {
            scanner.setVisibility(View.GONE);
            proceed.setVisibility(View.VISIBLE);
            return false;
        });
        proceed.setOnClickListener(view1 -> {
            if (edtCrNo.getText().toString().length() != 15) {
                Toast.makeText(getActivity(), "Please Enter a Valid CR Number", Toast.LENGTH_SHORT).show();
            } else {
                navigation.setVisibility(View.VISIBLE);
                getPatientDataFromCrno(edtCrNo.getText().toString());

//                msd.setCrNo(edtCrNo.getText().toString());
//                DoctorHomeFragment doctorHomeFragment = new DoctorHomeFragment();
//                getFragmentManager().beginTransaction().replace(R.id.container, doctorHomeFragment).commit();


                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btnFlash = view.findViewById(R.id.btn_flash);
        btnFlash.setOnClickListener(view12 -> {
            if (isFlashOn == 0) {
                isFlashOn = 1;
                barcodeView.setTorchOn();
            } else if (isFlashOn == 1) {
                isFlashOn = 0;
                barcodeView.setTorchOff();
            }

        });

        barcodeView = view.findViewById(R.id.barcode_scanner);
        Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText("");
        barcodeView.setCameraDistance(50);


        beepManager = new BeepManager(getActivity());
        return view;
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            strcrno = result.getText();


            if (lastText.length() < 20 && lastText.length() > 10) {
                getCrFromBarcode(result.getText());
            } else if (lastText.length() < 9) {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "retry", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getText());
                    String cr = obj.getString("crno");
                    getCrFromQr(cr);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            beepManager.playBeepSoundAndVibrate();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    public void getCrFromBarcode(final String crno) {
        getPatientDataFromCrno(crno);
//        msd.setCrNo(crno);
//        DoctorHomeFragment prescriptionScannerHomeFragment = new DoctorHomeFragment();
//        Bundle args = new Bundle();
//        args.putString("crno", crno);
//        prescriptionScannerHomeFragment.setArguments(args);
//        getFragmentManager().beginTransaction().replace(R.id.container, prescriptionScannerHomeFragment).commit();
    }

    public void getCrFromQr(final String crno) {
        getPatientDataFromCrno(crno);
//        msd.setCrNo(crno);
//        DoctorHomeFragment prescriptionScannerHomeFragment = new DoctorHomeFragment();
//        Bundle args = new Bundle();
//        args.putString("crno", crno);
//        prescriptionScannerHomeFragment.setArguments(args);
//        getFragmentManager().beginTransaction().replace(R.id.container, prescriptionScannerHomeFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        barcodeView.resume();
        resume(barcodeView);
    }

    @Override
    public void onPause() {
        super.onPause();

        barcodeView.pause();
        pause(barcodeView);
    }

    public void getPatientDataFromCrno(String crno) {
        progressView.setVisibility(View.VISIBLE);
        Log.d("scanpatient",ServiceUrl.getPatDetailsScanPrescriptionurl);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.getPatDetailsScanPrescriptionurl, response -> {
            progressView.setVisibility(View.GONE);
            Timber.i("onResponse: %s", response);

            try {
                JSONObject jsonObj = new JSONObject(response);

                // Getting JSON Array node
                JSONArray patDetails = jsonObj.getJSONArray("pat_details");
                if (patDetails.length() == 0) {
                    Toast.makeText(getActivity(), "Invalid CR Number.", Toast.LENGTH_SHORT).show();
                } else {


//                    for (int i = 0; i < patDetails.length(); i++) {
                        JSONObject c = patDetails.getJSONObject(0);
                        String crNo = c.getString("HRGNUM_PUK");

                        msd.setCrNo(crNo);
                        navigateTo(navigateTo);
//                        break;

//                    }

                }

            } catch (final JSONException e) {
                progressView.setVisibility(View.GONE);
              e.printStackTrace();
            }


        }, error -> {
//            Log.i("error", "onErrorResponse: " + error);
            if (getActivity() != null) {
                progressView.setVisibility(View.GONE);
                AppUtilityFunctions.handleExceptions(error, getActivity());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("hosp_code", ServiceUrl.hospId);
                data.put("CrNo", crno);
                data.put("seatid", msd.getSeatId());
                return data;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);


    }


    private void navigateTo(int navigateTo) {
        switch (navigateTo) {
            case 1: {
                //labreport
                /*DoctorLabReportFragment doctorLabReportFragment = new DoctorLabReportFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, doctorLabReportFragment);
                transaction.commit();*/
                Intent intent = new Intent(getActivity(), PrescriptionListActivity.class);
                intent.putExtra("list", 2);
                startActivity(intent);
                break;
            }
            case 2: {
                //scan prescription
                PatientFragment patientFragment = new PatientFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, patientFragment);
                transaction.commit();
                break;
            }
            case 3: {
                //view prescription
                ViewPrescriptionSelectEpisodeFragment viewPrescriptionSelectEpisodeFragment = new ViewPrescriptionSelectEpisodeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, viewPrescriptionSelectEpisodeFragment);
                transaction.commit();
                break;
            }
            case 4:{
                //view web prescription
//                startActivity(new Intent(getActivity(), PatientEpisodeListActivity.class));
                Intent intent = new Intent(getActivity(), PrescriptionListActivity.class);
                intent.putExtra("list", 1);
                startActivity(intent);
                break;
            }
            case 5:{
                //EMR Desk
startActivity(new Intent(getActivity(), EMRDeskActivity.class));
                break;
            }
            default: {
                DoctorHomeFragment doctorHomeFragment = new DoctorHomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, doctorHomeFragment);
                transaction.commit();
            }
        }


    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }
}
