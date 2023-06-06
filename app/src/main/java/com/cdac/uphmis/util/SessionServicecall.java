package com.cdac.uphmis.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED;
import static android.net.NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED;
import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GSM;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_NR;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;

public class SessionServicecall {

    static String networkType = "unknown";

    private Context context;
    private TelephonyManager telephonyManager;


    public SessionServicecall(Context context) {
        this.context = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "SessionServicecall: permission not granted");
            //return "Permission not granted";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            telephonyManager.listen(new SessionPhoneStateListener(context), PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED);
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {

            telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            getNetworkType();
        }
    }

    public void saveSession(String crno, String mobileNo, String hospCode, String menuAccessed, String geoLocation, String jitter, String errorRate,String documentSize) {
        ConnectivityManager cm = getConnectivityManager(context);

        float bandwidth = getBandwidth(cm);
        String connectedBy = connectedBy(cm);
        Log.i("connectedBy", "saveSession: " + connectedBy);
        Log.i("bandwidth", "saveSession: " + bandwidth);
        String isMetered = (isMetered(context)) ? "1" : "0";


        String latency = "";

String url=ServiceUrl.testurl+"mobileAppServices/insetAppSession?crno=" + crno + "&mobileNo=" + mobileNo + "&hospCode=" + hospCode + "&networkType=" + networkType + "&isMetered=" + isMetered + "&menuAccessed=" + menuAccessed + "&bandwidth=" + bandwidth + "&geoLocation=" + geoLocation + "&latency=" + latency + "&jitter=" + jitter + "&errorRate=" + errorRate + "&connectedBy=" + connectedBy+"&documentSize="+documentSize;
        Log.i("TAG", "saveSession: " +url );
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("sessionresponse", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("sessionerror", "onErrorResponse: " + error);
            }
        });


        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    private ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private float getBandwidth(ConnectivityManager cm) {

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //should check null because in airplane mode it will be null
        if (netInfo != null) {
            Log.i("getTypeName", "getBandwidth: " + netInfo.getTypeName());
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

            int downSpeed = nc.getLinkDownstreamBandwidthKbps();
            int upSpeed = nc.getLinkUpstreamBandwidthKbps();
            Log.i("TAG", "getBandwidth: " + downSpeed);
            Log.i("TAG", "getBandwidth: " + upSpeed);
            return downSpeed / upSpeed;
        } else {
            return 0.0f;
        }
    }

    private String connectedBy(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            Log.i("getTypeName", "getBandwidth: " + netInfo.getTypeName());


            return netInfo.getTypeName();
        } else {
            return "";
        }
    }


    private boolean isMetered(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager
                .getNetworkCapabilities(network);
        if (capabilities != null) {
            if (capabilities.hasCapability(NET_CAPABILITY_NOT_METERED) || capabilities.hasCapability(NET_CAPABILITY_TEMPORARILY_NOT_METERED)) {
                return false;
            } else {
                return true;
            }
        }
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNetworkType() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        switch (telephonyManager.getDataNetworkType()) {
            case NETWORK_TYPE_EDGE:
                SessionServicecall.networkType = "EDGE";
                break;
            case NETWORK_TYPE_GSM:
                SessionServicecall.networkType = "GSM";
                break;
            case NETWORK_TYPE_GPRS:
                SessionServicecall.networkType = "GPRS";
                break;
            case NETWORK_TYPE_CDMA:
                SessionServicecall.networkType = "CDMA";
                break;
            case NETWORK_TYPE_IDEN:
            case NETWORK_TYPE_1xRTT:
                SessionServicecall.networkType = "2G";
                break;
            case NETWORK_TYPE_UMTS:
                SessionServicecall.networkType = "UMTS";
                break;
            case NETWORK_TYPE_HSDPA:
                SessionServicecall.networkType = "HSDPA";
                break;
            case NETWORK_TYPE_HSPA:
                SessionServicecall.networkType = "HSPA";
                break;
            case NETWORK_TYPE_HSPAP:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_EVDO_B:
                SessionServicecall.networkType = "3G";
                break;
            case NETWORK_TYPE_LTE:
                SessionServicecall.networkType = "4G";
                break;
            case NETWORK_TYPE_NR:
                SessionServicecall.networkType = "5G";
                break;
            default:
                SessionServicecall.networkType = "Unknown";
        }
    }
}

class SessionPhoneStateListener extends PhoneStateListener {

    Context context;

    SessionPhoneStateListener(Context c) {
        this.context = c;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onDisplayInfoChanged(@NonNull TelephonyDisplayInfo telephonyDisplayInfo) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "onDisplayInfoChanged: permission not granted");
        } else {
            super.onDisplayInfoChanged(telephonyDisplayInfo);
            Log.w("getNetworkType",""+telephonyDisplayInfo.getNetworkType());
            if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA) {
                Log.w("TAG", "5G_NSA"+telephonyDisplayInfo.getOverrideNetworkType());
                SessionServicecall.networkType="5G_NSA";
            }
           else if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE) {
                Log.w("TAG", "5G_NSA_MMWAVE");

             SessionServicecall.networkType="5G_NSA_MMWAVE";
            }
           else if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO) {
                Log.w("TAG", "OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO");
                SessionServicecall.networkType="LTE_ADVANCED_PRO";
            }
           else
            {
                switch (telephonyDisplayInfo.getNetworkType()) {
                    case NETWORK_TYPE_EDGE:
                        SessionServicecall.networkType= "EDGE";
                        break;
                        case NETWORK_TYPE_GSM:
                        SessionServicecall.networkType= "GSM";
                        break;
                    case NETWORK_TYPE_GPRS:
                        SessionServicecall.networkType= "GPRS";
                        break;
                    case NETWORK_TYPE_CDMA:
                        SessionServicecall.networkType= "CDMA";
                        break;
                    case NETWORK_TYPE_IDEN:
                    case NETWORK_TYPE_1xRTT:
                        SessionServicecall.networkType= "2G";
                        break;
                    case NETWORK_TYPE_UMTS:
                        SessionServicecall.networkType= "UMTS";
                        break;
                    case NETWORK_TYPE_HSDPA:
                        SessionServicecall.networkType= "HSDPA";
                        break;
                    case NETWORK_TYPE_HSPA:
                        SessionServicecall.networkType= "HSPA";
                        break;
                    case NETWORK_TYPE_HSPAP:
                    case NETWORK_TYPE_EVDO_0:
                    case NETWORK_TYPE_EVDO_A:
                    case NETWORK_TYPE_EVDO_B:
                        SessionServicecall.networkType= "3G";
                      break;
                    case NETWORK_TYPE_LTE:
                        SessionServicecall.networkType= "4G";
                      break;
                    case NETWORK_TYPE_NR:
                        SessionServicecall.networkType= "5G";
                       break;
                    default:
                        SessionServicecall.networkType= "Unknown";
                }
            }

            Log.i("TAG", "onDisplayInfoChanged: "+SessionServicecall.networkType);


        }

    }


}
    /*public String getLatency() {
        String latency = "";
        String ip = "10.226.21.136";
        String pingCmd = "ping -c 25 " + ip;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            String latencyResult = null;
            while ((inputLine = in.readLine()) != null) {
                latencyResult = inputLine;
            }

            Log.i("TAG", "getLatency: " + latencyResult);
            String[] keyValue = latencyResult.split("=");
            String[] value = keyValue[1].split("/");
            latency = value[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latency;
    }*/