package ru.ifmo.lab.exceptions;

public class InvalidValueException extends  RuntimeException {
    public InvalidValueException(String message) {
        super(message);
    }
}
