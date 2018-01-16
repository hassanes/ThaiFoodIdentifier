package com.apps.hassan.thaifoodidentifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    public String allPath = "";
    public static ArrayList<String> directoryNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        getPathValue();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            Intent main_intent = new Intent(this, MainActivity.class);
            startActivity(main_intent);
        } else if (id == R.id.nav_contributor) {
            Intent contributor_intent = new Intent(this, ContributorActivity.class);
            startActivity(contributor_intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startCameraActivity(){
        directoryNameList = stringProcess(allPath);
        /*
        new FinestWebView.Builder(this)
                .theme(R.style.RedTheme)
                .showUrl(false)
                .titleDefault("Restaurant nearby")
                .swipeRefreshColorRes(R.color.redPrimaryDark)
                .webViewSupportZoom(true)
                .show("http://wongnai.com");
                */
        //Intent intent = new Intent(this, BrowseWebActivity.class);
        //startActivity(intent);
    }

    private void getPathValue(){
        Query retrivedData = database.getReference("directories").orderByValue();
        retrivedData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    allPath = String.valueOf(postSnapshot.getValue());
                    Log.e("Path",allPath);
                    //directoryNameList = stringProcess(allPath);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public static ArrayList<String> stringProcess(String stringIn){
        stringIn = stringIn.substring(17,(stringIn.length() - 2));
        String[] stringList = stringIn.split(",");
        List<String> itemList = Arrays.asList(stringList);
        ArrayList<String> filterElement = new ArrayList<String>();

        for (int counter = 0; counter < itemList.size(); counter++) {
            String element = String.valueOf(itemList.get(counter));
            String[] elementList = element.split("=");
            filterElement.add(elementList[1]);
        }
        return filterElement;
    }

    public ArrayList<String> getArrayList(){
        return  directoryNameList;
    }
}
