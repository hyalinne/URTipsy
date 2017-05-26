package com.example.hyalinne.urtipsy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by hyalinne on 2017-05-27.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch(Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
