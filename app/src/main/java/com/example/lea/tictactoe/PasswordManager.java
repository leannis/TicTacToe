package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

public class PasswordManager {

    DBAccess access;

    private Activity act;
    private Context cont;
    private BackgroundTask backgroundTask;
    private Tools tools;


    public PasswordManager(Context cont) {
        access = new DBAccess(cont, "ttt_db");
        act = (Activity) cont;
        this.cont = cont;
        this.tools = new Tools(cont);
    }

    public void addUser(String user, String password) {
        String query = "insert into users (user, password) values('" + user + "','" + password + "');";

        backgroundTask = new BackgroundTask("addData", act);
        backgroundTask.execute(query);
        act.finish();
    }

    public int checkUserRegister(String user) {

        Cursor cursor = access.db.query("users", null, "user = " + "'" +
                user + "'", null, null, null, null);

        if (cursor.getCount() > 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
