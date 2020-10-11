package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.CommandsManager;
import ru.ifmo.lab.managers.ConsoleManager;

import java.util.List;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(){
        cmdName = "help";
        description = "выводит список команд и их описание";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        List<AbstractCommand> aCommands = CommandsManager.getInstance().getAllCommands();
        for (AbstractCommand cmd: aCommands){
            consoleManager.writeln(cmd.getCmdName() + " - " + cmd.getDescription() );
        }

        return true;
    }
}
