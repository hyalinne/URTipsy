package com.example.hyalinne.urtipsy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AuthorizeActivity extends AppCompatActivity {

    private EditText guardianNumberET;
    private EditText authorizationNumberET;
    private String guardianNumber;
    private String authorizationNumber;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        pref = getSharedPreferences("Setting", MODE_PRIVATE);
        if(pref.getString("Guardian", "0").equals("0"))
            ((TextView)findViewById(R.id.checkAuthorizationText)).setText(" ");

        guardianNumberET = (EditText)findViewById(R.id.guardianPhone);
        authorizationNumberET = (EditText)findViewById(R.id.authorizationNum);
        guardianNumber = "0";
        //랜덤한 번호 생성해야함
        authorizationNumber = "2345678";
        Button sendBtn = (Button)findViewById(R.id.sendGuardianBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAuthorizationNumber();
            }
        });

        Button checkBtn = (Button)findViewById(R.id.authorizationBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAuthorizationNumber();
            }
        });
    }

    public void sendAuthorizationNumber() {
        guardianNumber = guardianNumberET.getText().toString();
        SMSManager.sendSMS(getApplicationContext(), guardianNumber, authorizationNumber);
    }

    public void checkAuthorizationNumber() {
        if(authorizationNumber.equals(authorizationNumberET.getText().toString())) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Guardian", guardianNumber);
            editor.commit();
            ((TextView)findViewById(R.id.checkAuthorizationText)).setText("인증되었습니다.");
        }
    }
}
