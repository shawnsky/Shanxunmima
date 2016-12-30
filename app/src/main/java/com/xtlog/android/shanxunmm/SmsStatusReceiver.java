package com.xtlog.android.shanxunmm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by admin on 2016/12/30.
 */

public class SmsStatusReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsStatusReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"SmsStatusReceiver onReceive.");
        switch(getResultCode()) {
            case Activity.RESULT_OK:
                Log.d(TAG, "Activity.RESULT_OK");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Log.d(TAG, "RESULT_ERROR_GENERIC_FAILURE");
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Log.d(TAG, "RESULT_ERROR_NO_SERVICE");
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Log.d(TAG, "RESULT_ERROR_NULL_PDU");
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Log.d(TAG, "RESULT_ERROR_RADIO_OFF");
                break;
        }
    }
}
