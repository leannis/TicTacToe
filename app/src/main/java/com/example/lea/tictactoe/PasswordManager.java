package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import java.util.concurrent.ExecutionException;

/*
*CLASS PASSWORDMANAGER.JAVA
*
* Enthält Funktionen, die zum User-Managementprüfung nötig sind.
*
 */

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

    //User zur Datenbank hinzufügen
    public void addUser(String user, String password) {
        String query = "insert into users (user, password) values('" + user + "','" + password + "');";

        backgroundTask = new BackgroundTask("addData", act);
        backgroundTask.execute(query);
        act.finish();
    }

    //Prüfen ob ein User bereits registriert ist.
    public boolean checkUserRegister(String user) throws ExecutionException, InterruptedException {


        String res = new BackgroundTask("getUser", cont).execute("select user, " +
                "password from users where user='" + user + ";").get();
        System.out.println(res);
        if (tools.checkResult(res)){
            System.out.println("true");
            return true;
        }
        else{
            System.out.println("false");
            return false;
        }

    }
}
