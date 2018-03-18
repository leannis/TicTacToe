package com.example.lea.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
/*
*CLASS TOOLS.JAVA
*
* Diese Klasse beinhaltet eine Reihe von Methoden, die immer wieder im Laufe des Programms
* verwendet werden. Dazu gehören z.B. Custom-Messageboxen etc.
* Außerdem enthält sie zentrale Informationen zum Spiel selbst.
*
 */
public  class Tools {

    public Context con;
    public static int flag = 0;
    public static int game; //Spiel-ID
    public static String logged_user; //aktuell eingeloggter Spieler

    public Tools(Context cont) {
        this.con = cont;
    }

    public enum MsgState {
        REGISTER, EXIT, ACCEPT, LOGOUT, ACCEPT_AND_EXit
    }

    public void showMsgBox(String msg, MsgState state) {

        //Zeigt Messageboxen an, es wird ein MsgState übergeben. Anhand von Diesem können
        //unterschiedliche Reaktionen erfolen.

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

        //Zeigt einen Toast (ähnlich wie Benachrichtung) mit der übergebenen Nachricht
        //msg an.

        Context context = con.getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public boolean checkResult(String input){

        //Trimmt einen String input auf den Wert zwischen [ und ]
        //Ist dieser Länger als 0, gibt sie true zurück, sonst null.
        //Wird zum Überprüfen von Datenbankresults verwendet.

        String debug = input.substring(input.indexOf('[') +1, input.indexOf(']') );

        if (debug.length() < 1){
            return false;
        }
        else{
            return true;
        }
    }

    public String parse(String key, String input){

        //Gibt aus einem String input den zugehörigen Key zurück.
        //Die Ergebnisse aus der Datenbank kommen im Folgenden Format:
        // ".....([Spalte]":"[Wert]).....
        //Um nun den Wert zu einer Spalte (key) zurückzugeben, wird diese Funktion
        //benötigt. Wird der eingegebene Key nicht im input gefunden, wirde -1 zurückgegeben

        if(input.indexOf(key) > 0){
            String debug = input.substring(input.indexOf(key)+key.length()+3, input.length());
            String ret= debug.substring(0, debug.indexOf("\""));
            return ret;
        }
        return "-1";
    }
}
