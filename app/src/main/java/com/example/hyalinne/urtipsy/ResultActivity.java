package com.example.hyalinne.urtipsy;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.telephony.SmsManager;
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
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        pref = getSharedPreferences("Data", MODE_PRIVATE);
        setPenalty();

        ((Button)findViewById(R.id.resultBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChoiceActivity.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        lm.removeUpdates(ll);
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
            registerLocationUpdates();
            penaltyWarning.setText("300~400만원의 벌금");
            ((RelativeLayout)findViewById(R.id.resultLayout)).setBackgroundColor(Color.rgb(255,255,200));
        }
        // ...
    }

    private final LocationListener ll = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                double longitude = location.getLongitude(); //경도
                double latitude = location.getLatitude(); //위도
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREAN);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    String addrline = addressList.get(0).getAddressLine(0);
                    Toast.makeText(getApplicationContext(), addrline, Toast.LENGTH_SHORT).show();
                    sendSMS("01033535553", addrline);
                } catch(IOException e) {

                }
            } else {

            }
        }
        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void registerLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, ll);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, ll);
    }

    public void sendSMS(String smsNumber, String smsText){
        Toast.makeText(getApplicationContext(), smsNumber + " " + smsText, Toast.LENGTH_SHORT).show();
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }
}
