package com.example.lea.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class LogIn extends AppCompatActivity {
// hallo
// basst
    public String user, password = null;
    public Button b_signin, b_login;
    public EditText et_user, et_pw;
    Tools tools = new Tools(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        b_signin = (Button) findViewById(R.id.b_signup);
        b_login = (Button) findViewById(R.id.b_login);
        et_user = (EditText) findViewById(R.id.t_username);
        et_pw = (EditText) findViewById(R.id.t_passwd);

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        b_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user = et_user.getText().toString();
                password = et_pw.getText().toString();

                System.out.println(user + " " + password);

                if (user.isEmpty()) {

                    tools.showMsgBox("Please enter a Username");
                } else if (password.isEmpty()) {
                    tools.showMsgBox("Please enter a Password");
                } else//(!(user.isEmpty()) && !(password.isEmpty())){
                {

                    int x = PasswordManager.checkUser(user, password);
                    if (x == 1) {
                        tools.showMsgBox("Login erfolgreich");
                    } else if (x == 2) {
                        tools.showMsgBox("Passwort falsch");
                    } else if (x == 3) {
                        tools.msg_registry = true;
                        tools.showMsgBox("User existiert nicht");

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LogIn.this, StartScreen.class));

    }
}
