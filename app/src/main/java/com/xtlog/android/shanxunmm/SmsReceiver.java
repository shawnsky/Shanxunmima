package com.xtlog.android.shanxunmm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/12/30.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsReceiver";



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: run");
        // TODO Auto-generated method stub
        if(SMS_RECEIVED.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                Object[] pdus = (Object[])bundle.get("pdus");

                SmsMessage[] msg = new SmsMessage[pdus.length];
                for(int i = 0 ;i<pdus.length;i++){
                    msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }

                String password="",dateStr="";

                SmsMessage sx = msg[0];//获取闪讯短信
                String body = sx.getDisplayMessageBody();
                String number = sx.getDisplayOriginatingAddress();
                if(number.equals("106593005")){
                    Pattern p1 = Pattern.compile("\\d{6}");
                    Pattern p2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
                    Matcher m1 = p1.matcher(body);
                    Matcher m2 = p2.matcher(body);
                    if(m1.find()) password = m1.group(); else password = "ERROR";
                    if(m2.find()) dateStr = m2.group(); else dateStr = "2050-01-01 00:00:00";


                    long date = getLongFromString(dateStr);
                    //保存数据
                    SharedPreferences.Editor editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putString("password", password);
                    editor.putLong("date", date);
                    editor.apply();

                    //更新UI
                    abortBroadcast();
                    MainActivity.sPasswordText.setText(password);
                    MainActivity.sUsableText.setText("密码有效");

                }


            }
        }
    }

    private long getLongFromString(String date){
        Calendar c = Calendar.getInstance();
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7)) - 1;
        int day = Integer.parseInt(date.substring(8,10));
        int hour = Integer.parseInt(date.substring(11,13));
        int min = Integer.parseInt(date.substring(14,16));
        int sec = Integer.parseInt(date.substring(17,19));
        c.set(year, month, day, hour, min, sec);
        return c.getTimeInMillis();
    }

}
