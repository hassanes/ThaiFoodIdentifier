package com.apps.hassan.thaifoodidentifier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContributorActivity extends AppCompatActivity {

    private boolean fabExpanded = false;
    private FloatingActionButton fab;
    private LinearLayout layoutFabAddImage, layoutFabAddClass;
    private Button btnSubmitClass;
    private EditText editClassName;
    private ArrayAdapter<String> mAdapter;
    private ListView classListView;
    private FirebaseListAdapter<String> adapter;
    private String editClassName_value;
    public FirebaseDatabase database;
    public DatabaseReference databaseRef;
    private String className = " ";
    private String classNameIn;
    private AVLoadingIndicatorView avi;
    private int count = 0;

    AlertDialog.Builder builder;
    LayoutInflater layoutinflater;
    AlertDialog alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Contribution");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        if (!isNetworkAvailable()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please connect the internet.")
                    .setTitle("Connection not available !")
                    .setPositiveButton("WIFI Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS );
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            backToMainActivity();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getInstance().getReference();

        database = FirebaseDatabase.getInstance();


        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        layoutFabAddClass = (LinearLayout) this.findViewById(R.id.layoutFabAddClass);
        layoutFabAddImage = (LinearLayout) this.findViewById(R.id.layoutFabAddImage);

        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator("Loading ... ");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        closeSubMenusFab();

        layoutFabAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(ContributorActivity.this);
                layoutinflater = getLayoutInflater();
                View dView = layoutinflater.inflate(R.layout.edit_class_name,null);
                builder.setCancelable(true);
                builder.setView(dView);
                btnSubmitClass = (Button) dView.findViewById(R.id.btnSubmitClass);
                editClassName = (EditText) dView.findViewById(R.id.editClassName);
                alertdialog = builder.create();
                alertdialog.show();

                btnSubmitClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertdialog.cancel();
                        editClassName_value = editClassName.getText().toString();
                        makeNewDirectory();
                        Log.e("Click button", editClassName_value);
                    }
                });

                Log.e("Fab add class", "Click on fab add class");
            }
        });

        /*
        layoutFabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage.setVisibility(view.VISIBLE);
                Log.e("Fab add image", "Click on fab add image");
            }
        });
        */
        DatabaseReference directoryRef = database.getReference("directories").child("directory_name");

        directoryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                count++;

                startAnim();

                if(count >= dataSnapshot.getChildrenCount()){
                    stopAnim();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String filePath = storageRef.getPath();
        Log.e("Ref : ", (String.valueOf(filePath)));

        Query retrivedData = database.getReference("directories").child("directory_name").orderByValue();
        FirebaseListOptions<String> options = new FirebaseListOptions.Builder<String>()
                .setQuery(retrivedData, String.class)
                .setLayout(R.layout.item_class)
                .build();

        adapter = new FirebaseListAdapter<String>(options) {
            @Override
            protected void populateView(View v, String model, int position) {
                final TextView classTitle = (TextView) v.findViewById(R.id.class_title);
                final Button addImage = (Button) v.findViewById(R.id.class_add);
                final View viewBtn = v;
                classTitle.setText(model);

                addImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(className != null){
                            classNameIn = String.valueOf(classTitle.getText());
                            setClassName(classNameIn);
                            ResultHolder.setClassName(classNameIn);
                            Log.e("Class name", classNameIn);
                            className = null;
                        }
                        else {
                            Log.e("Class name is", "null");
                        }
                        startCapture();
                    }
                });

                layoutFabAddImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addImage.setVisibility(viewBtn.INVISIBLE);
                        Log.e("Fab add image", "Click on fab add image");
                    }
                });


            }
        };

        final ListView lv = (ListView) findViewById(R.id.class_list_view);
        lv.setAdapter(adapter);


    }

    private void startCapture(){
        Intent intent = new Intent(this, CapturePhoto.class);
        startActivity(intent);
    }

    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabAddImage.setVisibility(View.INVISIBLE);
        layoutFabAddClass.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fabExpanded = false;
    }
    //Opens FAB submenus
    private void openSubMenusFab(){
        //layoutFabAddImage.setVisibility(View.VISIBLE);
        layoutFabAddClass.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    private void makeNewDirectory(){

        databaseRef = database.getInstance().getReference("directories");
        databaseRef = databaseRef.child("directory_name");
        databaseRef.push().setValue(editClassName_value);

    }

    public void setClassName(String name){
        classNameIn = name;
    }

    void startAnim(){
        avi.smoothToShow();
    }

    void stopAnim(){
        avi.smoothToHide();
    }

    @Override
    public void onBackPressed()
    {
        backToMainActivity();
    }

    public void backToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
