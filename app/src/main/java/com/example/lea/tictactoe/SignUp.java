package com.example.lea.tictactoe;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SignUp extends Activity {

    public static final String EXTRA_MESSAGE = "com.example.TicTacToe.MESSAGE";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button b_cancel = (Button) findViewById(R.id.b_cancel);

        b_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });
    }
}
