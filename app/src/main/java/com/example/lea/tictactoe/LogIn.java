package com.example.lea.tictactoe;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class LogIn extends AppCompatActivity {
    public Button b_signin = (Button) findViewById(R.id.b_signup);
    public Button b_login = (Button) findViewById(R.id.b_login);

    public EditText et_user = (EditText) findViewById(R.id.t_username);
    public EditText et_pw = (EditText) findViewById(R.id.t_username);

    public String user, password = null;

    public PasswordManager pwm = new PasswordManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        pwm.addUser("test", "test123");

        b_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user = et_user.getText().toString();
                password = et_pw.getText().toString();

                if (user.isEmpty()){
                    //Msgbox User füllen
                }
                else if (password.isEmpty()){
                    //Msgbox PW füllen
                }
                if(!user.isEmpty() && !password.isEmpty()){
                   int x = pwm.checkUser(user, password);
                    if (x == 1){
                        //Gehe ins Game
                    }
                    else if(x == 2){
                        //messagebox PW Falsch
                    }
                    else if(x == 3){
                        //MEssagebox User gibts nicht
                        // --> Registrierung?!
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
