package com.xtlog.android.shanxunmm;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static TextView sPasswordText;
    public static TextView sUsableText;
    private Button mGetButton;

    private String SMS_SEND_ACTION = "SMS_SEND";
    private String SMS_DELIVERED_ACTION = "SMS_DELIVERED";
    private static final String TAG = "MainActivity";


    private SmsStatusReceiver mSmsStatusReceiver;
    private SmsDeliveryStatusReceiver mSmsDeliveryStatusReceiver;
    private SmsReceiver mSmsReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPasswordText = (TextView) findViewById(R.id.main_password_text);
        mGetButton = (Button) findViewById(R.id.main_get_button);
        sUsableText = (TextView) findViewById(R.id.main_usable_text);

        initText();

        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS
                    }, 4);
                } else {
                    sendMessage();
                    sUsableText.setText("密码获取中");
                }
            }
        });


    }

    private void initText(){
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String sharedPassword = pref.getString("password","ERROR");
        long sharedDate = pref.getLong("date", 0);
        sPasswordText.setText(sharedPassword);
        long currentTime = new Date(System.currentTimeMillis()).getTime();
        sUsableText.setText(currentTime >= sharedDate ? "密码过期" : "密码有效");
    }


    private void sendMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SEND_ACTION), 0);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(SMS_DELIVERED_ACTION), 0);
        smsManager.sendTextMessage("1065930051", null,
                "mm", sentIntent, deliveryIntent);
        Log.d(TAG,"sent message.");
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED){

                    sendMessage();
                    sUsableText.setText("密码获取中...");
                }
                else{
                    Toast.makeText(this, "没权限给锤子密码", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mSmsStatusReceiver = new SmsStatusReceiver();
        registerReceiver(mSmsStatusReceiver,new IntentFilter(SMS_SEND_ACTION));

        mSmsDeliveryStatusReceiver = new SmsDeliveryStatusReceiver();
        registerReceiver(mSmsDeliveryStatusReceiver,new IntentFilter(SMS_DELIVERED_ACTION));

        mSmsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sms_received");
        registerReceiver(mSmsReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSmsStatusReceiver);
        unregisterReceiver(mSmsDeliveryStatusReceiver);
        unregisterReceiver(mSmsReceiver);
    }



}
