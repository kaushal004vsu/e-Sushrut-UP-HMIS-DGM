package com.cdac.uphmis.util;

import android.content.Context;
import android.util.Log;

import com.cdac.uphmis.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class NukeSSLCerts {
    // TODO: 2020-10-28 add you server certificates 
    public static void nuke(Context context) {
        try {
            //HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(context));

        } catch (Exception e) {
            Log.i("certificate exception", "nuke: " + e);
        }
    }
}
    /*public static SSLSocketFactory getSocketFactory(Context context) {
        try {
            // Load CAs from an InputStream (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.prod_cert));

            InputStream caInput2 = new BufferedInputStream(context.getResources().openRawResource(R.raw.gts1c3));

            InputStream caInput3 = new BufferedInputStream(context.getResources().openRawResource(R.raw.uat_cert));
            InputStream caInput4 = new BufferedInputStream(context.getResources().openRawResource(R.raw.ndhm_cert));
            InputStream caInput5 = new BufferedInputStream(context.getResources().openRawResource(R.raw.ndhm_prod));
            // I paste my myFile.crt in raw folder under res.
            Certificate ca,ca2,ca3,ca4,ca5;

            //noinspection TryFinallyCanBeTryWithResources
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

                ca2 = cf.generateCertificate(caInput2);
                System.out.println("ca2=" + ((X509Certificate) ca2).getSubjectDN());

                ca3 = cf.generateCertificate(caInput3);
                System.out.println("ca3=" + ((X509Certificate) ca3).getSubjectDN());

                ca4 = cf.generateCertificate(caInput4);
                System.out.println("ca4=" + ((X509Certificate) ca4).getSubjectDN());
                ca5 = cf.generateCertificate(caInput5);
                System.out.println("ca5=" + ((X509Certificate) ca5).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            keyStore.setCertificateEntry("ca2",ca2);


            keyStore.setCertificateEntry("ca3",ca3);
            keyStore.setCertificateEntry("ca4",ca4);
            keyStore.setCertificateEntry("ca5",ca5);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
        }
        return null;
    }*/



   /* public static void nuke(Context context) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            //X509Certificate[] myTrustedAnchors =
                            return  new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            Log.i("checktrustedclient", "checkClientTrusted: "+authType);
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            Log.i("checktrustedclient", "checkClientTrusted: "+authType);
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    if(!(arg0.equalsIgnoreCase(".012345")&&arg1.toString().equalsIgnoreCase("abcd")))
                    {
                        // Log.i("verify certificate", "verify: "+arg0);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            Log.i("certificate exception", "nuke: "+e);
        }
    }
}*/
