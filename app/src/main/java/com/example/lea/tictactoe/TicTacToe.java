package com.example.lea.tictactoe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TicTacToe extends AppCompatActivity {

    public int g_player = 1;
    Button b_1_1, b_1_2, b_1_3, b_2_1, b_2_2, b_2_3, b_3_1, b_3_2, b_3_3;
    Button buttons[][] = new Button[3][3];
    ColorDrawable colors[][] = new ColorDrawable[3][3];

    TextView player_view;

    ColorDrawable buttonColor;
    int i , j;

    static int gamemode;
    //
    // 1:   single
    // 2:   multi
    // 3:   multi web


    int move_count = 0;
    Tools tools;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        tools = new Tools(this);


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
        player_view.setText("Aktueller Spieler: Spieler " + g_player);

        //init board
        for (i = 0; i < 3; i++) {
            for( j = 0; j < 3; j++) {
                buttons[i][j].setBackgroundColor(Color.BLACK);
                final Button temp = buttons[i][j];

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            make_move(temp);

                    }
                });
            }
        }


    }


    public boolean set_field(Button b) {

        buttonColor = (ColorDrawable) b.getBackground();

        if (buttonColor.getColor() == -65536 || buttonColor.getColor() == -256) {
            return false;

        } else {
            if (g_player == 1) {
                b.setBackgroundColor(Color.RED);
                move_count++;
                //In die DB schreiben

                player_view.setText("Aktueller Spieler: Spieler " + g_player);
                return true;
            } else if (g_player == 2) {
                b.setBackgroundColor(Color.YELLOW);
                //In die DB Schreiben
                move_count++;

                player_view.setText("Aktueller Spieler: Spieler " + g_player);
                return true;
            } else {
                return false;
            }

        }


    }


    public void make_move(Button b){

        if(gamemode == 2) {

            if (set_field(b) == false) {
                tools.showMsgBox("Feld schon belegt", Tools.MsgState.ACCEPT);
            } else {
                set_field(b);

            }
            boolean c = check_winner();
            if (c == true) {
                tools.showMsgBox("Spieler " + g_player + " hat gewonnen!", Tools.MsgState.ACCEPT);
            }
            if (c == false && move_count == 9) {
                tools.showMsgBox("Unentschieden!", Tools.MsgState.ACCEPT);
            }

            if (g_player == 1) {
                g_player = 2;
            } else if (g_player == 2) {
                g_player = 1;
            }
        }


    }

    public boolean check_winner() {
        //rot  -65536 //gelb -256 // schwarz -16777216
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j++){
                colors[i][j] = (ColorDrawable) buttons[i][j].getBackground();
            }
        }
        return(checkRows() || checkCols() || checkDia());
    }

    private boolean checkRows(){
        for(int i = 0; i < 3; i++){
            if(checkRowCol(colors[i][0].getColor(), colors[i][1].getColor(), colors[i][2].getColor() ) == true){
                return true;
            }
        }
        return false;
    }
    private boolean checkCols(){
        for(int i = 0; i < 3; i++){
            if(checkRowCol(colors[0][i].getColor(), colors[1][i].getColor(), colors[2][i].getColor() ) == true){
                return true;
            }
        }
        return false;
    }
    private boolean checkDia(){
        return ((checkRowCol(colors[0][0].getColor(), colors[1][1].getColor(), colors[2][2].getColor()) == true) || (checkRowCol(colors[0][2].getColor(), colors[1][1].getColor(), colors[2][0].getColor()) == true));
    }


    private boolean checkRowCol(int i1, int i2, int i3){
        return ((i1 != -16777216) && (i1 == i2) && (i2==i3));
    }
}
