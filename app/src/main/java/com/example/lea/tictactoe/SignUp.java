package com.example.lea.tictactoe;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignUp extends Activity {

    public static final String EXTRA_MESSAGE = "com.example.TicTacToe.MESSAGE";

    Tools tools = new Tools(this);
    Button b_cancel, b_signup;
    EditText et_user, et_pw1, et_pw2;

    String user, pw1, pw2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        b_cancel = (Button) findViewById(R.id.b_cancel);
        b_signup = (Button) findViewById(R.id.b_signup);
        et_user = (EditText) findViewById(R.id.t_reg_username);
        et_pw1 = (EditText) findViewById(R.id.t_reg_passwd1);
        et_pw2 = (EditText) findViewById(R.id.t_reg_passwd2);

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = et_user.getText().toString();
                pw1 = et_pw1.getText().toString();
                pw2 = et_pw2.getText().toString();


                if (user.isEmpty()) {
                    tools.showMsgBox("Please enter a Username");
                } else if (pw1.isEmpty() || pw2.isEmpty()) {
                    tools.showMsgBox("Please enter/confirm  Password");
                } else if (!(user.isEmpty()) && !(pw1.isEmpty()) && !(pw2.isEmpty())) {
                    System.out.println("debug");
                    int ret = PasswordManager.checkUserRegister(user);
                    System.out.println(ret);
                    if (ret == -1) {
                        tools.showMsgBox("This Username has already been taken.");
                    } else if (ret == 1) {

                        if (!(pw1.equals(pw2))) {
                            tools.showMsgBox("Passwords don't match.");
                        } else {
                            PasswordManager.addUser(user, pw1);
                            tools.showMsgBox("User has succesfully been signed up.");
                        }

                    }

                }

            }
        });
    }

   /* @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUp.this, LogIn.class));
    }*/
}
