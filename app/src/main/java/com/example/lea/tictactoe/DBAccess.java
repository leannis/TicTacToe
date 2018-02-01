package com.example.lea.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public  class DBAccess extends SQLiteOpenHelper {

    public  SQLiteDatabase db;

    public DBAccess(Context context, String dbname){
        super(context, dbname, null, 1);
        db = getWritableDatabase();

        Cursor test = db.query("users", null, null, null, null, null, null);
        System.out.println("***** :" + test.getCount());

        Cursor res = db.query("users", null, null, null, null, null, null);

        if (res.getCount() == 0){
            ContentValues init = new ContentValues();
            init.put("user", "test");
            init.put("password", "test123");
            db.insert("users", null, init);
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            String sql = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, user varchar(50), password varchar(50))";
            db.execSQL(sql);
        }
        catch(Exception ex){
            Log.e("ÄÄÄÄHHHHH", ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized  void close(){
        if(db != null){
            db.close();
            db = null;
        }
        super.close();
    }


    public  String getUserPWbyName(String user){
        System.out.println("DEBUG");
        Cursor result = db.query("users", null, "user = " + "'" + user + "'", null, null, null, null);

        System.out.println("Anzahl in Tabelle : " + result.getCount());

        if (result.getCount() > 0) {
            result.moveToFirst();
            String temp = result.getString(result.getColumnIndex("password"));
            return temp;
        }
        else{
            return null;
        }
    }
}
