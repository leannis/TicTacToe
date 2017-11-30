package com.example.lea.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Patrick on 30.11.2017.
 */

public class DBAccess extends SQLiteOpenHelper {

    public SQLiteDatabase db;

    public DBAccess(Context activity, String dbname){
        super(activity, dbname, null, 1);
        db = getWritableDatabase();



        Cursor test = db.query("users", null, null, null, null, null, null);
        System.out.println("***** :" + test.getCount());


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

    public long addUser(String user, String password){
        ContentValues data = new ContentValues();
        data.put("user", "test");
        data.put("password", "test123");

        return db.insert("users", null, data);
    }

    public Cursor getUserPW(long id){
        Cursor result = db.query("users", null, "id = " + id, null, null, null, null);
        return result;
    }
}
