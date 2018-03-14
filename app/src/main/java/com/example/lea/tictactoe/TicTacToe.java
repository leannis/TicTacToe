package com.example.lea.tictactoe;

import android.annotation.SuppressLint;
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

public class TicTacToe extends AppCompatActivity {

    public int g_player = 1;
    Button buttons[][] = new Button[3][3];
    ColorDrawable colors[][] = new ColorDrawable[3][3];

    Boolean check = false;

    Random rand = new Random();

    TextView player_view;
    int i, j;

    Timer timer;


    static int gamemode;
    // 1:   single
    // 2:   multi
    // 3:   multi web

    int move_count = 0;
    Tools tools;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        tools = new Tools(this);

        Button homeButton = (Button) findViewById(R.id.b_home);

        if(gamemode == 3){

            tools.showMsgBox("Dein Spiel ist " + Tools.game, Tools.MsgState.ACCEPT);
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicTacToe.this, StartScreen.class));
            }
        });

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
        player_view.setText("Current Player: Player " + g_player);

        //init board
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
        if(gamemode == 3) {


        }
    }

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
                    //In die DB schreiben

                    return true;
                } else if (g_player == 2) {
                    b.setBackgroundColor(Color.YELLOW);
                    //In die DB Schreiben
                    move_count++;

                    return true;
                } else {
                    return false;
                }
            }
            if (gamemode == 1) {
                b.setBackgroundColor(Color.RED);
                move_count++;
                //In die DB schreiben
                return true;
            }

        }

        return false;
    }

    public boolean set_field_cpu(Button b) {
        ColorDrawable buttonColor = (ColorDrawable) b.getBackground();


        if (buttonColor.getColor() == -65536 || buttonColor.getColor() == -256) {
            return false;
        } else {

            b.setBackgroundColor(Color.YELLOW);
            //In die DB Schreiben
            move_count++;

            return true;
        }
    }



    public void make_move(Button b) throws ExecutionException, InterruptedException {
        if (gamemode == 1) {

            player_move(b);


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (move_count != 9 && !check) {
                cpu_move();
            }

        }

        if (gamemode == 2) {
            player_move(b);
        }

        if(gamemode == 3){



            String query = "select id, flag from game where id = " + Tools.game + ";";

            String res = new BackgroundTask("getGame", this).execute(query).get();



            int flag_db = Integer.parseInt(tools.parse("flag", res));
            System.out.println("DB FLAG: " + flag_db + "\nDein Flag: " + Tools.flag);

            if(flag_db == Tools.flag){
                System.out.println("DU BIST DRAN");

                int x = 0,y = 0;

                for (int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j ++){
                        if ( buttons[i][j].getId() == b.getId()){
                            System.out.println(i + " " + j);
                            x=i;
                            y=j;
                            break;
                        }
                    }
                }
                String query2 = "update field set col"+y+" = "+Tools.flag+" where row = "+ x+";";

                new BackgroundTask("addData", this).execute(query2);

                int temp = 0;
                if(Tools.flag == 1){
                    temp = 2;
                }
                else if (Tools.flag == 2){
                    temp = 1;
                }


                new BackgroundTask("addData", this).execute("update game set flag = "+temp+" where id = "+Tools.game+";");


            }
            else{



                System.out.println("DU BIST NICHT DRAN");


                int flag_check = 0;

                while(flag_check != tools.flag){
                    String res2 = (new BackgroundTask("getGame", this).execute("select id, flag from game where id = " + Tools.game + ";")).get();
                    flag_check = Integer.parseInt(tools.parse("flag", res2));
                    Thread.sleep(500);

                }




            }

        }

    }

    public boolean check_winner() {
        //rot  -65536 //gelb -256 // grau -7829368
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colors[i][j] = (ColorDrawable) buttons[i][j].getBackground();
                System.out.println(colors[i][j].getColor());
            }
        }
        return (checkRows() || checkCols() || checkDia());
    }

    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(colors[i][0].getColor(), colors[i][1].getColor(), colors[i][2].getColor())) {
                return true;
            }
        }
        return false;
    }



    private boolean checkCols() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(colors[0][i].getColor(), colors[1][i].getColor(), colors[2][i].getColor())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDia() {
        return ((checkRowCol(colors[0][0].getColor(), colors[1][1].getColor(), colors[2][2].getColor()))
                || (checkRowCol(colors[0][2].getColor(), colors[1][1].getColor(), colors[2][0].getColor())));
    }

    private boolean checkRowCol(int i1, int i2, int i3) {
        return ((i1 != -7829368) && (i1 == i2) && (i2 == i3));
    }

    private void player_move(Button b) throws ExecutionException, InterruptedException {
        if (gamemode == 2) {
            if (!set_field_player(b)) {
                tools.showMsgBox("Field already taken", Tools.MsgState.ACCEPT);
            } else {
                set_field_player(b);

                check = check_winner();
                if (check) {
                    if (gamemode == 2) {
                        tools.showMsgBox("Player " + g_player + " won!", Tools.MsgState.ACCEPT_AND_EXit);
                    } else if (gamemode == 1) {
                        if (g_player == 2) {
                            tools.showMsgBox("Computer won!", Tools.MsgState.ACCEPT_AND_EXit);

                        } else {
                            tools.showMsgBox("You won!", Tools.MsgState.ACCEPT_AND_EXit);

                        }
                    }
                }
                if (!check && move_count == 9) {
                    tools.showMsgBox("Tied!", Tools.MsgState.ACCEPT_AND_EXit);
                }

                if (g_player == 1) {
                    g_player = 2;
                } else if (g_player == 2) {
                    g_player = 1;
                }


                if (gamemode == 1) {
                    player_view.setText("Current Player: Computer");
                } else {
                    player_view.setText("Current player: " + g_player);
                }
            }
        }



    }

    private void cpu_move() {

        int rand_x = rand.nextInt(2 + 1);
        int rand_y = rand.nextInt(2 + 1);
        System.out.println("Random numbers: x: " + rand_x + " y: " + rand_y);

        if (!set_field_cpu(buttons[rand_x][rand_y])) {
            cpu_move();
        } else {
            set_field_cpu(buttons[rand_x][rand_y]);
        }
        check = check_winner();
        if (check) {
            if (g_player == 2) {
                tools.showMsgBox("Computer won!", Tools.MsgState.ACCEPT_AND_EXit);

            } else {
                tools.showMsgBox("You won!", Tools.MsgState.ACCEPT_AND_EXit);

            }
        }
        if (!check && move_count == 9) {
            tools.showMsgBox("Tied!", Tools.MsgState.ACCEPT);
        }

        player_view.setText("Current player: Player 1");

    }

    @Override
    public void onBackPressed() {
        String query = "delete from game where id = '"+Tools.game+"';";
        new BackgroundTask("addData", getParent()).execute("update users set logged = 0 where user = '"+Tools.logged_user+"';");
        new BackgroundTask("addData", this).execute(query);
        tools.showMsgBox("Exit app?", Tools.MsgState.EXIT);
    }


    public void refresh() throws ExecutionException, InterruptedException {

        for (int i = 0; i < 3 ; i++){
            for(int j = 0; j < 3; j++){
                String query = "select row, col"+j+ " from field where row = "+ i+";";

                String res = new BackgroundTask("getField", this).execute(query).get();


                int col = Integer.parseInt(tools.parse("column", res));

                if (col == 1){
                    buttons[i][j].setBackgroundColor(Color.RED);
                }
                else if (col == 2){
                    buttons[i][j].setBackgroundColor(Color.YELLOW);
                }


            }
        }

    }



}

