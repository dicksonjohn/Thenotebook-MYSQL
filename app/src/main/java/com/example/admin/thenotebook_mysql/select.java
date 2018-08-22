package com.example.admin.thenotebook_mysql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class select extends AppCompatActivity {
    ImageButton diary,todo;
    private static final  int MY_PERMISSION_REQUEST_LOCATION=1;
    String locationintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        String username=getIntent().getStringExtra("username");
        Intent intent = new Intent(getApplicationContext(), Todo.class);
        intent.putExtra("username",username);

        diary=(ImageButton)findViewById(R.id.diary);
        todo=(ImageButton)findViewById(R.id.todo);
        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //granting permission to use location
                if (ContextCompat.checkSelfPermission(select.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(select.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        ActivityCompat.requestPermissions(select.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                    }else {
                        ActivityCompat.requestPermissions(select.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                    }
                }else{
                    LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        locationintent=hereLocation(location.getLatitude(),location.getLongitude());
                        Toast.makeText(select.this,locationintent,Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(select.this, "Not found", Toast.LENGTH_SHORT).show();
                    }

                }
                Thread mythread=new Thread(){
                    @Override
                    public void run(){
                        try {
                            sleep(3000);
                            String username=getIntent().getStringExtra("username");
                            Intent intent = new Intent(getApplicationContext(), diary.class);
                            intent.putExtra("username",username);
                            intent.putExtra("locationintent",locationintent);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mythread.start();
                }
        });
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=getIntent().getStringExtra("username");
                Intent intent = new Intent(getApplicationContext(), Todo.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST_LOCATION:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(select.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{
                            locationintent=hereLocation(location.getLatitude(),location.getLongitude());
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(select.this, "Not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this, "No permissions granted",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //get closest city name

    public String hereLocation(double lat, double lon){
        String ourCity="";
        //converting geographic coordinates into a human-readable address.
        Geocoder geocoder=new Geocoder(select.this, Locale.getDefault());
        List<Address> addressList;
        try{
            addressList=geocoder.getFromLocation(lat,lon,1);
            if (addressList.size()>0){
                ourCity=addressList.get(0).getLocality();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  ourCity;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("You will be Signed out")
                .setMessage("Are you sure you want to Logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //diary.super.onBackPressed();
                        Intent intent=new Intent(select.this,ActivityLogin.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}
