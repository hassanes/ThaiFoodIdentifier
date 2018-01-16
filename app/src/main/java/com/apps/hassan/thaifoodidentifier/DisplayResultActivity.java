package com.apps.hassan.thaifoodidentifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayResultActivity extends AppCompatActivity {

    @BindView(R.id.imageViewResult)
    ImageView imageViewResult;

    @BindView(R.id.textViewResult)
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        ButterKnife.bind(this);

        byte[] jpeg = ResultHolder.getImage();

        if (jpeg != null) {
            imageViewResult.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);

            if (bitmap == null) {
                finish();
                return;
            }

            imageViewResult.setImageBitmap(bitmap);

        }
    }
}
