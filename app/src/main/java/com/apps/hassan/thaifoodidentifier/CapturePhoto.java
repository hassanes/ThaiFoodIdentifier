package com.apps.hassan.thaifoodidentifier;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CapturePhoto extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraView camera;

    @BindView(R.id.btn_capture)
    FloatingActionButton btn_capture;

    private int cameraMethod = CameraKit.Constants.METHOD_STILL;
    private boolean cropOutput = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Capture food image");

        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                    @Override
                    public void callback(CameraKitImage event) {
                        Log.w("Info","Enter callback()");
                        imageCaptured(event);
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        camera.stop();
        super.onPause();
    }

    public void imageCaptured(CameraKitImage image) {
        byte[] jpeg = image.getJpeg();

        ResultHolder.dispose();
        ResultHolder.setImage(jpeg);
        ResultHolder.setNativeCaptureSize(camera.getCaptureSize());

        Log.w("Info","Enter imageCaptured()");

        Intent intent = new Intent(getApplicationContext(), PreviewImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ContributorActivity.class);
        startActivity(intent);
    }

}
