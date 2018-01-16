package com.apps.hassan.thaifoodidentifier;

import android.os.Bundle;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private List<String> arr;
    private String editClassName_value;
    public FirebaseDatabase database;
    public DatabaseReference databaseRef;
    MainActivity mainActivity = new MainActivity();


    AlertDialog.Builder builder;
    LayoutInflater layoutinflater;
    AlertDialog alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getInstance().getReference();


        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        layoutFabAddClass = (LinearLayout) this.findViewById(R.id.layoutFabAddClass);
        layoutFabAddImage = (LinearLayout) this.findViewById(R.id.layoutFabAddImage);


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
                        updateUI();

                        //Log.e("type directory name list", tp)

                        Log.e("Click button", editClassName_value);
                    }
                });

                Log.e("Fab add class", "Click on fab add class");
            }
        });



        layoutFabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Fab add image", "Click on fab add image");

                Log.e("Value is: ", String.valueOf(mainActivity.directoryNameList));
            }
        });


        String filePath = storageRef.getPath();
        Log.e("Ref : ", (String.valueOf(filePath)));

        classListView = (ListView) findViewById(R.id.class_list_view);
        arr = new ArrayList<String>();
        arr.add("Hello");
        arr.add("My");
        arr.add("Name");
        mAdapter = new ArrayAdapter<>(this, R.layout.item_class, R.id.class_title, mainActivity.getArrayList());
        classListView.setAdapter(mAdapter);
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
        layoutFabAddImage.setVisibility(View.VISIBLE);
        layoutFabAddClass.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    private void makeNewDirectory(){

        databaseRef = database.getInstance().getReference("directories");
        databaseRef = databaseRef.child("directory_name");
        databaseRef.push().setValue(editClassName_value);

    }

    private void updateUI() {

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this, R.layout.item_class, R.id.class_title, mainActivity.getArrayList());
            classListView.setAdapter(mAdapter);
            Log.e("Update UI", "mAdapter == null");
        } else {
            mAdapter.clear();
            mAdapter.addAll(mainActivity.getArrayList());
            mAdapter.notifyDataSetChanged();
            //Empty array list
            Log.e("getArrayList",String.valueOf(mainActivity.getArrayList()));
            Log.e("Update UI", "mAdapter != null");
        }

    }


}
