package com.example.admin.thenotebook_mysql;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener{

    private static final String LOGIN_URL = "https://diarydb.000webhostapp.com/login.php";
    private EditText editTextUserName;
    private EditText editTextPassword;
    private Button buttonLogin,buttonRegister;
    SharedPreferences logindb;
    SharedPreferences.Editor logineditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);
        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        logindb=getSharedPreferences("db1",MODE_PRIVATE);
        logineditor=logindb.edit();

        //Register for new account
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        editTextUserName.setText(logindb.getString("text", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextUserName.setText(logindb.getString("text",""));
    }

    private void login(){
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        userLogin(username,password);
        logineditor.putString("text", String.valueOf(username));
        logineditor.commit();
    }

    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityLogin.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.contains("success")){
                    Intent intent = new Intent(ActivityLogin.this,select.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    editTextUserName.setText("");
                    editTextPassword.setText("");
                }if(s.contains("Invalid Username or Password")){
                    Toast.makeText(ActivityLogin.this,s, Toast.LENGTH_LONG).show();
                }
                if(s.equals("")){
                    Toast.makeText(ActivityLogin.this,"Check your Network Connection!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            login();
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();

        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
