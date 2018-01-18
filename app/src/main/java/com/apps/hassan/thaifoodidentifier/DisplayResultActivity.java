package com.apps.hassan.thaifoodidentifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayResultActivity extends AppCompatActivity {

    @BindView(R.id.imageViewResult)
    ImageView imageViewResult;

    @BindView(R.id.recycler_view)
    RecyclerView rv;

    //@BindView(R.id.find_restaurant)
    //Button findRestaurant;

    private List<ClassifyResult> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Identify Result");

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                String result = resultList.get(position).name;
                Log.e("Clicked", result);
            }

            @Override public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));

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


    private void initializeData(){
        resultList = new ArrayList<>();
        resultList.add(new ClassifyResult("Tom Yum Goong", "Confidential 90.26%"));
        resultList.add(new ClassifyResult("Pad Thai", "Confidential 61.15%"));
    }

    private void initializeAdapter(){
        MyAdapter adapter = new MyAdapter(resultList);
        rv.setAdapter(adapter);
    }
}
