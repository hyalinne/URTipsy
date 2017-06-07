package com.example.hyalinne.urtipsy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    private final String sendMsg = "보호자님! URTipsy 사용자가 술을 드셨습니다.\n";
    private final String addMsg = "현재 위치 : ";

    private SharedPreferences pref;

    private GpsInfo gps;

    private String addrline;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        pref = getSharedPreferences("Data", MODE_PRIVATE);
        gps = new GpsInfo(ResultActivity.this);
        addrline = null;
        setPenalty();

        (findViewById(R.id.resultBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChoiceActivity.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setPenalty() {
        // 알콜 수치
        int alcohol = Integer.parseInt((pref.getString("testData", "120")).substring(0,3));
        alcohol -= 100;
        String guardianNumber = pref.getString("Guardian", " ");
        String msg;

        TextView measureValue = (TextView) findViewById(R.id.measureValue);
        TextView penaltyWarning = (TextView) findViewById(R.id.penaltyWarning);
        ImageView person = (ImageView) findViewById(R.id.resultImageView);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "record.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO record VALUES(null, '" + str_date + "', " + alcohol +");");


        measureValue.setText(String.valueOf(alcohol));

        getLocation();
        if(addrline != null) {
            msg = sendMsg + addMsg + addrline;
        } else {
            msg = sendMsg;
        }

        // 알콜 수치에 따른 벌금 및 색상 변경
        if(0 < alcohol  && alcohol <= 100) {
            // nothing
        } else if(100 < alcohol && alcohol <= 150) {
            penaltyWarning.setText("벌금\n150~300만");
            person.setImageResource(R.drawable.standing_up_man_yellow);
        } else if(150 < alcohol && alcohol <= 200) {
            getLocation();
            penaltyWarning.setText("벌금\n300~400만");
            person.setImageResource(R.drawable.standing_up_man_yellow);
        } else if(200 < alcohol && alcohol <= 300) {
            getLocation();
            penaltyWarning.setText("벌금\n400~500만");
            person.setImageResource(R.drawable.standing_up_man_orange);
            SMSManager.sendSMS(getApplicationContext(), guardianNumber, msg);
        } else if(300 < alcohol && alcohol <= 400) {
            getLocation();
            penaltyWarning.setText("벌금\n500~600만");
            person.setImageResource(R.drawable.standing_up_man_orange);
            SMSManager.sendSMS(getApplicationContext(), guardianNumber, msg);
        } else if(400 < alcohol && alcohol <= 500) {
            getLocation();
            penaltyWarning.setText("벌금\n600~700만");
            person.setImageResource(R.drawable.standing_up_man_red);
            SMSManager.sendSMS(getApplicationContext(), guardianNumber, msg);
        } else if(500 < alcohol) {
            getLocation();
            penaltyWarning.setText("벌금\n600~700만");
            person.setImageResource(R.drawable.standing_up_man_red);
            SMSManager.sendSMS(getApplicationContext(), guardianNumber, msg);
        } else {
            // error
        }
    }

    private void getLocation() {
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREAN);

        try {
            List<Address> addressList = null;
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if(addressList.size() == 0) return;
            addrline = addressList.get(0).getAddressLine(0);
            TextView tv = (TextView)findViewById(R.id.currentAddressTV);
            tv.setText(addrline);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
