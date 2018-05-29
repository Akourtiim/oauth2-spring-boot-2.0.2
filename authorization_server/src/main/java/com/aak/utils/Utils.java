package com.aak.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ahmed on 29.5.18.
 */
public class Utils {

    public static String passwordEncoder(String password){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        return encodedPassword;
    }
}
