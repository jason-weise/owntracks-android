package org.owntracks.android.wrapper;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.owntracks.android.R;

public class GoogleApiAvailability {
    private static final String TAG = "GooglePlayCheck";

    public static final int SUCCESS = 0;
    public static final int API_UNAVAILABLE = 16;
    public static final int SERVICE_INVALID = 9;


    protected static GoogleApiAvailability instance;

    public int isGooglePlayServicesAvailable(Context context) {
        Log.d(TAG, "isGooglePlayServicesAvailable Wrapper" + API_UNAVAILABLE);

        return API_UNAVAILABLE;
    }

    public static GoogleApiAvailability getInstance() {
        if (instance == null) {
            instance = new GoogleApiAvailability();
        }
        return instance;
    }

    public boolean isUserResolvableError(int errorCode) {
        Log.d(TAG, "isUserResolvableError Wrapper" + errorCode);
        // Always resolvable (by overriding)
        return true;
    }

    public Dialog getErrorDialog(Activity activity, int errorCode, int requestCode) {
        return getOverrideDialog(activity, errorCode, requestCode);
    }

    public Dialog getOverrideDialog(Activity activity, int errorCode, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(R.string.play_override_question);

        builder.setPositiveButton(R.string.play_override_continue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "User clicked OK button"); //TODO write overridePlay to sharedPreferences
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "User clicked Cancel button"); //TODO do nothing
            }
        });

        return builder.create();
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int errorCode, int requestCode) {
        return null;
    }

    public boolean isWrapper() {
        return false;
    }

    public void provisionRecoveryButton(Button button, final Activity activity, final int errorCode, final int requestCode) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getErrorDialog(activity, errorCode, requestCode).show();
                tryErrorResolution(activity, errorCode, requestCode);
            }
        });
    }

    protected void tryErrorResolution(final Activity activity, final int resultCode, final int requestCode){
        PendingIntent p = getErrorResolutionPendingIntent(activity, resultCode, requestCode);
        try {
            if(p != null) {
                p.send();
            }
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
