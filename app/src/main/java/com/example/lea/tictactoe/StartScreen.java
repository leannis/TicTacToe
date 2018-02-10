package com.example.lea.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class StartScreen extends AppCompatActivity {

    Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_screen);

        tools = new Tools(this);
        System.out.println("test");

        Button b_single = (Button) findViewById(R.id.b_single);
        Button b_multi = (Button) findViewById(R.id.b_multi);
        Button b_multi_web = (Button) findViewById(R.id.b_multi_web);
        Button b_logout = (Button) findViewById(R.id.b_logout);

        if(LogIn.connected == false){
            b_multi_web.setEnabled(false);
        }

        b_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, TicTacToe.class));
                TicTacToe.gamemode = 1;
            }
        });

        b_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, TicTacToe.class));
                TicTacToe.gamemode = 2;
            }
        });

        b_multi_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, TicTacToe.class));
                TicTacToe.gamemode = 3;
            }
        });

        b_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.showMsgBox("Logout?", Tools.MsgState.LOGOUT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(LogIn.connected == true){
            startActivity(new Intent(StartScreen.this, LogIn.class));
        }
        else if (LogIn.connected == false){
            tools.showMsgBox("Do you really want to exit?", Tools.MsgState.EXIT);
        }
    }
}
