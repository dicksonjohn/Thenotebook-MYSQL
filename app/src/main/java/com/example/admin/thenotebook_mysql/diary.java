package com.example.admin.thenotebook_mysql;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;




public class diary extends AppCompatActivity {


    private static final String DIARY_URL = "https://diarydb.000webhostapp.com/diary.php";
    private static final String UPDATE_DIARY_URL = "https://diarydb.000webhostapp.com/update.php";
    private static final String URL_DELETE = "https://diarydb.000webhostapp.com/delete.php?date=";
    public static final String DATA_URL = "https://diarydb.000webhostapp.com/search.php?date=";
    public static final String KEY_DIARYVALUE = "diaryentry";
    public static final String KEY_LOCATION = "location";
    public static final String JSON_ARRAY = "result";
    private static final String TAG = "Diary";

    EditText showdate, diaryentryED;
    FloatingActionButton fab, fab1, updatebtn, deletebtn;
    TextView locationTV;
    private Button pickdate;
    private DatePickerDialog.OnDateSetListener DateSetListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        updatebtn = (FloatingActionButton) findViewById(R.id.updatebtn);
        deletebtn = (FloatingActionButton) findViewById(R.id.deletebtn);
        showdate = (EditText) findViewById(R.id.showdate);
        pickdate = (Button) findViewById(R.id.pickdate);
        diaryentryED = (EditText) findViewById(R.id.diaryentry);
        locationTV = (TextView) findViewById(R.id.locationTV);
        String username=getIntent().getStringExtra("username");
        String location=getIntent().getStringExtra("locationintent");
        Toast.makeText(this,"Welcome "+username,Toast.LENGTH_LONG).show();
        locationTV.setText(location);

        pickdate.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        diary.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListner,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        DateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                showdate.setText(date);
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryentries();
                }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getData();
                Toast.makeText(diary.this,"Data Retrieved",Toast.LENGTH_SHORT).show();

            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    confirmUpdateEntry();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteEntry();
            }
        });
    }



   private void diaryentries() {
        String date = showdate.getText().toString().trim();
        String diaryvalue = diaryentryED.getText().toString().trim();
        String username=getIntent().getStringExtra("username");
        String location=getIntent().getStringExtra("locationintent");
        Diary(date,diaryvalue,username,location);
    }

    private void Diary(String date,String diaryvalue,String username,String location) {
        class DiaryClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(diary.this, "Saving Diary entry", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                    if (s.equals("")){
                        Toast.makeText(diary.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(diary.this,s, Toast.LENGTH_LONG).show();
                    }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("date", params[0]);
                data.put("diaryentry", params[1]);
                data.put("username",params[2]);
                data.put("location",params[3]);

                String result = ruc.sendPostRequest(DIARY_URL, data);

                return result;
            }
        }
        DiaryClass dc = new DiaryClass();
        dc.execute(date, diaryvalue,username,location);
    }


    private void updatediaryentries() {
        String date = showdate.getText().toString().trim();
        String diaryvalue = diaryentryED.getText().toString().trim();
        String username=getIntent().getStringExtra("username");
        String location=getIntent().getStringExtra("locationintent");
        UpdateDiary(date,diaryvalue,username,location);
    }

    private void UpdateDiary(String date,String diaryvalue, String username, String location) {
        class UpdateDiaryClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(diary.this, "Updating entry", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("")){
                    Toast.makeText(diary.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(diary.this,s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("date", params[0]);
                data.put("diaryentry", params[1]);
                data.put("username",params[2]);
                data.put("location",params[3]);

                String result = ruc.sendPostRequest(UPDATE_DIARY_URL, data);

                return result;
            }
        }
        UpdateDiaryClass dc = new UpdateDiaryClass();
        dc.execute(date, diaryvalue, username, location);
    }

    private void confirmUpdateEntry(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to update this entry?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        updatediaryentries();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

        private ProgressDialog loading;

        private void getData() {
            String username=getIntent().getStringExtra("username").trim();
            String date = showdate.getText().toString().trim();
            if (date.equals("")) {
                Toast.makeText(this, "Please select date", Toast.LENGTH_LONG).show();
                return;
            }
            loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

            String url = DATA_URL+showdate.getText().toString().trim()+"&username="+username.trim();

            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    showJSON(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(diary.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        private void showJSON(String response){
            String diaryentry="";
            String location="";

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                JSONObject collegeData = result.getJSONObject(0);
                diaryentry = collegeData.getString(KEY_DIARYVALUE);
                location=collegeData.getString(KEY_LOCATION);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            diaryentryED.setText(diaryentry);
            locationTV.setText(location);
        }

    private void deleteEntry(){
        final String username=getIntent().getStringExtra("username");
        final String location=getIntent().getStringExtra("locationintent");
        final String date=showdate.getText().toString();
        class DeleteEntry extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(diary.this, "Deleting...", "Wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("")){
                    Toast.makeText(diary.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(diary.this,s, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                RegisterUserClass ruc= new RegisterUserClass();
                String s = ruc.sendGetRequestParam(URL_DELETE, date, username);
                return s;
            }
        }

        DeleteEntry de = new DeleteEntry();
        de.execute();
    }

    private void confirmDeleteEntry(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this entry?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEntry();
                        diaryentryED.setText("");
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Leaving diary?")
                .setMessage("Are you sure you want to leave Diary?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        String username=getIntent().getStringExtra("username");
                        Intent intent=new Intent(diary.this,select.class);
                        intent.putExtra("username",username);
                        startActivity(intent);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                    }
                }).create().show();
    }
    }

