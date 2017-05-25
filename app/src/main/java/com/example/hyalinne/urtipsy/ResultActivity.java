package com.example.hyalinne.urtipsy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        pref = getSharedPreferences("Data", MODE_PRIVATE);
        gps = new GpsInfo(ResultActivity.this);
        setPenalty();

        ((Button)findViewById(R.id.resultBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChoiceActivity.class));
            }
        });
    }

    public void setPenalty() {
        int alcohol = Integer.valueOf(pref.getString("measureData", "0"));

        TextView measureValue = (TextView)findViewById(R.id.measureValue);
        TextView penaltyWarning = (TextView)findViewById(R.id.penaltyWarning);

        // 알콜 수치
        measureValue.setText(String.valueOf(alcohol));
        // 알콜 수치에 따른 벌금 및 색상 변경
        if(alcohol <= 100) {
            penaltyWarning.setText("150~300만원의 벌금");
        } else if(100 < alcohol && alcohol <= 200) {
            getLocation();
            penaltyWarning.setText("300~400만원의 벌금");
            ((RelativeLayout)findViewById(R.id.resultLayout)).setBackgroundColor(Color.rgb(255,255,200));
        }
        // ...
    }

    private void getLocation() {
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREAN);

            try {
                List<Address> addressList = null;
                addressList = geocoder.getFromLocation(latitude, longitude, 1);
                if(addressList.size() == 0) return;
                String addrline = addressList.get(0).getAddressLine(0);
                TextView tv = (TextView)findViewById(R.id.addressTv);
                tv.setText(addrline);
                SMSManager.sendSMS(getApplicationContext(), "01033535553", addrline);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
            getLocation();
        }
    }
}
