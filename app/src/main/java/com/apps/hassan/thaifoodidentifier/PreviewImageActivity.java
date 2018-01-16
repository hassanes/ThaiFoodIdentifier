package com.apps.hassan.thaifoodidentifier;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewImageActivity extends AppCompatActivity {

    @BindView(R.id.imageViewResult)
    ImageView imageViewResult;

    @BindView(R.id.textViewResult)
    TextView textViewResult;

    @BindView(R.id.btn_upload)
    Button btnUpload;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private FirebaseStorage storage;
    private StorageReference storageRef, imageRef;
    private Bitmap bitmap;

    private ContributorActivity contributorActivity = new ContributorActivity();
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getInstance().getReference();



        byte[] jpeg = ResultHolder.getImage();

        if (jpeg != null) {
            imageViewResult.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);

            if (bitmap == null) {
                finish();
                return;
            }

            imageViewResult.setImageBitmap(bitmap);


            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    className = ResultHolder.getClassName();
                    if(className == null){
                        className = "Unknown_class";
                    }
                    Log.e("Class name", className);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    imageRef = storageRef.child(className + "/" + UUID.nameUUIDFromBytes(data).toString() + ".jpg");


                    imageRef.putBytes(data);
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backToContributor();
                }
            });

        }
    }

    private void backToContributor(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 20, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}
