package ru.ifmo.lab.network.packets;

import java.io.Serializable;

public class CommandExecutionPacket implements Serializable {

    private String messageType;
    private final Object message;
    private Boolean isSuccessful;
    public CommandExecutionPacket(String messageType, Object message, Boolean isSuccessful){
        this.messageType = messageType;
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    public Object getMessage() {
        return message;
    }

    public String getMessageType() {
        return messageType;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }
}
