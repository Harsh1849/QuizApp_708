package com.quizapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Extensions {

    static String USERNAME = "username";
    static String QUIZDATA = "quizData";
    static String QUIZPOSITION = "quizPosition";
    static String ISQUESTION = "isQuestion";

    /**
     * This Extension has been using for showing Toast messages
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This Extension has been using for hide keyboard
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static ProgressDialog dialog;

    /**
     * This function is used for showing a progress dialog
     */
    public static void showProgress(Context context) {
        dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage(context.getString(R.string.Please_wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * This function is used for hiding the progress dialog
     */
    public static void hideProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * This function is used for checking internet connection
     */
    public static boolean isConnect(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    isConnected = true;
                }
            }
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        if (!isConnected) {
            hideProgress(); // Assuming HideProgress and DebugToast are methods in your context
            showToast(context, context.getString(R.string.check_internet));
        }
        return isConnected;
    }
}