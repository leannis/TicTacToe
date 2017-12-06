package com.example.lea.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class StartScreen extends AppCompatActivity {

    public DBAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_screen);

        db = new DBAccess(this, "ttt_db");

        System.out.println("test");

        Button b_single = (Button) findViewById(R.id.b_single);
        Button b_multi = (Button) findViewById(R.id.b_multi);
        Button b_multi_web = (Button) findViewById(R.id.b_multi_web);

        b_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, LogIn.class));
            }
        });

        b_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, LogIn.class));
            }
        });

        b_multi_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, LogIn.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StartScreen.this, LogIn.class));
    }
}
