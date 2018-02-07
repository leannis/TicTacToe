package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;


public class Tools {

    public boolean msg_registry = false;
    public Context con;

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
                        System.exit(0);
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
                       con.startActivity(new Intent(con, LogIn.class));
                       Activity act = (Activity) con;
                       act.finish();
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
}
