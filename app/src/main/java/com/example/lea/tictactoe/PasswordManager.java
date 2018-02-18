package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

public class PasswordManager {

    DBAccess access;

    private Activity act;

    public PasswordManager(Context cont) {
       access = new DBAccess(cont, "ttt_db");
       act = (Activity) cont;
    }

    public void addUser(String user, String password){
        // variable query request
        String query = "insert into users (user, password, highscore) " +
                "values('"+user+"','"+password+"','0');";

        new BackgroundTask("addData").execute(query);
        act.finish();
    }

    public void sentUserRequest() {
        // variable query request
        String query = "select user, password from users where id=29;";
        BackgroundTask backgroundTask = new BackgroundTask("getData");
        backgroundTask.execute(query);
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

        Cursor cursor = access.db.query("users", null, "user = " + "'" +
                user + "'",null,null,null,null);

        if (cursor.getCount() > 0 ) {
            return -1;
        } else {
            return 1;
        }
    }
}
