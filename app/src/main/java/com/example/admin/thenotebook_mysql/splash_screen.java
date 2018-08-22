package com.example.admin.thenotebook_mysql;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        Thread myThread=new Thread(){
            @Override

            public void run(){
                try {
                    sleep(3000);
                    Intent intent=new Intent(getApplicationContext(),ActivityLogin.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
