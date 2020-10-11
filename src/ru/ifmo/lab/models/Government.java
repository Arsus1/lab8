package ru.ifmo.lab.models;

import ru.ifmo.lab.exceptions.InvalidValueException;

import java.io.Serializable;

public enum Government implements Serializable {
    MATRIARCHY(0),
    MERITOCRACY(1),
    NOOCRACY(2),
    PLUTOCRACY(3);

    private int id;

    Government(int id){
        this.id = id;
    }

    public static Government byOrdinal(int s) {
        for (Government value : Government.values()) {
            if (value.getId() == s) {
                return value;
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public static Government byName(String s) {
        for (Government value : Government.values()) {
            if (value.equals(s.toUpperCase())) {
                return value;
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public int getId() {
        return id;
    }
}