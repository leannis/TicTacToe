package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.HashMap;

public class PasswordManager {

    DBAccess access;


    public PasswordManager(Context cont) {

       access = new DBAccess(cont, "ttt_db");



    }

    public long addUser(String user, String password){
        ContentValues data = new ContentValues();
        data.put("user", user);
        data.put("password", password);



        return access.db.insert("users", null, data);

    }



    public  int checkUser(String user, String pw) {
        // 1 = OK
        // 2 = PW falsch
        // 3 = User gibts nicht
        String pw_by_name = access.getUserPWbyName(user);
        System.out.println("PW VOM USER " + user + " : " + pw_by_name);


        if (pw_by_name != null) {
            if (pw_by_name.equals(pw)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }

    }

    public  int checkUserRegister(String user) {

        Cursor cursor = access.db.query("users", null, "user = " + "'" + user + "'",null,null,null,null);

        if (cursor.getCount() > 0 ) {
            return -1;
        } else {
            return 1;
        }

    }

   /* @Override
    protected void finalize() throws Throwable {
        super.finalize();
        access.close();
    }*/



}
