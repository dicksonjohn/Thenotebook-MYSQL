package com.example.admin.thenotebook_mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by ADMIN on 25/07/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(Context context)
    {
        super(context,DATABASE_NAME,null,4);
    }


    static String DATABASE_NAME="UserDataBase";



    //Login table AKA UserTable
    public static final String TABLE_NAME="UserTable";

    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Name="name";

    public static final String Table_Column_2_Email="email";

    public static final String Table_Column_3_Password="password";

    //Diary table
    public static final String TABLE2_NAME="DiaryTable";

    public static final String Table_Column_1_date="date";

    public static final String Table_Column_2_diary="diary";

    //Task table
    public static final String TABLE3_NAME="TaskTable";
    public static final String Table_Column_2_username="username";
    public static final String Table_Column_1_task="todo";
    public static final String _ID = BaseColumns._ID;




    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {


        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_1_Name+" VARCHAR, "+Table_Column_2_Email+" VARCHAR, "+Table_Column_3_Password+" VARCHAR)";
        database.execSQL(CREATE_TABLE);

        String CREATE_TABLE_DIARY="CREATE TABLE IF NOT EXISTS "+TABLE2_NAME+" ("+Table_Column_1_date+" VARCHAR PRIMARY KEY, "+Table_Column_2_diary+" VARCHAR)";
        database.execSQL(CREATE_TABLE_DIARY);

        String CREATE_TABLE_TODO= "CREATE TABLE IF NOT EXISTS "+TABLE3_NAME+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +Table_Column_1_task+" VARCHAR, "+Table_Column_2_username+" VARCHAR)";
        database.execSQL(CREATE_TABLE_TODO);
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS "+TABLE2_NAME);
        database.execSQL("DROP TABLE IF EXISTS "+TABLE3_NAME);
        onCreate(database);
    }


    public Boolean insertData(String date, String diary){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Table_Column_1_date,date);
        contentValues.put(Table_Column_2_diary,diary);
        //Checks if data is already there and replaces(Source: https://stackoverflow.com/questions/26326696/how-to-prevent-to-insert-duplicate-value-in-sqlite-databse-if-duplicate-then-ov)
        long result = database.insertWithOnConflict(TABLE2_NAME, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);

        if (result==-1)
            return false;
        else
            return true;

    }


    //Query for diary entry using DATE
    public Cursor getAllData(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res=database.rawQuery("select * from " + TABLE2_NAME,null);//"where "+Table_Column_1_date+"?=? ", new String[] {String.valueOf(date)});
        return res;
    }
}
