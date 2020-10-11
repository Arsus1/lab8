package ru.ifmo.lab.exceptions;

public class AuthException extends RuntimeException {
    public AuthException(String msg){
        super(msg);
    }
}
