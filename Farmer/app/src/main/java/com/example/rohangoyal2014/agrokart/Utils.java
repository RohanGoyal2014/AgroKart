package com.example.rohangoyal2014.agrokart;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class Utils {
    public static FirebaseAuth mAuth;
    public static String phoneNumber;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String byIdName(Context context, String name) {
//        Log.e("Utils",name);
        Resources res = context.getResources();
//        return "";
        return res.getString(res.getIdentifier(name, "string", context.getPackageName()));
    }
}
