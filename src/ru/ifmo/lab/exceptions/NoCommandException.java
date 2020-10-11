package ru.ifmo.lab.exceptions;

public class NoCommandException extends RuntimeException {
    public NoCommandException(String msg){
        super(msg);
    }
}
