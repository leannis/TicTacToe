package com.example.lea.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class LogIn extends AppCompatActivity {

    // TODO: maybe login via google account
    // TODO: database optimization

    public String user, password = null;
    public Button b_signin, b_login;
    public EditText et_user, et_pw;
    Tools tools = new Tools(this);
    PasswordManager pwm;

    public static boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        pwm = new PasswordManager(this);

        b_signin = (Button) findViewById(R.id.b_signup);
        b_login = (Button) findViewById(R.id.b_login);
        et_user = (EditText) findViewById(R.id.t_username);
        et_pw = (EditText) findViewById(R.id.t_passwd);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            connected = true;
            tools.showToast("Connected to network");
        } else {
           connected = false;
           tools.showToast("Disconnected from network");
            startActivity(new Intent(LogIn.this, StartScreen.class));
        }

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(LogIn.this, SignUp.class));
            finish();
            }
        });

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwm.sentUserRequest();
                user = et_user.getText().toString();
                password = et_pw.getText().toString();

                System.out.println(user + " " + password);

                if (user.isEmpty()) {
                    tools.showMsgBox("Please enter an username", Tools.MsgState.ACCEPT);
                } else if (password.isEmpty()) {
                    tools.showMsgBox("Please enter a password", Tools.MsgState.ACCEPT);
                } else {
                    int x = pwm.checkUser(user, password);

                    if (x == 1) {
                        startActivity(new Intent(LogIn.this, StartScreen.class));
                        finish();
                    } else if (x == 2) {
                        tools.showMsgBox("Wrong password", Tools.MsgState.ACCEPT);
                    } else if (x == 3) {
                        tools.msg_registry = true;
                        tools.showMsgBox("User doesn't exists", Tools.MsgState.REGISTER);
                    }
                }

                et_pw.setText(null);
                et_user.setText(null);
                }

        });
    }

    @Override
    public void onBackPressed() {
        tools.showMsgBox("Exit app?", Tools.MsgState.EXIT);
    }
}
