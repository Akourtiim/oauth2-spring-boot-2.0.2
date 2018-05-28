package com.aak;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ahmed on 27.5.18.
 */
public class Utils {

    public static void main(String[] args){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String user ="user";
        System.out.println(passwordEncoder.encode(user));
    }
}
