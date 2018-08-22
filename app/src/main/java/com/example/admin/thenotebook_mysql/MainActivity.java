package com.example.admin.thenotebook_mysql;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegister,buttonLogin;

    private static final String REGISTER_URL = "https://diarydb.000webhostapp.com/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            onButtonPressed();
        }
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim().toLowerCase();
        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String email = editTextEmail.getText().toString().trim().toLowerCase();

        register(name,username,password,email);
    }


    private void register(String name, String username, String password, String email) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Check your Network connection",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("username",params[1]);
                data.put("password",params[2]);
                data.put("email",params[3]);


                String result = ruc.sendPostRequest(REGISTER_URL,data);



                return  result;

            }}
            RegisterUser ru = new RegisterUser();
        ru.execute(name,username,password,email);
        }


    public void onButtonPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm details?")
                .setMessage("Are you sure you want to proceed")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        registerUser();

                    }
                }).create().show();
    }
    @Override
    public void onBackPressed() {
         Intent intent=new Intent(MainActivity.this,ActivityLogin.class);
         startActivity(intent);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
         }

    }

