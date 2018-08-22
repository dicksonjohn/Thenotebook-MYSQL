package com.example.admin.thenotebook_mysql;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Todo extends ListActivity {
    private ListAdapter todoListAdapter;
    private SQLiteHelper todoListSQLHelper;
    Button addtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        updateTodoList();
        addtask=(Button)findViewById(R.id.addtask);
        final String username=getIntent().getStringExtra("username");
        Toast.makeText(this,"Welcome "+username,Toast.LENGTH_LONG).show();
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(Todo.this);
                todoTaskBuilder.setTitle("Add a new Task");
                todoTaskBuilder.setMessage("Enter the task...");
                final EditText todoET = new EditText(getApplicationContext());
                todoTaskBuilder.setView(todoET);

                todoTaskBuilder.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String todoTaskInput = todoET.getText().toString();
                        todoListSQLHelper = new SQLiteHelper(Todo.this);
                        SQLiteDatabase sqLiteDatabase = todoListSQLHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.clear();

                        //write the Todo task input into database table
                        values.put(SQLiteHelper.Table_Column_1_task, todoTaskInput);
                        values.put(SQLiteHelper.Table_Column_2_username, username);
                        sqLiteDatabase.insertWithOnConflict(SQLiteHelper.TABLE3_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                        //update the Todo task list UI
                        updateTodoList();
                    }
                });

                todoTaskBuilder.setNegativeButton("Cancel", null);

                todoTaskBuilder.create().show();
            }
        });

    }

    //update the todo task list
    private void updateTodoList() {
        todoListSQLHelper = new SQLiteHelper(Todo.this);
        SQLiteDatabase sqLiteDatabase = todoListSQLHelper.getReadableDatabase();

        //cursor to read todo task list from database
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE3_NAME,
                new String[]{SQLiteHelper._ID, SQLiteHelper.Table_Column_1_task},
                null, null, null, null, null);

        //binds the todo task list with the UI
        todoListAdapter = new SimpleCursorAdapter(this,
                R.layout.todotask, cursor, new String[]{SQLiteHelper.Table_Column_1_task}, new int[]{R.id.todoTaskTV}, 0);

        this.setListAdapter(todoListAdapter);
    }

    //deleting the todo task item
    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView todoTV = (TextView) v.findViewById(R.id.todoTaskTV);
        String todoTaskItem = todoTV.getText().toString();

        String deleteTodoItemSql = "DELETE FROM " + SQLiteHelper.TABLE3_NAME +
                " WHERE " + SQLiteHelper.Table_Column_1_task + " = '" + todoTaskItem + "'";

        todoListSQLHelper = new SQLiteHelper(Todo.this);
        SQLiteDatabase sqlDB = todoListSQLHelper.getWritableDatabase();
        sqlDB.execSQL(deleteTodoItemSql);
        updateTodoList();
    }

    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Leaving To-do list?")
                .setMessage("Are you sure you want to leave To-do list?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        String username=getIntent().getStringExtra("username");
                        Intent intent=new Intent(Todo.this,select.class);
                        startActivity(intent);
                        intent.putExtra("username",username);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                    }
                }).create().show();
    }
}
