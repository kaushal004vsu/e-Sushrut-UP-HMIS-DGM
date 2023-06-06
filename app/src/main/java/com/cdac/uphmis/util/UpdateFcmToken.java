package com.cdac.uphmis.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.model.PatientDetails;
import com.google.firebase.iid.FirebaseInstanceId;

public class UpdateFcmToken {
    public static void updateToken(Context context, PatientDetails patient)
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, instanceIdResult -> {
           String token=instanceIdResult.getToken();
            Log.e("Token", ServiceUrl.testurl+"callPushNotfication/saveFcmToken?modeval=1&mobileNo="+patient.getMobileNo()+"&crno="+patient.getCrno()+"&hospCode="+patient.getHospitalCode()+"&geolocation=&deviceToken="+token+"&platform=android");
            StringRequest request=new StringRequest(Request.Method.GET, ServiceUrl.testurl+"callPushNotfication/saveFcmToken?modeval=1&mobileNo="+patient.getMobileNo()+"&crno="+patient.getCrno()+"&hospCode="+patient.getHospitalCode()+"&geolocation=&deviceToken="+token+"&platform=android",
                    response -> Log.i("response", "onResponse: "+response), error -> Log.i("error", "onErrorResponse: "+error));
            MySingleton.getInstance(context).addToRequestQueue(request);

        });
            }
}