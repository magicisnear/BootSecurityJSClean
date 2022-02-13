package com.SpringBootCrud.JavaMentor.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class NoUserWithSuchLogin extends UsernameNotFoundException {

    public NoUserWithSuchLogin(String msg) {
        super(msg);
    }
}
