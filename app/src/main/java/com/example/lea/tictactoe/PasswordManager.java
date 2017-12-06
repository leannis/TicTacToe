package com.example.lea.tictactoe;

import java.util.HashMap;

public class PasswordManager {

    public static HashMap<String, String> users = new HashMap<String, String>();

    public PasswordManager() {





    }

    public static void addUser(String user, String pw) {
        users.put(user, pw);
    }

    public static int checkUser(String user, String pw) {
        // 1 = OK
        // 2 = PW falsch
        // 3 = User gibts nicht
        String temp = users.get(user);

        if (temp != null) {
            if (temp.equals(pw)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }

    }

    public static int checkUserRegister(String user) {


        if (users.containsKey(user)) {
            return -1;
        } else {
            return 1;
        }
    }

}
