package com.example.lea.tictactoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;



/*
*CLASS TICTACTOE.JAVA
* Diese Klasse gehört zur Activity TicTacToe. Sie beherbergt die Spiellogik und das Spiel an sich.
* Die Logik für alle 3 Spielmodi begindet sich in einer Klasse.
* Über den statischen Parameter gamemode wird von außen übergeben, um welchen Modus es sich handelt.
*
 */

public class TicTacToe extends AppCompatActivity {


    //Allgemein
    public int g_player = 1;
    Button buttons[][] = new Button[3][3];
    ColorDrawable colors[][] = new ColorDrawable[3][3];
    Boolean check = false;
    TextView player_view;
    int i, j;
    Context con = this;
    static int gamemode; // 1: single, 2: multi, 3: multi web
    int flag_check;
    int flag_winner;
    int move_count = 0;
    Tools tools;

    //Einzelspieler
    Random rand = new Random();
    //Mehrspieler

    //Mehrspieler Web
    String res2 = "";
    Timer timer;


    //beim Starten der Activity
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tic_tac_toe);
        tools = new Tools(this);
        Button homeButton = (Button) findViewById(R.id.b_home);

        if (gamemode == 3) {
            tools.showMsgBox("Your game is " + Tools.game + "\nYou are player " + Tools.flag, Tools.MsgState.ACCEPT);
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicTacToe.this, StartScreen.class));
            }
        });


        //Füllen des Spielfeld-Array mit den zugehörigen Buttons
        buttons[0][0] = (Button) findViewById(R.id.b_1_1);
        buttons[0][1] = (Button) findViewById(R.id.b_1_2);
        buttons[0][2] = (Button) findViewById(R.id.b_1_3);
        buttons[1][0] = (Button) findViewById(R.id.b_2_1);
        buttons[1][1] = (Button) findViewById(R.id.b_2_2);
        buttons[1][2] = (Button) findViewById(R.id.b_2_3);
        buttons[2][0] = (Button) findViewById(R.id.b_3_1);
        buttons[2][1] = (Button) findViewById(R.id.b_3_2);
        buttons[2][2] = (Button) findViewById(R.id.b_3_3);

        player_view = (TextView) findViewById(R.id.playerView);
        player_view.setText("current player: " + g_player);

        //Initialisieren des Boards. Versorgen der Buttons mit den zugehörigen Actionlistenern.
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                buttons[i][j].setBackgroundColor(Color.GRAY);
                final Button temp = buttons[i][j];

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            make_move(temp);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        //Im Multiplayer Web Modus wird in regelmäßigen Abständen über einen Timer die Datenbank abgefragt und die GUI aktualisiert.
        if (gamemode == 3) {

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        res2 = (new BackgroundTask("getGame", con).execute("select id, flag, move_count from game where id = " + Tools.game + ";")).get();

                        if (flag_check == -1) {
                            timer.cancel();
                            timer.purge();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    refresh(); //GUI aktualisieren
                                    String str_move = new BackgroundTask("getGame", con).execute("select id, flag, move_count from game where id = " + Tools.game + ";").get();
                                    move_count = Integer.parseInt(tools.parse("move_count", str_move));
                                    boolean check = check_winner();
                                    if (check) {

                                        if(flag_check == 1) {
                                            tools.showMsgBox("Player 2 won!", Tools.MsgState.ACCEPT_AND_EXit);
                                        }else if (flag_check == 2){
                                            tools.showMsgBox("Player 1 won!", Tools.MsgState.ACCEPT_AND_EXit);
                                        }

                                        timer.cancel();
                                        timer.purge();
                                    } else if (!check && move_count == 9) {


                                        tools.showMsgBox("Tied!", Tools.MsgState.ACCEPT_AND_EXit);
                                        timer.cancel();
                                        timer.purge();
                                    }



                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        if (res2.length() > 0) {
                            flag_check = Integer.parseInt(tools.parse("flag", res2));
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }, 100, 1000);
        }
    }


    //Haputmethode zum Ausführen eines Zuges
    public void make_move(Button b) throws ExecutionException, InterruptedException {

        if (gamemode == 1) {
            //Spieler führt Zug aus
            boolean ret = player_move(b);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Wenn noch kein Gewinner da ist, und der Spieler seinen Zug erfolgreich ausgeführt hat,
            //darf der Computer seinen Zug ausführen
            if (move_count != 9 && !check && ret==true) {
                cpu_move();
            }

        }

        if (gamemode == 2) {
            player_move(b);
        }

        if (gamemode == 3) {

            //Prüfen, ob man selbst an der Reihe ist.
            if (flag_check == Tools.flag) {

                int x = 0, y = 0;

                //X und Y Koordinaten für den geklicken Button herausfinden.
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttons[i][j].getId() == b.getId()) {

                            x = i;
                            y = j;
                            break;
                        }
                    }
                }

                //Wert in der Tabelle für Reihe X und Spalte Y herausfinden.
                String value = new BackgroundTask("getField", con).execute("select row, col" + y + " from field where row = " + x + ";").get();
                int val = Integer.parseInt(tools.parse("column", value));

                //Wenn noch kein Spieler seine Kennung reingesetzt hat, darf der Spieler das Feld belegen.
                if (val == 0) {
                    String query2 = "update field set col" + y + " = " + Tools.flag + " where row = " + x + ";";

                    new BackgroundTask("addData", this).execute(query2);

                    //Spielerwechsel
                    int temp = 0;
                    if (Tools.flag == 1) {
                        temp = 2;
                    } else if (Tools.flag == 2) {
                        temp = 1;
                    }

                    String str_mc = new BackgroundTask("getGame", this).execute("select id, flag, move_count from game where id = " + Tools.game + ";").get();
                    move_count = Integer.parseInt(tools.parse("move_count", str_mc)) + 1;


                    new BackgroundTask("addData", this).execute("update game set move_count = " + move_count + " where id = " + Tools.game + ";");
                    new BackgroundTask("addData", this).execute("update game set flag = " + temp + " where id = " + Tools.game + ";");
                } else {
                    tools.showMsgBox("Field already taken", Tools.MsgState.ACCEPT);
                }
            } else {
                tools.showToast("It's not your turn!");
            }
        }
    }

    //Spieler führt Zug aus
    private boolean player_move(Button b) throws ExecutionException, InterruptedException {

        if (!set_field_player(b)) {
            tools.showMsgBox("Field already taken", Tools.MsgState.ACCEPT);
            return false;
        } else {
            set_field_player(b);

            check = check_winner();
            if (check) {
                if (gamemode == 2) {
                    tools.showMsgBox("Player " + g_player + " won!", Tools.MsgState.ACCEPT_AND_EXit);
                } else if (gamemode == 1) {
                    tools.showMsgBox("You won!", Tools.MsgState.ACCEPT_AND_EXit);
                }
            }
            if (g_player == 1) {
                g_player = 2;
            } else if (g_player == 2) {
                g_player = 1;
            }
        }
        if (!check && move_count == 9) {
            tools.showMsgBox("Tied!", Tools.MsgState.ACCEPT_AND_EXit);
        }
        if (!check) {
            if (gamemode == 1) {
                player_view.setText("current player: computer");
            } else {
                player_view.setText("current player: player " + g_player);
            }
        }
        return true;
    }

    //KI führt Zug aus
    private void cpu_move() {

        int rand_x = rand.nextInt(2 + 1);
        int rand_y = rand.nextInt(2 + 1);

        if (!set_field_cpu(buttons[rand_x][rand_y])) {
            cpu_move();
        } else {
            set_field_cpu(buttons[rand_x][rand_y]);
        }
        check = check_winner();
        if (check) {
            tools.showMsgBox("Computer won!", Tools.MsgState.ACCEPT_AND_EXit);
        }
        if (!check && move_count == 9) {
            tools.showMsgBox("Tied!", Tools.MsgState.ACCEPT);
        }
        player_view.setText("current player: player 1");
        g_player = 1;

    }

    //Spielfelder setzen
    @SuppressLint("SetTextI18n")
    public boolean set_field_player(Button b) {
        ColorDrawable buttonColor = (ColorDrawable) b.getBackground();

        if (buttonColor.getColor() == -65536 || buttonColor.getColor() == -256) {
            return false;
        } else {
            if (gamemode == 2) {
                if (g_player == 1) {
                    b.setBackgroundColor(Color.RED);
                    move_count++;
                    return true;
                } else if (g_player == 2) {
                    b.setBackgroundColor(Color.YELLOW);
                    move_count++;
                    return true;
                } else {
                    return false;
                }
            }
            if (gamemode == 1) {
                b.setBackgroundColor(Color.RED);
                move_count++;
                return true;
            }
        }
        return false;
    }

    //KI Spielfelder setzen
    public boolean set_field_cpu(Button b) {
        ColorDrawable buttonColor = (ColorDrawable) b.getBackground();

        if (buttonColor.getColor() == -65536 || buttonColor.getColor() == -256) {
            return false;
        } else {

            b.setBackgroundColor(Color.YELLOW);
            move_count++;
            return true;
        }
    }

    //Spielfeld in der Datenbank nachlesen und GUI updaten
    public void refresh() throws ExecutionException, InterruptedException {

        System.out.println("REFRESH");
        player_view.setText("current player: player " + flag_check);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String query = "select row, col" + j + " from field where row = " + i + ";";
                String res = new BackgroundTask("getField", con).execute(query).get();
                int col = Integer.parseInt(tools.parse("column", res));
                if (col == 1) {
                    buttons[i][j].setBackgroundColor(Color.RED);
                } else if (col == 2) {
                    buttons[i][j].setBackgroundColor(Color.YELLOW);
                }
            }
        }
    }

    //Gewinner prüfen, diese ruft verschachtelt die Folgenden Methoden auf
    public boolean check_winner() {
        //rot  -65536 //gelb -256 // grau -7829368
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colors[i][j] = (ColorDrawable) buttons[i][j].getBackground();
            }
        }
        return (checkRows() || checkCols() || checkDia());
    }

    //Reihen prüfen
    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(colors[i][0].getColor(), colors[i][1].getColor(), colors[i][2].getColor())) {
                return true;
            }
        }
        return false;
    }

    //Spalten prüfen
    private boolean checkCols() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(colors[0][i].getColor(), colors[1][i].getColor(), colors[2][i].getColor())) {
                return true;
            }
        }
        return false;
    }

    //Diagonalen prüfen
    private boolean checkDia() {
        return ((checkRowCol(colors[0][0].getColor(), colors[1][1].getColor(), colors[2][2].getColor()))
                || (checkRowCol(colors[0][2].getColor(), colors[1][1].getColor(), colors[2][0].getColor())));
    }

    //3 Felder miteinander Vergleichen
    private boolean checkRowCol(int i1, int i2, int i3) {
        return ((i1 != -7829368) && (i1 == i2) && (i2 == i3));
    }

    //Zurücktaste
    @Override
    public void onBackPressed() {
        String query = "delete from game where id = '" + Tools.game + "';";
        new BackgroundTask("addData", getParent()).execute("update users set logged = 0 where user = '" + Tools.logged_user + "';"); //Ausloggen
        new BackgroundTask("addData", this).execute(query);
        tools.showMsgBox("Exit app?", Tools.MsgState.EXIT);
    }
}