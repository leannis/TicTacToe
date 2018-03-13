package com.example.lea.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.concurrent.ExecutionException;


public class StartScreen extends AppCompatActivity {

    Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_screen);

        tools = new Tools(this);

        Button b_single = (Button) findViewById(R.id.b_single);
        Button b_multi = (Button) findViewById(R.id.b_multi);
        Button b_multi_web = (Button) findViewById(R.id.b_multi_web);
        Button b_logout = (Button) findViewById(R.id.b_logout);





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

                try {
                    joinGame();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
        if(LogIn.connected){
            startActivity(new Intent(StartScreen.this, LogIn.class));
        }
        else if (!LogIn.connected){
            tools.showMsgBox("Do you really want to exit?", Tools.MsgState.EXIT);
        }
    }

    public void joinGame() throws ExecutionException, InterruptedException {

      String res = new BackgroundTask("getGame", this).execute("select id from game where player1 is not null").get();

      String id = "";



      if(!tools.checkResult(res)){
          //Satz in Tabelle einfügen

          String query = "insert into game (player1, player2, flag) values('"+Tools.logged_user+"','', '');";
          new BackgroundTask("addData", this).execute(query);
          Tools.flag = 1;

          id = new BackgroundTask("getGame", this).execute("select id from game where player1 = '"+Tools.logged_user+"';").get();
          Tools.game =  Integer.parseInt(tools.parse("id", id));



      }
      else if (tools.checkResult(res)){
            id = new BackgroundTask("getGame", this).execute("select id from game where player1 is not null;").get();
            String parsedid = tools.parse("id", id);
            String query = "update game set player2 = '"+Tools.logged_user+"' where id = "+parsedid+";";
          System.out.println(query);
            new BackgroundTask("addData", this).execute(query);
            System.out.println("Updated");
            Tools.flag = 2;
            Tools.game = Integer.parseInt((tools.parse("id", id)));
      }

    }
}
