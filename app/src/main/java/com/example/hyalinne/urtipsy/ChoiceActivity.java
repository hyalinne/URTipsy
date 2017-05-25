package com.example.hyalinne.urtipsy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class ChoiceActivity extends AppCompatActivity {
    private final String taxiSMS = "URTipsy - 택시호출 ";
    private final String driverSMS = "URTipsy - 대리기사호출 ";
    private SharedPreferences pref;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        pref = getSharedPreferences("Setting", MODE_PRIVATE);
        address = pref.getString("Address", " ");
    }

    public void callGuardian(View view) {
        String guardianPhone = pref.getString("Guardian", " ");
        if(guardianPhone.equals(" ")) {
            Toast.makeText(getApplicationContext(), "보호자 전화번호가 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        String tel = "tel:" + guardianPhone;
        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
    }

    public void callDriver(View view) {
        String tel = "01033535553";
        String msg;
        if(address.equals(" ")) {
            msg = driverSMS;
        } else {
            msg = driverSMS + "주소 : " + address;
        }
        SMSManager.sendSMS(getApplicationContext(), tel, msg);
    }

    public void callTaxi(View view) {
        String tel = "01033535553";
        String msg;
        if(address.equals(" ")) {
            msg = taxiSMS;
        } else {
            msg = taxiSMS + "주소 : " + address;
        }
        SMSManager.sendSMS(getApplicationContext(), tel, msg);
    }

    public void returnHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
