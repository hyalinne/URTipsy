package com.example.hyalinne.urtipsy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class MeasureActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final int REQUEST_ENABLE_BT = 123456789;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBtDevice;
    private BluetoothSocket mBtSocket;
    private InputStream mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        pref = getSharedPreferences("Data", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("measureData", "120");
        editor.commit();

        // BluetoothAdapter 인스턴스를 얻는다
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // MAC 주소에서 BluetoothDevice 인스턴스를 얻는다 (주소변환 필요)
        mBtDevice = mBluetoothAdapter.getRemoteDevice("00:00:00:00:00:00");

        try {
            // 연결에 사용할 프로파일을 지정하여 BluetoothSocket 인스턴스를 얻는다
//            mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    // 소켓을 연결한다
                    mBtSocket.connect();
                    // 입출력을 위한 스트림 오브젝트를 얻는다
                    mInput = mBtSocket.getInputStream();
                    while(true) {

                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        startActivity(new Intent(getApplicationContext(), ResultActivity.class));
    }

    @Override
    public void onDestroy() {
        try {
            mBtSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
