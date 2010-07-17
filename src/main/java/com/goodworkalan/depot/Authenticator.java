package com.goodworkalan.depot;

public class Authenticator {
    public String passwordOf(String name) {
        if (name.equals("alan")) {
            return "password";
        }
        return "";
    }

    public String validCredentials(String name, String password) {
        if (name.equals("alan") && password.equals("password")) {
            return "alan";
        }
        return null;
    }
}
