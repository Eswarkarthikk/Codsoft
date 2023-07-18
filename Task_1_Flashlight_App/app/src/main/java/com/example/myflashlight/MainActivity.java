package com.example.myflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button btn;
    private boolean hasCam = false;
    private boolean flashOn = false;
    private BroadcastReceiver screenOffReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        hasCam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // Register broadcast receiver for screen off
        screenOffReceiver = new ScreenOffReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, intentFilter);
    }

    public void onClick(View view) {
        if (hasCam) {
            if (flashOn) {
                flashOn = false;
                btn.setBackgroundResource(R.drawable.flashoff);
                btn.setText("Flash is Off");
                flashLightOff();
            } else {
                flashOn = true;
                btn.setBackgroundResource(R.drawable.flashon);
                btn.setText("Flash is On");
                flashLightOn();
            }
        } else {
            Toast.makeText(MainActivity.this, "No flash available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOffReceiver);
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        String cameraId;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(MainActivity.this, "Flash is off", Toast.LENGTH_SHORT).show();
    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        String cameraId;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(MainActivity.this, "Flash is on", Toast.LENGTH_SHORT).show();
    }

    private class ScreenOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (flashOn) {
                flashOn = false;
                btn.setBackgroundResource(R.drawable.flashoff);
                btn.setText("Flash is Off");
                flashLightOff();
            }
        }
    }
}
