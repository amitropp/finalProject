package com.friends.stay.keepintouch;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CALL = 1 ;

    Call mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        test();

    }



    private void test()
    {
        Contact me = new Contact();
        Date date = new Date();
//        Msg waMessage = new WhatsappMessage(me, date, "hello", null, this);
//        Msg smsMessage = new SmsMessage(me, date, "hello", null, this);
//        smsMessage.send();

//        Call mCall = new Call(me, date, null, this);
//        mCall.callNow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCall.callNow();
                }
                break;
        }
    }


}
