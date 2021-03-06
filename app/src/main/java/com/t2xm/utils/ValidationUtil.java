package com.t2xm.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern username_pattern = Pattern.compile("^[A-Za-z][A-Za-z0-9._\\-]{6,12}$");
    private static final Pattern password_pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$");
    private static final Pattern email_pattern = Pattern.compile("^[a-z0-9][a-z0-9\\-\\._]+@[a-z0-9]{2,}.[a-z]{2,3}.([a-z]{2,3})?$", Pattern.CASE_INSENSITIVE);

    public static boolean validateUsername(String username) {
        return username_pattern.matcher(username).find();
    }

    public static boolean validatePassword(String password) {
        return password_pattern.matcher(password).find();
    }

    public static boolean validateEmail(String email) {
        return email_pattern.matcher(email).find();
    }
}
