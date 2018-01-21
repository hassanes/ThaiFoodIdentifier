package com.apps.hassan.thaifoodidentifier;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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

    @BindView(R.id.btn_upload)
    FloatingActionButton btnUpload;

    @BindView(R.id.btn_cancel)
    FloatingActionButton btnCancel;

    private FirebaseStorage storage;
    private StorageReference storageRef, imageRef;
    private Bitmap bitmap;
    private NumberProgressBar bnp;

    private String className;

    AlertDialog.Builder builder;
    LayoutInflater layoutinflater;
    AlertDialog alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Upload this image ?");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getInstance().getReference();

        bnp = (NumberProgressBar)findViewById(R.id.number_progress_bar);


        byte[] jpeg = ResultHolder.getImage();

        if (jpeg != null) {
            imageViewResult.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = calculateInSampleSize(options, 2064,1161);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);

            builder = new AlertDialog.Builder(PreviewImageActivity.this);
            layoutinflater = getLayoutInflater();
            View dView = layoutinflater.inflate(R.layout.uploading_progress,null);
            builder.setCancelable(true);
            builder.setView(dView);
            bnp = (NumberProgressBar) dView.findViewById(R.id.number_progress_bar);
            alertdialog = builder.create();

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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data = baos.toByteArray();
                    imageRef = storageRef.child(className + "/" + UUID.nameUUIDFromBytes(data).toString() + ".jpg");
                    alertdialog.show();

                    UploadTask uploadTask = imageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Context context = getApplicationContext();
                            CharSequence text = "Upload failed :(";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            alertdialog.cancel();

                            Context context = getApplicationContext();
                            CharSequence text = "Upload completed :)";

                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            backToContributor();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            int progressInt = (int) progress;
                            Log.e("Upload is ", String.valueOf(progress) + "% done");

                            bnp.incrementProgressBy(progressInt);

                        }
                    });
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

    private int calculateInSampleSize(BitmapFactory.Options options, int i, int i1) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > i || width > i1) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= i
                    && (halfWidth / inSampleSize) >= i1) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;

    }


    private void backToContributor(){
        Intent intent = new Intent(this, ContributorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        backToContributor();
    }


}
