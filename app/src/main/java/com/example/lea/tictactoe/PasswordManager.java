package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONException;

import java.util.Map;

public class PasswordManager {

    DBAccess access;

    private Activity act;
    private BackgroundTask backgroundTask;

    public PasswordManager(Context cont) {
       access = new DBAccess(cont, "ttt_db");
       act = (Activity) cont;
    }

    public void addUser(String user, String password){
        // variable query request
        String query = "insert into users (user, password) values('"+user+"','"+password+"');";

        backgroundTask = new BackgroundTask("addData");
        backgroundTask.execute(query);
        act.finish();
    }

    public void sentUserRequest(String user) {
        // variable query request

        System.out.println("sent user request");

        String query = "select user, password from users where user='" + user + "';";
        backgroundTask = new BackgroundTask("getData");
        backgroundTask.execute(query);
    }

    public int checkUser(String user, String pw) {
        // 1 = OK
        // 2 = PW falsch
        // 3 = User gibts nicht

        backgroundTask = new BackgroundTask();
        try {
            backgroundTask.getUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO check user and pw

        return 1;
    }

    public  int checkUserRegister(String user) {

        Cursor cursor = access.db.query("users", null, "user = " + "'" +
                user + "'",null,null,null,null);

        if (cursor.getCount() > 0 ) {
            return -1;
        } else {
            return 1;
        }
    }
}
