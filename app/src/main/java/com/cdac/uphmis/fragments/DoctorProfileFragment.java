package com.cdac.uphmis.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.ManagingSharedData;

import static android.app.Activity.RESULT_OK;

public class DoctorProfileFragment extends Fragment {

    CircleImageView img;
    ImageView uploadImage;
    File imagepath;
    CircleImageView navImage;
    TextView tvName;
    ManagingSharedData msd;
    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        msd=new ManagingSharedData(getActivity());
        tvName =  view.findViewById(R.id.tv_name);
        tvName.setText(msd.getUsername());
        uploadImage =  view.findViewById(R.id.upload_picture);
        img =  view.findViewById(R.id.profile);
        navImage=getActivity().findViewById(R.id.profile_nav_header);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    openCamra();
                }
                catch(Exception ex)
                {
                    Toast.makeText(getActivity(), "Please grant camera and storage permissions.", Toast.LENGTH_SHORT).show();
                }
        }
        });

        try {
            File f = new File(getContext().getFilesDir().toString(), "/doctorpicture.jpg");


            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());

            if(bmp!=null)
            {
                img.setImageBitmap(bmp);
                navImage.setImageBitmap(bmp);
            }


        } catch (Exception ex) {
            Toast.makeText(getActivity(), "error:" + ex, Toast.LENGTH_SHORT).show();
        }




        return view;
    }
    private void openCamra() {
        imagepath = new File(getContext().getFilesDir().toString(), "/doctorpicture.jpg");


        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity().getApplicationContext(),
                BuildConfig.APPLICATION_ID+ ".provider",
                imagepath));

        //   i.putExtra(MediaStore.EXTRA_OUTPUT, pathuri);
        startActivityForResult(i, 123);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Bitmap bmp=changeOrientation(imagepath.getAbsolutePath().toString());
            //Bitmap bmp = BitmapFactory.decodeFile(imagepath.getAbsolutePath());
            img.setImageBitmap(bmp);
            navImage.setImageBitmap(bmp);
            //  Toast.makeText(getActivity(), "path:" + imagepath, Toast.LENGTH_LONG).show();
            Log.d("path=", imagepath.toString());
            // Log.d("path=", imagepath.getAbsolutePath().toString());
        }

    }



    public static Bitmap changeOrientation(String file)
    {
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
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        return rotatedBitmap;
    }
}
