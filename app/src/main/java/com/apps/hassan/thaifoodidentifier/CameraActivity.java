package com.apps.hassan.thaifoodidentifier;

import android.*;
import android.app.ActionBar;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;


import butterknife.BindView;
import butterknife.ButterKnife;


public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraView camera;

    @BindView(R.id.btnDetectObject)
    ImageButton btnDetectObject;

    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;
    private boolean cropOutput = false;

    // TODO Move location logic to MainActivity
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    public double usrLatitude, usrLongitude = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_camera);
        activateGetLocation();
        ButterKnife.bind(this);

        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
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

        Intent intent = new Intent(getApplicationContext(), DisplayResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);


    }

    public void activateGetLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            usrLatitude = location.getLatitude();
                            usrLongitude = location.getLongitude();
                            Log.e("Location Latitude", String.valueOf(usrLatitude));
                            Log.e("Location Longitude", String.valueOf(usrLongitude));
                        }

                        else {
                            Log.e("Location", "Location is null");
                        }
                    }
                });
    }

}
