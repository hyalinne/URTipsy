package com.example.hyalinne.urtipsy;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    private TextView guardian;
    private TextView address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        SharedPreferences pref = getSharedPreferences("Setting", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        guardian = (TextView)findViewById(R.id.guardianPhoneTextView);
        address = (TextView)findViewById(R.id.addressTextView);

        guardian.setText(pref.getString("Guardian", " "));
        address.setText(pref.getString("Address", " "));

        Button resetBtn = (Button)findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                guardian.setText(" ");
                address.setText(" ");
            }
        });
    }
}
