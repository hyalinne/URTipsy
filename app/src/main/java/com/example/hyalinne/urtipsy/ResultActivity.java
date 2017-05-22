package com.example.hyalinne.urtipsy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        pref = getSharedPreferences("Data", MODE_PRIVATE);
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
            penaltyWarning.setText("300~400만원의 벌금");
            ((RelativeLayout)findViewById(R.id.resultLayout)).setBackgroundColor(Color.rgb(255,255,200));
        }
        // ...
    }

    public void locationService() {

    }
}
