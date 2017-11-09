package com.example.lea.tictactoe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by Patrick on 30.11.2017.
 */

public class Tools {

    public boolean msg_registry = false;
    public Context con;

    public Tools(Context cont) {
        this.con = cont;
    }

    public void showMsgBox(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("TicTacToe");
        builder.setMessage(msg);

        if (msg_registry == false) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
        } else {
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    con.startActivity(new Intent(con, SignUp.class));
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
