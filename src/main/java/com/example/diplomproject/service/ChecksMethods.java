package com.example.diplomproject.service;

import com.example.diplomproject.exception.IncorrectArgumentException;

public class ChecksMethods {
    public static String checkForLogin(String str) {
        if (str == null || str.isBlank()) {
            throw new IncorrectArgumentException();
        }
        return str.trim();
    }
    public static <T> T checkForChangeParameter(T obj) {
        if (obj == null) {
            throw new IncorrectArgumentException();
        }
        return obj;
    }
    public static String checkForEmail(String email) {
        if (!checkForLogin(email).contains("@")) {
            throw new IncorrectArgumentException();
        }
        return email.trim();
    }

    public static String checkValidatePassword(String password) {
        if (checkForLogin(password).length() < 8) {
            throw new IncorrectArgumentException();
        }
        return password.trim();
    }
}
