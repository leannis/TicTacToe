package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public  class Tools {

    public Context con;
    public static int flag = 0;
    public static int game;
    public static String logged_user;

    public Tools(Context cont) {
        this.con = cont;
    }

    public enum MsgState {
        REGISTER, EXIT, ACCEPT, LOGOUT, ACCEPT_AND_EXit
    }

    public void showMsgBox(String msg, MsgState state) {

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("TicTacToe");
        builder.setMessage(msg);

        switch (state) {
            case ACCEPT:
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                break;
            case REGISTER:
                builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        con.startActivity(new Intent(con, SignUp.class));
                    }
                });
                break;
            case EXIT:
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = (Activity) con;
                        activity.finish();
                        activity.finishAffinity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case LOGOUT:
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = (Activity) con;
                        activity.finish();
                        activity.finishAffinity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing stay in the same activity
                    }
                });
                break;

            case ACCEPT_AND_EXit:


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new BackgroundTask("addData", con).execute("update field set col0 = 0, col1 = 0, col2 = 0;");
                        new BackgroundTask("addData", con).execute("delete from game");
                        con.startActivity(new Intent(con, StartScreen.class));
                    }
                });
                break;

            default:
                builder.setMessage("Unknown state!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // default value
                    }
                });
                break;
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showToast(String msg) {
        Context context = con.getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public boolean checkResult(String input){

        String debug = input.substring(input.indexOf('[') +1, input.indexOf(']') );


        if (debug.length() < 1){

            return false;
        }
        else{

            return true;
        }
    }

    public String parse(String key, String input){

        if(input.indexOf(key) > 0){
            String debug = input.substring(input.indexOf(key)+key.length()+3, input.length());
            String test= debug.substring(0, debug.indexOf("\""));
            return test;
        }
        return "-1";
    }
}
