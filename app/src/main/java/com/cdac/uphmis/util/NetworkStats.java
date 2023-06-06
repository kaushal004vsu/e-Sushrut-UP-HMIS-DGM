package com.cdac.uphmis.util;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.cdac.uphmis.R;

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

public class NetworkStats {
     static TelephonyManager telephonyManager;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void appUpdateDialog(Context context) {
        Log.i("TAG", "appUpdateDialog: ");
        TextView tvNetwork, tvMetered, tvDownstream, tvUpstream, tvConnectedBy, tvBandwidth, tvClose;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.network_stats_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(true);
        tvNetwork = dialog.findViewById(R.id.tv_network);
        tvMetered = dialog.findViewById(R.id.tv_metered);
        tvDownstream = dialog.findViewById(R.id.tv_downstream);
        tvUpstream = dialog.findViewById(R.id.tv_upstream);
        tvConnectedBy = dialog.findViewById(R.id.tv_connected_by);
        tvBandwidth = dialog.findViewById(R.id.tv_bandwidth);
        tvClose = dialog.findViewById(R.id.tv_close);
        Button btnRefresh = dialog.findViewById(R.id.btn_refresh);


        telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        getNetworkType(context,tvNetwork);
        tvMetered.setText(checkMeteredness(context));
        tvBandwidth.setText("Bandwidth:\n" + String.valueOf(getBandwidth(context,tvDownstream,tvUpstream)));
        tvConnectedBy.setText("Connected via:\n" + connectedBy(context) + " Network");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            telephonyManager.listen(new SessionStateListener(context, tvNetwork, tvMetered, tvDownstream, tvUpstream, tvConnectedBy, tvBandwidth), PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED);
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {

            telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        }


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "resfreshonClick: ");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Log.i("TAG", "inside r: ");
                    telephonyManager.listen(new SessionStateListener(context, tvNetwork, tvMetered, tvDownstream, tvUpstream, tvConnectedBy, tvBandwidth), PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED);
                } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                    Log.i("TAG", "inside n: ");

                    telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

                }
                getNetworkType(context,tvNetwork);
                tvMetered.setText(checkMeteredness(context));
                tvBandwidth.setText("Bandwidth:\n" + String.valueOf(getBandwidth(context,tvDownstream,tvUpstream)));
                tvConnectedBy.setText("Connected via:\n" + connectedBy(context) + " Network");

            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void getNetworkType(Context context, TextView tvNetwork) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("permisionng", "getNetworkType: ");
            return;
        }
        Log.i("switch statement", "getNetworkType: ");
        Log.i("TAG", "getNetworkType: "+telephonyManager.getDataNetworkType());
        switch (telephonyManager.getDataNetworkType()) {
            case NETWORK_TYPE_EDGE:
                tvNetwork.setText("EDGE");
                break;
            case NETWORK_TYPE_GSM:
                tvNetwork.setText("GSM");
                break;
            case NETWORK_TYPE_GPRS:
                tvNetwork.setText("GPRS");
                break;
            case NETWORK_TYPE_CDMA:
                tvNetwork.setText("2G");
                break;
            case NETWORK_TYPE_IDEN:
            case NETWORK_TYPE_1xRTT:
                tvNetwork.setText("2G");
                break;
            case NETWORK_TYPE_UMTS:
                tvNetwork.setText("UMTS");
                break;
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_HSPAP:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_EVDO_B:
                tvNetwork.setText("3G");
                break;
            case NETWORK_TYPE_LTE:
                tvNetwork.setText("4G");
                break;
            case NETWORK_TYPE_NR:
                tvNetwork.setText("5G");
                break;
            default:
                tvNetwork.setText("Unknown");
        }
    }
    public static float getBandwidth(Context context,TextView tvDownstream,TextView tvUpstream) {
       ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //should check null because in airplane mode it will be null
        if (netInfo != null) {
            Log.i("getTypeName", "getBandwidth: " + netInfo.getTypeName());
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

            int downSpeed = nc.getLinkDownstreamBandwidthKbps();
            int upSpeed = nc.getLinkUpstreamBandwidthKbps();

            tvDownstream.setText("Down Speed: " + String.valueOf(downSpeed) + "\nKbps");
            tvUpstream.setText("Up Speed: " + String.valueOf(upSpeed) + "\nKbps");
            Log.i("TAG", "getBandwidth: " + downSpeed);
            Log.i("TAG", "getBandwidth: " + upSpeed);
            return downSpeed / upSpeed;
        } else {
            return 0.0f;
        }
    }
    public static String connectedBy(Context context) {
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            Log.i("getTypeName", "getBandwidth: " + netInfo.getTypeName());


            return netInfo.getTypeName();
        } else {
            return "";
        }
    }

    private static String checkMeteredness(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager
                .getNetworkCapabilities(network);

        if (capabilities != null) {
            if (capabilities.hasCapability(NET_CAPABILITY_NOT_METERED) ||
                    capabilities.hasCapability(NET_CAPABILITY_TEMPORARILY_NOT_METERED)) {
                //  Toast.makeText(context, "unmetered", Toast.LENGTH_SHORT).show();
                return "Not Metered";

            } else {
                return "Metered";
                //Toast.makeText(context, "metered", Toast.LENGTH_SHORT).show();
            }
        } else {
            return "";

        }

    }
}

class SessionStateListener extends PhoneStateListener {

    Context context;
    TextView tvNetwork, tvMetered, tvDownstream, tvUpstream, tvConnectedBy, tvBandwidth;

    SessionStateListener(Context c) {
        this.context = c;
    }

    SessionStateListener(Context context, TextView tvNetwork, TextView tvMetered, TextView tvDownstream, TextView tvUpstream, TextView tvConnectedBy, TextView tvBandwidth) {
        this.context = context;
        this.tvNetwork = tvNetwork;
        this.tvMetered = tvMetered;
        this.tvDownstream = tvDownstream;
        this.tvUpstream = tvUpstream;
        this.tvConnectedBy = tvConnectedBy;
        this.tvBandwidth = tvBandwidth;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onDisplayInfoChanged(@NonNull TelephonyDisplayInfo telephonyDisplayInfo) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "onDisplayInfoChanged: permission not granted");
        } else {
            super.onDisplayInfoChanged(telephonyDisplayInfo);
            Log.w("getNetworkType", "" + telephonyDisplayInfo.getNetworkType());
            if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA) {
                Log.w("TAG", "5G_NSA" + telephonyDisplayInfo.getOverrideNetworkType());
                tvNetwork.setText("5G NSA");
            } else if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE) {
                Log.w("TAG", "5G_NSA_MMWAVE");
                tvNetwork.setText("5G NSA MMWave");

            } else if (telephonyDisplayInfo.getOverrideNetworkType() == TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO) {
                Log.w("TAG", "OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO");
                tvNetwork.setText("LTE ADVANCED PRO");
            } else {
                switch (telephonyDisplayInfo.getNetworkType()) {
                    case NETWORK_TYPE_EDGE:
                        tvNetwork.setText("EDGE");
                        break;
                    case NETWORK_TYPE_GSM:
                        tvNetwork.setText("GSM");
                        break;
                    case NETWORK_TYPE_GPRS:
                        tvNetwork.setText("GPRS");
                        break;
                    case NETWORK_TYPE_CDMA:
                        tvNetwork.setText("2G");
                        break;
                    case NETWORK_TYPE_IDEN:
                    case NETWORK_TYPE_1xRTT:
                        tvNetwork.setText("2G");
                        break;
                    case NETWORK_TYPE_UMTS:
                        tvNetwork.setText("UMTS");
                        break;
                    case NETWORK_TYPE_HSDPA:
                    case NETWORK_TYPE_HSPA:
                    case NETWORK_TYPE_HSPAP:
                    case NETWORK_TYPE_EVDO_0:
                    case NETWORK_TYPE_EVDO_A:
                    case NETWORK_TYPE_EVDO_B:
                        tvNetwork.setText("3G");
                        break;
                    case NETWORK_TYPE_LTE:
                        tvNetwork.setText("4G");
                        break;
                    case NETWORK_TYPE_NR:
                        tvNetwork.setText("5G");
                        break;
                    default:
                        tvNetwork.setText("Unknown");
                }
            }

            Log.i("TAG", "onDisplayInfoChanged: " + telephonyDisplayInfo.getNetworkType());

            tvMetered.setText(checkMeteredness(context));
            ConnectivityManager cm = getConnectivityManager(context);
            tvBandwidth.setText("Bandwidth:\n" + String.valueOf(getBandwidth(cm)));
            tvConnectedBy.setText("Connected via:\n" + connectedBy(cm) + " Network");
        }

    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public  float getBandwidth(ConnectivityManager cm) {

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //should check null because in airplane mode it will be null
        if (netInfo != null) {
            Log.i("getTypeName", "getBandwidth: " + netInfo.getTypeName());
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

            int downSpeed = nc.getLinkDownstreamBandwidthKbps();
            int upSpeed = nc.getLinkUpstreamBandwidthKbps();

            tvDownstream.setText("Down Speed: " + String.valueOf(downSpeed) + "\nKbps");
            tvUpstream.setText("Up Speed: " + String.valueOf(upSpeed) + "\nKbps");
            Log.i("TAG", "getBandwidth: " + downSpeed);
            Log.i("TAG", "getBandwidth: " + upSpeed);
            return downSpeed / upSpeed;
        } else {
            return 0.0f;
        }
    }

    private  String checkMeteredness(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager
                .getNetworkCapabilities(network);

        if (capabilities != null) {
            if (capabilities.hasCapability(NET_CAPABILITY_NOT_METERED) ||
                    capabilities.hasCapability(NET_CAPABILITY_TEMPORARILY_NOT_METERED)) {
                //  Toast.makeText(context, "unmetered", Toast.LENGTH_SHORT).show();
                return "Not Metered";

            } else {
                return "Metered";
                //Toast.makeText(context, "metered", Toast.LENGTH_SHORT).show();
            }
        } else {
            return "";

        }

    }

    private  String connectedBy(ConnectivityManager cm) {
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            Log.i("getTypeName", "getBandwidth: " + netInfo.getTypeName());


            return netInfo.getTypeName();
        } else {
            return "";
        }
    }
}

