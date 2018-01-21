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

import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayResultActivity extends AppCompatActivity {

    @BindView(R.id.imageViewResult)
    ImageView imageViewResult;

    @BindView(R.id.recycler_view)
    RecyclerView rv;

    private String latitude;
    private String longitude;

    private Bitmap bitmapToClassify;
    private String classifyResult;

    //@BindView(R.id.find_restaurant)
    //Button findRestaurant;

    private List<ClassifyResult> resultList;

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/output_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/output_labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Identification Result");

        classifier = TensorFlowImageClassifier.create(
                getAssets(),
                MODEL_FILE,
                LABEL_FILE,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME);

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rv.setLayoutManager(llm);

        latitude = ResultHolder.getLatitude();
        longitude = ResultHolder.getLongitude();


        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                String result = resultList.get(position).name;
                Log.e("Clicked", result);
                Log.e("Latitude", latitude);
                Log.e("Longitude", longitude);

                if (result != "Unknown"){
                    new FinestWebView.Builder(DisplayResultActivity.this)
                            .theme(R.style.RedTheme)
                            .showUrl(false)
                            .titleDefault("Restaurant nearby")
                            .swipeRefreshColorRes(R.color.redPrimaryDark)
                            .webViewSupportZoom(true)
                            .show("https://www.wongnai.com/restaurants?mode=3&domain=1&spatialInfo.coordinate.latitude=" + latitude + "&spatialInfo.radius=5&q=" + result + "&spatialInfo.coordinate.longitude=" + longitude + "&page.number=1");
                }

                else {
                    Log.e("Result", result);
                }


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

            bitmapToClassify = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

            Log.e("Bitmap", "Complete created bitmap");

            if (bitmap == null) {
                finish();
                return;
            }

            imageViewResult.setImageBitmap(bitmap);

        }

        if (classifier == null){
            Log.e("Classifier","null");
        }
        else {

            List<Classifier.Recognition> results = classifier.recognizeImage(bitmapToClassify);
            classifyResult = results.toString();
            // String classifyResult = String.valueOf(results);
            Log.e("Classifier"," not null");
            Log.e("classifyResult", classifyResult);
            initializeData();
            initializeAdapter();

        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }


    private void initializeData(){
        resultList = new ArrayList<>();

        classifyResult = classifyResult.substring(1,(classifyResult.length() - 1));
        String[] classifyResultList = classifyResult.split(",");
        for (int i = classifyResultList.length - 1; i >= 0; i--){
            String[] result = classifyResultList[i].split("/");
            for (int j = 0; j < 2; j++ ){
                if(j % 2 != 0)
                    resultList.add(new ClassifyResult(result[j-1], result[j]));
            }
        }

        // resultList.add(new ClassifyResult("Tom Yum Goong", "Confidential 90.26%"));
        // resultList.add(new ClassifyResult("Pad Thai", "Confidential 61.15%"));
    }

    private void initializeAdapter(){
        MyAdapter adapter = new MyAdapter(resultList);
        rv.setAdapter(adapter);
    }

/*
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
    */

}
