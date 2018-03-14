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

import java.util.concurrent.ExecutionException;


public class LogIn extends AppCompatActivity {

    public String user, password = null;
    public Button b_signin, b_login;
    public EditText et_user, et_pw;
    public static boolean connected;
    Tools tools = new Tools(this);
    PasswordManager pwm;
    Context con = this;

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

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
                user = et_user.getText().toString();
                password = et_pw.getText().toString();

                if (user.isEmpty()) {
                    tools.showMsgBox("Please enter an username", Tools.MsgState.ACCEPT);
                } else if (password.isEmpty()) {
                    tools.showMsgBox("Please enter a password", Tools.MsgState.ACCEPT);
                } else {
                    String res = "";
                    try {
                        res = new BackgroundTask("getUser", con).execute("select user, " +
                                "password from users where user='" + user + "' and password = '" +
                                password + "';").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (!tools.checkResult(res)) {
                        tools.showMsgBox("Username and/or password not correct!",
                                Tools.MsgState.ACCEPT);
                    } else {
                        String res_logged = "";
                        try {
                            res_logged = new BackgroundTask("getUser", con).
                                    execute("select user, password, logged from users where user='"
                                            + user + "' and password = '" + password + "';").get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        System.out.println("print res_logged: " + res_logged);
                        String debug = tools.parse("logged", res_logged);
                        System.out.println("DEBUG nach parse: " + debug);

                        int logged = Integer.parseInt(debug);
                        if (logged == 1) {
                            tools.showToast("You're already logged in!");
                            startActivity(new Intent(LogIn.this, StartScreen.class));
                        } else {
                            String query = "update users set logged = 1 where user='" + user + "' " +
                                    "and password = '" + password + "';";
                            System.out.println("SSSS: " + query);
                            new BackgroundTask("addData", con).execute(query);
                            Tools.logged_user = user;
                            startActivity(new Intent(LogIn.this, StartScreen.class));
                            finish();
                        }
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
