package ru.ifmo.lab.network.packets;

import ru.ifmo.lab.commands.AbstractCommand;
import ru.ifmo.lab.database.Credentials;

import java.io.Serializable;

public class CommandPacket implements Serializable {

    private AbstractCommand command;
    private Credentials credentials;

    public CommandPacket(AbstractCommand command, Credentials credentials){
        this.command = command;
        this.credentials = credentials;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
