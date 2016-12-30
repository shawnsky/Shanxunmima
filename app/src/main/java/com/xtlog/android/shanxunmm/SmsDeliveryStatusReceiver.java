package com.xtlog.android.shanxunmm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by admin on 2016/12/30.
 */

public class SmsDeliveryStatusReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsDeliveryStatus";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"SmsDeliveryStatusReceiver onReceive.");
        switch(getResultCode()) {
            case Activity.RESULT_OK:
                Log.i(TAG, "RESULT_OK");
                break;
            case Activity.RESULT_CANCELED:
                Log.i(TAG, "RESULT_CANCELED");
                break;
        }
    }
}
