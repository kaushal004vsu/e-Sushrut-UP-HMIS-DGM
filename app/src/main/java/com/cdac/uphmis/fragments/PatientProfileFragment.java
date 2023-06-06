package com.cdac.uphmis.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.adapter.PatientProfileAdapter;
import com.cdac.uphmis.model.PatientProfileDetails;
import com.cdac.uphmis.model.UMIDData;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientProfileFragment extends Fragment {
    ImageView img;
    ImageView uploadImage;
    File imagepath;

    TextView tvName, tvCrno, tvAddress, tvAge, tvGender;
    ManagingSharedData msd;
    RequestQueue requestQueue;
    CircleImageView navImage;

    RelativeLayout rrImage;
    public PatientProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        uploadImage = view.findViewById(R.id.upload_picture);
        img = view.findViewById(R.id.profile);
        tvName = view.findViewById(R.id.tv_name);
        rrImage=view.findViewById(R.id.header_cover_image);

       /* tvCrno = view.findViewById(R.id.tv_crno);
        tvGender = view.findViewById(R.id.tv_gender);
        tvAge = view.findViewById(R.id.tv_age);*/
        navImage = getActivity().findViewById(R.id.profile_nav_header);


        msd = new ManagingSharedData(getActivity());

        setUMIDData(view);


        uploadImage.setOnClickListener(view1 -> {
            try {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Information")
                        .setMessage("All patients linked with the mobile number  XXXXXX" + msd.getPatientDetails().getMobileNo().substring(6, 10) + " will be shown the same profile picture.\nDo you want to continue?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    openCamra();
                                }catch(Exception ex){ex.printStackTrace();}
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            } catch (Exception ex) {
                Toast.makeText(getActivity(), "Please grant camera and storage permissions.", Toast.LENGTH_SHORT).show();
            }
        });
//         submitCr();


        try {
            File f = new File(getContext().getFilesDir().toString(), "/"+msd.getPatientDetails().getCrno()+".jpg");
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());

            if (bmp != null) {
                img.setImageBitmap(bmp);
                navImage.setImageBitmap(bmp);
            }else {
                img.setImageDrawable(getContext().getDrawable(R.drawable.profile_icon));
                navImage.setImageDrawable(getContext().getDrawable(R.drawable.profile_icon));
               // navImage.setImageBitmap(bmp);
               // if (msd.getPatientDetails().getUmidData()!=null)
               // getPatientProfileImage();
            }
        } catch (Exception ex) {
//            Toast.makeText(getActivity(), "error:" + ex, Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void saveFile(byte[] data, String fileName) throws RuntimeException {
        if (!getContext().getFilesDir().exists() && !getContext().getFilesDir().mkdirs()) {
            return;
        }
        File mainPicture = new File(getContext().getFilesDir().toString(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(mainPicture);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
           // throw new RuntimeException("Image could not be saved.", e);
        }
    }
    private void getPatientProfileImage()
    {
        StringRequest request=new StringRequest(Request.Method.GET, ServiceUrl.testurl+"AppOpdService/getImageByCrNoAndUmid?crNo=&episodeCode=&hospCode=&seatid=&umid="+msd.getPatientDetails().getUmidData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("resopnse", "onResponse: ");
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        byte[] decodedString = Base64.decode(jsonObject.getString("profilePicBase64"), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        navImage.setImageBitmap(decodedByte);
                        img.setImageBitmap(decodedByte);

                        saveFile(decodedString,msd.getPatientDetails().getCrno()+".jpg");
//                        BitmapDrawable ob = new BitmapDrawable(getResources(), decodedByte);
//                        rrImage.setBackground(ob);
                    }
                }catch(Exception ex)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onResponse: ");
            }
        });

request.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void setUMIDData(View view) {
        UMIDData umidData = msd.getPatientDetails().getUmidData();
        tvName.setText(umidData.getName());

        List<PatientProfileDetails> patientProfileDetailsList = new ArrayList<>();

        patientProfileDetailsList.add(new PatientProfileDetails("CR No:", msd.getPatientDetails().getCrno()));

        if (!(msd.getPatientDetails().getHealthId()==null||msd.getPatientDetails().getHealthId().isEmpty()))
            patientProfileDetailsList.add(new PatientProfileDetails("ABHA Address:", msd.getPatientDetails().getHealthId()));


        if (!(msd.getPatientDetails().getPatHealthId()==null||msd.getPatientDetails().getPatHealthId().isEmpty()))
            patientProfileDetailsList.add(new PatientProfileDetails("ABHA Number:", msd.getPatientDetails().getPatHealthId()));





        //  patientProfileDetailsList.add(new PatientProfileDetails("PCH Id:", umidData.getPchId()));
//        patientProfileDetailsList.add(new PatientProfileDetails("Beneficiary UUID:", umidData.getBeneficiaryUuid()));
        patientProfileDetailsList.add(new PatientProfileDetails("UMID No:", umidData.getUmidNo()));
        patientProfileDetailsList.add(new PatientProfileDetails("Name:", umidData.getName()));
        patientProfileDetailsList.add(new PatientProfileDetails("Relation:", umidData.getRelation()));
        patientProfileDetailsList.add(new PatientProfileDetails("Gender:", umidData.getGender()));
        patientProfileDetailsList.add(new PatientProfileDetails("Date of Birth:", umidData.getDob()));
        patientProfileDetailsList.add(new PatientProfileDetails("Marital Status:", umidData.getMaritalStatus()));
        patientProfileDetailsList.add(new PatientProfileDetails("Residential Address:", umidData.getResidentialAddress()));
        patientProfileDetailsList.add(new PatientProfileDetails("City:", umidData.getCity()));
        patientProfileDetailsList.add(new PatientProfileDetails("Pincode:", umidData.getPincode()));
        patientProfileDetailsList.add(new PatientProfileDetails("Health Unit Opted:", umidData.getHealthUnitOpted()));
        patientProfileDetailsList.add(new PatientProfileDetails("Mobile No:", umidData.getMobileNo()));
        patientProfileDetailsList.add(new PatientProfileDetails("Email Id:", umidData.getEmailId()));
        patientProfileDetailsList.add(new PatientProfileDetails("Validity:", umidData.getValidity()));
        patientProfileDetailsList.add(new PatientProfileDetails("Job Type:", umidData.getJobType()));
        patientProfileDetailsList.add(new PatientProfileDetails("Current Status:", umidData.getCurrentStatus()));
        patientProfileDetailsList.add(new PatientProfileDetails("Middle Name:", umidData.getMiddleName()));
        patientProfileDetailsList.add(new PatientProfileDetails("Last Name:", umidData.getLastName()));
        patientProfileDetailsList.add(new PatientProfileDetails("Age:", umidData.getAge()));
        patientProfileDetailsList.add(new PatientProfileDetails("Blood Group:", umidData.getBloodGroup()));

   //     patientProfileDetailsList.add(new PatientProfileDetails("Aadhaar No:", umidData.getAadhaarNo()));
        String value = umidData.getPanNo();
        String maskedPanNumber ="";
        try {
            maskedPanNumber = new StringBuilder(value)
                    .replace(0, value.length() - 4, new String(new char[value.length() - 4]).replace("\0", "x")).toString();
            System.out.println(maskedPanNumber);
        }catch(Exception ex){}
        patientProfileDetailsList.add(new PatientProfileDetails("PAN No:", maskedPanNumber));
        patientProfileDetailsList.add(new PatientProfileDetails("Father Name:", umidData.getFatherName()));
        patientProfileDetailsList.add(new PatientProfileDetails("Spouse Name:", umidData.getSpouseName()));
        patientProfileDetailsList.add(new PatientProfileDetails("Street:", umidData.getStreet()));
        patientProfileDetailsList.add(new PatientProfileDetails("Landmark:", umidData.getLandmark()));
        patientProfileDetailsList.add(new PatientProfileDetails("Location:", umidData.getLocation()));
        patientProfileDetailsList.add(new PatientProfileDetails("District:", umidData.getDistrict()));
        patientProfileDetailsList.add(new PatientProfileDetails("State Name:", umidData.getStateName()));
       // patientProfileDetailsList.add(new PatientProfileDetails("Country Name:", umidData.getCountryName()));
       // patientProfileDetailsList.add(new PatientProfileDetails("Handicap Status:", umidData.getHandicapStatus()));
        patientProfileDetailsList.add(new PatientProfileDetails("Level of Entitilment:", umidData.getLevelOfEntitilment()));
        patientProfileDetailsList.add(new PatientProfileDetails("Department:", umidData.getDepartment()));
        patientProfileDetailsList.add(new PatientProfileDetails("Designation:", umidData.getDesignation()));
        patientProfileDetailsList.add(new PatientProfileDetails("OPD Eligibility:", umidData.getOpdEligibility()));
        patientProfileDetailsList.add(new PatientProfileDetails("IPD Eligibility:", umidData.getIpdEligibility()));
        patientProfileDetailsList.add(new PatientProfileDetails("Beneficiary Type:", umidData.getBeneficiaryType()));
        patientProfileDetailsList.add(new PatientProfileDetails("Division/Unit:", umidData.getCustomUnit()));
        //patientProfileDetailsList.add(new PatientProfileDetails("Custom Unit Code:", umidData.getCustomUnitCode()));
        patientProfileDetailsList.add(new PatientProfileDetails("Zone/PU:", umidData.getCustomZone()));
       // patientProfileDetailsList.add(new PatientProfileDetails("Custom Zone Code:", umidData.getCustomZoneCode()));
       // patientProfileDetailsList.add(new PatientProfileDetails("card Status:", umidData.getCardStatus()));
       // patientProfileDetailsList.add(new PatientProfileDetails("Card Inactive Remarks:", umidData.getCardInactiveRemarks()));
        //patientProfileDetailsList.add(new PatientProfileDetails("UMID Remarks:", umidData.getUmidRemarks()));
        patientProfileDetailsList.add(new PatientProfileDetails("Id Card Validity Status Flag:", umidData.getIdCardValidityStatusFlag()));
      //  patientProfileDetailsList.add(new PatientProfileDetails("Patient Category:", String.valueOf(umidData.getPatientCategory())));



    setUpRecyclerView(view,patientProfileDetailsList);
    }

    private void setUpRecyclerView(View view,List<PatientProfileDetails> PatientProfileDetailsList) {
        if (PatientProfileDetailsList != null && PatientProfileDetailsList.size() > 0) {


          RecyclerView recyclerView=view.findViewById(R.id.rv_profile);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
          PatientProfileAdapter  adapter = new PatientProfileAdapter(getContext(), PatientProfileDetailsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

    }
    private void openCamra() {
        imagepath = new File(getContext().getFilesDir().toString(), "/userpicture.jpg");
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity().getApplicationContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imagepath));

        startActivityForResult(i, 123);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            try {
                Bitmap bmp = changeOrientation(imagepath.getAbsolutePath().toString());
                //Bitmap bmp = BitmapFactory.decodeFile(imagepath.getAbsolutePath());
                img.setImageBitmap(bmp);
                navImage.setImageBitmap(bmp);
                Log.d("path=", imagepath.toString());
            }catch(Exception ex){ex.printStackTrace();}
        }

    }

    public static Bitmap changeOrientation(String file) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        return rotatedBitmap;
    }

    private void saveBase64ImageToFile(String base64ImageData)
    {
        FileOutputStream fos = null;
        try {
            if (base64ImageData != null) {
                fos = getContext().openFileOutput("imageName.png", Context.MODE_PRIVATE);
                byte[] decodedString = Base64.decode(base64ImageData, Base64.DEFAULT);
                fos.write(decodedString);
                fos.flush();
                fos.close();
            }

        } catch (Exception e) {

        } finally {
            if (fos != null) {
                fos = null;
            }
        }
    }
}
