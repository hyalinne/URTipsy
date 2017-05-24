package com.example.hyalinne.urtipsy;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
    }

    public void callGuardian(View view) {
        String tel = "tel:01033535553";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
    }

    public void callDriver(View view) {
        String tel = "tel:01033535553";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
    }

    public void callTaxi(View view) {
        String tel = "tel:01033535553";
        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
    }

    public void returnHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
